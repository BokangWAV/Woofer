package com.example.woofer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.allposts>
{
    Context context;
    List<ModelUser>modelList;

    public UsersAdapter(Context context, List<ModelUser> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public allposts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        We are linking the design we created to a view, card design, parent object, false
        View view = LayoutInflater.from(context).inflate(R.layout.search_users,parent,false);
        allposts allposts=new allposts(view);
        return allposts;
    }

    @Override
    public void onBindViewHolder(@NonNull allposts holder, int position) {

        ModelUser model  = modelList.get(position);
        holder.userName.setText(model.getUserName());
        holder.fullname.setText(model.getFullName());
        holder.cardView.setOnClickListener(view -> {
            Intent newPage = new Intent(holder.itemView.getContext(),FriendProfileActivity.class);
            newPage.putExtra("username",model.getUserName());
            newPage.putExtra("fullname",model.getFullName());
            holder.itemView.getContext().startActivity(newPage);
        });
        //holder.numComments.setText(model.getNumComments());

    }

    @Override
    public int getItemCount() {
        return this.modelList.size();
    }

    public static class allposts extends RecyclerView.ViewHolder{
        TextView userName, fullname, postText, numLikes, numComments;
        CardView cardView;
        ImageButton commentButton, likeButton;
        public allposts(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.userName);
            fullname=itemView.findViewById(R.id.searchName);
            cardView=itemView.findViewById(R.id.cardView);


        }
    }
}
