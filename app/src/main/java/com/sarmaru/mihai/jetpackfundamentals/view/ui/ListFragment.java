package com.sarmaru.mihai.jetpackfundamentals.view.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sarmaru.mihai.jetpackfundamentals.R;
import com.sarmaru.mihai.jetpackfundamentals.model.DogBreed;
import com.sarmaru.mihai.jetpackfundamentals.view.adapter.DogListAdapter;
import com.sarmaru.mihai.jetpackfundamentals.viewmodel.DogListViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {

    private DogListViewModel dogListViewModel;
    private DogListAdapter dogListAdapter = new DogListAdapter(new ArrayList<>());

    @BindView(R.id.recycler_view_dogs_list)
    RecyclerView dogRecyclerView;

    @BindView(R.id.text_view_error)
    TextView textViewError;

    @BindView(R.id.progress_bar_loading)
    ProgressBar progressBar;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set view model
        dogListViewModel = ViewModelProviders.of(this).get(DogListViewModel.class);
        dogListViewModel.refresh();

        // Set recycler view layout and adapter
        dogRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dogRecyclerView.setAdapter(dogListAdapter);

        // Refresh functionality
        swipeRefreshLayout.setOnRefreshListener(() -> {
            dogRecyclerView.setVisibility(View.GONE);
            textViewError.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            dogListViewModel.refreshBypassCache();
            swipeRefreshLayout.setRefreshing(false);
        });

        observeViewModel();
    }

    private void observeViewModel() {
        dogListViewModel.dogsList.observe(this, dogBreeds -> {
            if (dogBreeds != null && dogBreeds instanceof List) {
                dogRecyclerView.setVisibility(View.VISIBLE);
                dogListAdapter.updateDogsList(dogBreeds);
            }
        });

        dogListViewModel.dogsLoadError.observe(this, isError -> {
            if (isError != null && isError instanceof Boolean) {
                textViewError.setVisibility(isError ? View.VISIBLE : View.GONE);
            }
        });

        dogListViewModel.loading.observe(this, isLoading -> {
            if (isLoading != null && isLoading instanceof Boolean) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isLoading) {
                    dogRecyclerView.setVisibility(View.GONE);
                    textViewError.setVisibility(View.GONE);
                }
            }
        });
    }
}