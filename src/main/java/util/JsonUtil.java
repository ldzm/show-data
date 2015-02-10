package util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

import define.Define;
import net.sf.json.JSON;
import net.sf.json.JSONObject;

public class JsonUtil {

	public static JSON readFileToJSON(File file) {
		
		String content = null;
		try {
			content = FileUtils.readFileToString(file, Define.ENCODING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JSON result = JSONObject.fromObject(content);
		
		return result;
	}
	/**
	 * 将json格式的字符串解析成Map对象
	 * json格式：{"a":"A","b":"B"}
	 */
	public static HashMap<String, String> toHashMap(JSON json) {
		HashMap<String, String> result = new HashMap<String, String>();
		// 将json转换成jsonObject
		JSONObject jsonObject = (JSONObject) json;
		Iterator<?> it = jsonObject.keys();
		// 遍历jsonObject数据，添加到Map对象
		while (it.hasNext()) {
			String key = String.valueOf(it.next());
			String value = (String) jsonObject.get(key);
			result.put(key, value);
		}
		return result;
	}
}
