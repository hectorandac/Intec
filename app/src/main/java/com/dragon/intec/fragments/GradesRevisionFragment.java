package com.dragon.intec.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragon.intec.R;
import com.dragon.intec.components.TokenRequester;
import com.dragon.intec.objects.GradesRev;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GradesRevisionFragment extends Fragment {

    private static final String keyToken = "TOKEN";
    LayoutInflater inflater;
    private View gView;
    private Activity gActivity;

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

        gView = getView();
        gActivity = getActivity();

        new CompleteData().execute(gView, gActivity);

    }

    LinearLayout linearLayout;

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

            linearLayout = (LinearLayout) view.findViewById(R.id.revision_container);

            for(final GradesRev gradesRev : gradesRevs) {

                java.util.TimeZone tz = java.util.TimeZone.getTimeZone("GMT-3");
                java.util.Calendar c = java.util.Calendar.getInstance(tz);
                c.add(Calendar.DATE, -3);
                Date date = gradesRev.getTiming();

                Date difference = new Date(date.getTime() - c.getTime().getTime());
                String valDate = difference.getDate() + " " + activity.getResources().getString(R.string.day_rev)
                        + ", " + String.format("%02d", difference.getHours()) + ":" + String.format("%02d", difference.getMinutes()) + ":" + String.format("%02d", difference.getSeconds());

                System.out.println(valDate);

                View layout = inflater.inflate(R.layout.revision_layout_1, null);

                setterText(R.id.rev_signature, gradesRev.getSignature(), layout);
                setterText(R.id.rev_grade, String.valueOf(gradesRev.getGrade()), layout);
                setterText(R.id.rev_teacher, gradesRev.getTeacher(), layout);
                setterText(R.id.rev_timing, valDate, layout);

                View view = null;

                int consultId = gradesRev.getStatus();
                switch (consultId){
                    case 0:
                        view = new Button(activity);

                        Button button = (Button) view;

                        button.setText(R.string.request_rev);
                        button.setTextSize(10);
                        button.setSingleLine();
                        button.setEllipsize(TextUtils.TruncateAt.END);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new makeRequest().execute(gradesRev.getConsultId());
                            }
                        });

                        break;
                    case 1:
                        view = new TextView(activity);
                        ((TextView) view).setText(R.string.waiting_rev);
                        ((TextView) view).setTextSize(10);
                        ((TextView) view).setSingleLine();
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        ((TextView) view).setGravity(Gravity.CENTER);
                        setterText(R.id.rev_timing, R.string.waiting_rev, layout);
                        break;
                    case 2:
                        view = new TextView(activity);
                        ((TextView) view).setText(R.string.accepted_rev);
                        ((TextView) view).setTextSize(10);
                        ((TextView) view).setSingleLine();
                        ((TextView) view).setEllipsize(TextUtils.TruncateAt.END);
                        ((TextView) view).setGravity(Gravity.CENTER);

                        try {
                            setterText(R.id.rev_timing, modifyDateLayout(toCalendar(date)), layout);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        break;
                }

                if(view != null)
                    ((FrameLayout)layout.findViewById(R.id.rev_state)).addView(view);

                linearLayout.addView(layout);

            }
        }

        private void setterText(int resource, String text, View view){
            ((TextView) view.findViewById(resource)).setText(text);
        }

        private void setterText(int resource, int res, View view){
            ((TextView) view.findViewById(resource)).setText(res);
        }

        private class makeRequest extends AsyncTask<Integer, Void, Void>{

            @Override
            protected Void doInBackground(Integer... integers) {

                SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
                String token = sharedPref.getString(keyToken, "");

                try {
                    new TokenRequester(token).postObject("http://angularjsauthentication20161012.azurewebsites.net/api/GradesRev/RequestRev?id=" + integers[0]);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                linearLayout.removeAllViews();
                new CompleteData().execute(gView, gActivity);

            }
        }

        private String modifyDateLayout(Calendar cal) throws ParseException{
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return format1.format(cal.getTime());
        }

        private Calendar toCalendar(Date date){
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        }
    }

}
