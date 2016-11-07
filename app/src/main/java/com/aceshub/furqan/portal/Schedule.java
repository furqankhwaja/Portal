package com.aceshub.furqan.portal;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static java.lang.String.valueOf;

public class Schedule extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener
        , WeekView.EmptyViewLongPressListener, CalendarDatePickerDialogFragment.OnDateSetListener {

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_FOUR_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private static final String FRAG_TAG_DATE_PICKER = "Go To Date";
    private WeekView mWeekView;
    private int mWeekViewType = TYPE_FOUR_DAY_VIEW;
    private int mDayOfMonth = 0;
    private int mMonthOfYear = 0;
    private int mYear = 0;


    public Schedule() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_schedule, container, false);
        mWeekView = (WeekView) mView.findViewById(R.id.weekView);
        mWeekView.goToHour(9);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setEmptyViewLongPressListener(this);
        setupDateTimeInterpreter(false);
        setHasOptionsMenu(true);

        return mView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.schedule_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.today:
                mWeekView.goToToday();
                mWeekView.goToHour(9);
                mYear = 0;
                return true;
            case R.id.go_to_date:
                selectDate();
                //Toast.makeText(getContext(),"Make 'Go To Date' fragment",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);
                    mWeekView.goToHour(9);
                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.four_day_view:
                if (mWeekViewType != TYPE_FOUR_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_FOUR_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(4);
                    mWeekView.goToHour(9);
                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 11, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);
                    mWeekView.goToHour(9);
                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 9, getResources().getDisplayMetrics()));
                    mWeekView.setEventCornerRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(getContext(), "Go to Attendance", Toast.LENGTH_SHORT).show();
    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        /*Calendar startTime = Calendar.getInstance();
        //startTime.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        startTime.set(Calendar.HOUR_OF_DAY, 14);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set((Calendar.YEAR), newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        endTime.set(Calendar.MONTH, newMonth-1);
        WeekViewEvent event = new WeekViewEvent(1, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_03));
        events.add(event);

        startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 14);
        startTime.add(Calendar.DAY_OF_MONTH, 2);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        endTime = (Calendar) startTime.clone();
        endTime.roll(Calendar.HOUR, 2);
        endTime.set(Calendar.MONTH, newMonth-1);
        endTime.set(Calendar.YEAR, newYear);
        event = new WeekViewEvent(2, getEventTitle(startTime), startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_04));
        events.add(event);*/

        Calendar day = Calendar.getInstance();
        day.set(Calendar.YEAR, newYear);
        day.set(Calendar.MONTH, newMonth);
        day.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        day.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
        for (int t = day.get(Calendar.DAY_OF_MONTH), i = 1; t <= day.getActualMaximum(Calendar.DAY_OF_MONTH); t += 7, i++) {
            if (newMonth == Calendar.NOVEMBER | newMonth == Calendar.OCTOBER) {
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.YEAR, newYear);
                startTime.set(Calendar.MONTH, newMonth);
                startTime.set(Calendar.DAY_OF_MONTH, t);
                startTime.set(Calendar.HOUR_OF_DAY, 12);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.SECOND, 0);

                Calendar endTime = (Calendar) startTime.clone();
                endTime.roll(Calendar.HOUR_OF_DAY, 1);
                endTime.set(Calendar.MINUTE, 30);
                endTime.set(Calendar.MONTH, newMonth);
                endTime.set(Calendar.YEAR, newYear);

                WeekViewEvent event = new WeekViewEvent(i, "Digital Communication", startTime, endTime);
                event.setColor(getResources().getColor(R.color.event_color_02));
                events.add(event);
            }
        }
        day = Calendar.getInstance();
        day.set(Calendar.YEAR, newYear);
        day.set(Calendar.MONTH, newMonth);
        day.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        day.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
        for (int t = day.get(Calendar.DAY_OF_MONTH), i = 1; t <= day.getActualMaximum(Calendar.DAY_OF_MONTH); t += 7, i++) {
            if (newMonth == Calendar.NOVEMBER | newMonth == Calendar.OCTOBER) {
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.YEAR, newYear);
                startTime.set(Calendar.MONTH, newMonth);
                startTime.set(Calendar.DAY_OF_MONTH, t);
                startTime.set(Calendar.HOUR_OF_DAY, 10);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.SECOND, 0);

                Calendar endTime = (Calendar) startTime.clone();
                endTime.roll(Calendar.HOUR_OF_DAY, 1);
                endTime.set(Calendar.MONTH, newMonth);
                endTime.set(Calendar.YEAR, newYear);

                WeekViewEvent event = new WeekViewEvent(i, "Digital Communication", startTime, endTime);
                event.setColor(getResources().getColor(R.color.event_color_02));
                events.add(event);
            }
        }
        day = Calendar.getInstance();
        day.set(Calendar.YEAR, newYear);
        day.set(Calendar.MONTH, newMonth);
        day.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        day.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
        for (int t = day.get(Calendar.DAY_OF_MONTH), i = 1; t <= day.getActualMaximum(Calendar.DAY_OF_MONTH); t += 7, i++) {
            if (newMonth == Calendar.NOVEMBER | newMonth == Calendar.OCTOBER) {
                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.YEAR, newYear);
                startTime.set(Calendar.MONTH, newMonth);
                startTime.set(Calendar.DAY_OF_MONTH, t);
                startTime.set(Calendar.HOUR_OF_DAY, 14);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.SECOND, 0);

                Calendar endTime = (Calendar) startTime.clone();
                endTime.add(Calendar.HOUR_OF_DAY, 1);
                endTime.set(Calendar.MONTH, newMonth);
                endTime.set(Calendar.YEAR, newYear);

                WeekViewEvent event = new WeekViewEvent(i, "Digital Communication", startTime, endTime);
                event.setColor(getResources().getColor(R.color.event_color_02));
                events.add(event);
            }
        }
        return events;
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Toast.makeText(getContext(), "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }

    public void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                if (shortDate)
                    weekday = valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour, int minutes) {
                return hour > 11 ? (hour == 12 ? "12 PM" : (hour - 12) + " PM") : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    public WeekView getWeekView() {
        return mWeekView;
    }

    public void selectDate() {
        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment();
        cdp.setFirstDayOfWeek(Calendar.MONDAY);
        cdp.setOnDateSetListener(Schedule.this);
        cdp.setDoneText("OK");
        cdp.setCancelText("Cancel");
        cdp.setThemeCustom(R.style.MyCustomBetterPickersDialogs);
        //cdp.setThemeLight();

        if (mYear != 0)
            cdp.setPreselectedDate(mYear, mMonthOfYear, mDayOfMonth);

        cdp.show(getActivity().getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        mYear = year;
        mMonthOfYear = monthOfYear;
        mDayOfMonth = dayOfMonth;

        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, mYear);
        date.set(Calendar.MONTH, mMonthOfYear);
        date.set(Calendar.DAY_OF_MONTH, mDayOfMonth);
        mWeekView.goToDate(date);
        mWeekView.goToHour(9);
    }
}
