package com.rollcallsystem.CustomInterface;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.example.rollcallsystem.R;
import com.rollcallsystem.DB.Manage.CurriculumManager;
import com.rollcallsystem.DB.VO.CurriculumVO;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
public class DiaLogPubMessage {
	private Dialog dialog;
	private Context context;
	private EditText ET;
	private Spinner SP;                        
	private Button BT_Pub;
	private ImageButton IB;
	private ArrayAdapter<String> LT; 
	private String SPText;
	private String[] CuName;
	private CurriculumManager curriculummanager;
	
	public DiaLogPubMessage(Context conText ,String LoginUserName) {
		this.context = conText;
		dialog = new Dialog(context);
		curriculummanager = new CurriculumManager(context);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.publishmessage_dialog);
		SP = (Spinner) dialog.findViewById(R.id.SP);
		ET = (EditText) dialog.findViewById(R.id.ET);
		BT_Pub = (Button) dialog.findViewById(R.id.BT_Pub);
		IB = (ImageButton) dialog.findViewById(R.id.IB_hide);
		Log.i("DiaLogPubMessage", "LoginUserName >>"+LoginUserName);
		List<CurriculumVO> curruculumData = curriculummanager.getCurriculumByUserName(LoginUserName);
		final String[] newname = new String[curruculumData.size()];
		CuName = new String[curruculumData.size()];
		for(int i=0;i<curruculumData.size();i++)
		{
			String name = curruculumData.get(i).getCurriculum_NAME()+ curruculumData.get(i).getCurriculum_CLASS()+ curruculumData.get(i).getCurriculum_SEASON();
			newname[i]=name;
			CuName[i]=curruculumData.get(i).getCurriculum_NAME()+"\t"+curruculumData.get(i).getCurriculum_CLASS()+"\t"+curruculumData.get(i).getCurriculum_SEASON();
		}
		LT = new ArrayAdapter<String>(conText, android.R.layout.simple_spinner_item, newname);                                     
		SP.setAdapter(LT);
		SP.setOnItemSelectedListener(new OnItemSelectedListener(){

		   
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				SPText = CuName[arg2].toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	@SuppressLint("NewApi") public DiaLogPubMessage(Context conText)
	{
		this.context = conText;
		dialog = new Dialog(context);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.publishmessage_dialog);
		LinearLayout LinearLayout = (LinearLayout) dialog.findViewById(R.id.LinearLayout);
		SP = (Spinner) dialog.findViewById(R.id.SP);
		LinearLayout.setVisibility(View.GONE);
		ET = (EditText) dialog.findViewById(R.id.ET);
		BT_Pub = (Button) dialog.findViewById(R.id.BT_Pub);
		IB = (ImageButton) dialog.findViewById(R.id.IB_hide);
	}
	
	public void setButton(Button.OnClickListener Listener) {
		BT_Pub.setOnClickListener(Listener);
		IB.setOnClickListener(Listener);
}
	public String getText()
	{
		String Text = ET.getText().toString();
		return Text;
	}
	
	public String getSpText()
	{
		return SPText;
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
