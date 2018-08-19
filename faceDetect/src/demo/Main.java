package demo;

import videoStreamDetect.VideoStreamFaceDetector;

public class Main {

	
	
	private static VideoStreamFaceDetector vsfd = new VideoStreamFaceDetector();
	
	public static void main(String[] args) {
		vsfd.startDetecting();
	}
	
}
