package com.example.tab4;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class AnalysisTabActivity extends Activity {
	private XYMultipleSeriesDataset mDataset;
	private XYMultipleSeriesRenderer mRenderer;
	// private XYSeries mCurrentSeries;
	// private XYSeriesRenderer mCurrentRenderer;
	private GraphicalView mChartView;
	private LinearLayout layout = null;

	public static double[] curArr = null;// ��������
	public static double[] meanCurArr = null;// ƽ����������
	public static int handleIndex;// Ҷ��λ��
	public static int handleLength;// Ҷ������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xy_chart);
	}

	protected void onResume() {
		super.onResume();

		// ����x������
		List<double[]> xList = new ArrayList<double[]>();
		double[] xArr = null;
		// ����y������
		List<double[]> yList = new ArrayList<double[]>();
		if (curArr != null) {
			// ����ͼ������
			String[] titles = new String[] { "����", "�ֲ�ƽ������", "Ҷ��λ��", "Ҷ������",
					"Ҷ������" };
			int curLength = curArr.length;
			xArr = new double[curLength];
			for (int i = 0; i < curLength; i++) {
				xArr[i] = i;
			}
			for (int i = 0; i < 2; i++) {
				xList.add(xArr);
			}
			xList.add(new double[] { handleIndex, handleIndex });
			xList.add(new double[] { (handleIndex + handleLength) % curLength,
					(handleIndex + handleLength) % curLength });
			xList.add(new double[] {
					(handleIndex - handleLength + curLength) % curLength,
					(handleIndex - handleLength + curLength) % curLength });
			yList.add(curArr);
			yList.add(meanCurArr);
			yList.add(new double[] { 0, 1 });
			yList.add(new double[] { 0, 1 });
			yList.add(new double[] { 0, 1 });
			// ����������ɫ
			int[] colors = new int[] { Color.BLUE, Color.RED, Color.GREEN,
					Color.GRAY, Color.GRAY };
			// ���������
			PointStyle[] styles = new PointStyle[] { PointStyle.POINT,
					PointStyle.POINT, PointStyle.POINT, PointStyle.POINT,
					PointStyle.POINT };
			mRenderer = buildRenderer(colors, styles);
			int length = mRenderer.getSeriesRendererCount();
			for (int i = 0; i < length; i++) {
				((XYSeriesRenderer) mRenderer.getSeriesRendererAt(i))
						.setFillPoints(true);
				((XYSeriesRenderer) mRenderer.getSeriesRendererAt(i))
						.setLineWidth(2);
			}
			// ����ͼ����⡢�������ע�������᷶Χ����������ɫ
			setChartSettings(mRenderer, "Ҷ�����ʷֲ�", "λ��", "����", 0, curLength, 0,
					1, Color.LTGRAY, Color.LTGRAY);
			mRenderer.setXLabels(18);
			mRenderer.setYLabels(10);
			// mRenderer.setShowGrid(true);
			mRenderer.setXLabelsAlign(Align.RIGHT);
			mRenderer.setYLabelsAlign(Align.RIGHT);
			mRenderer.setZoomButtonsVisible(true);
			mRenderer.setPanLimits(new double[] { 0, curLength, 0, 1 });
			mRenderer.setZoomLimits(new double[] { 0, curLength, 0, 1.2 });
			// �õ�Ҫ���Ƶ����ݼ�
			mDataset = buildDataset(titles, xList, yList);

			// �õ�ͼ����ͼ�������Ƶ�������
			layout = (LinearLayout) findViewById(R.id.chart);
			mChartView = ChartFactory.getLineChartView(this, mDataset,
					mRenderer);
			mChartView.repaint();
			// �Ƴ�ԭ�е�LinearLayout�е���ͼ�ؼ�
			layout.removeAllViewsInLayout();
			xList.clear();
			yList.clear();
			layout.addView(mChartView, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		} else {
			// ����ͼ������
			String[] titles = new String[] { "����", "�ֲ�ƽ������" };
			// ����x������
			xArr = new double[100];
			for (int i = 0; i < 100; i++) {
				xArr[i] = Math.PI * i / 100;
			}

			// ����y������
			curArr = new double[100];
			meanCurArr = new double[100];
			for (int i = 0; i < 100; i++) {
				curArr[i] = Math.sin(Math.PI * i / 100);
				meanCurArr[i] = Math.cos(Math.PI * i / 100);
			}
			for (int i = 0; i < titles.length; i++) {
				xList.add(xArr);
			}
			yList.add(curArr);
			yList.add(meanCurArr);
			// ����������ɫ
			int[] colors = new int[] { Color.BLUE, Color.RED };
			// ���������
			PointStyle[] styles = new PointStyle[] { PointStyle.POINT,
					PointStyle.POINT };
			mRenderer = buildRenderer(colors, styles);
			int length = mRenderer.getSeriesRendererCount();
			for (int i = 0; i < length; i++) {
				((XYSeriesRenderer) mRenderer.getSeriesRendererAt(i))
						.setFillPoints(true);
				((XYSeriesRenderer) mRenderer.getSeriesRendererAt(i))
						.setLineWidth(2);
			}
			// ����ͼ����⡢�������ע�������᷶Χ����������ɫ
			setChartSettings(mRenderer, "Ҷ�����ʷֲ�", "λ��", "����", 0, Math.PI, -1,
					1, Color.LTGRAY, Color.LTGRAY);
			mRenderer.setXLabels(20);
			mRenderer.setYLabels(10);
			// mRenderer.setShowGrid(true);
			mRenderer.setXLabelsAlign(Align.RIGHT);
			mRenderer.setYLabelsAlign(Align.RIGHT);
			mRenderer.setZoomButtonsVisible(true);
			mRenderer.setPanLimits(new double[] { 0, Math.PI, -1, 1 });
			mRenderer.setZoomLimits(new double[] { 0, 4, -1.2, 1.2 });
			// mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.VERTICAL);
			// �õ�Ҫ���Ƶ����ݼ�
			mDataset = buildDataset(titles, xList, yList);
			// �õ�ͼ����ͼ�������Ƶ�������
			layout = (LinearLayout) findViewById(R.id.chart);
			mChartView = ChartFactory.getLineChartView(this, mDataset,
					mRenderer);
			mChartView.repaint();
			// �Ƴ�ԭ�е�LinearLayout�е���ͼ�ؼ�
			layout.removeAllViewsInLayout();

			xList.clear();
			yList.clear();
			layout.addView(mChartView, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}

	}

	// ��DataSet���������.
	public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles,
			List<double[]> xValues, List<double[]> yValues, int scale) {
		int length = titles.length;
		for (int i = 0; i < length; i++) {
			XYSeries series = new XYSeries(titles[i], scale);
			double[] xV = xValues.get(i);
			double[] yV = yValues.get(i);
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]);
			}
			dataset.addSeries(series);
		}
	}

	protected XYMultipleSeriesDataset buildDataset(String[] titles,
			List<double[]> xValues, List<double[]> yValues) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		addXYSeries(dataset, titles, xValues, yValues, 0);
		return dataset;
	}

	// ����ͼ����������
	protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors,
			PointStyle[] styles) {
		renderer.setAxisTitleTextSize(16);
		renderer.setChartTitleTextSize(30);
		renderer.setLabelsTextSize(15);
		renderer.setLegendTextSize(15);
		renderer.setPointSize(5f);
		renderer.setMargins(new int[] { 35, 35, 15, 20 });
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			renderer.addSeriesRenderer(r);
		}
	}

	// ����XYMultipleSeriesRenderer.
	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		setRenderer(renderer, colors, styles);
		return renderer;
	}

	// ����renderer��һЩ����.
	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor,
			int labelsColor) {
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
	}

}
