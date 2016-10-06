package tonpose;

public class Message {
	private String type;
	private String data;
	
	public Message(String t){
		this.type = t;
	}
	public String getType(){
		return this.type;
	}
	public void setData(String d){
		this.data = d;
	}
	public String getData(){
		return this.data;
	}
}
