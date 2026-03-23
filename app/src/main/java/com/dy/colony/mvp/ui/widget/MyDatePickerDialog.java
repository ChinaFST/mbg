package com.dy.colony.mvp.ui.widget;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.RequiresApi;

import com.apkfuns.logutils.LogUtils;
import com.dy.colony.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MyDatePickerDialog extends AlertDialog {

    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";

    private final DatePicker mDatePicker_start;
    private final DatePicker mDatePicker_stop;
    private final Calendar mCalendar;

    private boolean mTitleNeedsUpdate = true;

    private OnImgDialogListener listener;

    public void setOnImgDialogListener(OnImgDialogListener listener) {
        this.listener = listener;
    }

    public interface OnImgDialogListener {
        void onItemImg(int year1_start, int month1_start, int day1_start, int year1_stop, int month1_stop, int day1_stop, String type);
        void cancle();
    }
    private View view;


    @SuppressLint("ClickableViewAccessibility")
    public MyDatePickerDialog(Context context, int theme) {
        super(context, theme);
        mCalendar = Calendar.getInstance();
        Context themeContext = getContext();
        LayoutInflater inflater = LayoutInflater.from(themeContext);
        view = inflater.inflate(R.layout.dare_picker_dialog, null);
        view.setBackgroundColor(Color.WHITE);
        mDatePicker_start = (DatePicker) view.findViewById(R.id.datePicker_start);
        mDatePicker_stop = (DatePicker) view.findViewById(R.id.datePicker_stop);
        mDatePicker_stop.setMaxDate(mCalendar.getTime().getTime());
        mDatePicker_start.setMaxDate(mCalendar.getTime().getTime());
        //mDatePicker.init(year, monthOfYear, dayOfMonth, this);
        mDatePicker_start.setCalendarViewShown(false);
        mDatePicker_stop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String datas = mDatePicker_stop.getYear() + "-" + mDatePicker_stop.getMonth() + "-" + mDatePicker_stop.getDayOfMonth();
                LogUtils.d(datas);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date parse = simpleDateFormat.parse(datas);
                    mDatePicker_start.setMaxDate(parse.getTime());
                    mDatePicker_start.invalidate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        setButton();
    }

    @SuppressLint("ClickableViewAccessibility")
    public MyDatePickerDialog(Context context, int theme, boolean show) {
        super(context, theme);
        mCalendar = Calendar.getInstance();
        Context themeContext = getContext();
        LayoutInflater inflater = LayoutInflater.from(themeContext);
        view = inflater.inflate(R.layout.dare_picker_dialog, null);
        view.setBackgroundColor(Color.WHITE);
        mDatePicker_start = (DatePicker) view.findViewById(R.id.datePicker_start);
        mDatePicker_start.setVisibility(show?View.VISIBLE:View.GONE);
        mDatePicker_stop = (DatePicker) view.findViewById(R.id.datePicker_stop);
        mDatePicker_stop.setMaxDate(mCalendar.getTime().getTime());
        mDatePicker_start.setMaxDate(mCalendar.getTime().getTime());
        //mDatePicker.init(year, monthOfYear, dayOfMonth, this);
        mDatePicker_start.setCalendarViewShown(false);
        mDatePicker_stop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String datas = mDatePicker_stop.getYear() + "-" + mDatePicker_stop.getMonth() + "-" + mDatePicker_stop.getDayOfMonth();
                LogUtils.d(datas);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date parse = simpleDateFormat.parse(datas);
                    mDatePicker_start.setMaxDate(parse.getTime());
                    mDatePicker_start.invalidate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        setButton();
    }


    private void setButton() {
        view.findViewById(R.id.date_picker_ok).setOnClickListener(
                new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        if (listener!=null){
                            listener.onItemImg(mDatePicker_start.getYear(), mDatePicker_start.getMonth(),
                                    mDatePicker_start.getDayOfMonth(),mDatePicker_stop.getYear(),mDatePicker_stop.getMonth(),mDatePicker_stop.getDayOfMonth(),"1");
                            dismiss();
                        }

                    }
                });
        view.findViewById(R.id.date_picker_cancle).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener!=null){
                            listener.cancle();
                            dismiss();
                        }
                    }
                });

    }
    public void myShow() {
        //自己实现show方法，主要是为了把setContentView方法放到show方法后面，否则会报错。
        show();
        setContentView(view);
    }




    /**
     * Gets the {@link DatePicker} contained in this dialog.
     *
     * @return The calendar view.
     */
    public DatePicker getDatePicker() {
        return mDatePicker_start;
    }

    /**
     * Sets the current date.
     *
     * @param year The date year.
     * @param monthOfYear The date month.
     * @param dayOfMonth The date day of month.
     */
    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        mDatePicker_start.updateDate(year, monthOfYear, dayOfMonth);
    }



    @Override
    public Bundle onSaveInstanceState() {
        final Bundle state = super.onSaveInstanceState();
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

}
