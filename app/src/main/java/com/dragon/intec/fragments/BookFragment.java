package com.dragon.intec.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dragon.intec.R;
import com.dragon.intec.objects.Cubicle;
import com.dragon.intec.objects.PartialStudent;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;

public class BookFragment extends Fragment {

    private View view;

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
        final Activity activity = getActivity();

        FloatingActionButton actionButton = (FloatingActionButton) view.findViewById(R.id.action_button_book_fragment);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater i = getActivity().getLayoutInflater();
                final View layout = i.inflate(R.layout.layout_cubicle_reserve,null);

                AlertDialog.Builder dialog = new AlertDialog.Builder(activity)
                        .setTitle(R.string.reserve_cubicle)
                        .setView(layout)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });


                final View timePickerView = layout.findViewById(R.id.time_picker);

                TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String timeStr = hourOfDay + ":00";
                        ((EditText) timePickerView).setText(timeStr);
                    }
                };


                final TimePickerDialog timePicker = new TimePickerDialog(activity, time, Calendar.getInstance().getTime().getHours(), 0, true);

                timePickerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePicker.show();
                    }
                });

                timePickerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus)
                            timePicker.show();
                    }
                });

                dialog.show();
            }
        });

        Cubicle cubicle = new Cubicle(activity);
        //new getCubicle().execute(cubicle);

    }

    public class getCubicle extends AsyncTask<Cubicle, Void, Boolean> {

        Cubicle cubicle;

        @Override
        protected Boolean doInBackground(Cubicle... params) {

            Boolean available = false;
            cubicle = params[0];

            try {
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

            if (aBoolean){

                FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.action_button_book_fragment);
                fab.setImageResource(R.drawable.ic_more_vert_white_24dp);
                LayoutInflater inflater = LayoutInflater.from(getActivity());

                View to_add = inflater.inflate(R.layout.cubicle_layout, null);
                ((TextView) to_add.findViewById(R.id.time_cub_tex)).setText(cubicle.getReserved_hour() + ":00 AM");
                ((TextView) to_add.findViewById(R.id.cubicle_number)).setText(cubicle.getNumber());
                ((TextView) to_add.findViewById(R.id.duration_cub_tex)).setText(cubicle.getDuration());
                ((TextView) to_add.findViewById(R.id.location_cub_text)).setText(cubicle.getLocation());

                LinearLayout studentsList = (LinearLayout) to_add.findViewById(R.id.students_list);
                for(PartialStudent partialStudent : cubicle.getStudents()){
                    String student = partialStudent.getName() + " - (" + partialStudent.getId() + ")";
                    TextView textView = new TextView(getActivity());
                    textView.setText(student);
                    studentsList.addView(textView);
                }
                ((FrameLayout) view.findViewById(R.id.view_display)).addView(to_add);


            }

        }
    }
}
