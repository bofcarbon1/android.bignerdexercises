package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    //Create button objects
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mPrevButton;
    private ImageButton mNextButton;
    private Button mCheatButton;
    //Create textview objects
    private TextView mQuestionTextView;
    //Create static final constants
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    //Challenge #2 - persist cheating state
    private static final String IS_CHEATING = "cheating";
    //Create model objects
    private Question[] mQuestonBank = new Question[] {
        new Question(R.string.question_oceans, true),
        new Question(R.string.question_mideast, true),
        new Question(R.string.question_africa, true),
        new Question(R.string.question_americas, true),
        new Question(R.string.question_asia, true),
    };
    //Create assorted variables
    private int mCurrentIndex = 0;
    private boolean[] mIsCheater = {false, false, false, false, false};

    //Overriden functions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log a message
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        //Wire up the button objects to their resource IDs
        //and add listeners with functions to adt on
        mTrueButton = (Button) findViewById(R.id.true_button);

        //Persist the state of the activity
        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            //Chanllenge #2 set boolean is cheating from persisted state
            mIsCheater[mCurrentIndex] = savedInstanceState.getBoolean(IS_CHEATING);

        }

        //Using an anonymous function approach
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                checkAnswer(false);
            }
        });

        //Challenge extra - add a listener for a prev button
        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                //Challenge #2 set the
                //We need to avoid a divide by zero in the modulus
                if(mCurrentIndex == 1) {
                    mCurrentIndex = 0;
                }
                else {
                    if (mCurrentIndex == 0) {
                        mCurrentIndex = mQuestonBank.length - 1;
                    }
                    else {
                        if (mCurrentIndex > 1) {
                            mCurrentIndex = (mCurrentIndex - 1) % mQuestonBank.length;
                        }
                    }
                }
                //Challenge #2 Remove since we are keeping all cheats in an array
                //mIsCheater[mCurrentIndex] = false;

                updateQuestion();
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                mCurrentIndex = (mCurrentIndex + 1) % mQuestonBank.length;
                //Challenge #2 Remove since we are keeping all cheats in an array
                //mIsCheater[mCurrentIndex] = false;

                updateQuestion();
            }
        });

       //Challenge #1 - make the text view move to the next question
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestonBank.length;

                updateQuestion();
            }
        });

        //Added cheat activity
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Start Activity
                //Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestonBank[mCurrentIndex].isAnswerTrue();
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                startActivityForResult(i, REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();
    }

    //Handle the activity result event
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if(data == null) {
                return;
            }
            mIsCheater[mCurrentIndex] = CheatActivity.wasAnwwerShown(data);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        //Challenge2 - Retain the state of the cheat option
        //in case the orientation changes
        savedInstanceState.putBoolean(IS_CHEATING, mIsCheater[mCurrentIndex]);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    //Local functinns

    private void updateQuestion() {

        int question = mQuestonBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestonBank[mCurrentIndex].isAnswerTrue();

        int messageResId;

        if (mIsCheater[mCurrentIndex]) {
            messageResId = R.string.judgement_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

}
