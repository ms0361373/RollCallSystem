package com.rollcallsystem.CustomInterface;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rollcallsystem.R;

public class DialogDataUpdateAndDowload {
	private Dialog dialog;
	private Context context;
	private ImageButton[] IB = new ImageButton[2];
	public DialogDataUpdateAndDowload(Context conText, String CuName,String CuClass) {
		this.context = conText;
		dialog = new Dialog(context);
		//dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialogupdateanddowload);
		IB[0] = (ImageButton) dialog.findViewById(R.id.IB);
		IB[1] = (ImageButton) dialog.findViewById(R.id.IB1);
	}
	public void setButton(Button.OnClickListener Listener) {
		for (int i = 0; i < IB.length; i++) {
			IB[i].setOnClickListener(Listener);
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
