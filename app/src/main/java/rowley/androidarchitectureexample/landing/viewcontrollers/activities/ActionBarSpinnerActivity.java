package rowley.androidarchitectureexample.landing.viewcontrollers.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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
import rowley.androidarchitectureexample.landing.viewcontrollers.fragments.DataFragmentListener;
import rowley.androidarchitectureexample.landing.viewcontrollers.fragments.DataListFragment;
import rowley.androidarchitectureexample.landing.viewcontrollers.fragments.DataSpinnerFragment;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataLocalDao;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Activity to hold a zip code spinner in the action bar
 */
public class ActionBarSpinnerActivity extends AppCompatActivity implements ZipCodeListView,
        AdapterView.OnItemSelectedListener, DataFragmentListener {
    private static final String INTENT_KEY_VIEW_TYPE = "viewType";
    private static final String VIEW_TYPE_SPINNER = "spinner";
    private static final String VIEW_TYPE_LIST = "list";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbarSpinner)
    Spinner toolbarSpinner;
    @Bind(R.id.click_blocker)
    View clickBlockerView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Inject
    ZipCodeDemographicDataLocalDao sqliteDao;
    @Inject
    ZipCodeDemographicDataDao networkDao;
    @Inject
    SharedPreferences userDefaultSharedPrefs;

    private ZipCodeSpinnerAdapter spinnerAdapter;
    private ZipCodeListPresenter presenter;

    private ZipCodeSelectionReceiver zipCodeSelectionReceiver;

    public static void startActivity(Context context, boolean useListView) {
        Intent intent = new Intent(context, ActionBarSpinnerActivity.class);
        if(useListView) {
            intent.putExtra(INTENT_KEY_VIEW_TYPE, VIEW_TYPE_LIST);
        } else {
            intent.putExtra(INTENT_KEY_VIEW_TYPE, VIEW_TYPE_SPINNER);
        }
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_bar_spinner_activity);

        DaggerInjector.getInstance().getApplicationComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        toolbarSpinner.setOnItemSelectedListener(this);
        getSupportActionBar().setTitle("");

        presenter = new ZipCodeListPresenter(
                new ZipCodeListInteractor(sqliteDao, networkDao, userDefaultSharedPrefs, AndroidSchedulers.mainThread()), this);
        presenter.startPresenter();

        if(VIEW_TYPE_SPINNER.equals(getIntent().getStringExtra(INTENT_KEY_VIEW_TYPE))) {
            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragment_container, DataSpinnerFragment.newInstance()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().
                    add(R.id.fragment_container, DataListFragment.newInstance()).commit();
        }
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
        if(zipCodeSelectionReceiver != null) {
            zipCodeSelectionReceiver.onZipCodeSelected(selectedZipCode);
        }
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

    @Override
    public void registerZipCodeSelectionReceiver(ZipCodeSelectionReceiver receiver) {
        this.zipCodeSelectionReceiver = receiver;
    }

    public interface ZipCodeSelectionReceiver {
        void onZipCodeSelected(String zipCode);
    }
}
