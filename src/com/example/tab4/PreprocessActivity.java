package com.example.tab4;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

public class PreprocessActivity extends Activity{
	// Image image;
	private ImageView editImgView;
	private Canvas canvas;
	private Paint paint;
	private Bitmap editBmp;
	private float scale;
//	private Button toneButton;
	private Button finishButton;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preprocess);
		editImgView = (ImageView) findViewById(R.id.editImgView);
//		toneButton=(Button)findViewById(R.id.toneButton);
		finishButton=(Button)findViewById(R.id.finishButton);
		Intent intent = getIntent();
		byte[] imgByte = intent.getByteArrayExtra("imgByte");
		Bitmap bmp = getBitmap(imgByte);
		imgByte=null;
		// �ж��ǹ���Ҫת90��
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		if (width > height) {
			Matrix m = new Matrix();
			int angle = 90;
			m.setRotate(angle);
			bmp = Bitmap.createBitmap(bmp, 0, 0, width, height, m, true);// ��������ͼƬ
		}
		editBmp = Bitmap.createBitmap( bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
		canvas = new Canvas(editBmp);
		paint=new Paint();
		paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
        Matrix matrix = new Matrix();
        canvas.drawBitmap(bmp, matrix, paint);
		editImgView.setImageBitmap(editBmp);
		editImgView.setOnTouchListener(new EditListener());
		//����ͼƬ����ͼ�ı���
		scale= (float)bmp.getHeight()/(getResources().getDisplayMetrics().density*400f);

//		toneButton.setOnClickListener(new OnClickListener() {
//			public void onClick(View arg0) {
//				GPUImage gpuImage=new GPUImage(PreprocessActivity.this);
//				gpuImage.setImage(editBmp);
//				gpuImage.setFilter(new GPUImageToneCurveFilter());
//				editBmp=gpuImage.getBitmapWithFilterApplied();
//				editImgView.setImageBitmap(editBmp);
//			}
//		});
		
		finishButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent=new Intent();
				intent.putExtra("finish", getBytes(editBmp));
				setResult(3, intent);
				finish();
			}
		});
	}

	
	public byte[] getBytes(Bitmap bitmap) {
		// ʵ�����ֽ����������
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);// ѹ��λͼ
		return baos.toByteArray();// ���������ֽ�����
	}

	public Bitmap getBitmap(byte[] data) {
		return BitmapFactory.decodeByteArray(data, 0, data.length);// ���ֽ��������λͼ
	}

	
	class EditListener implements OnTouchListener{	
		private float downx = 0;
	    private float downy = 0;
	    private float upx = 0;
	    private float upy = 0;
	    
		public boolean onTouch(View v, MotionEvent event) {
	        int action = event.getAction();
	        switch (action) {
	        case MotionEvent.ACTION_DOWN:
	            downx = event.getX()*scale;
	            downy = (event.getY())*scale;
	            //System.out.println(downx+" "+downy);
	            break;
	        case MotionEvent.ACTION_MOVE:      	
	            upx = event.getX()*scale;
	            upy = (event.getY())*scale;
	            canvas.drawLine(downx, downy, upx, upy, paint);
	            editImgView.invalidate();
	            downx = upx;
	            downy = upy;
	            break;
	        case MotionEvent.ACTION_UP:
	            // ֱ�߻���
	            upx = event.getX()*scale;
	            upy = (event.getY())*scale;
	            canvas.drawLine(downx, downy, upx, upy, paint);
	            editImgView.invalidate();// ˢ��
	            break;
	        default:
	            break;
	        }
	        return true;
	    }
	}
	
}
