package com.rollcallsystem.ClassDataManageActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.rollcallsystem.R;
import com.rollcallsystem.Connector.UpdataHttpClient;
import com.rollcallsystem.CustomInterface.AttendanceRateCalculationTool;
import com.rollcallsystem.CustomInterface.DiaLogStudentDataEdit;
import com.rollcallsystem.CustomInterface.DiaLogStudentRecord;
import com.rollcallsystem.CustomInterface.ProgressBarStyle;
import com.rollcallsystem.CustomInterface.StudentDataListAdapter;
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
import com.rollcallsystem.spf.manage.SPFManager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class StudentRecord extends Activity
{
	private static final String LOG = "StudentRecord";
	private View view;
	private Context context;
	private LinearLayout LL;
	private ProgressBarStyle PBS;
	private String LoginUserName;
	private SPFManager Spfmanager;
	private CurriculumManager curriculummanager;
	private UserManager usermanager;
	private ListView LV_List;
	private Thread ClientTest;
	private Button BT_UPDATA, BT_Recalculate;
	private DiaLogStudentRecord DSR;
	private List<CurriculumVO> curruculumData;
	private List<String> stdName, stdId, stdCardId, stdRate;
	private RollCall_DateManage rollCall_DateManage;
	private RollCall_StudentManager rollCall_StudentManager;
	private List<RollCall_StudentVO> RollCall_Student;
	private int ListIndex = 0;
	private ToastStyle TS;
	private String cuName, cuClass,cuYear;
	private TextView tv4;
	private UserVO user;
	private AttendanceRateCalculationTool ARCT;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.i(LOG, "onCreate Start >>>");
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.studentdata_cardregister);
		LayoutInflater inflater = getLayoutInflater();
		view = inflater.inflate(R.layout.toast_layout,
				(ViewGroup) findViewById(R.id.toast_layout));
		try
		{
			context = StudentRecord.this;
			TS = new ToastStyle();
			PBS = new ProgressBarStyle(context);
			
			Spfmanager = new SPFManager(context);
			usermanager = new UserManager(context, Spfmanager);
			rollCall_DateManage = new RollCall_DateManage(context);
			rollCall_StudentManager = new RollCall_StudentManager(context);
			user = usermanager.getUserById(Integer.valueOf(Spfmanager
					.load(UserColumn.TABLE_NAME + UserColumn._ID)));
			LoginUserName = user.getUSER_NAME();
			curriculummanager = new CurriculumManager(context);
		} catch (Exception e)
		{
		}
		ListViewInto();
		ButtonInto();
		setTitle("點名系統-" + LoginUserName + "-" + cuName);
	}
	
	private void ListViewInto()
	{
		LV_List = (ListView) findViewById(R.id.LV_List);
		LL = (LinearLayout) findViewById(R.id.LL);
		tv4 = (TextView) findViewById(R.id.textView4);
		tv4.setText("出席率");
		Bundle bundleClassName = getIntent().getExtras();
		cuName = bundleClassName.getString("cuName");
		cuClass = bundleClassName.getString("cuClass");
		cuYear = bundleClassName.getString("cuYear");
		ListViewAdapter();
		
	}
	
	private void ListViewAdapter()
	{
		curriculummanager = new CurriculumManager(context);
		curruculumData = new ArrayList<CurriculumVO>();
		curruculumData = curriculummanager.getCurriculumByClassName(cuName, cuClass,cuYear);
		stdName = new ArrayList<String>();
		stdId = new ArrayList<String>();
		stdCardId = new ArrayList<String>();
		stdRate = new ArrayList<String>();
		try
		{
			JSONArray stdData = new JSONArray(curruculumData.get(0).getCurriculum_STD_Data());
			for (int i = 0; i < stdData.length(); i++)
			{
				JSONObject jsonData = stdData.getJSONObject(i);
				stdName.add("".equals(jsonData.getString("st_name")) ? "無名氏" : jsonData
						.getString("st_name"));
				stdId.add("".equals(jsonData.getString("st_number")) ? "無學號" : jsonData
						.getString("st_number"));
				stdCardId.add("".equals(jsonData.getString("st_card")) ? "尚未註冊" : jsonData
						.getString("st_card"));
				RollCall_Student = new ArrayList<RollCall_StudentVO>();
				RollCall_Student = rollCall_StudentManager.getRollCall_StudentByCu(cuName, cuClass,cuYear, "".equals(jsonData.getString("st_number")) ? "無學號" : jsonData.getString("st_number"));
				Log.i(LOG, "RollCall_Student Size>>" + RollCall_Student.size());
				Double Rate = 0.0;
				for (int x = 0; x < RollCall_Student.size(); x++)
				{
					Rate = Rate
							+ Double.valueOf(RollCall_Student.get(x)
									.getRollCall_DateColumn_AttendanceRate());
					Log.i(LOG, "Rate >>"
							+ RollCall_Student.get(x).getRollCall_DateColumn_AttendanceRate());
				}
				
				DecimalFormat df = new DecimalFormat("#.##");
				String s = df.format(Rate / RollCall_Student.size());
				Log.i(LOG, "s >>" + s);
				stdRate.add(s + " %");
				
			}
		} catch (JSONException e)
		{
			// TODO Auto-generated catch blocks
			e.printStackTrace();
		}
		Log.i(LOG, "stdNameSize" + stdName.size());
		
		LV_List.setAdapter(new StudentDataListAdapter(context, stdName, stdId,
				stdRate));
		LV_List.setOnItemClickListener(LVListener);
		
	}
	
	private ListView.OnItemClickListener LVListener = new OnItemClickListener()
	{
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			// String Student =
			// LV_List.getItemAtPosition(position).toString();
			Log.i(LOG, "開始");
			List<RollCall_DateVO> rollCall_Date = rollCall_DateManage
					.getRollCall_DateByCu(cuName, cuClass,cuYear);
			List<RollCall_StudentVO> RollCall_Student = rollCall_StudentManager
					.getRollCall_StudentByCu(cuName, cuClass,cuYear, stdId.get(position));
			List<String> StudentRollData = new ArrayList<String>();
			for (int x = 0; x < RollCall_Student.size(); x++)
			{
				Log.i("S", "AttendanceRate >>"
						+ RollCall_Student.get(x).getRollCall_DateColumn_StudentRollCallDate());
				
				if (RollCall_Student.get(x).getRollCall_DateColumn_StudentRollCallDate().trim().equals(""))
				{
					StudentRollData.add("未簽到");
				} else
				{
					StudentRollData.add(RollCall_Student.get(x)
							.getRollCall_DateColumn_StudentRollCallDate());
				}
			}
			
			Log.i(LOG, "學生靠卡紀錄List size >>>" + StudentRollData.size());
			DSR = new DiaLogStudentRecord(context, stdRate.get(position),
					rollCall_Date, StudentRollData);
			DSR.setButton(BTListener);
			DSR.show();
		}
	};
	
	private void ButtonInto()
	{
		BT_UPDATA = (Button) findViewById(R.id.BT_UPDATA);
		BT_Recalculate = (Button) findViewById(R.id.BT_Recalculate);
		BT_Recalculate.setVisibility(View.VISIBLE);
		BT_UPDATA.setOnClickListener(BTListener);
		BT_Recalculate.setOnClickListener(BTListener);
	}
	
	private Button.OnClickListener BTListener = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
				case R.id.BT_UPDATA:
					Export();
					break;
				case R.id.BT_Save:
					DSR.cancel();
					break;
				case R.id.BT_Recalculate:
					
					PBS.show();
					PBS.setText("請稍後...");
					ClientTest = new Thread(new Runnable()
					{
						@Override
						public void run()
						{
							
							List<RollCall_StudentVO> RollCall_StudentManagerVO = rollCall_StudentManager.getRollCall_StudentByClass(cuName, cuClass,cuYear);
							for (int i = 0; i < RollCall_StudentManagerVO.size(); i++)
							{
								Log.i(LOG, "StudentID >>>"+ RollCall_StudentManagerVO.get(i).getRollCall_DateColumn_StudentID().toString());
								ARCT = new AttendanceRateCalculationTool();
								ARCT.AttendanceRateCalculationTool(context, rollCall_StudentManager,cuName, cuClass, RollCall_StudentManagerVO.get(i).getRollCall_DateColumn_StudentID().toString(),cuYear);
								
							}
							myHandle.sendEmptyMessage(0);
							
						}
					});
					ClientTest.start();
					
					break;
			
			}
		}
	};
	
	private void Export()
	{
		try
		{
			// Data
			JSONObject Data = new JSONObject();// data資料
			JSONArray Time = new JSONArray();// 學生紀錄
			JSONArray ClassRecordJA = new JSONArray();// 課程紀錄
			curruculumData = curriculummanager.getCurriculumByClassName(cuName, cuClass,cuYear);
			List<RollCall_DateVO> rollcalldate = rollCall_DateManage
					.getRollCall_DateByCu(cuName, cuClass,cuYear);
			for (int i = 0; i < rollcalldate.size(); i++)
			{
				// 開始結束 物件
				JSONArray ClassRecord = new JSONArray();
				ClassRecord.put(rollcalldate.get(i).getRollCall_DateColumn_StartDate());
				ClassRecord.put(rollcalldate.get(i).getRollCall_DateColumn_EndDate());
				JSONObject ClassRecordJ = new JSONObject();
				ClassRecordJ.put(String.valueOf(rollcalldate.get(i).get_id()), ClassRecord);
				ClassRecordJA.put(ClassRecordJ);
			}
			
			for (int i = 0; i < stdName.size(); i++)
			{
				
				JSONObject stdata = new JSONObject();
				stdata.put("st_name", stdName.get(i));
				stdata.put("st_number", stdId.get(i));
				stdata.put("attend_rate", stdRate.get(i));
				
				JSONArray A = new JSONArray();
				for (int y = 0; y < rollcalldate.size(); y++)
				{
					// 學生記錄物件
					JSONObject a = new JSONObject();
					List<RollCall_StudentVO> RollCall_Student = rollCall_StudentManager
							.getRollCall_StudentByCu(cuName, cuClass,cuYear, stdId.get(i));
					a.put(String.valueOf(rollcalldate.get(y).get_id()), RollCall_Student.get(y)
							.getRollCall_DateColumn_StudentRollCallDate());
					A.put(a);
				}
				stdata.put("Time", A);
				Time.put(stdata);
			}
			
			Data.put("CuRecord", ClassRecordJA);
			Data.put("StdRecord", Time);
			
			JSONObject JSONString = new JSONObject();
			
			JSONString.put("TchName", LoginUserName);
			JSONString.put("CuName", cuName + cuClass
					+ curruculumData.get(0).getCurriculum_SEASON());
			JSONString.put("Email", usermanager.getUserByName(LoginUserName).get(0)
					.getUSER_EMAIL());
			JSONString.put("Data", Data);
			Log.i(LOG, "JSONString >>" + JSONString);
			String Send = UpdataHttpClient.executeQuery(JSONString.toString(), "8");
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		
	}
	
	/*
	 * 連線測試
	 */
	private void ConnectionTest()
	{
		
		PBS.show();
		PBS.setText("連線測試...");
		ClientTest = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				Log.i(LOG, "連線測試   Start>>");
				String ConnectionTest = UpdataHttpClient.executeQuery(null, "4");
				Log.i(LOG, "ConnectionTest >>" + ConnectionTest);
				if (ConnectionTest.toString().trim().equals("0"))
				{
					Log.i(LOG, "Connection Success! >>");
					PBS.cancel();
					// Clienttest.sendEmptyMessage(0);
					Log.i(LOG, "連線測試   End>>");
				} else if (ConnectionTest.toString().trim().equals("-1"))
				{
					Log.i(LOG, "Connection Fail! >>");
					PBS.cancel();
					ClientTest.stop();
					TS.ToastSet("連線異常", context, view, Toast.LENGTH_LONG);
				}
			}
		});
		ClientTest.start();
	}
	
	Handler myHandle = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			ListViewAdapter();
			PBS.cancel();
		}
	};
	
}
