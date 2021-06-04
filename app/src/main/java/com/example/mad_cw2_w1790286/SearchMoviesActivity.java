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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchMoviesActivity extends AppCompatActivity {
    ArrayList<Movie> movies = new ArrayList<>();
    ArrayList<Movie> searchedMovies = new ArrayList<>();
    listViewAdapter dataAdapter;
    EditText searchEditText;
    Button searchViewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        MovieDatabase dataBase = new MovieDatabase(this);

        //Assigning fields
        searchEditText = findViewById(R.id.edit_text_search);
        searchViewButton = findViewById(R.id.search_btn_search);


        Cursor cursor = dataBase.getMovieDetails();
        if(cursor.getCount()==0){
            Toast.makeText(SearchMoviesActivity.this, "No Movies Found !", Toast.LENGTH_SHORT).show();
        }
        else {

            while(cursor.moveToNext()){
                String title = cursor.getString(0).toLowerCase();
                int year = Integer.parseInt(cursor.getString(1));
                String director = cursor.getString(2).toLowerCase();
                String actors = cursor.getString(3).toLowerCase();
                int rating = Integer.parseInt(cursor.getString(4));
                String review = cursor.getString(5).toLowerCase();
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

            searchViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchedMovies.clear();
                    String searchText = searchEditText.getText().toString().toLowerCase().trim();

                    for (Movie movie : movies) {
                        if (searchText.isEmpty()) {
                            System.out.println("Empty");
                        }
                        else if (movie.getTitle().contains(searchText) || movie.getDirector().contains(searchText) || movie.getActors().contains(searchText)) {
                            searchedMovies.add(movie);
                        }
                    }

                    searching();
                }
            });


        }


    }

    private void searching(){
        //Custom created ArrayAdapter
        dataAdapter = new listViewAdapter(this, R.layout.list_view_only_movies, searchedMovies);
        ListView listView = (ListView) findViewById(R.id.list_view_search);

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
            holder.title.setText(movie.getTitle() + ", Director: " + movie.getDirector() + ", Actors: " + movie.getActors());


            return convertView;

        }

    }



}