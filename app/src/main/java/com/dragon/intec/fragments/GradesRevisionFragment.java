package com.dragon.intec.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragon.intec.R;
import com.dragon.intec.objects.GradesRev;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GradesRevisionFragment extends Fragment {

    LayoutInflater inflater;

    public GradesRevisionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.activity_grades_revision_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new CompleteData().execute(getView(), getActivity());

    }

    private class CompleteData extends AsyncTask<Object, Void, GradesRev[]>{

        View view;
        Activity activity;

        @Override
        protected GradesRev[] doInBackground(Object... params) {

            activity = (Activity) params[1];
            view = (View) params[0];

            GradesRev[] gradesRevs = null;

            try {
                gradesRevs = new GradesRev().getRevisions(activity);
            } catch (IOException | JSONException | ParseException e) {
                e.printStackTrace();
            }

            return gradesRevs;
        }

        @Override
        protected void onPostExecute(GradesRev[] gradesRevs) {
            super.onPostExecute(gradesRevs);

            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.revision_container);

            for(GradesRev gradesRev : gradesRevs) {

                java.util.TimeZone tz = java.util.TimeZone.getTimeZone("GMT-3");
                java.util.Calendar c = java.util.Calendar.getInstance(tz);
                c.add(Calendar.DATE, -3);
                Date date = gradesRev.getTiming();

                Date difference = new Date(date.getTime() - c.getTime().getTime());
                String valDate = difference.getDate() + " " + activity.getResources().getString(R.string.day_rev)
                        + ", " + difference.getHours() + ":" + difference.getMinutes() + ":" + difference.getSeconds();

                System.out.println(valDate);

                View layout = inflater.inflate(R.layout.revision_layout_1, null);

                setterText(R.id.rev_signature, gradesRev.getSignature(), layout);
                setterText(R.id.rev_grade, String.valueOf(gradesRev.getGrade()), layout);
                setterText(R.id.rev_teacher, gradesRev.getTeacher(), layout);
                setterText(R.id.rev_timing, valDate, layout);

                linearLayout.addView(layout);

            }




        }

        private void setterText(int resource, String text, View view){
            ((TextView) view.findViewById(resource)).setText(text);
        }
    }

}
