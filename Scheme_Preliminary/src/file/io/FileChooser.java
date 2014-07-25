package file.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;

import com.example.scheme_preliminary.R;

public class FileChooser extends ListActivity {
    
    private File currentDir;
    private FileArrayAdapter adapter;
    
    @SuppressLint("NewApi") @Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String defaultPath = Environment.getExternalStorageDirectory().getPath() + "/";
		defaultPath += "com.example.scheme_preliminary/";
		String path = getIntent().getStringExtra("absolutePath");
		currentDir = new File(path);
		if (! currentDir.exists()) currentDir.mkdirs();
		fill(currentDir);
    }
    
    private void fill(File f) {
    	File[]dirs = f.listFiles();
    	this.setTitle("Current Dir: " + f.getName());
    	List<Option> dir = new ArrayList<Option>();
     	List<Option> fls = new ArrayList<Option>();
     	try {
         	for (File ff: dirs) {
         		String name = ff.getName();
         		if (! name.startsWith(".")) {
	         		if (ff.isDirectory())
	         			dir.add(new Option(name, "Folder" , ff.getAbsolutePath()));
	         		else if (name.endsWith(".scm") || name.endsWith(".rkt"))
	            		fls.add(new Option(ff.getName(), "File Size: " + ff.length(), ff.getAbsolutePath()));
         		}
         	}
     	}
     	catch(Exception e) {
     		System.out.println(e);
     	}
     	Collections.sort(dir);
     	Collections.sort(fls);
     	dir.addAll(0, fls);
//     	if (!f.getName().equalsIgnoreCase("sdcard"))
//     		dir.add(0, new Option("..","Parent Directory", f.getParent()));
     	adapter = new FileArrayAdapter(FileChooser.this, R.layout.file_view, dir);
     	this.setListAdapter(adapter);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	super.onListItemClick(l, v, position, id);
    	Option o = adapter.getItem(position);
    	if (o.getData().equalsIgnoreCase("folder") || 
			o.getData().equalsIgnoreCase("parent directory")) {
    		currentDir = new File(o.getPath());
    		fill(currentDir);
        }
        else
            onFileClick(o);
    }
    
    private void onFileClick(Option o) {
//        Toast.makeText(this, "File Clicked: "+o.getName(), Toast.LENGTH_SHORT).show();
    	Intent i = new Intent();
    	i.putExtra("path", o.getPath());
    	setResult(RESULT_OK, i);
    	this.finish();
    }
    
}
