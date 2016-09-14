package com.dragon.intec.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragon.intec.R;
import com.dragon.intec.objects.Cubicle;
import com.dragon.intec.objects.PartialStudent;

import org.json.JSONException;

import java.io.IOException;

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
        Activity activity = getActivity();

        Cubicle cubicle = new Cubicle(activity);
        new getCubicle().execute(cubicle);

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
