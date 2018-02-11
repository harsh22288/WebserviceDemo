package com.systemplus.webservice.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.systemplus.webservice.R;
import com.systemplus.webservice.adapter.MovieClassAdapter;
import com.systemplus.webservice.api.ApiClient;
import com.systemplus.webservice.api.ApiInterface;
import com.systemplus.webservice.model.MovieData;
import com.systemplus.webservice.model.MoviesResponse;
import com.systemplus.webservice.model.Result;
import com.systemplus.webservice.util.RecyclerItemClickListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseClass {

    RecyclerView moviesList;

    private MovieClassAdapter mMovieClassAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        moviesList = findViewById(R.id.moviesList);

        moviesList.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Result  result = mMovieClassAdapter.getItem(position);

                      // This approach
                     /*   final ApiInterface apiService =
                                ApiClient.getClient().create(ApiInterface.class);
                        Call<MoviesResponse> call = apiService.getMovieDetails(19404,API_KEY);
                        call.enqueue(new Callback<MoviesResponse>() {
                            @Override
                            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                                MoviesResponse moviesResponse = response.body();
                            }

                            @Override
                            public void onFailure(Call<MoviesResponse> call, Throwable t) {

                            }
                        });*/
                        Intent newIntent = new Intent(MainActivity.this, ShowDetail.class);
                        newIntent.putExtra("id", result.getId());
                        startActivity(newIntent);


                    }
                })
        );

        Call<MovieData> call = apiService.getTopRatedMovies(API_KEY);

        showProgressDialog();
        call.enqueue(new Callback<MovieData>() {
            @Override
            public void onResponse(Call<MovieData> call, Response<MovieData> response) {
                hidProgressDialog();
                MovieData movieData = response.body();
                List<Result> moviesResult = movieData.getResults();

                 mMovieClassAdapter = new MovieClassAdapter(moviesResult, MainActivity.this);
                moviesList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                moviesList.setAdapter(mMovieClassAdapter);

            }

            @Override
            public void onFailure(Call<MovieData> call, Throwable t) {
                hidProgressDialog();
                showToast(t.getMessage());
            }
        });
    }


}
