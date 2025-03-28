package com.example.chatboot.adapers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatboot.activites.ChatActivity;
import com.example.chatboot.databinding.ItemUserBinding;
import com.example.chatboot.models.Users;
import com.example.chatboot.utilites.Constants;
import com.google.firebase.firestore.auth.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
   private final List<Users> users;
   private Context context;
   private final UserListener userListener;

    public UserAdapter(List<Users> users,UserListener userListener) {
        this.users = users;
        this.userListener=userListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding =ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
      Users user =users.get(position);
      holder.SetUsers(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
       ItemUserBinding binding;
        public UserViewHolder(ItemUserBinding itemUserBinding){
            super(itemUserBinding.getRoot());
            this.binding=itemUserBinding;
        }
        public void SetUsers(Users user){
         binding.textName.setText(user.name);
         binding.imageProfile.setImageBitmap(getUserImage(user.image));
         binding.getRoot().setOnClickListener(c->userListener.onUserClicked(user));
        }
    }

    private Bitmap getUserImage(String encodedImage){
        byte[] bytes= Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}
