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

        RadioGroup questions = findViewById(R.id.RadioGroupQuestions);
        RadioGroup difficulty = findViewById(R.id.RadioGroupDifficulty);
        RadioGroup type = findViewById(R.id.RadioGroupType);

        questions.setOnCheckedChangeListener(new OnCheckedChangeListener());
        difficulty.setOnCheckedChangeListener(new OnCheckedChangeListener());
        type.setOnCheckedChangeListener(new OnCheckedChangeListener());

        updateRadioGroup(questions);
        updateRadioGroup(difficulty);
        updateRadioGroup(type);
    }

    private class OnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            updateRadioGroup(group);
        }
    }

    private void updateRadioGroup(RadioGroup group) {
        int checkedRadioButtonId = group.getCheckedRadioButtonId();
        for (int i = 0; i<group.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) group.getChildAt(i);
            if (radioButton.getId() == checkedRadioButtonId) {
                radioButton.setBackgroundResource(R.drawable.button_border_selected);
            } else {
                radioButton.setBackgroundResource(R.drawable.button_border);
            }
        }
    }

    public void startTrivia(View view) {
        RadioGroup questions = findViewById(R.id.RadioGroupQuestions);
        RadioGroup difficulty = findViewById(R.id.RadioGroupDifficulty);
        RadioGroup type = findViewById(R.id.RadioGroupType);

        RadioButton checkedQuestionsButton = findViewById(questions.getCheckedRadioButtonId());
        RadioButton checkedDifficultyButton = findViewById(difficulty.getCheckedRadioButtonId());
        RadioButton checkedTypeButton = findViewById(type.getCheckedRadioButtonId());

        String questionsButtonText = (String) checkedQuestionsButton.getText();
        String difficultyButtonText = (String) checkedDifficultyButton.getText();
        String typeButtonText = (String) checkedTypeButton.getText();

        Intent intent = new Intent(MenuActivity.this, GamePlayActivity.class);
        intent.putExtra("questions", questionsButtonText);
        intent.putExtra("difficulty", difficultyButtonText);
        intent.putExtra("type", typeButtonText);
        startActivity(intent);
    }
}
