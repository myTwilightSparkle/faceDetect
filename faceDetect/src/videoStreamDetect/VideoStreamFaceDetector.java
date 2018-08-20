package videoStreamDetect;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class VideoStreamFaceDetector {
	private static final int INTERVAL = 200;// 检测时间间隔

	private OpenCVFrameGrabber grabber;
	protected static BufferedImage bi;
	
	private MatOfRect prvFaceDetections;// 上一次检测结果
	
	private CanvasFrame canvas;

	static {
		Java2DFrameConverter converter = new Java2DFrameConverter();
		bi = converter.getBufferedImage(new Frame());
	}

	public VideoStreamFaceDetector() {
		grabber = new OpenCVFrameGrabber(0);// device number
		canvas = new CanvasFrame("检测到人脸");
	}

	public VideoStreamFaceDetector(OpenCVFrameGrabber grabber) {
		this.grabber = grabber;
		canvas = new CanvasFrame("检测到人脸");
	}

	// 开始检测
	public void startDetecting() {
		Thread cameraThread = new CameraThread(grabber);
		cameraThread.start();
		while (true) {
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (bi != null) {
				MatOfRect faceDetections = DetectInPhoto.detect(bi);
				Mat image = Util.getMat(bi, BufferedImage.TYPE_3BYTE_BGR, CvType.CV_8UC3);
				// 画人脸框
				if (faceDetections.toArray().length > 0) {
					for (Rect rect : faceDetections.toArray()) {
						Imgproc.rectangle(image, new Point(rect.x, rect.y),
								new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
					}
					// frame显示
					BufferedImage bi1 = Util.getBi(image, ".png");
					showOnCanvas(bi1);
					// 是否有新人脸
					boolean flag = hasNewFaces(faceDetections);
					System.out.println("new faces: " + flag);
					if (flag) {
						output(image);
					}
				}
				prvFaceDetections = faceDetections;
			}
		}
	}

	private boolean hasNewFaces(MatOfRect faceDetections) {
		if (faceDetections.toArray().length <= 0) {
			return false;
		} else if (prvFaceDetections == null) {
			return true;
		} else if (prvFaceDetections.toArray().length < faceDetections.toArray().length) {
			return true;
		}
		return false;
	}

	public void output(Mat image) {
		
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
	}

	public void showOnCanvas(BufferedImage bi) {
		canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.setAlwaysOnTop(true);
		if (bi != null)
			canvas.showImage(bi);
	}
}
