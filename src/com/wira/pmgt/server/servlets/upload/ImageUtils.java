package com.wira.pmgt.server.servlets.upload;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;

public class ImageUtils {

	static int IMG_WIDTH = 32;
	static int IMG_HEIGHT = 32;
	static Logger log = Logger.getLogger(ImageUtils.class);

	public static void resizeImage(HttpServletResponse resp, byte[] bites)
			throws IOException {
		BufferedImage bimage = ImageIO.read(new ByteArrayInputStream(bites));
		// no resize unless size was provided
		// BufferedImage image = resizeImage(bimage, IMG_WIDTH, IMG_HEIGHT);
		ImageIO.write(bimage, "png", resp.getOutputStream());

	}

	public static void resizeImage(HttpServletResponse resp, byte[] bites,
			int width, int height) throws IOException {

		BufferedImage bimage = ImageIO.read(new ByteArrayInputStream(bites));
		BufferedImage image = resizeImage(bimage, width, height);
		ImageIO.write(image, "png", resp.getOutputStream());

	}

	private static BufferedImage resizeImage(BufferedImage image, int width,
			int height) {
		log.debug("Resizing image: width=" + width + ", height=" + height);
		ResampleOp resampleOp = new ResampleOp(width, height);
		resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Normal);
		BufferedImage rescaledImage = resampleOp.filter(image, null);

		return rescaledImage;
	}

}
