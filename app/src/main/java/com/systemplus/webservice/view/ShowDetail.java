package com.systemplus.webservice.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.systemplus.webservice.R;
import com.systemplus.webservice.api.ApiClient;
import com.systemplus.webservice.api.ApiInterface;
import com.systemplus.webservice.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDetail extends BaseClass {
    private static final String API_KEY = "7e8f60e325cd06e164799af1e317d7a7";
    private MoviesResponse moviesResponse;
    public Integer gotPosition;
    private TextView txtGenre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        gotPosition = getIntent().getIntExtra("id", 0);

        final ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        showProgressDialog();
        Call<MoviesResponse> call = apiService.getMovieDetails(gotPosition,API_KEY);

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                hidProgressDialog();
                moviesResponse = response.body();
                //Toast.makeText(ShowDetail.this, new Gson().toJson(moviesResponse),Toast.LENGTH_SHORT).show();
                setView();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                hidProgressDialog();

            }
        });



    }

    private void setView() {
        txtGenre = findViewById(R.id.txtGenre);
        txtGenre.setText(moviesResponse.getOriginalTitle().toString());
    }
}
