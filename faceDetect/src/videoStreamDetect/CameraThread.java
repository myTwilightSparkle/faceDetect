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
		} // ��ʼ��ȡ����ͷ����
		System.out.println("camera started");
		CanvasFrame canvas = new CanvasFrame("����ͷ");// �½�һ������
		canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		canvas.setAlwaysOnTop(true);
		Frame currentFrame = new Frame();
		Java2DFrameConverter converter = new Java2DFrameConverter();
		while (true) {
			if (!canvas.isDisplayable()) {// �����Ƿ�ر�
				try {
					grabber.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}// ֹͣץȡ
				System.exit(2);// �˳�
			}
			try {
				currentFrame = grabber.grab();
		        VideoStreamFaceDetector.bi = converter.getBufferedImage(currentFrame);
				canvas.showImage(currentFrame);
			} catch (Exception e1) {
				e1.printStackTrace();
			}// ��ȡ����ͷͼ�񲢷ŵ���������ʾ�� �����Frame frame=grabber.grab(); frame��һ֡��Ƶͼ��
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}// 50����ˢ��һ��ͼ��
		}
	}

}
