package org.iitb.techfest.techfest;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Stack;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    ImageView tf_logo;

    ArrayList<EventSummary> events = new ArrayList<EventSummary>();
    Stack<Fragment> fragStack = new Stack<Fragment>();

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        tf_logo = new ImageView(this);
        tf_logo.setImageResource(R.drawable.tf_logo);

        events.add(new EventSummary(0, R.drawable.tf_icon, R.layout.details_robowars, "Robowars", "Frickin' epic","SOM","17:00","3/01/2015"));
        events.add(new EventSummary(1, R.drawable.tf_icon, R.layout.details_robowars, "Compi 2", "Arbit desc 2","","",""));
        events.add(new EventSummary(2, R.drawable.tf_icon, R.layout.details_robowars, "Compi 3", "Arbit desc 3","","",""));

        restoreActionBar();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment frag;

        if(position==1)
            frag= EventListFragment.newInstance(position+1,R.drawable.competitions,R.layout.fragment_competitions, events);
        else
            frag= PlaceholderFragment.newInstance(position + 1);

        fragStack.push(frag);

        fragmentManager.beginTransaction()
                .replace(R.id.container, frag)
                .commit();
    }

    public void loadDetails(View v){
        EventDetailsFragment eventDetails = EventDetailsFragment.newInstance(events.get(v.getId()));

        fragStack.push(eventDetails);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, eventDetails)
                .commit();
    }

    public void setAlarm(View v){
        EventSummary es = events.get((Integer) v.getTag());

        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_MESSAGE, es.title+" @ "+es.venue);
        i.putExtra(AlarmClock.EXTRA_HOUR, Integer.valueOf(es.time.split(":")[0]));
        i.putExtra(AlarmClock.EXTRA_MINUTES, Integer.valueOf(es.time.split(":")[1]));
        startActivity(i);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        fragStack.pop();

        if(fragStack.empty())
            super.onBackPressed();
        else
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragStack.peek())
                    .commit();
            onSectionAttached(fragStack.peek().getArguments().getInt("section_number"));
            restoreActionBar();
        }
    }
}
