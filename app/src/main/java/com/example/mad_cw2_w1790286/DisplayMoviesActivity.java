package com.example.mad_cw2_w1790286;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DisplayMoviesActivity extends AppCompatActivity {

    Button addToFavButton;
    listViewAdapter dataAdapter = null;
    ArrayList<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movies);

        addToFavButton = findViewById(R.id.addToFavBtn);
        MovieDatabase dataBase = new MovieDatabase(this);

        Cursor cursor = dataBase.getMovieDetails();
        if(cursor.getCount()==0){
            Toast.makeText(DisplayMoviesActivity.this, "No Movies Found!", Toast.LENGTH_SHORT).show();
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
            //Setting movies ArrayList in alphabetical order
            Collections.sort(movies, new SortByTitle());
        }

        //Custom created ArrayAdapter
        dataAdapter = new listViewAdapter(this, R.layout.list_view_movies, movies);
        ListView listView = (ListView) findViewById(R.id.scroll);

        // Declaring adapter to ListView
        listView.setAdapter(dataAdapter);

        //On click of the add to favourites button
        addToFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Movie> moviesList = dataAdapter.moviesList;

                for(int i=0;i<moviesList.size();i++){
                    Movie movie = moviesList.get(i);
                    if(movie.isFav()) {
                        boolean isUpdated = dataBase.updateMovieDetails(movie.getTitle(), movie.getYear(), movie.getDirector(), movie.getActors(), movie.getRating(), movie.getReview(), 1);

                        if (isUpdated){
                            Toast.makeText(DisplayMoviesActivity.this, "Added to favourites!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private class listViewAdapter extends ArrayAdapter<Movie> {

        private ArrayList<Movie> moviesList;

        private listViewAdapter(Context context, int textViewResourceId, ArrayList<Movie> moviesList) {
            super(context, textViewResourceId, moviesList);
            this.moviesList = new ArrayList<Movie>();
            this.moviesList.addAll(moviesList);
        }

        private class ViewHolder {
            CheckBox fav;
            TextView title;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.list_view_movies, null);

                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.textView_lv);
                holder.fav = (CheckBox) convertView.findViewById(R.id.checkBox_lv);
                convertView.setTag(holder);

                holder.fav.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox checkBox = (CheckBox) v ;
                        Movie movie = (Movie) checkBox.getTag();
                        movie.setFav(checkBox.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            Movie movie = moviesList.get(position);
            holder.title.setText(movie.getTitle());
            holder.fav.setChecked(movie.isFav());
            holder.fav.setTag(movie);
            return convertView;

        }
    }

    public static class SortByTitle implements Comparator<Movie> {
        //Overriding compare method to set movies ArrayList in alphabetical order according to the title
        @Override
        public int compare(Movie movie1, Movie movie2) {
            return movie1.getTitle().toLowerCase().compareTo(movie2.getTitle().toLowerCase());
        }
    }
}