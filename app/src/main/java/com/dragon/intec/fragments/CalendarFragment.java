package com.dragon.intec.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dragon.intec.R;
import com.dragon.intec.objects.Calendar;

import org.json.JSONException;

import java.io.IOException;

public class CalendarFragment extends Fragment {

    Calendar calendar;

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

        calendar = new Calendar(getActivity(), 0);
        new getCalendar().execute(calendar);

    }

    private class getCalendar extends AsyncTask<Calendar, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Calendar... calendars) {

            boolean available = false;

            try {
                available = calendars[0].getData();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return available;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean){
                System.out.print(calendar.getRow(0)[0]);
            }

        }
    }
}
