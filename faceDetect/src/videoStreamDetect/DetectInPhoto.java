package videoStreamDetect;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class DetectInPhoto {

	private static CascadeClassifier faceDetector;

	static {
		System.loadLibrary("opencv_java341");
		System.out.println("\nRunning FaceDetector");
		faceDetector = new CascadeClassifier(
				DetectInPhoto.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1));
	}

	public static boolean detect(BufferedImage bi) {
		Mat image = getMat(bi, BufferedImage.TYPE_3BYTE_BGR, CvType.CV_8UC3);

		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);

		System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

		if (faceDetections.toArray().length > 0) {
			for (Rect rect : faceDetections.toArray()) {
				Imgproc.rectangle(image, new Point(rect.x, rect.y),
						new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
			Date date = new Date();
			String prefix = "faces_"+sdf.format(date);
			String suffix = (date.getTime()) % (1000 * 3600) + "";
			
			File dir = new File(prefix);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			String ofilename = prefix+"/ouput" + suffix + ".png";
			System.out.println(String.format("Writing %s", ofilename));
			Imgcodecs.imwrite(ofilename, image);
			return true;
		}
		return false;
	}

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
}
