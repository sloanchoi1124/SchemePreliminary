package com.example.scheme_preliminary;

import java.util.LinkedList;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.TextView;

public class Navigation_Activity extends Activity implements Navigation_Fragment.MyListFragmentCommunicator {

	final String ROOT_TEXT = "ROOT";
	private TextView titleView; 
	private LinkedList<Object> listSourceList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation_);
		Bundle b = getIntent().getExtras();
		String json;
//		if (b != null) {
//			json = b.getString("JSON_A3");
//		} else {
//			return;
//		}
//
//        this.listSourceList = new LinkedList<Object>();
//        
//        Object obj;
//        JSONParser parser = new JSONParser();
//        try {
//	        obj = parser.parse(json);
//        }
//        catch (ParseException e) {
//        	e.printStackTrace();
//        	obj = null;
//        }
//
//        if (obj != null) {
//        	newFragment(obj, savedInstanceState);
//        	
//            // Set the title (path)
//            this.titleView = (TextView) findViewById(R.id.listTitle);
//            this.titleView.setText(ROOT_TEXT);
//        }
        
//        System.out.println("Activity created");
	}

	@Override
	public void onBackPressed() {
		System.out.println(this.titleView.getText());
		super.onBackPressed();
//		if (((String) this.titleView.getText()).equals(ROOT_TEXT) == false) {
//			popFromPath();
//			this.listSourceList.removeLast();
//		}
//		else finish();
	}
	
	public void newFragment(Object obj, Bundle savedInstanceState) {
		if (findViewById(R.id.fragment_container) != null) {
			// If we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) return;
            else newFragment(obj, true);
		}
		else System.out.println("Can't find fragment_container");
	}
	
	public void newFragment(Object obj, boolean firstFragment) {
		this.listSourceList.add(obj);
		Navigation_Fragment fragment = new Navigation_Fragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		if (firstFragment)
			transaction.add(R.id.fragment_container, fragment,
					(String) ((TextView)findViewById(R.id.listTitle)).getText());
		else {
			transaction.replace(R.id.fragment_container, fragment,
					(String) ((TextView)findViewById(R.id.listTitle)).getText());
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}
	
	public Object getListSource() {
		return this.listSourceList.getLast();
	}
	
	// Called when item in object is touched
	public void onItemTouch(Object obj, String key) {
		addToPath(key);
		newFragment(obj, false);
	}
	// Called when item in list is touched
	public void onItemTouch(Object obj, int position) {
		addToPath(position);
		newFragment(obj, false);
	}
    
	private void addToPath(String key) {
		TextView title = ((TextView) findViewById(R.id.listTitle));
		title.setText(title.getText() + "/" + key);
	}
	private void addToPath(int index) {
		TextView title = ((TextView) findViewById(R.id.listTitle));
		title.setText(title.getText() + "[" + index + "]");
	}
	private void popFromPath() {
		TextView title = ((TextView) findViewById(R.id.listTitle));
		String s = (String) title.getText();
		int pos;
		if (s.endsWith("]")) pos = s.lastIndexOf("["); // was a list item
		else pos = s.lastIndexOf("/"); // was an object item
		if (pos == -1) {
			System.out.println("Why is this still being called?");
			return;
		}
		s = s.substring(0, pos);
		title.setText(s);
	}
	

}



//if (getListAdapter() != null) {
//ListView listView = (ListView) findViewById(android.R.id.list);
//  
//  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//  	@SuppressLint("NewApi") @Override
//  	public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
//
//			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//			String title = (String) ((TextView) findViewById(R.id.listTitle)).getText();
//			Object item = parent.getItemAtPosition(position);
//  		if (item instanceof JSONPair) {
//  			JSONPair pair = (JSONPair) item;
//  			intent.putExtra("json", pair.getValue().toString());
//  			intent.putExtra("title", title + " / " + pair.getKey());
//  		}
//  		else {
//  			intent.putExtra("json", item.toString());
//  			intent.putExtra("title", String.format(title + "[%d]", position));
//  		}
//  		startActivity(intent);
//  	}
//  });
//  
//}
