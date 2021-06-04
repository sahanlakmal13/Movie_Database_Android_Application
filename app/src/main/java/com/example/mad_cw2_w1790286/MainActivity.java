package com.example.mad_cw2_w1790286;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void mainMenuButtons(View view) {

        Intent intent = new Intent();

        switch (view.getId()){

            case R.id.regMovBtn:
                intent = new Intent(MainActivity.this, RegisterMovieActivity.class);
                break;
            case R.id.disMovBtn:
                intent = new Intent(MainActivity.this, DisplayMoviesActivity.class);
                break;
            case R.id.favBtn:
                intent = new Intent(MainActivity.this, FavouriteMoviesActivity.class);
                break;
            case R.id.editMovBtn:
                intent = new Intent(MainActivity.this, EditMoviesActivity.class);
                break;
            case R.id.searchBtn:
                intent = new Intent(MainActivity.this, SearchMoviesActivity.class);
                break;
            case R.id.ratingBtn:
                intent = new Intent(MainActivity.this, RatingsMoviesActivity.class);
                break;
        }

        startActivity(intent);
    }
}