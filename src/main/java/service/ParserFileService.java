package service;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.Validate;

import util.DateUtil;
import util.FileUtil;
import bean.AnalysisResultBean;
import bean.LineOptionBean;
import bean.SeriesBean;
import bean.TitleBean;

import com.google.common.collect.Lists;

public class ParserFileService {

	public static final String SEPARATOR = ",";

	public LineOptionBean getLineOptionBean(List<String> linesContent, String label, Boolean success, Integer interval, Integer divisor) {

		Validate.notNull(linesContent);
		
		LineOptionBean bean = new LineOptionBean();
		
		TitleBean title = new TitleBean();
		title.setText(label + " 统计");
		title.setSubtext("RT");
		bean.setTitle(title);

		List<String> legend = Lists.newArrayList("平均响应时间（ms）", "平均吞吐量(byte/" + 1 / Math.pow(10, divisor) + " s)");
		bean.setLegend(legend);

		for (int i = 0; i < legend.size(); i++) {
			bean.addSeries(new SeriesBean(legend.get(i)));
		}

		for (int i = 0; i < linesContent.size(); i++) {

			String[] content = linesContent.get(i).split("\t");
			String[] labels = content[0].split(SEPARATOR);
			String[] numbers = content[1].split(SEPARATOR);
			if (label.equals(labels[1])) {
				bean.addXAxis(DateUtil.date2String(new Date(Long.parseLong(labels[0]) * interval * 1000)));
				DecimalFormat df = new DecimalFormat("##0.000");
				if (success) {
					bean.getSeries().get(0).getData().add(new Float(numbers[1]));
					bean.getSeries().get(1).getData().add(Float.parseFloat(df.format(new Float(numbers[5]) / Math.pow(10, divisor))));
				} else {
					bean.getSeries().get(0).getData().add(new Float(numbers[3]));
					bean.getSeries().get(1).getData().add(Float.parseFloat(df.format(new Float(numbers[7]) / Math.pow(10, divisor))));
				}
			}
		}

		return bean;
	}

	public List<AnalysisResultBean> getAnalysisResultBeans(List<String> linesContent) {

		Validate.notNull(linesContent);
		
		List<AnalysisResultBean> beans = Lists.newArrayList();

		for (int i = 0; i < linesContent.size(); i++) {
			if (linesContent.get(i).length() > 0) {
				String[] content = linesContent.get(i).split("\t");
				String[] numbers = content[1].split(SEPARATOR);

				AnalysisResultBean bean = new AnalysisResultBean();
				bean.setLabel(content[0]);
				bean.setMax(Integer.parseInt(numbers[0]));
				bean.setMin(Integer.parseInt(numbers[1]));
				bean.setMean(Float.parseFloat(numbers[2]));
				bean.setSuccMean(Float.parseFloat(numbers[3]));
				bean.setStdDev(Float.parseFloat(numbers[4]));
				bean.setSuccStdDev(Float.parseFloat(numbers[5]));
				bean.setSuccCount(Integer.parseInt(numbers[7]));
				bean.setFailCount(Integer.parseInt(numbers[8]));
				bean.setFailRate(Float.parseFloat(numbers[9]));
				bean.setStartTime(new Date(Long.parseLong(numbers[10])));
				bean.setEndTime(new Date(Long.parseLong(numbers[11])));
				bean.setThroughput(Float.parseFloat(numbers[12]));

				beans.add(bean);
			}
		}
		return beans;
	}
	
	public List<String> getLinesContent(File src) {
		
		List<String> linesContent = FileUtil.readLines(src);
		
		return linesContent;
	}

	/**
	 * 返回值是atc 和 ac的差,也就是数量级的差
	 * @param linesContent
	 * @param label
	 * @param success
	 * @return
	 */
	public Integer getDivisor(List<String> linesContent, String label, Boolean success) {
		
		Validate.notNull(linesContent);
		
		long averageResponseTimeCount = 0L;
		long averageResponseThroughputCount = 0L;
		long count = 0L;
		for (int i = 0; i < linesContent.size(); i++) {

			String[] content = linesContent.get(i).split("\t");
			String[] labels = content[0].split(SEPARATOR);
			String[] numbers = content[1].split(SEPARATOR);
			if (label.equals(labels[1])) {
				if (success) {
					averageResponseTimeCount += numbers[1].indexOf(".") == -1 ? numbers[1].length() : numbers[1].indexOf(".");
					averageResponseThroughputCount += numbers[5].indexOf(".") == -1 ? numbers[5].length() : numbers[5].indexOf(".");
				} else {
					averageResponseTimeCount += numbers[3].indexOf(".") == -1 ? numbers[3].length() : numbers[3].indexOf(".");
					averageResponseThroughputCount += numbers[7].indexOf(".") == -1 ? numbers[7].length() : numbers[7].indexOf(".");
				}
				
				count ++;
			}
		}
		if (0 == count) {
			return 0;
		}
		Integer divisor = null;
		
		long ac = averageResponseTimeCount / count;
		long atc = averageResponseThroughputCount / count;
		
		divisor = Integer.parseInt(new Long(atc - ac).toString());
		
		return divisor;
	}
}
