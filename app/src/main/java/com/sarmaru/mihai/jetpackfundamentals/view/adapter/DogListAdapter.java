package com.sarmaru.mihai.jetpackfundamentals.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.sarmaru.mihai.jetpackfundamentals.R;
import com.sarmaru.mihai.jetpackfundamentals.model.DogBreed;
import com.sarmaru.mihai.jetpackfundamentals.util.GlideUtil;
import com.sarmaru.mihai.jetpackfundamentals.view.ui.ListFragmentDirections;

import java.util.List;

public class DogListAdapter extends RecyclerView.Adapter<DogListAdapter.DogViewHolder> {

    private List<DogBreed> dogsList;

    public DogListAdapter(List<DogBreed> dogsList) {
        this.dogsList = dogsList;
    }

    public void updateDogsList(List<DogBreed> newDogsList) {
        dogsList.clear();
        dogsList.addAll(newDogsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_list, parent, false);
        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {
        LinearLayout linearLayout = holder.itemView.findViewById(R.id.list_item_layout);
        ImageView imageViewDog = holder.itemView.findViewById(R.id.image_view_dog_list);
        TextView dogName = holder.itemView.findViewById(R.id.text_view_list_title);
        TextView lifespan = holder.itemView.findViewById(R.id.text_view_list_subtitle);

        dogName.setText(dogsList.get(position).dogBreed);
        lifespan.setText(dogsList.get(position).lifeSpan);

        // Use Glide to load images
        GlideUtil.loadImage(imageViewDog, dogsList.get(position).imageUrl, GlideUtil.getProgressDrawable(imageViewDog.getContext()));

        //
        linearLayout.setOnClickListener(v -> {
            ListFragmentDirections.ActionDetail action = ListFragmentDirections.actionDetail();
            action.setDogUuid(dogsList.get(position).uuid);
            Navigation.findNavController(linearLayout).navigate(action);
        });
    }

    @Override
    public int getItemCount() {
        return dogsList.size();
    }

    class DogViewHolder extends RecyclerView.ViewHolder {

        public View itemView;

        public DogViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
