package views.fragment;


import it.uniba.dib.pronuntiapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.os.Bundle;

import java.time.LocalDate;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class TimeAndDatePicker extends AbstractNavigationBetweenFragment {

    private GridLayout gridLayout;

    private TextInputEditText editTextBirthdatePatient;

    private ImageButton buttonBirthdatePatient;

    private TextInputLayout mainTextInputLayout;

    private DatePickerDialog datePickerDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.time_and_date_picker, container, false);

        editTextBirthdatePatient = view.findViewById(R.id.editTextBirthdatePatient);
        buttonBirthdatePatient = view.findViewById(R.id.buttonBirthdatePatient);
        mainTextInputLayout = view.findViewById(R.id.mainTextInputLayout);

        editTextBirthdatePatient.setOnClickListener(v ->
                datePickerDialog());
        mainTextInputLayout.setEndIconOnClickListener(v ->
                datePickerDialog());
        buttonBirthdatePatient.setOnClickListener(v ->
                datePickerDialog());

        gridLayout = view.findViewById(R.id.gridLayout);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {

            View viewChild = gridLayout.getChildAt(i);

            if (viewChild instanceof TextView) {
                final TextView textView = (TextView) viewChild;
                textView.setOnClickListener(v ->
                        backgroundTextViewSelected(textView));
            }
        }
        return view;
    }


    // method for setting background of time picker selected
    private void backgroundTextViewSelected(TextView textViewSelected) {

        textViewSelected.setBackground(getContext().getDrawable(R.drawable.rettangolo_grigio_bordo_blu));

        for (int i = 0; i < gridLayout.getChildCount(); i++) {

            View viewChild = gridLayout.getChildAt(i);

            if (viewChild instanceof TextView) {
                TextView textView = (TextView) viewChild;
                if (textView != textViewSelected) {
                    textView.setBackground(getContext().getDrawable(R.drawable.rettangolo_bordo_grigio));
                }
            }
        }
    }

    private String formatDate(int year, int month, int day) {
        LocalDate localDate = LocalDate.of(year, month + 1, day);
        return localDate.toString();
    }

    // method of date picker for patient birthdate
    private void datePickerDialog() {

        LocalDate localDate = LocalDate.now();

        datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, day) -> {
            String date = formatDate(year, month, day);
            editTextBirthdatePatient.setText(date);
        }, localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());

        datePickerDialog.show();
    }

}
