package ui.quillpeer.com.quillpeer.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 18/11/2014.
 * This is the activity corresponding to login screen
 */
public class LoginActivity extends Activity {
    private Button btnLogin;
    private EditText edtUsername;
    private EditText edtPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        //connect objects to xml resources
        initializeViewResources();
    }

    private void initializeViewResources() {
        //initialize resources objects with the corresponding resources found in login_activity.xml file
        btnLogin = (Button)findViewById(R.id.btnLogin);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        //assign the click listener for login button
        btnLogin.setOnClickListener(btnLoginClickListener);
    }

    View.OnClickListener btnLoginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO check for empty text boxes (edtUsername and edtPassword) and then check with the server for user authenication
            Intent intent = new Intent(getApplicationContext(),TakePicActivity.class);
            startActivity(intent);
        }
    };
}
