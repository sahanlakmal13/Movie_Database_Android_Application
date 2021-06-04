package com.example.mad_cw2_w1790286;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetMovies {

    private static final String LOG_TAG = GetMovies.class.getSimpleName();

    static String getDrinkInfo(String url) {

        System.out.println(url);
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String drinkJSONString = null;

        try {
//Uri builtURI = https://api.openweathermap.org/data/2.5/weather?q=London,uk&APPID=607503d4e6a218e86c089f635e4142e9
//String url = BOOK_BASE_URL + queryString + PRINT_TYPE;
//URL requestURL = new URL("https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=Vodka"
            // Open the network connection.);
            URL requestURL = new URL(url);
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Create a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                // Add the current line to the string.
                builder.append(line);

                // Since this is JSON, adding a newline isn't necessary (it won't
                // affect parsing) but it does make debugging a *lot* easier
                // if you print out the completed buffer for debugging.
                builder.append("\n");
            }

            if (builder.length() == 0) {
                // Stream was empty.  Exit without parsing.
                return null;
            }
            drinkJSONString = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the connection and the buffered reader.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(LOG_TAG, drinkJSONString);
        return drinkJSONString;
    }
}

