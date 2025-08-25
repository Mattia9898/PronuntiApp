package views.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalDate;

import it.uniba.dib.pronuntiapp.R;

public class TempoAndDataPicker extends AbstractNavigationFragment {

    private TextInputEditText textInputEditTextDataNascitaProfiloPaziente;
    private TextInputLayout textInputLayoutDataNascitaProfiloPaziente;
    private DatePickerDialog datePickerDialog;
    private ImageButton imageButtonDataNascitaProfiloPaziente;
    private GridLayout gridLayout;
    private String data;
    private String tempo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_picker, container, false);

        //parte del picker della data
        textInputEditTextDataNascitaProfiloPaziente = view.findViewById(R.id.textInputEditTextDataNascitaProfiloPaziente);
        imageButtonDataNascitaProfiloPaziente = view.findViewById(R.id.imageButtonDataNascitaProfiloPaziente);
        textInputLayoutDataNascitaProfiloPaziente = view.findViewById(R.id.textInputLayoutDataNascitaProfiloPaziente);

        textInputEditTextDataNascitaProfiloPaziente.setOnClickListener(v -> showDatePickerDialog());
        textInputLayoutDataNascitaProfiloPaziente.setEndIconOnClickListener(v -> showDatePickerDialog());
        imageButtonDataNascitaProfiloPaziente.setOnClickListener(v -> showDatePickerDialog());

        //parte del picker del tempo
        gridLayout = view.findViewById(R.id.gridLayout);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);

            if (child instanceof TextView) {
                final TextView textView = (TextView) child;
                textView.setOnClickListener(v -> handleTextViewSelection(textView));
            }
        }
        return view;
    }


    //metodi del picker del tempo
    private void handleTextViewSelection(TextView selectedTextView) {
        selectedTextView.setBackground(getContext().getDrawable(R.drawable.rettangolo_grigio_bordo_blu));
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                if (textView != selectedTextView) {
                    textView.setBackground(getContext().getDrawable(R.drawable.rettangolo_bordo_grigio));
                }
            }
        }
        String selectedText = selectedTextView.getText().toString();
        tempo = selectedText;
    }

    //metodi del picker della data
    private void showDatePickerDialog() {
        LocalDate now = LocalDate.now();
        datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            String data = formatDate(year, month, dayOfMonth);
            textInputEditTextDataNascitaProfiloPaziente.setText(data);
            this.data = data;
        }, now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
        datePickerDialog.show();
    }

    private String formatDate(int year, int month, int dayOfMonth) {
        LocalDate selectedDate = LocalDate.of(year, month + 1, dayOfMonth);
        return selectedDate.toString();
    }

}
