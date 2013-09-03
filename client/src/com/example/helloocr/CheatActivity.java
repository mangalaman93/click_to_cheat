package com.example.helloocr;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class CheatActivity extends Activity {

	private Context context;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //view = findViewById(R.id.layout1);
        context = this;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
        	public void run() {
        		startActivity(context);
        	}
        }, 3000);
        
    }
    
    public void startActivity(Context a){
    	Intent intent = new Intent(a, AppActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	Uri selectedImage = null;
    	intent.putExtra("selectedImage", selectedImage);
    	startActivity(intent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
