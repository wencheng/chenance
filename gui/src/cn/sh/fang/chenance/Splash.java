package cn.sh.fang.chenance;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Splash {

	private Display display;

	private Shell shell;

	private Thread t;

	private Boolean isRunning = false;

	public Splash(Display display) {
		this.display = display;
	}
	
	private void init() {
		shell = new Shell( display, SWT.NO_TRIM );
		final Image image = new Image( null, "src/cn/sh/fang/chenance/splash.gif" );

		//define a region 
		Region region = new Region();
		Rectangle pixel = new Rectangle(0, 0, 1, 1);
		Rectangle bounds = image.getBounds();
		ImageData id = image.getImageData();
		for (int y = 0; y < bounds.height; y++) {
			for (int x = 0; x < bounds.width; x++) {
				if ( id.getPixel(x, y) != 16777215 ) {
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

}