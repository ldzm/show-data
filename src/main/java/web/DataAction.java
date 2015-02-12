package web;

import java.io.File;
import java.util.Iterator;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import util.FileUtil;
import util.JsonUtil;

import com.opensymphony.xwork2.ActionSupport;

public class DataAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private String actionName;
	private String requestTypeKey;
	private String requestTypeValue;
	private JSON data;

	
	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getRequestTypeKey() {
		return requestTypeKey;
	}

	public void setRequestTypeKey(String requestTypeKey) {
		this.requestTypeKey = requestTypeKey;
	}

	public String getRequestTypeValue() {
		return requestTypeValue;
	}

	public void setRequestTypeValue(String requestTypeValue) {
		this.requestTypeValue = requestTypeValue;
	}

	public JSON getData() {
		return data;
	}

	public void setData(JSON data) {
		this.data = data;
	}

	@Override
	public String execute() {

		if ("Average".equals(actionName)) {
			data = readData("data/average_request_time.json");
		} else if ("Analysis".equals(actionName)) {
			data = readData("data/analysis.json");
		} else if (!("".equals(requestTypeKey) || "".equals(requestTypeValue))) {
			data = readData("data/request_type.json");
			
			if (!contains(data, requestTypeKey)) {
				((JSONObject) data).accumulate(requestTypeKey, requestTypeValue);
				
				// 保存数据
				String file = DataAction.class.getClassLoader().getResource("data/request_type.json").getPath();
				FileUtil.saveFile(new File(file), data.toString(), false);
			} else {
				requestTypeKey = "false";
			}
			
		} else {
			this.addActionError("数据请求错误！");
			return ERROR;
		}

		return SUCCESS;
	}

	private JSON readData(String filename) {
		String filepath = DataAction.class.getClassLoader().getResource(filename).getPath();
		return JsonUtil.readFileToJSON(new File(filepath));
	}
	
	private boolean contains(JSON json, String key) {

		JSONObject jsonObject = (JSONObject) json;
		Iterator<?> it = jsonObject.keys();
		// 遍历jsonObject的keys，看是否含有key，包含返回true
		while (it.hasNext()) {
			String temKey = String.valueOf(it.next());
			if (key.equals(temKey)) {
				return true;
			}
		}
		return false;
	}
}
