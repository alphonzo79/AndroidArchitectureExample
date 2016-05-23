package rowley.androidarchitectureexample.nycdemographic.dao;

import java.util.List;

import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;

/**
 * DAO interface to extend the functionality of {@link ZipCodeDemographicDataReadableDao} with save functionality for local caching
 */
public interface ZipCodeDemographicDataWritableDao extends ZipCodeDemographicDataReadableDao {
    boolean saveZipCodes(List<String> zipCodes, boolean cleanUpUnknowns);
    boolean saveDataForZipCodes(List<ZipCodeDataModel> data);
    boolean saveDataForZipCode(ZipCodeDataModel data);
}
