package com.dragon.intec;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dragon.intec.fragments.AcademicOfferFragment;
import com.dragon.intec.fragments.BookFragment;
import com.dragon.intec.fragments.CalendarFragment;
import com.dragon.intec.fragments.GradesRevisionFragment;
import com.dragon.intec.fragments.HomeFragment;
import com.dragon.intec.fragments.PreselectionFragment;
import com.dragon.intec.fragments.ReportsFragment;
import com.dragon.intec.fragments.RetireFragment;
import com.dragon.intec.fragments.SelectionFragment;
import com.dragon.intec.fragments.SignaturesProgramsFragment;
import com.dragon.intec.fragments.TeacherEvaluationFragment;
import com.dragon.intec.objects.Student;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public NavigationView navigationView;
    //Determines the current position of th menu
    int currentPosition = 0;
    //Key attached to the value of the position on view created
    private static final String keyPosition = "POSITION";
    private static final String keyStudent = "STUDENT";

    public Student student;

    public Student getStudent() {
        return student;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        student = bundle.getParcelable(keyStudent);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment f = null;

        //Check for the last fragment and displays it depending on the past position if any position stored.
        if (savedInstanceState != null){
            int position = savedInstanceState.getInt(keyPosition);
            Log.i("POSITION_reported", String.valueOf(position));
            switch(position) {
                case 0: f = getFragment(position); break;
                case 1: f = getFragment(position); break;
                case 2: f = getFragment(position); break;
                case 3: f = getFragment(position); break;
                case 4: f = getFragment(position); break;
                case 5: f = getFragment(position); break;
                case 6: f = getFragment(position); break;
                case 7: f = getFragment(position); break;
                case 8: f = getFragment(position); break;
                case 9: f = getFragment(position); break;
                case 10: f = getFragment(position); break;
            }
        }
        //Get the first fragment in case no position stored
        else {
            f = getFragment(0);
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout, f); // f_container is your FrameLayout container
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //noinspection deprecation
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    //Returns the fragment depending on the position
    private Fragment getFragment(int position){
        navigationView.getMenu().getItem(position).setChecked(true);
        setMenuItemId(position);

        Fragment f = null;

        if (position == 0) {
            f = new HomeFragment();
        } else if (position == 1){
            f = new PreselectionFragment();
        } else if (position == 2){
            f = new SelectionFragment();
        } else if (position == 3){
            f = new RetireFragment();
        } else if (position == 4){
            f = new TeacherEvaluationFragment();
        } else if (position == 5){
            f = new ReportsFragment();
        } else if (position == 6){
            f = new GradesRevisionFragment();
        } else if (position == 7){
            f = new SignaturesProgramsFragment();
        } else if (position == 8){
            f = new AcademicOfferFragment();
        } else if (position == 9){
            f = new BookFragment();
        } else if (position == 10){
            f = new CalendarFragment();
        }

        return f;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Modifies the position of the current fragment
    private void setMenuItemId(int menuItemId) {
        this.currentPosition = menuItemId;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int menuItemId = item.getItemId();
        Log.i("POSITION_fragment", String.valueOf(menuItemId));

        Fragment f = null;

        if (menuItemId == R.id.nav_home_process) {
            f = new HomeFragment();
            setMenuItemId(0);
        } else if (menuItemId == R.id.nav_preselection_process){
            f = new PreselectionFragment();
            setMenuItemId(1);
        } else if (menuItemId == R.id.nav_selection_process){
            f = new SelectionFragment();
            setMenuItemId(2);
        } else if (menuItemId == R.id.nav_retire_process){
            f = new RetireFragment();
            setMenuItemId(3);
        } else if (menuItemId == R.id.nav_teachers_evaluation){
            f = new TeacherEvaluationFragment();
            setMenuItemId(4);
        } else if (menuItemId == R.id.nav_reports){
            f = new ReportsFragment();
            setMenuItemId(5);
        } else if (menuItemId == R.id.nav_grades){
            f = new GradesRevisionFragment();
            setMenuItemId(6);
        } else if (menuItemId == R.id.nav_signatures_programs){
            f = new SignaturesProgramsFragment();
            setMenuItemId(7);
        } else if (menuItemId == R.id.nav_academic_offer){
            f = new AcademicOfferFragment();
            setMenuItemId(8);
        } else if (menuItemId == R.id.nav_book){
            f = new BookFragment();
            setMenuItemId(9);
        } else if (menuItemId == R.id.nav_calendar){
            f = new CalendarFragment();
            setMenuItemId(10);
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout, f); // f is your FrameLayout container
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("POSITION_transition", String.valueOf(currentPosition));
        outState.putInt(keyPosition, currentPosition);
    }

}
