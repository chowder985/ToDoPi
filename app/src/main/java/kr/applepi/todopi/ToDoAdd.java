package kr.applepi.todopi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by ilhoon on 8/30/16.
 */
public class ToDoAdd extends AppCompatActivity {

    TextView textView;
    Button btn2;
    Button add_btn2;
    EditText todo_content;
    Intent intent;
    String input;

    int Month, Day, Year;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_todo);

        btn2 = (Button) findViewById(R.id.showDatePickerDialog);
        add_btn2 = (Button) findViewById(R.id.add_btn2);
        todo_content = (EditText) findViewById(R.id.todo_content);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        add_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input = todo_content.getText().toString();
                intent = new Intent();
                intent.putExtra("key1", input);
                intent.putExtra("key2", Month);
                intent.putExtra("key3", Day);
                intent.putExtra("key4", Year);

                setResult(-1, intent);

                finish();
            }
        });

    }

    public void populateSetDate(int year, int month, int day){
        textView = (TextView) findViewById(R.id.text_date);
        textView.setText(month+"/"+day+"/"+year);
        Month = month;
        Day = day;
        Year = year;
    }
    /**
     * Created by ilhoon on 8/15/16.
     */
    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            populateSetDate(year, month+1, day);
        }

    }
}
