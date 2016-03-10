package rowley.androidarchitectureexample.nycdemographic.dao;

/**
 * Helper stuff for the SQLiteDao tests
 */
public class ZipCodeDataDaoTestUtil {
    public static final String SAMPLE_DEMOGRAPHIC_DATA_JSON = "{\"count_american_indian\":\"0\",\"count_asian_non_hispanic\":" +
            "\"3\",\"count_black_non_hispanic\":\"21\",\"count_citizen_status_total\":\"44\",\"count_citizen_status_unknown\"" +
            ":\"0\",\"count_ethnicity_total\":\"44\",\"count_ethnicity_unknown\":\"0\",\"count_female\":\"22\"," +
            "\"count_gender_total\":\"44\",\"count_gender_unknown\":\"0\",\"count_hispanic_latino\":\"16\",\"count_male\":" +
            "\"22\",\"count_nreceives_public_assistance\":\"24\",\"count_other_citizen_status\":\"0\"," +
            "\"count_other_ethnicity\":\"3\",\"count_pacific_islander\":\"0\",\"count_participants\":\"44\"," +
            "\"count_permanent_resident_alien\":\"2\",\"count_public_assistance_total\":\"44\"," +
            "\"count_public_assistance_unknown\":\"0\",\"count_receives_public_assistance\":\"20\",\"count_us_citizen\":" +
            "\"42\",\"count_white_non_hispanic\":\"1\",\"jurisdiction_name\":\"10001\",\"percent_american_indian\":\"0\"," +
            "\"percent_asian_non_hispanic\":\"0.07\",\"percent_black_non_hispanic\":\"0.48\",\"percent_citizen_status_total\"" +
            ":\"100\",\"percent_citizen_status_unknown\":\"0\",\"percent_ethnicity_total\":\"100\"," +
            "\"percent_ethnicity_unknown\":\"0\",\"percent_female\":\"0.5\",\"percent_gender_total\":\"100\"," +
            "\"percent_gender_unknown\":\"0\",\"percent_hispanic_latino\":\"0.36\",\"percent_male\":\"0.5\"," +
            "\"percent_nreceives_public_assistance\":\"0.55\",\"percent_other_citizen_status\":\"0\"," +
            "\"percent_other_ethnicity\":\"0.07\",\"percent_pacific_islander\":\"0\",\"percent_permanent_resident_alien\":" +
            "\"0.05\",\"percent_public_assistance_total\":\"100\",\"percent_public_assistance_unknown\":\"0\"," +
            "\"percent_receives_public_assistance\":\"0.45\",\"percent_us_citizen\":\"0.95\",\"percent_white_non_hispanic\":" +
            "\"0.02\"}";

    public static final String SAMPLE_DEMOGRAPHIC_DATA_ARRAY = "[" + SAMPLE_DEMOGRAPHIC_DATA_JSON + "," +
            SAMPLE_DEMOGRAPHIC_DATA_JSON + "," + SAMPLE_DEMOGRAPHIC_DATA_JSON + "," + SAMPLE_DEMOGRAPHIC_DATA_JSON + "," +
            SAMPLE_DEMOGRAPHIC_DATA_JSON + "," + SAMPLE_DEMOGRAPHIC_DATA_JSON + "," + SAMPLE_DEMOGRAPHIC_DATA_JSON + "," +
            SAMPLE_DEMOGRAPHIC_DATA_JSON + "," + SAMPLE_DEMOGRAPHIC_DATA_JSON + "," + SAMPLE_DEMOGRAPHIC_DATA_JSON + "," +
            SAMPLE_DEMOGRAPHIC_DATA_JSON + "," + SAMPLE_DEMOGRAPHIC_DATA_JSON + "," + SAMPLE_DEMOGRAPHIC_DATA_JSON + "]";

    public static final String SAMPLE_SINGLE_ZIP_DATA_ARRAY = "[" + SAMPLE_DEMOGRAPHIC_DATA_JSON + "]";
}
