package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    // reference to buttons and other controls on the layout
    private Button btnLogin;
    private EditText editTextUserName, editTextPassword;
    private String token;

    int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextPassword = findViewById(R.id.editTextPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Code that I used to create admin
//                DatabaseReference databaseUser;
//                databaseUser = FirebaseDatabase.getInstance().getReference("users");
//
//                String id = databaseUser.push().getKey();
//
//                UserHelperClass adminData = new UserHelperClass( true, "admin@123.com", false,
//                        id,"admin", "123admin456");
//
//                databaseUser.child("admin").setValue(adminData);

                String currentUserName = editTextUserName.getText().toString();

                Common.userName = currentUserName;


                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
                DatabaseReference refUsersAccount = ref.child(currentUserName);


                if (editTextUserName.getText().toString().isEmpty()) {
                    editTextUserName.setError("Please Enter User Name");
                } else if (editTextPassword.getText().toString().isEmpty()) {
                    editTextPassword.setError("Please Enter Password");
                } else {
                    refUsersAccount.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String userPassword = dataSnapshot.child("password").getValue(String.class);
                                if (editTextPassword.getText().toString().equals(userPassword)) {
                                    boolean isAdmin = dataSnapshot.child("admin").getValue(boolean.class);
                                    boolean isEmployee = dataSnapshot.child("employee").getValue(boolean.class);
                                    if (isAdmin == true) {
                                        Intent intent = new Intent(getApplicationContext(), WelcomePageAdmin.class); //Directs to Admin Screen
                                        startActivityForResult(intent, 0);
                                    } else if (isEmployee == true) {
                                        Intent intent = new Intent(getApplicationContext(), WelcomePageEmployee.class); //Directs to Employee Screen
                                        startActivityForResult(intent, 0);
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), WelcomePageUsers.class); //Directs to Users Screen
                                        startActivityForResult(intent, 0);
                                    }

                                }
                                ;
                            } else {
                                editTextUserName.setError("Username or password is incorrect");
                                editTextPassword.setError("Username or password is incorrect");
                            }
                        }

                        ;

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
    }

    // sign up button
    public void SignUp(View view) {
        //Application Context and Activity
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivityForResult(intent, 0);
    }

}

