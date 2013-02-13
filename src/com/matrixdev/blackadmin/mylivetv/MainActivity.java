package com.matrixdev.blackadmin.mylivetv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.matrixdev.blackadmin.mylivetv.client.ServiceGetStreamsDetail;

public class MainActivity extends ListActivity {
	private UIHandler uiHandler;
	private ArrayList<String> streamList;
	private ArrayAdapter<String> streamListAdapter;
	private Map<String, StreamBean> mapStream;
	private ProgressDialog progressDialog;
	private Button btnExitApp;
	private Button btnRefreshList;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(android.R.id.list);
		btnExitApp = (Button) findViewById(R.id.btnExitApp);
		btnRefreshList = (Button) findViewById(R.id.btnRefreshList);

		btnExitApp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		btnRefreshList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				refreshStreamList();

			}
		});

		uiHandler = new UIHandler(this);
		listView.setOnItemClickListener(streamListItemListener);
		loadStreamList();
	}

	private void refreshStreamList() {
		loadStreamList();
	}

	private void loadStreamList() {

		progressDialog = ProgressDialog.show(this, "Loading ...",
				"Please wait while stream list is loading.", true, false);
		streamList = new ArrayList<String>();
		streamListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, streamList);
		mapStream = new HashMap<String, StreamBean>();
		setListAdapter(streamListAdapter);
		Intent intentStreamDetailService = new Intent(this,
				ServiceGetStreamsDetail.class);
		intentStreamDetailService.putExtra("messenger",
				new Messenger(uiHandler));
		startService(intentStreamDetailService);
	}

	private OnItemClickListener streamListItemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			StreamBean streamBean = mapStream.get(streamListAdapter.getItem(
					position).toString());
			System.out.println(streamBean.getProtocol());
			System.out.println(streamListAdapter.getItem(position));
			String streamURL = streamBean.getFullAddress();
			Intent vlcIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(streamURL));
			vlcIntent
					.setClassName("org.videolan.vlc.betav7neon",
							"org.videolan.vlc.betav7neon.gui.video.VideoPlayerActivity");
			startActivity(vlcIntent);

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	// METHOD WHICH WILL HANDLE DYNAMIC INSERTION
	public void addStreamItem(StreamBean streamBean) {
		streamList.add(streamBean.getDisplayName());
		mapStream.put(streamBean.getDisplayName(), streamBean);
		streamListAdapter.notifyDataSetChanged();
	}

	class UIHandler extends Handler {
		private MainActivity mainActivity;

		public UIHandler(MainActivity mainActivity) {
			this.mainActivity = mainActivity;
		}

		@Override
		public void handleMessage(Message msg) {
			@SuppressWarnings("unchecked")
			List<StreamBean> streamList = (List<StreamBean>) msg.obj;
			for (StreamBean streamBean : streamList) {
				mainActivity.addStreamItem(streamBean);
			}
			progressDialog.dismiss();
			super.handleMessage(msg);
		}
	}

}
