package com.dragon.intec.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dragon.intec.R;
import com.dragon.intec.objects.ClassRooms;

import org.json.JSONException;

import java.io.IOException;

public class AcademicOfferFragment extends Fragment {

    ClassRooms classRooms = null;

    public AcademicOfferFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_academic_offer_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        classRooms = new ClassRooms(getActivity(), "Fisi");
        new getData().execute(classRooms);
    }

    private class getData extends AsyncTask<ClassRooms, Void, Boolean> {

        @Override
        protected Boolean doInBackground(ClassRooms... classRoomses) {

            boolean available = false;

            try {
                available = classRoomses[0].getData();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return available;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (aBoolean) {
                System.out.println(classRooms.getClassRooms().get(0).getName());
            }

        }
    }

}
