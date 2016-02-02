package com.example.tab4;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Contour {
	Mat img = new Mat();
	Mat bw = new Mat();
	MatOfPoint maxContour = new MatOfPoint();
	Point[] betterArr = null;
	MatOfPoint removeContourMat = null;
	List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	List<MatOfPoint> contours1 = new ArrayList<MatOfPoint>();
	// ���캯��
	public Contour(Mat image) {
		Imgproc.cvtColor(image, img, Imgproc.COLOR_RGB2GRAY);
		image.release();
	}

	// ��ֵ
	public int getThreshValue() {
		int rowNum = img.rows();
		int colNum = img.cols();
		int[] pixNum = new int[256];
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				pixNum[(int) img.get(i, j)[0]]++;
			}
		}

		int tempPix;// ƽ������
		int total;
		for (int i = 0; i < 256; i++) {
			total = 0;
			for (int j = -2; j <= 2; j++) {
				tempPix = i + j;
				if (tempPix < 0)
					tempPix = 0;
				if (tempPix > 255)
					tempPix = 255;
				total += pixNum[tempPix];
			}
			Double temp = Double.valueOf((double) total / 5.0 + 0.5);
			pixNum[i] = temp.intValue();
		}

		int size = rowNum * colNum;// �����ܸ���
		double maxVar = 0;// ����
		int threshold = 0;

		for (int i = 0; i < 256; i++) {
			int count1 = 0;
			int sum1 = 0;
			int count2 = 0;
			int sum2 = 0;
			double u1 = 0;// ǰ��ƽ���Ҷ�
			double u2 = 0;
			double w1 = 0;// ǰ����ռ����
			double w2 = 0;
			for (int j = 0; j < i; j++) {
				count1 += pixNum[j];
				sum1 += j * pixNum[j];
			}
			u1 = (double) sum1 / (double) count1;
			w1 = (double) count1 / (double) size;

			for (int j = i; j < 256; j++) {
				count2 += pixNum[j];
				sum2 += j * pixNum[j];
			}
			u2 = (double) sum2 / (double) count2;
			w2 = 1 - w1;

			double var = w1 * w2 * (u1 - u2) * (u1 - u2);// ����

			if (var > maxVar) {
				maxVar = var;
				threshold = i;
			}
		}
		return threshold;
	}

	// ��ֵ��
	public void getBw() {
		int level = getThreshValue();
		//System.out.println("��ֵ��" + level);
		// int level = 102;
		Imgproc.threshold(img, bw, level, 255, Imgproc.THRESH_BINARY_INV);
	}

	// ���������
	public void getMaxContour() {
		Mat hierarchy = new Mat();
		Imgproc.findContours(bw, contours, hierarchy, Imgproc.RETR_LIST,
				Imgproc.CHAIN_APPROX_NONE);
		hierarchy.release();//�ͷ��ڴ�
		double maxArea = 0;
		for (int i = 0; i < contours.size(); i++) {
			double area = Imgproc.contourArea((MatOfPoint) contours.get(i));
			if (area > maxArea) {
				maxArea = area;
				maxContour =  (MatOfPoint) contours.get(i);
			}
		}
	}

	// ��ȥ��Ҷ��������
	public Mat getContourMat(int[] removeContourIndex) {
		// ȥ��Ҷ���������
		int curLength = removeContourIndex.length;
		Point[] removeContourArr = new Point[curLength];
		for (int i = 0; i < curLength; i++) {
			removeContourArr[i] = betterArr[removeContourIndex[i]];
		}
		removeContourMat = new MatOfPoint(removeContourArr);
		// ������
		contours1.add(removeContourMat);
		Mat cm = Mat.zeros(img.rows(), img.cols(), CvType.CV_8UC1);
		Scalar color = new Scalar(255, 255, 255);
		Imgproc.drawContours(cm, contours1, -1, color, 4);
		return cm;
	}

	// ��δȥҶ������
	public Mat getContourMat() {
		
		contours1.add(maxContour);
		Mat cm = Mat.zeros(img.rows(), img.cols(), CvType.CV_8UC1);
		Scalar color = new Scalar(255, 255, 255);
		Imgproc.drawContours(cm, contours1, -1, color, 4);
		return cm;
	}

	// ��������Сֵ���±�
	public void findMinValue(int[] a, int[] valueIndex) {
		valueIndex[0] = a[0];
		valueIndex[1] = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] < valueIndex[0]) {
				valueIndex[0] = a[i];
				valueIndex[1] = i;
			}
		}
	}

	// ���������ֵ���±�
	public void findMaxValue(int[] a, int[] valueIndex) {
		valueIndex[0] = a[0];
		valueIndex[1] = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] > valueIndex[0]) {
				valueIndex[0] = a[i];
				valueIndex[1] = i;
			}
		}
	}

	// ����ѡ���������
	public void getBetterContour() {
		Point[] pointArr = maxContour.toArray();

		int conLength = pointArr.length;
		//System.out.println("������:" + conLength);
		int[] ii = new int[conLength];
		int[] jj = new int[conLength];
		for (int i = 0; i < conLength; i++) {
			ii[i] = (int) pointArr[i].y;
			jj[i] = (int) pointArr[i].x;
		}
		// ������ֵ���±�
		int[] Left = new int[2], Buttom = new int[2], Right = new int[2], Top = new int[2];
		findMinValue(jj, Left);
		findMinValue(ii, Buttom);
		findMaxValue(jj, Right);
		findMaxValue(ii, Top);
		// ���Ե�ľ�����������±�
		int valueEdge[] = { Left[0], Buttom[0], img.cols() - Right[0],
				img.rows() - Top[0] };
		int indexEdge[] = { Left[1], Buttom[1], Right[1], Top[1] };
		int[] minIndex = new int[2];// ��¼��С��Ե���±�
		int startPoint;
		findMinValue(valueEdge, minIndex);
		// ��С��Ե�ĶԱ���Ϊ���
		if (minIndex[1] < 2)
			startPoint = indexEdge[minIndex[1] + 2];
		else
			startPoint = indexEdge[minIndex[1] - 2];

		betterArr = new Point[conLength];
		for (int i = 0; i < conLength - startPoint; i++) {
			betterArr[i] = pointArr[i + startPoint];
		}
		for (int i = conLength - startPoint; i < conLength; i++) {
			betterArr[i] = pointArr[i - (conLength - startPoint)];
		}

	}

	// ͼƬ���
	void outputMat() throws IOException {
		File file = new File("/storage/extSdCard/mat.txt"); // ����������ݵ��ļ�
		FileWriter out = new FileWriter(file); // �ļ�д����

		int rowNum = img.rows(), colNum = img.cols();
		double[] a = new double[rowNum * colNum];
		for (int i = 0; i < rowNum; i++) {
			for (int j = 0; j < colNum; j++) {
				a[i * colNum + j] = img.get(i, j)[0];
			}
		}

		int n = a.length;
		// �������е�����д�뵽�ļ��С�ÿ�и�����֮��ո���
		for (int i = 0; i < n; i++) {
			out.write(a[i] + " ");
		}
		out.close();
	}

	
	//�ͷ��ڴ�
	public void releaseMemory(){
		img.release();
		bw.release();
		maxContour.release();
		removeContourMat.release();
		for(int i=0;i<contours.size();i++){
			contours.get(i).release();
		}
		contours1.get(0).release();//�ͷ��ڴ�
	}
	

	

}
