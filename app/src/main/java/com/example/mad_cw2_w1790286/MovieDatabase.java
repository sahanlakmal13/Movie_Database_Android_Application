package com.example.mad_cw2_w1790286;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MovieDatabase extends SQLiteOpenHelper {


    public MovieDatabase(@Nullable Context context) {
        super(context,"MoviesDataBase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table MovieDetails(title TEXT primary key, year NUMBER, director TEXT, actors TEXT, rating NUMBER, review TEXT, fav NUMBER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists MovieDetails");
    }

    //To insert data into the database
    public Boolean insertMovieDetails(String title, int year, String director, String actors, int rating, String review, int fav) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("title", title);
        cv.put("year", year);
        cv.put("director", director);
        cv.put("actors", actors);
        cv.put("rating", rating);
        cv.put("review", review);
        cv.put("fav", fav);

        long result = db.insert("MovieDetails", null, cv);
        if (result == -1) {
            return false;
        }else {
            return true;
        }
    }

    //To retrieve data from the database
    public Cursor getMovieDetails () {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("Select * from MovieDetails", null);
        return cursor;
    }


    //To delete data in the database
    public Boolean deleteMovieDetails (String employeeID) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from MovieDetails where employeeID = ?", new String[]{employeeID});

        if (cursor.getCount() > 0) {
            long result = DB.delete("Employeedetails", "employeeID=?", new String[]{employeeID});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    //To update data in the database
    public Boolean updateMovieDetails(String title, int year, String director, String actors, int rating, String review, int fav){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("title", title);
        cv.put("year", year);
        cv.put("director", director);
        cv.put("actors", actors);
        cv.put("rating", rating);
        cv.put("review", review);
        cv.put("fav", fav);

        DB.update("MovieDetails", cv, "title=?", new String[]{title});
        return true;
        }
}
