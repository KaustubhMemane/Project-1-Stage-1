package com.kmema.android.movienow;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONException;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ProgressBar pb_progressBar;
            @Override
                protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                pb_progressBar =(ProgressBar)findViewById(R.id.pbloadingIndicator);
                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?api_key=/*User Need to enter their Personal Key before running this APP*/";
                makeMovieDBquery(MOVIE_BASE_URL);
    }
    void gridView(final MovieDB[] s)
    {
        final String[] posterLinks = new String[s.length];
        final String[]  title = new String[s.length];
        final String[] overview = new String[s.length];
        final String[] ratings = new String[s.length];
        final String[] release_date = new String[s.length];
        final String[] backdrop_path= new String[s.length];

        for(int i=0;i<s.length;i++)
        {
            posterLinks[i]= "http://image.tmdb.org/t/p/w500/"+s[i].poster_path;
            title[i] = s[i].originalTitle;
            overview[i]=s[i].overview;
            ratings[i]=s[i].vote_average;
            release_date[i]=s[i].release_date;
            backdrop_path[i] = s[i].backdrop_path;
        }
        GridView gridView = (GridView) findViewById(R.id.gridView);
        GridImageViewerClass adapter=new GridImageViewerClass(MainActivity.this,posterLinks);
        gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Context context= MainActivity.this;
                    Class ReceivingObject = ChildActivityPage.class;
                    Intent newIntent = new Intent(context,ReceivingObject);
                    newIntent.putExtra("TITLE", title[position]);
                    newIntent.putExtra("OVERVIEW", overview[position]);
                    newIntent.putExtra("RATING", ratings[position]);
                    newIntent.putExtra("RELEASE_DATE", release_date[position]);
                    newIntent.putExtra("BACKDROP_PATH", backdrop_path[position]);
                    startActivity(newIntent);
                }
            });
    }

    void makeMovieDBquery(String link)
    {
        URL jsonResponse= ConnectingNetwork.buildUrl(link);
        new MovieDbAsyncTask().execute(jsonResponse);
    }

    void showErrorMessage()
    {
        Context context=MainActivity.this;
        String ErrorMessage = "Error Please Try Again";
        Toast.makeText(context,ErrorMessage,Toast.LENGTH_LONG).show();
    }

    /*
To check the connectivity to the internet, I have referred the code from provided reference of Stackover flow.
http://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
*/
    public class MovieDbAsyncTask extends AsyncTask<URL, Void, MovieDB[]>
    {
        @Override
        protected MovieDB[] doInBackground(URL... params) {

            Context context=MainActivity.this;
            URL searchURL = params[0];
            String jsonResponse;
            try{
                final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
                final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if(networkInfo != null && networkInfo.isConnected()) {
                    jsonResponse = ConnectingNetwork.getResponseFromHttpUrl(searchURL);
                    return JsonToSimple.jsonConvertString(MainActivity.this, jsonResponse);
                }
            }
            catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb_progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(MovieDB[] s) {
            super.onPostExecute(s);
            pb_progressBar.setVisibility(View.INVISIBLE);
            if(s != null)
            {
                gridView(s);
            }
            else
            {
                showErrorMessage();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    @Override
        public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatwasSelected = item.getItemId();
            if(itemThatwasSelected == R.id.action_toprated)
            {
              String topRatedlink = "http://api.themoviedb.org/3/movie/top_rated?api_key=/*User Need to enter their Personal Key before running this APP*/";
                makeMovieDBquery(topRatedlink);
            }
                else if(itemThatwasSelected == R.id.popular)
            {
                String popularMovies = "http://api.themoviedb.org/3/movie/popular?api_key=/*User Need to enter their Personal Key before running this APP*/";
                makeMovieDBquery(popularMovies);
            }
        return super.onOptionsItemSelected(item);
        }
}