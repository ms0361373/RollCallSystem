package com.rollcallsystem.ClassDataManageActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.rollcallsystem.R;
import com.rollcallsystem.Connector.UpdataHttpClient;
import com.rollcallsystem.CustomInterface.DiaLogStudentDataEdit;
import com.rollcallsystem.CustomInterface.ProgressBarStyle;
import com.rollcallsystem.CustomInterface.StudentDataListAdapter;
import com.rollcallsystem.CustomInterface.ToastStyle;
import com.rollcallsystem.DB.Column.UserColumn;
import com.rollcallsystem.DB.Manage.CurriculumManager;
import com.rollcallsystem.DB.Manage.UserManager;
import com.rollcallsystem.DB.VO.CurriculumVO;
import com.rollcallsystem.DB.VO.UserVO;
import com.rollcallsystem.nfc.manage.NfcManage;
import com.rollcallsystem.spf.manage.SPFManager;
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
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CardRegister extends Activity {
	
	private static final String LOG = "CardRegister";
	private View view;
	private Context context;
	private ProgressBarStyle PBS;
	private String LoginUserName;
	private SPFManager Spfmanager;
	private CurriculumManager curriculummanager;
	private UserManager usermanager;
	private ListView LV_List;
	private Thread ClientTest;
	private Button BT_UPDATA;
	private DiaLogStudentDataEdit DSDE;
	private List<CurriculumVO> curruculumData;
	private List<String> stdName,stdId,stdCardId;
	private int ListIndex=0;
	private ToastStyle TS;
	//private Boolean nfckey = false;
	private NfcManage nfcManage;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(LOG, "onCreate Start >>>");
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studentdata_cardregister);
		LayoutInflater inflater = getLayoutInflater();
		view = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout));
		try {
			context = CardRegister.this;
			TS = new ToastStyle();
			PBS = new ProgressBarStyle(context);
			curriculummanager = new CurriculumManager(context);
			Spfmanager = new SPFManager(context);
			usermanager = new UserManager(context,Spfmanager);
			UserVO user = usermanager.getUserById(Integer.valueOf(Spfmanager.load(UserColumn.TABLE_NAME + UserColumn._ID)));
			LoginUserName = user.getUSER_NAME();
			setTitle("點名系統-"+LoginUserName);
		} catch (Exception e) {
		}
		ListViewInto();
		ButtonInto();
		Log.i(LOG, "NfcManager Start>>");
		NfcManager();
	}

	//NFC Intent
	public void onNewIntent(Intent intent) 
	{
		byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
		DSDE.setETCardID(String.valueOf(getReversed(tagId)));
	}

	private void ListViewInto() {
		LV_List = (ListView) findViewById(R.id.LV_List);
		Bundle bundleClassName = getIntent().getExtras();
		curruculumData = new ArrayList<CurriculumVO>();
		curruculumData = curriculummanager.getCurriculumByClassName(bundleClassName.getString("cuName"), bundleClassName.getString("cuClass"), bundleClassName.getString("cuYear"));
		stdName = new ArrayList<String>();
		stdId = new ArrayList<String>();
		stdCardId = new ArrayList<String>();
		 try {
				JSONArray stdData = new JSONArray(curruculumData.get(0).getCurriculum_STD_Data());
			 for(int i = 0; i < stdData.length(); i++) {
					JSONObject jsonData = stdData.getJSONObject(i);
					stdName.add("".equals(jsonData.getString("st_name"))?"無名氏":jsonData.getString("st_name"));
					stdId.add("".equals(jsonData.getString("st_number"))?"無學號":jsonData.getString("st_number"));
					stdCardId.add("".equals(jsonData.getString("st_card"))?"尚未註冊":jsonData.getString("st_card"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch blocks
				e.printStackTrace();
			}
		 Log.i(LOG, "stdNameSize"+stdName.size());
		LV_List.setAdapter(new StudentDataListAdapter(context, stdName,stdId,stdCardId));
		LV_List.setOnItemClickListener(LVListener);
		
	}
	 
	private ListView.OnItemClickListener LVListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			String Student = LV_List.getItemAtPosition(position).toString();
			ListIndex = position; 
			DSDE = new DiaLogStudentDataEdit(context, stdName.get(position), stdId.get(position), stdCardId.get(position));
			DSDE.setETCardID(stdCardId.get(position));
			DSDE.setButton(BTListener);
			DSDE.show();
		}
	};
	
	private void ButtonInto() {
		BT_UPDATA = (Button) findViewById(R.id.BT_UPDATA);
		BT_UPDATA.setOnClickListener(BTListener);
	}
	
	
	private  Button.OnClickListener BTListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		switch (v.getId()) {
		case R.id.BT_UPDATA:
		ConnectionTest();
		break;
		case R.id.BT_Save:
		StudentUpdata();
		
		
		break;
		}
			
		}
	};
	
	private void StudentUpdata() 
	{
		try {
			JSONArray stdData = new JSONArray(curruculumData.get(0).getCurriculum_STD_Data());
		
			JSONObject newstdData = new JSONObject();
			newstdData.put("st_name", DSDE.getET()[0].getText());
			newstdData.put("st_number", DSDE.getET()[1].getText());
			newstdData.put("st_card", DSDE.getET()[2].getText());
			//stdData.remove(ListIndex);
			stdData.put(ListIndex, newstdData);
			String newStd_Data = stdData.toString();
			CurriculumVO CuData = new CurriculumVO();
			//curruculumData.get(0).setCurriculum_STD_Data(newStd_Data);
			CuData = curruculumData.get(0);
			CuData.setCurriculum_STD_Data(newStd_Data);
			curriculummanager.update(CuData);
			ListViewInto();
			//LV_List.setAdapter(new StudentDataListAdapter(context, stdName,stdId,stdCardId));
			DSDE.cancel();
		} catch (JSONException e) {
			TS.ToastSet("有問題", context, view, Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
		
	}
	
	/*
	 * 連線測試
	 */
	private void ConnectionTest() {
	
		PBS.show();
		PBS.setText("連線測試...");
		ClientTest = new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i(LOG, "連線測試   Start>>");
				String ConnectionTest = UpdataHttpClient.executeQuery(null, "4");
				Log.i(LOG, "ConnectionTest >>" + ConnectionTest);
				if (ConnectionTest.toString().trim().equals("0")) {
					Log.i(LOG, "Connection Success! >>");
					PBS.cancel();
					//Clienttest.sendEmptyMessage(0);
					Log.i(LOG, "連線測試   End>>");
				} else if (ConnectionTest.toString().trim().equals("-1")) {
					Log.i(LOG, "Connection Fail! >>");
					PBS.cancel();
					ClientTest.stop();
					TS.ToastSet("連線異常", context, view, Toast.LENGTH_LONG);
				}
			}
		});
		ClientTest.start();
	}

	private void NfcManager() {
		NfcManager manager = (NfcManager) getSystemService(NFC_SERVICE);
		nfcManage = new NfcManage();
		nfcManage.NFCManager(manager, context);
	}

	public void onPause() {
		super.onPause();
		nfcManage.disableForegroundDispatch(this);
	}

	public void onResume() {
		super.onResume();
		nfcManage.enableForegroundDispatch(this);
	}

	/**
	 * 轉換碼
	 * 
	 * @param bytes
	 * @return
	 */
	private long getReversed(byte[] bytes) {
		long result = 0;
		long factor = 1;
		for (int i = bytes.length - 1; i >= 0; --i) {
			long value = bytes[i] & 0xffl;
			result += value * factor;
			factor *= 256l;
		}
		return result;
	}
	
}
