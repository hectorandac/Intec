package com.dragon.intec.objects.CustomAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dragon.intec.R;
import com.dragon.intec.objects.ClassRoom;

import java.util.HashMap;
import java.util.List;

/*
 * Created by HOME on 9/15/2016.
 */
public class ExpandableListAdapterChild extends BaseExpandableListAdapter {

    private final double mDensity;
    private Context context;
    private List<ClassRoom> _listDataHeader;
    private HashMap<ClassRoom, List<ClassRoom>> _listDataChild;

    public ExpandableListAdapterChild(Context context,
                                      List<ClassRoom> listDataHeader,
                                      HashMap<ClassRoom, List<ClassRoom>> listChildData){
        this.context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        mDensity = context.getResources().getDisplayMetrics().density;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this._listDataChild.get(this._listDataHeader.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return this._listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this._listDataChild.get(this._listDataHeader.get(i))
                .get(i1);
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
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.expandable_list_child_groupe, null);
        }

        ((TextView) view.findViewById(R.id.signature_code)).setText(classRoomTitle.getCode());
        ((TextView) view.findViewById(R.id.signature_name)).setText(classRoomTitle.getName());
        ((TextView) view.findViewById(R.id.signature_cr)).setText(classRoomTitle.getCredits());

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

        final ClassRoom classRoom = (ClassRoom) getChild(i, i1);
        LayoutInflater inflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.expandable_list_child_groupe_child, null);

        String mon[] = classRoom.getMon();
        String tue[] = classRoom.getTue();
        String wed[] = classRoom.getWed();
        String thu[] = classRoom.getThu();
        String fri[] = classRoom.getFri();
        String sat[] = classRoom.getSat();

        ((TextView) view.findViewById(R.id.exp_type)).setText(classRoom.getType());
        ((TextView) view.findViewById(R.id.exp_sec)).setText(classRoom.getSec());
        ((TextView) view.findViewById(R.id.exp_room)).setText(classRoom.getRoom());
        ((TextView) view.findViewById(R.id.exp_teacher)).setText(classRoom.getTeacher());
        ((TextView) view.findViewById(R.id.exp_mon)).setText(classRoom.fixedTime(mon[0], mon[1]));
        ((TextView) view.findViewById(R.id.exp_tue)).setText(classRoom.fixedTime(tue[0], tue[1]));
        ((TextView) view.findViewById(R.id.exp_wed)).setText(classRoom.fixedTime(wed[0], wed[1]));
        ((TextView) view.findViewById(R.id.exp_thu)).setText(classRoom.fixedTime(thu[0], thu[1]));
        ((TextView) view.findViewById(R.id.exp_fri)).setText(classRoom.fixedTime(fri[0], fri[1]));
        ((TextView) view.findViewById(R.id.exp_sat)).setText(classRoom.fixedTime(sat[0], sat[0]));

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.header);

        ((LinearLayout) view.findViewById(R.id.container_fields)).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Log.i("TEST", view.toString());
                if (frameLayout.getVisibility() == View.GONE){
                    frameLayout.setVisibility(View.VISIBLE);
                }else{
                    frameLayout.setVisibility(View.GONE);
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
