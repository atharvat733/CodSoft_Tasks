package com.example.assignment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView fetchquote;
    private TextView fetchauthor;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchquote = findViewById(R.id.quote);
        fetchauthor = findViewById(R.id.author);
        Button buttonParse = findViewById(R.id.new_quote);
        Button shareButton = findViewById(R.id.shareButton);
        Button favoriteButton = findViewById(R.id.favoriteButton);

        mQueue = Volley.newRequestQueue(this);

        // Set OnClickListener for the "New Quote" button
        buttonParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });

        // Set OnClickListener for the "Share Quote" button
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareQuote();
            }
        });

        // Set OnClickListener for the "Favorite Quote" button
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFavoriteQuote();
            }
        });
    }

    private void jsonParse() {
        String URL = "https://api.quotable.io/random";
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String quote = response.getString("content");
                            String author = response.getString("author");

                            fetchquote.setText(quote);
                            fetchauthor.setText(author);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(objectRequest);
    }

    private void shareQuote() {
        String currentQuote = fetchquote.getText().toString();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, currentQuote);
        startActivity(Intent.createChooser(shareIntent, "Share Quote"));
    }

    private void saveFavoriteQuote() {
        String currentQuote = fetchquote.getText().toString();

        // Retrieve existing favorites from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("FavoriteQuotes", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Add the current quote to favorites
        editor.putString("quote", currentQuote);

        // Save updated favorites back to SharedPreferences
        editor.apply();

        Toast.makeText(this, "Quote added to favorites", Toast.LENGTH_SHORT).show();
    }
}
