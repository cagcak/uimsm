package iaau.mas.uimsm.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import iaau.mas.uimsm.R;

/**
 * Created by Administrator on 13.02.2014.
 */
public class Fragment_About extends SherlockFragment
{
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.layout_fragment_about, container, false);
        
        clearActionBar();
        
        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        // Set title
        ((SherlockFragmentActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_about);
    }
    
    public void clearActionBar()
    {
    	getSherlockActivity().getSupportActionBar().getNavigationMode();
        getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

}