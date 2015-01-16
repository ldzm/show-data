package bean;

import java.util.List;

import com.google.common.collect.Lists;

public class AverageRequestTimeBean {

	private List<String> legend;
	private List<String> xAxis;
	private List<SeriesBean> series;
	
	public AverageRequestTimeBean() {
		legend = Lists.newArrayList();
		xAxis = Lists.newArrayList();
		series = Lists.newArrayList();
	}
	
	public void addLegend(String value) {
		legend.add(value);
	}
	public void addXAxis(String value) {
		xAxis.add(value);
	}
	public void addSeries(SeriesBean value) {
		series.add(value);
	}
	public List<String> getLegend() {
		return legend;
	}
	public void setLegend(List<String> legend) {
		this.legend = legend;
	}
	public List<String> getxAxis() {
		return xAxis;
	}
	public void setxAxis(List<String> xAxis) {
		this.xAxis = xAxis;
	}
	public List<SeriesBean> getSeries() {
		return series;
	}
	public void setSeries(List<SeriesBean> series) {
		this.series = series;
	}

	@Override
	public String toString() {
		return "AverageRequestTimeBean [legend=" + legend + ", xAxis=" + xAxis
				+ ", series=" + series + "]";
	}
}
