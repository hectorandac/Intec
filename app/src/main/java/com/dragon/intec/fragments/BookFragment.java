package com.dragon.intec.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dragon.intec.R;
import com.dragon.intec.objects.Cubicle;

import org.json.JSONException;

import java.io.IOException;

public class BookFragment extends Fragment {

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

        Activity activity = getActivity();

        Cubicle cubicle = new Cubicle(activity);
        try {
            cubicle.getData();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
}
