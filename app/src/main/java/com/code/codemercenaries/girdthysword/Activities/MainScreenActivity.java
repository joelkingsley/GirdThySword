package com.code.codemercenaries.girdthysword.Activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.Font.FontHelper;
import com.code.codemercenaries.girdthysword.Objects.LeaderboardUser;
import com.code.codemercenaries.girdthysword.Objects.Version;
import com.code.codemercenaries.girdthysword.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainScreenActivity extends AppCompatActivity {

    TextToSpeech tts;
    String SYSTEM_PREF = "system_pref";
    FirebaseAuth mAuth;
    GoogleApiClient mGoogleApiClient;
    String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new FontHelper(this).initialize();

        setContentView(R.layout.activity_main_screen);

        SharedPreferences systemPreferences = getSharedPreferences(SYSTEM_PREF, 0);


        DBHandler dbHandler = new DBHandler(this);
        List<Version> versionList = dbHandler.getVersions();
        int versesMemorized = 0;
        for (Version version : versionList) {
            versesMemorized += dbHandler.getTotalNumberOfVersesMemorized(version.get_id());
        }
        systemPreferences.edit().putLong("verses_memorized", versesMemorized).commit();

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference leaderboard = FirebaseDatabase.getInstance().getReference("leaderboard").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        leaderboard.setValue(new LeaderboardUser(0, mAuth.getCurrentUser().getPhotoUrl().toString(), mAuth.getCurrentUser().getDisplayName(), 1, "Beginner", -systemPreferences.getLong("verses_memorized", 0)));

        InputStream ims;
        try {
            ImageView logo = findViewById(R.id.logo);
            CardView readCard = findViewById(R.id.readCard);
            CardView memorizeCard = findViewById(R.id.memorizeCard);
            TextView read = findViewById(R.id.read);
            TextView memorize = findViewById(R.id.memorize);
            Button help = findViewById(R.id.help);
            Button settings = findViewById(R.id.settings);
            ImageButton logout = findViewById(R.id.logOut);

            Typeface buttonFont = Typeface.createFromAsset(getAssets(), getString(R.string.notosans_eng_black_font));
            read.setTypeface(buttonFont);
            memorize.setTypeface(buttonFont);
            help.setTypeface(buttonFont);
            settings.setTypeface(buttonFont);

            ims = getAssets().open("dark_logo.png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            logo.setImageDrawable(d);

            readCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainScreenActivity.this, SelectVersionActivity.class));
                }
            });

            memorizeCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainScreenActivity.this, HomeScreenActivity.class));
                }
            });

            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainScreenActivity.this, SettingsScreenActivity.class));
                }
            });

            help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainScreenActivity.this, HelpScreenActivity.class));
                }
            });

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });

            displayName = mAuth.getCurrentUser().getDisplayName();

            // Configure Google Sign In
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            // Build a GoogleSignInClient with the options specified by gso.
            //mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Toast.makeText(MainScreenActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void logout() {

        if (mAuth.getCurrentUser() != null) {

            // Firebase sign out
            mAuth.signOut();

            // Google sign out
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            Toast.makeText(MainScreenActivity.this, displayName + " Logged out", Toast.LENGTH_LONG).show();
                        }
                    });
        }

        FileUtils.deleteQuietly(getCacheDir());
        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
            ((ActivityManager) getSystemService(ACTIVITY_SERVICE))
                    .clearApplicationUserData(); // note: it has a return value!
        }
    }
}
