package cs309.tonpose;

import java.util.Date;
import java.util.Random;

/**
 * Created by Quade Spellman on 9/14/2016.
 */
public class Encryption {
    private static int keyLength = 32;

    public static String createKey(){                                               //creates a new random key
            StringBuffer buffer = new StringBuffer();
            Date now = new Date();
            Random ranGen = new Random(now.getTime());
            for (int i = 0; i < keyLength; i++) {
                buffer.append((char) ranGen.nextInt(100));
            }
            return buffer.toString();
    }

    public static String encrypt(String inputString, String key){                   //encrypts a string using the given key
        int length = inputString.length();
        StringBuffer buffer = new StringBuffer(inputString);
        int temp = 1;
        int currentKey = 1;
        int j = 0;

        for(int i = 0; i < length; i++){
            temp = (int) buffer.charAt(i);
            currentKey = (int) key.charAt(i);
            temp = (temp * (currentKey-31));
            buffer.setCharAt(i, (char)temp);

            j++;
            if(j >= length){
                j = 0;
            }
        }

        return buffer.toString();
    }
    public static String decrypt(String inputString, String key) {                      //decrypts a string using the given key
        int length = inputString.length();
        StringBuffer buffer = new StringBuffer(inputString);
        int temp = 1;
        int currentKey = 1;
        int j = 0;

        for(int i = 0; i < keyLength; i++){
            temp = (int) buffer.charAt(j);
            currentKey = (int) key.charAt(i);
            temp = (temp / (currentKey-31));
            buffer.setCharAt(j, (char)temp);

            j++;
            if(j >= length){
                j = 0;
            }
        }

        return buffer.toString();
    }
}
