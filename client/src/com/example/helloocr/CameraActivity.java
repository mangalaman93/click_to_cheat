package com.example.helloocr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class CameraActivity extends Activity {
	
	private static final int IMAGE_CROP = 1111;
	private static final int IMAGE_DONE = 2222;
	private Bitmap photo;
	private String selectedImagePath;
	private Uri selectedImage;
	private File tempFile;
	private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView = (ImageView) findViewById(R.id.imageView);
        selectedImage = (Uri) getIntent().getExtras().get("selectedImage");
        selectedImagePath = getPath(selectedImage);
        Toast.makeText(this, selectedImagePath, Toast.LENGTH_LONG).show();
        photo = BitmapFactory.decodeFile(selectedImagePath);
    	tempFile = new File(selectedImagePath);
    	imageView.setImageBitmap(photo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_camera, menu);
        return true;
    }
    
    public void startCrop(View view){
        Crop(selectedImage);
    }
    
	public void Crop(Uri uri){
		Intent in = new Intent("com.android.camera.action.CROP");
    	in.setDataAndType(uri, "image/*");
        in.putExtra("crop", "true");
        in.putExtra("output", Uri.fromFile(tempFile));
        in.putExtra("outputFormat", "JPEG");
        startActivityForResult(in, IMAGE_CROP);		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
    	switch(requestCode){
    	case IMAGE_CROP :
    		if (resultCode == RESULT_OK && data != null ){
    			Bundle extras = data.getExtras();
    			if(extras !=null){
    				selectedImagePath = getPath(selectedImage);
    				//Toast.makeText(this,selectedImagePath , Toast.LENGTH_LONG).show();
        			Bitmap photo = BitmapFactory.decodeFile(selectedImagePath);
        			imageView.setImageBitmap(photo);
    			}
    		}
    		super.onActivityResult(requestCode, resultCode, data);
    		break;
    		
    	case IMAGE_DONE :
    		if(resultCode == 2){
    	        finish();
    	    }
    	}
	}
    	
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	
	public void goBack(View view){
		Intent in = new Intent(this, AppActivity.class);
        in.putExtra("selectedImagePath", selectedImagePath);
        in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	in.putExtra("selectedImage", selectedImage);
    	startActivityForResult(in, IMAGE_DONE);
	}
	
	public void startRotate(View view){
		Matrix matrix = new Matrix();
		matrix.postRotate(90);
		Bitmap rotatedBitmap = Bitmap.createBitmap(photo , 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
		imageView.setImageBitmap(rotatedBitmap);
		photo = rotatedBitmap;
	}
	
	public void startDone(View view) throws FileNotFoundException{
		Intent in = new Intent(this, AppActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        OutputStream outStream = new FileOutputStream(tempFile);
		photo.compress(Bitmap.CompressFormat.PNG, 100, outStream);
    	in.putExtra("selectedImage", selectedImage);
    	startActivityForResult(in, IMAGE_DONE);
	}
}
