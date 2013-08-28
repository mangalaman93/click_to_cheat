package com.example.helloocr;

import java.io.File;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

public class AppActivity extends Activity {

	private static final int CAMERA_REQUEST = 1888; 
	private static final int PHOTO_GALLERY = 1009;
	private Uri selectedImage;
	private ImageView imageView;
	private Button proceed_button;
	private Button edit_button;
	private GridLayout grid;
	private EditText question;
	private EditText option_a;
	private EditText option_b;
	private EditText option_c;
	private EditText option_d;
	
	protected void onStop() {
	    setResult(2);
	    super.onStop();
	}
	
	@Override
	protected void onDestroy() {
	    setResult(2);
	    super.onDestroy();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity);
        imageView = (ImageView) findViewById(R.id.imageView);
        proceed_button = (Button) findViewById(R.id.bt_find);
        edit_button = (Button) findViewById(R.id.bt_edit);
        grid = (GridLayout) findViewById(R.id.grid);
        question = (EditText) findViewById(R.id.question);
        option_a = (EditText) findViewById(R.id.a);
        option_b = (EditText) findViewById(R.id.b);
        option_c = (EditText) findViewById(R.id.c);
        option_d = (EditText) findViewById(R.id.d);
        proceed_button.setVisibility(View.GONE);
        edit_button.setVisibility(View.GONE);
        grid.setVisibility(View.GONE);
        selectedImage = (Uri) getIntent().getExtras().get("selectedImage");
        if(selectedImage != null){
        	proceed_button.setVisibility(View.VISIBLE);
        	edit_button.setVisibility(View.VISIBLE);
        	String selectedImagePath = getPath(selectedImage);
            Bitmap photo = BitmapFactory.decodeFile(selectedImagePath);
        	imageView.setImageBitmap(photo);
        }
    }
	
	public void openGallery(View view){
		proceed_button.setVisibility(View.GONE);
		edit_button.setVisibility(View.GONE);
		grid.setVisibility(View.GONE);
    	Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    	startActivityForResult(Intent.createChooser(intent,"Select Picture"), PHOTO_GALLERY);
    }
    
    public void openCamera(View view){
    	edit_button.setVisibility(View.GONE);
    	proceed_button.setVisibility(View.GONE);
    	grid.setVisibility(View.GONE);
    	ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
    	selectedImage = getContentResolver().insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    	Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
    	startActivityForResult(intent, CAMERA_REQUEST);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
    	switch(requestCode){
    	case CAMERA_REQUEST :
    		if (resultCode == Activity.RESULT_OK) {
    			try {
    				startCameraActivity(selectedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
    			//Toast.makeText(this, "Image saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
    		}
    		else if(resultCode == Activity.RESULT_CANCELED){
    			Toast.makeText(this, "Capture canceled by user", Toast.LENGTH_LONG).show();
    		}
    		break;
    		
    	case PHOTO_GALLERY :
    		super.onActivityResult(requestCode, resultCode, data);
    		if (resultCode == RESULT_OK && null != data ){
    			selectedImage = data.getData();
                startCameraActivity(selectedImage);
    		}
    		break;
    	}
    }

    private void startCameraActivity(Uri selectedImage) {
    	Intent in = new Intent(this,CameraActivity.class);
        in.putExtra("selectedImage", selectedImage);
        in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(in);
	}
    
    public void startEdit(View view){
    	startCameraActivity(selectedImage);
    }

	public void Search(View view){
    	imageView.setVisibility(View.GONE);
    	getText(selectedImage);
    	grid.setVisibility(View.VISIBLE);
    }
	
	public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    
    public void getText(Uri uri){
    	//Assuming the tessdata folder is in sdcard
    	String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    	Toast.makeText(this, path, Toast.LENGTH_LONG).show();
    	
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.init(path, "eng");        
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opts.inSampleSize = 2;
        Bitmap photo = BitmapFactory.decodeFile(getPath(selectedImage), opts);
        baseApi.setImage(photo);
        String recognizedText = baseApi.getUTF8Text(); // Log or otherwise display this string...
        
        String reg = "(\\(|\\[)(a|b|c|d|A|B|C|D|1|2|3|4|P|p|Q|q|R|r|S|s|)(\\)|\\])";
		String[] splitString = (recognizedText.split(reg));
		
		//Show the Question with Options
		if(splitString.length >= 5){
		   question.setText(splitString[0]);
		   option_a.setText(splitString[1]);
		   option_b.setText(splitString[2]);
		   option_c.setText(splitString[3]);
		   option_d.setText(splitString[4]);
		}
		
        baseApi.end(); 
    }
	
}
