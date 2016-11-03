package com.dragon.intec.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragon.intec.R;
import com.dragon.intec.objects.ClassRoom;
import com.dragon.intec.objects.Student;

import org.json.JSONException;

import java.io.IOException;

public class HomeFragment extends Fragment {

    LayoutInflater inflater;
    Student student;

    public HomeFragment(){
        //required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.inflater = inflater;

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        new FillBlanks().execute(activity);


    }

    private class FillBlanks extends AsyncTask<Activity, Void, Student> {

        Activity activity = null;

        @Override
        protected Student doInBackground(Activity... params) {

            activity = params[0];
            student = new Student(params[0]);

            try {
                student.getData();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return student;
        }

        @Override
        protected void onPostExecute(Student student) {
            super.onPostExecute(student);

            View mainView = getView();

            assert mainView != null;
            ((TextView) mainView.findViewById(R.id.id_user)).setText(student.getId());
            ((TextView) mainView.findViewById(R.id.program_user)).setText(student.getProgram());
            ((TextView) mainView.findViewById(R.id.condition_user)).setText(student.getAcademicCondition());
            ((TextView) mainView.findViewById(R.id.quarter_user)).setText(student.getQuarter());
            ((TextView) mainView.findViewById(R.id.last_condition_user)).setText(student.getLastCondition());
            ((TextView) mainView.findViewById(R.id.quarter_index_user)).setText(String.valueOf(student.getQuarterIndex()));
            ((TextView) mainView.findViewById(R.id.general_index_user)).setText(String.valueOf(student.getGeneralIndex()));
            ((TextView) mainView.findViewById(R.id.validated_credits_user)).setText(String.valueOf(student.getValidatedCredits()));
            ((TextView) mainView.findViewById(R.id.approved_credits_user)).setText(String.valueOf(student.getApprovedCredits()));
            ((TextView) mainView.findViewById(R.id.approved_quarter_user)).setText(String.valueOf(student.getApprovedQuarters()));

            LinearLayout linearLayout = (LinearLayout) mainView.findViewById(R.id.alerts_tap);

            try {
                String[] alerts = student.getAlerts();
                for (String alert : alerts) {
                    TextView textView = new TextView(activity);
                    textView.setText(alert);
                    linearLayout.addView(textView);
                }
                if (alerts.length == 0){
                    TextView textView = new TextView(activity);
                    textView.setText(R.string.no_alerts);
                    linearLayout.addView(textView);
                }
            }catch (Exception e){
                TextView textView = new TextView(activity);
                textView.setText(R.string.no_alerts);
                linearLayout.addView(textView);
            }

            ClassRoom[] classRooms = student.getSignatures();
            LinearLayout classRoomsList = (LinearLayout) mainView.findViewById(R.id.tab_signatures);

            try {
                for (ClassRoom classRoom : classRooms) {
                    View v = inflater.inflate(R.layout.signature_layout, null);

                    String[] mon = classRoom.getMon();
                    String[] tue = classRoom.getTue();
                    String[] wed = classRoom.getWed();
                    String[] thu = classRoom.getThu();
                    String[] fri = classRoom.getFri();
                    String[] sat = classRoom.getSat();


                    ((TextView) v.findViewById(R.id.signature_code)).setText(classRoom.getCode());
                    ((TextView) v.findViewById(R.id.signature_name)).setText(classRoom.getName());
                    ((TextView) v.findViewById(R.id.signature_sections)).setText(classRoom.getSec());
                    ((TextView) v.findViewById(R.id.signature_aula)).setText(classRoom.getRoom());
                    ((TextView) v.findViewById(R.id.signature_grades)).setText(classRoom.getGrades());


                    try {
                        ((TextView) v.findViewById(R.id.signature_mon)).setText(classRoom.fixedTime(mon[0], mon[1]));
                    } catch (Exception e) {
                        ((TextView) v.findViewById(R.id.signature_mon)).setText("");
                    }

                    try {
                        ((TextView) v.findViewById(R.id.signature_tue)).setText(classRoom.fixedTime(tue[0], tue[1]));
                    } catch (Exception e) {
                        ((TextView) v.findViewById(R.id.signature_tue)).setText("");
                    }

                    try {
                        ((TextView) v.findViewById(R.id.signature_wen)).setText(classRoom.fixedTime(wed[0], wed[1]));
                    } catch (Exception e) {
                        ((TextView) v.findViewById(R.id.signature_wen)).setText("");
                    }

                    try {
                        ((TextView) v.findViewById(R.id.signature_thu)).setText(classRoom.fixedTime(thu[0], thu[1]));
                    } catch (Exception e) {
                        ((TextView) v.findViewById(R.id.signature_thu)).setText("");
                    }

                    try {
                        ((TextView) v.findViewById(R.id.signature_fri)).setText(classRoom.fixedTime(fri[0], fri[1]));
                    } catch (Exception e) {
                        ((TextView) v.findViewById(R.id.signature_fri)).setText("");
                    }

                    try {
                        ((TextView) v.findViewById(R.id.signature_sat)).setText(classRoom.fixedTime(sat[0], sat[1]));
                    } catch (Exception e) {
                        ((TextView) v.findViewById(R.id.signature_sat)).setText("");
                    }

                    ((TextView) v.findViewById(R.id.signature_grades)).setText(classRoom.getGrades());
                    ((TextView) v.findViewById(R.id.signature_teacher)).setText(classRoom.getTeacher());


                    classRoomsList.addView(v);

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
