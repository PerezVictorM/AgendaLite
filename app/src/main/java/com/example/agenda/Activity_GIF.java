package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class Activity_GIF extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__gif);

        ImageView imageView = findViewById(R.id.imageView_1);


        Glide.with(getBaseContext()).load(R.raw.g)
                .apply(new RequestOptions().placeholder(R.drawable.ic_cloud_download_black_24dp).error(R.drawable.ic_close_black_24dp).override(600,600))
                .into(imageView);
    }
}
