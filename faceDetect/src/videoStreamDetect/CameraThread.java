package videoStreamDetect;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;


public class CameraThread extends Thread {
	
	private OpenCVFrameGrabber grabber;

	public CameraThread(OpenCVFrameGrabber grabber) {
		this.grabber = grabber;
	}

	@Override
	public void run() {
		super.run();
		try {
			grabber.start();
		} catch (Exception e) {
			e.printStackTrace();
		} // 开始获取摄像头数据
		System.out.println("camera started");
		CanvasFrame canvas = new CanvasFrame("摄像头");// 新建一个窗口
		canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.setAlwaysOnTop(true);
		Frame currentFrame = new Frame();
		Java2DFrameConverter converter = new Java2DFrameConverter();
		while (true) {
			if (!canvas.isDisplayable()) {// 窗口是否关闭
				try {
					grabber.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}// 停止抓取
				System.exit(2);// 退出
			}
			try {
				currentFrame = grabber.grab();
		        VideoStreamFaceDetector.bi = converter.getBufferedImage(currentFrame);
				canvas.showImage(currentFrame);
			} catch (Exception e1) {
				e1.printStackTrace();
			}// 获取摄像头图像并放到窗口上显示， 这里的Frame frame=grabber.grab(); frame是一帧视频图像
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}// 50毫秒刷新一次图像
		}
	}

}
