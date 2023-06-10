package com.example.woofer;

import android.content.Intent;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class FriendProfileModel {

    private String userName;
    private String fullName;
    private ArrayList<String> friends = new ArrayList<>();
    private ArrayList<String> commonFriends= new ArrayList<>();
    private String currentUser;

    private ArrayList<String> currentUsersFriends = new ArrayList<>();

    public FriendProfileModel(String userName, String fullName, String currentUser, ArrayList<String> profileFriendsList, ArrayList<String> currentUserFriends) {
        this.userName = userName;
        this.fullName = fullName;
        this.friends=profileFriendsList;
        this.currentUsersFriends = currentUserFriends;
        this.currentUser = currentUser;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

//    public void setFriends(List friends){
//        this.friends=friends;
//    }

    public Integer getNumFriends(){
        return this.friends.size();
    }



    public String getUserName() {
        return userName;
    }

    public List getFriends(){
        return friends;
    }

    public String getFullName() {
        return fullName;
    }

//    public void setCurrentUsersFriends(List currentUsersFriends) {
//        this.currentUsersFriends = currentUs
//    }

    public ArrayList<String> getCommonFriends(){

        calculateCommonFriends(this.currentUsersFriends);

        return this.commonFriends;
    }



//    private void calculateCommonFriends(@NonNull List currentUsersFriends){
//        for(int x=0;x<this.currentUsersFriends.size();x++){
//            for(int y=0;y<this.friends.size();y++){
//                if(this.currentUsersFriends.get(x).equals(this.friends.get(y))){
//                    this.commonFriends.add(this.friends.get(y));
//                }
//                break;
//            }
//        }
//    }
    private void calculateCommonFriends(@NonNull List<String> currentUsersFriends) {
        for (String currentUserFriend : currentUsersFriends) {
            for (String friend : this.friends) {
                if (currentUserFriend.equals(friend)) {
                    this.commonFriends.add(friend);
                    break;
                }
            }
        }

    }

}
