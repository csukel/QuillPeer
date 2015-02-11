package ui.quillpeer.com.quillpeer.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 18/11/2014.
 * This class defines the Take a picture screen which is intended to be used by the user to take a profile picture
 */
public class TakePicActivity extends Activity {
    private static final String TEMP_PHOTO_FILE = "tmp_ihis.jpg";
    private ImageView profilePic;
    private ImageButton btnCallCameraApp;
    private ImageButton btnAcceptPic;
    private Bitmap capturedImage = null;
    //keep track of cropping intent
    final int PIC_CROP = 2;
    //uri to pass to intent camera.crop to pass the captured image
    private Uri picUri;
    private Toast m_currentToast;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_pic_activity);
        try{
            getActionBar().setTitle("Plese take a profile picture!");
        }catch(NullPointerException ex){
            Log.e("TakePicActivity: Error",ex.toString());
        }
        //initialise objects to resources
        initializeViewResources();


    }
    //connect the objects with the corresponding resources in take_pic_activity.xml
    private void initializeViewResources() {
        profilePic = (ImageView)findViewById(R.id.imgProfilePic);
        btnCallCameraApp = (ImageButton)findViewById(R.id.btnCallCameraApp);
        btnAcceptPic = (ImageButton)findViewById(R.id.btnAcceptPhoto);
        //set button listeners to corresponding button objects
        btnCallCameraApp.setOnClickListener(btnCallCameraAppOnClickListener);
        btnAcceptPic.setOnClickListener(btnAcceptPicOnClickListener);
    }

    View.OnClickListener btnCallCameraAppOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    getTempUri());
            intent.putExtra("outputFormat",
                    Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, 0);


        }
    };

    //when the camera activity is closed check the result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request is image
        if (requestCode == 0) {
            //and the user has successfully taken a picture
            if (resultCode == RESULT_OK) {
                File tmpFile =getTempFile();
                picUri = Uri.fromFile(tmpFile);
                performCrop();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                showToast("Please take a picture and then crop it accordingly...",Toast.LENGTH_SHORT);
            } else {
                // Image capture failed, advise user
                showToast("Image capture failed. Please try again...",Toast.LENGTH_SHORT);

            }
        }
        //if it returned back from crop activity do..
        else if (requestCode==PIC_CROP){
            //crop successful
            if (resultCode == RESULT_OK){
                if (data != null) {
                    Bundle extras = data.getExtras();
                    capturedImage = extras.getParcelable("data");
                }

                profilePic.setImageBitmap(capturedImage);

                File tempFile = getTempFile();
                File cacheDir = getCacheDir();
                File file = new File(cacheDir, getTempFile().toString());
                file.delete();
            }
            //crop cancelled by user
            else if (resultCode==RESULT_CANCELED){
                showToast("Please take a picture and then crop it accordingly...",Toast.LENGTH_SHORT);
            }
            //crop action failed due to unknown reason
            else{
                //something went wrong
                // Image capture failed, advise user
                showToast("Image crop failed. Please try again to take a picture again...",Toast.LENGTH_SHORT);
            }
        }
    }
    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    private File getTempFile() {

        File f = new File(Environment.getExternalStorageDirectory(),
                TEMP_PHOTO_FILE);
        try {
            f.createNewFile();
        } catch (IOException e) {
            Log.e("getTempFile()->", e.getMessage().toString());
        }

        return f;
    }

    private void performCrop() {

        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 0);
            cropIntent.putExtra("aspectY", 0);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 512);
            cropIntent.putExtra("outputY", 512);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            //TODO if the device does not support crop then crop it ussing bitmap method for scaling
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    View.OnClickListener btnAcceptPicOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO send pic to the server
            //move to main activity
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            //close this activity
            finish();
        }
    };

    void showToast(String text,int toast_length)
    {
        if(m_currentToast != null)
        {
            m_currentToast.cancel();
        }
        m_currentToast = Toast.makeText(this, text,toast_length);
        m_currentToast.show();

    }


}
