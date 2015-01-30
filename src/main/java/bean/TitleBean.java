package bean;

public class TitleBean {

	private String text;
	private String subtext;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSubtext() {
		return subtext;
	}
	public void setSubtext(String subtext) {
		this.subtext = subtext;
	}
	
	@Override
	public String toString() {
		return "TitleBean [text=" + text + ", subtext=" + subtext + "]";
	}
}
