package com.example.stefan.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Find the radio button groups used to select the game modes
        RadioGroup questions = findViewById(R.id.RadioGroupQuestions);
        RadioGroup difficulty = findViewById(R.id.RadioGroupDifficulty);
        RadioGroup type = findViewById(R.id.RadioGroupType);

        // Add listeners to the radio button groups
        questions.setOnCheckedChangeListener(new OnCheckedChangeListener());
        difficulty.setOnCheckedChangeListener(new OnCheckedChangeListener());
        type.setOnCheckedChangeListener(new OnCheckedChangeListener());

        // Show which button is pressed
        updateRadioGroup(questions);
        updateRadioGroup(difficulty);
        updateRadioGroup(type);
    }

    private class OnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // If the selected radio button is changed update what button is visible as clicked
            updateRadioGroup(group);
        }
    }

    private void updateRadioGroup(RadioGroup group) {
        // Get what button is checked
        int checkedRadioButtonId = group.getCheckedRadioButtonId();

        // Loop over all buttons in the radio button group
        for (int i = 0; i<group.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) group.getChildAt(i);

            if (radioButton.getId() == checkedRadioButtonId) {
                // If the button was the checked button set the background to the selected button layout
                radioButton.setBackgroundResource(R.drawable.button_border_selected);
            } else {
                // Otherwise set the background to the standard button layout
                radioButton.setBackgroundResource(R.drawable.button_border);
            }
        }
    }

    public void startTrivia(View view) {
        // Find the radio button groups used to select the game modes
        RadioGroup questions = findViewById(R.id.RadioGroupQuestions);
        RadioGroup difficulty = findViewById(R.id.RadioGroupDifficulty);
        RadioGroup type = findViewById(R.id.RadioGroupType);

        // Get the selected game options radio button
        RadioButton checkedQuestionsButton = findViewById(questions.getCheckedRadioButtonId());
        RadioButton checkedDifficultyButton = findViewById(difficulty.getCheckedRadioButtonId());
        RadioButton checkedTypeButton = findViewById(type.getCheckedRadioButtonId());

        // Get the text from the selected buttons to determine what game options were selected
        String questionsButtonText = (String) checkedQuestionsButton.getText();
        String difficultyButtonText = (String) checkedDifficultyButton.getText();
        String typeButtonText = (String) checkedTypeButton.getText();

        // Start an intent to the GamePlayActivity with the game options as extras
        Intent intent = new Intent(MenuActivity.this, GamePlayActivity.class);
        intent.putExtra("number of questions", questionsButtonText);
        intent.putExtra("difficulty", difficultyButtonText);
        intent.putExtra("type", typeButtonText);
        startActivity(intent);
    }
}
