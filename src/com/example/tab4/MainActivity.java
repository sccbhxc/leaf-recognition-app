package com.example.tab4;

import android.annotation.SuppressLint;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup {

	@SuppressWarnings("deprecation")
	private LocalActivityManager localActivityManager = null;
	private LinearLayout mainTabContainer = null;
	private Intent mainTabIntent = null;
	// Tab banner title
	private TextView mainTabTitleTextView = null;
	// Tab ImageView
	private ImageView homeImageView = null;
	private ImageView browseImageView = null;
	private ImageView picImageView = null;
	private ImageView resultImageView = null;
	private ImageView analysisImgeView = null;
//	private ImageView[] imageViewArr = { homeImageView, browseImageView,
//			picImageView, resultImageView, analysisImgeView };
//	private int[] imageViewDraw={};
	private final int receiveNum = 6;
	private int[] initOrder = new int[receiveNum];

	// public static boolean isIntial=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainTabContainer = (LinearLayout) findViewById(R.id.main_tab_container);
		localActivityManager = getLocalActivityManager();
		setContainerView("home", HomeTabActivity.class);
		initTab();
	}

	private void initTab() {
		mainTabTitleTextView = (TextView) findViewById(R.id.main_tab_banner_title);
		homeImageView = (ImageView) findViewById(R.id.home_tab_btn);
		browseImageView = (ImageView) findViewById(R.id.browse_tab_btn);
		picImageView = (ImageView) findViewById(R.id.pic_tab_btn);
		resultImageView = (ImageView) findViewById(R.id.result_tab_btn);
		analysisImgeView = (ImageView) findViewById(R.id.analysis_tab_btn);

		for (int i = 0; i < initOrder.length; i++) {
			initOrder[i] = i;
		}

		// ֲ������ʶ��
		homeImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mainTabTitleTextView.setText("ֲ������ʶ��");
				setContainerView("home", HomeTabActivity.class);
				homeImageView.setImageResource(R.drawable.home_press);
				browseImageView.setImageResource(R.drawable.browse);
				picImageView.setImageResource(R.drawable.pic);
				resultImageView.setImageResource(R.drawable.result);
				analysisImgeView.setImageResource(R.drawable.analysis);
			}
		});

		// ���ݿ����
		browseImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mainTabTitleTextView.setText("���ݿ����");
				setContainerView("browse", BrowseTabActivity.class);
				homeImageView.setImageResource(R.drawable.home);
				browseImageView.setImageResource(R.drawable.browse_press);
				picImageView.setImageResource(R.drawable.pic);
				resultImageView.setImageResource(R.drawable.result);
				analysisImgeView.setImageResource(R.drawable.analysis);
			}
		});

		// ֲ��ʶ��
		picImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mainTabTitleTextView.setText("ֲ��ʶ��");
				setContainerView("pic", PicTabActivity.class);
				homeImageView.setImageResource(R.drawable.home);
				browseImageView.setImageResource(R.drawable.browse);
				picImageView.setImageResource(R.drawable.pic_press);
				resultImageView.setImageResource(R.drawable.result);
				analysisImgeView.setImageResource(R.drawable.analysis);
			}
		});

		// ����鿴
		resultImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mainTabTitleTextView.setText("����鿴");
				setContainerView("result", ResultTabActivity.class);
				homeImageView.setImageResource(R.drawable.home);
				browseImageView.setImageResource(R.drawable.browse);
				picImageView.setImageResource(R.drawable.pic);
				resultImageView.setImageResource(R.drawable.result_press);
				analysisImgeView.setImageResource(R.drawable.analysis);
			}
		});
		// new Thread(new RefreshThread()).start();

		// �������
		analysisImgeView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mainTabTitleTextView.setText("�������");
				setContainerView("analysis", AnalysisTabActivity.class);
				homeImageView.setImageResource(R.drawable.home);
				browseImageView.setImageResource(R.drawable.browse);
				picImageView.setImageResource(R.drawable.pic);
				resultImageView.setImageResource(R.drawable.result);
				analysisImgeView.setImageResource(R.drawable.analysis_press);
//				Intent intent=(new AnalysisTab()).execute(MainActivity.this);
//				startActivity(intent);
			}
		});
		// new Thread(new RefreshThread()).start();

	}

	public void setContainerView(String id, Class<?> activity) {
		mainTabContainer.removeAllViews();
		mainTabIntent = new Intent(this, activity);
		mainTabContainer.addView(localActivityManager.startActivity(id,
				mainTabIntent).getDecorView());
	}
	//�˳�����
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	boolean isExit = false;
	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�",
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			System.exit(0);
		}
	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};
	
}
