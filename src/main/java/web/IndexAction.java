package web;

import java.io.File;
import java.util.Map;

import net.sf.json.JSON;
import util.JsonUtil;

import com.opensymphony.xwork2.ActionSupport;

public class IndexAction extends ActionSupport{


	private static final long serialVersionUID = 1L;
	
	private  Map<String, String> requestTypeMap;

	public IndexAction() {
	}

	public Map<String, String> getRequestTypeMap() {
		return requestTypeMap;
	}
	public void setRequestTypeMap(Map<String, String> requestTypeMap) {
		this.requestTypeMap = requestTypeMap;
	}

	@Override
	public String execute() throws Exception {
		String filepath = AverageResponseTimeAction.class.getClassLoader().getResource("data/request_type.json").getPath();
		JSON requestType = JsonUtil.readFileToJSON(new File(filepath));
		requestTypeMap = JsonUtil.toHashMap(requestType);

		return SUCCESS;
	}
}
