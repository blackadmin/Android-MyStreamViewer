package com.matrixdev.blackadmin.mylivetv.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.matrixdev.blackadmin.mylivetv.StreamBean;

public class ServiceGetStreamsDetail extends IntentService {
	public static final String STREAM_SERVER_ADDRESS = "blackadmin.no-ip.org";
	//public static final String STREAM_SERVER_ADDRESS = "192.168.1.13";
	public static final int STREAM_SERVER_PORT = 13000;

	public ServiceGetStreamsDetail() {
		super("ServiceGetStreamsDetail");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			Messenger messenger = (Messenger) bundle.get("messenger");

			try {
				List<StreamBean> streamList = new ArrayList<StreamBean>();
				Socket socket = new Socket(STREAM_SERVER_ADDRESS,
						STREAM_SERVER_PORT);
				ObjectInputStream inputStream = new ObjectInputStream(
						socket.getInputStream());
				JSONArray jsonArray = new JSONArray(inputStream.readObject()
						.toString());
				Message message = Message.obtain();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String displayName = jsonObject.getString("displayName");
					String protocol = jsonObject.getString("protocol");
					String serverAddress = jsonObject
							.getString("serverAddress");
					int serverPort = jsonObject.getInt("serverPort");
					String description = jsonObject.getString("description");
					String category = jsonObject.getString("category");
					String name = jsonObject.getString("name");
					boolean status = jsonObject.getBoolean("status");

					streamList.add(new StreamBean(displayName, protocol,
							serverAddress, serverPort, description, category,
							name, status));
				}

				/*
				 * Feeling bad with following code style... its temp
				 */
				message.obj = streamList;
				inputStream.close();
				socket.close();
				messenger.send(message);
			} catch (IOException ex) {
				System.out.println(ex);
			} catch (ClassNotFoundException ex) {
				System.out.println(ex);
			} catch (RemoteException ex) {
				System.out.println(ex);
			} catch (JSONException ex) {
				System.out.println(ex);
			}
		}

	}
}
