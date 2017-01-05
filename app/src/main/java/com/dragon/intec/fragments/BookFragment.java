package com.dragon.intec.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dragon.intec.R;
import com.dragon.intec.components.CubInfoConstant;
import com.dragon.intec.components.TokenRequester;
import com.dragon.intec.objects.Cubicle;
import com.dragon.intec.objects.PartialStudent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class BookFragment extends Fragment {

    private View view;
    private boolean[] nonValid = new boolean[3];
    private Activity activity;

    private static final String keyToken = "TOKEN";

    public BookFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_book_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        view = getView();
        activity = getActivity();

        try {
            loadView(activity, false, false);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean loadView(final Activity activity, boolean wait, boolean flag) throws ExecutionException, InterruptedException {

        FloatingActionButton actionButton = (FloatingActionButton) view.findViewById(R.id.action_button_book_fragment);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new newCubicle().execute(activity);

            }
        });

        if(wait){
            Cubicle cubicle = new Cubicle(activity);
            return new getCubicle().execute(cubicle, activity, flag).get();
        }else{
            Cubicle cubicle = new Cubicle(activity);
            new getCubicle().execute(cubicle, activity, flag);
            return true;
        }

    }

    public class newCubicle extends AsyncTask<Object, Void, Integer[]>{

        Activity activity;
        Cubicle[] cubicles;

        @Override
        protected Integer[] doInBackground(Object... params) {

            activity = (Activity) params[0];
            cubicles = null;
            try {
                cubicles = new Cubicle(activity).availableList();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return Cubicle.getAvailableHours(cubicles);
        }

        @Override
        protected void onPostExecute(Integer[] hours) {
            super.onPostExecute(hours);

            final LayoutInflater i = getActivity().getLayoutInflater();
            final View layout = i.inflate(R.layout.layout_cubicle_reserve,null);

            final LinearLayout hourList = (LinearLayout) layout.findViewById(R.id.available_list);

            for(int hour : hours){
                ToggleButton hourButton = (ToggleButton) hourList.getChildAt(hour - 8);
                hourButton.setEnabled(true);
            }

            for(int l = 0; l < hourList.getChildCount(); l++){
                final ToggleButton hourBtn = (ToggleButton) hourList.getChildAt(l);
                hourBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            hourSelected(hourBtn, hourList, layout.findViewById(R.id.radioButton2), layout.findViewById(R.id.radioButton));
                        }
                    }
                });
            }

            ImageButton addStudent = (ImageButton) layout.findViewById(R.id.add_student_btn);
            final LinearLayout studentsContainer = (LinearLayout) layout.findViewById(R.id.students_container);

            addStudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View field = i.inflate(R.layout.layout_cubicle_reserve_stu, null);

                    field.findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((LinearLayout)field.getParent()).removeView(field);
                        }
                    });

                    studentsContainer.addView(field);
                }
            });

            AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                    .setTitle(R.string.reserve_cubicle)
                    .setView(layout)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (validateField(layout)) {

                                ArrayList<PartialStudent> students = new ArrayList<>();

                                for (int i = 0; i < studentsContainer.getChildCount(); i++) {
                                    PartialStudent student = new PartialStudent();

                                    View child = studentsContainer.getChildAt(i);
                                    TextView name = (TextView) child.findViewById(R.id.name);
                                    TextView id = (TextView) child.findViewById(R.id.id);

                                    student.setId(id.getText().toString());
                                    student.setName(name.getText().toString());

                                    students.add(student);

                                }

                                int selectedHour = getSelectedHour(hourList);
                                PartialStudent[] studentsArray = students.toArray(new PartialStudent[students.size()]);

                                RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.identifier_group);
                                View checked = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                                int position = radioGroup.indexOfChild(checked);
                                int identifier = position + 1;

                                boolean val = false;
                                try {
                                    val = new Cubicle(activity)
                                            .makeReservationIntent(cubicles, selectedHour, studentsArray, identifier);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    loadView(activity, false, false);
                                } catch (ExecutionException | InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (val) {
                                    dialog.dismiss();
                                }
                            } else {

                                String errors = "";

                                for(String error : errorHandler(nonValid)){
                                    Log.i("ERROR###-DATA", error);
                                    errors = errors + error + "\n";
                                }

                                AlertDialog.Builder dialogError = new AlertDialog.Builder(activity)
                                        .setTitle(R.string.err_msg)
                                        .setMessage(errors)
                                        .setIcon(R.drawable.ic_error_black_24dp);

                                dialogError.show();

                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            dialog.show();

        }
    }

    private ArrayList<String> errorHandler(boolean[] flags) {
        ArrayList<String> returner = new ArrayList<>();

        if (!flags[0])
            returner.add(getResources().getString(R.string.err_1));

        if (!flags[1])
            returner.add(getResources().getString(R.string.err_2));

        if (!flags[2])
            returner.add(getResources().getString(R.string.err_3));

        return returner;
    }

    private boolean validateField(View v){
        boolean returner = true;

        for (int i = 0; i < nonValid.length; i++)
            nonValid[i] = true;

        LinearLayout selectedHour = (LinearLayout) v.findViewById(R.id.available_list);
        RadioGroup duration = (RadioGroup) v.findViewById(R.id.identifier_group);
        LinearLayout students = (LinearLayout) v.findViewById(R.id.students_container);

        //1
        if(getSelectedHour(selectedHour) == -1){
            returner = false;
            nonValid[0] = false;
        }

        //2
        if(duration.getCheckedRadioButtonId() == -1){
            returner = false;
            nonValid[1] = false;
        }

        for (int i = 0; i < students.getChildCount(); i++){
            View field = students.getChildAt(i);
            String name = ((TextView) field.findViewById(R.id.name)).getText().toString();
            String id = ((TextView) field.findViewById(R.id.id)).getText().toString();

            if (name.equals("")){
                returner = false;
                nonValid[2] = false;
                break;
            }

            if (id.equals("")){
                returner = false;
                nonValid[2] = false;
                break;
            }
        }
        return returner;
    }

    Cubicle cubicle;

    //Gets if there is any reserve cubicle
    private class getCubicle extends AsyncTask<Object, Boolean, Boolean> {

        Activity activity;
        boolean aBooleanFlag = false;

        @Override
        protected Boolean doInBackground(Object... params) {

            Boolean available = false;
            cubicle = (Cubicle) params[0];
            activity = (Activity) params[1];
            aBooleanFlag = (boolean) params[2];

            try {
                //Get the data of the "reserved" cubicle if not return s false
                available = cubicle.getData();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return available;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.action_button_book_fragment);

            if (aBoolean && !aBooleanFlag){

                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View to_add = inflater.inflate(R.layout.cubicle_layout, null);

                fab.setImageResource(R.drawable.ic_delete_forever_white_24dp);
                fab.setOnClickListener(new FloatingButtonActionDelete());

                ((TextView) to_add.findViewById(R.id.time_cub_tex)).setText(cubicle.getReserved_hour() + ":00");
                ((TextView) to_add.findViewById(R.id.cubicle_number)).setText(cubicle.getNumber());
                ((TextView) to_add.findViewById(R.id.duration_cub_tex)).setText(cubicle.getDuration());

                ((TextView) to_add.findViewById(R.id.location_cub_text))
                        .setText(CubInfoConstant.location(Integer.parseInt(cubicle.getNumber()), activity));
                ((TextView) to_add.findViewById(R.id.state_changer_icon_cub_tex))
                        .setText(activity.getResources().getString(R.string.status) + " " + CubInfoConstant.Status(Integer.parseInt(cubicle.getStatus()), activity));

                LinearLayout studentsList = (LinearLayout) to_add.findViewById(R.id.students_list);
                for(PartialStudent partialStudent : cubicle.getStudents()){
                    String student = partialStudent.getName() + " - (" + partialStudent.getId() + ")";
                    TextView textView = new TextView(getActivity());
                    textView.setText(student);
                    studentsList.addView(textView);
                }

                ((FrameLayout) view.findViewById(R.id.view_display)).addView(to_add);
            }

            if(!aBoolean){
                ((FrameLayout) view.findViewById(R.id.view_display)).removeAllViews();

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new newCubicle().execute(activity);

                    }
                });
                fab.setImageResource(R.drawable.ic_add_white_24dp);

            }

            if (aBooleanFlag){
                new FloatingButtonActionDelete().createJSON().execute(false);
                try {
                    loadView(activity, false, false);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private class FloatingButtonActionDelete extends AsyncTask<Boolean, Void, Boolean> implements View.OnClickListener {

        JSONObject jsonObject;
        FloatingButtonActionDelete floatingButtonActionDelete;

        FloatingButtonActionDelete createJSON(){
            String uniqueId = cubicle.getUniqueId();
            String number = cubicle.getNumber();
            int reservedHour = cubicle.getReserved_hour();

            jsonObject = new JSONObject();
            try {
                jsonObject.put("uniqueId", uniqueId);
                jsonObject.put("number", number);
                jsonObject.put("reservedHour", reservedHour);
                jsonObject.put("duration", 1);
                jsonObject.put("status", 3);
                jsonObject.put("students", null);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return this;
        }

        @Override
        public void onClick(View v) {

            floatingButtonActionDelete = this;

            AlertDialog.Builder dialogError = new AlertDialog.Builder(activity)
                    .setTitle(R.string.err_delete_title)
                    .setMessage(R.string.err_delete)
                    .setIcon(R.drawable.ic_error_black_24dp)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            createJSON();

                            int duration = Integer.parseInt(cubicle.getDuration());
                            boolean aBoolean = false;

                            if (duration == 2)
                                aBoolean = true;

                            floatingButtonActionDelete.execute(aBoolean);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            dialogError.show();
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {

            Log.i("CancelJSON ##", jsonObject.toString());

            SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
            String token = sharedPref.getString(keyToken, "");

            try {
                new TokenRequester(token).postJSONObject("http://angularjsauthentication20161012.azurewebsites.net/api/cubicle/cancel", jsonObject);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return params[0];
        }

        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);

            try {
                loadView(activity, false, bool);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private int getSelectedHour(LinearLayout parent){

        int hour = 0;

        for (int i = 0; i < parent.getChildCount(); i++){
            if(((ToggleButton) parent.getChildAt(i)).isChecked()){
                hour = i + 8;
                break;
            }

            if (i == (parent.getChildCount()-1)){
                hour = -1;
            }
        }

        return hour;
    }

    private void hourSelected(View v, LinearLayout parent, View selectableFail, View selectableAlternately) {

        int viewPosition = parent.indexOfChild(v);

        if(parent.getChildAt(viewPosition + 1) == null || !(parent.getChildAt(viewPosition + 1)).isEnabled()){
            selectableFail.setEnabled(false);
            ((RadioButton)selectableAlternately).setChecked(true);
        }else{
            selectableFail.setEnabled(true);
        }

        for (int childIndex = 0; childIndex < parent.getChildCount(); childIndex++) {
            if (childIndex != viewPosition){
                ToggleButton hourBtn = (ToggleButton) parent.getChildAt(childIndex);
                hourBtn.setChecked(false);
            }
        }
    }
}
