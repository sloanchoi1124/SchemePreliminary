package com.example.scheme_preliminary.boxFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.example.scheme_preliminary.R;

import scheme_ast.DefOrExp;
import scheme_ast.Program;
import unparser.Unparser;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class Clipboard_Fragment extends Fragment {

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private ClipboardCommunicator myActivityCommunicator;
	private List<DefOrExp> buffer;
	private List<TextView> bufferView;
	private TextView tv0;
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private ViewFlipper vf;
	private GestureDetector detector=new GestureDetector(new SwipeGestureDetector());
	private int toPassIndex=-1;
	private Button sendButton;
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
		View v = inflater.inflate(R.layout.activity_clipboard__fragment, container, false);
		
		vf=(ViewFlipper) v.findViewById(R.id.viewFlipper1);
		vf.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				detector.onTouchEvent(event);
				return true;
			}
		});
		if(this.buffer!=null){
		sendButton=(Button) v.findViewById(R.id.clipboardbutton);
		sendButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(toPassIndex>=0)
				{
					if(toPassIndex>=buffer.size())
					{
						System.out.println("ignore");
					}
					else
					{
						myActivityCommunicator.chooseFromBuffer(buffer.get(toPassIndex));
					}
				}

			}
		});
		tv0=(TextView) v.findViewById(R.id.buffer0_text);
		tv1=(TextView) v.findViewById(R.id.buffer1_text);
		tv2=(TextView) v.findViewById(R.id.buffer2_text);
		tv3=(TextView) v.findViewById(R.id.buffer3_text);
		tv4=(TextView) v.findViewById(R.id.buffer4_text);
		bufferView=new ArrayList<TextView>();
		bufferView.add(tv0);
		bufferView.add(tv1);
		bufferView.add(tv2);
		bufferView.add(tv3);
		bufferView.add(tv4);
		for(final DefOrExp temp:this.buffer)
		{
			Stack<Integer> fake=new Stack<Integer>();
			bufferView.get(buffer.indexOf(temp)).setText(Unparser.unparse(temp, fake));
			bufferView.get(buffer.indexOf(temp)).setTextSize(20);
			bufferView.get(buffer.indexOf(temp)).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					toPassIndex=buffer.indexOf(temp);
					for(int i=0;i<buffer.size();i++)
					{
						if(i==toPassIndex)
							bufferView.get(i).setBackgroundColor(Color.YELLOW);
						else
							bufferView.get(i).setBackgroundColor(Color.WHITE);
					}
					
				}
			});
		}
		}
		return v;
	}
	
	class SwipeGestureDetector extends SimpleOnGestureListener{

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			try{
				if(e1.getX()-e2.getX()>SWIPE_MIN_DISTANCE&&Math.abs(velocityX)>SWIPE_THRESHOLD_VELOCITY)
				{
					System.out.println("swipe right");
					vf.showNext();
					return true;
				}
				else if(e2.getX()-e1.getX()>SWIPE_MIN_DISTANCE&&Math.abs(velocityX)>SWIPE_THRESHOLD_VELOCITY)
				{
					System.out.println("swipe left");
					vf.showPrevious();
					return true;
				}
			} catch(Exception e){};
			return false;
		}
		
	}
	
	public interface ClipboardCommunicator{
		public List<DefOrExp> passBufferToClipboard();
		public void chooseFromBuffer(DefOrExp deforexp);
	}

}
