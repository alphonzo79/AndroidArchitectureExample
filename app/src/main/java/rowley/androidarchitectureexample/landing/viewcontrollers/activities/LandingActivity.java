package rowley.androidarchitectureexample.landing.viewcontrollers.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rowley.androidarchitectureexample.R;
import rowley.androidarchitectureexample.core.dagger.DaggerInjector;
import rowley.androidarchitectureexample.landing.adapters.ZipCodeSpinnerAdapter;
import rowley.androidarchitectureexample.landing.interactor.ZipCodeListInteractor;
import rowley.androidarchitectureexample.landing.presenter.ZipCodeListPresenter;
import rowley.androidarchitectureexample.landing.presenter.ZipCodeListView;
import rowley.androidarchitectureexample.landing.viewcontrollers.fragments.LandingFragment;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataNetworkDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataSqliteDao;
import rx.android.schedulers.AndroidSchedulers;

/**
 * The main landing activity for the app. Really, this is the only activity, but we'll keep all the
 * structure as example of good practice for organization
 */
public class LandingActivity extends AppCompatActivity implements ZipCodeListView,
        AdapterView.OnItemSelectedListener, LandingFragment.LandingFragmentListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbarSpinner)
    Spinner toolbarSpinner;
    @Bind(R.id.click_blocker)
    View clickBlockerView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Inject
    ZipCodeDemographicDataSqliteDao sqliteDao;
    @Inject
    ZipCodeDemographicDataNetworkDao networkDao;
    @Inject
    SharedPreferences userDefaultSharedPrefs;

    private ZipCodeSpinnerAdapter spinnerAdapter;
    private ZipCodeListPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);

        DaggerInjector.getInstance().getApplicationComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        toolbarSpinner.setOnItemSelectedListener(this);
        getSupportActionBar().setTitle("");

        presenter = new ZipCodeListPresenter(
                new ZipCodeListInteractor(sqliteDao, networkDao, userDefaultSharedPrefs, AndroidSchedulers.mainThread()), this);
        presenter.startPresenter();
    }

    @Override
    protected void onDestroy() {
        presenter.stopPresenter();
        super.onDestroy();
    }

    @Override
    public void showZipCodeList(List<String> zipCodeList) {
        if(spinnerAdapter == null) {
            spinnerAdapter = new ZipCodeSpinnerAdapter(zipCodeList);
            toolbarSpinner.setAdapter(spinnerAdapter);
        } else {
            spinnerAdapter.setData(zipCodeList);
        }
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void notifyDemographicDataPresenterOfSelectedZipCode(String selectedZipCode) {
        LandingFragment frag = (LandingFragment) getSupportFragmentManager().findFragmentById(R.id.landing_fragment);
        frag.setZipCodeSelected(selectedZipCode);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        presenter.onZipCodeSelected(spinnerAdapter.getItem(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }

    @Override
    public void showProgressBar(boolean show) {
        clickBlockerView.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
