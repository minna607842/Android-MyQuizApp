package com.example.myquizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class MyQuiz extends AppCompatActivity {

	public final static String EXTRA_MYSCORE= "com.example.myquizapp.MYSCORE";
    private ArrayList<String[]> quizSet = new ArrayList<String[]>();

    private TextView scoreText;
    private TextView qText;
    private Button a0Button;
    private Button a1Button;
    private Button a2Button;
    private Button nextButton;

    private int currentQuiz = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_quiz);

        loadQuizSet();

        getViews();

        setQuiz();
    }

    private void showScore(){
    	scoreText.setText("score: " + score + "/" + quizSet.size());
    }

    private void setQuiz(){
        qText.setText(quizSet.get(currentQuiz)[0]);

        ArrayList<String> answers;
        answers = new ArrayList<>();
        for (int i = 1; i <= 3; i++){
            answers.add(quizSet.get(currentQuiz)[i]);
        }
        Collections.shuffle(answers);

        a0Button.setText(answers.get(0));
        a1Button.setText(answers.get(1));
        a2Button.setText(answers.get(2));

        a0Button.setEnabled(true);
	    a1Button.setEnabled(true);
	    a2Button.setEnabled(true);

        nextButton.setEnabled(false);

        showScore();
    }

    public void checkAnswer(View view){
        //answer
        Button clickedButton = (Button) view;
        String clickedAnswer = clickedButton.getText().toString();

        //judge
        if (clickedAnswer.equals(quizSet.get(currentQuiz)[1])){
            clickedButton.setText("〇" + clickedAnswer);
            score++;
        } else {
            clickedButton.setText(("×" + clickedAnswer));
        }
        showScore();

        //button
        a0Button.setEnabled(false);
        a1Button.setEnabled(false);
        a2Button.setEnabled(false);
        nextButton.setEnabled(true);

        //next quiz
        currentQuiz++;

        if (currentQuiz == quizSet.size()){
        	nextButton.setText("Check result");
        }
    }

    public void goNext(View view) {
	    if (currentQuiz == quizSet.size()) {
		    //show result
		    Intent intent = new Intent(this, MyResult.class);
		    intent.putExtra(EXTRA_MYSCORE, score + "/" + quizSet.size());
		    startActivity(intent);
	    } else {
		    setQuiz();
	    }
    }

    	@Override
    	public void onResume() {
    		super.onResume();
    		nextButton.setText("Next");
    		currentQuiz = 0;
    		score= 0;
    		setQuiz();

    }

    private void getViews(){
        scoreText = (TextView) findViewById(R.id.scoreText);
        qText = (TextView) findViewById(R.id.qText);
        a0Button = (Button) findViewById(R.id.a0Button);
        a1Button = (Button) findViewById(R.id.a1Button);
        a2Button = (Button) findViewById(R.id.a2Button);
        nextButton = (Button) findViewById(R.id.nextButton);
    }

    private void loadQuizSet(){
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try{
            inputStream = getAssets().open("quiz.txt");
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String s;
            while ((s = bufferedReader.readLine()) != null){
                quizSet.add(s.split("\t"));
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if (inputStream != null) inputStream.close();
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}