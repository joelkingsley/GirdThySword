package com.code.codemercenaries.girdthysword.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.code.codemercenaries.girdthysword.Objects.Chunk;
import com.code.codemercenaries.girdthysword.Objects.Section;
import com.code.codemercenaries.girdthysword.Objects.User;
import com.code.codemercenaries.girdthysword.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;

public class LoginActivity extends AppCompatActivity {

    public static final String C_KEY_ID = "id";
    public static final String C_KEY_SEQ = "seq";
    public static final String C_KEY_BOOK_NAME = "bookName";
    public static final String C_KEY_CHAP_NUM = "chapNum";
    public static final String C_KEY_START_VERSE_NUM = "startVerseNum";
    public static final String C_KEY_END_VERSE_NUM = "endVerseNum";
    public static final String C_KEY_NEXT_DATE_OF_REVIEW = "nextDateOfReview";
    public static final String C_KEY_SPACE = "space";
    public static final String C_KEY_SEC_ID = "secId";
    public static final String C_KEY_MASTERED = "mastered";
    public static final String C_KEY_VER_ID = "_version";
    public static final String S_KEY_ID = "id";
    public static final String S_KEY_BOOK_NAME = "book_name";
    public static final String S_KEY_CHAP_NUM = "chapter_num";
    public static final String S_KEY_START_VERSE_NUM = "start_verse_num";
    public static final String S_KEY_END_VERSE_NUM = "end_verse_num";
    public static final String S_KEY_VER_ID = "version";
    private final static int RC_SIGN_IN = 2;
    final String SYSTEM_PREF = "system_pref";
    private final String TAG = "LoginActivity";
    SignInButton signInButton;
    FirebaseAuth mAuth;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth.AuthStateListener mAuthListener;
    ImageView background;
    TextToSpeech tts;
    SharedPreferences systemPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mAuth = FirebaseAuth.getInstance();
        background = (ImageView) findViewById(R.id.girdThySword);

        systemPreferences = getSharedPreferences(SYSTEM_PREF, 0);

        InputStream ims;
        try {
            ims = getAssets().open("dark_logo.png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            background.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    if (mAuth.getCurrentUser() != null) {
                        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);

                        progressDialog.setMessage("Downloading Data from Firebase Database");

                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());

                        if (isNetworkAvailable()) {
                            progressDialog.show();
                        }

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("registered").getValue(boolean.class) != null) {
                                    Log.d("Firebase:", "Logged in");
                                    if (!systemPreferences.getBoolean("first_time", false)) {
                                        downloadToDatabase();
                                        systemPreferences.edit().putBoolean("first_time", true).apply();
                                    }
                                } else {
                                    dataSnapshot.getRef().setValue(new User(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getDisplayName(), mAuth.getCurrentUser().getEmail()));
                                    Log.d("Firebase:", "Created new user");
                                }
                                //theme = dataSnapshot.child("theme").getValue(String.class);
                                progressDialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainScreenActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("Firebase:", "Initialization failed");
                                Log.d("DatabaseError", databaseError.toString());
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Initialization failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    /*tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if(status != TextToSpeech.ERROR){
                                tts.setLanguage(Locale.UK);
                                tts.speak("Welcome to GirdThySword! Please login using your Google Account to join the club.", TextToSpeech.QUEUE_ADD, null);
                            }
                        }
                    });*/
                }
            }
        };

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
                        Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            //Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                Toast.makeText(this, "Logging in", Toast.LENGTH_LONG).show();
                Log.d(TAG, "First time");
            } else {
                Toast.makeText(LoginActivity.this, "Authorization went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //Disable back button
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void signIn() {
        //Intent signInIntent = mGoogleSignInClient.getSignInIntent();\

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void downloadToDatabase() {

        try {
            DatabaseReference chunks = FirebaseDatabase.getInstance().getReference("chunks").child(mAuth.getCurrentUser().getUid());
            DatabaseReference sections = FirebaseDatabase.getInstance().getReference("sections").child(mAuth.getCurrentUser().getUid());
            DatabaseReference users = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
            DatabaseReference userBible = FirebaseDatabase.getInstance().getReference("user-bible").child(mAuth.getCurrentUser().getUid());
            ;

            Log.d(TAG, "Download");

            chunks.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DBHandler dbHandler = new DBHandler(getApplicationContext());
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        /*String _id, int _seq, String _book_name, int _chapter_num,
                        int _start_verse_num, int _end_verse_num,
                        String _next_date_of_review, int space, String _sec_id, boolean _mastered, String _version*/
                        String id = snapshot.child(C_KEY_ID).getValue(String.class);
                        int seq = snapshot.child(C_KEY_SEQ).getValue(int.class);
                        String bookName = snapshot.child(C_KEY_BOOK_NAME).getValue(String.class);
                        int chapNum = snapshot.child(C_KEY_CHAP_NUM).getValue(int.class);
                        int startNum = snapshot.child(C_KEY_START_VERSE_NUM).getValue(int.class);
                        int endNum = snapshot.child(C_KEY_END_VERSE_NUM).getValue(int.class);
                        String nextDateOfReview = snapshot.child(C_KEY_NEXT_DATE_OF_REVIEW).getValue(String.class);
                        int space = snapshot.child(C_KEY_SPACE).getValue(int.class);
                        String secId = snapshot.child(C_KEY_SEC_ID).getValue(String.class);
                        boolean mastered = snapshot.child(C_KEY_MASTERED).getValue(Boolean.class);
                        String version = snapshot.child(C_KEY_VER_ID).getValue(String.class);
                        dbHandler.addChunk(new Chunk(id, seq, bookName, chapNum, startNum, endNum, nextDateOfReview, space, secId, mastered, version));
                        Log.d(TAG, "Chunk " + id);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("Login:", databaseError.toString());
                }
            });

            sections.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DBHandler dbHandler = new DBHandler(getApplicationContext());
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        /*String _id, String _book_name, int _chapter_num, int _start_verse_num, int _end_verse_num, String _version*/
                        String id = snapshot.child("_" + DBHandler.S_KEY_ID).getValue(String.class);
                        String bookName = snapshot.child("_" + DBHandler.S_KEY_BOOK_NAME).getValue(String.class);
                        int chapNum = snapshot.child("_" + DBHandler.S_KEY_CHAP_NUM).getValue(int.class);
                        int startNum = snapshot.child("_" + DBHandler.S_KEY_START_VERSE_NUM).getValue(int.class);
                        int endNum = snapshot.child("_" + DBHandler.S_KEY_END_VERSE_NUM).getValue(int.class);
                        String version = snapshot.child("_" + DBHandler.S_KEY_VER_ID).getValue(String.class);
                        dbHandler.addSection(new Section(id, bookName, chapNum, startNum, endNum, version));
                        Log.d(TAG, "Section " + id);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("Login:", databaseError.toString());
                }
            });

            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.child("score") != null && dataSnapshot.child("verses_memorized") != null) {
                        systemPreferences.edit().putLong("verses_memorized", dataSnapshot.child("verses_memorized").getValue(int.class)).apply();
                        systemPreferences.edit().putInt("score", dataSnapshot.child("score").getValue(int.class)).apply();
                        Log.d(TAG, "UserBible");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("Login:", databaseError.toString());
                }
            });

            userBible.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DBHandler dbHandler = new DBHandler(getApplicationContext());
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String version = snapshot.getKey();
                        Log.d(TAG, "Version:" + version);
                        for (DataSnapshot book : snapshot.getChildren()) {
                            String bookName = book.getKey();
                            Log.d(TAG, "Book Name:" + bookName);
                            for (DataSnapshot chap : book.getChildren()) {
                                int chapNum = Integer.parseInt(chap.getKey());
                                Log.d(TAG, "Chap Num:" + chapNum);
                                for (DataSnapshot verse : chap.getChildren()) {
                                    int verseNum = Integer.parseInt(verse.getKey());
                                    int memory = verse.getValue(int.class);
                                    dbHandler.setVerseMemoryInDBOnly(version, bookName, chapNum, verseNum, memory);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
