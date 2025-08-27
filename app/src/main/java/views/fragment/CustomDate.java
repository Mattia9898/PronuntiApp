package views.fragment;


import java.time.LocalDate;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;


public class CustomDate {

    public static void DatePickerDialog(Context context, TextView textView) {

        LocalDate localDate = LocalDate.now();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, month, day) -> {
            String date = LocalDate.of(year, month + 1, day).toString();
            textView.setText(date);
        }, localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());

        datePickerDialog.show();
    }

}