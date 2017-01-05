package com.dragon.intec.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dragon.intec.R;
import com.dragon.intec.components.TokenRequester;
import com.dragon.intec.objects.ClassRoom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RetireFragment extends Fragment {

    private String keyTokenPersonal = "";
    ProgressBar bar;
    View incorrect;
    View confirmation;
    List<ClassRoom> listC = new ArrayList<>();
    TableLayout tableLayout;

    public RetireFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_retire_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tableLayout = (TableLayout) view.findViewById(R.id.values_holder);

        final EditText a = ((EditText) view.findViewById(R.id.id_text_r));
        final EditText b = ((EditText) view.findViewById(R.id.password_text_r));

        ImageButton send = (ImageButton) view.findViewById(R.id.show_back);
        bar = (ProgressBar) view.findViewById(R.id.load);
        bar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        incorrect = view.findViewById(R.id.incorrect);

        bar.setVisibility(View.INVISIBLE);
        incorrect.setVisibility(View.INVISIBLE);
        confirmation = view.findViewById(R.id.confirmation);
        confirmation.setVisibility(View.VISIBLE);

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bar.setVisibility(View.INVISIBLE);
                incorrect.setVisibility(View.INVISIBLE);
                listC.clear();
                confirmation.setVisibility(View.VISIBLE);
                tableLayout.removeAllViews();

                a.setText("");
                b.setText("");
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View fview) {

                String user = a.getText().toString();
                String password = b.getText().toString();

                Log.i("CA#", user + " " + password);
                new LogInRequest().execute(user, password, getActivity());
                bar.setVisibility(View.VISIBLE);
                listC.clear();
                tableLayout.removeAllViews();

                a.setText("");
                b.setText("");
            }
        });

        view.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(ClassRoom classRoom : listC){

                    /*
                    *
                    *
                    *
                    *
                    *******************This space is  reserved to apply the conditions needed
                    *
                    *
                    *
                    */

                    new retire().execute(classRoom);
                }
                bar.setVisibility(View.INVISIBLE);
                incorrect.setVisibility(View.INVISIBLE);
                listC.clear();
                confirmation.setVisibility(View.VISIBLE);
                tableLayout.removeAllViews();

                a.setText("");
                b.setText("");
            }
        });
    }

    //Authenticates the user
    public class LogInRequest extends AsyncTask<Object, Void, String> {

        Activity activity;
        int authorized = 403;

        @Override
        protected String doInBackground(Object... objects) {

            //Gets user credentials from passed data
            String id = (String) objects[0];
            String password = (String) objects[1];

            //May be used to create the request and start next Activity
            this.activity = (Activity) objects[2];
            HttpURLConnection conn = null;
            try {
                byte[] rawData = ("grant_type=password&username="+id+"&password="+password+"").getBytes();
                URL u = new URL("http://angularjsauthentication20161012.azurewebsites.net/token");
                conn = (HttpURLConnection) u.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                conn.setFixedLengthStreamingMode(rawData.length);
                conn.setRequestProperty("Content-Type", "x-www-form-urlencoded; charset=UTF-8");
                conn.connect();

                OutputStream os = conn.getOutputStream();
                os.write(rawData);

            } catch (IOException e) {
                e.printStackTrace();
            }

            String token = "";
            try {
                assert conn != null;
                authorized = conn.getResponseCode();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
                token = readAll(rd);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return token;
        }

        @Override
        protected void onPostExecute(String token) {
            super.onPostExecute(token);

            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(token);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(authorized == 200) {

                String token_got = null;
                try {
                    assert jsonObject != null;
                    token_got = jsonObject.getString("token_type") + " " + jsonObject.getString("access_token");
                    Log.i("TOKEN###", token_got);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                keyTokenPersonal = token_got;
                confirmation.setVisibility(View.GONE);
                new DisplaySignatures().execute();
            }
            else {
                bar.setVisibility(View.INVISIBLE);
                incorrect.setVisibility(View.VISIBLE);
            }
        }

        private String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }
    }

    public class DisplaySignatures extends AsyncTask<Object, Void, JSONArray>{
        @Override
        protected JSONArray doInBackground(Object... objects) {

            JSONArray jsonArray = null;

            try {
                jsonArray = new TokenRequester(keyTokenPersonal).getArray("http://angularjsauthentication20161012.azurewebsites.net/api/Retire", "GET");
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray array) {
            super.onPostExecute(array);

            final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Log.i("Selection##", array.toString());

            ClassRoom[] classRooms = new ClassRoom[array.length()];

            for(int i = 0; i < array.length(); i++) {
                try {
                    ClassRoom classRoom = ClassRoom.parseToClassRoom(array.getJSONObject(i));
                    classRooms[i] = classRoom;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            tableLayout.removeAllViews();

            for(final ClassRoom classRoom : classRooms) {
                final TableRow tableRow = (TableRow) inflater.inflate(R.layout.layout_retire_fragment_row, null);
                CheckBox checkBox = (CheckBox) tableRow.findViewById(R.id.select);
                ((TextView) tableRow.findViewById(R.id.code)).setText(classRoom.getCode());
                ((TextView) tableRow.findViewById(R.id.signature)).setText(classRoom.getName());
                String sec = "Sec." + classRoom.getSec();
                ((TextView) tableRow.findViewById(R.id.sec)).setText(sec);
                ((TextView) tableRow.findViewById(R.id.room)).setText(classRoom.getRoom());
                ((TextView) tableRow.findViewById(R.id.teacher)).setText(classRoom.getTeacher());

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b) {
                            listC.add(classRoom);
                            for(ClassRoom c: listC){
                                Log.i("Classes$#", c.getName());
                            }
                        }
                        else {
                            listC.remove(classRoom);
                            for(ClassRoom c: listC){
                                Log.i("Classes$#", c.getName());
                            }
                        }
                    }
                });

                tableLayout.addView(tableRow);
            }
        }
    }

    private class retire extends AsyncTask<Object, Void, Void>{

        @Override
        protected Void doInBackground(Object... objects) {

            RetireFormModel formModel = new RetireFormModel((ClassRoom) objects[0], 1000000);
            try {
                new TokenRequester(keyTokenPersonal).postJSONObject("http://angularjsauthentication20161012.azurewebsites.net/api/Retire/Done", formModel.parseToJSON());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class RetireFormModel{
        private int idStu = 0;
        private int classId = 0;

        RetireFormModel(ClassRoom classRoom, int idStu){
            this.idStu = idStu;
            this.classId = Integer.parseInt(classRoom.getId());
        }

        int getIdStu() {
            return idStu;
        }

        public void setIdStu(int idStu) {
            this.idStu = idStu;
        }

        int getClassId() {
            return classId;
        }

        public void setClassId(int classId) {
            this.classId = classId;
        }

        JSONObject parseToJSON() throws JSONException {
            JSONObject obj = new JSONObject();
            obj.put("studentId", this.getIdStu());
            obj.put("classId", this.getClassId());

            return obj;
        }
    }
}
