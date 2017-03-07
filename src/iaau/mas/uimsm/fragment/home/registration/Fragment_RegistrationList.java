/*package iaau.mas.uimsm.fragment.home.registration;

import java.util.ArrayList;

import iaau.mas.uimsm.HomeActivity;
import iaau.mas.uimsm.R;
import iaau.mas.uimsm.factory.FragmentLifecycle;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.squareup.otto.Bus;

public class Fragment_RegistrationList extends SherlockListFragment implements FragmentLifecycle
{	
	EditText yearFilter;
	EditText subjectCodeFilter;
	ListView unRegisteredTakenSubjectList;
	Button saveSubject;
	
	private static Bundle bundle;
	
	private static ArrayAdapter<String> bundleTakenSubjectList;
	private static String[] list_subject_name;
	
//	@Inject
	Bus bus;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.layout_fragment_registration_registration, container, false);
		
		yearFilter = (EditText) view.findViewById(R.id.filter_by_year_register);
        subjectCodeFilter = (EditText) view.findViewById(R.id.filter_by_code_edit_register);
        unRegisteredTakenSubjectList = (ListView) view.findViewById(android.R.id.list);
        saveSubject = (Button) view.findViewById(R.id.save_registration_button);
        
        getRegistrationList();
        
//        Bundle bundle = this.getArguments();
//        list_subject_name = bundle.getStringArray("selectedItems");
//        bundle.getStringArray("selectedItems");
//        bundle.getStringArray("selectedItems");
        
//        extras.getBundle("selectedItems");
//        Log.d("selectedItems", String.valueOf(extras.getBundle("selectedItems")));
//        list_subject_name.add(extras);
        
        updateRegistrationList();
		
		saveSubject.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				
			}
		});
        
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);;

		getRegistrationList();	
	}
	
	@Override
    public void onResume()
    {
        super.onResume();
        // Set title
        ((SherlockFragmentActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_registration);
        
        getRegistrationList();
    }
	
	private void getRegistrationList()
	{
		bundle = getArguments();
		Log.d("received bundle", String.valueOf(bundle));
        if(getArguments()!=null)
		{
			list_subject_name = bundle.getStringArray("selectedItems");
		} else {
			list_subject_name = new String[] {"liste boş"};
		}
	}
	
	private void updateRegistrationList()
	{
//		list_subject_name = new String[list_subject_name.length];
		bundleTakenSubjectList = new ArrayAdapter<String>(getSherlockActivity(), android.R.layout.simple_list_item_1, list_subject_name);
		unRegisteredTakenSubjectList.setAdapter(bundleTakenSubjectList);
		bundleTakenSubjectList.notifyDataSetChanged();
	}

	@Override
	public void onPauseFragment() {
		Log.i(getTag(), "PAGE 2 PAUSED");
	}

	@Override
	public void onResumeFragment() {
		Log.i(getTag(), "PAGE 2 RESUMED");
		getRegistrationList();
//		updateRegistrationList();
		bus = new Bus();
		bus.register(this);
	}

	@Override
	public void onButtonPressed(String[] listSubject) {
		// TODO Auto-generated method stub
		
	}
	
	

}
*/