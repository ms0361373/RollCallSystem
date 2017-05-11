package com.rollcallsystem.nfc.manage;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.os.StrictMode;

public class NfcManage {
	private NfcAdapter mAdapter;
	private PendingIntent mPendingIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressLint("NewApi")
	public void NFCManager(NfcManager manager,Context context){
		try {
			 mAdapter = manager.getDefaultAdapter();
			 mPendingIntent = PendingIntent.getActivity(context, 0,
			 new Intent()
			 .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			 IntentFilter ndef = new IntentFilter(
			 NfcAdapter.ACTION_TECH_DISCOVERED);
			 mFilters = new IntentFilter[] { ndef, };
			 mTechLists = new String[][] { new String[] { NfcA.class.getName() }
			 };
			 } catch (Exception e) {
			 e.printStackTrace();
			 }
			
			 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
			 .detectDiskReads().detectDiskWrites().detectNetwork()
			 .penaltyLog().build());
			 StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			 .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
			 .build());
			 }
	/**
	 * onPause()
	 * @param activity
	 */
	@SuppressLint("NewApi")
	public void disableForegroundDispatch(Activity activity){
		if (mAdapter != null && mAdapter.isEnabled()) {
		mAdapter.disableForegroundDispatch(activity);
		}
	}
	
	/**
	 * onResume()
	 * @param activity
	 */
	@SuppressLint("NewApi")
	public void enableForegroundDispatch(Activity activity){
		if (mAdapter != null && mAdapter.isEnabled()) {
		mAdapter.enableForegroundDispatch(activity, mPendingIntent, mFilters,mTechLists);
		}
	}
	}
	
	

	

