package com.dragon.intec.objects.CustomAdapters;/*
 * Created by HOME on 9/16/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragon.intec.R;
import com.dragon.intec.objects.ClassRoom;
import com.dragon.intec.objects.ClassRooms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapterGroupe extends BaseExpandableListAdapter{

    private Activity activity;
    private List<ClassRoom> _listDataHeader;
    private HashMap<ClassRoom, ClassRooms> _listDataChild;
    private BottomSheetBehavior mBottomSheetBehavior;
    private boolean call = true;

    public ExpandableListAdapterGroupe(Activity activity,
                                       List<ClassRoom> listDataHeader,
                                       HashMap<ClassRoom, ClassRooms> listChildData){
        this.activity = activity;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return this._listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this._listDataChild.get(this._listDataHeader.get(i)).getClassRooms();
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

        ClassRoom classRoomTitle = (ClassRoom) getGroup(i);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.expandable_list_groupe, null);
        }

        ((TextView) view.findViewById(R.id.title_main)).setText(classRoomTitle.getArea());

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        final ArrayList<ClassRoom> classRooms = (ArrayList<ClassRoom>) getChild(i, i1);

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandable_list_child_groupe, null);
        }

        final String heading = classRooms.get(0).getName();

        ((TextView) view.findViewById(R.id.signature_code)).setText(classRooms.get(0).getCode());
        ((TextView) view.findViewById(R.id.signature_name)).setText(heading);
        ((TextView) view.findViewById(R.id.signature_cr)).setText(classRooms.get(0).getCredits());


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("gre", "LOST");
                View bottomSheet = activity.findViewById( R.id.bottom_sheet );
                mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

                LinearLayout list = (LinearLayout) bottomSheet.findViewById(R.id.class_room_list);
                list.removeAllViews();

                LayoutInflater inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
