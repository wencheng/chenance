package cn.sh.fang.chenance;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Splash {

	// http://dev.eclipse.org/viewcvs/index.cgi/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet180.java?view=markup&content-type=text%2Fvnd.viewcvs-markup&revision=HEAD
	// http://www.ibm.com/developerworks/java/library/j-2dswt/
	
	public static void main(String[] args) {
		Display display = new Display();
		final Image image = display.getSystemImage(SWT.ICON_WARNING);
		//Shell must be created with style SWT.NO_TRIM
		final Shell shell = new Shell(display, SWT.NO_TRIM | SWT.ON_TOP);
		shell.setBackground(display.getSystemColor(SWT.COLOR_RED));
		//define a region 
		Region region = new Region();
		Rectangle pixel = new Rectangle(0, 0, 1, 1);
		for (int y = 0; y < 200; y+=2) {
				for (int x = 0; x < 200; x+=2) {
					pixel.x = x;
					pixel.y = y;
					region.add(pixel);
				}
			}
		//define the shape of the shell using setRegion
		shell.setRegion(region);
		Rectangle size = region.getBounds();
		shell.setSize(size.width, size.height);
		shell.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				Rectangle bounds = image.getBounds();
				Point size = shell.getSize();
				e.gc.drawImage(image, 0, 0, bounds.width, bounds.height, 10, 10, size.x-20, size.y-20);
			}
		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		region.dispose();
		display.dispose();
	}

}