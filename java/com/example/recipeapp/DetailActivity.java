package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import io.grpc.Context;

public class DetailActivity extends AppCompatActivity {

    TextView foodTitle, foodDescription, cookingTime;
    ImageView foodImage;
    String key = "";
    String imageUrl = "";

    @Override
    //Creating the Detail view
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Create a variable to store fields specified ID in View.
        // Setting value in these variable will reflect in the detail.xml.Works like field symbol.
        foodTitle = (TextView)findViewById(R.id.txtTitle);
        foodDescription = (TextView)findViewById(R.id.txtDescription);
        cookingTime = (TextView)findViewById(R.id.txtTime);
        foodImage = (ImageView)findViewById(R.id.ivImage2);

        //Description can be long so set scrolling
        foodDescription.setMovementMethod(new ScrollingMovementMethod());

        //Get intent that are set from My Adapter
        Bundle mBundle  = getIntent().getExtras();

        if(mBundle!=null){
            //Transfer data from mBundle to variables that will reflect to detail.xml
            foodDescription.setText(mBundle.getString("Description"));
            foodTitle.setText(mBundle.getString("RecipeName"));
            cookingTime.setText(mBundle.getString("CookingTime"));
            key = mBundle.getString("keyValue");
            imageUrl = mBundle.getString("Image");
//            foodImage.setImageResource(mBundle.getInt("Image"));

            //Glide is a way to display image
            Glide.with(this).load(mBundle.getString("Image")).into(foodImage);
        }
    }

    public void btnDeleteRecipe(View view) {
        //Get all images data from Storage called "Recipe"
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipe");
        //Get instance of Firebase
        FirebaseStorage storage = FirebaseStorage.getInstance();
        //Get reference for the image to be deleted
        StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);

        //Delete the image in the firebase based on its imageURL
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Delete the row of the image that was deleted above
                reference.child(key).removeValue();
                Toast.makeText(DetailActivity.this, "Recipe deleted", Toast.LENGTH_SHORT).show();
                //Go back to Main screen by starting MainActivity.class
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //Finish() method will destroy the current activity. It clears the activity from the.current stack.
                finish();
            }
        });
    }


    public void btnUpdateRecipe(View view) {
        //When clicking the UPDATE button, start an intent which is the UpdateRecipeActivity class.
        //The fields that are put below will show in the bundle in DetailActivity class.
        startActivity(new Intent(getApplicationContext(), UpdateRecipeActivity.class)
        .putExtra("recipeNameKey", foodTitle.getText().toString())
        .putExtra("descriptionKey", foodDescription.getText().toString())
        .putExtra("timeKey", cookingTime.getText().toString())
        .putExtra("oldImageURL", imageUrl)
        .putExtra("key",key)
         );
    }
}