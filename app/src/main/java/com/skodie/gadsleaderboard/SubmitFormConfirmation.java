package com.skodie.gadsleaderboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.skodie.gadsleaderboard.services.SubmitToGoogleFormsWebService;

import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SubmitFormConfirmation extends Activity {
    private static final String BASE_URL = "https://docs.google.com/forms/d/e/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int) (width * .9), (int) (height * .6));

        //Get String extras from intent
        String mFirstName, mLastName, mEmail, mLink;
        Intent intent = getIntent();
        mFirstName = intent.getStringExtra("firstName");
        mLastName = intent.getStringExtra("lastName");
        mEmail = intent.getStringExtra("email");
        mLink = intent.getStringExtra("githubLink");

        //Initialize Retrofit
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        //Start The SubmitWebservice
        final SubmitToGoogleFormsWebService submitToGoogleForms = retrofit.create(SubmitToGoogleFormsWebService.class);
        Button submit = findViewById(R.id.submit_project);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Void> submitProjectRequest = submitToGoogleForms.submitProject(mFirstName, mLastName, mLink, mEmail);
                submitProjectRequest.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(SubmitFormConfirmation.this, SubmitSucessful.class));
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(SubmitFormConfirmation.this, SubmitFailed.class));
                    }
                });
            }
        });

        ImageView cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
