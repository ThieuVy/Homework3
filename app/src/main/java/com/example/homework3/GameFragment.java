package com.example.homework3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class GameFragment extends Fragment {

    private static final String ARG_DIGITS = "digits";
    private static final int MAX_ATTEMPTS = 10;

    private int numberOfDigits;
    private int targetNumber;
    private int remainingAttempts = MAX_ATTEMPTS;
    private final ArrayList<Integer> guesses = new ArrayList<>();

    private TextView lastGuessText;
    private TextView remainingAttemptsText;
    private TextView feedbackText;
    private TextInputEditText guessInput;
    private Button confirmButton;

    public static GameFragment newInstance(int digits) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DIGITS, digits);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            numberOfDigits = getArguments().getInt(ARG_DIGITS);
        }
        targetNumber = generateRandomNumber(numberOfDigits);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        initializeViews(view);
        setupInitialState();
        setupClickListeners();

        return view;
    }

    private void initializeViews(View view) {
        lastGuessText = view.findViewById(R.id.last_guess_text);
        remainingAttemptsText = view.findViewById(R.id.remaining_attempts_text);
        feedbackText = view.findViewById(R.id.feedback_text);
        guessInput = view.findViewById(R.id.guess_input);
        confirmButton = view.findViewById(R.id.confirm_button);
    }

    @SuppressLint("SetTextI18n")
    private void setupInitialState() {
        lastGuessText.setText("Your last guess: -");
        remainingAttemptsText.setText("Remaining attempts: " + remainingAttempts);
        feedbackText.setText("Enter a " + numberOfDigits + "-digit number");
    }

    private void setupClickListeners() {
        confirmButton.setOnClickListener(v -> processGuess());
    }

    @SuppressLint("SetTextI18n")
    private void processGuess() {
        String guessStr = Objects.requireNonNull(guessInput.getText()).toString().trim();

        // Validate input
        String validationError = validateInput(guessStr);
        if (validationError != null) {
            feedbackText.setText(validationError);
            return;
        }

        int guess = Integer.parseInt(guessStr);

        // Process the guess
        guesses.add(guess);
        remainingAttempts--;

        updateUI(guess);

        // Check game end conditions
        if (guess == targetNumber) {
            handleGameWin();
        } else if (remainingAttempts == 0) {
            handleGameLoss();
        } else {
            provideFeedback(guess);
        }

        guessInput.setText("");
    }

    private String validateInput(String input) {
        if (input.isEmpty()) {
            return "Please enter a number";
        }

        if (input.length() != numberOfDigits) {
            return "Please enter a " + numberOfDigits + "-digit number";
        }

        try {
            int number = Integer.parseInt(input);
            // Check for leading zeros
            if (String.valueOf(number).length() != numberOfDigits) {
                return "Please enter a valid " + numberOfDigits + "-digit number (no leading zeros)";
            }
        } catch (NumberFormatException e) {
            return "Please enter a valid number";
        }

        return null; // No error
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(int guess) {
        lastGuessText.setText("Your last guess: " + guess);
        remainingAttemptsText.setText("Remaining attempts: " + remainingAttempts);
    }

    @SuppressLint("SetTextI18n")
    private void handleGameWin() {
        feedbackText.setText("🎉 Congratulations! You've guessed the number!");
        confirmButton.setEnabled(false);
        showGameOverDialog(true);
    }

    @SuppressLint("SetTextI18n")
    private void handleGameLoss() {
        feedbackText.setText("💔 Game Over! No more attempts left.");
        confirmButton.setEnabled(false);
        showGameOverDialog(false);
    }

    @SuppressLint("SetTextI18n")
    private void provideFeedback(int guess) {
        if (guess < targetNumber) {
            feedbackText.setText("📈 Try a bigger number");
        } else {
            feedbackText.setText("📉 Try a smaller number");
        }
    }

    private int generateRandomNumber(int digits) {
        Random random = new Random();
        int min = (int) Math.pow(10, digits - 1);
        int max = (int) Math.pow(10, digits) - 1;
        return random.nextInt(max - min + 1) + min;
    }

    private void showGameOverDialog(boolean won) {
        GameOverDialogFragment dialog = GameOverDialogFragment.newInstance(
                targetNumber,
                guesses,
                won,
                guesses.isEmpty() ? 0 : guesses.get(guesses.size() - 1)
        );

        dialog.show(getParentFragmentManager(), "GameOverDialog");
    }

    // Method to reset the game when user chooses to play again
    public void resetGame() {
        targetNumber = generateRandomNumber(numberOfDigits);
        remainingAttempts = MAX_ATTEMPTS;
        guesses.clear();

        setupInitialState();
        confirmButton.setEnabled(true);
        guessInput.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up references to prevent memory leaks
        lastGuessText = null;
        remainingAttemptsText = null;
        feedbackText = null;
        guessInput = null;
        confirmButton = null;
    }
}