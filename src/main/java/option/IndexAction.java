package option;

import java.util.Map;
import java.util.TreeMap;

import com.opensymphony.xwork2.ActionSupport;

public class IndexAction extends ActionSupport{


	private static final long serialVersionUID = 1L;
	
	private  Map<String, String> requestTypeMap;

	public IndexAction() {
		requestTypeMap = new TreeMap<String, String>();
		requestTypeMap.put("HTTP Request", "HTTP 请求");
		requestTypeMap.put("JAVA Request", "JAVA 请求");
	}

	public Map<String, String> getRequestTypeMap() {
		return requestTypeMap;
	}
	public void setRequestTypeMap(Map<String, String> requestTypeMap) {
		this.requestTypeMap = requestTypeMap;
	}

	@Override
	public String execute() throws Exception {
		
		return SUCCESS;
	}
}
