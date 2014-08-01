package com.example.scheme_preliminary.boxFragment;

import java.util.Stack;

import com.example.scheme_preliminary.R;
import scheme_ast.DefOrExp;
import unparser.Unparser;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Popup_Viewing extends Fragment {

	private DefOrExp deforexp;
	private TextView tv;
	private Button send;
	private PrivateClipboardSimpleCommunicator myActivityCommunicator;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.myActivityCommunicator=(PrivateClipboardSimpleCommunicator) activity;
		this.deforexp=this.myActivityCommunicator.getDefOrExpFromSimpleClip();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.activity_popup__viewing, container, false);
		this.tv=(TextView) v.findViewById(R.id.popupview_text);
		this.send=(Button) v.findViewById(R.id.popupview_send);
		tv.setText(Unparser.unparse(deforexp, new Stack<Integer>()));
		send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myActivityCommunicator.passClipboardItemToCalculator(deforexp);
			}
		});
		return v;
	}
	
	public interface PrivateClipboardSimpleCommunicator{
		public DefOrExp getDefOrExpFromSimpleClip();
		public void passClipboardItemToCalculator(DefOrExp deforexp);
	}


}
