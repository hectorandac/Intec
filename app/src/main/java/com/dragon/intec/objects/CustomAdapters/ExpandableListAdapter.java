package com.dragon.intec.objects.CustomAdapters;

/*
 * Created by hecto on 12/17/2016.
 */

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dragon.intec.R;
import com.dragon.intec.fragments.SelectionFragment;
import com.dragon.intec.objects.Signature;

import java.util.ArrayList;
import java.util.HashMap;

import static com.dragon.intec.fragments.PreselectionFragment.send;
import static com.dragon.intec.fragments.SelectionFragment.mBottomSheetBehavior;
import static com.dragon.intec.fragments.SelectionFragment.open;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private HashMap<String, Signature[]> map = new HashMap<>();
    private String[] areas;
    private Context context;
    private TableLayout tableLayout;
    private static final String keyToken = "TOKEN";
    private View bottomSheet;

    public ExpandableListAdapter(String[] areas, Signature[][] signature, Context context, TableLayout tableLayout, View bottomSheet) {

        this.bottomSheet = bottomSheet;
        this.areas = areas;
        this.context = context;
        this.tableLayout = tableLayout;

        for(int i = 0; i < areas.length; i++){
            map.put(areas[i], signature[i]);
        }
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return areas.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return map.get(areas[i]).length;
    }

    @Override
    public Object getGroup(int i) {
        return areas[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        return map.get(areas[i])[i1];
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_title_el, null);
        }

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText((String)getGroup(i));

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_selection_signature_min, null);
        }

        final Signature signature = (Signature) getChild(i, i1);

        ((TextView) view.findViewById(R.id.code)).setText(signature.getCode());
        ((TextView) view.findViewById(R.id.signature)).setText(signature.getName());
        ((TextView) view.findViewById(R.id.credits)).setText(signature.getCr());
        ((TextView) view.findViewById(R.id.pre_req)).setText(signature.getPrerequisite());
        ((TextView) view.findViewById(R.id.pre_req_cre)).setText(signature.getReq_cred());

        if(bottomSheet != null) {
            view.findViewById(R.id.see_classes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count = tableLayout.getChildCount();
                    boolean there = false;
                    for (int i = 0; i < count; i++) {
                        if (((TextView) tableLayout.getChildAt(i).findViewById(R.id.code)).getText().equals(signature.getCode())) {
                            there = true;
                        }
                    }
                    if (!there) {
                        final TableLayout layout = (TableLayout) bottomSheet.findViewById(R.id.data_holder);
                        layout.removeAllViewsInLayout();

                        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        final TableRow tableRow = (TableRow) inflater.inflate(R.layout.layout_selection_signature, null);
                        ((TextView) tableRow.findViewById(R.id.code)).setText(signature.getCode());
                        ((TextView) tableRow.findViewById(R.id.signature)).setText(signature.getName());
                        ((TextView) tableRow.findViewById(R.id.credits)).setText(signature.getCr());
                        ((TextView) tableRow.findViewById(R.id.pre_req)).setText(signature.getPrerequisite());
                        ((TextView) tableRow.findViewById(R.id.pre_req_cre)).setText(signature.getReq_cred());

                        tableRow.findViewById(R.id.see_classes).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                new SelectionFragment.changeState_r().execute(context, signature.getId(), tableRow);
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
            });
        }else {
            view.findViewById(R.id.see_classes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count = tableLayout.getChildCount();
                    boolean there = false;
                    for (int i = 0; i < count; i++) {
                        if (((TextView) tableLayout.getChildAt(i).findViewById(R.id.code)).getText().equals(signature.getCode())) {
                            there = true;
                        }
                    }
                    if (!there) {
                        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, data);
                        spinner.setAdapter(adapter);

                        switch (signature.getPreSelectionType()){
                            case 0:
                                spinner.setSelection(0);
                                tableRow.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.softColorAccent, null));
                                send.add(signature);
                                break;
                            case 1:
                                spinner.setSelection(1);
                                tableRow.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.softColorAccent, null));
                                send.add(signature);
                                break;
                            case -1:
                                tableRow.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), android.R.color.white, null));
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

                        final boolean[] a = {false};

                        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                if(a[0]) {
                                    tableRow.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.softColorAccent, null));
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
                                }else {
                                    a[0] = true;
                                    for (Signature s : send) {
                                        Log.i("TO_SEND", s.getName() + " " + s.getPreSelectionType());
                                    }
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });

                        tableLayout.addView(tableRow);
                    }
                }
            });

        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {

    }

    @Override
    public void onGroupCollapsed(int i) {

    }

    @Override
    public long getCombinedChildId(long l, long l1) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }

}
