package bean;

import java.util.Date;

public class AnalysisResultBean {


	private String label;
	private Integer max;
	private Integer min;
	private Float mean;
	private Float succMean;
	private Float stdDev;
	private Float succStdDev;
	private Integer succCount;
	private Integer failCount;
	private Float failRate;
	private Date startTime;
	private Date endTime;
	private Float throughput;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getMax() {
		return max;
	}
	public void setMax(Integer max) {
		this.max = max;
	}
	public Integer getMin() {
		return min;
	}
	public void setMin(Integer min) {
		this.min = min;
	}
	public Float getMean() {
		return mean;
	}
	public void setMean(Float mean) {
		this.mean = mean;
	}
	public Float getSuccMean() {
		return succMean;
	}
	public void setSuccMean(Float succMean) {
		this.succMean = succMean;
	}
	public Float getStdDev() {
		return stdDev;
	}
	public void setStdDev(Float stdDev) {
		this.stdDev = stdDev;
	}
	public Float getSuccStdDev() {
		return succStdDev;
	}
	public void setSuccStdDev(Float succStdDev) {
		this.succStdDev = succStdDev;
	}
	public Integer getSuccCount() {
		return succCount;
	}
	public void setSuccCount(Integer succCount) {
		this.succCount = succCount;
	}
	public Integer getFailCount() {
		return failCount;
	}
	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}
	public Float getFailRate() {
		return failRate;
	}
	public void setFailRate(Float failRate) {
		this.failRate = failRate;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Float getThroughput() {
		return throughput;
	}
	public void setThroughput(Float throughput) {
		this.throughput = throughput;
	}
	
	@Override
	public String toString() {
		return "AnalysisResultBean [label=" + label + ", max=" + max + ", min=" + min + ", mean=" + mean
				+ ", succMean=" + succMean + ", stdDev=" + stdDev + ", succStdDev=" + succStdDev + ", succCount="
				+ succCount + ", failCount=" + failCount + ", failRate=" + failRate + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", throughput=" + throughput + "]";
	}
}
