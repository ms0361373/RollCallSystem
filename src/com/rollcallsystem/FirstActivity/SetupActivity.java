package com.rollcallsystem.FirstActivity;

import com.example.rollcallsystem.R;
import com.rollcallsystem.Connector.UpdataHttpClient;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SetupActivity extends Activity
{

	private EditText[] ET = new EditText[4];
	private Button BT;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setupactivity);
		
		ET[0] = (EditText) findViewById(R.id.ET_01);
		ET[1] = (EditText) findViewById(R.id.ET_02);
		ET[2] = (EditText) findViewById(R.id.ET_03);
		ET[3] = (EditText) findViewById(R.id.ET_04);
		BT = (Button) findViewById(R.id.BT_Send);
		BT.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
			UpdataHttpClient.setServerIp(ET[0].getText().toString(), ET[1].getText().toString(), ET[2].getText().toString(), ET[3].getText().toString());
			}
		});
	}
	
	
	
	
}
