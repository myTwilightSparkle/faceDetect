package videoStreamDetect;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

public class VideoStreamFaceDetector {
	private static final int INTERVAL = 200;// 检测时间间隔

	private OpenCVFrameGrabber grabber;
	protected static BufferedImage bi;
	static {
		Java2DFrameConverter converter = new Java2DFrameConverter();
		bi = converter.getBufferedImage(new Frame());
	}

	public VideoStreamFaceDetector() {
		grabber = new OpenCVFrameGrabber(0);// device number
	}

	public VideoStreamFaceDetector(OpenCVFrameGrabber grabber) {
		this.grabber = grabber;
	}

	// 开始检测
	public void startDetecting() {
		Thread cameraThread = new CameraThread(grabber);
		cameraThread.start();
		CanvasFrame canvas = new CanvasFrame("检测到人脸");
		canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.setAlwaysOnTop(true);
		while (true) {
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (bi != null) {
				boolean hasFaces = DetectInPhoto.detect(bi);
				if (hasFaces)
					canvas.showImage(bi);
			}
		}
	}
}

