package com.sarmaru.mihai.jetpackfundamentals.view.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sarmaru.mihai.jetpackfundamentals.R;
import com.sarmaru.mihai.jetpackfundamentals.model.DogBreed;
import com.sarmaru.mihai.jetpackfundamentals.util.GlideUtil;
import com.sarmaru.mihai.jetpackfundamentals.viewmodel.DogDetailViewModel;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {

    private int dogUuid;

    private DogDetailViewModel dogDetailViewModel;

    @BindView(R.id.image_view_dog_detail)
    ImageView imageViewDogImage;

    @BindView(R.id.text_view_dog_name)
    TextView textViewDogName;

    @BindView(R.id.text_view_dog_purpose)
    TextView textViewDogPurpose;

    @BindView(R.id.text_view_dog_temperament)
    TextView textViewDogTemperament;

    @BindView(R.id.text_view_dog_lifespan)
    TextView textViewDogLifeSpan;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get arguments
        if (getArguments() != null) {
            dogUuid = DetailFragmentArgs.fromBundle(getArguments()).getDogUuid();
        }

        // Set view model
        dogDetailViewModel = ViewModelProviders.of(this).get(DogDetailViewModel.class);
        dogDetailViewModel.fetch(dogUuid);

        // Observe View Model
        observeViewModel();
    }

    private void observeViewModel() {
        dogDetailViewModel.dogLiveData.observe(this, dogLiveData -> {
            textViewDogName.setText(dogLiveData.dogBreed);
            textViewDogPurpose.setText(dogLiveData.bredFor);
            textViewDogTemperament.setText(dogLiveData.temperament);
            textViewDogLifeSpan.setText(dogLiveData.lifeSpan);

            // Load image from URL using Glide
            if (dogLiveData.imageUrl != null) {
                GlideUtil.loadImage(imageViewDogImage, dogLiveData.imageUrl, GlideUtil.getProgressDrawable(imageViewDogImage.getContext()));
            }
        });
    }
}