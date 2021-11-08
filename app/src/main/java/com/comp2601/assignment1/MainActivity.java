package com.comp2601.assignment1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG =  MainActivity.class.getSimpleName();
    private static final int RESULT = 0;
    public static final String EMAIL_KEY = "EMAIL_KEY";
    public static final String EXAM_KEY = "EXAM_KEY";

    //all the buttons
    private Button mButtonA;
    private Button mButtonB;
    private Button mButtonC;
    private Button mButtonD;
    private Button mButtonE;
    private Button mNextButton;
    private Button mPrevButton;
    private Button msubmitButton;

    private TextView mQuestionTextView;
    private ArrayList<Question> questions; //Have an arraylist of Questions, and each Question has 5 options and an answer

    private int mCurrentQuestionIndex; //used to determine which question user is on
    private static String QUESTION_INDEX_KEY = "question_index";

    private int[] buttons = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //used to determine which answer user has selected for each of the 10 questions
    private String emailString = "";
    //private int mCurrentSelectedButton; //used to determine which button is selected by the user at current question index
    //private static String BUTTON_KEY = "button";
    //private boolean gameHasStarted = false; //something is wrong here...

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //DO I NEED THIS METHOD???
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //set and inflate UI to manage

        mButtonA = (Button) findViewById(R.id.a_button);
        mButtonB = (Button) findViewById(R.id.b_button);
        mButtonC = (Button) findViewById(R.id.c_button);
        mButtonD = (Button) findViewById(R.id.d_button);
        mButtonE = (Button) findViewById(R.id.e_button);
        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);
        msubmitButton = (Button) findViewById(R.id.submit_button);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mButtonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Button A Clicked"); //print to console for debugging

                questions.get(mCurrentQuestionIndex).setButton(1); //'get' returns the element at the specified position in this list
                //mCurrentSelectedButton = 1;
                buttons[mCurrentQuestionIndex] = 1;
                buttonColour();
            }
        });
        mButtonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Button B Clicked"); //print to console for debugging

                questions.get(mCurrentQuestionIndex).setButton(2);
                buttons[mCurrentQuestionIndex] = 2;
                buttonColour();
            }
        });
        mButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Button C Clicked"); //print to console for debugging

                questions.get(mCurrentQuestionIndex).setButton(3);
                buttons[mCurrentQuestionIndex] = 3;
                buttonColour();
            }
        });
        mButtonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Button D Clicked"); //print to console for debugging

                questions.get(mCurrentQuestionIndex).setButton(4);
                buttons[mCurrentQuestionIndex] = 4;
                buttonColour();
            }
        });
        mButtonE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Button E Clicked"); //print to console for debugging

                questions.get(mCurrentQuestionIndex).setButton(5);
                buttons[mCurrentQuestionIndex] = 5;
                buttonColour();
            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCurrentQuestionIndex++;
                if(mCurrentQuestionIndex>=questions.size()) mCurrentQuestionIndex = 0; //goes back to first question
                mQuestionTextView.setText("" + (mCurrentQuestionIndex+1) + ") " + questions.get(mCurrentQuestionIndex).toString());
                buttonColour();
            }
        });
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentQuestionIndex--;
                if(mCurrentQuestionIndex<0) mCurrentQuestionIndex = questions.size() - 1; //goes to last question
                mQuestionTextView.setText("" + (mCurrentQuestionIndex+1) + ") " + questions.get(mCurrentQuestionIndex).toString());
                buttonColour();
            }
        });
        msubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Submit Button Clicked"); //print to console for debugging
                Intent intent = new Intent(MainActivity.this, Submission.class); //goes to a new page
                Log.i(TAG, "Submit Button Clicked"); //print to console for debugging
                intent.putExtra(EMAIL_KEY, emailString);

                intent.putExtra(EXAM_KEY, buttons); //have different keys
                startActivityForResult(intent, RESULT);
                //startActivity(intent);
            }
        });

        //Initialise Data Model objects
        //questions = Question.exampleSet1();
        questions = null;

        mCurrentQuestionIndex = 0;

        //Try to read resource data file with questions
        Exam emailObject = new Exam();
        ArrayList<Question> parsedModel = null;
        try {
            InputStream iStream = getResources().openRawResource(R.raw.comp2601exam);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(iStream));
            //ArrayList<Question> parsedModel = Exam.parseFrom(bs);
            //emailString = Exam.pullParseFrom(bReader);
            parsedModel = Exam.pullParseFrom(bReader); //calls this static method to parse the data and create the exam questions
            emailString = emailObject.getEmail(); //gets the email text
            bReader.close();
        }
        catch (java.io.IOException e){
            e.printStackTrace();
        }
        if(parsedModel == null || parsedModel.isEmpty())
            Log.i(TAG, "ERROR: Questions Not Parsed");
        questions = parsedModel;
        Log.i(TAG, "Email is: " + emailString);

        if(questions != null && questions.size() > 0)
            mQuestionTextView.setText("" + (mCurrentQuestionIndex + 1) + ") " +
                    questions.get(mCurrentQuestionIndex).toString());

        if(savedInstanceState != null){ //check for the possiblity that there is no saved state information
            mCurrentQuestionIndex = savedInstanceState.getInt(QUESTION_INDEX_KEY, 0);
            buttons = (int[]) savedInstanceState.getSerializable("arr");
            //mCurrentSelectedButton = savedInstanceState.getInt(BUTTON_KEY, 0);
            mQuestionTextView.setText("" + (mCurrentQuestionIndex+1) + ") " + questions.get(mCurrentQuestionIndex).toString());
            Log.i(TAG, "Current question index: " + mCurrentQuestionIndex);
            //Log.i(TAG, "Current button selected: " + questions.get(mCurrentQuestionIndex).getButton());
            Log.i(TAG, "Current button selected: " + buttons[mCurrentQuestionIndex]);
            buttonColour();
        }

        /*if(gameHasStarted==true){
            mQuestionTextView.setText("" + (mCurrentQuestionIndex+1) + ") " + questions.get(mCurrentQuestionIndex).toString());
            Log.i(TAG, "Current question index: " + mCurrentQuestionIndex);
            Log.i(TAG, "Current button selected: " + questions.get(mCurrentQuestionIndex).getButton());
            if(questions.get(mCurrentQuestionIndex).getButton() > 0){
                buttonColour();
            }
        }*/
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){ //the state is restored in onCreate()
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(QUESTION_INDEX_KEY, mCurrentQuestionIndex); //saves the current question user is on
        savedInstanceState.putSerializable("arr", buttons); //saves all of the user's selected answers
        //savedInstanceState.putInt(BUTTON_KEY, mCurrentSelectedButton);
        Log.i(TAG, "onSaveInstanceState(Bundle)");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(TAG, "onStart()");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(TAG, "onStop()");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i(TAG, "onRestart()");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }

    private void buttonColour(){
        buttonClear();

        /*if(questions.get(mCurrentQuestionIndex).getButton()==1){
            mButtonA.setBackgroundColor(Color.BLUE);
        }
        if(questions.get(mCurrentQuestionIndex).getButton()==2){
            mButtonB.setBackgroundColor(Color.BLUE);
        }
        if(questions.get(mCurrentQuestionIndex).getButton()==3){
            mButtonC.setBackgroundColor(Color.BLUE);
        }
        if(questions.get(mCurrentQuestionIndex).getButton()==4){
            mButtonD.setBackgroundColor(Color.BLUE);
        }
        if(questions.get(mCurrentQuestionIndex).getButton()==5){
            mButtonE.setBackgroundColor(Color.BLUE);
        }*/

        if(buttons[mCurrentQuestionIndex]==1){
            mButtonA.setBackgroundColor(Color.BLUE);
        }
        if(buttons[mCurrentQuestionIndex]==2){
            mButtonB.setBackgroundColor(Color.BLUE);
        }
        if(buttons[mCurrentQuestionIndex]==3){
            mButtonC.setBackgroundColor(Color.BLUE);
        }
        if(buttons[mCurrentQuestionIndex]==4){
            mButtonD.setBackgroundColor(Color.BLUE);
        }
        if(buttons[mCurrentQuestionIndex]==5){
            mButtonE.setBackgroundColor(Color.BLUE);
        }
    }

    private void buttonClear(){
        mButtonA.setBackgroundColor(Color.GRAY);
        mButtonB.setBackgroundColor(Color.GRAY);
        mButtonC.setBackgroundColor(Color.GRAY);
        mButtonD.setBackgroundColor(Color.GRAY);
        mButtonE.setBackgroundColor(Color.GRAY);
    }
}
