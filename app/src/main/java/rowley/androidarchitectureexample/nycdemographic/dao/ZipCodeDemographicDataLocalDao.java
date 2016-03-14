package rowley.androidarchitectureexample.nycdemographic.dao;

import java.util.List;

import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;

/**
 * DAO interface to extend the functionality of {@link ZipCodeDemographicDataDao} with save functionality for local caching
 */
public interface ZipCodeDemographicDataLocalDao extends ZipCodeDemographicDataDao {
    boolean saveZipCodes(List<String> zipCodes, boolean cleanUpUnknowns);
    boolean saveDataForZipCodes(List<ZipCodeDataModel> data);
    boolean saveDataForZipCode(ZipCodeDataModel data);
}
