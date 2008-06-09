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

	private Thread t;

	private Boolean isRunning = false;

	private boolean isMoving;

	private Point point;

	public Splash(Display display) {
		this.display = display;
	}
	
	private void init() {
		shell = new Shell( display, SWT.NO_TRIM );
		final Image image = new Image( null, 
				this.getClass().getClassLoader().getResourceAsStream( "cn/sh/fang/chenance/splash.gif" ));

		//define a region 
		Region region = new Region();
		Rectangle pixel = new Rectangle(0, 0, 1, 1);
		Rectangle bounds = image.getBounds();
		ImageData id = image.getImageData();
		for (int y = 0; y < bounds.height; y++) {
			for (int x = 0; x < bounds.width; x++) {
//				System.out.println(id.getPixel(x, y));
				// mac why? 0xffffff = 16777215
//				if ( id.getPixel(x, y) != 16777215 ) {
				// win
				if ( id.getPixel(x, y) != 255 ) {
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
		Rectangle b = display.getBounds();
		System.out.println(b);
		System.out.println(size);
		shell.setLocation( (b.width+b.x-size.width)/2, (b.height+b.y-size.height)/2 );
		
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

//		t = new Thread( runnable );
//		t.start();
//		while ( isRunning && t.isAlive() ) {
//			if ( !display.readAndDispatch() ) {
//				display.sleep();
//			}
//		}
		
		runnable.run();
		
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