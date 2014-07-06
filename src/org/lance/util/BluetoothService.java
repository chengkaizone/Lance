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
 * 连接某一个设备只是用于对话---发送文件不需要设备信息
 * 
 * @author lance
 * 
 */
public class BluetoothService {
	// 唯一序列ID
	private static final UUID WALL_UUID = UUID
			.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

	private Context context;
	private static BluetoothAdapter mAdapter;
	private final Handler mHandler;
	private ConnectThread connectThread;
	private int state;
	// 蓝牙连接的三种状态
	public static final int STATE_NONE = 0;
	public static final int STATE_CONNECTING = 1;
	public static final int STATE_CONNECTED = 2;

	/**
	 * 创建蓝牙服务
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
	 * 开启蓝牙
	 */
	public synchronized void enable() {
		mAdapter.enable();
	}

	/**
	 * 关闭蓝牙
	 */
	public synchronized void disable() {
		mAdapter.disable();
	}

	/**
	 * 允许被发现指定时间
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
	 * 取消发现
	 */
	public synchronized void cancelDiscovery() {
		mAdapter.cancelDiscovery();
	}

	/**
	 * 保证一个返回蓝牙适配器
	 * 
	 * @return
	 */
	public BluetoothAdapter getAdapter() {
		if (mAdapter == null) {
			mAdapter = BluetoothAdapter.getDefaultAdapter();
		}
		return mAdapter;
	}

	/** 发送文件 */
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
			Toast.makeText(context, "文件不存在!", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 连接失败
	 */
	private void connectionFailed() {
		setState(STATE_NONE);
		Message msg = mHandler.obtainMessage(0x111);
		Bundle bundle = new Bundle();
		bundle.putString("toast", "连接失败!");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}

	/**
	 * 连接丢失
	 */
	private void connectionLost() {
		setState(STATE_NONE);
		Message msg = mHandler.obtainMessage(0x111);
		Bundle bundle = new Bundle();
		bundle.putString("toast", "连接丢失!");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
	}

	/**
	 * 发送文件
	 * 
	 * @param filePath
	 */
	public synchronized void sendFile(BluetoothDevice device, String filePath) {
		if (connectThread == null) {
			connectThread = new ConnectThread(device);
		}
		connectThread.sendFile(filePath);
	}

	// 设置状态
	private synchronized void setState(int state) {
		this.state = state;
		mHandler.obtainMessage(0x111, state, -1).sendToTarget();

	}

	/**
	 * 获取当前状态
	 * 
	 * @return
	 */
	public synchronized int getState() {
		return state;
	}

	/**
	 * 启动线程连接设备---或者配对
	 * 
	 * @param socket
	 * @param device
	 */
	public synchronized void connect(BluetoothDevice device) {
		// 首先取消线程
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}
		// 创建套接字
		connectThread = new ConnectThread(device);
		// 建立连接
		connectThread.start();
		setState(STATE_CONNECTING);
	}

	/**
	 * 停止
	 */
	public synchronized void stop() {
		if (connectThread != null) {
			connectThread.cancel();
			connectThread = null;
		}
		setState(STATE_NONE);
	}

	/**
	 * 该线程用于尝试连接设备
	 * 
	 * @author Administrator
	 * 
	 */
	private class ConnectThread extends Thread {
		private final BluetoothSocket socket;

		// 创建可以连接的套接字
		public ConnectThread(BluetoothDevice device) {
			BluetoothSocket temp = null;
			try {
				// 指向一个连接
				temp = device.createRfcommSocketToServiceRecord(WALL_UUID);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.socket = temp;
		}

		public void run() {
			// 首先取消发现---不然会减少连接
			mAdapter.cancelDiscovery();
			try {
				// 该方法用于连接或配对---未配对则进行配对
				socket.connect();
			} catch (Exception e) {
				e.printStackTrace();
				// 通知UI线程连接失败!
				connectionFailed();
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// 如果连接失败重新连接
				this.start();
				return;
			}
			// 建立连接后开始连接设备
			setState(STATE_CONNECTED);
		}

		/** 发送文件 */
		public synchronized void sendFile(final String filePath) {
			if (socket == null) {
				Toast.makeText(context, "连接未建立!", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(context, "文件不存在!", Toast.LENGTH_SHORT).show();
			}
		}

		// 关闭连接
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
