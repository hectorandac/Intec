package com.dragon.intec.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragon.intec.MainActivity;
import com.dragon.intec.R;
import com.dragon.intec.objects.Student;

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

        View mainView = getView();
        Activity activity = getActivity();
        Student student = ((MainActivity) activity).getStudent();

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
    }
}
