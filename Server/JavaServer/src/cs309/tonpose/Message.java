package cs309.tonpose;

import java.io.Serializable;

public class Message implements Serializable{

	private String type;
	private String data1 = null;
	private String data2 = null;
	
	public Message(String t){
		this.type = t;
	}
	public String getType(){
		return this.type;
	}
	public void setData1(String d){
		this.data1 = d;
	}
	public void setData2(String d){
		this.data2 = d;
	}
	public String getData1(){
		return this.data1;
	}
	public String getData2(){
		return this.data2;
	}
}
