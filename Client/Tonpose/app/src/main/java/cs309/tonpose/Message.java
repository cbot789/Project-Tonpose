package cs309.tonpose;

import java.util.Date;
//import java.util.Random;															//unused TODO delete?

public class Message {
	private String type;
	private String data;
	//private static int keyLength = 32;
	private final String key = "132950YUDS9FH920U3Y4IDFJ3IRNMD0W";

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

	public void encrypt(){                   //encrypts type and data using the given key
		int length = type.length();
		StringBuffer buffer = new StringBuffer(type);
		int temp = 1;
		int currentKey = 1;
		int j = 0;

		for(int i = 0; i < key.length(); i++){
			temp = (int) buffer.charAt(j);
			currentKey = (int) key.charAt(i);
			temp = (temp * (currentKey-31));
			buffer.setCharAt(j, (char)temp);

			j++;
			if(j >= length){
				j = 0;
			}
		}
		type = buffer.toString();
		length = data.length();
		buffer = new StringBuffer(data);
		j = 0;

		for(int i = 0; i < key.length(); i++){
			temp = (int) buffer.charAt(j);
			currentKey = (int) key.charAt(i);
			temp = (temp * (currentKey-31));
			buffer.setCharAt(j, (char)temp);

			j++;
			if(j >= length){
				j = 0;
			}
		}
	}
	public void decrypt() {                      //decrypts type and data using the given key
		int length = type.length();
		StringBuffer buffer = new StringBuffer(type);
		int temp = 1;
		int currentKey = 1;
		int j = 0;

		for(int i = 0; i < key.length(); i++){
			temp = (int) buffer.charAt(j);
			currentKey = (int) key.charAt(i);
			temp = (temp / (currentKey-31));
			buffer.setCharAt(j, (char)temp);

			j++;
			if(j >= length){
				j = 0;
			}
		}
		type = buffer.toString();
		length = data.length();
		buffer = new StringBuffer(data);
		j = 0;

		for(int i = 0; i < key.length(); i++){
			temp = (int) buffer.charAt(j);
			currentKey = (int) key.charAt(i);
			temp = (temp / (currentKey-31));
			buffer.setCharAt(j, (char)temp);

			j++;
			if(j >= length){
				j = 0;
			}
		}
	}
	/*public static String createKey(){                                               //creates a new random key (not used) //TODO delete?
		StringBuffer buffer = new StringBuffer();
		Date now = new Date();
		Random ranGen = new Random(now.getTime());
		for (int i = 0; i < keyLength; i++) {
			buffer.append((char) ranGen.nextInt(100));
		}
		return buffer.toString();
	}*/
}
