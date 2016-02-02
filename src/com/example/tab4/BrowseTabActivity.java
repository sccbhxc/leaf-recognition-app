package com.example.tab4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class BrowseTabActivity extends Activity {
	private String LeafList[] = { "˨Ƥ��", "������", "��צ��", "������", "�Ƿ�", "ƽ�����",
			"��������", "���ô��Ͼ�", "����������", "ɽ����", "�", "��ʢ��ɽ�", "������", "����", "�����ľ",
			"�޻���", "����", "�Һ���", "����������", "���������", "��Ҷľ��", "��������", "���ô���ľ",
			"�����", "��ɽӣ", "�ܽ�", "����montana", "������", "ˮ��", "����", "����stellata",
			"����", "����", "���廨", "������", "ë����", "����", "����", "����÷", "���", "��ë�ž�",
			"���", "��ɽ�", "��ɽ����", "�������", "ʼ����", "��", "������", "������", "��ľ", "����" };
	private ListView listView;
	private int[] order = new int[51];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browselayout);

		// ��result����ֵ
		for (int i = 0; i < order.length; i++) {
			order[i] = i;
		}
		// ��Ӧ��Ŀ
		listView = (ListView) findViewById(R.id.listView);
		int resId;
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < order.length; i++) {
			resId = getResources().getIdentifier("pic_" + order[i], "drawable",
					"com.example.tab4");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pic", resId);
			map.put("plant_name", LeafList[order[i]]);
			list.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(BrowseTabActivity.this, list,
				R.layout.adapter, new String[] { "pic", "plant_name" },
				new int[] { R.id.pic, R.id.plant_name });
		listView.setAdapter(adapter);
	}
}
