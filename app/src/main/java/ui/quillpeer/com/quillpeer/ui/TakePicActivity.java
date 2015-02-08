package ui.quillpeer.com.quillpeer.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 18/11/2014.
 * This class defines the Take a picture screen which is intended to be used by the user to take a profile picture
 */
public class TakePicActivity extends Activity {
    private ImageView profilePic;
    private ImageButton btnCallCameraApp;
    private ImageButton btnAcceptPic;

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
                //get the picture and store it in a bitmap object
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                bp = Bitmap.createScaledBitmap(bp,256,256,false);
                //set image to the profilePic object
                profilePic.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }

    View.OnClickListener btnAcceptPicOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO send pic to the server
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
    };


}
