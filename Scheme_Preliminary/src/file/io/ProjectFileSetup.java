package file.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

public class ProjectFileSetup extends Service {

	String rootPath = Environment.getExternalStorageDirectory().getPath();
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (externalStorageWritable()) {
			try {
				File examplesFolder = new File(rootPath + "/com.example.scheme_preliminary/.examples");
				if (! examplesFolder.exists()) {
					examplesFolder.mkdirs();
					File example0File = new File(rootPath + "/com.example.scheme_preliminary/.examples/example0.rkt");
					FileOutputStream fos = new FileOutputStream(example0File);
					InputStream is = getAssets().open("example0.rtk");
					int b;
					while ((b = is.read()) != -1) {
						fos.write(b);
					}
					is.close();
					fos.close();
				}
				else {
				}
				InputStream is = getAssets().open("example0.rkt");
			} catch (IOException e) {
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
