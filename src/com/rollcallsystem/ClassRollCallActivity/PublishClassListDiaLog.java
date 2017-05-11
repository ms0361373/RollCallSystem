package com.rollcallsystem.ClassRollCallActivity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.rollcallsystem.R;
import com.rollcallsystem.CustomInterface.DiaLogClassDataListAdapter;
import com.rollcallsystem.DB.Manage.RollCall_StudentManager;
import com.rollcallsystem.DB.VO.CurriculumVO;
import com.rollcallsystem.DB.VO.RollCall_StudentVO;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;

public class PublishClassListDiaLog {
private Dialog dialog;
private Context context;
private ListView LV;
private ListAdapter LA;
private List<String> stdName, stdId,stdisRollcall;
private Button BT;
private List<String> list;
private CheckBox cbx;
private List<CurriculumVO> curruculumData;
private RollCall_StudentManager rollCall_StudentManager;
public PublishClassListDiaLog(Context context, List<CurriculumVO> CurriculumVO)
{
	this.context=context;
	this.curruculumData = CurriculumVO;
	dialog = new Dialog(context);
	dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	dialog.setContentView(R.layout.publishclasslistdialog);
	LV = (ListView) dialog.findViewById(R.id.LV);
	BT = (Button) dialog.findViewById(R.id.BT_Publish);
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
	
	LV.setAdapter(new DiaLogPublishClassListAdapter(context, stdName, stdId));
	list = new ArrayList<String>();
	LV.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View v, int position,
				long _id) {
		
			cbx = (CheckBox) v.findViewById(R.id.CB);
			if (cbx.isChecked()) {
			cbx.setChecked(false);
			boolean isRemove = list.remove(String.valueOf(position));
			if (isRemove) {
			} else {
			}
			} else {
			cbx.setChecked(true);
			boolean isAdd = list.add(String.valueOf(position));
			if (isAdd) {
			} else {
			}
			}
			
		Log.i("Log", "DiaLog >>"+list.size());
		}
	});
	
	
}

public void setButton(Button.OnClickListener Listener) {
	
		BT.setOnClickListener(Listener);
	
}
public List<String> getList() {
return list;
}
public List<String> getSTID() {
return stdId;
}
public List<CurriculumVO> getCurriculumVO() {
return curruculumData;
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
