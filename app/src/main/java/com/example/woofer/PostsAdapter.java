package com.example.woofer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.allposts>
{
    Context context;
    List<Model>modelList;

    public PostsAdapter(Context context, List<Model> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public allposts onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        We are linking the design we created to a view, card design, parent object, false
        View view = LayoutInflater.from(context).inflate(R.layout.row_post,parent,false);
        allposts allposts=new allposts(view);
        return allposts;
    }

    @Override
    public void onBindViewHolder(@NonNull allposts holder, int position) {

        Model model  = modelList.get(position);
        holder.userName.setText(model.getUserName());
        holder.postTime.setText(model.getPostTime());
        holder.postText.setText(model.getPostText());
        //holder.numLikes.setText(model.getNumLikes());
        //holder.numComments.setText(model.getNumComments());

    }

    @Override
    public int getItemCount() {
        return this.modelList.size();
    }

    public static class allposts extends RecyclerView.ViewHolder{
        TextView userName, postTime, postText, numLikes, numComments;
        ImageButton likeButton;
        public allposts(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.HomeUsername);
            postTime=itemView.findViewById(R.id.PostTime);
            postText=itemView.findViewById(R.id.userPost);
            //numLikes=itemView.findViewById(R.id.liketext);
            //likeButton = itemView.findViewById(R.id.likebutton);
            //numComments=itemView.findViewById(R.id.commenttext);

        }
    }
}
