package com.dragon.intec.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragon.intec.R;
import com.dragon.intec.objects.Calendar;

import org.json.JSONException;

import java.io.IOException;

public class CalendarFragment extends Fragment {

    public CalendarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_calendar_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Activity activity = getActivity();

        Button getQuarter = (Button) view.findViewById(R.id.quarter_calendar);
        Button getSelection = (Button) view.findViewById(R.id.selection_calendar);
        Button getAnnual = (Button) view.findViewById(R.id.annual_calendar);
        Button getFinance = (Button) view.findViewById(R.id.finance_calendar);

        getQuarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = new Calendar(getActivity(), 0);
                new getCalendar().execute(calendar, activity);
            }
        });

        getSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = new Calendar(getActivity(), 1);
                new getCalendar().execute(calendar, activity);
            }
        });

        getAnnual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = new Calendar(getActivity(), 2);
                new getCalendar().execute(calendar, activity);
            }
        });

        getFinance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = new Calendar(getActivity(), 3);
                new getCalendar().execute(calendar, activity);
            }
        });

    }

    private void showCalendar(Context context, Calendar calendarData){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setTitle(R.string.calendar);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        int designedCalendar = calendarData.getType()+1;

        View designedLayout = inflater.inflate(getResources().getIdentifier("calendar_"+designedCalendar, "layout", context.getPackageName()), null);
        String title = ((TextView)designedLayout.findViewById(R.id.title_calendar)).getText().toString();

        LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.content_holder);

        for(String[] row : calendarData.getCalendarRows()){
            if (!row[1].equals(title)){
                if(((LinearLayout) designedLayout.findViewById(R.id.row_container)).getChildCount() > 0){
                    linearLayout.addView(designedLayout);
                }
                designedLayout = inflater.inflate(getResources().getIdentifier("calendar_"+designedCalendar, "layout", context.getPackageName()), null);
                title = row[1];
                ((TextView)designedLayout.findViewById(R.id.title_calendar)).setText(title);
            }

            LinearLayout tempHolder = (LinearLayout) (inflater.inflate(
                    getResources().getIdentifier("row_layout_"+designedCalendar, "layout", context.getPackageName()), null))
                    .findViewById(R.id.container);

            int count = tempHolder.getChildCount();
            TextView v;
            for(int i=0; i<count; i++) {
                v = (TextView) tempHolder.getChildAt(i);
                v.setText(row[i+2]);
            }

            ((LinearLayout) designedLayout.findViewById(R.id.row_container)).addView(tempHolder);
        }

        linearLayout.addView(designedLayout);

        dialog.show();
    }

    private class getCalendar extends AsyncTask<Object, Void, Boolean>{

        private Calendar calendar;
        private Activity activity;

        @Override
        protected Boolean doInBackground(Object... objects) {

            calendar = (Calendar) objects[0];
            activity = (Activity) objects[1];

            boolean available = false;

            try {
                available = calendar.getData();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return available;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean){
                showCalendar(activity, calendar);
            }

        }
    }
}
