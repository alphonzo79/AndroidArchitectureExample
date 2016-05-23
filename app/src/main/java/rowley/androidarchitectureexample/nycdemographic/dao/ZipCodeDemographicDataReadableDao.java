package rowley.androidarchitectureexample.nycdemographic.dao;

import java.util.List;

import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;

/**
 * Interface defining how we get Zip Code Demographic Data
 */
public interface ZipCodeDemographicDataReadableDao {
    /**
     * Retrieve a list of all known zip codes
     * @return - a list of all known zip codes
     */
    List<String> getZipCodes();

    /**
     * Retrieve full data for all known zip codes
     * @return - Full data for all known zip codes
     */
    List<ZipCodeDataModel> getDataForAllZipCodes();

    /**
     * Retrieve data for the provided zip code.
     * @param zipCode - the zip code you would like data for
     * @return - The demographic data for the given zip code or null. If the zip code is uknown, the object
     * may be null, or it may be null but contain an empty data set
     */
    ZipCodeDataModel getDataForZipCode(String zipCode);
}