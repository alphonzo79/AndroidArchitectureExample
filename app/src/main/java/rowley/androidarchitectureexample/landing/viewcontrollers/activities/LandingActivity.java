package rowley.androidarchitectureexample.landing.viewcontrollers.activities;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rowley.androidarchitectureexample.R;

/**
 * The main landing activity for the app. Really, this is the only activity, but we'll keep all the
 * structure as example of good practice for organization
 */
public class LandingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>> {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbarSpinner)
    Spinner toolbarSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }
}
