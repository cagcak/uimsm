package iaau.mas.uimsm.factory;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import java.util.ArrayList;
/**
 * This is the custom pager tab listener. Tabs are implemented in ActionBar.
 * Every tab's content is a Sherlock fragment.
 *
 * Created by Administrator on 12.02.2014.
 */
public class HelpTabListener extends FragmentStatePagerAdapter implements ActionBar.TabListener, ViewPager.OnPageChangeListener
{
    private final Context _context;
    private final ActionBar _actionBar;
    private final ViewPager _viewPager;
    private final ArrayList<TabInfo> _tabs = new ArrayList<TabInfo>();
    private final ArrayList<TabChangedListener> _tabChangedListeners = new ArrayList<TabChangedListener>();

    public HelpTabListener(SherlockFragmentActivity activity, ViewPager pager)
    {
        super(activity.getSupportFragmentManager());
        _context = activity;
        _actionBar = activity.getSupportActionBar();
        _viewPager = pager;
        _viewPager.setAdapter(this);
        _viewPager.setOnPageChangeListener(this);
    }

    //  used as a tag for every new tab added
    static final class TabInfo
    {
        private final Class<?> _class;
        private final Bundle _args;

        TabInfo(Class<?> clss, Bundle args)
        {
            _class = clss;
            _args = args;
        }
    }


    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

//  In onPageSelected() the Action Bar should be notified
//  about the tab position, so we’re making a
//  setSelectedNavigationItem(position) call.
//  In onTabSelected() we iterate though all tabs and
//  check their tags to see which one should be selected in ViewPager.
    @Override
    public void onPageSelected(int position)
    {
        _actionBar.setSelectedNavigationItem(position);
    }

//  We also must notify our TabChangedListener about the change at onPageSelected()
    private void notifyTabChangedListeners(int tabIndex, ActionBar.Tab tab, View tabView)
    {
        for (TabChangedListener listener : _tabChangedListeners)
        {
            listener.onTabChanged(tabIndex, tab, tabView);
        }
    }

    /**
     * Adds tabs to the ActionBar.
     *
     * @param tab  Tab which will added
     * @param clss Class which is connected with the tab
     * @param args Extra tab arguments
     */
    public void addTab(Tab tab, Class<?> clss, Bundle args)
    {
        TabInfo info = new TabInfo(clss, args);
        tab.setTag(info);
        tab.setTabListener(this);
        _tabs.add(info);
        _actionBar.addTab(tab);
        notifyDataSetChanged();
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public void addTabChangedListener(TabChangedListener listener)
    {
        _tabChangedListeners.add(listener);
    }

//  getCount() method will return the size of the tabs
    @Override
    public int getCount()
    {
        return _tabs.size();
    }

//  getItem(int position) will return a new Fragment instantiated
//  Because the type returned is a Fragment, there can be a simple fragment
//  in any of Action Bar tabs not only a ListFragment.
    @Override
    public Fragment getItem(int position)
    {
        TabInfo info = _tabs.get(position);
        return Fragment.instantiate(_context, info._class.getName(), info._args);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
    {
        Object tag = tab.getTag();

        for (int i = 0; i < _tabs.size(); i++)
        {
            if (_tabs.get(i) == tag)
            {
                _viewPager.setCurrentItem(i);
                notifyTabChangedListeners(i, tab, tab.getCustomView());
            }
        }
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {

    }
}
