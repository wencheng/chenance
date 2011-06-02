/*
 * Copyright 2008 Wencheng FANG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sh.fang.chenance;

import static cn.sh.fang.chenance.i18n.UIMessageBundle.setText;
import static cn.sh.fang.chenance.util.SWTUtil.setFormLayoutData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.ILineSeries;
import org.swtchart.ISeries;
import org.swtchart.ISeriesLabel;
import org.swtchart.ISeriesSet;
import org.swtchart.Range;
import org.swtchart.ISeries.SeriesType;

import cn.sh.fang.chenance.data.dao.TransactionService;
import cn.sh.fang.chenance.data.entity.Account;
import cn.sh.fang.chenance.util.SimpleDate;
import cn.sh.fang.chenance.util.swt.DateChooserCombo;

// 編集欄
public class CategoryStatsForm {
	
	private Logger LOG = Logger.getLogger(CategoryStatsForm.class);

	public WritableValue id;
	private DateChooserCombo from;
	private DateChooserCombo to;
	private Chart chart;
	private Composite parent;
	
	public CategoryStatsForm(Composite parent, int style) {
		this.parent = parent;
		createControl(parent);
	}

	private void createControl(final Composite parent) {
		id = WritableValue.withValueType(Integer.class);
		from = new DateChooserCombo(parent, SWT.NONE);
		from.setFooterVisible(true);
		Label lblTo = new Label(parent, SWT.NONE);
		setText(lblTo, "~");
		to = new DateChooserCombo(parent, SWT.NONE);
		to.setFooterVisible(true);
	
		Calendar cal = Calendar.getInstance();
		Date t1 = cal.getTime();
		to.setValue(t1);
		cal.add(Calendar.MONTH, -1);
		cal.add(Calendar.DATE, 1);
		t1 = cal.getTime();
		from.setValue(t1);
		
		// レイアウト
		setFormLayoutData(from, 0, 0, 0, 10);//.width = 130;
		setFormLayoutData(lblTo, from, 0, SWT.TOP, from, 20, SWT.NONE);
		setFormLayoutData(to, from, 0, SWT.TOP, lblTo, 20, SWT.NONE);//.width = 130;

		createChart();
		
		// イベント
		id.addValueChangeListener(new IValueChangeListener() {
			public void handleValueChange(ValueChangeEvent e) {
				refreshChart();
			}
		});
		SelectionListener s = new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				refreshChart();
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		};
		from.addSelectionListener(s);
		to.addSelectionListener(s);
	}
	
	private void createChart() {
		if ( chart != null ) {
			chart.dispose();
		}
		
		// create a chart
		chart = new Chart(parent, SWT.NONE);
		// set titles
		chart.getTitle().setVisible(false);
//		setText(chart.getAxisSet().getXAxis(0).getTitle(), "Date");
		chart.getAxisSet().getXAxis(0).getTitle().setVisible(false);
		setText(chart.getAxisSet().getYAxis(0).getTitle(), "Amount");
		chart.getLegend().setPosition(SWT.TOP);
		chart.getAxisSet().getXAxis(0).getTick().setFont(
				new Font(Display.getDefault(), "Tahoma", 8, SWT.NORMAL) );

		FormData fd = setFormLayoutData(chart, from, 0, SWT.NONE, from, 0, SWT.LEFT);
		fd.height = 280;
		fd.width = 700;
	}
	
	private void refreshChart() {
		ISeriesSet ss = chart.getSeriesSet();
		for ( ISeries s : ss.getSeries() ) {
			ss.deleteSeries(s.getId());
		}

		if (id.doGetValue() == null) {
			return;
		}

		// DateChooser gives us a time of 00:00:00
		SimpleDate from = SimpleDate.UTC(this.from.getValue());
		SimpleDate to = SimpleDate.UTC(this.to.getValue());
		int diff = to.dayDiff(from);
		LOG.debug("refresh chart category: " + id.doGetValue() + " " + from + " " + to);
		LOG.debug("diff: " + diff);

		TransactionService cs = new TransactionService();
		SimpleDateFormat dateFormat = null;
		List<Date> xseries = new ArrayList<Date>();
		HashMap<Account, List<Object[]>>  yl = null;
		HashMap<Account, double[]> ym = new HashMap<Account, double[]>();
		if ( diff <= 31 ) {
			// daily
			dateFormat = new SimpleDateFormat("MM/dd");

			for ( SimpleDate l = from; l.compareTo(to) <= 0; l = l.nextDay() ) {
				xseries.add(l);
			}

			yl = cs.getDailyAmount((Integer)id.doGetValue(), from, to);

			// Map<Account, List<{String, Long}>> => Map<Account, List<{Date, Long}>>
			for ( Account a : yl.keySet() ) {
				List<Object[]> l = yl.get(a);
				for ( int i = 0; i < l.size(); i++ ) {
					LOG.debug((String)l.get(i)[0] + " " + SimpleDate.UTC((String)l.get(i)[0]));
					l.get(i)[0] = SimpleDate.UTC((String)l.get(i)[0]);
				}
			}
		} else if ( diff <= 186 ) {
			// weekly
			SimpleDate.resetDateRange(from, to, Calendar.WEEK_OF_YEAR, null);
			dateFormat = new SimpleDateFormat("MM/dd~");

			for ( SimpleDate l = from; l.compareTo(to) <= 0; l = l.nextWeek() ) {
				xseries.add(l);
			}

			yl = cs.getWeeklyAmount((Integer)id.doGetValue(), from, to);

			// Map<Account, List<{YYYYWW, Long}>> => Map<Account, List<{Date, Long}>>
			for ( Account a : yl.keySet() ) {
				List<Object[]> l = yl.get(a);
				for ( int i = 0; i < l.size(); i++ ) {
					LOG.debug((String)l.get(i)[0] + " " + SimpleDate.yyyyww((String)l.get(i)[0]));
					l.get(i)[0] = SimpleDate.yyyyww((String)l.get(i)[0]);
				}
			}
		} else {//if ( diff <= 1100 ) {
			// monthly
			SimpleDate.resetDateRange(from, to, Calendar.MONTH, null);
			dateFormat = new SimpleDateFormat("yyyy/MM");

			for ( SimpleDate l = from; l.compareTo(to) <= 0; l = l.nextMonth() ) {
				xseries.add(l);
			}

			yl = cs.getMonthlyAmount((Integer)id.doGetValue(), from, to);

			// Map<Account, List<{YYYYMM, Long}>> => Map<Account, List<{Date, Long}>>
			for ( Account a : yl.keySet() ) {
				List<Object[]> l = yl.get(a);
				for ( int i = 0; i < l.size(); i++ ) {
					LOG.debug((String)l.get(i)[0] + " " + SimpleDate.yyyymm((String)l.get(i)[0]));
					l.get(i)[0] = SimpleDate.yyyymm((String)l.get(i)[0]);
				}
			}
			/*
		} else {
			// yearly
			SimpleDate.resetDateRange(from, to, Calendar.MONTH, null);
			dateFormat = new SimpleDateFormat("yyyy/MM");

			for ( SimpleDate l = from; l.compareTo(to) <= 0; l = l.nextYear() ) {
				xseries.add(l);
			}

			CategoryService cs = new CategoryService();
			yl = cs.getMonthlyAmount((Integer)id.doGetValue(), from, to);

			// Map<Account, List<{YYYYWW, Long}>> => Map<Account, List<{Date, Long}>>
			for ( Account a : yl.keySet() ) {
				List<Object[]> l = yl.get(a);
				for ( int i = 0; i < l.size(); i++ ) {
					LOG.debug((String)l.get(i)[0] + " " + SimpleDate.yyyymm((String)l.get(i)[0]));
					l.get(i)[0] = SimpleDate.yyyymm((String)l.get(i)[0]);
				}
			}
			*/
		}
		
		// find max for range
		double max = 1;
		// Map<Account, List<{Date, Long}>> => Map<Account, double[]>
		for ( Account a : yl.keySet() ) {
			List<Object[]> l = yl.get(a);
			double[] d = new double[xseries.size()];
//			for ( int i = 0; i < xseries.size(); i++ ) {
//				d[i] = 0;
//				LOG.debug(xseries.get(i));
//			}

			for ( int i = 0; i < l.size(); i++ ) {
				int idx = xseries.indexOf((Date)l.get(i)[0]);
				if ( idx >= 0 ) {
					d[idx] = (idx==0?0:d[idx-1])
						+ ((Number) l.get(i)[1]).doubleValue();
					LOG.debug(d[idx]);
					if ( d[idx] > max ) {
						max = d[idx];
					}
				} else {
					LOG.warn("cannot found " + (Date)l.get(i)[0] + " in x axis");
				}
			}
			
			// make a increasing graph
			for ( int i = 1; i < d.length; i++ ) {
				d[i] += d[i-1];
			}

			ym.put(a, d);
		}
		
		// create line series
		int color = 0;
		for ( Account a : ym.keySet() ) {
			ILineSeries lineSeries = (ILineSeries)ss
				.createSeries(SeriesType.LINE, a.getName());
			double[] y = ym.get(a);
			lineSeries.setYSeries(y);
			lineSeries.setLineWidth(3);

			// adjust the y-axis range
			chart.getAxisSet().getYAxis(0).setRange(
					new Range(0, max * 1.1));

			// no 0, no 1=SWT.COLOR_WHITE
			int[] COLORS = {11, 3, 5, 7, 9, 2, 13, 15, 2, 4, 6, 8, 10, 12, 14, 16};
			lineSeries.setLineColor(Display.getDefault().getSystemColor(COLORS[color++]));
			if ( color == COLORS.length ) {
				color = 0;
			}

			// show non-zero labels
			ISeriesLabel seriesLabel = lineSeries.getLabel();
			seriesLabel.setVisible(true);
			String[] formats = new String[y.length];
			for ( int i = 0; i < y.length; i++ ) {
				if ( y[i] == 0 || (i > 0 && y[i] == y[i-1]) ) {
					formats[i] = "";
				} else {
					formats[i] = "￥#,###";
				}
			}
			seriesLabel.setFormats(formats);

			IAxis xAxis = chart.getAxisSet().getXAxis(lineSeries.getXAxisId());
			if (diff <= 31) {
				lineSeries.setXDateSeries(xseries.toArray(new Date[]{}));
				xAxis.getTick().setFormat(dateFormat);
				xAxis.setRange(new Range(
						from.getTime()-SimpleDate.ONE_DAY_MILI+1,
						to.getTime()+SimpleDate.ONE_DAY_MILI-1));
			} else {
				String[] xs = new String[xseries.size()];
				for (int i = 0; i < xs.length; i++ ) {
					xs[i] = dateFormat.format(xseries.get(i));
				}
				xAxis.setCategorySeries(xs);
				xAxis.adjustRange();
				xAxis.enableCategory(true);
			}
		}

		// redraw
		chart.getPlotArea().update();
		chart.redraw(); // work for win
	}

}
