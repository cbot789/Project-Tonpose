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

public class LoginPage extends AppCompatActivity {

    EditText userField;
    EditText passField;
    TextView textView;

    Client client;
    static Boolean connected = false;                                           //is the app currently connected to the server //TODO remove?

    private Message response = null;                                       //response from server

    public static final int NAMEMAX = 32;                                       //Max and min lenghts for passwords and usernames
    public static final int PASSMAX = 32;
    public static final int NAMEMIN = 4;
    public static final int PASSMIN = 4;
    public static final String TEMPKEY = "132950YUDS9FH920U3Y4IDFJ3IRNMD0W";        //key used for encryption

    private String userName;                                                        //last entered strings in username and password fields
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

        loginButton.setOnClickListener(new View.OnClickListener() {             //login attempt when "Login" Button is pressed
            @Override
            public void onClick(View view) {                                    //when login button is pressed
                if (!connected) {
                    userName = userField.getText().toString();
                    password = passField.getText().toString();
                    try {
                        loginAttempt();
                    } catch (Exception e) {
                        e.printStackTrace();
                        popup("Cannot Connect to Server", true);
                    }
                } else {
                    if (client.getMessage().getType() != null) {
                        if (response == null) {
                            response = client.getMessage();
                            receiveData();
                        }
                    }else{
                        popup("Waiting for Server response", false);
                    }
                }
            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener() {           //create new user attempt when "New User" button is pressed
            @Override
            public void onClick(View view) {                                 //when new user button is pressed
                if (!connected) {
                    userName = userField.getText().toString();
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
    }

    public void popup(String message, Boolean button) {                         //create popup with "message" and ok button if input 'button' is true
        if (button) {
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

    public void loginAttempt() throws Exception {                               //attempts to log user in with given password and username
        if (checkLength()) {
            sendData("login", userName + " " + password);                       //TODO change strings
        }
    }

    public void login() {                                                       //when server responds to login attempt
        if (response.getData() == "true") {                                     //TODO change string
            popup("Log in success!", false);
            goToMainMenu();
        } else {
            popup("Incorrect Username or Password", false);
        }
    }

    public void createUserAttempt() throws Exception {                          //connects to server with request to create new user
        if (checkLength()) {
            sendData("create", userName + " " + password);                      //TODO change strings
        }
    }

    public void userCreated() {                                                  //when server responds to create attempt
        if (response.getData() == "true") {                                     //TODO change string
            popup("User created with Username '" + userName + "'", false);
            goToMainMenu();
        } else {
            popup("Username '" + userName + "' Taken", false);
        }
    }

    public void sendData(String inputType, String toSend) throws Exception {                      //sends message with type of inputType and Data of toSend, also encrypts
        if (!connected) {
            client = new Client("10.25.70.112", 8080);
            connected = true;
        }
        if (connected) {                                                            //TODO uneeded if?
            String type = Encryption.encrypt(inputType, TEMPKEY);
            String data = Encryption.encrypt(toSend, TEMPKEY);
            Message message = new Message(type);
            message.setData(data);
            client.setMsg(message);
            client.execute();
        } else {
            popup("ERROR: not connected to server", true);
        }
    }

    public void goToMainMenu() {                                                //goes to main menu screen      //TODO move to new global class?
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    public void receiveData() {                                                 //when data is reveived, deciedes which function to call based on message type
        connected = false;
        switch (response.getType()) {                                           //TODO test
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
    }

    public boolean checkLength() {                                              //returns false and displays message if either sting is too short or long
        if (userName.length() < NAMEMIN || userName.length() > NAMEMAX) {
            popup("Username must be between " + NAMEMIN + " and " + NAMEMAX + " chars", false);
            return false;

        } else if (password.length() < PASSMIN || password.length() > PASSMAX) {
            popup("Password must be between " + PASSMIN + " and " + PASSMAX + " chars", false);
            return false;
        }
        return true;
    }
}
