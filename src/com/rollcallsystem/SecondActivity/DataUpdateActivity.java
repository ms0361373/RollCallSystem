package com.rollcallsystem.SecondActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.rollcallsystem.R;
import com.rollcallsystem.Connector.UpdataHttpClient;
import com.rollcallsystem.CustomInterface.DataUpdataActivityListAdapter;
import com.rollcallsystem.CustomInterface.DiaLogLeaveStyle;
import com.rollcallsystem.CustomInterface.ProgressBarStyle;
import com.rollcallsystem.CustomInterface.ToastStyle;
import com.rollcallsystem.DB.Column.UserColumn;
import com.rollcallsystem.DB.Manage.ALLManager;
import com.rollcallsystem.DB.Manage.CurriculumManager;
import com.rollcallsystem.DB.Manage.RollCall_DateManage;
import com.rollcallsystem.DB.Manage.RollCall_StudentManager;
import com.rollcallsystem.DB.Manage.UserManager;
import com.rollcallsystem.DB.VO.CurriculumVO;
import com.rollcallsystem.DB.VO.RollCall_DateVO;
import com.rollcallsystem.DB.VO.RollCall_StudentVO;
import com.rollcallsystem.DB.VO.SeasonYearVO;
import com.rollcallsystem.DB.VO.UserVO;
import com.rollcallsystem.spf.manage.SPFManager;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.renderscript.Sampler.Value;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DataUpdateActivity extends Activity {
private static final String LOG = "SetUpActivity";
private View view;
private ALLManager AllManager;
private Context context;
private UserManager usermanger;
private CurriculumManager CuManager;
private ListView LV;
private DataUpdataActivityListAdapter SALA;
private RollCall_DateManage rollCall_DateManage;
private RollCall_StudentManager rollCall_StudentManager;
private CheckBox cbx;
private DiaLogLeaveStyle DLS;
private List<String> list;
private SPFManager Spfmanager;
private ImageButton[] IB = new ImageButton[3];
private List<RollCall_DateVO> rollCall_Date;
private List<RollCall_StudentVO> rollCall_Student;
private String ClassID,StudID,LoginUserName;
private ProgressBarStyle PBS;
private Thread ClientTest,DownloadData;
private ToastStyle TS;
@Override
protected void onCreate(Bundle savedInstanceState) {
	Log.i(LOG, "onCreate Start>>>");
	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
	super.onCreate(savedInstanceState);
	setContentView(R.layout.dataupdate_activity);
	
	LayoutInflater inflater = getLayoutInflater();
	view = inflater.inflate(R.layout.toast_layout,
			(ViewGroup) findViewById(R.id.toast_layout));
	
	Log.i(LOG, "allmanager Start>>");
	try {
	context = DataUpdateActivity.this;
	TS = new ToastStyle();
	AllManager = new ALLManager(context);
	Spfmanager = new SPFManager(context);
	CuManager = new CurriculumManager(context);
	usermanger = new UserManager(context, Spfmanager);
	PBS = new ProgressBarStyle(context);
	rollCall_DateManage = new RollCall_DateManage(context);
	rollCall_StudentManager = new RollCall_StudentManager(context);
	UserVO user = usermanger.getUserById(Integer.valueOf(Spfmanager
			.load(UserColumn.TABLE_NAME + UserColumn._ID)));
	LoginUserName =  user.getUSER_NAME();
	setTitle("點名系統-" + user.getUSER_NAME());
	} catch (Exception e) {
	Log.e(LOG, "Vo error !");
	e.printStackTrace();
	}
	
	ListViewInto();
	ImageButtonInto();
}

private void ListViewInto() {
	list = new ArrayList<String>(0);
	// FIXME
	rollCall_Date = rollCall_DateManage.getAllRollCall_Date();
	LV = (ListView) findViewById(R.id.LV);
	LV.setAdapter(new DataUpdataActivityListAdapter(context, rollCall_Date));
	LV.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> adapter, View v, int position,
				long _id) {
			cbx = (CheckBox) v.findViewById(R.id.CB);
			if (cbx.isChecked()) {
			cbx.setChecked(false);
			boolean isRemove = list.remove(String.valueOf(position));
			if (isRemove) {
			Log.i(LOG, "Remove Item =" + position + " success !");
			} else {
			Log.w(LOG, "Remove Item =" + position + " fail !");
			}
			} else {
			cbx.setChecked(true);
			boolean isAdd = list.add(String.valueOf(position));
			if (isAdd) {
			Log.i(LOG, "Add Item =" + position + " success !");
			} else {
			Log.w(LOG, "Add Item =" + position + " fail !");
			}
			}
			Log.d(LOG, "List size =" + String.valueOf(list.size()));
		}
	});
}

private void ImageButtonInto() {
	IB[0] = (ImageButton) findViewById(R.id.IB);
	IB[1] = (ImageButton) findViewById(R.id.IB1);
	IB[2] = (ImageButton) findViewById(R.id.IB2);
	for (int i = 0; i < IB.length; i++) {
	IB[i].setOnClickListener(IBListener);
	}
}

private ImageButton.OnClickListener IBListener = new ImageButton.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.IB:
				DLS = new DiaLogLeaveStyle(context, "確定下載資料至裝置?");
				DLS.setButton(Listener_Down);
				DLS.show();
			break;
			case R.id.IB1:
			DLS = new DiaLogLeaveStyle(context, "確定將裝置資料上傳?");
			DLS.setButton(Listener_UP);
			DLS.show();
			break;
			case R.id.IB2:
			DLS = new DiaLogLeaveStyle(context, "確定刪除?");
			DLS.setButton(Listener);
			DLS.show();
			break;
		}
	}
};

private Button.OnClickListener Listener = new Button.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.BT_YES:
			for (int i = 0; i < list.size(); i++) {
			int answer = Integer.valueOf(list.get(i));
			Log.i(
					LOG,
					"rollCall_DateManage status >>>"
							+ rollCall_DateManage.delete(rollCall_Date.get(answer)));
			Log.i(
					LOG,
					"rollCall_StudentManager status >>>"
							+ rollCall_StudentManager.delete(String.valueOf(rollCall_Date
									.get(answer).get_id())));
			}
			rollCall_Date = rollCall_DateManage.getAllRollCall_Date();
			LV.setAdapter(new DataUpdataActivityListAdapter(context, rollCall_Date));
			DLS.cancel();
			case R.id.BT_NO:
			DLS.cancel();
			break;
		}
		
	}
};

private Button.OnClickListener Listener_Down = new Button.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.BT_YES:
			ConnectionTest();
			DLS.cancel();
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
			String ConnectionTest = UpdataHttpClient
					.executeQuery(null, "4");
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
Handler Clienttest = new Handler() {
	@Override
	public void handleMessage(Message msg) {
		PBSThread(LoginUserName);
	}
};
Handler toast = new Handler() {
	@Override
	public void handleMessage(Message msg) {
		TS.ToastSet("連線異常", context, view, Toast.LENGTH_LONG);
	}
};
Handler myHandle = new Handler() {
	@Override
	public void handleMessage(Message msg) {
		PBS.cancel();
	}
};

/**
 * 資料更新
 * 
 * @param User
 */
private void PBSThread(final String User) {
	PBS.setText("下載中..請稍後...");
	DownloadData = new Thread(new Runnable() {
		@Override
		public void run() {
			String DownloadData = UpdataHttpClient.executeQuery(User, "10");
			try {
					JSONObject CRData = new JSONObject(DownloadData);
					String class_record = CRData.getString("class_record");
					JSONArray CR = new JSONArray(class_record);
					boolean CR_Lock = false;
					for (int x = 0; x < CR.length(); x++) {
						JSONObject jsonData_CR = CR.getJSONObject(x);
						
						Log.i(LOG, "add  >>>"+jsonData_CR.getString("id"));
						RollCall_DateVO RollCall_Date = new RollCall_DateVO();
						RollCall_Date.set_id(Integer.valueOf(jsonData_CR.getString("id")));
						RollCall_Date.setRollCall_DateColumn_Curriculum_ID(jsonData_CR.getString("cu_id"));
						RollCall_Date.setRollCall_DateColumn_StartDate(jsonData_CR.getString("start_time"));
						RollCall_Date.setRollCall_DateColumn_EndDate(jsonData_CR.getString("end_time"));
						RollCall_Date.setISUPDATE(true);
						Log.i(LOG, "RollCall_Date status >>>" + rollCall_DateManage.add(RollCall_Date));

						if (x == CR.length() - 1) {
							CR_Lock = true;
						}
					}
					
					
					String time_record = CRData.getString("time_record");
					JSONArray TR = new JSONArray(time_record);
					boolean TR_Lock = false;
					for (int x = 0; x < TR.length(); x++) {
						JSONObject jsonData_TR = TR.getJSONObject(x);
						
						Log.i(LOG, "stvo add  >>>");
						RollCall_StudentVO RollCall_Student = new RollCall_StudentVO();
						RollCall_Student.setRollCall_DateColumn_Curriculum_ID(jsonData_TR.getString("cu_id"));
						RollCall_Student.setRollCall_DateColumn_DateID(	jsonData_TR.getString("record_id"));
						RollCall_Student.setRollCall_DateColumn_StudentID(jsonData_TR.getString("st_number"));
						RollCall_Student.setRollCall_DateColumn_StudentRollCallDate(jsonData_TR.getString("time_record").equals("0000-00-00 00:00:00")? "":jsonData_TR.getString("time_record"));
						RollCall_Student.setRollCall_DateColumn_AttendanceRate("0.0");
						Log.i(LOG, "RollCall_Student status >>>" + rollCall_StudentManager.add(RollCall_Student));// 增加資料方法誤刪
						if (x == TR.length() - 1) {
							TR_Lock = true;
						}
					}
					
					if (CR_Lock && TR_Lock == true) {
						Log.i(LOG, "SystemUpdata End>>");
						myHandle.sendEmptyMessage(0);
						Looper.prepare();
						TS.ToastSet("更新完畢！", context, view, Toast.LENGTH_SHORT);
						Log.i(LOG, "Thread End>>");
						Looper.loop();
					}
					
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
	DownloadData.start();

}

private Button.OnClickListener Listener_UP = new Button.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		String Update="";
		String Parameter="";
		String Responses ="";
		switch (v.getId()) {
			case R.id.BT_YES:
				
			for (int i = 0; i < list.size(); i++) {
			int answer = Integer.valueOf(list.get(i));
			String RdateID =	String.valueOf(rollCall_Date.get(answer).get_id());// 日期id
			String RClassID =	rollCall_Date.get(answer).getRollCall_DateColumn_Curriculum_ID();// 課程ID
			String Starttime=	rollCall_Date.get(answer).getRollCall_DateColumn_StartDate();// 課程開始時間
			String Endtime=rollCall_Date.get(answer).getRollCall_DateColumn_EndDate();// 課程結束時間
			
			List<CurriculumVO> CurriculumVO = CuManager.getCurriculumByCuId(RClassID);
			String cuname = CurriculumVO.get(0).getCurriculum_NAME();
			String cuclass = CurriculumVO.get(0).getCurriculum_CLASS();
			String year = CurriculumVO.get(0).getCurriculum_SEASON();
			Log.i("dataupdate","year >>"+year);
			/****/
			Parameter = "SELECT id FROM curriculum WHERE cu_name ='"+ cuname +"' AND class_name ='"+ cuclass +"' AND year ='"+ year +"'";
			Update = UpdataHttpClient.executeQuery(Parameter, "9");
			String DataBaseCuID = null ;
			try {
			JSONArray jsonArray = new JSONArray(Update);
			JSONObject jsonData = jsonArray.getJSONObject(0);
			DataBaseCuID = jsonData.getString("id");
			} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			/****/
			/****/
			List<CurriculumVO> curruculumData = new ArrayList<CurriculumVO>();
			curruculumData = CuManager.getCurriculumByClassName(cuname, cuclass,year);
			
			/**上傳課程紀錄**/
			Parameter = "SELECT count(*) count FROM class_record WHERE id ='"+ RdateID +"' AND start_time ='"+ Starttime +"' AND end_time ='"+ Endtime +"'AND cu_id ='"+ DataBaseCuID +"'";
			Responses = UpdataHttpClient.executeQuery(Parameter, "9");
			try
			{
				JSONArray jsonArray = new JSONArray(Responses);
				JSONObject jsonData = jsonArray.getJSONObject(0);
				Responses = jsonData.getString("count");
				if(Integer.valueOf(Responses)<1)
				{
					/****/
					Parameter = "INSERT INTO class_record(id,start_time,end_time,cu_id) VALUES ('"
							+ RdateID+ "','"
							+ Starttime+ "','"
							+ Endtime+ "','"
							+ DataBaseCuID+ "')";
					Update = UpdataHttpClient.executeQuery(Parameter, "9");
					/****/
				}
			} catch (JSONException e)
			{
				e.printStackTrace();
			}
			
			/****/
			
			List<RollCall_StudentVO> StdData = rollCall_StudentManager
					.getRollCall_StudentByDateId(String.valueOf(rollCall_Date.get(answer)
							.get_id()));
			for (int x = 0; x < StdData.size(); x++) {
			String DateId =StdData.get(x).getRollCall_DateColumn_DateID();// 日期id
			String toDayRate =StdData.get(x).getRollCall_DateColumn_AttendanceRate();//當日出席率
			 StudID =StdData.get(x).getRollCall_DateColumn_StudentID();//學生學號
			String StudRCTime = StdData.get(x).getRollCall_DateColumn_StudentRollCallDate();//學生點名時間
			 ClassID = StdData.get(x).getRollCall_DateColumn_Curriculum_ID();//課程ID
			 
			 /**上傳學生點名紀錄**/
				Parameter = "SELECT count(*) count FROM time_record WHERE record_id ='"+ DateId +"' AND st_number ='"+ StudID +"' AND cu_id ='"+ ClassID +"'AND time_record ='"+ StudRCTime +"'";
				Responses = UpdataHttpClient.executeQuery(Parameter, "9");
				try
				{
					JSONArray jsonArray = new JSONArray(Responses);
					JSONObject jsonData = jsonArray.getJSONObject(0);
					Responses = jsonData.getString("count");
					if(Integer.valueOf(Responses)<1)
					{
						/****/
						Parameter = "INSERT INTO time_record(record_id,st_number,cu_id,time_record) VALUES ('"
						+ DateId+ "','"
						+ StudID+ "','"
						+ ClassID+ "','"
						+ StudRCTime+ "')";
						Update = UpdataHttpClient.executeQuery(Parameter, "9");
						/****/
					}
				} catch (JSONException e)
				{
					e.printStackTrace();
				}
				
				/****/
				List<RollCall_StudentVO>	RollCall_Student = new ArrayList<RollCall_StudentVO>();
				RollCall_Student = rollCall_StudentManager.getRollCall_StudentByCu(cuname,cuclass,year,StudID);
				Double Rate = 0.0;
				for (int t = 0; t < RollCall_Student.size(); t++) {
					Log.i("TAG","getRollCall_DateColumn_AttendanceRate >>"+RollCall_Student.get(t).getRollCall_DateColumn_AttendanceRate());
				Rate = Rate+ Double.valueOf(RollCall_Student.get(t).getRollCall_DateColumn_AttendanceRate());
				}
				Log.i("TAG","RollCall_Student size >>"+RollCall_Student.size());
				DecimalFormat df = new DecimalFormat("#.##");
				String s = df.format(Rate / RollCall_Student.size());
				
				/**上傳學生出席率**/
				Parameter = "SELECT count(*) count FROM rollcall_record WHERE class_name ='"+ ClassID +"' AND st_name ='"+ StudID +"'";
				Responses = UpdataHttpClient.executeQuery(Parameter, "9");
				try
				{
					JSONArray jsonArray = new JSONArray(Responses);
					JSONObject jsonData = jsonArray.getJSONObject(0);
					Responses = jsonData.getString("count");
					Log.i("TAG","ClassID >>"+ClassID);
					Log.i("TAG","StudID >>"+StudID);
					Log.i("TAG","rollcall_rate >>"+s);
					if(Integer.valueOf(Responses)<1)
					{
						
						/****/
						Parameter = "INSERT INTO rollcall_record(class_name,st_name,rollcall_rate) VALUES ('"
							+ ClassID + "','"
							+ StudID + "','"
							+ s + "')";
						Update = UpdataHttpClient.executeQuery(Parameter, "9");
						/****/
					}
					else
					{
						/****/
						Parameter = "UPDATE rollcall_record SET rollcall_rate='" + s + "'WHERE class_name='" + ClassID + "'AND st_name='" + StudID + "'";
						Update = UpdataHttpClient.executeQuery(Parameter, "9");
						/****/
					}
				} catch (JSONException e)
				{
					e.printStackTrace();
				}
				/****/
				
			}
			
			
			RollCall_DateVO	RollCall_DateVO = new RollCall_DateVO();
			RollCall_DateVO.set_id(rollCall_Date.get(answer).get_id());
			RollCall_DateVO.setISUPDATE(true);
			RollCall_DateVO.setRollCall_DateColumn_Curriculum_ID(RClassID);
			RollCall_DateVO.setRollCall_DateColumn_StartDate(Starttime);
			RollCall_DateVO.setRollCall_DateColumn_EndDate(Endtime);
			rollCall_DateManage.update(RollCall_DateVO);
			}
		
			DLS.cancel();
			rollCall_Date = rollCall_DateManage.getAllRollCall_Date();
			LV.setAdapter(new DataUpdataActivityListAdapter(context, rollCall_Date));
			
			case R.id.BT_NO:
			DLS.cancel();
			break;
		}
		
	}
};
}
