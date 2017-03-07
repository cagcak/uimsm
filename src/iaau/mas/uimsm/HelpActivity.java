package iaau.mas.uimsm;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.view.Window;

import iaau.mas.uimsm.factory.HelpTabListener;
import iaau.mas.uimsm.factory.TabChangedListener;
import iaau.mas.uimsm.fragment.help.DepartmentsFragment;
import iaau.mas.uimsm.fragment.help.NewsFragment;
import iaau.mas.uimsm.fragment.help.NotificationFragment;
import iaau.mas.uimsm.fragment.help.StatusesFragment;

/**
 * Created by Administrator on 12.02.2014.
 *
 * This is the main fragment activity with action bar tabs and view pager.
 */
public class HelpActivity extends SherlockFragmentActivity implements TabChangedListener
{
    private ActionBar _actionBar;
    private ViewPager _viewPager;

    @SuppressWarnings("unused")
	private static boolean _firstTabShowed;
    @SuppressWarnings("unused")
    private static boolean _secondTabShowed;
    @SuppressWarnings("unused")
    private static boolean _thirdTabShowed;
    @SuppressWarnings("unused")
    private static boolean _fourthTabShowed;
    
    private static Menu _menuInstance;


    protected static HelpActivity THIS = null;

    /**
     * Adds three tabs to the Action Bar (Ongoing, Previous and Best Rated tab).
     * In addTabs() method we create a MyTabListener instance and
     * add a tab changed listener to it. After this, four tabs are added.
     */
    private void addTabs()
    {
        HelpTabListener myTabListener = new HelpTabListener(this, _viewPager);
        myTabListener.addTabChangedListener(this);

        ActionBar.Tab firstTab = _actionBar.newTab();
        firstTab.setText("Notification");
        myTabListener.addTab(firstTab, NotificationFragment.class, null);
        firstTab.setTabListener(myTabListener);

        ActionBar.Tab secondTab = _actionBar.newTab();
        secondTab.setText("News");
        myTabListener.addTab(secondTab, NewsFragment.class, null);
        secondTab.setTabListener(myTabListener);

        ActionBar.Tab thirdTab = _actionBar.newTab();
        thirdTab.setText("Statuses");
        myTabListener.addTab(thirdTab, StatusesFragment.class, null);
        thirdTab.setTabListener(myTabListener);

        ActionBar.Tab fourthTab = _actionBar.newTab();
        fourthTab.setText("Departments");
        myTabListener.addTab(fourthTab, DepartmentsFragment.class, null);
        fourthTab.setTabListener(myTabListener);
    }

    /**
     * Hides all action bar menu items.
     * hideAllActionItems(Menu menu) method is needed for
     * resetting the visibility of our Action Bar menu items.
     * They might differ depending on tab selected and
     * it’s visibility should be managed somehow.
     *
     * @param menu Action bar menu instance
     */
    private void hideAllActionItems(Menu menu)
    {
        if (menu != null)
        {
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        }
    }

    /**
     * Initializes the action bar and sets it's navigation mode.
     *
     * In initActionBar() we just initialize the ActionBar and
     * set it’s navigation mode to TABS.
     */
    private void initActionBar()
    {
        _actionBar = getSupportActionBar();
        _actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }

    /**
     * Initializes the view pager and sets it as a content view.
     *
     * In initViewPager() we initialize our ViewPager,
     * set the id to it and set the content view to ViewPager.
     */
    private void initViewPager()
    {
        _viewPager = new ViewPager(this);
        _viewPager.setId(R.id.pager);
        setContentView(_viewPager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Overlaying fragment contents
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);

        THIS = this;
        initActionBar();
        initViewPager();
        addTabs();
    }

//  In onCreateOptionsMenu(Menu menu) the _menuInstance is initialized and
//  two action items are added. The Refresh item visibility is
//  always true but the Settings item should be visible only if
//  _thirdTabShowed is true. By default, it’s not.
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        _menuInstance = menu;

        menu
                .add(getString(R.string.action_home))
                .setIcon(R.drawable.ic_menu_home_inversed)
                .setVisible(true)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);


        menu
                .add(getString(R.string.action_refresh))
                .setIcon(R.drawable.action_refresh_inversed)
                .setVisible(false)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu
                .add(getString(R.string.action_settings))
                .setIcon(R.drawable.action_settings_inversed)
                .setVisible(false)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        /*menu
                .add(getString(R.string.action_lang))
                .setIcon(R.drawable.language_icon)
                .setVisible(true)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);*/

        SubMenu language_submenu = menu.addSubMenu("").setIcon(R.drawable.language_icon);
        language_submenu.add(R.string.action_lang_en).setIcon(R.drawable.flag_uk);
        language_submenu.add(R.string.action_lang_ru).setIcon(R.drawable.flag_ru);
        language_submenu.add(R.string.action_lang_kg).setIcon(R.drawable.flag_kg);
        language_submenu.add(R.string.action_lang_tr).setIcon(R.drawable.flag_tr);
        MenuItem language_submenuItem = language_submenu.getItem();
        //language_submenuItem.setIcon(R.drawable.language_icon);
        language_submenuItem.setVisible(true).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);



        return true;
    }

//  In onTabChanged(int pageIndex, ActionBar.Tab tab, View tabView) method,
//  the visibility of ActionBar items is reset along with their boolean indicators.
//  Depending on page index we’re currently on, this page’s menu items are showed.
    @Override
    public void onTabChanged(int pageIndex, ActionBar.Tab tab, View tabView)
    {
        resetVisibilityFields();

        if (_menuInstance != null)
        {
            hideAllActionItems(_menuInstance);

            switch (pageIndex)
            {
                case 0:
                    showFirstTabActionItems(_menuInstance);
                    break;

                case 1:
                    showSecondTabActionItems(_menuInstance);
                    break;

                case 2:
                    showThirdTabActionItems(_menuInstance);
                    break;

                case 3:
                    showFourthTabActionItems(_menuInstance);
                    break;
            }
        }
    }

    /**
     * Sets all tabs action item visibility fields to false.
     */
    private void resetVisibilityFields()
    {
        _firstTabShowed = false;
        _secondTabShowed = false;
        _thirdTabShowed = false;
        _fourthTabShowed = false;
    }

    /**
     * Shows First tab action items.
     *
     * @param menu Action bar menu instance
     */
    private void showFirstTabActionItems(Menu menu)
    {
        if (menu != null)
        {
            menu.getItem(0).setVisible(true); // Home action icon visible
            menu.getItem(1).setVisible(false); // Refresh action icon visible
            menu.getItem(2).setVisible(false); // Settings action icon visible
            menu.getItem(3).setVisible(true); // Language action icon visible

            _firstTabShowed = true;
        }
    }

    /**
     * Shows Second tab action items.
     *
     * @param menu Action bar menu instance
     */
    private void showSecondTabActionItems(Menu menu)
    {
        if (menu != null)
        {
            menu.getItem(0).setVisible(true); // Home action icon visible
            menu.getItem(1).setVisible(false); // Refresh action icon visible
            menu.getItem(2).setVisible(false); // Settings action icon visible
            menu.getItem(3).setVisible(true); // Language action icon visible

            _secondTabShowed = true;
        }
    }

    /**
     * Shows Third tab action items.
     *
     * @param menu Action bar menu instance
     */
    private void showThirdTabActionItems(Menu menu)
    {
        if (menu != null)
        {
            menu.getItem(0).setVisible(true); // Home action icon visible
            menu.getItem(1).setVisible(true); // Refresh action icon visible
            menu.getItem(2).setVisible(false); // Settings action icon visible
            menu.getItem(3).setVisible(false); // Language action icon visible

            _thirdTabShowed = true;
        }
    }

    /**
     * Shows Fourth tab action items.
     *
     * @param menu Action bar menu instance
     */
    private void showFourthTabActionItems(Menu menu)
    {
        if (menu != null)
        {
            menu.getItem(0).setVisible(true); // Home action icon visible
            menu.getItem(1).setVisible(true); // Refresh action icon visible
            menu.getItem(2).setVisible(false); // Settings action icon visible
            menu.getItem(3).setVisible(false); // Language action icon visible

            _fourthTabShowed = true;
        }
    }
}