package iaau.mas.uimsm.factory;

import android.view.View;

import com.actionbarsherlock.app.ActionBar;

/**
 * Created by Administrator on 12.02.2014.
 */
public interface TabChangedListener
{
    /**
     * This method is called when the user has changed page of the tab view.
     * @param pageIndex Index of the current page.
     * @param tab Instance of the selected tab control.
     * @param tabView Instance of the tab view.
     */
    void onTabChanged(int pageIndex, ActionBar.Tab tab, View tabView);
}
