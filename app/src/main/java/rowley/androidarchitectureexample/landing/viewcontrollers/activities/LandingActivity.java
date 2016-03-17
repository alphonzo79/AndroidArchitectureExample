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
import butterknife.OnClick;
import rowley.androidarchitectureexample.R;
import rowley.androidarchitectureexample.core.dagger.DaggerInjector;
import rowley.androidarchitectureexample.landing.adapters.ZipCodeSpinnerAdapter;
import rowley.androidarchitectureexample.landing.interactor.ZipCodeListInteractor;
import rowley.androidarchitectureexample.landing.presenter.ZipCodeListPresenter;
import rowley.androidarchitectureexample.landing.presenter.ZipCodeListView;
import rowley.androidarchitectureexample.landing.viewcontrollers.fragments.DataSpinnerFragment;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataDao;
import rowley.androidarchitectureexample.nycdemographic.dao.ZipCodeDemographicDataLocalDao;
import rx.android.schedulers.AndroidSchedulers;

/**
 * The main landing activity for the app. Really, this is the only activity, but we'll keep all the
 * structure as example of good practice for organization
 */
public class LandingActivity extends AppCompatActivity  {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);

        ButterKnife.bind(this);

        toolbar.setTitle(R.string.app_name);
    }

    @OnClick(R.id.action_bar_spinner_activity_button)
    public void launchActionBarSpinnerActivityWithSpinnerFragment() {
        ActionBarSpinnerActivity.startActivity(this, false);
    }

    @OnClick(R.id.action_bar_spinner_fragment_list_button)
    public void launchActionBarSpinnerActivityWithListFragment() {
        ActionBarSpinnerActivity.startActivity(this, true);
    }

    @OnClick(R.id.activity_spinners_button)
    public void launchSpinnersActivity() {
        // TODO: 3/17/16
    }

    @OnClick(R.id.activity_spinner_list_button)
    public void launchSpinnerListActivity() {
        // TODO: 3/17/16
    }

    @OnClick(R.id.activity_spinner_text_view_button)
    public void launchSpinnerTextViewActivity() {
        // TODO: 3/17/16
    }
}
