package com.example.recipeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.text.DateFormat;
import java.util.Calendar;

import javax.xml.transform.Result;

public class Upload_Recipe extends AppCompatActivity {
    ImageView recipeImage;
    Uri uri;
    EditText txt_name,txt_description,txt_Time;
    String ImageURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__recipe);

        //Create a variable to store fields specified ID in View.
        // Setting value in these variable will reflect in the detail.xml.Works like field symbol.
        recipeImage = (ImageView)findViewById(R.id.iv_foodImage);
        txt_name = (EditText) findViewById(R.id.text_recipe_name);
        txt_description = (EditText) findViewById(R.id.text_description);
        txt_Time = (EditText) findViewById(R.id.text_time);

    }

    public void btnSelectImage(View view) {
        //Go to local photo folder to chhose images
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker,1);
    }

    @Override
    //Result of image picking
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            uri = data.getData();
            recipeImage.setImageURI(uri);

        }
        else Toast.makeText(this,"You haven't picked image", Toast.LENGTH_LONG);
    }

    public void uploadImage(){
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mAuth.signInWithEmailAndPassword("YourEmail", "YourPassword")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Upload_Recipe.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        //Get instance of Image Storage
        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference().child("RecipeImage")
                .child(uri.getLastPathSegment());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recipe Uploading....");
        progressDialog.show();

        //Upload the new image to RecipeImage storage
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete());
                Uri urlImage =uriTask.getResult();
                ImageURI = urlImage.toString();
                uploadRecipe();
                progressDialog.dismiss();
//                Toast.makeText(Upload_Recipe.this,"Image Uploaded", Toast.LENGTH_LONG);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });
    }

    public void btnUploadRecipe(View view) {
        uploadImage();
    }

    public void uploadRecipe() {

        FoodData foodData = new FoodData(
                txt_name.getText().toString(),
                txt_description.getText().toString(),
                txt_Time.getText().toString(),
                ImageURI
        );
        String myCurrentDatetime = DateFormat.getDateTimeInstance()
                        .format(Calendar.getInstance().getTime());
        FirebaseDatabase.getInstance().getReference("Recipe")
                .child(myCurrentDatetime).setValue(foodData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Upload_Recipe.this, "Recipe Uploaded", Toast.LENGTH_LONG);
                            //Go back to Main screen by starting MainActivity.class
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Upload_Recipe.this, e.getMessage().toString()
                                , Toast.LENGTH_LONG);
                    }
                });
    }

}