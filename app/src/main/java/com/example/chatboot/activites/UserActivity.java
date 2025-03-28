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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatboot.R;
import com.example.chatboot.adapers.UserAdapter;
import com.example.chatboot.adapers.UserListener;
import com.example.chatboot.databinding.ActivityUserBinding;
import com.example.chatboot.models.Users;
import com.example.chatboot.utilites.Constants;
import com.example.chatboot.utilites.PerferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements UserListener {
   private ActivityUserBinding binding;
   private UserAdapter userAdapter;
   private PerferenceManager perferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            perferenceManager =new PerferenceManager(getApplicationContext());

            binding.recycleview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            getUsers();

           binding.imageSigneOut.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent =new Intent(UserActivity.this, InformationActivity.class);
                   startActivity(intent);
                   finish();
               }
           });






            return insets;
        });
    }
    private void loading(Boolean isloading){
        if(isloading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
    private void getUsers(){
        loading(true);
        FirebaseFirestore database =FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    loading(false);
                    String currentUserId=perferenceManager.getString(Constants.KEY_USER_ID);
                    if(task.isSuccessful() && task.getResult()!=null){
                        List<Users> usersList =new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot: task.getResult()){
                            if(currentUserId.equals(queryDocumentSnapshot.getId())){
                                binding.textName.setText(perferenceManager.getString(Constants.KEY_NAME));
                                byte [] bytes= Base64.decode(perferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                binding.imageProfile.setImageBitmap(bitmap);
                                continue;
                            }
                            Users user =new Users();
                            user.name=queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.image=queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token=queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id=queryDocumentSnapshot.getId();
                            usersList.add(user);
                        }
                        if(usersList.size()>0){
                            userAdapter =new UserAdapter(usersList,this);
                            binding.recycleview.setAdapter(userAdapter);
                            binding.recycleview.setVisibility(View.VISIBLE);
                        }
                    }

                });
    }

    @Override
    public void onUserClicked(Users user) {
        Intent intent =new Intent(UserActivity.this,ChatActivity.class);
        intent.putExtra(Constants.KEY_USER,user);
        startActivity(intent);
        finish();
    }
}