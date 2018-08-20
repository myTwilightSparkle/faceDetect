package videoStreamDetect;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class Util {
	public static Mat getMat(BufferedImage bi, int itype, int mtype) {
		if (bi == null) {
			throw new IllegalArgumentException("bi == null");
		}

		// Don't convert if it already has correct type
		if (bi.getType() != itype) {

			// Create a buffered image
			BufferedImage image = new BufferedImage(bi.getWidth(), bi.getHeight(), itype);

			// Draw the image onto the new buffer
			Graphics2D g = image.createGraphics();
			try {
				g.setComposite(AlphaComposite.Src);
				g.drawImage(bi, 0, 0, null);
			} finally {
				g.dispose();
			}
		}

		byte[] pixels = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		Mat mat = Mat.eye(bi.getHeight(), bi.getWidth(), mtype);
		mat.put(0, 0, pixels);
		return mat;
	}

	public static BufferedImage getBi(Mat matrix, String fileExtension) {
		// convert the matrix into a matrix of bytes appropriate for
        // this file extension
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(fileExtension, matrix, mob);
        // convert the "matrix of bytes" into a byte array
        byte[] byteArray = mob.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufImage;

	}
}
