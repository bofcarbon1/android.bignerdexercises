package com.bignerdranch.android.geoquiz;

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
    //Create textview objects
    private TextView mQuestionTextView;
    //Create TAG constants
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
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
                updateQuestion();
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                mCurrentIndex = (mCurrentIndex + 1) % mQuestonBank.length;

                updateQuestion();
            }
        });

        //Challenge extras - make the text view move to the next question
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestonBank.length;

                updateQuestion();
            }
        });

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        updateQuestion();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
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

        if(userPressedTrue == answerIsTrue){
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

}
