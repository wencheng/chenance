package cn.sh.fang.chenance;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Splash implements Runnable {

	private Shell shell;
	
	private Region region;

	// http://dev.eclipse.org/viewcvs/index.cgi/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet180.java?view=markup&content-type=text%2Fvnd.viewcvs-markup&revision=HEAD
	// http://www.ibm.com/developerworks/java/library/j-2dswt/

	public Splash(Display display, int style) {
		//Shell must be created with style SWT.NO_TRIM
		shell = new Shell( display, style | SWT.NO_TRIM );
		run();
	}
	
	public static void main(String[] args) {
		Display display = new Display();

		Splash s = new Splash(display, SWT.NO_TRIM | SWT.ON_TOP);
		try {
			Thread.sleep( 5000 );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		s.close();

		display.dispose();
	}
	
	public void run() {
		final Image image = new Image( null, "src/cn/sh/fang/chenance/splash.gif" );

		//define a region 
		region = new Region();
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
		Rectangle size = region.getBounds();
		shell.setSize(bounds.width, bounds.height);
		shell.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(image, 0, 0);
			}
		});

		shell.setVisible(true);
	}
	
	public void close() {
		region.dispose();
		shell.dispose();
	}

}