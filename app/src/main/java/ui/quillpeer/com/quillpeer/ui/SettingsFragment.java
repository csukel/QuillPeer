package ui.quillpeer.com.quillpeer.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.preference.PreferenceFragment;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import core.MyApplication;
import core.Server.ServerComm;
import ui.quillpeer.com.quillpeer.R;

/**
 * This class defines the behaviour of the settings' screen.
 * Created by loucas on 18/11/2014.
 * Library used for PreferenceFragment is a third party library downloaded from
 * https://github.com/kolavar/android-support-v4-preferencefragment
 * @author Loucas Stylianou
 */
public class SettingsFragment extends PreferenceFragment {
    /** a toast object to display small info msgs to the user */
    private Toast m_currentToast;
    /** shared preferences */
    private SharedPreferences sharedPrefs;

    /**
     * When this fragment is created, initialise the UI of the screen and define the behaviour of the UI elements.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the settings from an XML resource
        addPreferencesFromResource(R.xml.settings);

        sharedPrefs = MyApplication.getPrefs();

        Preference prefLogout = (Preference) findPreference("pref_log_out");
        prefLogout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            /**
             * When the logout button is clicked send the request to the server and exit the app
             * @param preference
             * @return
             */
            public boolean onPreferenceClick(Preference preference) {
                //open browser or intent here
                //Toast.makeText(getActivity().getApplicationContext(), "Log out is pressed", Toast.LENGTH_SHORT).show();
                //check the network state and proceed if there is internet connection
                if (ServerComm.isNetworkConnected(getActivity().getApplicationContext(), getActivity())) {
                    logout();
                } else
                    Toast.makeText(getActivity().getApplicationContext(), "Check your internet connection...", Toast.LENGTH_LONG).show();

                return false;
            }
        });

        Preference prefTutorial = (Preference)findPreference("pref_tutorial");
        prefTutorial.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            /**
             * When the tutorial option is clicked then launch the TutorialActivity which contains the tutorial slided show
             * @param preference
             * @return
             */
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(),TutorialActivity.class);
                startActivity(intent);
                return false;
            }
        });

    }


    /**
     * This method is used to send a log out request to the server.
     */
    private void logout(){
        class SendGetReqAsyncTask extends AsyncTask<Void, Void, String> {
            ProgressDialog dialog = ProgressDialog.show(getActivity(),
                    "Logging out...", "Please wait...", false);

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                dialog.show();
            }

            @Override
            protected String doInBackground(Void... params) {

                return ServerComm.logout();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                dialog.dismiss();
                JSONObject jsonObject=null;
                boolean outcome = false;
                try {
                    if (result!=null) {
                        jsonObject = new JSONObject(result);
                        outcome = jsonObject.getBoolean("successful");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //if logout is successful close the main activity
                if (outcome){
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.remove("credStored");
                    editor.remove("username");
                    editor.remove("password");
                    editor.commit();
                    getActivity().finish();
                }
            }
        }
        //check the network state and proceed if there is internet connection
        if (ServerComm.isNetworkConnected(getActivity().getApplicationContext(), getActivity())){
            SendGetReqAsyncTask sendGetReqAsyncTask = new SendGetReqAsyncTask();
            sendGetReqAsyncTask.execute();
        }else showToast("Check your internet connection...",Toast.LENGTH_SHORT);


    }
    /**
     * When fragment is attached to the activity invokes the onSectionAttached of the corresponding activity
     * in order to set the title on the ActionBar
     * @param: Activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //((MainActivity) activity).onSectionAttached(getArguments().getInt("Position"));
    }

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
        m_currentToast = Toast.makeText(getActivity().getApplicationContext(), text,toast_length);
        m_currentToast.show();

    }
}
