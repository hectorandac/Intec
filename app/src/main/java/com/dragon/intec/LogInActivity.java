package com.dragon.intec;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dragon.intec.objects.Student;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    //On login button pressed
    public void logIn(View v) {
        //Shows the loading screen
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.loading_screen);
        ((ProgressBar) frameLayout.findViewById(R.id.progressBar)).getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);;
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
    public class LogInRequest extends AsyncTask<Object, Void, Void> {

        Context context;

        @Override
        protected Void doInBackground(Object... objects) {

            //Gets user credentials from passed data
            String id = (String) objects[0];
            String password = (String) objects[1];

            //May be used to create the request and start next Activity
            Context context = (Context) objects[2];
            this.context = context;

            Log.i("Credentials_id", id);
            Log.i("Credentials_pss", password);

            //For debugging purpose
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }


}
