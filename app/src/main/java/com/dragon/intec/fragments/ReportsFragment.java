package com.dragon.intec.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.dragon.intec.R;
import com.dragon.intec.components.InfoBuilder;
import com.dragon.intec.components.TokenRequester;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportsFragment extends Fragment {

    private static final String keyToken = "TOKEN";
    Activity activity;
    View viewLoad;
    View view;

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
        view = getView();

        assert view != null;
        viewLoad = view.findViewById(R.id.waiting_reports);

        final List<Integer> years = new ArrayList<>();
        final List<String> quarters = new ArrayList<>();

        int year = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = year; i >= 2000; i--){
            years.add(i);
        }

        quarters.add("FEBRERO - ABRIL");
        quarters.add("MAYO - JULIO");
        quarters.add("AGOSTO - OCTUBRE");
        quarters.add("NOVIEMBRE - ENERO");

        ArrayAdapter<Integer> adapter1 = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, years);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, quarters);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner sItems = (Spinner) view.findViewById(R.id.final_grades_spinn_reports_1);
        sItems.setAdapter(adapter1);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner sItemsQuarter = (Spinner) view.findViewById(R.id.final_grades_spinn_reports_2);
        sItemsQuarter.setAdapter(adapter2);

        view.findViewById(R.id.final_grades_button_reports).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewLoad.setVisibility(View.VISIBLE);
                new GenerateTable_1().execute(activity, getQuarter(sItems, sItemsQuarter, years));
            }
        });

        final Spinner sItemsQuarter_2 = (Spinner) view.findViewById(R.id.mid_grades_spinn_reports_2);
        sItemsQuarter_2.setAdapter(adapter2);

        final Spinner sItems_2 = (Spinner) view.findViewById(R.id.mid_grades_spinn_reports_1);
        sItems_2.setAdapter(adapter1);

        view.findViewById(R.id.mid_grades_button_reports).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewLoad.setVisibility(View.VISIBLE);
                new GenerateTable_2().execute(activity, getQuarter(sItems_2, sItemsQuarter_2, years));
            }
        });


        final List<String> programs = new ArrayList<>();
        programs.add(HomeFragment.student.getProgram());
        ArrayAdapter<String> adapter_program = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_item, programs);

        final Spinner sItems_3 = (Spinner) view.findViewById(R.id.history_spinn_reports_2);
        sItems_3.setAdapter(adapter_program);

        view.findViewById(R.id.history_button_reports).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewLoad.setVisibility(View.VISIBLE);
                new GenerateTable_3().execute(activity, 0);
            }
        });
    }

    public int getQuarter(Spinner sItems, Spinner sItemsQuarter, List<Integer> years){
        int sum = 0;
        switch (sItemsQuarter.getSelectedItemPosition()){
            case 0:
                sum = 3;
                break;
            case 1:
                sum = 6;
                break;
            case 2:
                sum = 9;
                break;
            case 3:
                sum = 12;
                break;
        }
        return (((years.get(sItems.getSelectedItemPosition()) - 2000) * 12) + sum)/3;
    }

    public class GenerateTable_1 extends AsyncTask<Object, Void, JSONObject>{

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

            final Dialog dialog = new Dialog(activity,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
            dialog.setContentView(R.layout.info_builder_display_view);

            LinearLayout temp = (LinearLayout) dialog.findViewById(R.id.info_builder_content_holder);
            InfoBuilder infoBuilder = new InfoBuilder(activity, object);
            String[] titles_table_1 = {
                    "Créditos Acumulados",
                    "Puntos Acumulados",
                    "Índice Acumulado",
                    "Condición Académica"
            };
            String[] titles_table_2 = {
                    "Clave",
                    "Sec",
                    "Asignatura",
                    "Calif",
                    "Alpha",
                    "CR",
                    "Puntos"
            };
            String[] titles_table_3 = {
                    "",
                    "Créditos",
                    "Puntos",
                    "Índice"
            };
            String[] titles_table_4 = {
                    "",
                    "Créditos Aprobados"
            };
            String[] titles_table_5 = {
                    "Condición Académica"
            };
            String[] titles_table_6 = {
                    "Observaciones"
            };

            try {
                temp.addView(infoBuilder.getTitleType_1());
                temp.addView(infoBuilder.createHeading("Acumulados del trimestre anterior"));
                temp.addView(infoBuilder.createTableType_1(titles_table_1));
                temp.addView(infoBuilder.createHeading("Calificaciones del Trimestre"));
                temp.addView(infoBuilder.createTableType_2(titles_table_2));
                temp.addView(infoBuilder.createTableType_3(titles_table_3));
                temp.addView(infoBuilder.createTableType_4(titles_table_4));
                temp.addView(infoBuilder.createTableType_5(titles_table_5));
                temp.addView(infoBuilder.createTableType_6(titles_table_6));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.findViewById(R.id.close_image_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            dialog.show();
            viewLoad.setVisibility(View.INVISIBLE);

        }
    }

    public class GenerateTable_2 extends AsyncTask<Object, Void, JSONObject>{

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

            final Dialog dialog = new Dialog(activity,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
            dialog.setContentView(R.layout.info_builder_display_view);

            LinearLayout temp = (LinearLayout) dialog.findViewById(R.id.info_builder_content_holder);
            InfoBuilder infoBuilder = new InfoBuilder(activity, object);
            String[] titles_table_1 = {
                    "Clave",
                    "Sec",
                    "Asignatura",
                    "Profesor",
                    "CR",
                    "Calif Base",
                    "Calif"
            };
            String[] titles_table_2 = {
                    "",
                    "Acumulados"
            };

            try {
                temp.addView(infoBuilder.getTitleType_1());
                temp.addView(infoBuilder.createHeading("Acumulados del trimestre"));
                temp.addView(infoBuilder.createTableType_7(titles_table_1));
                temp.addView(infoBuilder.createTableType_8(titles_table_2));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.findViewById(R.id.close_image_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            dialog.show();
            viewLoad.setVisibility(View.INVISIBLE);

        }
    }
    public class GenerateTable_3 extends AsyncTask<Object, Void, JSONObject>{

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

            final Dialog dialog = new Dialog(activity,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
            dialog.setContentView(R.layout.info_builder_display_view);

            LinearLayout temp = (LinearLayout) dialog.findViewById(R.id.info_builder_content_holder);
            InfoBuilder infoBuilder = new InfoBuilder(activity, object);
            /*String[] titles_table_1 = {
                    "Clave",
                    "Sec",
                    "Asignatura",
                    "Profesor",
                    "CR",
                    "Calif Base",
                    "Calif"
            };
            String[] titles_table_2 = {
                    "",
                    "Acumulados"
            };*/

            try {
                temp.addView(infoBuilder.getTitleType_1());
                temp.addView(infoBuilder.getTitleType_2());
                /*temp.addView(infoBuilder.createHeading("Acumulados del trimestre"));
                temp.addView(infoBuilder.createTableType_7(titles_table_1));
                temp.addView(infoBuilder.createTableType_8(titles_table_2));*/

            } catch (JSONException e) {
                e.printStackTrace();
            }

            dialog.findViewById(R.id.close_image_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            dialog.show();
            viewLoad.setVisibility(View.INVISIBLE);

        }
    }
}
