package file.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import parser.Lexer;
import parser.Parser;
import scheme_ast.Program;
import android.content.res.AssetManager;
import android.os.Environment;

public class FileUtils {

	static String rootPath = Environment.getExternalStorageDirectory().getPath();
	static String appPath = rootPath + "/com.example.scheme_preliminary";
	public static final String NEW_FILE_NAME = ".new";
	
	public static boolean fileTreeSetup(AssetManager am) {
//		System.out.println("ProjectFileSetup started");
		if (externalStorageWritable()) {
//			System.out.println("external storage writable");
			File examplesFolder = new File(appPath + "/");
			try {
				if (! examplesFolder.exists()) {
					examplesFolder.mkdirs();
//					System.out.println("examples folder made");
				}
				String[] filenames = am.list("");
				for (int i=0; i<filenames.length; i++) {
					String filename = filenames[i];
					File file;
					if ((filename.endsWith(".rkt") || filename.endsWith(".scm")) &&
						! ((file = new File(examplesFolder.getPath() + File.separator + filename)).exists())) {
						InputStreamReader isr = new InputStreamReader(am.open(filename), "UTF-8");
						String output = getStringFromInputStreamReader(isr);
						writeToFile(file, output);
					}
				}
				return true;
			}
			catch (IOException e) {
				System.out.println(e);
				e.printStackTrace();
				return false;
			}
		}
		else if (externalStorageReadable()) {
			System.out.println("External storage currently read only");
			return false;
		}
		
		else {
			System.out.println("External storage inaccessible");
			return false;
		}
		
	}

	public static boolean externalStorageWritable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	public static boolean externalStorageReadable() {
		return  Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY) ||
				Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	public static List<String> getFileNames() {
		List<String> names = new ArrayList<String>();
		try {
			File[] filesList = (new File(appPath + "/")).listFiles();
			for (int i=0; i<filesList.length; i++) {
				String name = filesList[i].getName();
				if (name.endsWith(".rkt") || name.endsWith(".scm"))
					names.add(name.substring(0, name.length()-4));
			}
			return names;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			return null;
		}
	}
	
	public static String getStringFromFile(File file) {
		try {
			FileReader reader = new FileReader(file);
			return getStringFromInputStreamReader(reader);
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getStringFromInputStreamReader(InputStreamReader isr) {
		try {
			BufferedReader in = new BufferedReader(isr);
			StringBuilder buf = new StringBuilder();
		    String str;
		    while ((str = in.readLine()) != null)
		    	buf.append(str).append("\n");
		    in.close();
		    return buf.toString();
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}
	
	public static Program getProgramFromName(String extensionlessName) {
		if (externalStorageReadable()) {
			File file = new File(appPath + "/" + extensionlessName + ".rkt");
			if (file.exists()) {
//				System.out.println("Trying to get string from file now.");
				String source = getStringFromFile(file);
//				System.out.println("FileUtils.getProgram() parsing valid file now.");
				return Parser.parse(Lexer.lex(source));
			}
			else {
				System.out.println("File does not exist");
			}
		}
		else
			System.out.println("Cannot read external storage");
		return null;
	}
	
	public static boolean save(String filename, String source) {
		return writeToFile(new File(appPath + "/" + filename + ".rkt"), source);
	}
	
	public static boolean writeToFile(File file, String source) {
		PrintWriter out;
		try {
			out = new PrintWriter(file);
		    out.write(source);
		    out.close();
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
			return false;
		}
	}
	
	public static void createNewFile() {
		writeToFile(new File(appPath + "/" + NEW_FILE_NAME + ".rkt"), "");
	}
	
}
