package com.kkartikeya.home.FaceCapture;

public class TestFaceCapture {

	public static void main(String args[]) {
		FaceCapture frame = new FaceCapture();
		try {
			frame.add("test");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
