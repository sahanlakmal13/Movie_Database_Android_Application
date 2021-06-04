package com.example.mad_cw2_w1790286;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditMoviesActivity extends AppCompatActivity {
    ArrayList<Movie> movies = new ArrayList<>();
    listViewAdapter dataAdapter;
    public static String clickedMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movies);

        MovieDatabase dataBase = new MovieDatabase(this);

        Cursor cursor = dataBase.getMovieDetails();
        if(cursor.getCount()==0){
            Toast.makeText(EditMoviesActivity.this, "No Movies Found !", Toast.LENGTH_SHORT).show();
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
                boolean newFav = false;

                if (fav == 0){
                    newFav = false;
                }else {
                    newFav = true;
                }

                Movie movie = new Movie(title, year, director, actors, rating, review, newFav);
                movies.add(movie);

            }

        }

        //Custom created ArrayAdapter
        dataAdapter = new listViewAdapter(this, R.layout.list_view_only_movies, movies);
        ListView listView = (ListView) findViewById(R.id.list_view_edit);

        // Declaring adapter to ListView
        listView.setAdapter(dataAdapter);

    }



    private class listViewAdapter extends ArrayAdapter<Movie> {

        private ArrayList<Movie> moviesList;

        private listViewAdapter(Context context, int textViewResourceId, ArrayList<Movie> moviesList) {
            super(context, textViewResourceId, moviesList);
            this.moviesList = new ArrayList<Movie>();
            this.moviesList.addAll(moviesList);
        }

        private class ViewHolder {
            TextView title;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            listViewAdapter.ViewHolder holder;

            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_view_only_movies, null);

            holder = new listViewAdapter.ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.textView_only);

            Movie movie = moviesList.get(position);
            holder.title.setText(movie.getTitle());

            holder.title.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    clickedMovie = movie.getTitle();
                    openActivityEditTwo();
                }
            });


            return convertView;

        }

    }

    public void openActivityEditTwo() {
        Intent intent = new Intent(this, EditMoviesTwo.class);
        startActivity(intent);

    }

}

