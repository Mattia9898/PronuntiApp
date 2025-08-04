package views.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.time.LocalDate;

public class DataCustomizzata {

    public static void showDatePickerDialog(Context context, TextView textField) {
        LocalDate now = LocalDate.now();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
            String date = LocalDate.of(year, month + 1, dayOfMonth).toString();
            textField.setText(date);
        }, now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
        datePickerDialog.show();
    }

}