package com.example.mad_cw2_w1790286;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RatingsMoviesActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "NetworkStatusExample";
    private Button findInIMDB;
    private ListView movieList;
    ArrayList<Movie> movies = new ArrayList<>();
    private String url;
    private TextView textView;
    private ImageView im;
    private ImageView im2;
    private TextView ratingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        findInIMDB = findViewById(R.id.findInIMDB);
        movieList = findViewById(R.id.movieList);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        this.movieList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        MovieDatabase dataBase = new MovieDatabase(this);
        Cursor cursor = dataBase.getMovieDetails();
        if(cursor.getCount()==0){
            Toast.makeText(RatingsMoviesActivity.this, "No Movies Found!", Toast.LENGTH_SHORT).show();
        }
        else {

            while(cursor.moveToNext()){
                String title = cursor.getString(0);
                int year = Integer.parseInt(cursor.getString(1));
                String director = cursor.getString(2);
                String actors = cursor.getString(3);
                int rating = Integer.parseInt(cursor.getString(4));
                String review = cursor.getString(5);
                int fav = cursor.getInt(6);
                Boolean newFav = false;

                if (fav == 0){
                    newFav = false;
                }else {
                    newFav = true;
                }
                Movie movie = new Movie(title, year, director, actors, rating, review, newFav);
                movies.add(movie);
            }

            ArrayList<String> movieNames = new ArrayList<>();
            for (int i =0; i<movies.size(); i++){
                movieNames.add(movies.get(i).getTitle());
            }

            ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, movieNames);

            // Declaring adapter to ListView
            movieList.setAdapter(dataAdapter);
        }

        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                CheckedTextView checkedTextView = ((CheckedTextView)view);
//                checkedTextView.setChecked(!checkedTextView.isChecked());

                //Log.i(TAG, "onItemClick: " +position);
//                CheckedTextView v = (CheckedTextView) view;
//                boolean currentCheck = v.isChecked();
                int selectedID = movieList.getCheckedItemPosition();
                String title = movies.get(selectedID).getTitle();
                int year = movies.get(selectedID).getYear();
                url = title+" "+year;
                System.out.println(url);
//                UserAccount user = (UserAccount) movieList.getItemAtPosition(position);
//                user.setActive(!currentCheck);
            }
        });

        findInIMDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RatingsMoviesActivity.this, MovieDetailsViewActivity.class);
                Bundle switchBundle = new Bundle();
                switchBundle.putString("movieTitle", url);
                intent.putExtras(switchBundle);
                startActivity(intent);
            }
        });

    }
}