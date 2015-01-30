package bean;

import java.util.List;

import com.google.common.collect.Lists;

public class SeriesBean {

	private String name;
	private List<Float> data;

	public SeriesBean() {
		data = Lists.newArrayList();
	}

	public SeriesBean(String name) {
		this.name = name;
		data = Lists.newArrayList();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Float> getData() {
		return data;
	}

	public void setData(List<Float> data) {
		this.data = data;
	}

	public void add(Float value) {
		data.add(value);
	}

	@Override
	public String toString() {
		return "SeriesBean [name=" + name + ", data=" + data + "]";
	}
	
//	public static void main(String[] args) {
//		SeriesBean seriesBean = new SeriesBean();
//		
//		seriesBean.add(new Float(0));
//	}
}
