package com.code.codemercenaries.girdthysword;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AnswerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int qNo;
    TextView question;
    TextView answer;
    Button b1;
    Button b2;
    Button b3;
    ImageView i1;

    List<String> questions = new ArrayList<>(Arrays.asList("I'm confused. Where do I get an overview?"
            , "How do I add new sections to memorize?"
            , "How do I know when to review a particular chunk?"
            , "The speech recognition seems to be very inaccurate."
            , "How do I report a bug or request a feature?"));

    List<String> answers = new ArrayList<>(Arrays.asList("First, you'll add a section of the " +
                    "Bible that you want to memorize. Once you press ADD, the app will break down the section " +
                    "into chunks of equal size(you can change the chunk size in the settings menu). Then as " +
                    "you memorize the chunks that are assigned to you daily, you will click on the chunk and " +
                    "review by reciting the verses of that chunk. Depending upon how accurate you recite it, " +
                    "the next date of review will be scheduled. In the Bible which is integrated, the verses " +
                    "you've memorized will appear GREEN, and the verses added for memorization will appear YELLOW.",

            "You can add new sections by pressing the floating button in the bottom right corner of " +
                    "the HOME menu, and clicking on the Add Section button that appears.",

            "The app uses a cognitive model for spacing out time between subsequent reviews, and it " +
                    "automatically calculates when to schedule the next date of review and displays " +
                    "in the TODAY list on that particular day. So you don't have remember when to " +
                    "review a particular chunk. Anyways if you want to check when a chunk is scheduled to, " +
                    "you can go to the ALL tab which displays all the chunks along with the next " +
                    "date of review",

            "GirdThySword uses Google's speech API for speech recognition which is not very accurate " +
                    "with archaic words that are in the Bible. That being said there are some tips " +
                    "that you can use inorder to get the best possible accuracy.\n1.Review when " +
                    "connected to fast Internet such as WiFi or 4G data. This makes the speech engine " +
                    "to send the recording to the cloud for more rigorous processing.\n2.Recite the " +
                    "verses in a consistent and slow pace, with your own voice. The speech recognition " +
                    "is trained to your voice when you use voice typing or the Google Assistant in your Phone. " +
                    "Therefore it will understand your voice much easier than it understands someone " +
                    "else's who is using your phone.",

            "You can send a feedback or report a bug by clicking on the buttons in the About menu or" +
                    " down below."));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String SETTINGS_PREF = "settings_pref";
        if (getSharedPreferences(SETTINGS_PREF, 0).getString("font", getString(R.string.default_font_name)).equals(getString(R.string.default_font_name))) {
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath(getString(R.string.default_font))
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );
        } else if (getSharedPreferences(SETTINGS_PREF, 0).getString("font", getString(R.string.default_font_name)).equals(getString(R.string.coolvetica_font_name))) {
            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath(getString(R.string.coolvetica_font))
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );
        }
        setContentView(R.layout.activity_answer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        MenuItem tools = menu.findItem(R.id.nav_title_about);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.whiteText), 0, s.length(), 0);
        tools.setTitle(s);

        question = (TextView) findViewById(R.id.question);
        answer = (TextView) findViewById(R.id.answer);
        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        i1 = (ImageView) findViewById(R.id.image1);

        qNo = getIntent().getIntExtra("EXTRA_QUES_ID", -1);
        switch (qNo) {
            case 1:
                question.setText(questions.get(qNo - 1));
                answer.setText(answers.get(qNo - 1));
                b1.setVisibility(View.INVISIBLE);
                b2.setVisibility(View.INVISIBLE);
                b3.setVisibility(View.INVISIBLE);
                i1.setVisibility(View.INVISIBLE);
                break;
            case 2:
                question.setText(questions.get(qNo - 1));
                answer.setText(answers.get(qNo - 1));
                b1.setVisibility(View.INVISIBLE);
                b2.setVisibility(View.INVISIBLE);
                b3.setVisibility(View.INVISIBLE);
                i1.setVisibility(View.VISIBLE);
                try {
                    InputStream ims = getAssets().open("add.jpg");
                    // load image as Drawable
                    Drawable d = Drawable.createFromStream(ims, null);
                    // set image to ImageView
                    i1.setImageDrawable(d);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                i1.setScaleX(2);
                i1.setScaleY(2);
                break;
            case 3:
                question.setText(questions.get(qNo - 1));
                answer.setText(answers.get(qNo - 1));
                b1.setVisibility(View.INVISIBLE);
                b2.setVisibility(View.INVISIBLE);
                b3.setVisibility(View.INVISIBLE);
                i1.setVisibility(View.VISIBLE);
                try {
                    InputStream ims = getAssets().open("all.jpg");
                    // load image as Drawable
                    Drawable d = Drawable.createFromStream(ims, null);
                    // set image to ImageView
                    i1.setImageDrawable(d);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                i1.setScaleX(2);
                i1.setScaleY(2);
                break;
            case 4:
                question.setText(questions.get(qNo - 1));
                answer.setText(answers.get(qNo - 1));
                b1.setVisibility(View.INVISIBLE);
                b2.setVisibility(View.INVISIBLE);
                b3.setVisibility(View.INVISIBLE);
                i1.setVisibility(View.INVISIBLE);
                break;
            case 5:
                question.setText(questions.get(qNo - 1));
                answer.setText(answers.get(qNo - 1));
                b1.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                b3.setVisibility(View.INVISIBLE);
                i1.setVisibility(View.INVISIBLE);
                b1.setText("Report a Bug");
                b2.setText("Send Feedback");

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "codemercenaries@gmail.com"));
                            intent.putExtra(Intent.EXTRA_SUBJECT, "[GirdThySword-Android] Bug Report");
                            intent.putExtra(Intent.EXTRA_TEXT, "");
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(AnswerActivity.this, "No application can handle this request."
                                    + "Please install an email client", Toast.LENGTH_LONG);
                            e.printStackTrace();
                        }
                    }
                });

                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "codemercenaries@gmail.com"));
                            intent.putExtra(Intent.EXTRA_SUBJECT, "[GirdThySword-Android] Feedback");
                            intent.putExtra(Intent.EXTRA_TEXT, "");
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(AnswerActivity.this, "No application can handle this request."
                                    + "Please install an email client", Toast.LENGTH_LONG);
                            e.printStackTrace();
                        }
                    }
                });
                break;
            default:
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.answer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(AnswerActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(AnswerActivity.this, HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bible) {
            Intent intent = new Intent(AnswerActivity.this, BibleActivity.class);
            startActivity(intent);
        } /*else if (id == R.id.nav_rewards) {
            Intent intent = new Intent(ChapterListActivity.this,RewardsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_statistics) {
            Intent intent = new Intent(ChapterListActivity.this,StatsActivity.class);
            startActivity(intent);
        }*/ else if (id == R.id.nav_profile) {
            Intent intent = new Intent(AnswerActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(AnswerActivity.this, HelpActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(AnswerActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(AnswerActivity.this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
