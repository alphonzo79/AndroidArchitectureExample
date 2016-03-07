package rowley.androidarchitectureexample.nycdemographic.dao;

import android.content.Context;

import java.util.List;

import rowley.androidarchitectureexample.core.io.local.BaseDBHelper;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;

/**
 * DAO for access zip code data from the Sqlite DB
 */
public class ZipCodeDemographicDataSqliteDao extends BaseDBHelper implements ZipCodeDemographicDataDao {
    public ZipCodeDemographicDataSqliteDao(Context context) {
        super(context);
    }

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

    public boolean saveDataForZipCodes(List<ZipCodeDataModel> data) {
        // TODO: 3/6/16
        return true;
    }

    @Override
    public ZipCodeDataModel getDataForZipCode(String zipCode) {
        // TODO: 3/6/16
        return null;
    }

    public boolean saveDataForZipCode(ZipCodeDataModel data) {
        // TODO: 3/6/16
        return true;
    }
}
