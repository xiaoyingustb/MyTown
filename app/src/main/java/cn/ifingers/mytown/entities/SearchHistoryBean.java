package cn.ifingers.mytown.entities;

public class SearchHistoryBean {
	private String time;
	private String title;

	public SearchHistoryBean() {
	}

	public SearchHistoryBean(String time, String title) {
		this.time = time;
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
