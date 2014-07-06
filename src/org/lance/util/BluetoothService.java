package org.lance.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * ����ĳһ���豸ֻ�����ڶԻ�---�����ļ�����Ҫ�豸��Ϣ
 * 
 * @author lance
 * 
 */
public class BluetoothService {
	// Ψһ����ID
	private static final UUID WALL_UUID = UUID
			.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

	private Context context;
	private static BluetoothAdapter mAdapter;
	private final Handler mHandler;
	private ConnectThread connectThread;
	private int state;
	// �������ӵ�����״̬
	public static final int STATE_NONE = 0;
	public static final int STATE_CONNECTING = 1;
	public static final int STATE_CONNECTED = 2;

	/**
	 * ������������
	 * 
	 * @param context
	 * @param handler
	 */
	public BluetoothService(Context context, Handler handler) {
		this.context = context;
		state = STATE_NONE;
		this.mHandler = handler;
		mAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	/**
	 * ��������
	 */
	public synchronized void enable() {
		mAdapter.enable();
	}

	/**
	 * �ر�����
	 */
	public synchronized void disable() {
		mAdapter.disable();
	}

	/**
	 * ��������ָ��ʱ��
	 * 
	 * @param time
	 */
	public synchronized void allowDiscoverable(int time) {
		Intent converIntent = new Intent(
				BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		converIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
				time);
		context.startActivity(converIntent);
	}

	/**
	 * ȡ������
	 */
	public synchronized void cancelDiscovery() {
		mAdapter.cancelDiscovery();
	}

	/**
	 * ��֤һ����������������
	 * 
	 * @return
	 */
	public BluetoothAdapter getAdapter() {
		if (mAdapter == null) {
			mAdapter = BluetoothAdapter.getDefaultAdapter();
		}
		return mAdapter;
	}

	/** �����ļ� */
	public void sendFile(String filePath) {
		if (new File(filePath).exists()) {
			File localFile = new File(filePath);
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("*/*");
			intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(localFile));
			int k = PackageManager.MATCH_DEFAULT_ONLY;
			if (context.getPackageManager().queryIntentActivities(intent, k)
					.size() > 0) {
				context.startActivity(intent);
			}
		} else {
			Toast.makeText(context, "�ļ�������!", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * ����ʧ��
	 */
	private void connectionFailed() {
		setState(STATE_NONE);
		Message msg = mHandler.obtainMessage(0x111);
		Bundle bundle = new Bundle();
		bundle.putString("toast", "����ʧ��!");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}

	/**
	 * ���Ӷ�ʧ
	 */
	private void connectionLost() {
		setState(STATE_NONE);
		Message msg = mHandler.obtainMessage(0x111);
		Bundle bundle = new Bundle();
		bundle.putString("toast", "���Ӷ�ʧ!");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}

	/**
	 * �����ļ�
	 * 
	 * @param filePath
	 */
	public synchronized void sendFile(BluetoothDevice device, String filePath) {
		if (connectThread == null) {
			connectThread = new ConnectThread(device);
		}
		connectThread.sendFile(filePath);
	}

	// ����״̬
	private synchronized void setState(int state) {
		this.state = state;
		mHandler.obtainMessage(0x111, state, -1).sendToTarget();

	}

	/**
	 * ��ȡ��ǰ״̬
	 * 
	 * @return
	 */
	public synchronized int getState() {
		return state;
	}

	/**
	 * �����߳������豸---�������
	 * 
	 * @param socket
	 * @param device
	 */
	public synchronized void connect(BluetoothDevice device) {
		// ����ȡ���߳�
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}
		// �����׽���
		connectThread = new ConnectThread(device);
		// ��������
		connectThread.start();
		setState(STATE_CONNECTING);
	}

	/**
	 * ֹͣ
	 */
	public synchronized void stop() {
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}
		setState(STATE_NONE);
	}

	/**
	 * ���߳����ڳ��������豸
	 * 
	 * @author Administrator
	 * 
	 */
	private class ConnectThread extends Thread {
		private final BluetoothSocket socket;

		// �����������ӵ��׽���
		public ConnectThread(BluetoothDevice device) {
			BluetoothSocket temp = null;
			try {
				// ָ��һ������
				temp = device.createRfcommSocketToServiceRecord(WALL_UUID);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.socket = temp;
		}

		public void run() {
			// ����ȡ������---��Ȼ���������
			mAdapter.cancelDiscovery();
			try {
				// �÷����������ӻ����---δ�����������
				socket.connect();
			} catch (Exception e) {
				e.printStackTrace();
				// ֪ͨUI�߳�����ʧ��!
				connectionFailed();
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// �������ʧ����������
				this.start();
				return;
			}
			// �������Ӻ�ʼ�����豸
			setState(STATE_CONNECTED);
		}

		/** �����ļ� */
		public synchronized void sendFile(final String filePath) {
			if (socket == null) {
				Toast.makeText(context, "����δ����!", Toast.LENGTH_SHORT).show();
				return;
			}
			if (new File(filePath).exists()) {
				File localFile = new File(filePath);
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("*/*");
				intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(localFile));
				int k = PackageManager.MATCH_DEFAULT_ONLY;
				if (context.getPackageManager()
						.queryIntentActivities(intent, k).size() > 0) {
					context.startActivity(intent);
				}
			} else {
				Toast.makeText(context, "�ļ�������!", Toast.LENGTH_SHORT).show();
			}
		}

		// �ر�����
		public void cancel() {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
