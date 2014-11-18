package ui.quillpeer.com.quillpeer.ui;

import android.app.Activity;
import android.content.Intent;
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
 * Created by loucas on 18/11/2014.
 */
public class SplashActivity extends Activity {
    private ImageView imgLogo;
    private ScaleAnimation animImg;
    private Animation animSlogan;
    private TextView txtSlogan;
    private final int SPLASH_DISPLAY_LENGHT = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);


        imgLogo = (ImageView)findViewById(R.id.imgLogo);
        txtSlogan = (TextView)findViewById(R.id.txtSlogan);


        animImg = new ScaleAnimation(0,1 , 0, 1, 0.5f,0.5f);
        //Setup anim with desired properties
        animImg.setInterpolator(new LinearInterpolator());
        animImg.setRepeatCount(0); //Repeat animation indefinitely
        animImg.setDuration(1000); //Put desired duration per anim cycle here, in milliseconds
        //Start animation
        imgLogo.startAnimation(animImg);

        animSlogan = new AlphaAnimation(0.0f, 1.0f);
        animSlogan.setDuration(500); //You can manage the time of the blink with this parameter
        animSlogan.setStartOffset(20);
        animSlogan.setRepeatMode(Animation.REVERSE);
        animSlogan.setRepeatCount(Animation.INFINITE);
        txtSlogan.startAnimation(animSlogan);


        //continue to the next screen after 3 secs
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the login user activity. */
                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
