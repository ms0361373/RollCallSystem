package com.rollcallsystem.CustomInterface;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rollcallsystem.R;
import com.rollcallsystem.DB.Manage.CurriculumManager;
import com.rollcallsystem.DB.Manage.RollCall_StudentManager;
import com.rollcallsystem.DB.VO.CurriculumVO;
import com.rollcallsystem.DB.VO.RollCall_StudentVO;

public class DiaLogClassDataList {
	private Dialog dialog;
	private Context context;
	private List<String> stdName, stdId,stdisRollcall;
	private List<CurriculumVO> curruculumData;
	private RollCall_StudentManager rollCall_StudentManager;
	private ListView LV;
	private ListAdapter LA;
	private DiaLogClassDataListAdapter DCDLA;
	public DiaLogClassDataList(Context conText) {
		this.context = conText;
	}

	public DiaLogClassDataList(Context conText, List<CurriculumVO> CurriculumVO ,String curriculum_ID,String DateId) {
		this.context = conText;
		this.curruculumData = CurriculumVO;
		rollCall_StudentManager = new RollCall_StudentManager(context);
		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.classdatalist_dialog);
		LV = (ListView) dialog.findViewById(R.id.LV);
		stdName = new ArrayList<String>();
		stdId = new ArrayList<String>();
		stdisRollcall = new ArrayList<String>();
		try {
			JSONArray stdData = new JSONArray(curruculumData.get(0).getCurriculum_STD_Data());
			for (int i = 0; i < stdData.length(); i++) {
				JSONObject jsonData = stdData.getJSONObject(i);
				stdName.add("".equals(jsonData.getString("st_name")) ? "無名氏" : jsonData.getString("st_name"));
				stdId.add("".equals(jsonData.getString("st_number")) ? "無學號" : jsonData.getString("st_number"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch blocks
			e.printStackTrace();
		}
		for(int i=0;i<stdId.size();i++)
		{
		List<RollCall_StudentVO> RollCall_Student = rollCall_StudentManager.getRollCall_StudentByCuStd(CurriculumVO.get(0).getCurriculum_NAME(), CurriculumVO.get(0).getCurriculum_CLASS(),CurriculumVO.get(0).getCurriculum_SEASON(), stdId.get(i),DateId);
		stdisRollcall.add(RollCall_Student.get(0).getRollCall_DateColumn_StudentRollCallDate().equals("")? "尚未簽到":"已簽到");
		}		
		
		
		DCDLA = new DiaLogClassDataListAdapter(context, stdName, stdId, stdisRollcall, curriculum_ID, DateId);
		LV.setAdapter(DCDLA);
		}
	
	
	
	public void show() {
		dialog.show();
	}

	public void cancel() {
		dialog.cancel();
	}

	public void dismiss() {
		dialog.dismiss();
	}
}
