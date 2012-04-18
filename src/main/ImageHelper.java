package main;

import java.awt.Image;
import java.awt.image.PixelGrabber;

public class ImageHelper {

	public static int[] getImagePixels(Image image) throws InterruptedException{
		
		int width=image.getWidth(null);
		int height=image.getHeight(null);
		
		int[] pixels=new int[width*height];
		
		PixelGrabber grabber=new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
		grabber.grabPixels();
		
		return pixels;
	}
	
}
