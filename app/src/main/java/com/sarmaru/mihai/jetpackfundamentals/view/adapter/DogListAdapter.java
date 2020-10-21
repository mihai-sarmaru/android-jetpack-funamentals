package com.sarmaru.mihai.jetpackfundamentals.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.sarmaru.mihai.jetpackfundamentals.R;
import com.sarmaru.mihai.jetpackfundamentals.databinding.DogListBinding;
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        DogListBinding view = DataBindingUtil.inflate(inflater, R.layout.dog_list, parent, false);
        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {
        holder.itemView.setDog(dogsList.get(position));
    }

    @Override
    public int getItemCount() {
        return dogsList.size();
    }

    class DogViewHolder extends RecyclerView.ViewHolder {

        public DogListBinding itemView;

        public DogViewHolder(@NonNull DogListBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }
}
