package web;

import java.io.File;

import net.sf.json.JSON;
import util.JsonUtil;

import com.opensymphony.xwork2.ActionSupport;

public class DataAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private String actionName;
	private JSON data;

	
	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public JSON getData() {
		return data;
	}

	public void setData(JSON data) {
		this.data = data;
	}

	@Override
	public String execute() {

		if (getActionName().equals("Average")) {
			String filepath = AverageResponseTimeAction.class.getClassLoader().getResource("data/average_request_time.json").getPath();
			data = JsonUtil.readFileToJSON(new File(filepath));
		} else if (getActionName().equals("Analysis")) {
			String filepath = AverageResponseTimeAction.class.getClassLoader().getResource("data/analysis.json").getPath();
			data = JsonUtil.readFileToJSON(new File(filepath));
		} else {
			this.addActionError("数据请求错误！");
			return ERROR;
		}
		
		return SUCCESS;
	}
}
