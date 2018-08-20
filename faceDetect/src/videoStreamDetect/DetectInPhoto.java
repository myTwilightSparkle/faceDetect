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

	public static MatOfRect detect(BufferedImage bi) {
		Mat image = Util.getMat(bi, BufferedImage.TYPE_3BYTE_BGR, CvType.CV_8UC3);

		MatOfRect faceDetections = new MatOfRect();
		faceDetector.detectMultiScale(image, faceDetections);

		System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

		return faceDetections;
	}

	
}
