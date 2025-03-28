package com.example.chatboot.activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatboot.R;
import com.example.chatboot.databinding.ActivitySignUpBinding;
import com.example.chatboot.utilites.Constants;
import com.example.chatboot.utilites.PerferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
     private ActivitySignUpBinding binding;
     private String encodedImage;
     private PerferenceManager perferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding =ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            perferenceManager=new PerferenceManager(getApplicationContext());


            binding.textSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
                }
            });
            binding.buttonsignUp.setOnClickListener(n ->{
                if(isvalidSigUp()){
                    signeUp();
                }
            });
            binding.layoutImage.setOnClickListener(c->{
                Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);


            });


            return insets;
        });
    }
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void signeUp(){

        FirebaseFirestore database =FirebaseFirestore.getInstance();
        HashMap<String,Object> user =new HashMap<>();
        user.put(Constants.KEY_NAME,binding.inputUsername.getText().toString());
        user.put(Constants.KEY_EMAIL,binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD,binding.inputPassword.getText().toString());
        user.put(Constants.KEY_IMAGE,encodedImage);
        database.collection(Constants.KEY_COLLECTION_USERS).add(user)
                .addOnSuccessListener(documentReference -> {
                    perferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                    perferenceManager.putString(Constants.KEY_USER_ID,documentReference.getId());
                    perferenceManager.putString(Constants.KEY_NAME,binding.inputUsername.getText().toString());
                    perferenceManager.putString(Constants.KEY_IMAGE,encodedImage);
                    perferenceManager.putString(Constants.KEY_EMAIL,binding.inputEmail.getText().toString());
                    Intent intent =new Intent(SignUpActivity.this, UserActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    Toast.makeText(this, "Le compte a été créé avec succès.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(exception ->{
                    Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                });


    }
    private String encodeImage(Bitmap bitmap){
        int previewWidth=150;
        int previewHight=bitmap.getHeight() * previewWidth/bitmap.getWidth();
        Bitmap previeBitmap =Bitmap.createScaledBitmap(bitmap, previewWidth,previewHight,false);
        ByteArrayOutputStream byteArrayOutputStream =new ByteArrayOutputStream();
        previeBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes =byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);

    }
    private  final ActivityResultLauncher<Intent> pickImage= registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                if(result.getResultCode()==RESULT_OK){
                    if(result.getData()!=null) {
                        Uri imageUri = result.getData().getData();

                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
                            encodedImage =encodeImage(bitmap);

                        } catch (FileNotFoundException E) {
                             E.printStackTrace();
                        }
                    }
                }
            }
    );


    private  boolean isvalidSigUp(){
        if(encodedImage==null){
            showToast("Select profile image");
            return false;
        } else if (binding.inputUsername.getText().toString().trim().isEmpty()) {
            showToast("Enter Username ");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter valid email ");
            return false;
        }
        else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password ");
            return false;
        }
        else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("confirm your password ");
            return false;
        } else if (!binding.inputConfirmPassword.getText().toString().equals(binding.inputPassword.getText().toString())) {
            showToast("Password and confirm password must be same ");
            return false;
        }
        else{
            return true;
        }

    }


}