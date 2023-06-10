package com.example.woofer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommonFriendsAdapter extends  RecyclerView.Adapter<CommonFriendsAdapter.commonfriends> {

    Context context;
    List<ModelCommon> modelList;

    public CommonFriendsAdapter(Context context, List<ModelCommon> modelList){
        this.context = context;
        this.modelList = modelList;

    }

    @NonNull
    @Override
    public commonfriends onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //        We are linking the design we created to a view, card design, parent object, false
        View view = LayoutInflater.from(context).inflate(R.layout.common_friends,parent,false);
        commonfriends commonfriends = new commonfriends(view);

        return commonfriends;
    }

    @Override
    public void onBindViewHolder(@NonNull commonfriends holder, int position) {
        ModelCommon model = modelList.get(position);
        holder.userName.setText(model.getUserName());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class commonfriends extends RecyclerView.ViewHolder{
        TextView userName;
        public commonfriends(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userFriends);
        }
    }
}
