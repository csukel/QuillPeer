package ui.quillpeer.com.quillpeer.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import ui.quillpeer.com.quillpeer.R;

/**
 * This class defines the behaviour of the splash screen. This screen is shown only the first time
 * this application is launched.
 * Created by loucas on 18/11/2014.
 * @author Loucas Stylianou
 */
public class SplashActivity extends Activity {
    /** logo */
    private ImageView imgLogo;
    /** scale animation */
    private ScaleAnimation animImg;
    /** slogan animation */
    private Animation animSlogan;

    /** display duration is set to 3 secs */
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    /**
     * When this activity is created initialise the view and trigger the animations.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        SharedPreferences settings=getSharedPreferences("prefs",0);
        boolean firstRun=settings.getBoolean("firstRun",false);
        if(firstRun==false)//if running for first time
        //Splash will load for first time
        {
            SharedPreferences.Editor editor=settings.edit();
            editor.putBoolean("firstRun",true);
            editor.commit();
            imgLogo = (ImageView)findViewById(R.id.imgLogo);

            animImg = new ScaleAnimation(0,1 , 0, 1, 0.5f,0.5f);
            //Setup anim with desired properties
            animImg.setInterpolator(new LinearInterpolator());
            animImg.setRepeatCount(0); //Repeat animation indefinitely
            animImg.setDuration(1000); //Put desired duration per anim cycle here, in milliseconds
            //Start animation
            //imgLogo.startAnimation(animImg);

            animSlogan = new AlphaAnimation(0.0f, 1.0f);
            animSlogan.setDuration(500); //You can manage the time of the blink with this parameter
            animSlogan.setStartOffset(20);
            animSlogan.setRepeatMode(Animation.REVERSE);
            animSlogan.setRepeatCount(Animation.INFINITE);
            imgLogo.startAnimation(animSlogan);

            //continue to the next screen after 3 secs
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                /* Create an Intent that will start the login user activity. */
                    Intent mainIntent = new Intent(SplashActivity.this,TutorialActivity.class);
                    mainIntent.putExtra("class","SplashActivity");
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
        else
        {

            Intent a=new Intent(SplashActivity.this,LoginActivity.class);
            //a.putExtra("class","SplashActivity");
            startActivity(a);
            finish();
        }


    }
}
