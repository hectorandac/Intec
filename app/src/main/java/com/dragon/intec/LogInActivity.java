package com.dragon.intec;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.dragon.intec.objects.Student;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class LogInActivity extends AppCompatActivity {

    private static final String keyStudent = "STUDENT";
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    //On login button pressed
    public void logIn(View v) {
        //Shows the loading screen
        frameLayout = (FrameLayout) findViewById(R.id.loading_screen);
        ((ProgressBar) frameLayout.findViewById(R.id.progressBar)).getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        frameLayout.setVisibility(View.VISIBLE);

        //Hides the keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        //Manage the edit text fields for the password and the id of the user
        EditText id = (EditText) findViewById(R.id.id_text);
        EditText password = (EditText) findViewById(R.id.password_text);

        String idText = id.getText().toString();
        String passwordText = password.getText().toString();

        Object[] data = {idText, passwordText, this};

        new LogInRequest().execute(data);
    }

    //Authenticates the user
    public class LogInRequest extends AsyncTask<Object, Void, Student> {

        Context context;

        @Override
        protected Student doInBackground(Object... objects) {

            //Gets user credentials from passed data
            String id = (String) objects[0];
            String password = (String) objects[1];

            //May be used to create the request and start next Activity
            this.context = (Context) objects[2];

            byte[] rawData = ("{\"Username\": \""+ id +"\", \"Password\": \""+ password +"\"}").getBytes();
            URL u;
            HttpURLConnection conn = null;
            try {
                u = new URL("http://cppapp.azurewebsites.net/Account/MobileLogin");
                conn = (HttpURLConnection) u.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                conn.setFixedLengthStreamingMode(rawData.length);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.connect();

                OutputStream os = conn.getOutputStream();
                os.write(rawData);


            } catch (IOException e) {
                e.printStackTrace();
            }

            Student student = null;
            try {
                assert conn != null;
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
                student = new Student(readAll(rd), (Activity)objects[2]);
                student.getData();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return student;
        }

        @Override
        protected void onPostExecute(Student student) {
            super.onPostExecute(student);

            if(student.getToken().length() > 1) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(keyStudent, student);
                startActivity(intent);
                finish();
            }
            else {
                frameLayout.setVisibility(View.INVISIBLE);
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
