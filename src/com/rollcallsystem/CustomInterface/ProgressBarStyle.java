package com.rollcallsystem.CustomInterface;

import com.example.rollcallsystem.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBarStyle {
 private ProgressBar PB;
 private 	int myProgress = 0;


private  int s=0;
 private Dialog dialog;
 private Context context;
	private TextView TV_;
	private String[] tTV ={"更新中..請稍後 .","更新中..請稍後 ..","更新中..請稍後 ..."};
	
 public ProgressBarStyle(Context context){
		this.context = context;
		dialog = new Dialog(context);
		dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.progressbar);
		TV_  =(TextView)dialog.findViewById(R.id.TV_);
		PB =(ProgressBar)dialog.findViewById(R.id.PB);
		PB.setProgress(myProgress);
		
		
 }
 public void setText(String text)
 {
	 TV_.setText(text);
 }
 
 public void sendMessage()
 {
	 myHandle.sendMessage(myHandle.obtainMessage());
  
 }
 public int getMyProgress() {
	return myProgress;
}
 public void show() {
		dialog.show();
	}

	public void cancel() {
		dialog.cancel();
	}

public void setMyProgress(int myProgress) {
	this.myProgress = myProgress;
}
 
 Handler myHandle = new Handler(){
   @Override
   public void handleMessage(Message msg) {
       myProgress++;
       s++;
       if(s==3){s=1;}
       TV_.setText(tTV[s]);
       PB.setProgress(myProgress);
   }
};
}
