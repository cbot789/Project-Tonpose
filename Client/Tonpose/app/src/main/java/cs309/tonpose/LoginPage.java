package cs309.tonpose;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cs309.tonpose.user.R;

public class LoginPage extends AppCompatActivity {

    EditText userField;
    EditText passField;
    TextView textView;

    User currentUser;

    public static final int NAMEMAX = 32;
    public static final int PASSMAX = 32;
    public static final int NAMEMIN = 4;
    public static final int PASSMIN = 4;
    public static final String TEMPKEY = "132950YUDS9FH920U3Y4IDFJ3IRNMD0W";

    //move to server
    List<User> userList = new ArrayList<User>();
    int userCount = 0;
    //end move

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        userField = (EditText) findViewById(R.id.userID);//Assign Text boxes and buttons
        passField = (EditText) findViewById(R.id.passwordInput);
        textView = (TextView) findViewById(R.id.textView);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button newUserButton = (Button) findViewById(R.id.newUserButton);

        loginButton.setOnClickListener(new View.OnClickListener() {//login attempt when "Login" Button is pressed
            @Override
            public void onClick(View view) {
                String userName = userField.getText().toString();
                String password = passField.getText().toString();
                login(userName, password);
            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener() {//create new user attempt when "New User" button is pressed
            @Override
            public void onClick(View view) {
                String userName = userField.getText().toString();
                String password = passField.getText().toString();
                createUser(userName, password);
            }
        });
    }

    public void popup(String message) { //create popup with "message" and ok button        //from alertdialog experiment
        AlertDialog.Builder errorBox = new AlertDialog.Builder(this);
        errorBox.setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        errorBox.show();
    }

    public void login(String userName, String password) {//checks list for username, then checks if passwords are equal
        String tempData = Encryption.encrypt(userName + " + " + password, TEMPKEY);
        sendData(tempData);

        /*server side
        data = receiveData();
        decryptData = Encryption.decrypt(data);
        if(checkUserPass(decryptData)){
            sendData("true");
            sendData(serverList);
        }else{
            sendData("fales");
        }
        end server side*/

        tempData = waitForServer();
        if(tempData == "true"){
            popup("Log in success!");
            receiveData();
            //go to main menu
        }
        else{
            popup("Incorrect Username or Password");
        }
    }

    public String receiveData(){            //looks for data from mysql
        //placeholder to receive data from server
        return "true";
    }
    public void sendData(String toSend){    //sendsData to mysql
        //placeholder to send data to mysql
    }
    public String waitForServer(){          //waits until timeout or data is received
        while(receiveData() == null){
            //add timeout and waiting notification
        }
        return receiveData();
    }

    public void createUser(String name, String pass) {//creates a new user and adds to list, does not allow duplicate usernames
        if (name.length() < NAMEMIN || name.length() > NAMEMAX) {
            popup("Username must be between " + NAMEMIN + " and " + NAMEMAX + " chars");

        } else if (pass.length() < PASSMIN || pass.length() > PASSMAX) {
            popup("Password must be between " + PASSMIN + " and " + PASSMAX + " chars");

        } else {
            sendData("Create User " + name + " with password: " + pass);
            String response = waitForServer();
            if(response == "true"){
                popup("User #" + userCount + " created with Username '" + name + "'");
                currentUser = new User(name, pass, TEMPKEY);
            }
            else{
                popup("Username '" + name + "' Taken");
            }
            /*Server side
            if (checkUser(name)) {
                User user = new User(name, pass);
                userList.add(user);
                userCount++;
                String tempTesting = userList.get(userCount - 1).getUser();
                send(true);
            } else {
               send(false);
            }
            end server side*/
        }
    }
    //move to server side all functions below

    public boolean checkUserPass(String userName, String password){ //checks username and password with database of all existing users and password
        for (User current : userList) {
            if (userName.equals(current.getUser())) {
                if (password.equals(current.getPassword())) {       //if username and password match, login
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean checkUser(String name) {//checks list of users for givin string, returns false if name is there
        for (User user : userList) {
            if (name.equals(user.getUser())) {
                return false;
            }
        }
        return true;
    }
}
