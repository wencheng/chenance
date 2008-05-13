package cn.sh.fang.chenance.provider;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import cn.sh.fang.chenance.MainWindow;
import cn.sh.fang.chenance.data.entity.Category;
import cn.sh.fang.chenance.util.swt.CCombo;

/**
 * <p>
 * Based on org.eclipse.jface.viewers.ComboBoxCellEditor
 * </p>
 * 
 * A cell editor that presents a list of items in a combo box. The cell editor's
 * value is the zero-based index of the selected item.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 */
public class CategoryComboCellEditor extends CellEditor {

	private List<Category> items;

	int selection;

	CCombo comboBox;

	static Image grey_img = ImageDescriptor.createFromFile(
			MainWindow.class, 
			"icons/grey.png"
			).createImage();
	static Image plus_img = ImageDescriptor.createFromFile(
			MainWindow.class, 
			"icons/plus.gif"
			).createImage();
	static Image lvl1_img = ImageDescriptor.createFromFile(
			MainWindow.class, 
			"icons/lvl1.gif"
			).createImage();
	static Image lvl2_img= ImageDescriptor.createFromFile(
			MainWindow.class, 
			"icons/lvl2.gif"
			).createImage();

	private static final int defaultStyle = SWT.READ_ONLY;

	/**
	 * Creates a new cell editor with no control and no st of choices.
	 * Initially, the cell editor has no cell validator.
	 *
	 * @since 2.1
	 * @see CellEditor#setStyle
	 * @see CellEditor#create
	 * @see ComboBoxCellEditor#setItems
	 * @see CellEditor#dispose
	 */
	public CategoryComboCellEditor(Table parent) {
		super(parent, defaultStyle);
	}

	public List<Category> getItems() {
		return this.items;
	}

	public void setItems(List<Category> list) {
		this.items = list;
		populateComboBoxItems();
	}

	/*
	 * (non-Javadoc) Method declared on CellEditor.
	 */
	protected Control createControl(Composite parent) {

		comboBox = new CCombo(parent, getStyle()|SWT.APPLICATION_MODAL);
		comboBox.setFont(parent.getFont());

		populateComboBoxItems();

		comboBox.addKeyListener(new KeyAdapter() {
			// hook key pressed - see PR 14201
			public void keyPressed(KeyEvent e) {
				keyReleaseOccured(e);
			}
		});

		comboBox.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent event) {
				applyEditorValueAndDeactivate();
			}

			public void widgetSelected(SelectionEvent event) {
				selection = comboBox.getSelectionIndex();
				applyEditorValueAndDeactivate();
			}
		});

		comboBox.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE
						|| e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
				}
			}
		});

		comboBox.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				CategoryComboCellEditor.this.focusLost();
			}
		});
		return comboBox;
	}

	@Override
	public void activate(ColumnViewerEditorActivationEvent activationEvent) {
		super.activate(activationEvent);
	}

	/**
	 * The <code>ComboBoxCellEditor</code> implementation of this
	 * <code>CellEditor</code> framework method returns the zero-based index
	 * of the current selection.
	 *
	 * @return the zero-based index of the current selection wrapped as an
	 *         <code>Integer</code>
	 */
	protected Object doGetValue() {
		return new Integer(selection);
	}

	/*
	 * (non-Javadoc) Method declared on CellEditor.
	 */
	protected void doSetFocus() {
		comboBox.setFocus();
	}

	/**
	 * The <code>ComboBoxCellEditor</code> implementation of this
	 * <code>CellEditor</code> framework method sets the minimum width of the
	 * cell. The minimum width is 10 characters if <code>comboBox</code> is
	 * not <code>null</code> or <code>disposed</code> else it is 60 pixels
	 * to make sure the arrow button and some text is visible. The list of
	 * CCombo will be wide enough to show its longest item.
	 */
	public LayoutData getLayoutData() {
		LayoutData layoutData = super.getLayoutData();
		if ((comboBox == null) || comboBox.isDisposed()) {
			layoutData.minimumWidth = 60;
			layoutData.grabHorizontal = true;
		} else {
			// make the comboBox 10 characters wide
			GC gc = new GC(comboBox);
			layoutData.minimumWidth = (gc.getFontMetrics()
					.getAverageCharWidth() * 10) + 10;
			layoutData.grabHorizontal = true;
			gc.dispose();
		}
		return layoutData;
	}

	/**
	 * The <code>ComboBoxCellEditor</code> implementation of this
	 * <code>CellEditor</code> framework method accepts a zero-based index of
	 * a selection.
	 *
	 * @param value
	 *            the zero-based index of the selection wrapped as an
	 *            <code>Integer</code>
	 */
	protected void doSetValue(Object value) {
		Assert.isTrue(comboBox != null && (value instanceof Integer));
		selection = ((Integer) value).intValue();
		comboBox.select(selection);
	}

	/**
	 * Updates the list of choices for the combo box for the current control.
	 */
	private void populateComboBoxItems() {
		if (comboBox != null && items != null) {
			comboBox.removeAll();
			
			FontData fd = comboBox.getFont().getFontData()[0];
			fd.setStyle(SWT.BOLD);
			Font boldFont =
				new Font(comboBox.getDisplay(), fd);
			Color grey = new Color(comboBox.getForeground().getDevice(), 0xC0, 0xC0, 0xC0);
			
			for ( int i = 0; i < items.size(); i++ ) {
				Category c = items.get(i);
				if (c.getCode() % 10000 != 0) {
					comboBox.add(c.getName(), lvl1_img);
				} else if (c.getCode() % 1000000 != 0) {
					comboBox.add(c.getName(), plus_img);
				} else {
					comboBox.add(c.getName(), grey_img);
					TableItem ti = comboBox.getItem(i);
					ti.setFont(boldFont);
					ti.setBackground(grey);
					System.out.println(ti.getBounds().height);
				}
			}

			setValueValid(true);
			selection = 0;
		}
	}

	/**
	 * Applies the currently selected value and deactivates the cell editor
	 */
	void applyEditorValueAndDeactivate() {
		// must set the selection before getting value
		selection = comboBox.getSelectionIndex();
		Object newValue = doGetValue();
		markDirty();
		boolean isValid = isCorrect(newValue);
		setValueValid(isValid);

		if (!isValid) {
			// Only format if the 'index' is valid
			if (items.size() > 0 && selection >= 0 && selection < items.size()) {
				// try to insert the current value into the error message.
				setErrorMessage(MessageFormat.format(getErrorMessage(),
						new Object[] { items }));
			} else {
				// Since we don't have a valid index, assume we're using an
				// 'edit'
				// combo so format using its text value
				setErrorMessage(MessageFormat.format(getErrorMessage(),
						new Object[] { comboBox.getText() }));
			}
		}

		fireApplyEditorValue();
		deactivate();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.CellEditor#focusLost()
	 */
	protected void focusLost() {
		if (isActivated()) {
			applyEditorValueAndDeactivate();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.CellEditor#keyReleaseOccured(org.eclipse.swt.events.KeyEvent)
	 */
	protected void keyReleaseOccured(KeyEvent keyEvent) {
		if (keyEvent.character == '\u001b') { // Escape character
			fireCancelEditor();
		} else if (keyEvent.character == '\t') { // tab key
			applyEditorValueAndDeactivate();
		}
	}
}