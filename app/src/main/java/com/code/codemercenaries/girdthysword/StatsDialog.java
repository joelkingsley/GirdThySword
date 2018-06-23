package com.code.codemercenaries.girdthysword;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.code.codemercenaries.girdthysword.Database.DBHandler;
import com.daimajia.numberprogressbar.NumberProgressBar;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

/**
 * Created by Joel Kingsley on 25-05-2018.
 */

public class StatsDialog extends Dialog implements android.view.View.OnClickListener {

    Activity mActivity;
    ImageButton button;
    NumberProgressBar vProgress, cProgress, bProgress;
    TextView verses, chapters, books;
    String mVersion;

    public StatsDialog(@NonNull Activity activity, String version) {
        super(activity);
        mActivity = activity;
        mVersion = version;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.stats_dialog);

        vProgress = findViewById(R.id.verses_progress);
        cProgress = findViewById(R.id.chapters_progress);
        bProgress = findViewById(R.id.books_progress);

        verses = findViewById(R.id.nVerses);
        chapters = findViewById(R.id.nChapters);
        books = findViewById(R.id.nBooks);

        button = findViewById(R.id.close_button);
        button.setOnClickListener(this);

        DBHandler dbHandler = new DBHandler(mActivity);
        int nVerses = dbHandler.getTotalNumberOfVersesMemorized(mVersion);
        int nChapters = dbHandler.getTotalNumberOfChaptersMemorized(mVersion);
        int nBooks = dbHandler.getTotalNumberOfBooksMemorized(mVersion);

        int textSize1 = mActivity.getResources().getDimensionPixelSize(R.dimen.stats_large);
        int textSize2 = mActivity.getResources().getDimensionPixelSize(R.dimen.stats_small);

        String v1Text = Integer.toString(nVerses);
        String v2Text = Integer.toString(31102);
        String c1Text = Integer.toString(nChapters);
        String c2Text = Integer.toString(1189);
        String b1Text = Integer.toString(nBooks);
        String b2Text = Integer.toString(66);

        SpannableString span1 = new SpannableString(v1Text);
        span1.setSpan(new AbsoluteSizeSpan(textSize1), 0, v1Text.length(), SPAN_INCLUSIVE_INCLUSIVE);
        SpannableString span2 = new SpannableString(v2Text);
        span2.setSpan(new AbsoluteSizeSpan(textSize2), 0, v2Text.length(), SPAN_INCLUSIVE_INCLUSIVE);
        SpannableString span3 = new SpannableString(c1Text);
        span3.setSpan(new AbsoluteSizeSpan(textSize1), 0, c1Text.length(), SPAN_INCLUSIVE_INCLUSIVE);
        SpannableString span4 = new SpannableString(c2Text);
        span4.setSpan(new AbsoluteSizeSpan(textSize2), 0, c2Text.length(), SPAN_INCLUSIVE_INCLUSIVE);
        SpannableString span5 = new SpannableString(b1Text);
        span5.setSpan(new AbsoluteSizeSpan(textSize1), 0, b1Text.length(), SPAN_INCLUSIVE_INCLUSIVE);
        SpannableString span6 = new SpannableString(b2Text);
        span6.setSpan(new AbsoluteSizeSpan(textSize2), 0, b2Text.length(), SPAN_INCLUSIVE_INCLUSIVE);

        verses.setText(TextUtils.concat(span1, "/", span2));
        chapters.setText(TextUtils.concat(span3, "/", span4));
        books.setText(TextUtils.concat(span5, "/", span6));

        vProgress.setMax(100);
        cProgress.setMax(100);
        bProgress.setMax(100);

        vProgress.setProgress(nVerses / 31102);
        cProgress.setProgress(nChapters / 1189);
        bProgress.setProgress(nBooks / 66);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_button:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
