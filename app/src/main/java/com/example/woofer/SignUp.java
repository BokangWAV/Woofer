package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    TextView textViewLogin, textViewError;
    Button SignUp;
    TextInputEditText TextInputEditTextFullname, TextInputEditTextEmail, TextInputEditTextUsername, TextInputEditTextPassword;
    String fullname, email, username, password;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextInputEditTextFullname = findViewById(R.id.fullname);
        TextInputEditTextEmail = findViewById(R.id.email);
        TextInputEditTextUsername = findViewById(R.id.SignUpUsername);
        TextInputEditTextPassword = findViewById(R.id.SignUpPassword);

        textViewLogin = findViewById(R.id.loginText);
        textViewError = findViewById(R.id.SignUpError);
        progressBar = findViewById(R.id.progress);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        SignUp = findViewById(R.id.buttonSignUp);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textViewError.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                fullname = String.valueOf(TextInputEditTextFullname.getText());
                email = String.valueOf(TextInputEditTextEmail.getText());
                username = String.valueOf(TextInputEditTextUsername.getText());
                password = String.valueOf(TextInputEditTextPassword.getText());

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="https://lamp.ms.wits.ac.za/home/s2494554/register.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);
                                if(response.equals("SignUp successful")){
                                    Toast.makeText(getApplicationContext(), "SignUp successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplication(), Login.class);
                                    startActivity(intent);
                                    finish();
                                } else{
                                    textViewError.setText(response);
                                    textViewError.setVisibility(View.VISIBLE);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textViewError.setText("Username Already Exists");
                        textViewError.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }){
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("FULLNAME", fullname);
                        paramV.put("EMAIL", email);
                        paramV.put("USERNAME", username);
                        paramV.put("PASSWORD", password);

                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

    }
}