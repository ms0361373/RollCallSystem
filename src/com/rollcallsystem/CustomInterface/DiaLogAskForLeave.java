package com.rollcallsystem.CustomInterface;

import java.util.ArrayList;

import com.example.rollcallsystem.R;
import com.rollcallsystem.DB.Manage.RollCall_StudentManager;
import com.rollcallsystem.DB.VO.RollCall_StudentVO;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class DiaLogAskForLeave
{
	private Dialog dialog;
	private EditText[] ET = new EditText[2];
	private Spinner SP;
	private Button BT;
	private Context context;
	private RollCall_StudentManager rollCall_StudentManager;
	private String[] vacation = {"公假", "事假", "病假", "喪假", "其他"};
	private ArrayAdapter<String> vacationList; 
	public DiaLogAskForLeave(Context conText) {
		this.context = conText;
	}

	public DiaLogAskForLeave(Context conText, final String Curriculum_ID,final String DateID,final String StudentID){
		this.context = conText;
		rollCall_StudentManager = new RollCall_StudentManager(context);
		dialog = new Dialog(context);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogaskforleave);
		SP = (Spinner)  dialog.findViewById(R.id.SP);
		vacationList = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, vacation);
		SP.setAdapter(vacationList);
		
		ET[0] = (EditText)  dialog.findViewById(R.id.ET_1);
		ET[1] = (EditText)  dialog.findViewById(R.id.ET_2);
		BT = (Button) dialog.findViewById(R.id.BT);
		BT.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				Log.i("DLAFL", "DALFL BT >>");
				
				RollCall_StudentVO RollCall_Student = new RollCall_StudentVO();
				RollCall_Student.setRollCall_DateColumn_Curriculum_ID(Curriculum_ID);
				RollCall_Student.setRollCall_DateColumn_DateID(DateID);
				RollCall_Student.setRollCall_DateColumn_StudentID(StudentID);
				RollCall_Student.setRollCall_DateColumn_StudentRollCallDate(SP.getSelectedItem().toString()+",分數:"+ET[0].getText().toString());
				RollCall_Student.setRollCall_DateColumn_AttendanceRate("0");
				Log.i("DiaLogAskForLeave", "RollCall_Student status >>>" + rollCall_StudentManager.update(RollCall_Student));// 更新資料方法誤刪
				dialog.cancel();
			}
		});
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
