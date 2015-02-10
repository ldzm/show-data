package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;

import define.Define;

public class FileUtil{

	public static List<String> readLines(File file) {
		List<String> result = null;
		
		try {
			result = FileUtils.readLines(file, Define.ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void saveFile(File file, String content, boolean append) {
		
		FileUtil.createDir(file.getParentFile());
		FileUtil.createFile(file);
		OutputStream out = null;
		try {
			out = new FileOutputStream(file, append);
			try {
				out.write(content.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean createFile(File file) {
		
		boolean result = false;
		if (!file.exists()) {
			try {
				result = file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			result = true;
		}
		
		return result;
	}
	
	public static boolean createDir(File src) {
		
		boolean result = false;
		if (!src.exists()) {
			result = src.mkdirs();
		} else {
			result = true;
		}
		
		return result;
	}
	
//	public static void main(String[] args) {
//		FileUtil.saveFile(new File("/home/sky/Desktop/testtest1/test4/a.txt"), "abc", true);
//	}
}
