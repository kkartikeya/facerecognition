package com.kkartikeya.home.FaceCapture;

import static org.bytedeco.javacpp.helper.opencv_objdetect.cvHaarDetectObjects;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvClearMemStorage;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSeqElem;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvLoad;
import static org.bytedeco.javacpp.opencv_core.cvPoint;
import static org.bytedeco.javacpp.opencv_imgproc.CV_AA;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_INTER_LINEAR;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvEqualizeHist;
import static org.bytedeco.javacpp.opencv_imgproc.cvRectangle;
import static org.bytedeco.javacpp.opencv_imgproc.cvResize;
import static org.bytedeco.javacpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;

import javax.swing.JFrame;

import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_objdetect.CvHaarClassifierCascade;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameConverter;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;


public class FaceCapture {
	public final String XML_FILE = getClass().getClassLoader().getResource("haarcascade_frontalface_alt.xml").getPath();
	private FrameConverter converter = new OpenCVFrameConverter.ToIplImage();

	public void add(String path) throws Exception {

		FrameGrabber grabber = new FFmpegFrameGrabber("http://192.168.0.12/axis-cgi/mjpg/video.cgi");
		grabber.setFormat("mjpeg");
		grabber.setFrameRate(10);
		grabber.start();
		IplImage src = (IplImage) converter.convert(grabber.grab());

		while ((src = (IplImage) converter.convert(grabber.grab())) != null) {

			IplImage grayImg = cvCreateImage(cvGetSize(src), IPL_DEPTH_8U, 1);
			cvCvtColor(src, grayImg, CV_BGR2GRAY);

			// scale the grayscale (to speed up face detection)
			//IplImage smallImg = IplImage.create(grayImg.width()/2, grayImg.height()/2, IPL_DEPTH_8U, 1);
			//cvResize(grayImg, smallImg, CV_INTER_LINEAR);

			// equalize the grayscale
			//cvEqualizeHist(smallImg, smallImg);
			cvEqualizeHist(grayImg, grayImg);

			// create temp storage, used during object detection
			CvMemStorage storage = CvMemStorage.create();

			// instantiate a classifier cascade for face detection
			CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(cvLoad(XML_FILE));

			//CvSeq faces = cvHaarDetectObjects(smallImg, cascade, storage, 1.1, 8, CV_HAAR_DO_CANNY_PRUNING);
			CvSeq faces = cvHaarDetectObjects(grayImg, cascade, storage, 1.1, 8, CV_HAAR_DO_CANNY_PRUNING);

			cvClearMemStorage(storage);

			if( faces.total() > 0) {
				System.out.println("Found " + faces.total() + " face(s)");
				CvRect r = new CvRect(cvGetSeqElem(faces, 0));
				System.out.println("Found " + faces.total() + " face(s)");

			}
		}
		grabber.stop();
		System.exit(0);
	}
}