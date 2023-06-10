package com.example.woofer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class ProfileFragment extends Fragment {
    TextView textViewFname, textViewUname, textViewEmail;
    SharedPreferences sharedPreferences;
    TextView numOfFollowersView;
    OkHttpClient client = new OkHttpClient();
    Button Logout;
    ArrayList<String> friendsOfCurrentUser = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewFname = (TextView) rootView.findViewById(R.id.friendProfileFullname);
        textViewUname = (TextView) rootView.findViewById(R.id.friendProfileUsername);
        textViewEmail = (TextView) rootView.findViewById(R.id.pEmail);
        numOfFollowersView = rootView.findViewById(R.id.followersNumber);
        Logout = (Button) rootView.findViewById(R.id.ButtonLogout);
        String url = "https://lamp.ms.wits.ac.za/home/s2494554/common.php";

        sharedPreferences = this.requireActivity().getSharedPreferences("Woofer", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("logged", "false").equals("false")){
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }

        String currentUser = sharedPreferences.getString("USERNAME", "");

        textViewFname.setText(sharedPreferences.getString("FULLNAME", ""));
        textViewUname.setText(sharedPreferences.getString("USERNAME", ""));
        textViewEmail.setText(sharedPreferences.getString("EMAIL", ""));





        // Todo getting number of friends of the current user

        request(url, currentUser,"", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseStr = response.body().string();

                    if(getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                JSONArray posts = null;
                                ArrayList<String> friendsOnCurrentProfile = new ArrayList<>();
                                try {
                                    posts = processJSON(responseStr);
                                    for (int n = 0; n < posts.length(); n++) {
                                        JSONObject item = posts.getJSONObject(n);

                                        friendsOnCurrentProfile.add(item.getString("FRIEND"));
                                    }

                                    friendsOfCurrentUser = friendsOnCurrentProfile;
                                    numOfFollowersView.setText("" + friendsOfCurrentUser.size());


                                    /** Creates A and displays a post for every post in the "posts" database
                                     * */


                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                        });
                    }
                }


            }
        });

        //



        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                String url ="https://lamp.ms.wits.ac.za/home/s2494554/logout.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.equals("success")) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("logged", "");
                                    editor.putString("USERNAME", "");
                                    editor.putString("EMAIL", "");
                                    editor.putString("FULLNAME", "");
                                    editor.putString("apikey", "");
                                    editor.apply();
                                    Intent intent = new Intent(getActivity().getApplicationContext(), Login.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
                {
                    protected Map<String, String> getParams(){
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("EMAIL", sharedPreferences.getString("EMAIL", ""));
                        paramV.put("apikey", sharedPreferences.getString("apikey", ""));

                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

        return rootView;
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    Call request(String url, String username, String json, Callback callback){
        RequestBody formbody = new FormBody.Builder()
                .add("USERNAME",username)
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(formbody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public JSONArray processJSON(String json) throws JSONException {
        JSONArray all = new JSONArray(json);
        return all;
    }
}