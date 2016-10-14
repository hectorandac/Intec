package com.dragon.intec.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dragon.intec.R;
import com.dragon.intec.objects.Student;

import org.json.JSONException;

import java.io.IOException;

public class HomeFragment extends Fragment {

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        new FillBlanks().execute(activity);


        /*
        LinearLayout linearLayout = (LinearLayout) mainView.findViewById(R.id.alerts_user);
        String[] vals = student.getAlerts();
        for (String val : vals) {
            TextView textView = new TextView(activity);
            textView.setText(val);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 4, 0, 4);
            textView.setLayoutParams(lp);
            linearLayout.addView(textView);
        }

        String[][] signatures = student.getSignatures().getSignatures();
        Log.i("ARRAY_signatures", signatures[0][0]);
        for (String[] signature:signatures) {
            LinearLayout listSignatures = (LinearLayout) mainView.findViewById(R.id.tab_signatures);
            LinearLayout listElements = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.signature_layout, null);
            for (int j = 0; j < listElements.getChildCount(); j++){
                View v = listElements.getChildAt(j);
                if (j == 11) {
                    switch (signature[j]){
                        case "true":
                            ((CheckBox) v).setChecked(true);
                            break;
                        case "false":
                            ((CheckBox) v).setChecked(false);
                            break;
                    }
                }
                else {
                    ((TextView) v).setText(signature[j]);
                }

            }
            listSignatures.addView(listElements);
        }*/


    }

    private class FillBlanks extends AsyncTask<Activity, Void, Student> {

        @Override
        protected Student doInBackground(Activity... params) {

            Student student = new Student(null, params[0]);

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
        }
    }
}
