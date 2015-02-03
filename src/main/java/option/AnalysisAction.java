package option;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.Path;

import service.HDFSService;
import service.ParserFileService;
import bean.AnalysisResultBean;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;


public class AnalysisAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	
	private String basedir;
	private String filedir;
	private List<AnalysisResultBean> analysisResultBeans;
	private ParserFileService parserFileService;	
	private HDFSService hdfsService;
	
	public AnalysisAction() {
		parserFileService = new ParserFileService();
		hdfsService = new HDFSService();
	}
	
	public String getBasedir() {
		return basedir;
	}
	public void setBasedir(String basedir) {
		this.basedir = basedir;
	}
	public String getFiledir() {
		return filedir;
	}
	public void setFiledir(String filedir) {
		this.filedir = filedir;
	}
	public List<AnalysisResultBean> getAnalysisResultBeans() {
		return analysisResultBeans;
	}
	public void setAnalysisResultBeans(List<AnalysisResultBean> analysisResultBeans) {
		this.analysisResultBeans = analysisResultBeans;
	}
	
	@Override
	public String execute() {

		if (StringUtils.isBlank(basedir)) {
			this.addActionError("文件所在HDFS输入不能为空！");
			
			return Action.ERROR;
		}
		if (StringUtils.isBlank(filedir)) {
			this.addActionError("文件相对HDFS路径不能为空！");
			return Action.ERROR;
		}
		hdfsService.setBasePath(basedir);
		List<String> linesContent = null;
		try {
			linesContent = hdfsService.getLineContents(new Path(filedir));
		} catch (Exception e) {
			e.printStackTrace();
		}
		analysisResultBeans = parserFileService.getAnalysisResultBeans(linesContent);
		
		return Action.SUCCESS;
	}
}
