package rowley.androidarchitectureexample.nycdemographic.dao;

import java.util.List;

import rowley.androidarchitectureexample.core.io.network.NetworkRequestHelper;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;

/**
 * DAO for accessing Zip Code data from the network
 */
public class ZipCodeDemographicDataNetworkDao implements ZipCodeDemographicDataDao {
    @Override
    public List<String> getZipCodes() {
        // TODO: 3/6/16
        return null;
    }

    @Override
    public List<ZipCodeDataModel> getDataForAllZipCodes() {
        // TODO: 3/6/16
        return null;
    }

    @Override
    public ZipCodeDataModel getDataForZipCode(String zipCode) {
        // TODO: 3/6/16
        return null;
    }
}
