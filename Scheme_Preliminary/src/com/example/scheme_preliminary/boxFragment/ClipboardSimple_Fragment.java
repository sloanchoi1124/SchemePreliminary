package com.example.scheme_preliminary.boxFragment;

import java.util.ArrayList;
import java.util.List;

import scheme_ast.DefOrExp;
import scheme_ast.Expression;
import unparser.ShallowUnparser;

import com.example.scheme_preliminary.R;
import com.example.scheme_preliminary.boxFragment.Clipboard_Fragment.ClipboardCommunicator;
import com.example.scheme_preliminary.boxFragment.Popup_Viewing.PrivateClipboardSimpleCommunicator;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class ClipboardSimple_Fragment extends Fragment{

	private ClipboardCommunicator myActivityCommunicator;
	private List<DefOrExp> buffer;
	private ListView lv;
	private ClipboardSimple_Fragment context;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		myActivityCommunicator=(ClipboardCommunicator) activity;
		buffer=myActivityCommunicator.passBufferToClipboard();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_clipboard_simple__fragment, container, false);
		lv=(ListView) v.findViewById(R.id.clipboard_listview);
		List<String> bufferString=new ArrayList<String>();
		for(DefOrExp temp:buffer)
			bufferString.add(ShallowUnparser.shallowUnparse(temp, 1));
		lv.setAdapter(new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, bufferString));
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				//then pop-up a window to view what is at the position
				
				myActivityCommunicator.chooseFromBuffer(buffer.get(position));
				if(getFragmentManager().findFragmentByTag("popup_view")==null)
		    	{
		    		getFragmentManager().beginTransaction()
		    		.add(R.id.popup_frame, new Popup_Viewing(), "popup_view")
		    		.addToBackStack(null)
		    		.commit();
		    		getView().findViewById(R.id.popup_frame).bringToFront();
		    	}
			}
		});
		return v;
	}
}
