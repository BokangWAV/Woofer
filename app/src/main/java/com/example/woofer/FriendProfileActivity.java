package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FriendProfileActivity extends AppCompatActivity {
    TextView userNameView;
    TextView fullnameView, numFriends;

    LinearLayout layoutList;
    ImageButton addFriendButton, backButton, unfollow;

    private CommonFriendsAdapter recycleAdapter;
    private RecyclerView recyclerView;
    SharedPreferences sharedPreferences;

    ArrayList<String> friendsOfCurrentUser = new ArrayList<String>();
    FriendProfileModel friend;

    OkHttpClient client = new OkHttpClient();
    String url = "https://lamp.ms.wits.ac.za/home/s2494554/common.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        layoutList = findViewById(R.id.friends_layout_list);
        unfollow = findViewById(R.id.removeFriendBtn);

//  Todo: The below code is for getting info from the previous page e.g username and fullanme
        Intent friendPage = getIntent();
        String viewedProfileUsername = friendPage.getStringExtra("username");
        String viewedProfileFullname = friendPage.getStringExtra("fullname");

//  Todo: The code below is for getting the current user
        sharedPreferences = this.getSharedPreferences("Woofer", Context.MODE_PRIVATE);
        String currentUser = sharedPreferences.getString("USERNAME", "");

// Todo : The code below is for getting the list of friends of the CURRENT USER

        request(url, currentUser,"", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseStr = response.body().string();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONArray posts = null;
                            ArrayList<String> friendsOnCurrentProfile = new ArrayList<>();
                            try {
                                posts = processJSON(responseStr);
                                for(int n=0;n<posts.length();n++){
                                    JSONObject item = posts.getJSONObject(n);

                                    friendsOnCurrentProfile.add(item.getString("FRIEND"));
                                }
                                friendsOfCurrentUser = new ArrayList<>();
                                friendsOfCurrentUser=friendsOnCurrentProfile;


                                /** Creates A and displays a post for every post in the "posts" database
                                 * */


                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }


            }
        });

//  Todo: Must implement code for getting friends of the PROFILE BEING VIEWED

        request(url, viewedProfileUsername,"", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseStr = response.body().string();


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONArray posts = null;
                            try {
                                posts = processJSON(responseStr);

//                                    posts = processJSON(responseStr);
                                ArrayList<String> friendsOnCurrentProfile = new ArrayList<>();
                                for(int n=0;n<posts.length();n++){
                                    JSONObject item = posts.getJSONObject(n);

                                    friendsOnCurrentProfile.add(item.getString("FRIEND"));

                                }

                                if (friendsOnCurrentProfile.isEmpty()) {
                                    System.out.println("Current profile has no friends");
                                } else {
                                    friend = new FriendProfileModel(viewedProfileUsername, viewedProfileFullname, currentUser, friendsOnCurrentProfile, friendsOfCurrentUser);
                                    numFriends.setText(friend.getNumFriends().toString());
                                    ArrayList<String> commonFriends = friend.getCommonFriends();

                                    if (commonFriends.isEmpty()) {
                                        addFriendTextView("No Common Friends");
                                        TextView title = findViewById(R.id.title);
                                        title.setVisibility(View.INVISIBLE);
                                    } else {
                                        for (int i = 0; i < commonFriends.size(); i++) {
                                                addFriendTextView(commonFriends.get(i));
                                        }
                                    }
                                }

//                                Todo displaying the follow buttons
                                if(friendsOfCurrentUser.contains(viewedProfileUsername)){
                                    unfollow.setVisibility((View.VISIBLE));
                                    addFriendButton.setVisibility(View.INVISIBLE);
                                } else{
                                    unfollow.setVisibility(View.INVISIBLE);
                                }


//                                System.out.println("Below is the names of the profile being viewed, i.e. his list of friends");
//
//                                System.out.println(friendsOnCurrentProfile);
//                                List profileFriendsList = null;
//
//                                if(friendsOnCurrentProfile.isEmpty()){
//                                    System.out.println("Current profile has no friends");
//                                }else{
//                                    friend = new FriendProfileModel(viewedProfileUsername, viewedProfileFullname, currentUser, friendsOnCurrentProfile,friendsOfCurrentUser);
//                                }
//
////                                System.out.println(friend.getCommonFriends().);
//                                ArrayList<String> commonFriends = friend.getCommonFriends();
//                                 for(int i=0;i<commonFriends.size();i++){
//                                        System.out.println(commonFriends.get(i));
//                                    }

//                                if(friend.getCommonFriends().isEmpty()){
//                                    addFriendTextView("No Common Friends");
//                                }else{
//                                    for(int i=0;i<friend.getCommonFriends().size();i++){
//                                        addFriendTextView(friend.getCommonFriends().get(i));
//                                    }
//
//                                }






                                /** Creates A and displays a post for every post in the "posts" database
                                 * */


                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }


            }
        });




//  Todo: identifying the IDs of the components on the canvas.
        userNameView = (TextView) findViewById(R.id.friendProfileFullname);
        fullnameView = (TextView) findViewById(R.id.friendProfileUsername);
        numFriends = (TextView) findViewById(R.id.friendsNumber);
        addFriendButton = (ImageButton) findViewById(R.id.addFriendBtn);

//  Todo: Below code is for adding a friend to the friends list
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://lamp.ms.wits.ac.za/home/s2494554/friend.php";
                StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("Friend added successfully")) {

                                    Toast.makeText(getApplicationContext(), "Friend Added Successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(FriendProfileActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
//                    Todo : lol Whats going on here hehe
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("USERNAME", sharedPreferences.getString("USERNAME", ""));
                        paramV.put("FRIEND", viewedProfileUsername);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }

        });

        unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://lamp.ms.wits.ac.za/home/s2494554/unfollow.php";
                StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, url,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("Unfollow")) {
                                    Toast.makeText(getApplicationContext(), "Unfollowed", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(FriendProfileActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("USERNAME", sharedPreferences.getString("USERNAME", ""));
                        paramV.put("FRIEND", viewedProfileUsername);
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

//  Todo : The below code is for displaying the username of the someone's profile.
        userNameView.setText(viewedProfileUsername);
        fullnameView.setText(viewedProfileFullname);
// Todo : The below code is the back button
        backButton = (ImageButton) findViewById(R.id.ButtonLogout);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
// Todo: the logout button ends here

//        Todo: adding friends to the linear layout

    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    Call request(String url, String username, String json, Callback callback){
        RequestBody formbody = new FormBody.Builder()
                .add("USERNAME",username)
                .build();
        Request request = new Request.Builder()
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


    public void addFriendTextView(String newFriendName){
        TextView friendTextview = new TextView(this);
        friendTextview.setTextSize(25);
        friendTextview.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        friendTextview.setText(newFriendName);
        layoutList.addView(friendTextview);
    }

    private void changeFragment(Fragment newFragment, FragmentsAvailable newFragmentType, boolean withoutAnimation) {


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        try {
            getSupportFragmentManager().popBackStackImmediate(newFragmentType.toString(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (java.lang.IllegalStateException e) {

        }

    }


}