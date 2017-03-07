package iaau.mas.uimsm.factory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import iaau.mas.uimsm.R;

/**
 * Created by Administrator on 13.02.2014.
 */
public class MenuListAdapter extends BaseAdapter
{
    // Declare Variables
    Context context;
    String[] mTitle;
    String[] mSubTitle;
    int[] mIcon;
    LayoutInflater inflater;

    public MenuListAdapter(Context context, String[] title, String[] subtitle,
                           int[] icon) {
        this.context = context;
        this.mTitle = title;
        this.mSubTitle = subtitle;
        this.mIcon = icon;
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Override
    public Object getItem(int position) {
        return mTitle[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView txtTitle;
        TextView txtSubTitle;
        ImageView imgIcon;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.home_activity_drawer_list, parent,false);

        // Locate the TextViews in drawer_list_item.xml
        txtTitle = (TextView) itemView.findViewById(R.id.title_drawer);
        txtSubTitle = (TextView) itemView.findViewById(R.id.subtitle_drawer);

        // Locate the ImageView in drawer_list_item.xml
        imgIcon = (ImageView) itemView.findViewById(R.id.icon_drawer);

        // Set the results into TextViews
        txtTitle.setText(mTitle[position]);
        txtSubTitle.setText(mSubTitle[position]);

        // Set the results into ImageView
        imgIcon.setImageResource(mIcon[position]);

        return itemView;
    }
}
