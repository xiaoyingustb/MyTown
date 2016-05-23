package cn.ifingers.mytown.entities;

public class ResponseObject<T> { //所有的属性对应json这种的键值

	private int state; //1表示成功  0表示失败
	private String msg;//登录和注册的返回信息
	private T datas;
	private int page;
	private int size;
	private int count;
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public T getDatas() {
		return datas;
	}
	public void setDatas(T datas) {
		this.datas = datas;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
