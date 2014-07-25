package file.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.IBinder;

public class ProjectFileSetup extends Service {

	String rootPath = Environment.getExternalStorageDirectory().getPath();
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		System.out.println("ProjectFileSetup started");
		if (externalStorageWritable()) {
//			System.out.println("external storage writable");
			File examplesFolder = new File(rootPath + "/com.example.scheme_preliminary/");
			try {
				if (! examplesFolder.exists()) {
					examplesFolder.mkdirs();
//					System.out.println("examples folder made");
				}
				AssetManager am = getAssets();
				String[] filenames = am.list("");
				for (int i=0; i<filenames.length; i++) {
					String filename = filenames[i];
					File file;
					if ((filename.endsWith(".rkt") || filename.endsWith(".scm")) &&
						! ((file = new File(examplesFolder.getPath() + File.separator + filename)).exists())) {

						PrintWriter out = new PrintWriter(file);

					    BufferedReader in = new BufferedReader(new InputStreamReader(am.open(filename), "UTF-8"));
						
						StringBuilder buf = new StringBuilder();
					    String str;
					    while ((str = in.readLine()) != null)
					    	buf.append(str).append("\n");

					    in.close();
					    out.write(buf.toString());
					    out.close();
					}
				}
			}
			catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
			}
		}
		else if (externalStorageReadable())
			System.out.println("External storage currently read only");
		else
			System.out.println("External storage inaccessible");
		
		stopSelf();
		return 0;
	}

	public static boolean externalStorageWritable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	public static boolean externalStorageReadable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
}
