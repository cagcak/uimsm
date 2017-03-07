/*package iaau.mas.uimsm.factory;

import iaau.mas.uimsm.fragment.home.registration.Fragment_NotTaken;
import iaau.mas.uimsm.fragment.home.registration.Fragment_RegistrationList;
import iaau.mas.uimsm.fragment.home.registration.Fragment_TakenSubjects;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Registration_ViewPagerAdapter extends FragmentPagerAdapter
{
	public Registration_ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	// Declare the number of ViewPager pages
    final int PAGE_COUNT = 3;
    private String titles[] = new String[] { "Not Taken Subjects", "Confirm Registration", "Taken Current Subjects" };
    
    @Override
    public Fragment getItem(int position)
    {
    	switch (position)
        {
    	case 0:
    		Fragment_NotTaken tab1_notTakenSubjects = new Fragment_NotTaken();
    		return tab1_notTakenSubjects;
    		
    	case 1:
    		Fragment_RegistrationList tab2_registration = new Fragment_RegistrationList();
    		return tab2_registration;
    		
    	case 2:
    		Fragment_TakenSubjects tab3_takenSubjects = new Fragment_TakenSubjects();
    		return tab3_takenSubjects;
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
*/