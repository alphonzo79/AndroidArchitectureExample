package rowley.androidarchitectureexample.landing.viewcontrollers.activities;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rowley.androidarchitectureexample.R;
import rowley.androidarchitectureexample.core.dagger.DaggerInjector;
import rowley.androidarchitectureexample.core.io.local.IDatabaseConfig;
import rowley.androidarchitectureexample.core.io.network.NetworkRequestHelper;
import rowley.androidarchitectureexample.landing.adapters.ZipCodeSpinnerAdapter;
import rowley.androidarchitectureexample.landing.viewcontrollers.fragments.LandingFragment;
import rowley.androidarchitectureexample.nycdemographic.loader.ZipCodeListLoader;

/**
 * The main landing activity for the app. Really, this is the only activity, but we'll keep all the
 * structure as example of good practice for organization
 */
public class LandingActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<String>>, AdapterView.OnItemSelectedListener {
    private final int ZIP_CODE_LIST_LOADER_ID = 1;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbarSpinner)
    Spinner toolbarSpinner;

    @Inject
    IDatabaseConfig databaseConfig;
    @Inject
    NetworkRequestHelper networkRequestHelper;

    private ZipCodeSpinnerAdapter spinnerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);

        DaggerInjector.getInstance().getApplicationComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        toolbarSpinner.setOnItemSelectedListener(this);
        getSupportActionBar().setTitle("");

        getSupportLoaderManager().initLoader(ZIP_CODE_LIST_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        if(id == ZIP_CODE_LIST_LOADER_ID) {
            return new ZipCodeListLoader(this, databaseConfig, networkRequestHelper, getString(R.string.app_token));
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
        if(spinnerAdapter == null) {
            spinnerAdapter = new ZipCodeSpinnerAdapter(data);
        } else {
            spinnerAdapter.setData(data);
        }

        toolbarSpinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        LandingFragment frag = (LandingFragment) getSupportFragmentManager().findFragmentById(R.id.landing_fragment);
        frag.setZipCodeSelected(spinnerAdapter.getItem(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }
}
