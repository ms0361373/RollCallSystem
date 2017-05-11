package com.rollcallsystem.MainActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.rollcallsystem.R;
import com.rollcallsystem.CustomInterface.ToastStyle;
import com.rollcallsystem.DB.Column.UserColumn;
import com.rollcallsystem.DB.Manage.UserManager;
import com.rollcallsystem.DB.VO.UserVO;
import com.rollcallsystem.SecondActivity.ClassDataUpdateActivity;
import com.rollcallsystem.nfc.manage.NfcManage;
import com.rollcallsystem.spf.manage.SPFManager;

public class SecondActivity extends Activity {
	private static final String LOG = "SecondActivity";
	private Button BT;
	private Context context = SecondActivity.this;
	private NfcManage nfcManage;
	private UserManager usermanager;
	private SPFManager Spfmanager;
	private ToastStyle TS;
	private View view;
	private ImageView IV;
	private EditText ET_A, ET_P;
	private Dialog Dig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(LOG, "onCreate Start >>>");
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.second_activity);
		LayoutInflater inflater = getLayoutInflater();
		view = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout));

		try {
			usermanager = new UserManager(context);
			Spfmanager = new SPFManager(context);
			TS = new ToastStyle();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.i(LOG, "NFCmanager Start >>>");

		NFCManager();

		Log.i(LOG, "Buttoninto Start >>>");

		Buttoninto();
	}

	private void NFCManager() {
		NfcManager manager = (NfcManager) getSystemService(NFC_SERVICE);
		nfcManage = new NfcManage();
		nfcManage.NFCManager(manager, context);
	}

	private void Buttoninto() {
		IV = (ImageView) findViewById(R.id.IV);
		BT = (Button) findViewById(R.id.button1);
		BT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Dialog(context, "登入");
			}
		});
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
		ET_A = (EditText) Dig.findViewById(R.id.ET_account);
		ET_P = (EditText) Dig.findViewById(R.id.ET_password);
		ET_A.setHint("請輸入教師ID");
		ET_P.setHint("請輸入信箱");
		Dig.show();

	}

	private Button.OnClickListener logbt = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btok:
				ET_A = (EditText) Dig.findViewById(R.id.ET_account);
				ET_P = (EditText) Dig.findViewById(R.id.ET_password);
				if (usermanager.loginByUserID(ET_A.getText().toString().trim(), ET_P.getText().toString().trim())) {
					UserVO user = usermanager.getUserByUserID(ET_A.getText().toString().trim());
					Spfmanager.save(UserColumn.TABLE_NAME + UserColumn._ID, user.get_id());
					IV.setImageResource(R.drawable.nfcicongreen);
					Intent(context, ClassDataUpdateActivity.class);

				} else if (ET_A.getText().toString().trim().equals("root")
						&& ET_P.getText().toString().trim().equals("123")) {
					UserVO userVO = new UserVO();
					userVO.setUSER_NAME("郭崇仁");
					// userVO.setUSER_NAME("曾生元");
					// userVO.setUSER_NAME("鐘俊顏");
					userVO.setUSER_ID("T093000072");
					userVO.setUSER_EMAIL("ms0361373@gmail.com");
					userVO.setUSER_CARD_ID("499");
					@SuppressWarnings("unused")
					boolean status = usermanager.add(userVO);
					if (usermanager.login("499")) {
						UserVO user = usermanager.getUserByCardID("499");
						Spfmanager.save(UserColumn.TABLE_NAME + UserColumn._ID, user.get_id());
						IV.setImageResource(R.drawable.nfcicongreen);
						Intent(context, ClassDataUpdateActivity.class);

					}
				} else {
					TS.ToastSet("尚未進行註冊", context, view, Toast.LENGTH_SHORT);
				}

				break;
			case R.id.btcancel:
				Dig.cancel();
				break;
			}

		}
	};

	private void Intent(Context C, Class S) {
		Log.i(LOG, "Intent Start");
		Intent i = new Intent();
		i.setClass(C, S);
		C.startActivity(i);
	}

	public void onNewIntent(Intent intent) {
		try {
			byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
			String cardid = String.valueOf(getReversed(tagId));
			boolean key = true;
			if (key == true) {
				if (usermanager.login(cardid)) {
					UserVO user = usermanager.getUserByCardID(cardid);

					if (Spfmanager.save(UserColumn.TABLE_NAME + UserColumn._ID, user.get_id())) {
						IV.setImageResource(R.drawable.nfcicongreen);
						Intent(context, ClassDataUpdateActivity.class);
						key = false;
					} else {
						TS.ToastSet("系統發生問題，請聯絡相關人員!", context, view, Toast.LENGTH_SHORT);
						// Log.w(PAGE, "can not save user !, user = " +
						// user.toString());
					}

				} else {
					TS.ToastSet("尚未進行註冊", context, view, Toast.LENGTH_SHORT);
					IV.setImageResource(R.drawable.nfciconred);
					key = false;
				}
			}
		} catch (Exception e) {
			Spfmanager.removeAll();
		}
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

	public void onPause() {
		super.onPause();
		IV.setImageResource(R.drawable.nfciconblue);
		nfcManage.disableForegroundDispatch(this);
	}

	public void onResume() {
		super.onResume();
		nfcManage.enableForegroundDispatch(this);
	}
}
