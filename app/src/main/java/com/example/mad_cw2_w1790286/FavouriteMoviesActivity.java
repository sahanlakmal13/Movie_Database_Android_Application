package com.example.mad_cw2_w1790286;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class FavouriteMoviesActivity extends AppCompatActivity {

    Button saveButton;

    ArrayList<Movie> movies = new ArrayList<>();
    FavouriteMoviesActivity.listViewAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movies);

        MovieDatabase dataBase = new MovieDatabase(this);
        Cursor cursor = dataBase.getMovieDetails();

        if(cursor.getCount() == 0){
            Toast.makeText(FavouriteMoviesActivity.this, "No Movies Found!", Toast.LENGTH_SHORT).show();
        }
        else {

            while(cursor.moveToNext()){
                if (cursor.getInt(6) == 1){
                    String title = cursor.getString(0);
                    int year = Integer.parseInt(cursor.getString(1));
                    String director = cursor.getString(2);
                    String actors = cursor.getString(3);
                    int rating = Integer.parseInt(cursor.getString(4));
                    String review = cursor.getString(5);
                    boolean fav = true;

                    Movie movie = new Movie(title, year, director, actors, rating, review, fav);
                    movies.add(movie);
                }

            }
            //Setting movies ArrayList in alphabetical order
            Collections.sort(movies, new DisplayMoviesActivity.SortByTitle());
        }

        //Custom created ArrayAdapter
        dataAdapter = new FavouriteMoviesActivity.listViewAdapter(this, R.layout.list_view_movies, movies);
        ListView listView = (ListView) findViewById(R.id.list_view_fav);

        // Declaring adapter to ListView
        listView.setAdapter(dataAdapter);

        //onClick of save button
        saveButton = findViewById(R.id.save_btn_fav);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Movie> moviesList = dataAdapter.moviesList;

                for(int i=0;i<moviesList.size();i++){
                    Movie movie = moviesList.get(i);
                    if(!movie.isFav()) {
                        boolean isUpdated = dataBase.updateMovieDetails(movie.getTitle(), movie.getYear(), movie.getDirector(), movie.getActors(), movie.getRating(), movie.getReview(), 0);

                        if (isUpdated){
                            Toast.makeText(FavouriteMoviesActivity.this, "Removed from favourites !", Toast.LENGTH_SHORT).show();
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

            FavouriteMoviesActivity.listViewAdapter.ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.list_view_movies, null);

                holder = new FavouriteMoviesActivity.listViewAdapter.ViewHolder();
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
                holder = (FavouriteMoviesActivity.listViewAdapter.ViewHolder) convertView.getTag();
            }

            Movie movie = moviesList.get(position);
            holder.title.setText(movie.getTitle());
            holder.fav.setChecked(movie.isFav());
            holder.fav.setTag(movie);

            return convertView;

        }
    }
}




