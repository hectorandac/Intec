package com.dragon.intec.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.dragon.intec.MainActivity;
import com.dragon.intec.R;
import com.dragon.intec.components.PDFBuilder;
import com.dragon.intec.components.TokenRequester;
import com.itextpdf.text.DocumentException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportsFragment extends Fragment {

    private static final String keyToken = "TOKEN";
    Activity activity;
    View viewLoad;

    public ReportsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_reports_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        View view = getView();

        viewLoad = view.findViewById(R.id.waiting_reports);

        List<Integer> years = new ArrayList<>();
        List<String> quarters = new ArrayList<>();

        int year = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = year; i >= 2000; i--){
            years.add(i);
        }

        quarters.add("NOVIEMBRE - ENERO");
        quarters.add("FEBRERO - ABRIL");
        quarters.add("MAYO - JULIO");
        quarters.add("AGOSTO - OCTUBRE");

        ArrayAdapter<Integer> adapter1 = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, years);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, quarters);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = (Spinner) view.findViewById(R.id.final_grades_spinn_reports_1);
        sItems.setAdapter(adapter1);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItemsQuarter = (Spinner) view.findViewById(R.id.final_grades_spinn_reports_2);
        sItemsQuarter.setAdapter(adapter2);

        view.findViewById(R.id.final_grades_button_reports).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewLoad.setVisibility(View.VISIBLE);
                new GenerateTable().execute(activity, 69);
            }
        });
    }

    public class GenerateTable extends AsyncTask<Object, Void, JSONObject>{

        Activity activity;

        @Override
        protected JSONObject doInBackground(Object... objects) {

            activity = (Activity) objects[0];

            SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
            String token = sharedPref.getString(keyToken, "");

            String jsonObject = null;

            try {
                jsonObject = new TokenRequester(token).postObject("http://angularjsauthentication20161012.azurewebsites.net/api/Report/type_1?quarter=" + ((int)objects[1]));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            JSONObject jObj = null;
            try {
                jObj = new JSONObject(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jObj;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);

            Log.i("##TEST", object.toString());
            PDFBuilder builder = new PDFBuilder();
            try {
                builder.buildFinalGrades(68, object, activity);
                File pdfFile = new File(MainActivity.myDir.getPath(), "FinalGrade"+ 68 +".pdf");
                FileViewIntent(pdfFile);
            } catch (DocumentException | JSONException | IOException e) {
                e.printStackTrace();
            }

        }

        private void FileViewIntent(File file){
            Uri path = Uri.fromFile(file);

            viewLoad.setVisibility(View.INVISIBLE);

            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try{
                activity.startActivity(pdfIntent);
            }catch(ActivityNotFoundException e){
                Toast.makeText(activity, "No tienes aplicaciones para mostrar el PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
