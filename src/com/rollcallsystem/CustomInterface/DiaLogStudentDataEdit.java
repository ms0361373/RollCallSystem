package com.rollcallsystem.CustomInterface;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.rollcallsystem.R;
import com.rollcallsystem.DB.Manage.CurriculumManager;
import com.rollcallsystem.DB.VO.CurriculumVO;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DiaLogStudentDataEdit {
	private Dialog dialog;
	private Context context;
	private EditText[] ET = new EditText[3];

	private Button BT_Leave;
	private Button BT_S;
	
	public DiaLogStudentDataEdit(Context context)
	{
		this.context = context;
	}
	
	public  DiaLogStudentDataEdit(Context context,String Name,String ID,String CardId)
	{
		this.context = context;
		dialog = new Dialog(context);
		//dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.cardregister_editdialog);
		ET[0] = (EditText) dialog.findViewById(R.id.ET_Name);
		ET[1] = (EditText) dialog.findViewById(R.id.ET_ID);
		ET[2] = (EditText) dialog.findViewById(R.id.ET_CardID);
		ET[0].setText(Name);
		ET[1].setText(ID);
		ET[2].setText(CardId);
		BT_S = (Button)dialog.findViewById(R.id.BT_S);
		BT_S.setVisibility(View.GONE);
		BT_Leave= (Button) dialog.findViewById(R.id.BT_Save);
	}
	
	public  DiaLogStudentDataEdit(Context context,String Title,String CuName,String CuClass,String CardId)
	{
		this.context = context;
		dialog = new Dialog(context);
		//dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.cardregister_editdialog);
		TextView TV_Title = (TextView) dialog.findViewById(R.id.TV_Title);
		BT_S = (Button)dialog.findViewById(R.id.BT_S);
		BT_S.setVisibility(View.VISIBLE);
		TV_Title.setText(Title);
		
		ET[0] = (EditText) dialog.findViewById(R.id.ET_Name);
		ET[1] = (EditText) dialog.findViewById(R.id.ET_ID);
		ET[2] = (EditText) dialog.findViewById(R.id.ET_CardID);
		BT_Leave= (Button) dialog.findViewById(R.id.BT_Save);
	}
	
	public EditText[] getET() {
		return ET;
	}

	public void setET(EditText[] eT) {
		ET = eT;
	}
	
	public void setStdID(String StdID) {
		ET[1].setText(StdID);
	}
	public void setETCardID(String CardId) {
		ET[2].setText(CardId);
	}
	public void setButton(Button.OnClickListener Listener) {
			BT_Leave.setOnClickListener(Listener);
			BT_S.setOnClickListener(Listener);
	}
	
	public void show() {
		dialog.show();
	}
	public boolean isShow() {
		
		return dialog.isShowing();
	}
	public void cancel() {
		dialog.cancel();
	}

	public void dismiss() {
		dialog.dismiss();
	}
}
