package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    OkHttpClient client = new OkHttpClient();
    String url = "https://lamp.ms.wits.ac.za/home/s2556243/login.php";

    TextView output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output = (TextView)findViewById(R.id.CreateAccountId);

        output.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intents are objects of the android.content.Intent.
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });



    }
    public void doPost(View V){
        output = (TextView)findViewById(R.id.loginErrorID);
        EditText inputUsername = (EditText)findViewById(R.id.UsernameID);
        EditText inputPassword = (EditText)findViewById(R.id.PasswordID);
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();

        post(url,username,password,new Callback() {
            @Override
            public void onFailure(Call call, IOException e){

            }

            @Override
            public void onResponse(Call call, Response response) throws  IOException {
                if(response.isSuccessful()){
                    String responseStr = response.body().string();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processJSON(responseStr);
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

    Call post(String url, String username,String password,Callback callback){
        RequestBody formbody = new FormBody.Builder()
                .add("user_name",username)
                .add("password",password)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formbody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public void processJSON(String json){
        try {
            JSONArray all = new JSONArray(json);

            if(all.length()==0){ // means password & username don't match
                output.setTextColor(Color.RED);
                output.setText("Username Or Password Is Incorrect");
            }
            else{
                Intent intent = new Intent(LoginActivity.this, LandingPage.class);
                startActivity(intent);
                Toast toast=Toast.makeText(getApplicationContext(),"Login Successful.",Toast.LENGTH_SHORT);
                toast.setMargin(50,50);
                toast.show();

            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


}