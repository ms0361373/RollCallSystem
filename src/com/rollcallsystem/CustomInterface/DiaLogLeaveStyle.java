package com.rollcallsystem.CustomInterface;

import com.example.rollcallsystem.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DiaLogLeaveStyle {
	private Dialog dialog;
	private Button[] BT = new Button[2];
	private EditText ET_Date;
	private Context context;
	private TextView TV;
	public DiaLogLeaveStyle(Context conText) {
		this.context = conText;
	}

	public DiaLogLeaveStyle(Context conText, String text) {
		this.context = conText;
		dialog = new Dialog(context);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogleavestyle);
		TV = (TextView) dialog.findViewById(R.id.TV);
		BT[0] = (Button) dialog.findViewById(R.id.BT_YES);
		BT[1] = (Button) dialog.findViewById(R.id.BT_NO);
		TV.setText(text);

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
