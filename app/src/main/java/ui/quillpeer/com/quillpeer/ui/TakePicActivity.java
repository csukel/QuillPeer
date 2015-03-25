package ui.quillpeer.com.quillpeer.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.devspark.appmsg.AppMsg;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import core.ImageProcessing;
import core.People.User;
import core.Server.ServerComm;
import ui.quillpeer.com.quillpeer.R;

/**
 * This class defines the behaviour of the screen which is used by the user to take a profile picture or update the existing.
 * Created by loucas on 18/11/2014.
 * @author Loucas Stylianou
 */
public class TakePicActivity extends Activity {
    /** temp file location to store the result from image capture using the camera app */
    private static final String TEMP_PHOTO_FILE = "tmp_ihis.jpg";
    /** image view which displays the captured image */
    private ImageView profilePic;
    /** the button which launches the camer app */
    private ImageButton btnCallCameraApp;
    /** this button is used to store the image and continue to the app */
    private ImageButton btnAcceptPic;
    /** this bitmap object is used to store the captured image */
    private Bitmap capturedImage = null;
    /** keep track of cropping intent */
    final int PIC_CROP = 2;
    /** uri to pass to intent camera.crop to pass the captured image */
    private Uri picUri;
    /** temp file */
    private File tempFile;
    /** a toast object to display small info msges to the user */
    private Toast m_currentToast;
    /** handler responsible for checking if the user has taken a picture and display an info msg accordingly */
    private Handler handleTakenPicMsg;
    private AppMsg alertTakePicture;
    /** the name of the screen from which this activity was launched */
    private String prevScreen;

    /**
     * Initialise the UI of this screen
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_pic_activity);
        try{
            getActionBar().setTitle("Plese take a profile picture!");
            Intent prevIntent = getIntent();
            prevScreen = prevIntent.getStringExtra("activity");
            if (prevScreen!=null && prevScreen.equals("login")){
                handleTakenPicMsg = new Handler();
                handleTakenPicMsg.postDelayed(checkForTakenPicture,500);
            }
        }catch(NullPointerException ex){
            Log.e("TakePicActivity: Error",ex.toString());
        }
        //initialise objects to resources
        initializeViewResources();


    }

    Runnable checkForTakenPicture = new Runnable() {
        /** Check if a picture is captured. If it is false then display an informative message to the user */
        @Override
        public void run() {
            if (capturedImage==null){
                AppMsg.makeText(TakePicActivity.this,"You have to take a profile picture to continue to the application...",AppMsg.STYLE_INFO).show();
                handleTakenPicMsg.postDelayed(checkForTakenPicture,500);
            } else AppMsg.cancelAll();
        }
    };

    @Override
    public void onResume(){
        super.onResume();

    }
    /** Connect the objects with the corresponding resources in take_pic_activity.xml */
    private void initializeViewResources() {
        profilePic = (ImageView)findViewById(R.id.imgProfilePic);
        btnCallCameraApp = (ImageButton)findViewById(R.id.btnCallCameraApp);
        btnAcceptPic = (ImageButton)findViewById(R.id.btnAcceptPhoto);
        //set button listeners to corresponding button objects
        btnCallCameraApp.setOnClickListener(btnCallCameraAppOnClickListener);
        btnAcceptPic.setOnClickListener(btnAcceptPicOnClickListener);
        if (User.getInstance().getProfilePicture()!=null){
            profilePic.setImageBitmap(User.getInstance().getProfilePicture());
        }
    }

    /** Listen to click events for the button which triggers the camer app */
    View.OnClickListener btnCallCameraAppOnClickListener = new View.OnClickListener() {
        /**
         * If the button is clicked then launch the camer app
         * @param v View
         */
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            createFileTempFile();
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    getTempUri());
            intent.putExtra("outputFormat",
                    Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(intent, 0);


        }
    };

    /**
     * This method defines the screen behaviour when it returns from the camera app activity or the crop image activity.
     * @param requestCode
     * @param resultCode
     * @param data Data including the image
     */
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
                //set image file to profile pic image view
                profilePic.setImageBitmap(capturedImage);
                //delete temp file
                try {
                    getTempFile().delete();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED){
                //delete temp file
                try {
                    getTempFile().delete();
                } catch (Exception ex){
                    ex.printStackTrace();
                }
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

    /**
     * Create a temporary file on the device's memory
     */
    private void createFileTempFile(){
        tempFile = new File(Environment.getExternalStorageDirectory(),
                TEMP_PHOTO_FILE);
        try {
            tempFile.createNewFile();
        } catch (IOException e) {
            Log.e("getTempFile()->", e.getMessage().toString());
        }

    }

    /**
     * Get the uri of the file
     * @return
     */
    private Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    /**
     * Get the temporary file object
     * @return tempFile
     */
    private File getTempFile() {

        return tempFile;
    }

    /**
     * Launch the activity which is responsible to perform image cropping
     */
    private void performCrop() {

        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("scale", true);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("FaceDetection", true);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    getTempUri());
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /** Listens to click events for the accept button */
    View.OnClickListener btnAcceptPicOnClickListener = new View.OnClickListener() {
        /**
         * When the accept button is pressed check if a picture is captured and then send it to the server and
         * continue to next screen.
         * @param v
         */
        @Override
        public void onClick(View v) {

            //move to main activity

            if (capturedImage!=null) {
                String image_str = ImageProcessing.encodeImage(capturedImage);
                sendPictureToServer(image_str);
/*                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                //close this activity
                finish();*/
            }else if (User.getInstance().getProfilePicture()!=null){
                finish();
            }
            else showToast("Please take a picture...", Toast.LENGTH_SHORT);
        }
    };


    /**
     * Display info messages to the user
     * @param text Message
     * @param toast_length Duration
     */
    void showToast(String text,int toast_length)
    {
        if(m_currentToast != null)
        {
            m_currentToast.cancel();
        }
        m_currentToast = Toast.makeText(this, text,toast_length);
        m_currentToast.show();

    }

    /**
     * Send the profile picture to the server.
     * @param image_str
     */
    private void sendPictureToServer(final String image_str) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            ProgressDialog dialog = ProgressDialog.show(TakePicActivity.this,
                    "Uploading image...", "Please wait...", false);

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                dialog.show();
            }

            @Override
            protected String doInBackground(String... params) {

                String paramImage = params[0];


                return ServerComm.savePicture(paramImage);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                dialog.dismiss();
                JSONObject jsonObject=null;
                boolean outcome = false;
                try {
                    jsonObject = new JSONObject(result);
                    outcome= jsonObject.getBoolean("successful");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //if the picture has been uploaded successfully ...
                if (outcome){
                    //save the image in the user profile picture var
                    User.getInstance().setProfilePicture(ImageProcessing.decodeImage(image_str));
                    //step to the main activity
                    Intent previousIntent = getIntent();
                    if (previousIntent!=null && previousIntent.getStringExtra("activity")!=null){
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        //close this activity
                        finish();
                    }
                    else finish();

                }
                else{
                    showToast("Failed to upload image ...",Toast.LENGTH_SHORT);
                }
            }
        }
        //check the network state and proceed if there is internet connection
        if (ServerComm.isNetworkConnected(getApplicationContext(),this)){
            SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
            sendPostReqAsyncTask.execute(image_str);
        }else showToast("Check your internet connection...",Toast.LENGTH_SHORT);
    }


}
