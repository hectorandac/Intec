package com.dragon.intec.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dragon.intec.R;
import com.dragon.intec.objects.ClassRoom;
import com.dragon.intec.objects.CustomAdapters.ExpandableListAdapterGroupe;
import com.dragon.intec.objects.Signature;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AcademicOfferFragment extends Fragment {

    ArrayList<Signature> signatures = new ArrayList<>();

    View view;
    private static BottomSheetBehavior<View> mBottomSheetBehavior;

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

        this.view = view;
        final Activity activity = getActivity();

        new PrepareView().execute(activity, "");

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

        final TextView search = (TextView) view.findViewById(R.id.search_box);
        View searchButton = view.findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PrepareView().execute(activity, search.getText().toString());
            }
        });

    }

    private class PrepareView extends AsyncTask<Object, Void, Void>{

        private Activity activity;
        private HashMap<String, List<Signature>> listDataChild;
        private List<String> listDataHeader;

        @Override
        protected Void doInBackground(Object... params) {

            activity = (Activity) params[0];
            String name = (String) params[1];

            try {
                signatures = Signature.getSignatures(activity, name);
                //classRooms = ClassRoom.getClassrooms(activity);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            listDataHeader = getHeaders_Area(signatures);
            listDataChild = getHash(signatures, listDataHeader);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ExpandableListView expandableList = (ExpandableListView) view.findViewById(R.id.exp_container);
            ExpandableListAdapterGroupe expandableListAdapterGroupe = new ExpandableListAdapterGroupe(activity, listDataHeader, listDataChild);
            expandableList.setAdapter(expandableListAdapterGroupe);
        }

        private List<String> getHeaders_Area(ArrayList<Signature> signatures){

            List<String> headings = new ArrayList<>();

            for(Signature signature : signatures){
                if(!headings.contains(signature.getArea())){
                    headings.add(signature.getArea());
                }
            }

            return headings;
        }

        private HashMap<String, List<Signature>> getHash(ArrayList<Signature> signatures, List<String> headers){

            HashMap<String, List<Signature>> hashMapRelation = new HashMap<>();

            for(String header : headers){
                List<Signature> tempSignatures = new ArrayList<>();
                for (Signature signature : signatures){
                    if(signature.getArea().equals(header)){
                        tempSignatures.add(signature);
                    }
                }
                hashMapRelation.put(header, tempSignatures);
            }

            return hashMapRelation;
        }
    }


}
