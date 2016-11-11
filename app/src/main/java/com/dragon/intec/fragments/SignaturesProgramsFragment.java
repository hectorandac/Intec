package com.dragon.intec.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import com.dragon.intec.R;
import com.dragon.intec.objects.CustomAdapters.ExpandableListPrograms;
import com.dragon.intec.objects.ProgramPensum;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SignaturesProgramsFragment extends Fragment {

    private static final String keyObject = "STUDENT";

    Integer[] arrayIds = null;
    View view;

    public SignaturesProgramsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_signature_programs_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();
        this.view = view;

        SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
        String user = sharedPref.getString(keyObject, "");

        JSONObject jsonObject = null;
        String[] arraySpinner = null;
        try {
            jsonObject = new JSONObject(user);
            arraySpinner = new String[] {jsonObject.optString("program")};
            arrayIds = new Integer[] {Integer.parseInt(jsonObject.optString("programId"))};
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (arraySpinner != null) {
            Spinner s = (Spinner) view.findViewById(R.id.program_selector);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.spinner_text_style, arraySpinner);
            s.setAdapter(adapter);
            new ShowData().execute(activity, arrayIds[s.getSelectedItemPosition()]);
        }
    }


    HashMap<String, List<ProgramPensum.PensumSignature>> hashMap;
    List<String> indexQuarter;

    private class ShowData extends AsyncTask<Object, Void, Void>{

        Activity activity;

        @Override
        protected Void doInBackground(Object... params) {

            activity = (Activity) params[0];
            int id = (int) params[1];

            ProgramPensum programPensum = new ProgramPensum(activity);
            try {
                hashMap = programPensum.getPensum(id);
                indexQuarter = programPensum.quarters;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.programs_list);
            ExpandableListPrograms expandableListPrograms = new ExpandableListPrograms(activity, indexQuarter, hashMap);
            expandableListView.setAdapter(expandableListPrograms);

        }
    }
}
