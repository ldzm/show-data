package util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import net.sf.json.JSON;
import net.sf.json.JSONObject;

public class FileUtil{

	public static final String ENCODING = "UTF-8";
	
	
	public static JSON readFileToJSON(File file) {
		
		String content = null;
		try {
			content = FileUtils.readFileToString(file, ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JSON result = JSONObject.fromObject(content);
		
		return result;
	}
	
	public static List<String> readLines(File file) {
		List<String> result = null;
		
		try {
			result = FileUtils.readLines(file, ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	/*
	public static void main(String[] args) {
		JSON json = FileUtil.readFileToJSON(new File("resources/average_request_time.json"));
		
		System.out.println(json.toString());
	}*/
}
