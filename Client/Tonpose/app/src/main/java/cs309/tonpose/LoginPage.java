package cs309.tonpose;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import cs309.tonpose.user.R;

public class LoginPage extends AppCompatActivity { //TODO force login to landscape
                                                    //TODO
    EditText userField;
    EditText passField;
    TextView textView;

    Client client;
    static Boolean connected = false;                                           //is the app currently connected to the server

    private Message response = null;                                       //response from server

    public static final int NAMEMAX = 32;                                       //Max and min lenghts for passwords and usernames
    public static final int PASSMAX = 32;
    public static final int NAMEMIN = 4;
    public static final int PASSMIN = 4;

    private String username;                                                        //last entered strings in username and password fields
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        userField = (EditText) findViewById(R.id.userID);                           //Assign Text boxes and buttons
        passField = (EditText) findViewById(R.id.passwordInput);
        textView = (TextView) findViewById(R.id.textView);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button newUserButton = (Button) findViewById(R.id.newUserButton);
        Button offlineButton = (Button) findViewById(R.id.offline);

        loginButton.setOnClickListener(new View.OnClickListener() {             //login attempt when "Login" Button is pressed
            @Override
            public void onClick(View view) {                                    //when login button is pressed
                if (!connected) {
                    username = userField.getText().toString();
                    password = passField.getText().toString();
                    try {
                        loginAttempt();
                    } catch (Exception e) {
                        e.printStackTrace();
                        popup("Cannot Connect to Server", true);
                    }
                }
                /*else {
                    if (client.getMessage().getType() != null) {                                    //TODO change to always checking instead of just when login button is pressed
                        if (response == null) {
                            response = client.getMessage();
                            connected = false;
                            receiveData();
                        }
                    }else{
                        popup("Waiting for Server response", false);
                    }
                }*/
            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener() {           //create new user attempt when "New User" button is pressed
            @Override
            public void onClick(View view) {                                 //when new user button is pressed
                if (!connected) {
                    username = userField.getText().toString();
                    password = passField.getText().toString();
                    try {
                        createUserAttempt();
                    } catch (Exception e) {
                        e.printStackTrace();
                        popup("Cannot Connect to Server", true);
                    }
                } else {
                    popup("Waiting for Server response", false);
                }
            }
        });

        offlineButton.setOnClickListener(new View.OnClickListener() {           //create new user attempt when "New User" button is pressed
            @Override
            public void onClick(View view) {                                 //when new user button is pressed
               popup("Offline Mode", false);
                goToMainMenu();
            }
        });
    }

    public void popup(String message, Boolean button) {                         //create popup with "message" and ok button if input 'button' is true
        if (button) {                                                           //TODO move to 'global' static class? getApplicationContext and this are not static
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
        } else {
            Context context = getApplicationContext();
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void loginAttempt() throws Exception {                               //attempts to log user in with given password and username
        if (checkLength()) {
            connect();
            //sendData("login", username + " " + password);                       //TODO change strings?
        }
    }

    private void login() {                                                       //when server responds to login attempt
        if (response.getData().equals("true")) {                                     //TODO change string?
            popup("Log in success!", false);
            goToMainMenu();
        } else {
            popup("Incorrect Username or Password", false);
        }
    }

    private void createUserAttempt() throws Exception {                          //connects to server with request to create new user
        if (checkLength()) {
            connect();
            //sendData("create", username + " " + password);                      //TODO change strings?
        }
    }

    private void userCreated() {                                                  //when server responds to create attempt
        if (response.getData() == "true") {                                     //TODO change string?
            popup("User created with Username '" + username + "'", false);
            goToMainMenu();
        } else {
            popup("Username '" + username + "' Taken", false);
        }
    }

    /*public void sendData(String inputType, String toSend) throws Exception {                      //sends message with type of inputType and Data of toSend, also encrypts
        if (!connected) {                                                                           //TODO move to 'global' static class?
            client = new Client("10.25.70.122", username, 8080);
            connected = true;
        }
        Message message = new Message(inputType);
        message.setData(toSend);
        client.setMsg(message);
        client.execute();
    }*/

    public void goToMainMenu() {                                                //goes to main menu screen      //TODO move to new global class?
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    /*public void receiveData() {                                                 //when data is received, decides which function to call based on message type
        switch (response.getType()) {
            case "serverType":
                popup(response.getData(), true);
            case "login":
                login();
                break;
            case "create":
                userCreated();
                break;
            case "error":
                popup("Server Timeout", false);
                break;
            default:
                popup("Unknown Error", true);
                break;
        }
        response = null;
    }*/

    public boolean checkLength() {                                                      //returns false and displays message if either sting is too short or long
        if (username.length() < NAMEMIN || username.length() > NAMEMAX) {               //TODO dont allow spaces
            popup("Username must be between " + NAMEMIN + " and " + NAMEMAX + " chars", false);
            return false;

        } else if (password.length() < PASSMIN || password.length() > PASSMAX) {
            popup("Password must be between " + PASSMIN + " and " + PASSMAX + " chars", false);
            return false;
        }
        return true;
    }

    public void connect(){
        String ip = "10.25.70.122"; //DK-02 Server IP
        int port = 8080;
        String type = "type1";
        if(client == null) {
            try {
                client = new Client(ip, username, port);
                client.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Message msg = new Message(type);
        msg.setData("Attempting to connect");
        client.sendMsg(msg);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(client.serverMsg);

    }
}
