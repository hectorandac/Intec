package com.dragon.intec;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

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

public class LogInActivity extends AppCompatActivity {

    private static final String keyToken = "TOKEN";
    private FrameLayout frameLayout;
    private View incorrectMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        frameLayout = (FrameLayout) findViewById(R.id.loading_screen);
        incorrectMessage = findViewById(R.id.incorrect);

        ((ProgressBar) frameLayout.findViewById(R.id.progressBar)).getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        /*if(!sharedPref.getString(keyToken, "null").equals("null")) {

            frameLayout.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }*/

    }

    //On login button pressed
    public void logIn(View v) {
        //Shows the loading screen
        frameLayout.setVisibility(View.VISIBLE);

        //Hides the keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        //Manage the edit text fields for the password and the id of the user
        EditText id = (EditText) findViewById(R.id.id_text);
        EditText password = (EditText) findViewById(R.id.password_text_r);

        String idText = id.getText().toString();
        String passwordText = password.getText().toString();

        Object[] data = {idText, passwordText, this};

        new LogInRequest().execute(data);
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

                SharedPreferences sharedPref = activity.getSharedPreferences("token", 0);
                SharedPreferences.Editor editor = sharedPref.edit();
                String token_got = null;
                try {
                    assert jsonObject != null;
                    token_got = jsonObject.getString("token_type") + " " + jsonObject.getString("access_token");
                    Log.i("TOKEN###", token_got);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.putString(keyToken, token_got);
                if(editor.commit()) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    startActivity(intent);
                }
            }
            else {
                frameLayout.setVisibility(View.INVISIBLE);
                incorrectMessage.setVisibility(View.VISIBLE);
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


}
