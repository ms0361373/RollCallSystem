package com.rollcallsystem.CustomInterface;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rollcallsystem.R;

public class ToastStyle {
	private Context Context;
	/**
	 * Toast自訂義
	 * @param Text 顯示的文字
	 * @param context 
	 * @param v setview
	 * @param time 時間長短
	 */
	public void ToastSet(String Text, Context context,View v,int time) {
		this.Context = context;
		TextView text = (TextView) v.findViewById(R.id.TV);
		text.setText(Text);
		Toast toast = new Toast(Context);
		toast.setDuration(time); 
		toast.setView(v);
		toast.show();
	}
}
