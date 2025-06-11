package com.example.homework3;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;

public class GameOverDialogFragment extends DialogFragment {

    private static final String ARG_TARGET_NUMBER = "target_number";
    private static final String ARG_GUESSES = "guesses";
    private static final String ARG_WON = "won";
    private static final String ARG_LAST_GUESS = "last_guess";

    private int targetNumber;
    private ArrayList<Integer> guesses;
    private boolean won;

    public static GameOverDialogFragment newInstance(int targetNumber, ArrayList<Integer> guesses, boolean won, int lastGuess) {
        GameOverDialogFragment fragment = new GameOverDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TARGET_NUMBER, targetNumber);
        args.putIntegerArrayList(ARG_GUESSES, guesses);
        args.putBoolean(ARG_WON, won);
        args.putInt(ARG_LAST_GUESS, lastGuess);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            targetNumber = getArguments().getInt(ARG_TARGET_NUMBER);
            guesses = getArguments().getIntegerArrayList(ARG_GUESSES);
            won = getArguments().getBoolean(ARG_WON);
            getArguments().getInt(ARG_LAST_GUESS);
        }

        // Make dialog non-cancelable so user must choose YES or NO
        setCancelable(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // Remove title bar
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_over, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews(view);
        setupClickListeners(view);
    }

    @SuppressLint("SetTextI18n")
    private void setupViews(View view) {
        // Setup dialog content
        TextView gameOverTitle = view.findViewById(R.id.game_over_title);
        TextView gameOverMessage = view.findViewById(R.id.game_over_message);
        TextView correctNumberText = view.findViewById(R.id.correct_number_text);
        TextView guessesText = view.findViewById(R.id.guesses_text);

        setupGameOverContent(gameOverTitle, gameOverMessage);
        setupGameStats(correctNumberText, guessesText);
    }

    @SuppressLint("SetTextI18n")
    private void setupGameOverContent(TextView title, TextView message) {
        if (won) {
            title.setText("🎉 Number Guessing Game - Congratulations!");
            if (getContext() != null) {
                title.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_green_dark));
            }
            message.setText("You've successfully guessed the number!");
        } else {
            title.setText("💔 Number Guessing Game - Game Over");
            if (getContext() != null) {
                title.setTextColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
            }
            message.setText("You've used all your attempts. Better luck next time!");
        }
    }

    @SuppressLint("SetTextI18n")
    private void setupGameStats(TextView correctNumberText, TextView guessesText) {
        correctNumberText.setText("My number was: " + targetNumber);

        if (guesses != null && !guesses.isEmpty()) {
            StringBuilder guessesStr = new StringBuilder("Your guesses: ");
            for (int i = 0; i < guesses.size(); i++) {
                guessesStr.append(guesses.get(i));
                if (i < guesses.size() - 1) {
                    guessesStr.append(", ");
                }
            }
            guessesText.setText(guessesStr.toString());
        } else {
            guessesText.setText("Your guesses: None");
        }
    }

    private void setupClickListeners(View view) {
        Button yesButton = view.findViewById(R.id.yes_button);
        Button noButton = view.findViewById(R.id.no_button);

        yesButton.setOnClickListener(v -> playAgain());
        noButton.setOnClickListener(v -> exitGame());
    }

    private void playAgain() {
        // Find the GameFragment and reset it
        GameFragment gameFragment = (GameFragment) getParentFragmentManager()
                .findFragmentById(R.id.main);

        if (gameFragment != null) {
            gameFragment.resetGame();
        }

        dismiss();
    }

    private void exitGame() {
        // Clear the back stack and return to main activity
        getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // Reset MainActivity state
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).returnToMain();
        }

        dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Make dialog take up most of the screen width
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up to prevent memory leaks
        guesses = null;
    }
}