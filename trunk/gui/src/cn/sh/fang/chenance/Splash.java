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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Splash implements MouseListener, MouseMoveListener {

	private Display display;

	private Shell shell;

	private Boolean isRunning = false;

	private boolean isMoving;

	private Point point;

	public Splash(Display display) {
		this.display = display;
	}
	
	private void init() {
		shell = new Shell( display, SWT.NO_TRIM );
		final Image image = new Image( null, 
				this.getClass().getClassLoader().getResourceAsStream(
//						"cn/sh/fang/chenance/splash.gif" ));
						"cn/sh/fang/chenance/icons/1921_morgan_dollar_chapman_obv.jpg" ));
						

		//define a region 
		Region region = new Region();
		Rectangle pixel = new Rectangle(0, 0, 1, 1);
		Rectangle bounds = image.getBounds();
		ImageData id = image.getImageData();
//		int trans = id.transparentPixel;
		System.out.println(id.getPixel(0, 0));
		for (int y = 0; y < bounds.height; y++) {
			for (int x = 0; x < bounds.width; x++) {
				// mac why? 0xffffff = 16777215
				if ( id.getPixel(x, y) != 16777215 ) {
				// win
//				if ( id.getPixel(x, y) != trans ) {
					pixel.x = x;
					pixel.y = y;
					region.add(pixel);
				}
			}
		}
		
		//define the shape of the shell using setRegion
		shell.setRegion(region);
		shell.setSize(bounds.width, bounds.height);
		Rectangle size = region.getBounds();
		Rectangle b = display.getPrimaryMonitor().getBounds();
		System.out.println(b);
		System.out.println(size);
		shell.setLocation( b.x+(b.width-size.width)/2, b.y+(b.height-size.height)/2 );
		
		shell.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(image, 0, 0);
			}
		});

		shell.addMouseListener(this);
		shell.addMouseMoveListener(this);
	}
	
	public void close() {
		synchronized ( isRunning ) {
			if ( isRunning ) {
				isRunning = false;
				display.wake();
			}
		}
	}
	
	public void run(Runnable runnable) {
		isRunning = true;
		
		init();
		
		if ( ! isRunning ) {
			return;
		}
		
		shell.setVisible(true);

//		runnable.run();
		display.asyncExec( runnable );
		while ( isRunning ) {
			if ( !display.readAndDispatch() ) {
				display.sleep();
			}
		}
		
		shell.setVisible( false );

	}

	public static void main(String[] args) {
		Display display = new Display();

		final Splash s = new Splash( display );
		s.run( new Runnable() {
			public void run() {
				System.out.println("run()");
				try {
					Thread.sleep( 5000 );
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("run() end");
				
				s.close();
			}
		});

		display.dispose();
	}

	public void mouseDoubleClick(MouseEvent arg0) {
		// do nothing
	}

	public void mouseDown(MouseEvent e) {
		isMoving = true;
		point = new Point( e.x, e.y );
	}

	public void mouseUp(MouseEvent e) {
		isMoving = false;
	}

	public void mouseMove(MouseEvent e) {
		if ( isMoving ) {
			Point p = shell.getLocation();
			shell.setLocation( p.x+e.x-point.x, p.y+e.y-point.y );
		}
	}

}