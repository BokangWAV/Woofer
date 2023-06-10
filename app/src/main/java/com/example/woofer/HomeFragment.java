package com.example.woofer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//public class HomeFragment extends Fragment {
//
//    private PostsAdapter recycleAdapter;
//    private RecyclerView recyclerView;
//
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
//
//       return rootView;
//}
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        List<Model> allPosts = new ArrayList<>();
//        allPosts.add(new Model("Blessing","15:00","Hello My name is blah blah","5",
//                "5"));
////        getPostsFromDB();
//
//        recyclerView = recyclerView.findViewById(R.id.recycle);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recycleAdapter = new PostsAdapter(getContext(),allPosts);
//        recyclerView.setAdapter(recycleAdapter);
//    }
//
//
//
//
//}

// -------------

//package com.example.woofer;

        import android.os.Bundle;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
        import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private PostsAdapter recycleAdapter;
    private RecyclerView recyclerView;
    OkHttpClient client = new OkHttpClient();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Model> allPosts = new ArrayList<>();
//        getPostsFromDB();

        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2494554/display.php";

        request(url, "",new Callback() {
            @Override
            public void onFailure(Call call, IOException e){

            }

            @Override
            public void onResponse(Call call, Response response) throws  IOException {
                if(response.isSuccessful()){
                    final String responseStr = response.body().string();

                    if(getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                JSONArray posts = null;
                                try {
                                    posts = processJSON(responseStr);
                                    for(int i=0; i<posts.length(); i++){
                                        JSONObject item=posts.getJSONObject(i);
                                        String username = item.getString("USERNAME");
                                        String posttext = item.getString("POST");
                                        String posttime = item.getString("TIME");
                                        Random randomNum = new Random();
                                        Integer numberofLikes = randomNum.nextInt(100);
//                                        System.out.println(username);
                                        allPosts.add(
                                                new Model(username,posttime,posttext,numberofLikes.toString())
                                        );
                                        /** Creates A and displays a post for every post in the "posts" database
                                         * */
                                        recyclerView = view.findViewById(R.id.recycle);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        recycleAdapter = new PostsAdapter(getContext(),allPosts);
                                        recyclerView.setAdapter(recycleAdapter);
                                        recycleAdapter.notifyDataSetChanged();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                }
                else {
                    // request not successful
                }
            }
        });


        //recyclerView = view.findViewById(R.id.recycle);
       // recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recycleAdapter = new PostsAdapter(getContext(),allPosts);
       // recyclerView.setAdapter(recycleAdapter);
       // recycleAdapter.notifyDataSetChanged();
    }

    public  static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    Call request(String url, String json, Callback callback){
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
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

