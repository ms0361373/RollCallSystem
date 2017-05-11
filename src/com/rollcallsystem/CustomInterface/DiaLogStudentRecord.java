package com.rollcallsystem.CustomInterface;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.rollcallsystem.R;
import com.rollcallsystem.DB.VO.RollCall_DateVO;
import com.rollcallsystem.DB.VO.RollCall_StudentVO;

public class DiaLogStudentRecord {
	private Dialog dialog;
	private Context context;
	private ListView LV;
	private Button BT_Leave;
	private TextView TV_RateSubTitle;
	private ArrayAdapter<String> listAdapter;
	private StudentRecordDialogListAdapter SRDL;
	public DiaLogStudentRecord(Context context)
	{
		this.context = context;
	}
	
	public DiaLogStudentRecord(Context context,String Rate,List<RollCall_DateVO> dateList,List<String> studentList)
	{ 
		Log.i("DiaLogStudentRecord", "出席率 >>>" + Rate);
		Log.i("DiaLogStudentRecord", "dateList.size() >>>" + dateList.size());
		this.context = context;
		dialog = new Dialog(context);
		//dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.studentrecord_dialog);
		LV = (ListView) dialog.findViewById(R.id.LV);
		TV_RateSubTitle = (TextView) dialog.findViewById(R.id.TV_RateSubTitle);
		TV_RateSubTitle.setText(Rate);
		SRDL = new StudentRecordDialogListAdapter(context,dateList,studentList);
		LV.setAdapter(SRDL);
		BT_Leave= (Button) dialog.findViewById(R.id.BT_Save);
	}
	
	
	public void setButton(Button.OnClickListener Listener) {
			BT_Leave.setOnClickListener(Listener);
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
