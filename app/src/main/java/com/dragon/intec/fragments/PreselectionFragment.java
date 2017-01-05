package com.dragon.intec.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dragon.intec.R;
import com.dragon.intec.components.TokenRequester;
import com.dragon.intec.objects.CustomAdapters.ExpandableListAdapter;
import com.dragon.intec.objects.Signature;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PreselectionFragment extends Fragment {

    private static final String keyToken = "TOKEN";
    private TableLayout tableLayout = null;
    public static List<Signature> send = new ArrayList<>();


    public PreselectionFragment(){
        //required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_preselection_fragment, container, false);
    }

    @Override
    public void onViewCreated(View dview, Bundle savedInstanceState) {
        super.onViewCreated(dview, savedInstanceState);

        final View view = dview;

        final Context context = getActivity();
        ImageButton confirmation = (ImageButton) view.findViewById(R.id.selection);

        final RelativeLayout confirmation_l = (RelativeLayout) view.findViewById(R.id.confirmation);

        confirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View fview) {

                new PreselectionFragment.getPreselection().execute(context, true);
                new PreselectionFragment.PrepareView().execute(getActivity(), "");

                tableLayout = (TableLayout) view.findViewById(R.id.values_holder);
                tableLayout.removeAllViews();
                send.clear();

                confirmation_l.setVisibility(View.GONE);

                Button cancel = (Button) view.findViewById(R.id.cancel);
                Button save = (Button) view.findViewById(R.id.save);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View fview) {
                        confirmation_l.setVisibility(View.VISIBLE);
                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View fview) {
                        new PreselectionFragment.Save().execute(context);
                        confirmation_l.setVisibility(View.VISIBLE);
                    }
                });

                ((SearchView)view.findViewById(R.id.search_box)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        new PreselectionFragment.PrepareView().execute(getActivity(), query);

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
                ((SearchView)view.findViewById(R.id.search_box)).setOnCloseListener(new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {

                        new PreselectionFragment.PrepareView().execute(getActivity(), "");
                        return false;
                    }
                });
            }
        });
    }

    private class Save extends AsyncTask<Context, Void, Void>{
        @Override
        protected Void doInBackground(Context... contexts) {

            SharedPreferences sharedPref = contexts[0].getSharedPreferences("token", 0);
            String token = sharedPref.getString(keyToken, "");
            JSONArray array = new JSONArray();
            for(Signature s : send){
                JSONObject object = new JSONObject();
                try {
                    object.put("type", s.getPreSelectionType());
                    object.put("id", s.getId());
                    object.put("code", s.getCode());
                    object.put("nameClass", s.getName());
                    object.put("cre", s.getCr());
                    object.put("area", s.getArea());
                    object.put("preRequirements", s.getPrerequisite());
                    object.put("preRequirementCredits", s.getReq_cred());
                    object.put("uriPDF", s.getUriPDF());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                array.put(object);
            }

            try {
                new TokenRequester(token).postJSONArray("http://angularjsauthentication20161012.azurewebsites.net/api/Preselection/Save", array);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class getPreselection extends AsyncTask<Object, Void, JSONArray> {

        boolean check = false;

        @Override
        protected JSONArray doInBackground(Object... objects) {

            SharedPreferences sharedPref = ((Context)objects[0]).getSharedPreferences("token", 0);
            String token = sharedPref.getString(keyToken, "");
            check = (boolean) objects[1];

            JSONArray jsonArray = null;

            try {
                jsonArray = new TokenRequester(token).getArray("http://angularjsauthentication20161012.azurewebsites.net/api/Preselection", "GET");
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray array) {
            super.onPostExecute(array);

            View view = getView();
            final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Log.i("Selection##", array.toString());

            Signature[] signatures = new Signature[array.length()];

            for(int i = 0; i < array.length(); i++) {
                try {
                    Signature signature = Signature.parseToSignature(array.getJSONObject(i));
                    signatures[i] = signature;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            assert view != null;
            final TableLayout tableLayout = (TableLayout) view.findViewById(R.id.values_holder);

            for(final Signature signature : signatures){
                final TableRow tableRow = (TableRow) inflater.inflate(R.layout.layout_preselection_signature, null);
                ((TextView) tableRow.findViewById(R.id.code)).setText(signature.getCode());
                ((TextView) tableRow.findViewById(R.id.signature)).setText(signature.getName());
                ((TextView) tableRow.findViewById(R.id.credits)).setText(signature.getCr());
                ((TextView) tableRow.findViewById(R.id.pre_req)).setText(signature.getPrerequisite());
                ((TextView) tableRow.findViewById(R.id.pre_req_cre)).setText(signature.getReq_cred());

                ArrayList<String> data = new ArrayList<>();
                data.add("Matutina");
                data.add("Vespertina");

                Spinner spinner = ((Spinner) tableRow.findViewById(R.id.spinner));
                ImageButton remove = (ImageButton) tableRow.findViewById(R.id.cancel_pre);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, data);
                spinner.setAdapter(adapter);

                switch (signature.getPreSelectionType()){
                    case 0:
                        spinner.setSelection(0);
                        tableRow.setBackgroundColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.softColorAccent, null));
                        send.add(signature);
                        break;
                    case 1:
                        spinner.setSelection(1);
                        tableRow.setBackgroundColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.softColorAccent, null));
                        send.add(signature);
                        break;
                    case -1:
                        spinner.setSelected(false);
                        tableRow.setBackgroundColor(ResourcesCompat.getColor(getActivity().getResources(), android.R.color.white, null));
                        break;
                }

                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            send.remove(send.indexOf(signature));
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        tableLayout.removeView(tableRow);

                        for(Signature s : send) {
                            Log.i("TO_SEND", s.getName());
                        }
                    }
                });

                spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            tableRow.setBackgroundColor(ResourcesCompat.getColor(getActivity().getResources(), R.color.softColorAccent, null));
                            try {
                                send.remove(send.indexOf(signature));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            signature.setPreSelectionType(i);
                            send.add(signature);

                            for (Signature s : send) {
                                Log.i("TO_SEND", s.getName() + " " + s.getPreSelectionType());
                            }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                tableLayout.addView(tableRow);
            }
        }
    }

    private class PrepareView extends AsyncTask<Object, Void, Void>{

        private Activity activity;
        private Signature[][] listDataChild;
        private String[] listDataHeader;

        @Override
        protected Void doInBackground(Object... params) {

            activity = (Activity) params[0];
            String name = (String) params[1];
            List<Signature> signatures = null;

            try {
                signatures = Signature.getSignatures(activity, name);
                //classRooms = ClassRoom.getClassrooms(activity);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            listDataHeader = getHeaders_Area(signatures);
            listDataChild = getSeparatedSignatures(signatures, listDataHeader);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            @SuppressWarnings("ConstantConditions")
            ExpandableListView expandableList = (ExpandableListView) getView().findViewById(R.id.expandable_list_view);
            ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(listDataHeader, listDataChild, activity, tableLayout, null);
            expandableList.setAdapter(expandableListAdapter);

        }

        private String[] getHeaders_Area(List<Signature> signatures){

            List<String> headings = new ArrayList<>();

            for(Signature signature : signatures){
                if(!headings.contains(signature.getArea())){
                    headings.add(signature.getArea());
                }
            }

            return headings.toArray(new String[headings.size()]);
        }

        private Signature[][] getSeparatedSignatures(List<Signature> signatures, String[] headers){

            Signature[][] signatures_list = new Signature[headers.length][];

            for(int i = 0; i< headers.length; i++){
                List<Signature> tempSignatures = new ArrayList<>();
                for (Signature signature : signatures){
                    if(signature.getArea().equals(headers[i])){
                        tempSignatures.add(signature);
                    }
                }
                signatures_list[i] = tempSignatures.toArray(new Signature[tempSignatures.size()]);
            }

            return signatures_list;
        }
    }
}
