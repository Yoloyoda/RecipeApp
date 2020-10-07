package com.example.recipeapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<FoodViewHolder>{
    private Context mContext;
    private List<FoodData> myFoodList;
    private int lastPosition = -1;

    //Constructor
    public MyAdapter(Context mContext, List<FoodData> myFoodList) {
        this.mContext = mContext;
        this.myFoodList = myFoodList;
    }

    @NonNull
    @Override
    //onCreate is called for every card that is holding the recipe
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_item, parent,false);
        return new FoodViewHolder(mview); //This calls class FoodViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodViewHolder foodViewHolder, int position) {
        Glide.with(mContext).load(myFoodList.get(position).getItemImage()).into(foodViewHolder.imageView);

        //foodViewHolder.imageView.setImageResource(myFoodList.get(position).getItemImage());
        foodViewHolder.mTitle.setText(myFoodList.get(position).getItemName());
        foodViewHolder.mDescription.setText(myFoodList.get(position).getItemDescription());
        foodViewHolder.mTime.setText(myFoodList.get(position).getItemTime());

        //When clicking the card, start an intent which is the DetailActivity class.
        //The fields that are put below will show in the bundle in DetailActivity class.
        foodViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("Image",myFoodList.get(foodViewHolder.getAdapterPosition()).getItemImage());
                intent.putExtra("Description",myFoodList.get(foodViewHolder.getAdapterPosition()).getItemDescription());
                intent.putExtra("keyValue", myFoodList.get(foodViewHolder.getAdapterPosition()).getKey());
                intent.putExtra("RecipeName", myFoodList.get(foodViewHolder.getAdapterPosition()).getItemName());
                intent.putExtra("CookingTime", myFoodList.get(foodViewHolder.getAdapterPosition()).getItemTime());
                mContext.startActivity(intent);
            }
        });
        //Animation when scrolling up and down
        setAnimation(foodViewHolder.itemView, position);
    }

    public void setAnimation(View viewToAnimate, int position){
        if(position > lastPosition){
            ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f,1.0f,
                                                      Animation.RELATIVE_TO_SELF,0.5f,
                                                      Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(1500);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;

        }
    }

    @Override
    public int getItemCount() {
        return myFoodList.size();
    }

    public void filteredList(ArrayList<FoodData> filteredList) {
        myFoodList = filteredList;
        notifyDataSetChanged();
    }
}

class FoodViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView mTitle, mDescription, mTime;
    CardView mCardView;

    public FoodViewHolder( View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
        mDescription = itemView.findViewById(R.id.tvDescription);
        mTime = itemView.findViewById(R.id.tvTime);

        mCardView = itemView.findViewById(R.id.myCardView);
    }
}
