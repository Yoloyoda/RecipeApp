package com.example.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    List<FoodData> mFoodList;
    FoodData mFoodData;
    private DatabaseReference databaseReference;
    private ValueEventListener eventListener;
    ProgressDialog progressDialog;
    MyAdapter myAdapter;
    EditText txt_Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        txt_Search = (EditText)findViewById(R.id.txt_searchtext);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading items....");

        mFoodList = new ArrayList<>();

//        mFoodData = new FoodData("Chicken salad",
//                "Preparation time is 20 min. Cooking time is 1H","", String.valueOf(R.drawable.image1));
//        mFoodList.add(mFoodData);
//
//        mFoodData = new FoodData("Fried Tofu",
//                "So tasty","200JPY", String.valueOf(R.drawable.image2));
//        mFoodList.add(mFoodData);

        myAdapter = new MyAdapter(MainActivity.this, mFoodList);
        mRecyclerView.setAdapter(myAdapter);

        //Get instance of Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Recipe");

        progressDialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mFoodList.clear();
                //Loop all recipe in Firebase into mFoodList
                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                    FoodData foodData = itemSnapshot.getValue(FoodData.class);
                    foodData.setKey(itemSnapshot.getKey());  //Get key of firebase dataset, which is timestamp.
                    mFoodList.add(foodData);
                }
                //Update all the contents in FoodViewHolder by signaling the change
                myAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });

        txt_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            //After putting search keyword, apply filter by running filter method passing the search keyword
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    private void filter(String text ) {
        ArrayList<FoodData> filteredList= new ArrayList<>();
        for(FoodData item : mFoodList){
            if(item.getItemName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        //Filter recipe list by using filteredList
        myAdapter.filteredList(filteredList);
    }

    public void btn_uploadActivity(View view) {
        //Start Upload_Recipe class when upload button is clicked
        startActivity(new Intent(this,Upload_Recipe.class));
    }
}