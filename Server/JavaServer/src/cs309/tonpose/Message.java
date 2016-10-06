package cs309.tonpose;

import java.io.Serializable;

public class Message implements Serializable{

	private String type;
	private String data1 = null;
	private String data2 = null;
	private int data3;
	private int data4;
	
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
	public void setData3(int d){
		this.data3 = d;
	}
	public void setData4(int d){
		this.data4 = d;
	}
	public String getData1(){
		return this.data1;
	}
	public String getData2(){
		return this.data2;
	}
	public int getData3(){
		return this.data3;
	}
	public int getData4(){
		return this.data4;
	}
}
