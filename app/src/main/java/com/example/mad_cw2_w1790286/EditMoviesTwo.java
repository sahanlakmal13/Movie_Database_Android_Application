package com.example.mad_cw2_w1790286;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditMoviesTwo extends AppCompatActivity {
    ArrayList<Movie> movies = new ArrayList<>();
    EditText yearEditText, titleEditText, directorEditText, actorsEditText , reviewEditText ;
    Button saveButton;
    RatingBar ratingBar;
    CheckBox checkBoxFav;
    EditMoviesTwo.listViewAdapter dataAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movies_two);

        //Declaring fields
        yearEditText = findViewById(R.id.year_txt_edit_movie_edit_two);
        ratingBar = findViewById(R.id.rating_bar_movie_edit_two);
        titleEditText = findViewById(R.id.title_txt_edit_movie_edit_two);
        directorEditText = findViewById(R.id.director_txt_edit_movie_edit_two);
        actorsEditText = findViewById(R.id.actors_txt_edit_movie_edit_two);
        reviewEditText = findViewById(R.id.review_txt_edit_movie_edit_two);
        saveButton = findViewById(R.id.save_btn_movie_edit_two);
        checkBoxFav = findViewById(R.id.fav_check_box_movie_edit_two);

        MovieDatabase dataBase = new MovieDatabase(this);

        Cursor cursor = dataBase.getMovieDetails();

        while(cursor.moveToNext()){
            String title = cursor.getString(0);
            int year = Integer.parseInt(cursor.getString(1));
            String director = cursor.getString(2);
            String actors = cursor.getString(3);
            int rating = Integer.parseInt(cursor.getString(4));
            String review = cursor.getString(5);
            int fav = cursor.getInt(6);
            Boolean newFav;

            if (fav == 0){
                newFav = false;
            }else {
                newFav = true;
            }

            Movie movie = new Movie(title, year, director, actors, rating, review, newFav);
            movies.add(movie);

        }

        for (Movie movie: movies){
            if (movie.getTitle().equals(EditMoviesActivity.clickedMovie)){
                yearEditText.setText("" + movie.getYear());
                ratingBar.setRating(movie.getRating());
                titleEditText.setText(movie.getTitle());
                directorEditText.setText(movie.getDirector());
                actorsEditText.setText(movie.getActors());
                reviewEditText.setText(movie.getReview());
                if (movie.isFav()){
                    checkBoxFav.setChecked(true);
                }else {
                    checkBoxFav.setChecked(false);
                }
            }
        }

        dataAdapter = new EditMoviesTwo.listViewAdapter(this, R.layout.list_view_movies, movies);
        ListView listView = (ListView) findViewById(R.id.list_view_fav);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Movie> moviesList = dataAdapter.moviesList;

                for(int i=0;i<moviesList.size();i++){
                    Movie movie = moviesList.get(i);
                    int checkingFav;
                    if (checkBoxFav.isChecked()){
                        checkingFav = 1;
                    }else {
                        checkingFav = 0;
                    }
                    int tempYear = Integer.parseInt(yearEditText.getText().toString().trim());
                    int tempRating = (int) ratingBar.getRating();
                    if (tempYear > 2021 || tempYear < 1895) {
                        yearEditText.setError("Year must be between 1895 and 2021");
                    }else {
                        boolean isUpdated = dataBase.updateMovieDetails(titleEditText.getText().toString().trim(), tempYear, directorEditText.getText().toString().trim() , actorsEditText.getText().toString().trim(), tempRating, reviewEditText.getText().toString().trim(), checkingFav);
                        if (isUpdated){
                            Toast.makeText(EditMoviesTwo.this, "Movie Details Updated", Toast.LENGTH_SHORT).show();
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
            TextView title;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            EditMoviesTwo.listViewAdapter.ViewHolder holder;

            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_view_only_movies, null);

            holder = new EditMoviesTwo.listViewAdapter.ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.textView_only);

            Movie movie = moviesList.get(position);
            holder.title.setText(movie.getTitle());

            return convertView;

        }
    }
}

