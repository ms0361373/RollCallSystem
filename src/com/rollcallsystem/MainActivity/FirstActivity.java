package com.rollcallsystem.MainActivity;

import com.example.rollcallsystem.R;
import com.rollcallsystem.CustomInterface.ToastStyle;
import com.rollcallsystem.FirstActivity.RegisterActivity;
import com.rollcallsystem.FirstActivity.SetupActivity;
import com.rollcallsystem.spf.manage.SPFManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FirstActivity extends Activity {
	private static final String LOG = "FirstActivity";
	private Button[] BT = new Button[4];
	private Dialog Dig;
	private Context context = FirstActivity.this;
	private ToastStyle TS;
	private View view;
	private SPFManager Spfmanager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(LOG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		 LayoutInflater inflater = getLayoutInflater();
		 view  = inflater.inflate(R.layout.toast_layout,(ViewGroup) findViewById(R.id.toast_layout));
		 Spfmanager = new SPFManager(context);
		 TS=new  ToastStyle();
		Buttoninto();
	}

	private void Buttoninto() {
		Log.i(LOG, "Buttoninto");
		BT[0] = (Button) findViewById(R.id.BT_lOGIN);
		BT[1] = (Button) findViewById(R.id.BT_registered);
		BT[2] = (Button) findViewById(R.id.BT_SetUp);
		BT[3] = (Button) findViewById(R.id.BT_Introduction);
		for (int i = 0; i < BT.length; i++) {
			BT[i].setOnClickListener(OnClick);
		}

	}

	private OnClickListener OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.BT_lOGIN:
				Intent(context, SecondActivity.class);
				break;
			case R.id.BT_registered:
				Dialog(context, "權限登入");
				break;
			case R.id.BT_SetUp:
				Intent(context, SetupActivity.class);
				break;
			case R.id.BT_Introduction:
				break;
			}

		}
	};

	@SuppressWarnings("rawtypes")
	private void Intent(Context C, Class S) {
		Log.i(LOG, "Intent Start");
		Intent i = new Intent();
		i.setClass(C, S);
		C.startActivity(i);

	}

	private void Dialog(Context C, String Title) {
		
		Log.i(LOG, "Dialog Start");
		Dig = new Dialog(C);
		Dig.setTitle(Title);
		Dig.setCancelable(false);
		Dig.setContentView(R.layout.access_dialog);
		Button btok = (Button) Dig.findViewById(R.id.btok);
		Button btcancel = (Button) Dig.findViewById(R.id.btcancel);
		btok.setOnClickListener(logbt);
		btcancel.setOnClickListener(logbt);
		Dig.show();

	}

	private Button.OnClickListener logbt = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btok:
				EditText ET_A = (EditText) Dig.findViewById(R.id.ET_account);
				EditText ET_P = (EditText) Dig.findViewById(R.id.ET_password);
				if ("root".equals(ET_A.getText().toString().trim())) {
					if ("123".equals(ET_P.getText().toString().trim())) {
						TS.ToastSet("登入成功！", context, view, Toast.LENGTH_SHORT);
						Intent(context, RegisterActivity.class);
						Dig.cancel();
					} else
						TS.ToastSet("密碼錯誤！", context, view, Toast.LENGTH_SHORT);
					
				} else
					TS.ToastSet("無此帳號! ", context, view, Toast.LENGTH_SHORT);
				break;
			case R.id.btcancel:
				Dig.cancel();
				break;
			}

		}
	};
	@Override
	protected void onDestroy() {
		Spfmanager.removeAll();
		super.onDestroy();
	}

	

}
