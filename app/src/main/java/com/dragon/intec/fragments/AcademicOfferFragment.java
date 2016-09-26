package com.dragon.intec.fragments;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.dragon.intec.R;
import com.dragon.intec.objects.ClassRoom;
import com.dragon.intec.objects.ClassRooms;
import com.dragon.intec.objects.CustomAdapters.ExpandableListAdapterGroupe;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AcademicOfferFragment extends Fragment {

    private ClassRooms classRooms = null;

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;

    private List<ClassRoom> listDataHeader;
    private HashMap<ClassRoom, ClassRooms> listDataChild;

    private BottomSheetBehavior mBottomSheetBehavior;

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

        classRooms = new ClassRooms(getActivity(), null);
        new getData().execute(classRooms);

        View bottomSheet = view.findViewById( R.id.bottom_sheet );
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(true);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void showOffers(ClassRooms classRooms) {

        // get the listview
        if (expListView == null)
            expListView = (ExpandableListView) getView().findViewById(R.id.exp_container);

        // preparing list data
        prepareData(classRooms);
        listAdapter = new ExpandableListAdapterGroupe(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

    }

    private void prepareData(ClassRooms classRoomsMain) {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        ClassRooms classRooms = new ClassRooms(null, null);
        classRooms.setClassRooms(classRoomsMain.getClassRooms());

        while (classRooms.getClassRooms().size() > 0) {
            String name = classRooms.getClassRooms().get(0).getArea();

            ClassRooms result = classRooms.getByArea(name);

            if(result.getClassRooms().size() > 0) {
                listDataHeader.add(classRooms.getClassRooms().get(0));
                listDataChild.put(classRooms.getClassRooms().get(0), result);
            }

            for (ClassRoom c : result.getClassRooms()){
                classRooms.removeClassRoom(c);
            }

        }

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
                showOffers(classRooms);
            }

        }
    }

}
