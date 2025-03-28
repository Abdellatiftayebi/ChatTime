package com.example.chatboot.activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatboot.R;
import com.example.chatboot.adapers.ChatAdapter;
import com.example.chatboot.databinding.ActivityChatBinding;
import com.example.chatboot.models.ChatMessage;
import com.example.chatboot.models.Users;
import com.example.chatboot.utilites.Constants;
import com.example.chatboot.utilites.PerferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private Users recevied;
    private List<ChatMessage> chatMessageList;
    private ChatAdapter chatAdapter;
    private PerferenceManager perferenceManager;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            binding.imageRetour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChatActivity.this, UserActivity.class);
                    startActivity(intent);
                }
            });
            loadReceved();
            init();
            binding.layoutSend.setOnClickListener(c -> {
                sendMessage();
            });
            listeMessage();

            return insets;
        });
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            int count = chatMessageList.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.recivedId = documentChange.getDocument().getString(Constants.KEY_RECIVED_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDate(documentChange.getDocument().getDate(Constants.KEY_TIME));
                    chatMessage.dateObjet = documentChange.getDocument().getDate(Constants.KEY_TIME);
                    boolean isDuplicate = false;
                    for (ChatMessage existingMessage : chatMessageList) {
                        if (existingMessage.dateObjet.equals(chatMessage.dateObjet) &&
                                existingMessage.message.equals(chatMessage.message)) {
                            isDuplicate = true;
                            break;
                        }
                    }
                    if (!isDuplicate) {
                        chatMessageList.add(chatMessage);
                    }
                }
            }
            Collections.sort(chatMessageList, (obj1, obj2) -> obj1.dateObjet.compareTo(obj2.dateObjet));
            if (count == 0) {

             chatAdapter.notifyDataSetChanged();
            }
            else{
                chatAdapter.notifyItemRangeInserted(chatMessageList.size(),chatMessageList.size());
                binding.recyycleview.smoothScrollToPosition(chatMessageList.size()-1);
            }
            binding.recyycleview.setVisibility(View.VISIBLE);
        }
    };

    private  void listeMessage(){
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID,perferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECIVED_ID,recevied.id)
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID,recevied.id)
                .whereEqualTo(Constants.KEY_RECIVED_ID,perferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }
    private Bitmap getBitmap(String encodedImage){
        byte [] bytes= Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }
  private void sendMessage(){
      HashMap<String,Object> message=new HashMap<>();
      message.put(Constants.KEY_SENDER_ID,perferenceManager.getString(Constants.KEY_USER_ID));
      message.put(Constants.KEY_RECIVED_ID,recevied.id);
      message.put(Constants.KEY_MESSAGE,binding.inputMessage.getText().toString());
      message.put(Constants.KEY_TIME,new Date());
     database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
     binding.inputMessage.setText(null);

    }
    private  void init(){
        perferenceManager=new PerferenceManager(getApplicationContext());
        chatMessageList=new ArrayList<>();
        chatAdapter =new ChatAdapter(getBitmap(recevied.image),perferenceManager.getString(Constants.KEY_USER_ID),chatMessageList);
      binding.recyycleview.setAdapter(chatAdapter);
      database =FirebaseFirestore.getInstance();
    }
    private void loadReceved(){
        recevied =(Users) getIntent().getSerializableExtra(Constants.KEY_USER);
        if (recevied == null) {
            finish();
            return;
        }
        binding.textName.setText(recevied.name);
        byte [] bytes= Base64.decode(recevied.image,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);


    }
    private  String getReadableDate(Date date){
  return  new SimpleDateFormat("MMMM dd, yyyy -hh:mm a", Locale.getDefault()).format(date);
    }
}