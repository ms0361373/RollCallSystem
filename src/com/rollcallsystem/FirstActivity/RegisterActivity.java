package com.rollcallsystem.FirstActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.rollcallsystem.R;
import com.rollcallsystem.CustomInterface.DiaLogLeaveStyle;
import com.rollcallsystem.CustomInterface.ToastStyle;
import com.rollcallsystem.DB.Manage.ALLManager;
import com.rollcallsystem.DB.Manage.UserManager;
import com.rollcallsystem.DB.VO.UserVO;
import com.rollcallsystem.nfc.manage.NfcManage;

@TargetApi(Build.VERSION_CODES.GINGERBREAD) public class RegisterActivity extends Activity {
	private static final String LOG = "RegisterActivity";
	private EditText[] ET = new EditText[4];
	private Button BT;
	private DiaLogLeaveStyle DLS;
	private ALLManager AllManager;
	private Context context;
	private UserManager usermanger;
	private View view;
	private ToastStyle TS;
	private NfcManage nfcManage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(LOG, "onCreate Start>>>");
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ragister_activity);
		
		LayoutInflater inflater = getLayoutInflater();
		view = inflater.inflate(R.layout.toast_layout,(ViewGroup) findViewById(R.id.toast_layout));
		
		Log.i(LOG, "allmanager Start>>");
		try {
			context = RegisterActivity.this;
			AllManager = new ALLManager(context);
			usermanger = AllManager.getUserManager();
			TS = new ToastStyle();
		} catch (Exception e) {
			Log.e(LOG, "Vo error !");
			e.printStackTrace();
		}
		
		Log.i(LOG, "NfcManager Start>>");
		
		NfcManager();
		
		Log.i(LOG, "EditTextintio Start>>");
		
		EditTextintio();
		
		Log.i(LOG, "Buttonintio Start>>");
		
		Buttonintio();
		Log.i(LOG, "onCreate End");
	}

	private void NfcManager() {
		NfcManager manager = (NfcManager) getSystemService(NFC_SERVICE);
		nfcManage = new NfcManage();
		nfcManage.NFCManager(manager, context);
	}

	private void Buttonintio() {
		Log.i(LOG, "Buttonintio Start>>");
		BT = (Button) findViewById(R.id.BT_Save);
		BT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < ET.length; i++) {
					if ("".equals(ET[i].getText().toString().trim())) {
						TS.ToastSet("資料錯誤！", context, view, Toast.LENGTH_SHORT);
						break;
					}
					if (i == ET.length - 1) {
						DLS = new DiaLogLeaveStyle(context, "確定儲存?");
						DLS.setButton(Listener);
						DLS.show();
					}
				}
			}
		});
		Log.i(LOG, "Buttonintio End");
	}

	private Button.OnClickListener Listener = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.BT_YES:
				String[] content = new String[4];
				boolean Lock = false;
				for (int i = 0; i < ET.length; i++) {
					if (ET[i].getText().equals(null)
							|| ET[i].getText().equals("")) {
						System.out.print("資料不齊全  ！");
					} else {
						content[i] = ET[i].getText().toString();
						if (i == content.length - 1) {
							Log.i(LOG, "Lock true>>");
							Lock = true;
						}
					}
				}
				Boolean status = false;
				if (Lock == true) {
					Log.i(LOG, "Lock true>>");
					UserVO userVO = new UserVO();
					userVO.setUSER_NAME(content[0]);
					userVO.setUSER_ID(content[1]);
					userVO.setUSER_EMAIL(content[2]);
					userVO.setUSER_CARD_ID(content[3]);
					status=usermanger.add(userVO);
				}
				if(status.equals(true)){TS.ToastSet("使用者建立成功！", context, view, Toast.LENGTH_SHORT);RegisterActivity.this.finish();}
				else if(status.equals(false)){TS.ToastSet("已存在使用者。", context, view, Toast.LENGTH_SHORT);DLS.cancel();}
				break;

			case R.id.BT_NO:
				DLS.cancel();
				break;
			}

		}
	};

	private void EditTextintio() {
		Log.i(LOG, "EditTextintio Start>>");
		ET[0] = (EditText) findViewById(R.id.user_nametext);
		ET[1] = (EditText) findViewById(R.id.user_edit);
		ET[2] = (EditText) findViewById(R.id.ET_Email);
		ET[3] = (EditText) findViewById(R.id.number_user);
		Log.i(LOG, "EditTextintio End");
	}

	public void onPause() {
		super.onPause();
		nfcManage.disableForegroundDispatch(this);
	}

	public void onResume() {
		super.onResume();
		nfcManage.enableForegroundDispatch(this);
	}

	 public void onNewIntent(Intent intent) {
	
	 byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
	 ET[3].setText(String.valueOf(getReversed(tagId)));
	 }
	/**
	 * 轉換碼
	 * 
	 * @param bytes
	 * @return
	 */
	private long getReversed(byte[] bytes) {
		long result = 0;
		long factor = 1;
		for (int i = bytes.length - 1; i >= 0; --i) {
			long value = bytes[i] & 0xffl;
			result += value * factor;
			factor *= 256l;
		}
		return result;
	}
}
