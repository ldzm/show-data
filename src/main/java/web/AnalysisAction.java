package web;

import java.io.File;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.hadoop.fs.Path;

import service.HDFSService;
import service.ParserFileService;
import util.FileUtil;
import bean.AnalysisResultBean;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

public class AnalysisAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	private String basedir;
	private String hadoopcmd;
	private String taskdir;
	private String namelist;
	private String inputfiledir;
	private String outputfiledir;
	private Boolean exetask;
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

	public Boolean getExetask() {
		return exetask;
	}

	public void setExetask(Boolean exetask) {
		this.exetask = exetask;
	}

	public List<AnalysisResultBean> getAnalysisResultBeans() {
		return analysisResultBeans;
	}

	public void setAnalysisResultBeans(List<AnalysisResultBean> analysisResultBeans) {

		this.analysisResultBeans = analysisResultBeans;
	}

	@Override
	public String execute() {

		hdfsService.setBasePath(basedir);
		

		if (exetask) {
			// 如果输入的目录存在则删除
			Path path = new Path(outputfiledir);
			String fileRootPath = path.getParent().toString();
			String directoryName = path.getName();

			hdfsService.deleteDirectory(fileRootPath, directoryName);

			List<String> args = Lists.newArrayList();
			args.add("-l");
			args.add(getNamelist());
			boolean exeSucc = false;
			if (!hdfsService.isDirEmpty(getInputfiledir())) {
				// 执行hadoop任务
				exeSucc = hdfsService.startMapReduce(hadoopcmd, taskdir, basedir + inputfiledir, basedir
						+ path.getParent().toString(), args);
			}

			if (!exeSucc) {
				this.addActionError("执行" + outputfiledir + "任务失败:(");
				return Action.ERROR;
			} else {
				saveAnalysisFormData();
			}
		}
		List<String> linesContent = hdfsService.getLineContents(outputfiledir);

		if (null != linesContent) {
			saveAnalysisFormData();
		} else {
			this.addActionError("从" + outputfiledir + "读取数据失败:(");
			return Action.ERROR;
		}
		analysisResultBeans = parserFileService.getAnalysisResultBeans(linesContent);

		return Action.SUCCESS;
	}

	private void saveAnalysisFormData() {
		// 保存表单数据
		JSONObject json = new JSONObject();
		json.accumulate("basedir", basedir);
		json.accumulate("hadoopcmd", hadoopcmd);
		json.accumulate("taskdir", taskdir);
		json.accumulate("namelist", namelist);
		json.accumulate("inputfiledir", inputfiledir);
		json.accumulate("outputfiledir", outputfiledir);

		String file = AnalysisAction.class.getClassLoader().getResource("data/analysis.json").getPath();
		FileUtil.saveFile(new File(file), json.toString(), false);
	}
}
