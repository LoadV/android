package com.example.smiledome;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {
	private List<Smile> smiles = null; 
	private GridView emoj;
	private SmiliesEditText messageContent;
	private Button face;
	private boolean flag = false; // �������ʾ״̬
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		emoj = (GridView) findViewById(R.id.emoj);
		// �������鱻�������һ��
		emoj.setOnItemClickListener(gridViewFaceItemClickListener);
		messageContent = (SmiliesEditText) findViewById(R.id.EditText1);
		//messageContent.insertIcon(R.drawable.emo_001);
		
		face = (Button) findViewById(R.id.face);
		face.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!flag) {
					// �������
					flag = true;
					emoj.setVisibility(View.VISIBLE);
					face.setText("�ر�");
					messageContent.setVisibility(View.VISIBLE);
				} else {
					flag = false;
					emoj.setVisibility(View.GONE);
					messageContent.setVisibility(View.VISIBLE);
					face.setText("����");
				}
			}
		});
		
		initGridView();
	}
	
	private void initGridView() {
		try {
			InputStream inputStream = this.getResources().getAssets().open("brow.xml"); // ȡ��assets�е�borw.xml�ļ�
			smiles = ParserBrowXml.getInfo(inputStream); // ����borw.xml
			addexpression(this, smiles, emoj);// �����������ķ���
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addexpression(Context context, List<Smile> smiles, GridView gridView) throws Exception {
		// ͨ���������Դ�ļ��е�ͼƬȡ��������GridView��
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < smiles.size(); i++) {
			Smile smile = smiles.get(i);
			if (smile != null) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				Field f = (Field) R.drawable.class.getDeclaredField(smile.getName());
				int j = f.getInt(R.drawable.class);
				map.put("ItemImage", j);// ���ͼ����Դ��ID
				lstImageItem.add(map);
			}
		}

		// ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ
		SimpleAdapter saImageItems = new SimpleAdapter(context, lstImageItem,// ������Դ
				R.layout.brow_item,
				// ��̬������ImageItem��Ӧ������
				new String[] { "ItemImage" },
				// ImageItem��XML�ļ������һ��ImageView
				new int[] { R.id.iv_brow });
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));// ������GridView������ڵ����
		gridView.setAdapter(saImageItems);
	}
	
	private OnItemClickListener gridViewFaceItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
			// ���ȵõ���ǰ�û�����ı������Ϣ
			Smile smile = smiles.get(position);
			// �õ���ǰCURSORλ��
			int cursor = messageContent.getSelectionStart();
			Field f;
			try {
				// ������Դ���ֵõ�Resource�Ͷ�Ӧ��Drawable
				f = (Field) R.drawable.class.getDeclaredField(smile.getName());
				int j = f.getInt(R.drawable.class);
				Drawable d = getResources().getDrawable(j);
				d.setBounds(0, 0, 30, 30);// ���ñ���ͼƬ����ʾ��С
				// ��ʾ��EditText��
				String str = "img";
				SpannableString ss = new SpannableString(str);
				ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BOTTOM);
				ss.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				messageContent.getText().insert(cursor, ss);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
}
