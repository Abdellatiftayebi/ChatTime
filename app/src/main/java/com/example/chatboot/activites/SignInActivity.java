 package com.example.chatboot.activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatboot.R;
import com.example.chatboot.databinding.ActivitySingleBinding;
import com.example.chatboot.utilites.Constants;
import com.example.chatboot.utilites.PerferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

 public class SignInActivity extends AppCompatActivity {
    private ActivitySingleBinding binding;
     PerferenceManager perferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivitySingleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            setListener();
            perferenceManager = new PerferenceManager(getApplicationContext());
            if(perferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
                Intent intent= new Intent(SignInActivity.this, UserActivity.class);
                startActivity(intent);
                finish();
            }
            return insets;
        });
    }
     private void showToast(String message){
         Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
     }
     private  boolean isvalidSigIn() {
         if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
             showToast("Enter valid email ");
             return false;
         } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
             showToast("Enter password ");
             return false;
         }
        else if (binding.inputEmail.getText().toString().trim().isEmpty()) {
         showToast("Enter email ");
         return false;
     }
         else{
             return true;
         }
     }
     private void signIn(){
         FirebaseFirestore database =FirebaseFirestore.getInstance();
          database.collection(Constants.KEY_COLLECTION_USERS)
                  .whereEqualTo(Constants.KEY_EMAIL,binding.inputEmail.getText().toString())
                  .whereEqualTo(Constants.KEY_PASSWORD,binding.inputPassword.getText().toString())
                  .get()
                  .addOnCompleteListener(task -> {
                      if(task.isSuccessful() && task.getResult()!=null
                      && task.getResult().getDocuments().size()>0){
                          DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                          perferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                          perferenceManager.putString(Constants.KEY_USER_ID,documentSnapshot.getId());
                          perferenceManager.putString(Constants.KEY_NAME,documentSnapshot.getString(Constants.KEY_NAME));
                          perferenceManager.putString(Constants.KEY_IMAGE,documentSnapshot.getString(Constants.KEY_IMAGE));
                         Intent intent= new Intent(SignInActivity.this, UserActivity.class);
                          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                          startActivity(intent);

                      }
                      else{
                          showToast("Ce Compte ne exist pas");
                      }
                  });
     }
   private void setListener(){
        binding.textcreateNewAccont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
            }
        });
        binding.buttonsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(isvalidSigIn()){
                signIn();
            }
            }
        });

   }
}