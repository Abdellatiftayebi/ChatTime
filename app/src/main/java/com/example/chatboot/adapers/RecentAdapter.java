package com.example.chatboot.adapers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatboot.databinding.ItemUserBinding;
import com.example.chatboot.models.ChatMessage;
import com.example.chatboot.models.Users;

import java.util.List;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ConversationViewHolder> {

private final List<ChatMessage> chatMessageList;

    public RecentAdapter(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }

    @NonNull
    @Override
    public RecentAdapter.ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding =ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ConversationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentAdapter.ConversationViewHolder holder, int position) {
           holder.SetData(chatMessageList.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder {
        ItemUserBinding binding;
        public ConversationViewHolder(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        public void SetData(ChatMessage chatMessage){
            binding.textName.setText(chatMessage.conversionName);
            binding.imageProfile.setImageBitmap(getConversationImage(chatMessage.conversionImage));
        }
    }
    private Bitmap getConversationImage(String encodedImage){
        byte[] bytes= Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
}
