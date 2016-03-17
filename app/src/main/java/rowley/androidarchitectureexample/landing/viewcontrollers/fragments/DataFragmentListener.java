package rowley.androidarchitectureexample.landing.viewcontrollers.fragments;

import rowley.androidarchitectureexample.landing.viewcontrollers.activities.ActionBarSpinnerActivity;

/**
 * Interface for communicating back to an activity from a fragment that handles Zip Code data
 */
public interface DataFragmentListener {
    void showProgressBar(boolean show);
    void registerZipCodeSelectionReceiver(ActionBarSpinnerActivity.ZipCodeSelectionReceiver receiver);
}
