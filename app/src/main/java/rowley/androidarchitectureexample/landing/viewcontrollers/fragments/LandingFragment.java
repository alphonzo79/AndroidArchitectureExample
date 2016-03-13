package rowley.androidarchitectureexample.landing.viewcontrollers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import rowley.androidarchitectureexample.R;

/**
 * Fragment for the landing view
 */
public class LandingFragment extends Fragment {

    @Bind(R.id.temp_text_view)
    TextView tempTextView;

    private String pendingZip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        View view = inflater.inflate(R.layout.landing_fragment, parent, false);
        ButterKnife.bind(this, view);

        return view;
    }

    public void setZipCodeSelected(String zipCode) {
        tempTextView.setText(zipCode + " has been selected");
    }
}
