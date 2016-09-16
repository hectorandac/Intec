package com.dragon.intec.objects.CustomAdapters;/*
 * Created by HOME on 9/16/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
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

    private List<ClassRoom> listDataHeader;
    private HashMap<ClassRoom, List<ClassRoom>> listDataChild;

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
        return this._listDataChild.get(this._listDataHeader.get(i));
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

        final ClassRooms classRooms = (ClassRooms) getChild(i, 0);
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.expandable_list_child, null);

        // get the listview
        ExpandableListView expListView = (ExpandableListView) view.findViewById(R.id.exp_container_child);

        // preparing list data
        prepareData(classRooms.getClassRooms());
        ExpandableListAdapterChild listAdapter = new ExpandableListAdapterChild(activity, listDataHeader, listDataChild);
        listAdapter.notifyDataSetChanged();

        // setting list adapter
        expListView.setAdapter(listAdapter);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    private void prepareData(ArrayList<ClassRoom> classRooms) {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        ClassRoom classRoomHeader;
        List<ClassRoom> tempArray;

        ArrayList<ClassRoom> temp = new ArrayList<>();
        for (int i = 0; i < classRooms.size(); i++){
            temp.add(classRooms.get(i));
        }

        while (temp.size() > 0) {
            String name = temp.get(0).getName();

            classRoomHeader = null;
            tempArray = new ArrayList<>();

            for (ClassRoom classRoom : temp) {
                if(classRoom.getName().equals(name)){
                    tempArray.add(classRoom);
                    classRoomHeader = classRoom;
                }
            }


            if(classRoomHeader != null) {
                listDataHeader.add(classRoomHeader);
                listDataChild.put(classRoomHeader, tempArray);
            }

            for (ClassRoom c : tempArray){
                temp.remove(c);
            }

        }

    }
}
