package com.example.mad_cw2_w1790286;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterMovieActivity extends AppCompatActivity {

    private EditText movieTitle,movieYear,director,castMembers,ratings,reviews;
    private Button saveButton;

    Boolean errorFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movie);

        //Declaring fields
        movieTitle = findViewById(R.id.movieTitleEditText);
        movieYear = findViewById(R.id.movieYearEditText);
        director = findViewById(R.id.directorEditText);
        castMembers = findViewById(R.id.castMembersEditText);
        ratings = findViewById(R.id.ratingsEditText);
        reviews = findViewById(R.id.reviewsEditText);
        saveButton = findViewById(R.id.saveButton);

        MovieDatabase db = new MovieDatabase(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Re-setting fields
                errorFlag = true;

                if (movieTitle.getText().toString().trim().isEmpty() || movieYear.getText().toString().trim().isEmpty() ||
                        director.getText().toString().trim().isEmpty() || castMembers.getText().toString().trim().isEmpty() ||
                        ratings.getText().toString().trim().isEmpty() || reviews.getText().toString().trim().isEmpty()) {

                    Toast.makeText(RegisterMovieActivity.this,"Please fill all the fields!",Toast.LENGTH_SHORT).show();

                }else {
                    String title = movieTitle.getText().toString().trim();
                    String directorName = director.getText().toString().trim();
                    String actors = castMembers.getText().toString().trim();
                    String review = reviews.getText().toString().trim();
                    int year = Integer.parseInt(movieYear.getText().toString());
                    int rating = Integer.parseInt(ratings.getText().toString());
                    int fav = 0;

                    if (year > 2021 || year < 1895) {
                        movieYear.setError("Year must be between 1895 and 2021");
                        errorFlag = false;
                    }

                    if (rating > 10 || rating < 1  ) {
                        ratings.setError("Rating must be between 1 and 10");
                        errorFlag = false;
                    }

                    if (errorFlag) {
                        //Adding data into database
                        db.insertMovieDetails(title, year, directorName, actors, rating, review, fav);
                        Toast.makeText(RegisterMovieActivity.this, "Movie Added", Toast.LENGTH_SHORT).show();

                        movieTitle.setText("");
                        movieYear.setText("");
                        director.setText("");
                        castMembers.setText("");
                        ratings.setText("");
                        reviews.setText("");

                    }else {
                        Toast.makeText(RegisterMovieActivity.this,"Please correct the errors!",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}