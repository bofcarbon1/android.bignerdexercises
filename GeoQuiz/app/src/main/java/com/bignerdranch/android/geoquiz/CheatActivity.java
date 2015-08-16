package com.bignerdranch.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE =
        "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN =
        "com.bignerdranch.android.geoquiz.answer_shown";
    private static final String TAG = "CheatActivity";
    //Challenge #2 - Define a string variable for retaining cheat text value state
    private static final String ANSWER_SHOWN = "shown";
    private boolean mAnswerIsTrue;
    private boolean misAnswerShown;
    private TextView mAnswerTextView;
    //Challenge #3
    private TextView mAPILevelTextView;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    public static boolean wasAnwwerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        //Challenge #3 show API level
        mAPILevelTextView = (TextView)  findViewById(R.id.apilevel_text_view);
        mAPILevelTextView.setText("API Level " + Build.VERSION.SDK_INT);

        //Challenge #2 - if cheat text state was persisted in instnace state
        //set the cheat text answer value to that otherwise set it to the activity resource value

        if (savedInstanceState != null) {
            setAnswerShownResult(savedInstanceState.getBoolean(ANSWER_SHOWN));
        }
        else {
            setAnswerShownResult(false);
        }

        //Added cheat activity
        final Button showAnswer = (Button) findViewById(R.id.show_answer_button);
        showAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Activity
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true);

                //Adding code from later APIs safely
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    int cx = showAnswer.getWidth() / 2;
                    int cy = showAnswer.getHeight() / 2;
                    float radius = showAnswer.getWidth();
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(showAnswer, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mAnswerTextView.setVisibility(View.VISIBLE);
                            showAnswer.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mAnswerTextView.setVisibility(View.VISIBLE);
                    showAnswer.setVisibility(View.INVISIBLE);

                }
            }
        });
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        //Challenge #2 cheat persistance
        misAnswerShown = isAnswerShown;
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cheat, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        //Challenge2 - Retain the state of the cheat option
        //in case the orientation changes
        savedInstanceState.putBoolean(ANSWER_SHOWN, misAnswerShown);
        //Log.i(TAG, "KEY_CHEAT_BOOLEAN: " + KEY_CHEAT_BOOLEAN);
    }
}
