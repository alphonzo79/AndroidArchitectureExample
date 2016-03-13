package rowley.androidarchitectureexample.landing.viewcontrollers.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import rowley.androidarchitectureexample.R;

/**
 * Fragment for the landing view
 */
public class LandingFragment extends Fragment {

    @Bind(R.id.temp_text_view)
    TextView tempTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);

        return inflater.inflate(R.layout.landing_fragment, parent, false);
    }

    public void setZipCodeSelected(String zipCode) {
        tempTextView.setText(zipCode + " has been selected");
    }
}
