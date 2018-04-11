/*
  Written by Vlad Shaternik
  4/8/2018

  This class manages changes in the "Programming Language Selection" app. Managing goes within the
  following xml files: activity_main.xml, single_question.xml, results.xml.
 */
package com.example.android.programminglanguageselection;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /**
     * VARIABLES LIST:
     * isFirstEntry  : whether this is the first enter to questions part (after welcoming text)
     * questionCount : current question
     * questionsAmt  : total amount of questions
     * outcome1      : current amount of points toward first outcome
     * outcome2      : current amount of points toward second outcome
     * outcome3      : current amount of points toward third outcome
     * outcome4      : current amount of points toward fourth outcome
     * outcome5      : current amount of points toward fifth outcome
     */
    boolean isFirstEntry = true;
    int questionCount = 1;
    int questionsAmt = 8;
    int outcome1 = 0;
    int outcome2 = 0;
    int outcome3 = 0;
    int outcome4 = 0;
    int outcome5 = 0;

    /**
     * This function is used to initialise the scene.
     *
     * @param savedInstanceState - allows start an app with the saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This function is used to initialize next question after the user selects an answer. The
     * function handles the very first question, and the last question, which require switch of the
     * activities.
     *
     * @param view - used because it will be called from the xml file (onClick attribute)
     */
    public void nextQuestion(View view) {

        /*
          PROCESSING - check whether it is a first enter to the test. If yes then set isFirstEntry
          to false (so we do not come back to this state after each question), switch to the
          test activity (single_question), make image covering test activity GONE, and draw the
          first question.

          If this is not the first entry then check whether current question number is less then
          total amount of questions.

          In other case it means that there is no more questions
         */
        if (isFirstEntry) {
            // PROCESSING - set isFirstEntry to false (so we do not come back to this state after
            // each question)
            isFirstEntry = false;

            // PROCESSING - switch to the test activity (single_question)
            setContentView(R.layout.single_question);

            // PROCESSING - get and hide the image covering the activity
            ImageView imageView = findViewById(R.id.thinking_image_view);

            imageView.setVisibility(ImageView.GONE);

            // OUTPUT - draw the first question
            drawTest();
        } else if (questionCount < questionsAmt) {
            // PROCESSING - get all of the radio buttons
            RadioButton answer1 = findViewById(R.id.answer1);
            RadioButton answer2 = findViewById(R.id.answer2);
            RadioButton answer3 = findViewById(R.id.answer3);
            RadioButton answer4 = findViewById(R.id.answer4);

            // PROCESSING - check which button is checked
            if (answer1.isChecked()) {
                // PROCESSING - uncheck the button and calculate outcome
                answer1.setChecked(false);
                calcAnswer(1);
            } else if (answer2.isChecked()) {
                // PROCESSING - uncheck the button and calculate outcome
                answer2.setChecked(false);
                calcAnswer(2);
            } else if (answer3.isChecked()) {
                // PROCESSING - uncheck the button and calculate outcome
                answer3.setChecked(false);
                calcAnswer(3);
            } else {
                // PROCESSING - uncheck the button and calculate outcome
                answer4.setChecked(false);
                calcAnswer(4);
            }

            // INCREMENT - current question
            questionCount++;

            // OUTPUT - draw the current question
            drawTest();
        } else {
            // PROCESSING - get all of the radio buttons
            RadioButton answer1 = findViewById(R.id.answer1);
            RadioButton answer2 = findViewById(R.id.answer2);
            RadioButton answer3 = findViewById(R.id.answer3);
            RadioButton answer4 = findViewById(R.id.answer4);

            // PROCESSING - create a new toast
            Toast thinkingToast = Toast.makeText(getApplicationContext(), R.string.thinking_hmmm, Toast.LENGTH_LONG);

            // PROCESSING - get the image covering the activity
            ImageView imageView = findViewById(R.id.thinking_image_view);

            // PROCESSING - set all buttons to GONE
            answer1.setVisibility(RadioButton.GONE);
            answer2.setVisibility(RadioButton.GONE);
            answer3.setVisibility(RadioButton.GONE);
            answer4.setVisibility(RadioButton.GONE);

            // PROCESSING - set the image covering the activity VISIBLE
            imageView.setVisibility(ImageView.VISIBLE);

            // PROCESSING - disable the image (so user cannot click on it and interrupt toasts work)
            imageView.setEnabled(false);

            // OUTPUT - thinking toasts
            thinkingToast.show();

            thinkingToast = Toast.makeText(getApplicationContext(), R.string.thinking_interesting, Toast.LENGTH_SHORT);
            thinkingToast.show();

            thinkingToast = Toast.makeText(getApplicationContext(), R.string.thinking_maybe, Toast.LENGTH_SHORT);
            thinkingToast.show();

            thinkingToast = Toast.makeText(getApplicationContext(), R.string.thinking_result_ready, Toast.LENGTH_SHORT);
            thinkingToast.show();

            // PROCESSING - wait until all of the toasts displayed, then change activity, and draw
            // result
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // PROCESSING - change activity
                    setContentView(R.layout.results);

                    // PROCESSING - draw result
                    drawResult();
                }
            }, 10000);

        }
    }

    /**
     * This function will set all everything (variables and display the welcoming view)
     * to the initial values (so user can start the test again)
     *
     * @param view - used because it will be called from the xml file (onClick attribute)
     */
    public void returnToMain(View view) {
        // PROCESSING - set all of the variables to their initial values
        isFirstEntry = true;
        questionCount = 1;
        questionsAmt = 8;
        outcome1 = 0;
        outcome2 = 0;
        outcome3 = 0;
        outcome4 = 0;
        outcome5 = 0;

        // PROCESSING - change the activity
        setContentView(R.layout.activity_main);
    }

    /**
     * This function will show a result to the user (based on user's answers)
     */
    private void drawResult() {
        TextView answerTitle = findViewById(R.id.answer_title);
        TextView answerDesc = findViewById(R.id.answer_description);
        TextView answerLink = findViewById(R.id.language_link);
        ImageView answerImg = findViewById(R.id.answer_img);

        // PROCESSING - compare points of different outcomes and determine which outcome has
        // greatest amount of points
        int bestOption = greatestOfFive(outcome1, outcome2, outcome3, outcome4, outcome5);

        // OUTPUT - based on the outcome display the result
        switch (bestOption) {
            case 1:
                answerTitle.setText(R.string.system_programmer);
                answerDesc.setText(R.string.system_programmer_result);
                answerLink.setText(R.string.system_programmer_link);
                answerImg.setImageResource(R.drawable.system_programmer);
                break;
            case 2:
                answerTitle.setText(R.string.php_programmer);
                answerDesc.setText(R.string.php_programmer_result);
                answerLink.setText(R.string.php_programmer_link);
                answerImg.setImageResource(R.drawable.php_programmer);
                break;
            case 3:
                answerTitle.setText(R.string.front_end);
                answerDesc.setText(R.string.front_end_result);
                answerLink.setText(R.string.front_end_link);
                answerImg.setImageResource(R.drawable.js);
                break;
            case 4:
                answerTitle.setText(R.string.java_programmer);
                answerDesc.setText(R.string.java_programmer_result);
                answerLink.setText(R.string.java_programmer_link);
                answerImg.setImageResource(R.drawable.java);
                break;
            case 5:
                answerTitle.setText(R.string.python_programmer);
                answerDesc.setText(R.string.python_programmer_result);
                answerLink.setText(R.string.python_programmer_link);
                answerImg.setImageResource(R.drawable.python);
                break;
        }
    }

    /**
     * This function will determine which outcome has the largest amount of points.
     *
     * @param pts1 - points of the first outcome
     * @param pts2 - points of the second outcome
     * @param pts3 - points of the third outcome
     * @param pts4 - points of the fourth outcome
     * @param pts5 - points of the fifth outcome
     * @return number of the outcome
     */
    private int greatestOfFive(int pts1, int pts2, int pts3, int pts4, int pts5) {
        /*
           VARIABLES LIST:
           answer   : number of the outcome
           greatest : temporary value of the greatest amount of points
         */
        int answer = 1;
        int greatest = pts1;

        // PROCESSING - get the number of the outcome by comparing amount of points in each outcome
        if (pts2 > greatest) {
            greatest = pts2;
            answer = 2;
        }
        if (pts3 > greatest) {
            greatest = pts3;
            answer = 3;
        }
        if (pts4 > greatest) {
            greatest = pts4;
            answer = 4;
        }
        if (pts5 > greatest) {
            answer = 5;
        }

        return answer;
    }

    /**
     * This function calculates toward which outcome each user's answer goes
     *
     * @param answerNum - number of the answer
     */
    private void calcAnswer(int answerNum) {


        // PROCESSING - this switch checks the number of the current question (because each question
        // will have different answers and each answer goes toward different outcome)
        switch (questionCount) {
            // First question
            case 1: {
                // PROCESSING - this switch checks which answer was chosen and adds points toward
                // corresponding outcome
                switch (answerNum) {
                    case 1: {
                        outcome1++;
                        break;
                    }
                    case 2: {
                        outcome4++;
                        break;
                    }
                    case 3: {
                        outcome2++;
                        outcome3++;
                        break;
                    }
                    case 4: {
                        outcome5++;
                        break;
                    }
                }
                break;
            }
            // Second question
            case 2: {
                // PROCESSING - this switch checks which answer was chosen and adds points toward
                // corresponding outcome
                switch (answerNum) {
                    case 1: {
                        outcome5++;
                        break;
                    }
                    case 2: {
                        outcome1++;
                        break;
                    }
                    case 3: {
                        outcome2++;
                        outcome3++;
                        break;
                    }
                    case 4: {
                        outcome4++;
                        break;
                    }
                }
                break;
            }
            // Third question
            case 3: {
                // PROCESSING - this switch checks which answer was chosen and adds points toward
                // corresponding outcome
                switch (answerNum) {
                    case 1: {
                        outcome4++;
                        break;
                    }
                    case 2: {
                        outcome3++;
                        break;
                    }
                    case 3: {
                        outcome2++;
                        outcome3++;
                        outcome4++;
                        outcome5++;
                        break;
                    }
                    case 4: {
                        outcome2++;
                        outcome3++;
                        break;
                    }
                }
                break;
            }
            // Fourth question
            case 4: {
                // PROCESSING - this switch checks which answer was chosen and adds points toward
                // corresponding outcome
                switch (answerNum) {
                    case 1: {
                        outcome2++;
                        outcome3++;
                        break;
                    }
                    case 2: {
                        outcome1++;
                        outcome4++;
                        break;
                    }
                    case 3: {
                        outcome4++;
                        break;
                    }
                    case 4: {
                        outcome5++;
                        break;
                    }
                }
                break;
            }
            // Fifth question
            case 5: {
                // PROCESSING - this switch checks which answer was chosen and adds points toward
                // corresponding outcome
                switch (answerNum) {
                    case 1: {
                        outcome1++;
                        break;
                    }
                    case 2: {
                        outcome2++;
                        outcome3++;
                        outcome5++;
                        break;
                    }
                    case 3: {
                        outcome4++;
                        break;
                    }
                    case 4: {
                        outcome5++;
                        break;
                    }
                }
                break;
            }
            // Sixth question
            case 6: {
                // PROCESSING - this switch checks which answer was chosen and adds points toward
                // corresponding outcome
                switch (answerNum) {
                    case 1: {
                        outcome3++;
                        break;
                    }
                    case 2: {
                        outcome1++;
                        outcome4++;
                        break;
                    }
                    case 3: {
                        outcome2++;
                        break;
                    }
                    case 4: {
                        outcome5++;
                        break;
                    }
                }
                break;
            }
            // Seventh question
            case 7: {
                // PROCESSING - this switch checks which answer was chosen and adds points toward
                // corresponding outcome
                switch (answerNum) {
                    case 1: {
                        break;
                    }
                    case 2: {
                        outcome4++;
                        break;
                    }
                    case 3: {
                        outcome5++;
                        break;
                    }
                    case 4: {
                        outcome1++;
                        break;
                    }
                }
                break;
            }
            // Eighth question
            case 8: {
                // PROCESSING - this switch checks which answer was chosen and adds points toward
                // corresponding outcome
                switch (answerNum) {
                    case 1: {
                        outcome2++;
                        outcome3++;
                        outcome5++;
                        break;
                    }
                    case 2: {
                        outcome1++;
                        outcome4++;
                        break;
                    }
                    case 3: {
                        break;
                    }
                    case 4: {
                        outcome5++;
                        break;
                    }
                }
                break;
            }
        }
    }

    /**
     * This function draws the single question
     */
    @SuppressLint("StringFormatInvalid")
    private void drawTest() {
        // PROCESSING - get the TextView of the question number tracking and title of the question
        TextView questionNumTrack = findViewById(R.id.questionCount);
        TextView questionTitle = findViewById(R.id.questionTitle);

        // PROCESSING - get radio buttons
        RadioButton answer1 = findViewById(R.id.answer1);
        RadioButton answer2 = findViewById(R.id.answer2);
        RadioButton answer3 = findViewById(R.id.answer3);
        RadioButton answer4 = findViewById(R.id.answer4);

        // PROCESSING - get the layout of the question activity
        ConstraintLayout question = findViewById(R.id.question);

        // PROCESSING - start default animation
        TransitionManager.beginDelayedTransition(question);

        // OUTPUT - draw the question number tracking text
        questionNumTrack.setText(getString(R.string.question_count, questionCount, questionsAmt));

        // OUTPUT - draw the title of the question
        questionTitle.setText(
                getResources().getText(
                        getResources().getIdentifier(
                                "question_" + questionCount,
                                "string",
                                "com.example.android.programminglanguageselection"
                        )
                )
        );

        // OUTPUT - draw the first answer
        answer1.setText(getResources().getText(
                getResources().getIdentifier(
                        "q_" + questionCount + "_answer_1",
                        "string",
                        "com.example.android.programminglanguageselection"
                )
        ));

        // OUTPUT - draw the second answer
        answer2.setText(getResources().getText(
                getResources().getIdentifier(
                        "q_" + questionCount + "_answer_2",
                        "string",
                        "com.example.android.programminglanguageselection"
                )
        ));

        // OUTPUT - draw the third answer
        answer3.setText(getResources().getText(
                getResources().getIdentifier(
                        "q_" + questionCount + "_answer_3",
                        "string",
                        "com.example.android.programminglanguageselection"
                )
        ));

        // OUTPUT - draw the fourth answer
        answer4.setText(getResources().getText(
                getResources().getIdentifier(
                        "q_" + questionCount + "_answer_4",
                        "string",
                        "com.example.android.programminglanguageselection"
                )
        ));
    }


}
