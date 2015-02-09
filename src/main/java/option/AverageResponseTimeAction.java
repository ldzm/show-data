package option;
import java.io.File;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.Path;

import service.HDFSService;
import service.OptionService;
import service.ParserFileService;
import bean.LineOptionBean;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

public class AverageResponseTimeAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String requestType;
	private Boolean success;
	private String basedir;
	private String hadoopcmd;
	private String taskdir;
	private String namelist;
	private String inputfiledir;
	private String outputfiledir;
	private Integer interval;
	private JSON option;
	private OptionService optionService;
	private ParserFileService parserFileService;	
    private HDFSService hdfsService;
	
    public AverageResponseTimeAction() {
		optionService = new OptionService();
		parserFileService = new ParserFileService();
		hdfsService = new HDFSService();
    }
	public Integer getInterval() {
		return interval;
	}
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getBasedir() {
		return basedir;
	}
	public void setBasedir(String basedir) {
		this.basedir = basedir;
	}
	public String getHadoopcmd() {
		return hadoopcmd;
	}
	public void setHadoopcmd(String hadoopcmd) {
		this.hadoopcmd = hadoopcmd;
	}
	public String getTaskdir() {
		return taskdir;
	}
	public void setTaskdir(String taskdir) {
		this.taskdir = taskdir;
	}
	public String getNamelist() {
		return namelist;
	}
	public void setNamelist(String namelist) {
		this.namelist = namelist;
	}
	public String getInputfiledir() {
		return inputfiledir;
	}
	public void setInputfiledir(String inputfiledir) {
		this.inputfiledir = inputfiledir;
	}
	public String getOutputfiledir() {
		return outputfiledir;
	}
	public void setOutputfiledir(String outputfiledir) {
		this.outputfiledir = outputfiledir;
	}
	public JSON getOption() {
		return option;
	}
	public void setOption(JSON option) {
		this.option = option;
	}

	@Override
	public String execute() {

		if (StringUtils.isBlank(basedir)) {
			this.addFieldError(basedir, "文件所在HDFS输入不能为空！");
			return Action.ERROR;
		}
		if (StringUtils.isBlank(inputfiledir)) {
			this.addFieldError("filedir", "文件相对HDFS路径不能为空！");
			return Action.ERROR;
		}
		if (interval < 1) {
			this.addFieldError("interval", "时间间隔必须大于0！");
			return Action.ERROR;
		}
		String filepath = AverageResponseTimeAction.class.getClassLoader().getResource("option/average_request_time.json").getPath();
		option = optionService.getAverageRequestTimeOptionAsJSON(new File(filepath));
	
		// 如果输入的目录存在则删除
		Path path = new Path(getOutputfiledir());
		String fileRootPath = path.getParent().toString();
		String directoryName = path.getName();
		hdfsService.setBasePath(getBasedir());
		hdfsService.deleteDirectory(fileRootPath, directoryName);
		
		List<String> args = Lists.newArrayList();
		args.add("-l");
		args.add(getNamelist());
		args.add("-i");
		args.add(getInterval().toString());
		boolean exeSucc = false;
		if (!hdfsService.isDirEmpty(getInputfiledir())) {
		    // 执行hadoop任务
			exeSucc = hdfsService.startMapReduce(getHadoopcmd(), getTaskdir(), getBasedir() + getInputfiledir(), getBasedir() + getOutputfiledir(), args);
		}
		
		if (!exeSucc) {
			return Action.ERROR;
		}

		List<String> linesContent = hdfsService.getLineContents(new Path(getOutputfiledir() + "/part-00000"));

		Integer divisor = parserFileService.getDivisor(linesContent, getRequestType(), getSuccess());
		
		System.out.println(divisor);
		LineOptionBean bean = parserFileService.getLineOptionBean(linesContent, getRequestType(), getSuccess(), getInterval(), divisor);
		
		JSONObject jsonObject = (JSONObject)option;
		
		// set title
		JSONObject title = JSONObject.fromObject(bean.getTitle());
		jsonObject.remove("title");
		jsonObject.accumulate("title", title);
		// set legend
		JSONObject legend = jsonObject.getJSONObject("legend");
		legend.remove("data");
		legend.accumulate("data", bean.getLegend());
		jsonObject.remove("legend");
		jsonObject.accumulate("legend", legend);
		// set xAxis
		JSONArray xAxis = jsonObject.getJSONArray("xAxis");
		JSONObject xAxis0 = xAxis.getJSONObject(0);
		xAxis0.remove("data");
		xAxis0.accumulate("data", bean.getxAxis());
		xAxis.set(0, xAxis0);
		jsonObject.remove("xAxis");
		jsonObject.accumulate("xAxis", xAxis);
		
		// set series
		JSONArray series = jsonObject.getJSONArray("series");
		JSONObject series0 = series.getJSONObject(0);
		series0.remove("data");
		series0.accumulate("data", bean.getSeries().get(0).getData());
		series0.remove("name");
		series0.accumulate("name", bean.getSeries().get(0).getName());
		series.set(0, series0);
		
		JSONObject series1 = series.getJSONObject(1);
		series1.remove("data");
		series1.accumulate("data", bean.getSeries().get(1).getData());
		series1.remove("name");
		series1.accumulate("name", bean.getSeries().get(1).getName());
		series.set(1, series1);
		jsonObject.remove("series");
		jsonObject.accumulate("series", series);
		
		option = jsonObject;
		
		System.out.println(option.toString());
		return Action.SUCCESS;
	}
}