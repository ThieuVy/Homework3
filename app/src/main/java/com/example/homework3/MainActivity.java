package com.example.homework3;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int selectedDigits = 0;
    private RadioGroup digitRadioGroup;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        digitRadioGroup = findViewById(R.id.digit_radio_group);
        startButton = findViewById(R.id.start_button);
    }

    private void setupListeners() {
        // Set up radio group listener
        digitRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.two_digit_button) {
                selectedDigits = 2;
            } else if (checkedId == R.id.three_digit_button) {
                selectedDigits = 3;
            } else if (checkedId == R.id.four_digit_button) {
                selectedDigits = 4;
            }
        });

        startButton.setOnClickListener(v -> startGame());
    }

    private void startGame() {
        if (selectedDigits == 0) {
            Toast.makeText(this, "Please select number of digits first", Toast.LENGTH_SHORT).show();
            return;
        }

        GameFragment gameFragment = GameFragment.newInstance(selectedDigits);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, gameFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void returnToMain() {
        // Clear all fragments from back stack
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // Reset UI state
        resetUIState();
    }

    private void resetUIState() {
        digitRadioGroup.clearCheck();
        selectedDigits = 0;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}