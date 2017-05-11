package com.rollcallsystem.SecondActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.rollcallsystem.R;
import com.rollcallsystem.ClassDataManageActivity.CardRegister;
import com.rollcallsystem.ClassDataManageActivity.StudentRecord;
import com.rollcallsystem.Connector.UpdataHttpClient;
import com.rollcallsystem.CustomInterface.DiaLogClassManage;
import com.rollcallsystem.CustomInterface.DiaLogLeaveStyle;
import com.rollcallsystem.CustomInterface.DialogDataUpdateAndDowload;
import com.rollcallsystem.CustomInterface.ExpandableListAdapter;
import com.rollcallsystem.CustomInterface.ProgressBarStyle;
import com.rollcallsystem.CustomInterface.ToastStyle;
import com.rollcallsystem.DB.Column.UserColumn;
import com.rollcallsystem.DB.Manage.CurriculumManager;
import com.rollcallsystem.DB.Manage.RollCall_DateManage;
import com.rollcallsystem.DB.Manage.RollCall_StudentManager;
import com.rollcallsystem.DB.Manage.SeasonYearManager;
import com.rollcallsystem.DB.Manage.UserManager;
import com.rollcallsystem.DB.VO.CurriculumVO;
import com.rollcallsystem.DB.VO.RollCall_DateVO;
import com.rollcallsystem.DB.VO.RollCall_StudentVO;
import com.rollcallsystem.DB.VO.SeasonYearVO;
import com.rollcallsystem.DB.VO.UserVO;
import com.rollcallsystem.spf.manage.SPFManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;

public class ClassDataManage extends Activity {
	private static final String LOG = "ClassDataManage";
	private View view;
	private Context context;
	private String LoginUserName, cuName, cuClass, year;
	private SPFManager Spfmanager;
	private CurriculumManager curriculummanager;
	private UserManager usermanager;
	private SeasonYearManager seasonyearmanager;
	private ExpandableListAdapter EPListAdapter;
	private ExpandableListView EPListView;
	private List<String> EPListHeader; // 標題
	private HashMap<String, List<String>> EPListChild;// 內容
	private DiaLogClassManage DCM;
	private RollCall_DateManage rollCall_DateManage;
	private RollCall_StudentManager rollCall_StudentManager;
	private DialogDataUpdateAndDowload DUA;
	private DiaLogLeaveStyle DLS;
	private ToastStyle TS;
	private String YEAR;
	// 連線測試
	private ProgressBarStyle PBS;
	private Thread ClientTest, Data;
	private String K;
	private boolean isRunning = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(LOG, "onCreate Start >>>");
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classdatamanage);
		LayoutInflater inflater = getLayoutInflater();
		view = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout));
		try {
			context = ClassDataManage.this;
			curriculummanager = new CurriculumManager(context);
			Spfmanager = new SPFManager(context);
			usermanager = new UserManager(context, Spfmanager);
			seasonyearmanager = new SeasonYearManager(context);
			PBS = new ProgressBarStyle(context);
			TS = new ToastStyle();
			rollCall_DateManage = new RollCall_DateManage(context);
			rollCall_StudentManager = new RollCall_StudentManager(context);
			UserVO user = usermanager.getUserById(Integer.valueOf(Spfmanager.load(UserColumn.TABLE_NAME + UserColumn._ID)));
			LoginUserName = user.getUSER_NAME();
			setTitle("點名系統-" + LoginUserName);
		} catch (Exception e) {
			// TODO: handle exception
		}
		Log.i(LOG, "LoginUserName  >>>" + LoginUserName);
		Log.i(LOG, "ExpandableListInto Start >>>");
		ExpandableListInto();
		Log.i(LOG, "ExpandableListInto End >>>");
	}

	private void ExpandableListInto() {
		EPListDataSet();

		EPListView = (ExpandableListView) findViewById(R.id.EPListView);
		EPListAdapter = new ExpandableListAdapter(context, EPListHeader, EPListChild);

		EPListView.setAdapter(EPListAdapter);
		EPListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
			{
				String[] CU = (EPListChild.get(EPListHeader.get(groupPosition)).get(childPosition)).split("\r\n");
				cuName = new String();
				cuClass = new String();
				year = new String();
				year = EPListHeader.get(groupPosition).toString();
				String[] Year = year.split("學年度");
				String S = Year[1].equals("上學期")? "1":"2";
				YEAR = Year[0]+"-"+S;
				Log.i("ClassDataManger","year"+YEAR);
				cuName = CU[0];// 課程名稱
				cuClass = CU[1];// 課程班級
				Log.i(LOG, "cuName >>>" + cuName);
				Log.i(LOG, "cuClass >>>" + cuClass);
				DCM = new DiaLogClassManage(context, EPListChild.get(EPListHeader.get(groupPosition)).get(childPosition));
				DCM.setButton(Listener);
				DCM.show();
				return false;
			}
		});
	}

	private Button.OnClickListener Listener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.i(LOG, "id >>" + v.getId());
			switch (v.getId()) {
			case R.id.BT_CardRegister:
			Log.i(LOG, "BT_CardRegister");
			Intent(context, CardRegister.class, cuName, cuClass,YEAR);
			DCM.cancel();
			break;
			// 缺曠紀錄
			case R.id.BT_StudentRecord:
			Log.i(LOG, "BT_StudentRecord");
			List<RollCall_StudentVO> RollCall_Student = rollCall_StudentManager.getAllRollCall_Student();

			Intent(context, StudentRecord.class, cuName, cuClass,YEAR);
			DCM.cancel();
			break;
			// 同步資料
			case R.id.BT_SynchronizeData:
			DUA = new DialogDataUpdateAndDowload(context, cuName, cuClass);
			DUA.setButton(Listener);
			DUA.show();
			break;
			case R.id.BT_Leave:
			DCM.cancel();
			break;
			case R.id.IB:
			DLS = new DiaLogLeaveStyle(context, "確定上傳資料?");
			DLS.setButton(Listener);
			DLS.show();
			K = new String("上傳");
			break;
			case R.id.IB1:
			DLS = new DiaLogLeaveStyle(context, "確定下載資至手機?");
			DLS.setButton(Listener);
			DLS.show();
			K = new String("下載");
			break;
			case R.id.BT_YES:
			DLS.cancel();
			ConnectionTest();

			break;
			case R.id.BT_NO:
			DLS.cancel();
			ConnectionTest();

			break;
			}

		}
	};

	Handler Clienttest = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//TS.ToastSet("連線成功", context, view, Toast.LENGTH_LONG);
			UpdateThread();
		}
	};

	private void UpdateThread() {
		switch (K) {
		case "上傳":
		PBS.setText("資料上傳中請稍後..");
		break;
		case "下載":
		PBS.setText("資料下載中請稍後..");
		}
		Data = new Thread(new Runnable() {
			@Override
			public void run() {
				List<CurriculumVO> curruculumData = curriculummanager.getCurriculumByClassName(cuName, cuClass,YEAR);
				switch (K) {
				case "上傳":
				// List<CurriculumVO> curruculumData =
				// curriculummanager.getCurriculumByClassName(cuName, cuClass);
				String Data = curruculumData.get(0).getCurriculum_NAME() + "!"
						+ curruculumData.get(0).getCurriculum_CLASS() + "!"
						+ curruculumData.get(0).getCurriculum_SEASON() + "!"
						+ curruculumData.get(0).getCurriculum_STD_Data();
				Log.i(LOG, "Vlue >>>" + Data);
				String ConnectionTest = UpdataHttpClient.executeQuery(Data, "6");
				break;

				case "下載":
				String cData = curruculumData.get(0).getCurriculum_NAME() + "!"
						+ curruculumData.get(0).getCurriculum_CLASS() + "!"
						+ curruculumData.get(0).getCurriculum_SEASON();
				String Connection = UpdataHttpClient.executeQuery(cData, "7");
				try {
					JSONArray jsonArray = new JSONArray(Connection);

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonData = jsonArray.getJSONObject(i);
						
						if (jsonData.get("st_card") != null && !"".equals(jsonData.get("st_card"))) {
							// 比對學生資料進行update
							List<CurriculumVO> cu = curriculummanager.getAllCurriculum();
							for (int x = 0; x < cu.size(); x++)
							{
								if(!"無資料".equals(cu.get(x).getCurriculum_STD_Data())){
								
								JSONArray stdData = new JSONArray(cu.get(x).getCurriculum_STD_Data());

								if (stdData != null) {
									for (int z = 0; z < stdData.length(); z++)
									{
										JSONObject Datas = new JSONObject();
										Datas = stdData.getJSONObject(z);
										if (Datas.get("st_number").equals(jsonData.get("st_number")))
										{
											stdData.put(z, jsonData);
										}
									}
								}

								CurriculumVO Curriculum = new CurriculumVO();
								Curriculum = cu.get(x);
								if (stdData != null) {
									Curriculum.setCurriculum_STD_Data(stdData.toString());
								} else {
									Curriculum.setCurriculum_STD_Data("無資料");
								}
								
								Log.i(LOG, "Curriculum status >>" + curriculummanager.update(Curriculum));
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				toast2.sendEmptyMessage(0);
				break;
				}
			}
		});
		Data.start();

	}

	Handler toast2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			TS.ToastSet("下載完成", context, view, Toast.LENGTH_LONG);
			PBS.cancel();
		}
	};

	private void EPListDataSet() {
		Log.i(LOG, "EPListDataSet Start >>>");
		EPListHeader = new ArrayList<String>();
		EPListChild = new HashMap<String, List<String>>();
		ArrayList<String> child = new ArrayList<String>();
		List<SeasonYearVO> SeasonYear = seasonyearmanager.getAllSeasonYear();
		List<CurriculumVO> curruculumData = curriculummanager.getCurriculumByUserName(LoginUserName);

		String Season = null;
		Log.i(LOG, "curruculumData >>>" + curruculumData.size());
		for (int i = 0; i < SeasonYear.size(); i++)
		{
			EPListHeader.add(SeasonYear.get(i).getSeasonYear_SEASON());// 標題Array 塞入
																																	// 學年度

			String[] SEEason = SeasonYear.get(i).getSeasonYear_SEASON().split("學年度");
			if ("上學期".equals(SEEason[1])) {
				Season = new String("1");
			}
			if ("下學期".equals(SEEason[1])) {
				Season = new String("2");
			}
			String SEEasons = new String(SEEason[0] + "-" + Season);
			child = new ArrayList<String>();
			for (int x = 0; x < curruculumData.size(); x++)
			{
				if (SEEasons.equals(curruculumData.get(x).getCurriculum_SEASON()))
				{
					String Cu_Name = curruculumData.get(x).getCurriculum_NAME();
					String Cu_CLASS = curruculumData.get(x).getCurriculum_CLASS();
					child.add(Cu_Name.trim() + "\r\n" + Cu_CLASS.trim());
				}
			}
			EPListChild.put(EPListHeader.get(i), child);
		}

		Log.i(LOG, "EPListDataSet End >>>");
	}

	private void Intent(Context C, Class S, String cuname, String cuClass ,String YEAR) {
		Log.i(LOG, "Intent Start");
		Intent i = new Intent();
		Bundle bundle = new Bundle(); // new一個Bundle物件，並將要傳遞的資料傳入
		bundle.putString("cuName", cuname);
		bundle.putString("cuClass", cuClass);
		bundle.putString("cuYear", YEAR);
		i.putExtras(bundle);// 將Bundle物件assign給intent
		i.setClass(C, S);
		C.startActivity(i);

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

				isRunning = true;
				Log.i(LOG, "連線測試   Start>>");
				String ConnectionTest = UpdataHttpClient.executeQuery(null, "4");
				Log.i(LOG, "ConnectionTest >>" + ConnectionTest);
				if (ConnectionTest.toString().trim().equals("0")) {
					Log.i(LOG, "Connection Success! >>");
					//PBS.cancel();
					Clienttest.sendEmptyMessage(0);
					Log.i(LOG, "連線測試   End>>");
				} else if (ConnectionTest.toString().trim().equals("-1")) {
					Log.i(LOG, "Connection Fail! >>");
					//PBS.cancel();
					isRunning = false;
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

	@Override
	protected void onStop() {
		Log.i(LOG, "onStop");
		super.onStop();
	}

}
