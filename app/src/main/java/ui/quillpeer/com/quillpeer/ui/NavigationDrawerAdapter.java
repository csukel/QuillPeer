package ui.quillpeer.com.quillpeer.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ui.quillpeer.com.quillpeer.R;

/**
 * Created by loucas on 17/11/2014.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    public NavigationDrawerAdapter(Context context, String[] objects) {
        super(context, R.layout.navigation_drawer_list_item, objects);
        this.context = context;
        this.values = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.navigation_drawer_list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.txtItemTitle);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imgItemIcon);
        textView.setText(values[position]);
        // Change the icon depending on the item string
        String s = values[position];
        if (s.equals("Profile")) {
            imageView.setImageResource(R.drawable.ic_action_person);
        } else if (s.equals("People")) {
            imageView.setImageResource(R.drawable.ic_action_group);
        } /*else if (s.equals("Timetable")) {
            imageView.setImageResource(R.drawable.ic_action_time);
        }*/ else if (s.equals("About")){
            imageView.setImageResource(R.drawable.ic_action_about);
        } else if (s.equals("Settings")){
            imageView.setImageResource(R.drawable.ic_action_settings);
        }


        return rowView;
    }
}
