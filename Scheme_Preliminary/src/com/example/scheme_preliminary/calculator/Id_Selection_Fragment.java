package com.example.scheme_preliminary.calculator;

import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.scheme_preliminary.R;

public class Id_Selection_Fragment extends ListFragment {
	
	Calculator_Fragment_Listener mCallback;
	List<String> listSource;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_calculator_id_selection, container, false);
		
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>((Activity) this.mCallback,
																	  android.R.layout.simple_list_item_1,
																	  listSource);
		setListAdapter(adapter);
		return v;
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
            this.mCallback = (Calculator_Fragment_Listener) activity;
            this.listSource = this.mCallback.getListSource();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement IdSelector");
        }
	}
	
	@Override
	public void onStart() {
		super.onStart();
//		getView().setBackgroundColor(Color.LTGRAY);
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		String varId = (String) listView.getItemAtPosition(position);
		this.mCallback.onIdSelected(varId);
	}

}
