package iaau.mas.uimsm.fragment.home;

import iaau.mas.uimsm.R;
import iaau.mas.uimsm.factory.MyInfo_ViewPagerAdapter;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;


/**
 * Created by Administrator on 13.02.2014.
 */
public class Fragment_MyInformation extends SherlockFragment
{
	
	public void onAttach(FragmentActivity activity)
	{
		super.onAttach(activity);
		setHasOptionsMenu(false);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.layout_fragment_myinformation_viewpager, container, false);

        clearActionBar();
        
        // Locate the ViewPager in viewpager
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        // Set the ViewPagerAdapter into ViewPager
        mViewPager.setAdapter(new MyInfo_ViewPagerAdapter(getChildFragmentManager()));

        return view;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        try {
            Field childFragmentManager =
                    Fragment.class.getDeclaredField("mChildFragmentManager");

            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearActionBar()
    {
    	getSherlockActivity().getSupportActionBar().getNavigationMode();
        getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }


}