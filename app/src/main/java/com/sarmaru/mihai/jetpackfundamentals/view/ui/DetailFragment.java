package com.sarmaru.mihai.jetpackfundamentals.view.ui;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.palette.graphics.Palette;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sarmaru.mihai.jetpackfundamentals.R;
import com.sarmaru.mihai.jetpackfundamentals.databinding.FragmentDetailBinding;
import com.sarmaru.mihai.jetpackfundamentals.databinding.SendSmsDialogBinding;
import com.sarmaru.mihai.jetpackfundamentals.model.DogBreed;
import com.sarmaru.mihai.jetpackfundamentals.model.SmsInfo;
import com.sarmaru.mihai.jetpackfundamentals.palette.DogPalette;
import com.sarmaru.mihai.jetpackfundamentals.util.GlideUtil;
import com.sarmaru.mihai.jetpackfundamentals.viewmodel.DogDetailViewModel;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {

    private int dogUuid;
    private DogDetailViewModel dogDetailViewModel;
    private FragmentDetailBinding binding;

    private Boolean sendSmsStarted = false;

    private DogBreed currentDog;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        this.binding = binding;
        setHasOptionsMenu(true);
        return binding.getRoot();
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
            currentDog = dogLiveData;
            binding.setDog(dogLiveData);
            if (dogLiveData.imageUrl != null) {
                setBackgroundColor(dogLiveData.imageUrl);
            }
        });
    }

    private void setBackgroundColor(String url) {
        // Get image as Bitmap with Glide
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Use Palette on bitmap image
                        Palette.from(resource)
                                .generate(palette -> {
                                    // Palette could be null - need to check
                                    int color = palette.getLightMutedSwatch().getRgb();
                                    // Set color to binding
                                    DogPalette dogPalette = new DogPalette(color);
                                    binding.setPalette(dogPalette);
                                });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.details_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_send_sms:
                // prevent user from clicking multiple times
                if (!sendSmsStarted) {
                    sendSmsStarted = true;
                    ((MainActivity) getActivity()).checkSmsPermission();
                }
                break;
            case R.id.action_share:
                shareDogContent();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareDogContent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared Dog");
        shareIntent.putExtra(Intent.EXTRA_TEXT, currentDog.dogBreed + " - " + currentDog.lifeSpan);
        startActivity(Intent.createChooser(shareIntent, "Share with"));
    }

    public void onPermissionResult(Boolean permissionGranted) {
        if (isAdded() && sendSmsStarted && permissionGranted) {
            String smsText = currentDog.dogBreed + " - " + currentDog.lifeSpan;
            SmsInfo smsInfo = new SmsInfo("", smsText, currentDog.imageUrl);

            // Set custom dialog binding
            SendSmsDialogBinding dialogBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(getContext()),
                    R.layout.send_sms_dialog,
                    null,
                    false);
            dialogBinding.setSmsInfo(smsInfo);

            // Instantiate custom dialog
            new AlertDialog.Builder(getContext())
                    .setView(dialogBinding.getRoot())
                    .setPositiveButton("Send SMS", (dialog, which) -> {
                        if (!dialogBinding.editTextSmsDestination.getText().toString().isEmpty()) {
                            smsInfo.to = dialogBinding.editTextSmsDestination.getText().toString();
                            // Send SMS
                            sendSMS(smsInfo);
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {})
                    .show();

            sendSmsStarted = false;
        }
    }

    private void sendSMS(SmsInfo smsInfo) {
        int SMS_REQUEST_CODE = 0;
        Intent smsIntent = new Intent(getContext(), MainActivity.class);
        PendingIntent smsPendingIntent = PendingIntent.getActivity(getContext(), SMS_REQUEST_CODE, smsIntent, 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(smsInfo.to, null, smsInfo.text, smsPendingIntent, null);
    }
}