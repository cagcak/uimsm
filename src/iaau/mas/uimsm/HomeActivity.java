package iaau.mas.uimsm;

import iaau.mas.uimsm.factory.MenuListAdapter;
import iaau.mas.uimsm.fragment.home.Fragment_About;
import iaau.mas.uimsm.fragment.home.Fragment_AppsForms;
import iaau.mas.uimsm.fragment.home.Fragment_Diploma;
import iaau.mas.uimsm.fragment.home.Fragment_MyInformation;
import iaau.mas.uimsm.fragment.home.Fragment_Registration;
import iaau.mas.uimsm.fragment.home.Fragment_Success;
import iaau.mas.uimsm.fragment.home.Fragment_Transcript;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

/**
 * Created by Administrator on 13.02.2014.
 */
public class HomeActivity extends SherlockFragmentActivity
{
	public static String userID;
	
    // Variables
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    MenuListAdapter mMenuAdapter;
    String[] title;
    String[] subtitle;
    int[] icon;

    Fragment fragment_myinformation = new Fragment_MyInformation();
    Fragment fragment_transcript = new Fragment_Transcript();
    Fragment fragment_success = new Fragment_Success();
    Fragment fragment_appsforms = new Fragment_AppsForms();
    Fragment fragment_diploma = new Fragment_Diploma();
    Fragment fragment_registration = new Fragment_Registration();
    Fragment fragment_help = new Fragment_About();

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        SharedPreferences sharedPreferences1 = getSharedPreferences("UserCredentials", 0);
        String idNumber = sharedPreferences1.getString("idnumber_key","").toString();
        
        userID = idNumber;

        // Get the Title
        mTitle = mDrawerTitle = getTitle();

        // Generate title
        title = new String[]
                {
                        "MyInfo",
                        "Transcript",
                        "Success Report",
                        "Applications & Forms",
                        "Information for Diploma",
                        "Registration",
                        "About",
                        "Return"
                };

        // Generate subtitle
        subtitle = new String[]
                {
                        "See your profile",
                        "Check your all grades",
                        "Current semester lectures",
                        "Request documents",
                        "Specify your diploma",
                        "Validate your lectures",
                        "",
                        "Back to main screen"
                };

        // Generate icon
        icon = new int[]
                {
                        R.drawable.ic_action_user_inverted,
                        R.drawable.ic_action_transcript_inverted,
                        R.drawable.ic_action_success_report_inverted,
                        R.drawable.ic_action_appsforms_inverted,
                        R.drawable.ic_action_diploma,
                        R.drawable.ic_action_registration,
                        R.drawable.action_about,
                        R.drawable.ic_action_logout_inverted

                };

        // Locate DrawerLayout in activity_home.xml
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Locate ListView in activity_home.xml
        mDrawerList = (ListView) findViewById(R.id.listview_drawer);

        // Set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.home_activity_drawer_shadow,GravityCompat.START);

        // Pass string arrays to MenuListAdapter
        mMenuAdapter = new MenuListAdapter(HomeActivity.this, title, subtitle,icon);

        // Set the MenuListAdapter to the ListView
        mDrawerList.setAdapter(mMenuAdapter);

        // Capture listview menu item click
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle
               (this,                              /* host Activity */
                mDrawerLayout,                     /* DrawerLayout object */
                R.drawable.ic_drawer,              /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,          /* "open drawer" description for accessibility */
                R.string.drawer_close)         /* "close drawer" description for accessibility */
        {

            @Override
            public void onDrawerClosed(View view)
            {
                // TODO Auto-generated method stub
                super.onDrawerClosed(view);
            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                // TODO Auto-generated method stub
                // Set the title on the action when drawer open
                getSupportActionBar().setTitle(mDrawerTitle);
                super.onDrawerOpened(drawerView);
            }
        };

        // Defer code dependent on restoration of previous instance state.
        // NB: required for the drawer indicator to show up!
        mDrawerLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null)
        {
            selectItem(0);
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if (mDrawerLayout.isDrawerOpen(mDrawerList))
            {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    // ListView click listener in the navigation drawer
    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id)
        {
            selectItem(position);
        }
    }

    private void selectItem(int position)
    {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Locate Position
        switch (position)
        {
            case 0:
                ft.replace(R.id.content_frame, fragment_myinformation);
                break;
            case 1:
                ft.replace(R.id.content_frame, fragment_transcript);
                break;
            case 2:
                ft.replace(R.id.content_frame, fragment_success);
                break;
            case 3:
                ft.replace(R.id.content_frame, fragment_appsforms);
                break;
            case 4:
                ft.replace(R.id.content_frame, fragment_diploma);
                break;
            case 5:
                ft.replace(R.id.content_frame, fragment_registration);
                break;
            case 6:
                ft.replace(R.id.content_frame, fragment_help);
                break;
            case 7:
            	finish();
        }
        ft.commit();
        mDrawerList.setItemChecked(position, true);

        // Get the title followed by the position
        setTitle(title[position]);
        // Close drawer
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
    

    @Override
    public void onBackPressed()
    {
        FragmentManager manager = getSupportFragmentManager();

        if (manager.getBackStackEntryCount() > 0)
        {
            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.
            manager.popBackStack();
        } else {
            // Otherwise, ask user if he wants to leave
            super.onBackPressed();
        }
    }
    


}
