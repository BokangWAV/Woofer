package com.example.woofer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddFragment extends Fragment {
    EditText EditPost;
    String post, posttime;
    ProgressDialog pd;
    SharedPreferences sharedPreferences;
    Button Upload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add, container, false);

        EditPost = (EditText) rootView.findViewById(R.id.postText);
        Upload = (Button) rootView.findViewById(R.id.upload);
        /** "pd" is a progress dialog that will show up when the user clicks upload
         * */
        pd = new ProgressDialog(getContext());
        pd.setCanceledOnTouchOutside(false);

        sharedPreferences = this.requireActivity().getSharedPreferences("Woofer", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("logged", "false").equals("false")){
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }
            Upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pd.setMessage("Uploading Status Update");
                    pd.show();
                    post = "" + EditPost.getText().toString().trim();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                    String currentDateandTime = sdf.format(new Date());
                    if (post.isEmpty()) {
                        Toast.makeText(getActivity().getApplicationContext(), "Post Cannot Be Empty", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    } else {
                        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                        String url = "https://lamp.ms.wits.ac.za/home/s2494554/post.php";
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new com.android.volley.Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("Post successful")) {
                                            Toast.makeText(getActivity().getApplicationContext(), "Post successful", Toast.LENGTH_SHORT).show();
                                            loadFragment(new HomeFragment());
                                            pd.dismiss();
                                        } else {
                                            Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                                            pd.dismiss();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                pd.dismiss();
                            }
                        }) {
                            protected Map<String, String> getParams() {
                                Map<String, String> paramV = new HashMap<>();
                                paramV.put("USERNAME", sharedPreferences.getString("USERNAME", ""));
                                paramV.put("TIME",currentDateandTime);
                                paramV.put("POST", post);

                                return paramV;
                            }
                        };
                        queue.add(stringRequest);
                    }
                }
            });

        return rootView;
    }

    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}
