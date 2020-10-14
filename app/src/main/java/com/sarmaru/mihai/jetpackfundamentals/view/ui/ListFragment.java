package com.sarmaru.mihai.jetpackfundamentals.view.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sarmaru.mihai.jetpackfundamentals.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {

    @BindView(R.id.floatingActionButtonList)
    FloatingActionButton floatingActionButtonList;

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
        // Uses lambda expression
        floatingActionButtonList.setOnClickListener(v -> onGoToDetails());
    }

    private void onGoToDetails() {
        NavDirections action = ListFragmentDirections.actionDetail();
        Navigation.findNavController(floatingActionButtonList).navigate(action);
    }
}