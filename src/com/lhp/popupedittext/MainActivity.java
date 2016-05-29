package com.lhp.popupedittext;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener {

	private EditText et;
	private ImageButton ib;
	private ListView lv;
	private ArrayList<String> datas;
	private PopupWindow popupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initData();
	}

	private void initView() {
		et = (EditText) findViewById(R.id.tv);
		ib = (ImageButton) findViewById(R.id.ib);
		ib.setOnClickListener(this);
	}

	private void initData() {
		datas = new ArrayList<String>();
		for (int i = 0; i < 30; i++) {
			datas.add(i + "");
		}
	}

	private void showPopupWindow() {
		// 初始化ListView
		initListView();
		// 初始化PopupWindow
		popupWindow = new PopupWindow(lv, et.getWidth(), 500);
		/**
		 * setOutsideTouchable()要先于setFocusable()
		 * setBackgroundDrawable()会响应外部点击事件
		 */
		// 点击PopupWindow外部时隐藏PopupWindow
		popupWindow.setOutsideTouchable(true); // PopupWindow可响应外部点击事件
		popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 响应外部点击事件：设置空白背景
		// PopupWindow可获焦点
		popupWindow.setFocusable(true);
		// 将该PopupWindow显示在指定控件下方
		popupWindow.showAsDropDown(et);
	}

	private void initListView() {
		lv = new ListView(this);
		lv.setDividerHeight(0); // 去掉分割线
		lv.setBackgroundResource(R.drawable.listview_background);
		lv.setAdapter(new MyListViewAdapter());
		lv.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		showPopupWindow();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String data = datas.get(position); // 获取点击处的文本
		et.setText(data); // 设置要显示的文本
		popupWindow.dismiss(); // 隐藏PopupWindow
	}

	class MyListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) { // converView为空时才去创建View对象
				view = View.inflate(getApplicationContext(), R.layout.item_number, null);
			} else {
				view = convertView;
			}
			TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
			tv_number.setText(datas.get(position));

			view.findViewById(R.id.ib_delete).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					/**
					 * 注：position参数处于变化中，不能使用中间变量来获取position，再传给datas。
					 * 这里应使用原始的position来定位，且需修改position参数为final。
					 */
					datas.remove(position); // 数据源中删除该位置对应的数据
					notifyDataSetChanged(); // 刷新ListView
					if (datas.size() == 0) {
						popupWindow.dismiss(); // 表中无数据时隐藏PopupWindow
					}
				}
			});
			return view;
		}

	}

}
