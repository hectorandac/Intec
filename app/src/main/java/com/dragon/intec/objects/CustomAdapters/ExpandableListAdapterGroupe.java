package com.dragon.intec.objects.CustomAdapters;/*
 * Created by HOME on 9/16/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragon.intec.R;
import com.dragon.intec.components.TokenRequester;
import com.dragon.intec.objects.ClassRoom;
import com.dragon.intec.objects.Signature;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapterGroupe extends BaseExpandableListAdapter{

    private Activity _activity;
    private List<String> _listDataHeader;
    private HashMap<String, List<Signature>> _listDataChild;
    private boolean call = true;

    public  ExpandableListAdapterGroupe(Activity activity,
                                        List<String> listDataHeader,
                                        HashMap<String, List<Signature>> listDataChild)
    {
        this._activity = activity;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listDataChild;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final String headerTitle = (String) getGroup(groupPosition);

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this._activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_groupe, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title_main);
        title.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Signature signature = (Signature) getChild(groupPosition, childPosition);

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this._activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_child, null);
        }

        TextView code = (TextView) convertView.findViewById(R.id.signature_code);
        TextView name = (TextView) convertView.findViewById(R.id.signature_name);
        TextView cred = (TextView) convertView.findViewById(R.id.signature_cr);

        code.setText(signature.getCode());
        name.setText(signature.getName());
        cred.setText(signature.getCr());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShowClasses().execute(_activity, signature.getCode(), signature.getName());
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class ShowClasses extends AsyncTask<Object, Void, Void>{

        private String heading;
        ArrayList<ClassRoom> classRooms;

        @Override
        protected Void doInBackground(Object... params) {

            Activity activity = (Activity) params[0];
            String code = (String) params[1];
            heading = (String) params[2];

            try {
                classRooms = ClassRoom.getClassrooms(activity, code);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            View bottomSheet = _activity.findViewById( R.id.bottom_sheet );
            final BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

            LinearLayout list = (LinearLayout) bottomSheet.findViewById(R.id.class_room_list);
            list.removeAllViews();

            LayoutInflater inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View heading_01 = inflater.inflate(R.layout.exapandable_list_title_01_layout, null);
            list.addView(heading_01);

            if (call) {

                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                call = false;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 0.5s = 500ms
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        call = true;
                    }
                }, 500);
            }

            ((TextView)bottomSheet.findViewById(R.id.heading)).setText(heading);

            for (ClassRoom classRoom: classRooms) {
                View temp = inflater.inflate(R.layout.expandable_list_child_groupe_child, null);

                String[] mon = classRoom.getMon();
                String[] tue = classRoom.getTue();
                String[] wed = classRoom.getWed();
                String[] thu = classRoom.getThu();
                String[] fri = classRoom.getFri();
                String[] sat = classRoom.getSat();

                ((TextView) temp.findViewById(R.id.exp_type)).setText(classRoom.getType());
                ((TextView) temp.findViewById(R.id.exp_sec)).setText(classRoom.getSec());
                ((TextView) temp.findViewById(R.id.exp_room)).setText(classRoom.getRoom());
                ((TextView) temp.findViewById(R.id.exp_teacher)).setText(classRoom.getTeacher());
                ((TextView) temp.findViewById(R.id.exp_mon)).setText(classRoom.fixedTime(mon[0], mon[1]));
                ((TextView) temp.findViewById(R.id.exp_tue)).setText(classRoom.fixedTime(tue[0], tue[1]));
                ((TextView) temp.findViewById(R.id.exp_wed)).setText(classRoom.fixedTime(wed[0], wed[1]));
                ((TextView) temp.findViewById(R.id.exp_thu)).setText(classRoom.fixedTime(thu[0], thu[1]));
                ((TextView) temp.findViewById(R.id.exp_fri)).setText(classRoom.fixedTime(fri[0], fri[1]));
                ((TextView) temp.findViewById(R.id.exp_sat)).setText(classRoom.fixedTime(sat[0], sat[1]));

                list.addView(temp);
            }
        }
    }

}
