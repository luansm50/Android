package com.example.luanmelo.calendario;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

public class MainActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        /*calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(2015, 01, 23))
                .setMaximumDate(CalendarDay.from(2018,03,01))
                .commit();*/
        CharSequence meses[] = {"Janeiro", "Fevereiro", "Março", "Abril" ,"Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"}
         calendarView.setTitleMonths(meses);

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
                System.out.println((calendarDay.getMonth() + 1) + calendarDay.getYear());
            }
        });
    }
}
