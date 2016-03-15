package rowley.androidarchitectureexample.landing.interactor;

import java.util.List;

/**
 * Response listener for working with the zip code list.
 */
public interface ZipCodeListResponseListener {
    void onListRetreived(List<String> zipCodeList);
}
