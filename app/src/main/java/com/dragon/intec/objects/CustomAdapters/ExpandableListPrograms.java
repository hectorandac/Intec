package com.dragon.intec.objects.CustomAdapters;
/*
 * Created by Hector Acosta on 10/20/2016.
 */

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dragon.intec.MainActivity;
import com.dragon.intec.R;
import com.dragon.intec.components.FileDownloader;
import com.dragon.intec.objects.ProgramPensum;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ExpandableListPrograms extends BaseExpandableListAdapter {

    private Activity _activity;
    private List<String> _listDataHeader;
    private HashMap<String, List<ProgramPensum.PensumSignature>> _listDataChild;
    private boolean call = true;

    public  ExpandableListPrograms(Activity activity,
                                        List<String> listDataHeader,
                                        HashMap<String, List<ProgramPensum.PensumSignature>> listDataChild)
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

        if(childPosition > 0) {
            final ProgramPensum.PensumSignature signature = (ProgramPensum.PensumSignature) getChild(groupPosition, childPosition);

            LayoutInflater layoutInflater = (LayoutInflater) this._activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_child_program, null);

            TextView code = (TextView) convertView.findViewById(R.id.signature_code);
            TextView name = (TextView) convertView.findViewById(R.id.signature_name);
            TextView cred = (TextView) convertView.findViewById(R.id.signature_cr);
            TextView preR = (TextView) convertView.findViewById(R.id.signature_pre);
            TextView preC = (TextView) convertView.findViewById(R.id.signature_pre_cre);
            ImageButton uriPDF = (ImageButton) convertView.findViewById(R.id.signature_uri_pdf);


            code.setText(signature.getCode());
            name.setText(signature.getName());
            cred.setText(signature.getCr());
            preR.setText(signature.getPrerequisite());
            preC.setText(signature.getReq_cred());
            uriPDF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DownloadPdfShow().execute("UCM1.pdf", "https://procesos.intec.edu.do/pdf/CBM101.pdf");
                }
            });

        }else{
            LayoutInflater layoutInflater = (LayoutInflater) this._activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_child_program_title, null);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private class DownloadPdfShow extends AsyncTask<Object, Void, File>{
        @Override
        protected File doInBackground(Object... params) {

            String fileName = (String) params[0];
            String uri = (String) params[1];

            String fileDir = MainActivity.myDir.getPath();
            File pdfFile = new File(fileDir, fileName);
            try {
                new File(pdfFile.getPath()).delete();
                if(pdfFile.createNewFile())
                    Log.i("FILE##", "Created");
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileDownloader.downloadFile(uri, pdfFile);
            return pdfFile;
        }

        @Override
        protected void onPostExecute(File s) {
            super.onPostExecute(s);
            FileViewIntent(s);
        }
    }

    private void FileViewIntent(File pdfFile){
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try{
            _activity.startActivity(pdfIntent);
        }catch(ActivityNotFoundException e){
            Toast.makeText(_activity, "No tienes aplicaciones para mostrar el PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
