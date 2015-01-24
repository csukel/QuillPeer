package ui.quillpeer.com.quillpeer.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 18/11/2014.
 * This is the activity corresponding to login screen
 */
public class LoginActivity extends Activity {
    private Button btnLogin;
    private EditText edtUsername;
    private EditText edtPassword;
    private Toast m_currentToast;

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
            //get the username from the input box
            String username = edtUsername.getText().toString();
            //get the password from the input box
            String password = edtPassword.getText().toString();

            if (username.isEmpty()){
                edtUsername.setError("Please fill this field");
            }
            else if (password.isEmpty()){
                edtPassword.setError("Please fill this field");
            }
            else {
                //Authenticate user, display any error messages if there is something wrong with the authentication stage
                //otherwise....
                //if its the first time logging in the application create the account using account manager
                //else if its not then check the credentials against the stored values for this account
                //and if authentication is successful move to the next activity

                Intent intent = new Intent(getApplicationContext(), TakePicActivity.class);
                startActivity(intent);
            }
        }
    };
/*    void showToast(String text,int toast_length)
    {
        if(m_currentToast != null)
        {
            m_currentToast.cancel();
        }
        m_currentToast = Toast.makeText(this, text,toast_length);
        m_currentToast.show();

    }*/
}
