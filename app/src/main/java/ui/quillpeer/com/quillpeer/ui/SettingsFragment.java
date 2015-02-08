package ui.quillpeer.com.quillpeer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.preference.PreferenceFragment;

import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 18/11/2014.
 * Library used for PreferenceFragment is a third party library downloaded from
 * https://github.com/kolavar/android-support-v4-preferencefragment
 */
public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the settings from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }
    /**
     * When fragment is attached to the activity invokes the onSectionAttached of the corresponding activity
     * in order to set the title on the ActionBar
     * @param: Activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(getArguments().getInt("Position"));
    }
}
