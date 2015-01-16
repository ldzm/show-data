package service;

import java.io.File;
import java.util.List;

import util.FileUtil;
import bean.AverageRequestTimeBean;
import bean.SeriesBean;

public class ParserFileService {
	
	public static final String SEPARATOR = ",";
	
	public AverageRequestTimeBean getAverageRequestTimeBean(File file, List<String> label) {
		
		List<String> linesContent = FileUtil.readLines(file);
		AverageRequestTimeBean bean = new AverageRequestTimeBean();
		bean.setLegend(label);
		
		for (int i = 0; i < label.size(); i++) {
			bean.addSeries(new SeriesBean(label.get(i)));
		}
		
		for (int i = 0; i < linesContent.size(); i++) {

			String [] content = linesContent.get(i).split("\t");
			String [] labels = content[0].split(SEPARATOR);
			String [] numbers = content[1].split(SEPARATOR);
			
			if (!bean.getxAxis().contains(labels[0])) {
				bean.addXAxis(labels[0]);
			}
			
			for (int j = 0; j < label.size(); j++) {
				if (labels[1].equals(label.get(j))) {
					bean.getSeries().get(j).getData().add(new Integer(numbers[0]));
				}
			}
		}

		return bean;
	}
}
