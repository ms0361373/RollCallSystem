package com.rollcallsystem.ClassRollCallActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rollcallsystem.R;
import com.ibm.mqtt.IMqttClient;
import com.ibm.mqtt.MqttException;
import com.rollcallsystem.Connector.UpdataHttpClient;
import com.rollcallsystem.CustomInterface.AttendanceRateCalculationTool;
import com.rollcallsystem.CustomInterface.DiaLogClassDataList;
import com.rollcallsystem.CustomInterface.DiaLogLeaveStyle;
import com.rollcallsystem.CustomInterface.DiaLogPubMessage;
import com.rollcallsystem.CustomInterface.DiaLogStudentDataEdit;
import com.rollcallsystem.CustomInterface.ProgressBarStyle;
import com.rollcallsystem.CustomInterface.ToastStyle;
import com.rollcallsystem.DB.Column.UserColumn;
import com.rollcallsystem.DB.Manage.CurriculumManager;
import com.rollcallsystem.DB.Manage.RollCall_DateManage;
import com.rollcallsystem.DB.Manage.RollCall_StudentManager;
import com.rollcallsystem.DB.Manage.UserManager;
import com.rollcallsystem.DB.VO.CurriculumVO;
import com.rollcallsystem.DB.VO.RollCall_DateVO;
import com.rollcallsystem.DB.VO.RollCall_StudentVO;
import com.rollcallsystem.DB.VO.UserVO;
import com.rollcallsystem.MQTT.PushService;
import com.rollcallsystem.SecondActivity.ClassDataUpdateActivity;
import com.rollcallsystem.nfc.manage.NfcManage;
import com.rollcallsystem.spf.manage.SPFManager;

public class RollCallActivity extends Activity {

	private static final String LOG = "RollCallActivity";
	private View view;
	private Context context;
	private ProgressBarStyle PBS;
	private DiaLogLeaveStyle DLS;
	private DiaLogPubMessage DPM;
	private ArrayList<String> PCDList;
	private String ClassID, StudID, LoginUserName, CuName, CuClass, CuYear, Cuid;
	private CurriculumManager curriculummanager;
	private UserManager usermanager;
	private ToastStyle TS;
	private EditText ET_StuID;
	private LinearLayout LL_Check, LL_Date;
	private TextView[] TV = new TextView[7];
	private Button[] BT = new Button[4];
	private List<CurriculumVO> CurriculumList;
	private JSONArray stdData;
	private Date StatDate, EndDate, LateDate;
	private int ClassTimeLong;
	private RollCall_DateManage rollCall_DateManage;
	private RollCall_StudentManager rollCall_StudentManager;
	private AttendanceRateCalculationTool ARCT;
	private boolean isRunning = false;
	private boolean isShow = false;
	private SPFManager Spfmanager;
	private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private JSONObject jsonData;
	private DiaLogStudentDataEdit DSDE;
	private ImageButton[] IB = new ImageButton[3];
	private DiaLogClassDataList DCD;
	private PublishClassListDiaLog PCD;
	private List<CurriculumVO> curruculumData;
	private List<String> stdName, stdId, stdCardId;
	private Thread ClientTest, UpdateData, StartRollCallThread;
	private NfcManage nfcManage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(LOG, "onCreate Start >>>");
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rollcall_activity);
		LayoutInflater inflater = getLayoutInflater();
		view = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout));
		try {
			context = RollCallActivity.this;
			TS = new ToastStyle();
			PBS = new ProgressBarStyle(context);
			curriculummanager = new CurriculumManager(context);
			Spfmanager = new SPFManager(context);
			usermanager = new UserManager(context, Spfmanager);
			UserVO user = usermanager
					.getUserById(Integer.valueOf(Spfmanager.load(UserColumn.TABLE_NAME + UserColumn._ID)));
			LoginUserName = user.getUSER_NAME();
			rollCall_DateManage = new RollCall_DateManage(context);
			rollCall_StudentManager = new RollCall_StudentManager(context);
			setTitle("點名系統-" + LoginUserName);

		} catch (Exception e) {
		}
		Bundle bundleClassName = getIntent().getExtras();
		CuName = bundleClassName.getString("cuName");
		CuClass = bundleClassName.getString("cuClass");
		CuYear = bundleClassName.getString("cuYear");
		StudentDataInto();
		TextViewInto();
		LinearLayoutInto();
		ButtonInto();
		Log.i(LOG, "NfcManager Start>>");
		NfcManager();
	}

	private void TextViewInto() {
		TV[0] = (TextView) findViewById(R.id.TV_Title);
		TV[1] = (TextView) findViewById(R.id.TV01);
		TV[2] = (TextView) findViewById(R.id.TV02);
		TV[3] = (TextView) findViewById(R.id.TV03);
		TV[4] = (TextView) findViewById(R.id.TV_Start1);
		TV[5] = (TextView) findViewById(R.id.TV_End1);
		TV[6] = (TextView) findViewById(R.id.TV_SubTitle);
		TV[0].setText(CuName);
		TV[6].setText(CuClass);
	}

	private void StudentDataInto() {
		CurriculumList = curriculummanager.getCurriculumByClassName(CuName, CuClass, CuYear);
		try {
			stdData = new JSONArray(CurriculumList.get(0).getCurriculum_STD_Data());
			ClassTimeLong = new Integer(ClassTimeLong).valueOf(CurriculumList.get(0).getCurriculum_HOURS().toString());
			Log.i(LOG, "ClassTimeLong >>" + ClassTimeLong);
		} catch (JSONException e) {
			TS.ToastSet("錯誤!請聯絡管理員!", context, view, Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
	}

	private void LinearLayoutInto() {
		LL_Check = (LinearLayout) findViewById(R.id.LL_Check);
		LL_Date = (LinearLayout) findViewById(R.id.LL_Date);
		ET_StuID = (EditText) findViewById(R.id.ET_StuID);
	}

	private void ButtonInto() {
		BT[0] = (Button) findViewById(R.id.BT_Manually);
		BT[1] = (Button) findViewById(R.id.BT_Start);
		BT[2] = (Button) findViewById(R.id.BT_End);
		BT[3] = (Button) findViewById(R.id.BT_Check);
		IB[0] = (ImageButton) findViewById(R.id.IB);
		IB[1] = (ImageButton) findViewById(R.id.IB1);
		IB[2] = (ImageButton) findViewById(R.id.IB2);
		for (int i = 0; i < BT.length; i++) {
			BT[i].setOnClickListener(BTListener);
		}
		for (int i = 0; i < IB.length; i++) {
			IB[i].setOnClickListener(BTListener);
		}
	}

	// NFC Intent
	public void onNewIntent(Intent intent) {
		if (isShow == true) {
			try {
				byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
				DSDE.setETCardID(String.valueOf(getReversed(tagId)));
			} catch (Exception e) {

			}
		} else {
			try {
				byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
				Manually(String.valueOf(getReversed(tagId)));
			} catch (Exception e) {

			}
		}
	}

	// 開始點名Button 、 手動點名Button監聽
	private Button.OnClickListener BTListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			CurriculumManager Curriculummanager = new CurriculumManager(context);
			List<CurriculumVO> CurriculumVO = Curriculummanager.getAllCurriculum();
			switch (v.getId()) {
			case R.id.BT_Manually:
				LL_Check.setVisibility(View.VISIBLE);
				BT[0].setVisibility(View.GONE);
				IB[1].setVisibility(View.GONE);
				break;
			case R.id.BT_Start:
				DLS = new DiaLogLeaveStyle(context, "確定開始進行點名?");
				DLS.setButton(BTListener);
				DLS.show();
				break;
			case R.id.BT_End:
				DLS = new DiaLogLeaveStyle(context, "確定結束點名?");
				DLS.setButton(BTListenerByLeave);
				DLS.show();
				break;
			case R.id.BT_Check:
				LL_Check.setVisibility(View.GONE);
				BT[0].setVisibility(View.VISIBLE);
				IB[1].setVisibility(View.VISIBLE);
				try {
					Manually(ET_StuID.getText().toString());
				} catch (Exception e) {
					TS.ToastSet("錯誤!", context, view, Toast.LENGTH_SHORT);
					e.printStackTrace();
				}
				closeInputMethod();
				break;
			case R.id.BT_YES:
				BT[0].setVisibility(View.VISIBLE);
				BT[1].setVisibility(View.GONE);
				BT[2].setVisibility(View.VISIBLE);
				LL_Date.setVisibility(View.VISIBLE);
				StartRollCall();
				DLS.cancel();
				break;
			case R.id.BT_NO:
				DLS.cancel();
				break;
			case R.id.IB:
				DSDE = new DiaLogStudentDataEdit(context, "學生卡號註冊", null, null, null);
				DSDE.setButton(BTListener);
				DSDE.show();
				isShow = true;
				curruculumData = new ArrayList<CurriculumVO>();
				curruculumData = curriculummanager.getCurriculumByClassName(CuName, CuClass, CuYear);
				break;
			case R.id.IB1:
				CurriculumVO = Curriculummanager.getCurriculumByClassName(CuName, CuClass, CuYear);
				List<RollCall_StudentVO> StdData = rollCall_StudentManager.getRollCall_StudentByDateId(
						rollCall_DateManage.getRollCall_DateByDate(dateTimeFormat.format(StatDate)));
				ClassID = StdData.get(0).getRollCall_DateColumn_Curriculum_ID();// 課程ID
				Log.d("RollCall", "ClassID" + ClassID);
				DCD = new DiaLogClassDataList(context, CurriculumVO, ClassID,
						rollCall_DateManage.getRollCall_DateByDate(dateTimeFormat.format(StatDate)));
				DCD.show();
				break;
			case R.id.IB2:
				CurriculumVO = Curriculummanager.getCurriculumByClassName(CuName, CuClass, CuYear);
				PCD = new PublishClassListDiaLog(context, CurriculumVO);
				PCD.setButton(BTListener);
				PCD.show();
				break;
			case R.id.BT_Publish:
				PCDList = (ArrayList<String>) PCD.getSTID();
				Log.i("Log", "PCDList.size: >>" + PCDList.size());
				DPM = new DiaLogPubMessage(context);
				DPM.setButton(BTListener);
				DPM.show();
				break;
			case R.id.BT_Pub:
				List<CurriculumVO> CurriculumDate = Curriculummanager.getCurriculumByClassName(CuName, CuClass, CuYear);
				if (PushServicStatus() == true) {
					try {
						for (int i = 0; i < PCD.getList().size(); i++) {
							Log.i("Log", "Log: >>" + PCD.getList().size());
							Log.i("Log", "Log: >>" + Integer.valueOf(PCD.getList().get(i)));

							String ID = PCDList.get(Integer.valueOf(PCD.getList().get(i)));
							publishToTopicMessage(ID, "" + CuName + CuClass + ": 同學  " + ID + "您好 ~" + DPM.getText()
									+ "，若有問題請聯絡教師  " + LoginUserName + "");
						}

					} catch (MqttException e) {
						e.printStackTrace();
					}
				}
				break;
			case R.id.IB_hide:
				DPM.cancel();
				break;
			case R.id.BT_S:
				EditText[] e = DSDE.getET();
				try {
					JSONArray stdData = new JSONArray(curruculumData.get(0).getCurriculum_STD_Data());
					List<String> StdID = new ArrayList<String>();
					List<String> StdCd = new ArrayList<String>();
					for (int i = 0; i < stdData.length(); i++) {
						JSONObject jsonData = stdData.getJSONObject(i);
						if (e[0].getText().toString().equals(jsonData.getString("st_name"))) {
							StdID.add(jsonData.getString("st_number"));
							StdCd.add(
									"".equals(jsonData.getString("st_card")) ? "尚未註冊" : jsonData.getString("st_card"));
						}
					}
					if (StdID.size() < 1) {
						DSDE.setStdID("查無此人");
						DSDE.setETCardID("");
					} else {
						DSDE.setStdID(StdID.get(0));
						DSDE.setETCardID("".equals(StdCd.get(0)) ? "請靠卡輸入卡號" : StdCd.get(0));
					}
				} catch (JSONException s) {
					s.printStackTrace();
				}
				break;
			case R.id.BT_Save:
				try {
					JSONArray stdData = new JSONArray(curruculumData.get(0).getCurriculum_STD_Data());
					for (int i = 0; i < stdData.length(); i++) {
						JSONObject jsonData = stdData.getJSONObject(i);
						if (jsonData.getString("st_name").equals(DSDE.getET()[0].getText().toString())
								&& jsonData.getString("st_number").equals(DSDE.getET()[1].getText().toString())) {
							JSONObject newstdData = new JSONObject();
							newstdData.put("st_name", DSDE.getET()[0].getText());
							newstdData.put("st_number", DSDE.getET()[1].getText());
							newstdData.put("st_card", DSDE.getET()[2].getText());
							stdData.put(i, newstdData);
						}
					}
					String newStd_Data = stdData.toString();
					CurriculumVO CuData = new CurriculumVO();
					CuData = curruculumData.get(0);
					CuData.setCurriculum_STD_Data(newStd_Data);
					curriculummanager.update(CuData);

					DSDE.cancel();
					isShow = false;
				} catch (JSONException s) {
					s.printStackTrace();
				}
				break;
			}
		}
	};

	/**
	 * 
	 * @param calendar
	 * @param date
	 * @param MONTH
	 *            月
	 * @param DAY
	 *            日
	 * @param HOUR
	 *            時
	 * @param MINUTE
	 *            分
	 * @param SECOND
	 *            秒
	 */

	public Date MyCalendar(Calendar calendar, Date date, int MONTH, int DAY, int HOUR, int MINUTE, int SECOND) {
		date = new Date();
		calendar.setTime(date);
		date = new Date(calendar.get(Calendar.YEAR) - 1900, calendar.get(Calendar.MONTH) + MONTH,
				calendar.get(Calendar.DAY_OF_MONTH) + DAY, calendar.get(Calendar.HOUR_OF_DAY) + HOUR,
				calendar.get(Calendar.MINUTE) + MINUTE, calendar.get(Calendar.SECOND) + SECOND);
		return date;
	}

	// 開始點名執行續start

	private void StartRollCall() {
		Log.i(LOG, "StartRollCall  >>>");
		isRunning = true;
		BT[0].setEnabled(true);
		IB[1].setVisibility(View.VISIBLE);

		// StatDate = new Date();
		// Calendar c = Calendar.getInstance();
		// c.setTime(StatDate);
		// StatDate = new Date(c.get(Calendar.YEAR) - 1900,
		// c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
		// c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
		// c.get(Calendar.SECOND));

		// 開始時間設定
		Calendar Start_Calender = Calendar.getInstance();
		StatDate = MyCalendar(Start_Calender, StatDate, 0, 0, 0, 0, 0);

		// 結束時間設定
		Calendar End_Calender = Calendar.getInstance();
		EndDate = MyCalendar(End_Calender, EndDate, 0, 0, ClassTimeLong, 0, 0);
		// EndDate = MyCalendar(End_Calender, EndDate, 0, 0, 0, 0, 5);

		// 遲到時間設定
		Calendar Late_Calender = Calendar.getInstance();
		// LateDate = MyCalendar(Late_Calender,LateDate, 0, 0, 0, 15, 0);
		LateDate = MyCalendar(Late_Calender, LateDate, 0, 0, 0, 0, 5);

		TV[4].setText(dateTimeFormat.format(StatDate));
		TV[5].setText(dateTimeFormat.format(EndDate));
		Log.i(LOG, "StartRollCallThread  >>>");

		StartRollCallThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (isRunning) {
					myHandle.sendMessage(myHandle.obtainMessage());
					try {
						StartRollCallThread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});

		StartRollCallThread.start();

		Log.i(LOG, "add  >>>");
		RollCall_DateVO RollCall_Date = new RollCall_DateVO();
		RollCall_Date.setRollCall_DateColumn_Curriculum_ID(String.valueOf(CurriculumList.get(0).get_id()));
		RollCall_Date.setRollCall_DateColumn_StartDate(dateTimeFormat.format(StatDate));
		RollCall_Date.setRollCall_DateColumn_EndDate(dateTimeFormat.format(EndDate));
		RollCall_Date.setISUPDATE(false);
		Log.i(LOG, "RollCall_Date status >>>" + rollCall_DateManage.add(RollCall_Date));
		Cuid = String.valueOf(CurriculumList.get(0).get_id());
		jsonData = new JSONObject();
		for (int i = 0; i < stdData.length(); i++) {
			try {
				jsonData = stdData.getJSONObject(i);
				RollCall_StudentVO RollCall_Student = new RollCall_StudentVO();
				RollCall_Student.setRollCall_DateColumn_Curriculum_ID(String.valueOf(CurriculumList.get(0).get_id()));
				RollCall_Student.setRollCall_DateColumn_DateID(
						rollCall_DateManage.getRollCall_DateByDate(dateTimeFormat.format(StatDate)));
				RollCall_Student.setRollCall_DateColumn_StudentID(jsonData.getString("st_number").toString());
				RollCall_Student.setRollCall_DateColumn_StudentRollCallDate("");
				RollCall_Student.setRollCall_DateColumn_AttendanceRate("0.0");
				Log.i(LOG, "RollCall_Student status >>>" + rollCall_StudentManager.add(RollCall_Student));// 增加資料方法誤刪
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		Log.i(LOG, "end  >>>");
	}

	// 計時執行續
	Handler myHandle = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			Date NowTime = new Date();
			if (dateTimeFormat.format(NowTime).equals(dateTimeFormat.format(EndDate))) {
				isRunning = false;
				Restart();

			} else if (dateTimeFormat.format(NowTime).equals(dateTimeFormat.format(LateDate))) {
				Log.i("MQTT", "MQTT STD 1>>");
				List<CurriculumVO> CurriculumVO = curriculummanager.getCurriculumByClassName(CuName, CuClass, CuYear);
				stdName = new ArrayList<String>();
				stdId = new ArrayList<String>();
				try {
					Log.i("MQTT", "MQTT STD 2>>");
					JSONArray stdData = new JSONArray(CurriculumVO.get(0).getCurriculum_STD_Data());
					for (int i = 0; i < stdData.length(); i++) {
						JSONObject jsonData = stdData.getJSONObject(i);
						stdId.add("".equals(jsonData.getString("st_number")) ? "無學號" : jsonData.getString("st_number"));
					}
					Log.i("MQTT", "MQTT STD 3>>");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.i("MQTT", "MQTT STD 5>>");
				for (int i = 0; i < stdId.size(); i++) {
					List<RollCall_StudentVO> RollCall_Student = rollCall_StudentManager.getRollCall_StudentByCuStd(
							CurriculumVO.get(0).getCurriculum_NAME(), CurriculumVO.get(0).getCurriculum_CLASS(), CuYear,
							stdId.get(i), rollCall_DateManage.getRollCall_DateByDate(dateTimeFormat.format(StatDate)));

					if (RollCall_Student.get(0).getRollCall_DateColumn_StudentRollCallDate().equals("")) {
						// 發送遲到通知
						if (PushServicStatus() == true) {
							try {
								publishToTopicMessage(stdId.get(i), "" + CuName + CuClass + " : 同學  " + stdId.get(i)
										+ "您好  目前課程已開始超過15分鐘囉~ ,若有事假、病假請聯絡教師  " + LoginUserName + "");
								Log.i("MQTT", "MQTT STD >>" + stdId.get(i));
							} catch (MqttException e) {
								e.printStackTrace();
							}
						}
					}
				}
				Log.i("MQTT", "MQTT STD 5>>");
			}
		}
	};

	// 點名function
	@SuppressWarnings("deprecation")
	private void Manually(String CardID) {
		if (CardID == null || "".equals(CardID)) {
			TS.ToastSet("請輸入學號", context, view, Toast.LENGTH_SHORT);
		} else {

			Boolean ok = false;
			Boolean DeBug = true;
			try {
				CurriculumManager curriculummanager = new CurriculumManager(context);
				CurriculumList = curriculummanager.getCurriculumByClassName2(CuName, CuClass, CuYear);

				stdData = new JSONArray(CurriculumList.get(0).getCurriculum_STD_Data());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			List<RollCall_StudentVO> RollCall_StudentManagerVO = rollCall_StudentManager
					.getRollCall_StudentByClassAndDate(CuName, CuClass, CuYear,
							rollCall_DateManage.getRollCall_DateByDate(dateTimeFormat.format(StatDate)));
			for (int i = 0; i < stdData.length(); i++) {
				try {
					JSONObject jsonData = new JSONObject();
					jsonData = stdData.getJSONObject(i);
					String St_name = jsonData.getString("st_name").toString();
					String St_Number = jsonData.getString("st_number").toString();

					if (CardID.equals(St_Number) || CardID.equals(jsonData.get("st_card").toString())) {
						for (int y = 0; y < RollCall_StudentManagerVO.size(); y++) {

							if (RollCall_StudentManagerVO.get(y).getRollCall_DateColumn_StudentID().equals(St_Number)
									&& !"".equals(RollCall_StudentManagerVO.get(y)
											.getRollCall_DateColumn_StudentRollCallDate())) {
								TV[1].setText(St_name);
								TV[2].setText(St_Number);
								TV[3].setText("已完成點名");
								TV[3].setTextColor(Color.YELLOW);
								DeBug = false;
								break;
							}
						}

						if (DeBug == true) {
							TV[1].setText(St_name);
							TV[2].setText(St_Number);
							TV[3].setText("點名成功");
							TV[3].setTextColor(Color.GREEN);

							Date StdTime = new Date();
							Calendar StdTime_Calender = Calendar.getInstance();
							StdTime = MyCalendar(StdTime_Calender, StdTime, 0, 0, 0, 0, 0);

							RollCall_StudentVO RollCall_Student = new RollCall_StudentVO();
							RollCall_Student.setRollCall_DateColumn_Curriculum_ID(
									String.valueOf(CurriculumList.get(0).get_id()));
							RollCall_Student.setRollCall_DateColumn_DateID(
									rollCall_DateManage.getRollCall_DateByDate(dateTimeFormat.format(StatDate)));
							RollCall_Student.setRollCall_DateColumn_StudentID(St_Number);
							RollCall_Student.setRollCall_DateColumn_StudentRollCallDate(dateTimeFormat.format(StdTime));
							RollCall_Student.setRollCall_DateColumn_AttendanceRate("0");
							Log.i(LOG,
									"RollCall_Student status >>>" + rollCall_StudentManager.update(RollCall_Student));// 更新資料方法誤刪
							ok = true;
							break;
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				if (i == stdData.length() - 1) {
					if (ok == false && DeBug == true) {
						TV[1].setText("學生姓名");
						TV[2].setText("學生學號");
						TV[3].setText("點名失敗");
						TV[3].setTextColor(Color.RED);
					}
				}

			}
		}

	}

	// Avtivirty重啟
	public void Restart() {
		IB[1].setVisibility(View.GONE);
		isRunning = false;
		Date date = new Date();
		Calendar EndDate_calendar = Calendar.getInstance();
		EndDate = MyCalendar(EndDate_calendar, date, 0, 0, 0, 0, 0);

		RollCall_DateVO RollCall_Date = new RollCall_DateVO();
		RollCall_Date.setRollCall_DateColumn_Curriculum_ID(String.valueOf(CurriculumList.get(0).get_id()));
		RollCall_Date.setRollCall_DateColumn_StartDate(dateTimeFormat.format(StatDate));
		RollCall_Date.setISUPDATE(false);
		RollCall_Date.setRollCall_DateColumn_EndDate(dateTimeFormat.format(EndDate));
		Log.i(LOG, "RollCall_Date status >>>" + rollCall_DateManage.update(RollCall_Date));
		PBS.show();
		PBS.setText("計算中請稍後..");
		ClientTest = new Thread(new Runnable() {
			@Override
			public void run() {
				Count();
			}
		});
		ClientTest.start();
	}

	/*
	 * 連線測試
	 */
	Handler Clienttest = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			if (UpdataHttpClient.checkNetworkConnected(cm) == true) {
				String ConnectionTest = UpdataHttpClient.executeQuery(null, "4");
				Log.i(LOG, "ConnectionTest >>" + ConnectionTest);
				if (ConnectionTest.toString().trim().equals("0")) {
					Log.i(LOG, "Connection Success! >>");
					// Clienttest.sendEmptyMessage(0);
					PBSThread();
					Log.i(LOG, "連線測試   End>>");
				} else if (ConnectionTest.toString().trim().equals("-1")) {
					Log.i(LOG, "Connection Fail! >>");
					PBS.cancel();
					toast_InternetError.sendEmptyMessage(0);
					finish();
				}
			} else {
				toast_InternetError.sendEmptyMessage(0);
				finish();
			}

			DLS.cancel();

		}
	};

	Handler toast_InternetError = new Handler() {
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
	private void PBSThread() {
		PBS.setText("上傳中..請稍後...");
		UpdateData = new Thread(new Runnable() {
			@Override
			public void run() {
				ClassDate_Update();
			}
		});
		UpdateData.start();

	}

	private void ClassDate_Update() {
		String Update = "";
		String Parameter = "";
		String Responses = "";
		String DataID = rollCall_DateManage.getRollCall_DateByDate(dateTimeFormat.format(StatDate));

		List<RollCall_DateVO> rollCall_Date = rollCall_DateManage.getRollCall_DateByID(DataID);
		String Starttime = rollCall_Date.get(0).getRollCall_DateColumn_StartDate();// 課程開始時間
		String Endtime = rollCall_Date.get(0).getRollCall_DateColumn_EndDate();// 課程結束時間

		List<RollCall_StudentVO> StdData = rollCall_StudentManager.getRollCall_StudentByDateId(DataID);
		String RClassID = StdData.get(0).getRollCall_DateColumn_Curriculum_ID();// 課程ID
		String RdateID = StdData.get(0).getRollCall_DateColumn_DateID();// 日期id
		ClassID = StdData.get(0).getRollCall_DateColumn_Curriculum_ID();// 課程ID
		List<CurriculumVO> CurriculumVO = curriculummanager.getCurriculumByCuId(RClassID);
		String cuname = CurriculumVO.get(0).getCurriculum_NAME();
		String cuclass = CurriculumVO.get(0).getCurriculum_CLASS();
		String year = CurriculumVO.get(0).getCurriculum_SEASON();
		Log.i("dataupdate", "year >>" + year);
		/****/
		/*
		 * Parameter = "SELECT id FROM curriculum WHERE cu_name ='"+ cuname +
		 * "' AND class_name ='"+ cuclass +"' AND year ='"+ year +"'"; Update =
		 * UpdataHttpClient.executeQuery(Parameter, "9"); String DataBaseCuID =
		 * null ; try { JSONArray jsonArray = new JSONArray(Update); JSONObject
		 * jsonData = jsonArray.getJSONObject(0); DataBaseCuID =
		 * jsonData.getString("id"); } catch (JSONException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		/****/
		/****/
		List<CurriculumVO> curruculumData = new ArrayList<CurriculumVO>();
		curruculumData = curriculummanager.getCurriculumByClassName(cuname, cuclass, CuYear);

		/** 上傳課程紀錄 **/
		Parameter = "SELECT count(*) count FROM class_record WHERE id ='" + RdateID + "' AND start_time ='" + Starttime
				+ "' AND end_time ='" + Endtime + "'AND cu_id ='" + ClassID + "'";
		Responses = UpdataHttpClient.executeQuery(Parameter, "9");
		try {
			JSONArray jsonArray = new JSONArray(Responses);
			JSONObject jsonData = jsonArray.getJSONObject(0);
			Responses = jsonData.getString("count");
			if (Integer.valueOf(Responses) < 1) {
				/****/
				Parameter = "INSERT INTO class_record(id,start_time,end_time,cu_id) VALUES ('" + RdateID + "','"
						+ Starttime + "','" + Endtime + "','" + ClassID + "')";
				Update = UpdataHttpClient.executeQuery(Parameter, "9");
				/****/
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		/****/

		for (int x = 0; x < StdData.size(); x++) {
			String DateId = StdData.get(0).getRollCall_DateColumn_DateID();// 日期id
			String toDayRate = StdData.get(x).getRollCall_DateColumn_AttendanceRate();// 當日出席率
			StudID = StdData.get(x).getRollCall_DateColumn_StudentID();// 學生學號
			String StudRCTime = StdData.get(x).getRollCall_DateColumn_StudentRollCallDate();// 學生點名時間
			ClassID = StdData.get(0).getRollCall_DateColumn_Curriculum_ID();// 課程ID

			/** 上傳學生點名紀錄 **/
			Parameter = "SELECT count(*) count FROM time_record WHERE record_id ='" + DateId + "' AND st_number ='"
					+ StudID + "' AND cu_id ='" + ClassID + "'AND time_record ='" + StudRCTime + "'";
			Responses = UpdataHttpClient.executeQuery(Parameter, "9");
			try {
				JSONArray jsonArray = new JSONArray(Responses);
				JSONObject jsonData = jsonArray.getJSONObject(0);
				Responses = jsonData.getString("count");
				if (Integer.valueOf(Responses) < 1) {
					/****/
					Parameter = "INSERT INTO time_record(record_id,st_number,cu_id,time_record) VALUES ('" + DateId
							+ "','" + StudID + "','" + ClassID + "','" + StudRCTime + "')";
					Update = UpdataHttpClient.executeQuery(Parameter, "9");
					/****/
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			/****/
			rollCall_StudentManager = new RollCall_StudentManager(context);
			List<RollCall_StudentVO> RollCall_Student = new ArrayList<RollCall_StudentVO>();
			RollCall_Student = rollCall_StudentManager.getRollCall_StudentByCu(cuname, cuclass, year, StudID);
			Double Rate = 0.0;
			for (int t = 0; t < RollCall_Student.size(); t++) {
				Log.i("TAG", "getRollCall_DateColumn_AttendanceRate >>"
						+ RollCall_Student.get(t).getRollCall_DateColumn_AttendanceRate());
				Rate = Rate + Double.valueOf(RollCall_Student.get(t).getRollCall_DateColumn_AttendanceRate());
			}
			Log.i("TAG", "RollCall_Student size >>" + RollCall_Student.size());
			DecimalFormat df = new DecimalFormat("#.##");
			String s = df.format(Rate / RollCall_Student.size());

			/** 上傳學生出席率 **/
			Parameter = "SELECT count(*) count FROM rollcall_record WHERE class_name ='" + ClassID + "' AND st_name ='"
					+ StudID + "'";
			Responses = UpdataHttpClient.executeQuery(Parameter, "9");
			try {
				JSONArray jsonArray = new JSONArray(Responses);
				JSONObject jsonData = jsonArray.getJSONObject(0);
				Responses = jsonData.getString("count");
				Log.i("TAG", "ClassID >>" + ClassID);
				Log.i("TAG", "StudID >>" + StudID);
				Log.i("TAG", "rollcall_rate >>" + s);
				if (Integer.valueOf(Responses) < 1) {

					/****/
					Parameter = "INSERT INTO rollcall_record(class_name,st_name,rollcall_rate) VALUES ('" + ClassID
							+ "','" + StudID + "','" + s + "')";
					Update = UpdataHttpClient.executeQuery(Parameter, "9");
					/****/
				} else {
					/****/
					Parameter = "UPDATE rollcall_record SET rollcall_rate='" + s + "'WHERE class_name='" + ClassID
							+ "'AND st_name='" + StudID + "'";
					Update = UpdataHttpClient.executeQuery(Parameter, "9");
					/****/
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			/****/

		}

		RollCall_DateVO RollCall_DateVO = new RollCall_DateVO();
		RollCall_DateVO.set_id(Integer.valueOf(RdateID));
		RollCall_DateVO.setISUPDATE(true);
		RollCall_DateVO.setRollCall_DateColumn_Curriculum_ID(RClassID);
		RollCall_DateVO.setRollCall_DateColumn_StartDate(Starttime);
		RollCall_DateVO.setRollCall_DateColumn_EndDate(Endtime);
		rollCall_DateManage.update(RollCall_DateVO);
		finish();
	}

	// NFC封包管理
	private void NfcManager() {
		NfcManager manager = (NfcManager) getSystemService(NFC_SERVICE);
		nfcManage = new NfcManage();
		nfcManage.NFCManager(manager, context);
	}

	// 返回鑑
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 確定按下退出鍵and防止重複按下退出鍵

			if (isRunning == true) {
				DLS = new DiaLogLeaveStyle(context, "點名尚未結束確定離開?");
				DLS.setButton(BTKEYCODE_BACK_2);
			} else {
				DLS = new DiaLogLeaveStyle(context, "是否離開?");
				DLS.setButton(BTKEYCODE_BACK);
			}

			DLS.show();
		}

		return false;

	}

	// 結束點名Button監聽
	private Button.OnClickListener BTListenerByLeave = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.BT_YES:
				Restart();

				DLS.cancel();

				break;
			case R.id.BT_NO:
				DLS.cancel();
				break;
			}

		}
	};

	// 結束點名Button監聽
	private Button.OnClickListener BTKEYCODE_BACK = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.BT_YES:
				finish();
				break;
			case R.id.BT_NO:
				DLS.cancel();
				break;
			}

		}
	};

	// 點名尚未結束確定離開Button監聽
	private Button.OnClickListener BTKEYCODE_BACK_2 = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.BT_YES:
				PBS.show();
				PBS.setText("計算中請稍後..");
				ClientTest = new Thread(new Runnable() {
					@Override
					public void run() {
						Count();
						DLS.cancel();
						finish();
					}
				});
				ClientTest.start();

				break;
			case R.id.BT_NO:
				DLS.cancel();
				break;
			}

		}
	};

	// 轉換碼
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

	// 關閉鍵盤
	private void closeInputMethod() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(ET_StuID.getWindowToken(), 0);
	}

	// 出席率計算
	private void Count() {
		Log.i(LOG, "Count");
		List<RollCall_StudentVO> RollCall_StudentManagerVO = rollCall_StudentManager.getRollCall_StudentByClass(CuName,
				CuClass, CuYear);
		Log.i(LOG, "CuName >>>" + CuName);
		Log.i(LOG, "CuClass >>>" + CuClass);
		for (int i = 0; i < RollCall_StudentManagerVO.size(); i++) {
			// Log.i(LOG, "StudentID >>>" +
			// RollCall_StudentManagerVO.get(i).getRollCall_DateColumn_StudentID().toString());
			ARCT = new AttendanceRateCalculationTool();
			String Rate = ARCT.AttendanceRateCalculationTool(context, rollCall_StudentManager, CuName, CuClass,
					RollCall_StudentManagerVO.get(i).getRollCall_DateColumn_StudentID().toString(), CuYear);
			if (i == RollCall_StudentManagerVO.size() - 1
					|| RollCall_StudentManagerVO.size() == RollCall_StudentManagerVO.size()) {
				if (PushServicStatus() == true) {
					if (Double.valueOf(Rate) < 60) {
						// 出席率低於60發送通知
						try {
							String ID = RollCall_StudentManagerVO.get(i).getRollCall_DateColumn_StudentID().toString();
							publishToTopicMessage(ID, "" + CuName + CuClass + " : 同學  " + ID
									+ "您好  目前出席率過低 ,若有問題請聯絡教師  " + LoginUserName + "");
						} catch (MqttException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		Clienttest.sendEmptyMessage(0);
	}

	// mqtt主題發布
	public void publishToTopicMessage(String ID, String message) throws MqttException {
		IMqttClient I = PushService.getMqttClient();
		I.publish("Message/" + ID, message.getBytes(), PushService.MQTT_QUALITY_OF_SERVICE,
				PushService.MQTT_RETAINED_PUBLISH);
	}

	// mqtt Servier 狀態
	public boolean PushServicStatus() {
		SharedPreferences p = getSharedPreferences(PushService.TAG, MODE_PRIVATE);
		boolean started = p.getBoolean(PushService.PREF_STARTED, false);
		return started;
	}

	public void onPause() {
		super.onPause();
		nfcManage.disableForegroundDispatch(this);
	}

	public void onResume() {
		super.onResume();
		nfcManage.enableForegroundDispatch(this);
	}

}
