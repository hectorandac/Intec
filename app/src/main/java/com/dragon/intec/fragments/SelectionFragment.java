package com.dragon.intec.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dragon.intec.R;
import com.dragon.intec.components.TokenRequester;
import com.dragon.intec.objects.ClassRoom;
import com.dragon.intec.objects.CustomAdapters.ExpandableListAdapter;
import com.dragon.intec.objects.Signature;
import com.dragon.intec.objects.SignatureClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectionFragment extends Fragment {

    private static final String keyToken = "TOKEN";
    public static BottomSheetBehavior<View> mBottomSheetBehavior;
    @SuppressLint("StaticFieldLeak")
    public static View bottomSheet;
    private TableLayout tableLayout = null;

    public SelectionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_selection_fragment, container, false);
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

                new Cancel().execute(context);
                new getSelection().execute(context, true);
                new PrepareView().execute(getActivity(), "");
                tableLayout = (TableLayout) view.findViewById(R.id.values_holder);
                tableLayout.removeAllViews();

                confirmation_l.setVisibility(View.GONE);

                Button cancel = (Button) view.findViewById(R.id.cancel);
                Button save = (Button) view.findViewById(R.id.save);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View fview) {
                        new Cancel().execute(context);
                        confirmation_l.setVisibility(View.VISIBLE);
                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View fview) {
                        new Save().execute(context);
                        confirmation_l.setVisibility(View.VISIBLE);
                    }
                });

                bottomSheet = view.findViewById( R.id.bottom_sheet );
                mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
                mBottomSheetBehavior.setHideable(true);

                mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        // React to state change
                        Log.i("STATE##", newState+"");
                        if(newState == 5){
                            open = false;
                        }
                    }
                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        // React to dragging events
                    }
                });

                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                bottomSheet.setVisibility(View.VISIBLE);

                ((SearchView)view.findViewById(R.id.search_box)).setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        new PrepareView().execute(getActivity(), query);

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

                        new PrepareView().execute(getActivity(), "");

                        return false;
                    }
                });
            }
        });
    }

    private class Cancel extends AsyncTask<Context, Void, Void>{
        @Override
        protected Void doInBackground(Context... contexts) {

            SharedPreferences sharedPref = contexts[0].getSharedPreferences("token", 0);
            String token = sharedPref.getString(keyToken, "");

            try {
                new TokenRequester(token).postNoneReturn("http://angularjsauthentication20161012.azurewebsites.net/api/Selection/CancelSelection");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class Save extends AsyncTask<Context, Void, Void>{
        @Override
        protected Void doInBackground(Context... contexts) {

            SharedPreferences sharedPref = contexts[0].getSharedPreferences("token", 0);
            String token = sharedPref.getString(keyToken, "");

            try {
                new TokenRequester(token).postNoneReturn("http://angularjsauthentication20161012.azurewebsites.net/api/Selection/SaveSelection");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class getSelection extends AsyncTask<Object, Void, JSONArray> {

        boolean check = false;

        @Override
        protected JSONArray doInBackground(Object... objects) {

            SharedPreferences sharedPref = ((Context)objects[0]).getSharedPreferences("token", 0);
            String token = sharedPref.getString(keyToken, "");
            check = (boolean) objects[1];

            JSONArray jsonArray = null;

            try {
                jsonArray = new TokenRequester(token).getArray("http://angularjsauthentication20161012.azurewebsites.net/api/Selection", "GET");
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

            SignatureClasses[] signatureClasses_s = new SignatureClasses[array.length()];

            for(int i = 0; i < array.length(); i++) {
                try {
                    SignatureClasses signatureClasses = SignatureClasses.parseToSignatureClasses(array.getJSONObject(i));
                    signatureClasses_s[i] = signatureClasses;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            assert view != null;
            final TableLayout tableLayout = (TableLayout) view.findViewById(R.id.values_holder);

            for(final SignatureClasses signatureClass : signatureClasses_s){
                final TableRow tableRow = (TableRow) inflater.inflate(R.layout.layout_selection_signature, null);
                ((TextView) tableRow.findViewById(R.id.code)).setText(signatureClass.getCode());
                ((TextView) tableRow.findViewById(R.id.signature)).setText(signatureClass.getName());
                ((TextView) tableRow.findViewById(R.id.credits)).setText(signatureClass.getCr());
                ((TextView) tableRow.findViewById(R.id.pre_req)).setText(signatureClass.getPrerequisite());
                ((TextView) tableRow.findViewById(R.id.pre_req_cre)).setText(signatureClass.getReq_cred());

                new changeState_r().execute(getActivity(), signatureClass.getId(), tableRow, check);

                tableRow.findViewById(R.id.see_classes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new changeState_r().execute(getActivity(), signatureClass.getId(), tableRow);
                        final boolean[] call = {true};

                        if (call[0]) {

                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            call[0] = false;
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Do something after 0.5s = 500ms
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                    open = true;
                                    call[0] = true;
                                }
                            }, 500);
                        }
                    }
                });

                tableLayout.addView(tableRow);
            }


        }
    }

    public static class changeState extends AsyncTask<Object, Void, Integer>{

        TableRow finalTableRowData;
        String token;
        ClassRoom class_c;

        @Override
        protected Integer doInBackground(Object... obj) {

            finalTableRowData = (TableRow) obj[0];
            token = (String) obj[1];
            class_c = (ClassRoom) obj[2];
            Integer val = 0;
            try {
                val = Integer.parseInt(new TokenRequester(token).postObject("http://angularjsauthentication20161012.azurewebsites.net/api/Selection/Availability?id=" + class_c.getId()));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return val;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            runCheck(finalTableRowData, token, class_c);
            ((TextView) finalTableRowData.findViewById(R.id.cupo)).setText(String.valueOf(integer));
        }
    }

    public static void unCheckOthers(CheckBox cur, TableLayout iterate, int CheckId){
        for(int i = 1; i < iterate.getChildCount(); i++){
            TableRow c =  (TableRow) iterate.getChildAt(i);
            CheckBox checkBoxC = (CheckBox) c.findViewById(CheckId);
            if(checkBoxC != cur){
                checkBoxC.setChecked(false);

            }
        }

    }

    public static boolean open = true;

    public static void runCheck(final TableRow finalTableRowData, final String token, final ClassRoom class_c){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 0.5s = 500ms
                if(open) {
                    new changeState().execute(finalTableRowData, token, class_c);
                }
            }
        }, 2000);
    }

    public static class changeState_r extends AsyncTask<Object, Void, JSONObject>{

        String token;
        Context context;
        TableRow signatureAssociated;
        boolean check = false;

        @Override
        protected JSONObject doInBackground(Object... objects) {

            context = (Context)objects[0];
            signatureAssociated  = (TableRow) objects[2];
            try {
                check = (boolean) objects[3];
            }catch (Exception ex){
                ex.printStackTrace();
            }

            SharedPreferences sharedPref = context.getSharedPreferences("token", 0);
            token = sharedPref.getString(keyToken, "");

            String id = (String)objects[1];
            JSONObject jsonObject = null;
            Log.i("CLASS_ID##", id + " " + token);

            String query = "http://angularjsauthentication20161012.azurewebsites.net/api/Selection?signatureId="+id;

            try {
                jsonObject = new TokenRequester(token).getJSONObject(query);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);
            SignatureClasses signatureClasses = null;
            try {
                signatureClasses = SignatureClasses.parseToSignatureClasses(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            final TableLayout layout = (TableLayout) bottomSheet.findViewById(R.id.data_holder);
            layout.removeAllViewsInLayout();

            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            TableRow tableRowDataH = (TableRow) inflater.inflate(R.layout.layout_title_sheet_1, null);
            layout.addView(tableRowDataH);

            assert signatureClasses != null;
            for (final ClassRoom class_c : signatureClasses.getClasses()) {
                TableRow tableRowData = (TableRow) inflater.inflate(R.layout.layout_title_sheet_2, null);
                ((TextView) tableRowData.findViewById(R.id.tipo)).setText(class_c.getType());
                ((TextView) tableRowData.findViewById(R.id.sec)).setText(class_c.getSec());
                ((TextView) tableRowData.findViewById(R.id.room)).setText(class_c.getRoom());
                ((TextView) tableRowData.findViewById(R.id.teacher)).setText(class_c.getTeacher());
                ((TextView) tableRowData.findViewById(R.id.lun)).setText(ClassRoom.fixedTime(class_c.getMon()));
                ((TextView) tableRowData.findViewById(R.id.mar)).setText(ClassRoom.fixedTime(class_c.getTue()));
                ((TextView) tableRowData.findViewById(R.id.mie)).setText(ClassRoom.fixedTime(class_c.getWed()));
                ((TextView) tableRowData.findViewById(R.id.jue)).setText(ClassRoom.fixedTime(class_c.getThu()));
                ((TextView) tableRowData.findViewById(R.id.vie)).setText(ClassRoom.fixedTime(class_c.getFri()));
                ((TextView) tableRowData.findViewById(R.id.sab)).setText(ClassRoom.fixedTime(class_c.getSat()));
                ((TextView) tableRowData.findViewById(R.id.cupo)).setText(String.valueOf(class_c.getSpace()));

                final CheckBox checkBox = (CheckBox) tableRowData.findViewById(R.id.selection);

                if(class_c.getId_code_1().length() > 1){
                    checkBox.setChecked(true);
                    signatureAssociated.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.softColorAccent, null));
                }

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            signatureAssociated.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), android.R.color.transparent, null));
                            unCheckOthers(checkBox, layout, R.id.selection);
                            JSONObject user = new JSONObject();
                            try {
                                user.put("id", HomeFragment.student.getId());
                                user.put("classId", class_c.getId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            new select().execute(context, user);
                            signatureAssociated.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.softColorAccent, null));
                        }else{
                            signatureAssociated.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), android.R.color.transparent, null));
                            JSONObject user = new JSONObject();
                            try {
                                user.put("id", HomeFragment.student.getId());
                                user.put("classId", class_c.getId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            new cancel_select().execute(context, user);
                        }
                    }
                });
                layout.addView(tableRowData);

                if(!check){
                    runCheck(tableRowData, token, class_c);
                }
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
            ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(listDataHeader, listDataChild, activity, tableLayout, bottomSheet);
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

    public static class cancel_select extends AsyncTask<Object, Void, String>{

        @Override
        protected String doInBackground(Object... objects) {

            SharedPreferences sharedPref = ((Context)objects[0]).getSharedPreferences("token", 0);
            String token = sharedPref.getString(keyToken, "");

            String respond = null;

            try {
                respond = new TokenRequester(token).makeRequest("http://angularjsauthentication20161012.azurewebsites.net/api/Selection/UpGrade", (JSONObject) objects[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return respond;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public static class select extends AsyncTask<Object, Void, String>{

        @Override
        protected String doInBackground(Object... objects) {

            SharedPreferences sharedPref = ((Context)objects[0]).getSharedPreferences("token", 0);
            String token = sharedPref.getString(keyToken, "");

            String respond = null;

            try {
                respond = new TokenRequester(token).makeRequest("http://angularjsauthentication20161012.azurewebsites.net/api/Selection/DownGrade", (JSONObject) objects[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return respond;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}

