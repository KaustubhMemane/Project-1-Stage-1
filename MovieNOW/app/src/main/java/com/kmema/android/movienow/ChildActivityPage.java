package com.kmema.android.movienow;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
public class ChildActivityPage extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        TextView mDisplayTextTitle = (TextView) findViewById(R.id.tvDisplay);
        TextView mDisplayTextOverview = (TextView) findViewById(R.id.tvDisplayOverview);
        TextView mDisplayTextRating = (TextView) findViewById(R.id.tvDisplayRatings);
        TextView mDisplayTextReleaseDate = (TextView) findViewById(R.id.tvDisplayReleaseDate);
        ImageView backpadthimage = (ImageView) findViewById(R.id.backpathImage);

        Intent intentThatStartTheActivity =getIntent();

        if(intentThatStartTheActivity.hasExtra("TITLE"))
        {
            String title = intentThatStartTheActivity.getStringExtra("TITLE");
            mDisplayTextTitle.setText(title);
        }
        if(intentThatStartTheActivity.hasExtra("RELEASE_DATE"))
        {
            String release_date = intentThatStartTheActivity.getStringExtra("RELEASE_DATE");
            release_date = "Release Date : "+release_date;
            mDisplayTextReleaseDate.setText(release_date);
        }
        if(intentThatStartTheActivity.hasExtra("RATING"))
        {
            String rating = intentThatStartTheActivity.getStringExtra("RATING");
            rating = "Rating : " + rating + "/10";
            mDisplayTextRating.setText(rating);
        }
        if(intentThatStartTheActivity.hasExtra("OVERVIEW"))
        {
            String overview = intentThatStartTheActivity.getStringExtra("OVERVIEW");
            mDisplayTextOverview.setText(overview);
        }
        if(intentThatStartTheActivity.hasExtra("BACKDROP_PATH"))
        {
            String backdrop_path = intentThatStartTheActivity.getStringExtra("BACKDROP_PATH");
            Picasso.with(ChildActivityPage.this)
                    .load("http://image.tmdb.org/t/p/w780"+backdrop_path).fit()
                    .into(backpadthimage);
        }
    }
}