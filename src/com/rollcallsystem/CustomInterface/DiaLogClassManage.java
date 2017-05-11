package com.rollcallsystem.CustomInterface;

import com.example.rollcallsystem.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DiaLogClassManage {
	private Dialog dialog;
	private Context context;
	private Button[] BT = new Button[4];
	private TextView TV_Title;
	
	public DiaLogClassManage(Context conText) {
		this.context = conText;
	}
	
	public DiaLogClassManage(Context conText, String text) {
		this.context = conText;
		dialog = new Dialog(context);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.classdatamanager_dialog);
		TV_Title = (TextView) dialog.findViewById(R.id.TV_Title);
		BT[0] = (Button) dialog.findViewById(R.id.BT_CardRegister);
		BT[1] = (Button) dialog.findViewById(R.id.BT_StudentRecord);
		BT[2] = (Button) dialog.findViewById(R.id.BT_SynchronizeData);
		BT[3] = (Button) dialog.findViewById(R.id.BT_Leave);
		TV_Title.setText(text);
	}
	
	public void setButton(Button.OnClickListener Listener) {
		for (int i = 0; i < BT.length; i++) {
			BT[i].setOnClickListener(Listener);
		}
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
