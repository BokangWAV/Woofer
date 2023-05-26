package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {
    TextView login;
    OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        login = (TextView)findViewById(R.id.LoginAccountId);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intents are objects of the android.content.Intent.
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    public void doPost(View V){
        TextView requestOutput = (TextView)findViewById(R.id.accountTextId);
        EditText inputUsername = (EditText)findViewById(R.id.SignUpUsernameID);
        EditText inputPassword = (EditText)findViewById(R.id.SignUpPasswordID);
        EditText inputEmail = (EditText)findViewById(R.id.EmailAddressId);
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();
        String email = inputEmail.getText().toString();
        post("https://lamp.ms.wits.ac.za/home/s2556243/signup.php",username, password, email,new Callback() {
            @Override
            public void onFailure(Call call, IOException e){

            }

            @Override
            public void onResponse(Call call, Response response) throws  IOException {
                if(response.isSuccessful()){
                    final String responseStr = response.body().string();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            requestOutput.setText(responseStr);
                            if(responseStr == "Username Already Exists"){
                                Toast toast=Toast.makeText(getApplicationContext(),"Username Already Exists",Toast.LENGTH_SHORT);
                                toast.setMargin(50,50);
                                toast.show();
                            }
                            else if(responseStr == "Invalid Format"){
                                Toast toast=Toast.makeText(getApplicationContext(),"Invalid Format",Toast.LENGTH_SHORT);
                                toast.setMargin(50,50);
                                toast.show();
                            }
                            else if(responseStr == "Invalid Email Address"){
                                Toast toast=Toast.makeText(getApplicationContext(),"Invalid Email Address",Toast.LENGTH_SHORT);
                                toast.setMargin(50,50);
                                toast.show();
                            }
                            else{
                                Toast toast=Toast.makeText(getApplicationContext(),"Account Has Been Created Successfully",Toast.LENGTH_SHORT);
                                toast.setMargin(50,50);
                                toast.show();
                            }
                        }
                    });

                }
                else {
                    // request not successful
                }
            }
        });
    }


    public  static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Call post(String url, String username, String password, String email,Callback callback){
        RequestBody formbody = new FormBody.Builder()
                .add("user",username)
                .add("password", password)
                .add("email", email)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formbody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }
}