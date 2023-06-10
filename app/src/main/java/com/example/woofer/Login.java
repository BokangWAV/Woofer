package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class Login extends AppCompatActivity {
    TextView textViewSignUp, textViewError;
    Button Login;
    TextInputEditText TextInputEditTextLoginEmail, TextInputEditTextPassword;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    String apikey, username, fullname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputEditTextLoginEmail = findViewById(R.id.email);
        TextInputEditTextPassword = findViewById(R.id.password);

        textViewSignUp = findViewById(R.id.signUpText);
        textViewError = findViewById(R.id.LoginError);
        progressBar = findViewById(R.id.progress);
        sharedPreferences = getSharedPreferences("Woofer", MODE_PRIVATE);

        if(sharedPreferences.getString("logged", "false").equals("true")){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        Login = findViewById(R.id.buttonLogin);
        Login.setOnClickListener(new View.OnClickListener() {
            //textInputEditTextUsername where we input the email instead
            String email = String.valueOf(TextInputEditTextLoginEmail.getText());
            String password = String.valueOf(TextInputEditTextPassword.getText());

            @Override
            public void onClick(View view) {
                textViewError.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                email = String.valueOf(TextInputEditTextLoginEmail.getText());
                password = String.valueOf(TextInputEditTextPassword.getText());

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="https://lamp.ms.wits.ac.za/home/s2494554/login.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                //Passing JSON String to collect string
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    //Now access status and message from string object
                                    String status = jsonObject.getString("status");
                                    String message = jsonObject.getString("message");
                                    if(status.equals("success")){
                                        username = jsonObject.getString("USERNAME");
                                        email = jsonObject.getString("EMAIL");
                                        fullname = jsonObject.getString("FULLNAME");
                                        apikey = jsonObject.getString("apikey");
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("logged", "true");
                                        editor.putString("USERNAME",username);
                                        editor.putString("EMAIL",email);
                                        editor.putString("FULLNAME",fullname);
                                        editor.putString("apikey",apikey);
                                        editor.apply();
                                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else{
                                        textViewError.setText(message);
                                        textViewError.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("EMAIL", email);
                        paramV.put("PASSWORD", password);

                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });


    }

}