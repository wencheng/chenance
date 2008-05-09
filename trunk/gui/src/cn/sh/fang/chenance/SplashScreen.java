package cn.sh.fang.chenance;
import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JWindow;

/**
 * A splash screen that produces the illusion of transparency for a top level
 * Container.
 * 
 * @author F. Fleischer
 */
public class SplashScreen extends JWindow implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3386403508902417314L;

	/**
	 * File path of the image to display, preferrably .gif or .png. Best choice
	 * would be the png-24 format, since it supports 256 transparency levels.
	 */
	private String imageFile;

	/**
	 * A writable off screen image.
	 */
	private BufferedImage bufImage;

	/**
	 * The rectangle to be captured.
	 */
	private Rectangle rect;

	/**
	 * True if initialization thread is running.
	 */
	private boolean isAlive;

	/**
	 * Constructor for the SplashScreen object. Starts initialization and
	 * showing of the splash screen immediately.
	 * 
	 * @param imageFile
	 *            File path of the Image to display.
	 */
	public SplashScreen(String imageFile) {
		this.imageFile = imageFile;
		run();
	}
	
	public static void main(String[] args) {
		SplashScreen win = new SplashScreen("cn/sh/fang/chenance/splash.gif");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		win.close();
	}

	/**
	 * Starts the initialization thread of the SplashScreen.
	 */
	public void run() {
		isAlive = true;
		// use ImageIcon, so we don't need to use MediaTracker
		URL url = this.getClass().getClassLoader().getResource(imageFile);
		if ( url == null ) {
			System.err.println("File " + imageFile
					+ " was not found.");	
		}
		Image image = new ImageIcon(url).getImage();
		int imageWidth = image.getWidth(this);
		int imageHeight = image.getHeight(this);
		if (imageWidth > 0 && imageHeight > 0) {
			int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
			int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
			// a Rectangle centered on screen
			rect = new Rectangle((screenWidth - imageWidth) / 2,
					(screenHeight - imageHeight) / 2, imageWidth, imageHeight);
			// the critical lines, create a screen shot
			try {
				bufImage = new Robot().createScreenCapture(rect);
			} catch (AWTException e) {
				e.printStackTrace();
			}
			// obtain the graphics context from the BufferedImage
			Graphics2D g2D = bufImage.createGraphics();
			// Draw the image over the screen shot
			g2D.drawImage(image, 0, 0, this);
			// draw the modified BufferedImage back into the same space
			setBounds(rect);
			// present our work :)
			setVisible(true);
		} else {
			System.err.println("File " + imageFile
					+ " was not found or is not an image file.");
		}
		isAlive = false;
	}

	/**
	 * Disposes of the SplashScreen. To be called shortly before the main
	 * application is ready to be displayed.
	 * 
	 * @exception IllegalStateException
	 *                Is thrown if the initialization thread has not yet reached
	 *                it's end.
	 */
	public void close() throws IllegalStateException {
		if (!isAlive) {
			dispose();
		} else {
			// better not dispose a SplashScreen that has not been painted on
			// screen yet.
			throw new IllegalStateException(
					"SplashScreen not yet fully initialized.");
		}
	}

	/**
	 * Overrides the paint() method of JWindow.
	 * 
	 * @param g
	 *            The graphics context
	 */
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(bufImage, 0, 0, this);
	}
}
