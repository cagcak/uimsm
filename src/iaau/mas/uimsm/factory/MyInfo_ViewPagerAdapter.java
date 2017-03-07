package iaau.mas.uimsm.factory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import iaau.mas.uimsm.fragment.home.myinfo.Fragment_MyInformation_Account;
import iaau.mas.uimsm.fragment.home.myinfo.Fragment_MyInformation_Current;
import iaau.mas.uimsm.fragment.home.myinfo.Fragment_MyInformation_General;

/**
 * Created by Administrator on 17.02.2014.
 */
public class MyInfo_ViewPagerAdapter extends FragmentPagerAdapter
{
    // Declare the number of ViewPager pages
    final int PAGE_COUNT = 3;
    private String titles[] = new String[] { "Current Status", "General Information", "Accounting Status" };

    public MyInfo_ViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch (position)
        {

            // Open Fragment_MyInformation_Current.java
            case 0:
                Fragment_MyInformation_Current tab1_current = new Fragment_MyInformation_Current();
                return tab1_current;

            // Open Fragment_MyInformation_General.java
            case 1:
                Fragment_MyInformation_General tab2_general = new Fragment_MyInformation_General();
                return tab2_general;

            // Open Fragment_MyInformation_Account.java
            case 2:
                Fragment_MyInformation_Account tab3_account = new Fragment_MyInformation_Account();
                return tab3_account;
        }
        return null;
    }

    public CharSequence getPageTitle(int position)
    {
        return titles[position];
    }

    @Override
    public int getCount()
    {
        return PAGE_COUNT;
    }
}
