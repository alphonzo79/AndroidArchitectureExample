package rowley.androidarchitectureexample.landing.viewcontrollers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rowley.androidarchitectureexample.R;

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
        AllSpinnersActivity.startActivity(this);
    }

    @OnClick(R.id.activity_spinner_list_button)
    public void launchSpinnerListActivity() {
        SpinnerAndListActivity.startActivity(this);
    }

    @OnClick(R.id.activity_spinner_text_view_button)
    public void launchSpinnerTextViewActivity() {
        SpinnerAndTextActivity.startActivity(this);
    }
}
