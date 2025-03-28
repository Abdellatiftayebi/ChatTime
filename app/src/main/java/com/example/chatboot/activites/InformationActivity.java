package com.example.chatboot.activites;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chatboot.R;
import com.example.chatboot.databinding.ActivityInformationBinding;
import com.example.chatboot.utilites.Constants;
import com.example.chatboot.utilites.PerferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class InformationActivity extends AppCompatActivity {
private ActivityInformationBinding binding;
    public PerferenceManager perferenceManager;
    private FirebaseFirestore database;
    private String encodedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding=ActivityInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            perferenceManager = new PerferenceManager(getApplicationContext());
            database = FirebaseFirestore.getInstance();
            loadUserDetaille();
            getToken();

            binding.imageRetour.setOnClickListener(s ->{
                Intent intent=new Intent(InformationActivity.this,UserActivity.class);
                startActivity(intent);
            });

            binding.imageChange.setOnClickListener(c->{
                Intent intent =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            });

            binding.buttedit.setOnClickListener(n ->{
                binding.textUsername.setVisibility(View.INVISIBLE);
                binding.EditNmae.setVisibility(View.VISIBLE);
                binding.EditNmae.setText(perferenceManager.getString(Constants.KEY_NAME));
            });

            binding.logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Voulez-vous déconnecter ?");


                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sigOut();
                            Toast.makeText(InformationActivity.this, "Log Out", Toast.LENGTH_SHORT).show();
                        }
                    });


                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });


                    builder.show();
                }
            });


            binding.buttonSauvegarder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(binding.EditNmae!=null) {
                            promptUpdateName();
                        }
                        if(encodedImage!=null) {
                            updateProfileImage();
                        }
                        Toast.makeText(InformationActivity.this, "Modification bien enregistrer", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e){
                        Toast.makeText(InformationActivity.this, "error to change", Toast.LENGTH_SHORT).show();
                    }

                }
            });












            return insets;
        });
    }
    private  void loadUserDetaille(){
        binding.textUsername.setText(perferenceManager.getString(Constants.KEY_NAME));
        byte [] bytes= Base64.decode(perferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
        binding.emailtext.setText(perferenceManager.getString(Constants.KEY_EMAIL));

    }
            private void sigOut(){
                DocumentReference documentReference=database.collection(Constants.KEY_COLLECTION_USERS).document(
                        perferenceManager.getString(Constants.KEY_USER_ID)
                );
                HashMap<String,Object> updates =new HashMap<>();
                updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
                documentReference.update(updates)
                        .addOnSuccessListener(unused -> {
                            perferenceManager.claer();
                            startActivity(new Intent(InformationActivity.this, SignInActivity.class));
                        })
                        .addOnFailureListener(e ->showToast("impossible de sortie"));
            }

    private void updateToken(String token){
        DocumentReference documentReference=
                database.collection(Constants.KEY_COLLECTION_USERS).
                        document(perferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.update(Constants.KEY_FCM_TOKEN,token)

                .addOnFailureListener(e ->showToast("Unable to Update"));
    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }




    private void updateUserName(String newName) {
        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(perferenceManager.getString(Constants.KEY_USER_ID))
                .update(Constants.KEY_NAME, newName)
                .addOnSuccessListener(unused -> {
                    perferenceManager.putString(Constants.KEY_NAME, newName);
                    binding.textUsername.setText(newName);
                    showToast("Nom mis à jour avec succès !");
                })
                .addOnFailureListener(e -> showToast("Échec de la mise à jour du nom."));
    }
    private void promptUpdateName(){
        String name =binding.EditNmae.getText().toString().trim();
        if(!name.isEmpty()){
            updateUserName(name);
        }
        else{
            showToast("Le nom ne peut pas être vide.");
        }
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
                            encodedImage =encodeImage(bitmap);

                        } catch (FileNotFoundException E) {
                            E.printStackTrace();
                        }
                    }
                }
            }
    );

    private void updateProfileImage(){
        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(perferenceManager.getString(Constants.KEY_USER_ID))
                .update(Constants.KEY_IMAGE,encodedImage)
                .addOnSuccessListener(unused -> {
                    perferenceManager.putString(Constants.KEY_IMAGE,encodedImage);
                    showToast("Image mise à jour avec succès !");
                })
                .addOnFailureListener(e -> showToast("Échec de la mise à jour de l'image."));
    }
}