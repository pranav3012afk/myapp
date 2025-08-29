package com.example.myapp;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class gallary extends AppCompatActivity {
    GridView gallerygrid;
    ImageView homeicon, donationicon, profileicon, devicon, logouticon;
    FirebaseAuth mauth;

    int[] images = {
            R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3,
            R.drawable.img_4,
            R.drawable.img_5,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary);

        gallerygrid = findViewById(R.id.gallerygrid);
        gallerygrid.setAdapter(new GalleryAdapter());
        mauth = FirebaseAuth.getInstance();
        homeicon = findViewById(R.id.homeicon);
        donationicon = findViewById(R.id.donationicon);
        profileicon = findViewById(R.id.profileicon);
        devicon = findViewById(R.id.devicon);
        logouticon = findViewById(R.id.logouticon);
        gallerygrid.setOnItemClickListener((parent, view, position, id) -> {
            // Show full image dialog
            showFullImageDialog(images[position]);
        });
        homeicon.setOnClickListener(v -> {
            startActivity(new Intent(gallary.this, Home.class));
            finish();
        });

        donationicon.setOnClickListener(v -> {
            startActivity(new Intent(gallary.this, Donation_History.class));
        });

        profileicon.setOnClickListener(v -> {
            startActivity(new Intent(gallary.this, user_profile.class));
        });

        devicon.setOnClickListener(v -> {
            startActivity(new Intent(gallary.this, DeveloperInfoActivity.class));
        });

        logouticon.setOnClickListener(v -> {
            mauth.signOut();
            Intent intent = new Intent(gallary.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void showFullImageDialog(int imageResId) {
        Dialog dialog = new Dialog(gallary.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView fullImage = dialog.findViewById(R.id.fullimageview);
        fullImage.setImageResource(imageResId);
        dialog.show();
    }

    class GalleryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return images[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.gallery_item, parent, false);
            }

            ImageView img = convertView.findViewById(R.id.galleryimage);
            img.setImageResource(images[position]);

            return convertView;
        }

    }
}
