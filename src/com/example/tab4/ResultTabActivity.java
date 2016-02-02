package com.example.tab4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ResultTabActivity extends Activity {
	private String LeafList[] = { "˨Ƥ��", "������", "��צ��", "������", "�Ƿ�", "ƽ�����",
			"��������", "���ô��Ͼ�", "����������", "ɽ����", "�", "��ʢ��ɽ�", "������", "����", "�����ľ",
			"�޻���", "����", "�Һ���", "����������", "���������", "��Ҷľ��", "��������", "���ô���ľ",
			"�����", "��ɽӣ", "�ܽ�", "����montana", "������", "ˮ��", "����", "����stellata",
			"����", "����", "���廨", "������", "ë����", "����", "����", "����÷", "���" ,
			"��ë�ž�","���","��ɽ�","��ɽ����","�������","ʼ����","��","������","������",
			"��ľ","����"};
	private ListView listView;
	public static int[] result = new int[8];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultlayout);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int sum = 0;
		for (int i = 0; i < result.length; i++) {
			sum += result[i];
		}
		if (sum == 0) {
			// ��result����ֵ
			for (int i = 0; i < result.length; i++) {
				result[i] = i;
			}
		}
//		System.out.println("�յ����Ϊ��");
//		for (int i = 0; i < result.length; i++) {
//			System.out.println(result[i]);
//		}
		// ��Ӧ��Ŀ
		listView = (ListView) findViewById(R.id.listView);
		int resId;
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < result.length; i++) {
			resId = getResources().getIdentifier("pic_" + result[i],
					"drawable", "com.example.tab4");
			// System.out.println("ִ�д�����" + i + "��ԴID��" + resId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pic", resId);
			map.put("plant_name", LeafList[result[i]]);
			list.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(ResultTabActivity.this, list,
				R.layout.adapter, new String[] { "pic", "plant_name" },
				new int[] { R.id.pic, R.id.plant_name });
		listView.setAdapter(adapter);
		//new Thread(new RefreshThread()).start();
	}

//	class RefreshThread implements Runnable {
//		public void run() {
//			while (!Thread.currentThread().isInterrupted()) {
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					Thread.currentThread().interrupt();
//				}
//				// ʹ��postInvalidate����ֱ�����߳��и��½���
//				listView.postInvalidate();
//			}
//		}
//	}

}
