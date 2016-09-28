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

    List<User> userList = new ArrayList<User>();
    int userCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        userField = (EditText) findViewById(R.id.userID);//Assign Text boxes and buttons
        passField = (EditText) findViewById(R.id.passwordInput);
        textView = (TextView) findViewById(R.id.textView);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        Button newUserButton  = (Button) findViewById(R.id.newUserButton);

        loginButton.setOnClickListener(new View.OnClickListener(){//login attempt when "Login" Button is pressed
            @Override
            public void onClick(View view){
                String userName = userField.getText().toString();
                String password = passField.getText().toString();
                login(view, userName, password);
            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener(){//create new user attempt when "New User" button is pressed
            @Override
            public void onClick(View view){
                String userName = userField.getText().toString();
                String password = passField.getText().toString();
                createUser(view, userName, password);
            }
        });
    }

    public void popup(View view, String message){ //create popup with "message" and ok button        //from alertdialog experiment
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

    public boolean checkUserName(String name){//checks list of users for givin string, returns false if name is there
        for (User user: userList) {
            if(name.equals(user.getUser())){
                return false;
            }
        }
        return true;
    }

    public boolean login(View view, String userName, String password){//checks list for username, then checks if passwords are equal
        for (User current: userList) {
            if(userName.equals(current.getUser())) {
                if (password.equals(current.getPassword())) {
                    popup(view, "Logging in");

                    return true;
                }
                else{
                    popup(view, "Incorrect Username or Password");
                    return false;
                }
            }
        }
        popup(view, "Incorrect Username or Password");
        return false;
    }

    public  boolean checkLength(View view, String name, String pass) {//if password and username are between 4 and 32 chars, return true
        if (name.length() < 4 || name.length() > 32) {
            popup(view, "Username must be between 4 and 32 chars");
            return false;
        } else if (pass.length() < 4 || pass.length() > 32) {
            popup(view, "Password must be between 4 and 32 chars");
            return false;
        } else {
            return true;
        }
    }

    public void createUser(View view, String name, String pass){//creates a new user and adds to list
        if(checkLength(view, name, pass)){
            if(checkUserName(name)){
                User user = new User(name, pass);
                userList.add(user);
                userCount++;
                String tempTesting = userList.get(userCount - 1).getUser();
                popup(view, "User #" + userCount + " created with Username '" + name + "'");
            }
            else{
                popup(view, "Username '" + name + "' Taken");
            }
        }
    }
}
