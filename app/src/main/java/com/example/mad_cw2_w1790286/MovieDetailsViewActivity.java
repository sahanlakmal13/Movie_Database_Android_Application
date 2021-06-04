package com.example.mad_cw2_w1790286;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MovieDetailsViewActivity extends AppCompatActivity {

    private TextView movieDetails;
    private ImageView movieImage;
    String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details_view);
        System.out.println(url);
        Bundle extras = getIntent().getExtras();
        url = extras.getString("movieTitle");
        System.out.println(url);

        movieImage = findViewById(R.id.imageView2);
        movieDetails = findViewById(R.id.textView2);

        loadVodkaDrinks();

    }

    public void loadVodkaDrinks() {
        final String[] movieJson = new String[1];
        String DRINK_BASE_URL = "https://imdb-api.com/en/API/SearchTitle/k_phkkm7e6/";
        String FINAL_URL = DRINK_BASE_URL+url;

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {

                    movieJson[0] = GetMovies.getDrinkInfo(FINAL_URL);
                    getMovieDetails(movieJson[0]);
                }
            });
            t1.start();
        }
    }

    private void getMovieDetails(String s) {

        try {
            JSONObject jsonObject = new JSONObject(s);

            JSONArray itemsArray = jsonObject.getJSONArray("results");

            try {
                String id = itemsArray.getJSONObject(0).getString("id");
                String picUrl = itemsArray.getJSONObject(0).getString("image");
                System.out.println(id);
                String DRINK_BASE_URL = "https://imdb-api.com/en/API/UserRatings/k_phkkm7e6/";
                String FINAL_URL = DRINK_BASE_URL+id;

                String rating = GetMovies.getDrinkInfo(FINAL_URL);
                JSONObject jsonObject2 = new JSONObject(rating);
                String rating2 = jsonObject2.getString("totalRating");

                if (rating2.isEmpty()){
                    rating2 = "not available";
                }

                String finalRating = rating2;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        movieDetails.setText("Rating for "+url+" is "+ finalRating);
                    }
                });
                getPic(picUrl);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void getPic(String src){

        final Bitmap[] myBitmap = {null};

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("src",src);
                    URL url = new URL(src);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    myBitmap[0] = BitmapFactory.decodeStream(input);
                    Log.e("Bitmap","returned");
                    connection.disconnect();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            movieImage.setImageBitmap(myBitmap[0]);
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Exception",e.getMessage());
                }
            }
        });
        t2.start();
    }

}