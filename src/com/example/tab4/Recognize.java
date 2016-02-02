package com.example.tab4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;


public class Recognize {

	final int dataSize = 51;
	final int barNum = 21;
	final int radiusBase = 12;
	final int scaleNum = 6;

	double[] Hu = new double[7];
	double[] meanHist = new double[barNum];
	int[] resultIndex = new int[dataSize];
	int[] removeContourIndex = null;
	Contour contour = null;
	Mat removeHandleImg = new Mat();

	public Recognize(Mat image) {
		contour = new Contour(image);
		contour.getBw();
		contour.getMaxContour();
		contour.getBetterContour();
	}

	// ����Hu��
	public void getHu(MatOfPoint removeContourMat) {
		Moments moments = Imgproc.moments(removeContourMat);
		Mat hu = new Mat();
		Imgproc.HuMoments(moments, hu);
		for (int i = 0; i < 7; i++) {
			Hu[i] = hu.get(i, 0)[0];
			//System.out.println("Hu�أ�" + Hu[i]);
		}
	}

	// Bhattacharyya ����,ԽСԽ����
	void Bhattacharyya(double[][] Data, double[] meanHist, double[] dot) {
		for (int i = 0; i < dataSize; i++) {
			dot[i] = 0;
		}
		for (int i = 0; i < dataSize; i++) {
			for (int j = 0; j < barNum; j++) {
				dot[i] = dot[i] + Math.sqrt(Data[i][j] * meanHist[j]);
			}
			dot[i] = Math.sqrt(1 - dot[i]);
		}
	}

	// ð�ݷ���������
	void SortAscend(double[] data, int[] index, int n) {
		double temp;
		int temp1;
		for (int i = 0; i < n - 1; i++) {
			for (int j = i + 1; j > 0; j--) {
				if (data[j] < data[j - 1]) {
					temp = data[j];
					data[j] = data[j - 1];
					data[j - 1] = temp;
					temp1 = index[j];
					index[j] = index[j - 1];
					index[j - 1] = temp1;
				}
			}
		}
	}

	// ��ȡ���ݿ�
	void readDatabase(double[][] Data, String filename)
			throws NumberFormatException, IOException {
		File file = new File(filename);
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line; // һ������
		int row = 0;
		// ���ж�ȡ������ÿ��������뵽������
		while ((line = in.readLine()) != null&&row<dataSize) {
			String[] temp = line.split(" ");
			for (int j = 0; j < temp.length; j++) {
				Data[row][j] = Double.parseDouble(temp[j]);
			}
			row++;
		}
		in.close();
	}

	// ��Hu�ؾ���
	double getHuDst(double[] huData) {
		double dst = 0;
		int signHu1;
		int signHu2;
		
		//int count=0;
		
		for (int i = 0; i < 7; i++) {
			double absHu1 = Math.abs(Hu[i]);
			double absHu2 = Math.abs(huData[i]);

			if (Hu[i] > 0)
				signHu1 = 1;
			else if (Hu[i] < 0)
				signHu1 = -1;
			else
				signHu1 = 0;

			if (huData[i] > 0)
				signHu2 = 1;
			else if (huData[i] < 0)
				signHu2 = -1;
			else
				signHu2 = 0;
			if (absHu1 > 1e-5 && absHu2 > 1e-5) {
				dst += Math.abs(1/ (signHu1 * Math.log10(absHu1)) - 1
						/ (signHu2 * Math.log10(absHu2)));
				//count++;
			}
		}
		//System.out.println("Hu�ظ�����"+count);
		return dst;
	}

	// ֲ��ʶ��
	public void RecongnizePlant() throws NumberFormatException, IOException {
		int[] radius = new int[scaleNum];

		// ��߶�ƽ��ֱ��ͼ
		Curvature curvature = new Curvature(contour.bw, contour.betterArr);
		for (int i = 0; i < scaleNum; i++) {
			radius[i] = radiusBase + i;
			curvature.getCur(radius[i]);
			curvature.removeHandleCur();
			curvature.getHist();
			if (i == 3) {
				curvature.removeHandleIndex();
				removeContourIndex = curvature.removeContourIndex;
				// �õ�ȥ��Ҷ����������
				contour.getContourMat(curvature.removeContourIndex);
				getHu(contour.removeContourMat);
				//�����ʷֲ�ͼ��ʾ��ͼ����
				curvature.sendCurToAn();
			}
			// System.out.println("ֱ��ͼ��"+i);
			for (int j = 0; j < barNum; j++) {
				meanHist[j] += curvature.Hist[j] / scaleNum;
				// System.out.print(curvature.Hist[j]+" ");
			}
			// System.out.println();
		}

		// �������һ�ε�ȥ��Ҷ��������
		curvature.removeHandleIndex();
		removeContourIndex = curvature.removeContourIndex;
		// �õ�������ʾ��ͼƬ
		removeHandleImg = contour.getContourMat(removeContourIndex);
		
		// ��ȡ�������ݿ�
		double[][] Data = new double[dataSize][barNum];
		readDatabase(Data, "/storage/extSdCard/database.txt");

		// ����Bhattacharyya����
		double[] dot = new double[dataSize];
		Bhattacharyya(Data, meanHist, dot);

//		System.out.println("���ʾ��룺");
//		 for (int i = 0; i < dot.length; i++) {
//		 System.out.print(dot[i]+" ");
//		 if((i+1)%10==0)System.out.println();
//		 }

		// ��ȡHu�����ݿ�
		double[][] Data1 = new double[dataSize][7];
		readDatabase(Data1, "/storage/extSdCard/databaseHu.txt");
		
		//����Hu�ؾ���
		double[] dst=new double[dataSize];
		for(int i=0;i<dataSize;i++){
			dst[i]=getHuDst(Data1[i]);
		}

//		System.out.println("Hu�ؾ��룺");
//		for (int i = 0; i < dataSize; i++) {
//			System.out.print(dst[i] + " ");
//			if ((i + 1) % 10 == 0)
//				System.out.println();
//		}
		
		//�õ��ۺ�ָ��
		double[] objValue=new double[dataSize];
		for(int i=0;i<dataSize;i++){
			objValue[i]=0.7*dot[i]+0.3*dst[i];
		}
		
		// ֲ��ı�־�±�
		for (int i = 0; i < dataSize; i++) {
			resultIndex[i] = i;
		}

		SortAscend(objValue, resultIndex, dataSize);

		// �ͷ��ڴ�
		contour.releaseMemory();
		curvature.releaseMemory();
	}

	// ���ֱ��ͼ
	void outputHist(double[] a, String filename) throws IOException {
		File file = new File("/storage/extSdCard/" + filename); // ����������ݵ��ļ�

		FileWriter out = new FileWriter(file); // �ļ�д����
		int n = a.length;
		// �������е�����д�뵽�ļ��С�ÿ�и�����֮��ո���
		for (int i = 0; i < n; i++) {
			out.write(a[i] + " ");
		}
		out.close();
	}

}
