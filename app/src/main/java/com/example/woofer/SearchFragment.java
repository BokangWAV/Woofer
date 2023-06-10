package com.example.woofer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchFragment extends Fragment {
    private UsersAdapter recycleAdapter;
    private RecyclerView recyclerView;

    private CardView cardView;

    private SearchView searchView;
    OkHttpClient client = new OkHttpClient();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SearchView search;

    public SearchFragment() {
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
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);



        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<ModelUser> allUsers = new ArrayList<>();
//        getPostsFromDB();

        OkHttpClient client = new OkHttpClient();
        String url = "https://lamp.ms.wits.ac.za/home/s2494554/search.php";
        String urlusers = "https://lamp.ms.wits.ac.za/home/s2494554/users.php";
        searchView = (SearchView)  getActivity().findViewById(R.id.searchUsers);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!TextUtils.isEmpty(query.trim())){
                    request(url, query,"",new Callback() {
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
                                            System.out.println(responseStr);
                                            JSONArray posts = null;
                                            try {
                                                posts = processJSON(responseStr);
                                                for(int i=0; i<posts.length(); i++){
                                                    JSONObject item=posts.getJSONObject(i);
                                                    String username = item.getString("USERNAME");
                                                    String fullname = item.getString("FULLNAME");
                                                    System.out.println(username);
                                                    allUsers.add(
                                                            new ModelUser(username,fullname)
                                                    );
                                                    /** Creates A and displays a post for every post in the "posts" database
                                                     * */
                                                    recyclerView = view.findViewById(R.id.searchrecycle);
                                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                                    recycleAdapter = new UsersAdapter(getContext(),allUsers);
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
                    allUsers.clear();
                }
                else{
                    allUsers.clear();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText.trim())){
                    request(url, newText,"",new Callback() {
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
                                            System.out.println(responseStr);
                                            JSONArray posts = null;
                                            try {
                                                posts = processJSON(responseStr);
                                                for(int i=0; i<posts.length(); i++){
                                                    JSONObject item=posts.getJSONObject(i);
                                                    String username = item.getString("USERNAME");
                                                    String fullname = item.getString("FULLNAME");
                                                    System.out.println(username);
                                                    allUsers.add(
                                                            new ModelUser(username,fullname)
                                                    );
                                                    /** Creates A and displays a post for every post in the "posts" database
                                                     * */
                                                    recyclerView = view.findViewById(R.id.searchrecycle);
                                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                                    recycleAdapter = new UsersAdapter(getContext(),allUsers);
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
                    allUsers.clear();
                }
                else{
                    allUsers.clear();
                }

                return false;
            }
        });




        //recyclerView = view.findViewById(R.id.recycle);
        // recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //recycleAdapter = new PostsAdapter(getContext(),allPosts);
        // recyclerView.setAdapter(recycleAdapter);
        // recycleAdapter.notifyDataSetChanged();
    }

    public  static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
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
}