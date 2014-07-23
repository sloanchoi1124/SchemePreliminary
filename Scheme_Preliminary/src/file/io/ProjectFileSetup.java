package file.io;

import android.os.Environment;

public class ProjectFileSetup {

	public static void main(String[] args) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			
		}
		else if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY))
			System.out.println("External storage currently read only");
		else
			System.out.println("External storage inaccessible");
		
	}

	public boolean externalStorageWritable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	public boolean externalStorageReadable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
	}
	
}
