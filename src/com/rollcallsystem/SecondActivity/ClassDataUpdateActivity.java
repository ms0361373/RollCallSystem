package com.rollcallsystem.SecondActivity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.rollcallsystem.R;
import com.ibm.mqtt.IMqttClient;
import com.ibm.mqtt.MqttException;
import com.rollcallsystem.Connector.UpdataHttpClient;
import com.rollcallsystem.CustomInterface.DiaLogLeaveStyle;
import com.rollcallsystem.CustomInterface.DiaLogPubMessage;
import com.rollcallsystem.CustomInterface.ProgressBarStyle;
import com.rollcallsystem.CustomInterface.ToastStyle;
import com.rollcallsystem.DB.Column.UserColumn;
import com.rollcallsystem.DB.DAO.SeasonYearDAO;
import com.rollcallsystem.DB.Manage.CurriculumManager;
import com.rollcallsystem.DB.Manage.RollCall_DateManage;
import com.rollcallsystem.DB.Manage.SeasonYearManager;
import com.rollcallsystem.DB.Manage.StudentManager;
import com.rollcallsystem.DB.Manage.UserManager;
import com.rollcallsystem.DB.VO.CurriculumVO;
import com.rollcallsystem.DB.VO.RollCall_DateVO;
import com.rollcallsystem.DB.VO.SeasonYearVO;
import com.rollcallsystem.DB.VO.StudentVO;
import com.rollcallsystem.DB.VO.UserVO;
import com.rollcallsystem.FirstActivity.RegisterActivity;
import com.rollcallsystem.MQTT.PushService;
import com.rollcallsystem.spf.manage.SPFManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ClassDataUpdateActivity extends Activity {

	private static final String LOG = "ThirdActivity";
	private Button[] BT = new Button[5];
	private Context context = ClassDataUpdateActivity.this;
	private CurriculumManager curriculumManager;
	private UserManager Usermanager;
	private StudentManager Studentmanager;
	private SeasonYearManager seasonyearmanager;
	private DiaLogPubMessage DPM;
	private ToastStyle TS;
	private SPFManager Spfmanager;
	private DiaLogLeaveStyle DLS;
	private View view;
	private ProgressBarStyle PBS;
	private RollCall_DateManage rollCall_DateManage;
	private List<RollCall_DateVO> rollCall_Date;
	private String LoginUserName, mDeviceID;
	private Thread UpData, ClientTest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Log.i(LOG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.third_activity);

		LayoutInflater inflater = getLayoutInflater();
		view = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout));

		try {
			curriculumManager = new CurriculumManager(context);
			TS = new ToastStyle();
			Spfmanager = new SPFManager(context);
			Usermanager = new UserManager(context, Spfmanager);
			PBS = new ProgressBarStyle(context);
			Studentmanager = new StudentManager(context);
			seasonyearmanager = new SeasonYearManager(context);
			rollCall_DateManage = new RollCall_DateManage(context);
			mDeviceID = "123";
			// Start_Push_Server();

			List<SeasonYearVO> SeasonYearvo = seasonyearmanager.getAllSeasonYear();
			List<CurriculumVO> Curriculumvo = curriculumManager.getAllCurriculum();

			for (int i = 0; i < SeasonYearvo.size(); i++) {
				Log.i(LOG, "SeasonYear >> " + SeasonYearvo.get(i).getSeasonYear_SEASON());
//				if (i == 0)
//					seasonyearmanager.delete(SeasonYearvo.get(i));
			}

			for (int i = 0; i < Curriculumvo.size(); i++) {
				Log.i(LOG, "CLASS >> " + Curriculumvo.get(i).getCurriculum_CLASS());
				Log.i(LOG, "HOUR >> " + Curriculumvo.get(i).getCurriculum_HOURS());
				Log.i(LOG, "ID >> " + Curriculumvo.get(i).getCurriculum_ID());
				Log.i(LOG, "INSTRUCTORS >> " + Curriculumvo.get(i).getCurriculum_INSTRUCTORS());
				Log.i(LOG, "NAME >> " + Curriculumvo.get(i).getCurriculum_NAME());
				Log.i(LOG, "SEASON >> " + Curriculumvo.get(i).getCurriculum_SEASON());
				Log.i(LOG, "STD_Data >> " + Curriculumvo.get(i).getCurriculum_STD_Data());
				
//				if(Curriculumvo.get(i).getCurriculum_SEASON().equals("105-1")){
//					curriculumManager.delete(Curriculumvo.get(i));
//				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		UserVO user = Usermanager.getUserById(Integer.valueOf(Spfmanager.load(UserColumn.TABLE_NAME + UserColumn._ID)));
		setTitle("點名系統-" + user.getUSER_NAME());
		Buttoninto();

	}

	private void checkUpdate() {
		rollCall_Date = rollCall_DateManage.getAllRollCall_Date();
		int count = 0;

		for (int i = 0; i < rollCall_Date.size(); i++) {
			if (rollCall_Date.get(i).isISUPDATE() == false) {
				count = count + 1;
			}
		}
		if (count > 0) {
			DLS = new DiaLogLeaveStyle(context, "目前尚有" + count + "筆資料未上傳，是否上傳?");
			DLS.setButton(Listener_checkUpdate);
			DLS.show();
		}
	}

	private void Start_Push_Server() {
		Log.i(LOG, "Start_Push_Server >>>");
		Editor editor = getSharedPreferences(PushService.TAG, MODE_PRIVATE).edit();
		editor.putString(PushService.PREF_DEVICE_ID, mDeviceID);
		editor.commit();
		PushService.actionStart(getApplicationContext());

	}

	private void Buttoninto() {
		Log.i(LOG, "Buttoninto");
		BT[0] = (Button) findViewById(R.id.BT_ClassRollCall);
		BT[1] = (Button) findViewById(R.id.BT_DataManager);
		BT[2] = (Button) findViewById(R.id.BT_UserData);
		BT[3] = (Button) findViewById(R.id.BT_DataUpdata);
		BT[4] = (Button) findViewById(R.id.BT_SystemUpdata);

		for (int i = 0; i < BT.length; i++) {
			BT[i].setOnClickListener(OnClick);
		}

	}

	private OnClickListener OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			UserVO LogInuser = Usermanager.getLoginUser();
			LoginUserName = LogInuser.getUSER_NAME();
			switch (v.getId()) {
			case R.id.BT_ClassRollCall:

				if (curriculumManager.getCurriculumCheck(LogInuser.getUSER_NAME())) {
					Intent(context, ClassRollCallActivity.class);
				} else {
					TS.ToastSet("無資料請進行更新", context, view, Toast.LENGTH_SHORT);
				}

				break;
			case R.id.BT_DataManager:

				if (curriculumManager.getCurriculumCheck(LogInuser.getUSER_NAME())) {
					Intent(context, ClassDataManage.class);
				} else {
					TS.ToastSet("無資料請進行更新", context, view, Toast.LENGTH_SHORT);
				}
				break;
			case R.id.BT_UserData:
				Intent(context, UserDataActivity.class);
				break;
			case R.id.BT_DataUpdata:
				Intent(context, DataUpdateActivity.class);
				break;
			case R.id.BT_SystemUpdata:
				List<CurriculumVO> DATA = curriculumManager.getCurriculumByUserName(LoginUserName);
				if (DATA.size() < 1) {
					DLS = new DiaLogLeaveStyle(context, "是否進行更新?");
					DLS.setButton(Listener);
					DLS.show();
				} else {
					TS.ToastSet("系統已有資料", context, view, Toast.LENGTH_SHORT);
				}
				break;
			case R.id.BT_Pub:
				String[] Name = DPM.getSpText().split("\t");
				List<CurriculumVO> ListCu = curriculumManager.getCurriculumByClassName(Name[0], Name[1], Name[2]);
				try {
					JSONArray stdData = new JSONArray(ListCu.get(0).getCurriculum_STD_Data());

					for (int i = 0; i < stdData.length(); i++) {
						JSONObject jsonData = stdData.getJSONObject(i);
						try {
							Log.i(LOG, "Mqtt Message >>" + DPM.getText());
							publishToTopicMessage(jsonData.getString("st_number"),
									DPM.getSpText() + ":" + DPM.getText());
						} catch (MqttException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

				break;
			case R.id.IB_hide:
				DPM.cancel();
				break;
			}

		}

	};

	public void publishToTopicMessage(String ID, String message) throws MqttException {
		IMqttClient I = PushService.getMqttClient();
		I.publish("Message/" + ID, message.getBytes(), PushService.MQTT_QUALITY_OF_SERVICE,
				PushService.MQTT_RETAINED_PUBLISH);
	}

	private Button.OnClickListener Listener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.BT_YES:
				DLS.cancel();
				ConnectionTest();
			case R.id.BT_NO:
				DLS.cancel();
				break;
			}

		}
	};

	private Button.OnClickListener Listener_checkUpdate = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.BT_YES:
				DLS.cancel();
				Intent I = new Intent();
				I.setClass(ClassDataUpdateActivity.this, DataUpdateActivity.class);
				startActivity(I);
			case R.id.BT_NO:
				DLS.cancel();
				break;
			}

		}
	};

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
					Clienttest.sendEmptyMessage(0);
					Log.i(LOG, "連線測試   End>>");
				} else if (ConnectionTest.toString().trim().equals("-1")) {
					Log.i(LOG, "Connection Fail! >>");
					PBS.cancel();
					toast.sendEmptyMessage(0);
				}
			}
		});
		ClientTest.start();
	}

	Handler toast = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			TS.ToastSet("連線異常", context, view, Toast.LENGTH_LONG);
		}
	};

	/**
	 * 資料更新
	 * 
	 * @param User
	 */
	private void PBSThread(final String User) {
		PBS.setText("更新中..請稍後...");
		UpData = new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i(LOG, "Thread Start>>");

				Log.i(LOG, "SystemUpdata Start>>");

				Log.i(LOG, "getInternetData Start>>");
				String SystemUpdata = UpdataHttpClient.executeQuery(User, "1");
				List<JSONObject> Std_Data = new ArrayList<JSONObject>();
				try {
					JSONArray jsonArray = new JSONArray(SystemUpdata);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonData = jsonArray.getJSONObject(i);
						Std_Data.add(jsonData);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.i(LOG, "Std_Data Size>>" + Std_Data.size());
				Log.i(LOG, "getInternetData End>>");

				Log.i(LOG, "getDataBaseData Start>>");

				Log.i(LOG, "getSEASON Start>>");
				ArrayList<String> Season = new ArrayList<String>();
				String getSeasonByUserName = UpdataHttpClient.executeQuery(User, "5");
				try {
					JSONArray jsonArray = new JSONArray(getSeasonByUserName);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonData = jsonArray.getJSONObject(i);
						Season.add(jsonData.getString("year"));
					}
					Log.i(LOG, "Std_Data>>" + Season.get(0).toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.i(LOG, "getSEASON End>>");

				Log.i(LOG, "CurriculumVO Save Start >>");
				boolean SyLock = false;
				List<SeasonYearVO> seasonYear = seasonyearmanager.getAllSeasonYear();
				if (seasonYear.size() > 1) {
					SyLock = true;
					Log.i(LOG, "seasonYear is have Data >>");
				} else {
					for (int i = 0; i < Season.size(); i++) {
						SeasonYearVO SeasonYear = new SeasonYearVO();
						Log.i(LOG, "學期學年度儲存  >>" + Season.get(i));
						SeasonYear.setSeasonYear_SEASON(Season.get(i));
						seasonyearmanager.add(SeasonYear);
						if (i == Season.size() - 1) {
							SyLock = true;
						}
					}
				}

				Log.i(LOG, "CurriculumVO Save End >>");
				Log.i(LOG, "getCurriculum Start >>");

				String getCurriculum = UpdataHttpClient.executeQuery(User, "2");
				boolean CuLock = false;
				List<String> CuName = new ArrayList<String>();
				List<String> CuID = new ArrayList<String>();
				List<String> CuClass = new ArrayList<String>();
				List<String> CuHOURS = new ArrayList<String>();
				List<String> CuSEASON = new ArrayList<String>();
				List<String> StdData = new ArrayList<String>();

				try {
					JSONArray jsonArray = new JSONArray(getCurriculum);
					Log.i(LOG, "KEY>>" + jsonArray.length());
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonData = jsonArray.getJSONObject(i);
						String CurriculumName = jsonData.getString("cu_name");
						String CurriculumID = jsonData.getString("cu_number");
						String CurriculumClass = jsonData.getString("class_name");
						String CurriculumHOURS = jsonData.getString("CourseHours");
						String CurriculumSEASON = jsonData.getString("year");

						CuName.add(CurriculumName);
						CuID.add(CurriculumID);
						CuClass.add(CurriculumClass);
						CuHOURS.add(CurriculumHOURS);
						CuSEASON.add(CurriculumSEASON);
						Log.i(LOG, "Std_Data>>"
								+ Std_Data.get(i).get(CurriculumName + CurriculumClass + CurriculumSEASON).toString());
						Log.i(LOG, "KEY>>" + CurriculumName + CurriculumClass + CurriculumSEASON);
						for (int x = 0; x < Std_Data.size(); x++) {
							try {
								Log.i(LOG, "Std_Data>>" + Std_Data.get(x)
										.get(CurriculumName + CurriculumClass + CurriculumSEASON).toString());
								Log.i(LOG, "Std_Data add Success>>"
										+ Std_Data.get(x).get(CurriculumName + CurriculumClass + CurriculumSEASON));
								StdData.add("null".equals(Std_Data.get(x)
										.get(CurriculumName + CurriculumClass + CurriculumSEASON).toString())
												? "無資料"
												: Std_Data.get(x)
														.get(CurriculumName + CurriculumClass + CurriculumSEASON)
														.toString());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Log.i(LOG, "getCurriculum End>>");

				Log.i(LOG, "CurriculumVO Save Start >>");

				Log.i(LOG, "課程名稱儲存 >>" + CuName.size());
				Log.i(LOG, "課程名稱儲存 >>" + CuID.size());
				Log.i(LOG, "課程名稱儲存 >>" + CuClass.size());
				Log.i(LOG, "課程名稱儲存 >>" + CuHOURS.size());
				Log.i(LOG, "課程名稱儲存 >>" + CuSEASON.size());
				Log.i(LOG, "課程名稱儲存 >>" + StdData.size());

				for (int i = 0; i < CuName.size(); i++) {
					CurriculumVO Curriculum = new CurriculumVO();
					Curriculum.setCurriculum_INSTRUCTORS(User);
					Log.i(LOG, "課程名稱儲存 >>" + CuName.get(i));

					Curriculum.setCurriculum_NAME(CuName.get(i));
					Curriculum.setCurriculum_ID(CuID.get(i));
					Curriculum.setCurriculum_CLASS(CuClass.get(i));
					Curriculum.setCurriculum_HOURS(CuHOURS.get(i));
					Curriculum.setCurriculum_SEASON(CuSEASON.get(i));
					Curriculum.setCurriculum_STD_Data(StdData.get(i));
					Log.i(LOG, "Curriculum status >>" + curriculumManager.add(Curriculum));
					Log.i(LOG, "Curriculum getCurriculum_STD_Data >>" + Curriculum.getCurriculum_CLASS());
					Log.i(LOG, "Curriculum getCurriculum_STD_Data >>" + Curriculum.getCurriculum_SEASON());
					Log.i(LOG, "Curriculum getCurriculum_STD_Data >>" + Curriculum.getCurriculum_STD_Data());

					if (i == CuName.size() - 1) {
						CuLock = true;
					}
				}

				Log.i(LOG, "CurriculumVO Save End >>");

				if (!SystemUpdata.equals("") && SyLock && CuLock == true) {
					Log.i(LOG, "SystemUpdata End>>");
					myHandle.sendEmptyMessage(0);
					Looper.prepare();
					TS.ToastSet("更新完畢！", context, view, Toast.LENGTH_SHORT);
					Log.i(LOG, "Thread End>>");
					Looper.loop();
				}

			}
		});
		UpData.start();

	}

	Handler myHandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			PBS.cancel();
		}
	};
	Handler Clienttest = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			PBSThread(LoginUserName);
		}
	};

	@SuppressWarnings("rawtypes")
	private void Intent(Context C, Class S) {
		Log.i(LOG, "Intent Start");
		Intent i = new Intent();
		i.setClass(C, S);
		C.startActivity(i);

	}

	@Override
	protected void onDestroy() {
		Spfmanager.remove(UserColumn.TABLE_NAME + UserColumn._ID);
		PushService.actionStop(getApplicationContext());
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences p = getSharedPreferences(PushService.TAG, MODE_PRIVATE);
		checkUpdate();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.actbar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_settings:
			openAction_settings();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void openAction_settings() {
		DPM = new DiaLogPubMessage(context, LoginUserName);
		DPM.setButton(OnClick);
		DPM.show();
	}

}
