package com.example.chatboot.adapers;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatboot.databinding.ItemMessageBinding;
import com.example.chatboot.databinding.ItemmessageReceivedBinding;
import com.example.chatboot.models.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<ChatMessage> Messagechat;
    private Bitmap bitmap;
    private  final  String sendId;

    private static final int VIEW_TYPE_SENT =1;
    private static final int VIEW_TYPE_RECEIVED =2;

    public ChatAdapter(Bitmap bitmap, String sendId,List<ChatMessage>list) {
        this.bitmap = bitmap;
        this.sendId = sendId;
        this.Messagechat=list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_TYPE_SENT) {
            ItemMessageBinding binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ChatViewHolder(binding);
        }
        else{
            ItemmessageReceivedBinding binding = ItemmessageReceivedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ReceivedMessage(binding)  ;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
      if(getItemViewType(position)==VIEW_TYPE_SENT){
          ((ChatViewHolder)holder).setData(Messagechat.get(position));
      }
      else{
          ((ReceivedMessage)holder).setData(Messagechat.get(position),bitmap);
      }
    }

    @Override
    public int getItemCount() {
        return Messagechat.size();
    }
    public int getItemViewType(int position){
        if(Messagechat.get(position).senId.equals(sendId)){
            return VIEW_TYPE_SENT;
        }
        else{
            return VIEW_TYPE_RECEIVED;
        }
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{
       private ItemMessageBinding binding;
        public ChatViewHolder(ItemMessageBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        void setData(ChatMessage chatMessage){
            binding.texMessagesend.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }
    public class ReceivedMessage extends RecyclerView.ViewHolder{
       private final ItemmessageReceivedBinding binding;
        public ReceivedMessage(ItemmessageReceivedBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
        void setData(ChatMessage chatMessage,Bitmap recived){
            binding.texMessagesend.setText(chatMessage.message);
            binding.textDateTime.setText(chatMessage.dateTime);
            binding.imageProfile.setImageBitmap(recived);
        }
    }
}
