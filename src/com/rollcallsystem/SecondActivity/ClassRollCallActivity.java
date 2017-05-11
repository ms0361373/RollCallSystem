package com.rollcallsystem.SecondActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.rollcallsystem.R;
import com.rollcallsystem.ClassRollCallActivity.RollCallActivity;
import com.rollcallsystem.CustomInterface.DiaLogClassManage;
import com.rollcallsystem.CustomInterface.DiaLogLeaveStyle;
import com.rollcallsystem.CustomInterface.ExpandableListAdapter;
import com.rollcallsystem.DB.Column.UserColumn;
import com.rollcallsystem.DB.Manage.CurriculumManager;
import com.rollcallsystem.DB.Manage.SeasonYearManager;
import com.rollcallsystem.DB.Manage.UserManager;
import com.rollcallsystem.DB.VO.CurriculumVO;
import com.rollcallsystem.DB.VO.SeasonYearVO;
import com.rollcallsystem.DB.VO.UserVO;
import com.rollcallsystem.spf.manage.SPFManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class ClassRollCallActivity extends Activity {
	private static final String LOG = "ThirdActivity";
	private View view;
	private Context context;
	private String LoginUserName,cuName,cuClass,YEAR,year;
	private SPFManager Spfmanager;
	private CurriculumManager curriculummanager;
	private UserManager usermanager;
	private SeasonYearManager seasonyearmanager;
	private ExpandableListAdapter EPListAdapter;
	private DiaLogLeaveStyle DLS;
	private ExpandableListView EPListView;
	private List<String> EPListHeader; // 標題
	private HashMap<String, List<String>> EPListChild;// 內容
	private DiaLogClassManage DCM;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(LOG, "onCreate Start >>>");
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classrollcallactivity);
		LayoutInflater inflater = getLayoutInflater();
		view = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout));
		try {
			context = ClassRollCallActivity.this;
			curriculummanager = new CurriculumManager(context);
			Spfmanager = new SPFManager(context);
			usermanager = new UserManager(context,Spfmanager);
			seasonyearmanager = new SeasonYearManager(context);
		
			UserVO user = usermanager.getUserById(Integer.valueOf(Spfmanager.load(UserColumn.TABLE_NAME + UserColumn._ID)));
			LoginUserName = user.getUSER_NAME();
			setTitle("點名系統-"+LoginUserName);
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
				cuName = CU[0];//課程名稱
				cuClass = CU[1];//課程班級
				Log.i(LOG, "cuName >>>" + cuName);
				Log.i(LOG, "cuClass >>>" + cuClass);
				DLS = new DiaLogLeaveStyle(context, "進入課程?");
				DLS.setButton(Listener);
				DLS.show();
				return false;
			}
		});
		
	}
	private Button.OnClickListener Listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		switch (v.getId()) {
		case R.id.BT_YES:
		Intent(context, RollCallActivity.class, cuName, cuClass,YEAR);
		DLS.cancel();
		break;
		case R.id.BT_NO:
		DLS.cancel();
		break;
		}
			
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
					child.add(Cu_Name.trim() +"\r\n"+ Cu_CLASS.trim());
				}
			}
			EPListChild.put(EPListHeader.get(i), child);
		}

		Log.i(LOG, "EPListDataSet End >>>");
	}

	@SuppressWarnings("rawtypes")
	private void Intent(Context C, Class S,String cuname,String cuClass,String YEAR) {
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
	
	@Override
	protected void onStop() {
		Log.i(LOG, "onStop");
		super.onStop();
		finish();
	}

}
