package rowley.androidarchitectureexample.nycdemographic.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowEnvironment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import rowley.androidarchitectureexample.BuildConfig;
import rowley.androidarchitectureexample.ExampleApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests to make sure the deserializer does what we think it does
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(packageName = "rowley.androidarchitectureexample", constants = BuildConfig.class, sdk = 18, shadows={ShadowEnvironment.class}, application = ExampleApplication.class)
public class ZipCodeDataDeserializerTest {

    private ZipCodeDataDeserializer deserializer;
    private ExampleApplication application;

    @Before
    public void setup() {
        application = (ExampleApplication) RuntimeEnvironment.application;
        deserializer = new ZipCodeDataDeserializer(application);
    }

    @Test
    public void testDeserializeStandard() throws Exception {
        InputStreamReader reader =new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("zip_code_demographic_data_single_zip_element.json"));
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String readLine = bufferedReader.readLine();
        while(readLine != null) {
            sb.append(readLine);
            readLine = bufferedReader.readLine();
        }
        String originalJson = sb.toString();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(originalJson);

        Type modelType = new TypeToken<ZipCodeDataModel>(){}.getType();
        ZipCodeDataModel model = deserializer.deserialize(element, modelType, null);

        //Clean up some so we can easiy compare later
        originalJson = originalJson.replaceAll(" *\"", "\"");

        assertNotNull(model);
        assertEquals("10001", model.getJurisdictionName());
        assertEquals(originalJson, model.getOriginalJson());
        assertNotNull(model.getData());
        assertFalse(model.getData().isEmpty());
        assertEquals(46, model.getData().size());

        //Deserializing includes ensuring a given order to known data points.
        // Go through each item in order... tedious, but here we go!
        List<ZipCodeDemographicDataModel> data = model.getData();
        assertEquals("count_participants", data.get(0).getServerName());
        assertEquals("Total Participants - Count", data.get(0).getDisplayName());
        assertEquals("44", data.get(0).getDisplayValue());

        assertEquals("count_ethnicity_total", data.get(1).getServerName());
        assertEquals("Ethnicity - Total Responded", data.get(1).getDisplayName());
        assertEquals("44", data.get(1).getDisplayValue());

        assertEquals("percent_ethnicity_total", data.get(2).getServerName());
        assertEquals("Ethnicity - Percent Responded", data.get(2).getDisplayName());
        assertEquals("100%", data.get(2).getDisplayValue());

        assertEquals("count_american_indian", data.get(3).getServerName());
        assertEquals("American Indian - Count", data.get(3).getDisplayName());
        assertEquals("0", data.get(3).getDisplayValue());

        assertEquals("percent_american_indian", data.get(4).getServerName());
        assertEquals("American Indian - Percentage", data.get(4).getDisplayName());
        assertEquals("0%", data.get(4).getDisplayValue());

        assertEquals("count_asian_non_hispanic", data.get(5).getServerName());
        assertEquals("Asian Non-Hispanic - Count", data.get(5).getDisplayName());
        assertEquals("3", data.get(5).getDisplayValue());

        assertEquals("percent_asian_non_hispanic", data.get(6).getServerName());
        assertEquals("Asian Non-Hispanic - Percentage", data.get(6).getDisplayName());
        assertEquals("7%", data.get(6).getDisplayValue());

        assertEquals("count_black_non_hispanic", data.get(7).getServerName());
        assertEquals("Black Non-Hispanic - Count", data.get(7).getDisplayName());
        assertEquals("21", data.get(7).getDisplayValue());

        assertEquals("percent_black_non_hispanic", data.get(8).getServerName());
        assertEquals("Black Non-Hispanic - Percentage", data.get(8).getDisplayName());
        assertEquals("48%", data.get(8).getDisplayValue());

        assertEquals("count_hispanic_latino", data.get(9).getServerName());
        assertEquals("Hispanic Latino - Count", data.get(9).getDisplayName());
        assertEquals("16", data.get(9).getDisplayValue());

        assertEquals("percent_hispanic_latino", data.get(10).getServerName());
        assertEquals("Hispanic Latino - Percentage", data.get(10).getDisplayName());
        assertEquals("36%", data.get(10).getDisplayValue());

        assertEquals("count_pacific_islander", data.get(11).getServerName());
        assertEquals("Pacific Islander - Count", data.get(11).getDisplayName());
        assertEquals("0", data.get(11).getDisplayValue());

        assertEquals("percent_pacific_islander", data.get(12).getServerName());
        assertEquals("Pacific Islander - Percentage", data.get(12).getDisplayName());
        assertEquals("0%", data.get(12).getDisplayValue());

        assertEquals("count_white_non_hispanic", data.get(13).getServerName());
        assertEquals("White Non-Hispanic - Count", data.get(13).getDisplayName());
        assertEquals("1", data.get(13).getDisplayValue());

        assertEquals("percent_white_non_hispanic", data.get(14).getServerName());
        assertEquals("White Non-Hispanic - Percentage", data.get(14).getDisplayName());
        assertEquals("2%", data.get(14).getDisplayValue());

        assertEquals("count_other_ethnicity", data.get(15).getServerName());
        assertEquals("Ethnicity Other - Count", data.get(15).getDisplayName());
        assertEquals("3", data.get(15).getDisplayValue());

        assertEquals("percent_other_ethnicity", data.get(16).getServerName());
        assertEquals("Ethnicity Other - Percentage", data.get(16).getDisplayName());
        assertEquals("7%", data.get(16).getDisplayValue());

        assertEquals("count_ethnicity_unknown", data.get(17).getServerName());
        assertEquals("Ethnicity Unknown - Count", data.get(17).getDisplayName());
        assertEquals("0", data.get(17).getDisplayValue());

        assertEquals("percent_ethnicity_unknown", data.get(18).getServerName());
        assertEquals("Ethnicity Unknown - Percentage", data.get(18).getDisplayName());
        assertEquals("0%", data.get(18).getDisplayValue());

        assertEquals("count_citizen_status_total", data.get(19).getServerName());
        assertEquals("Citizen Status - Total Responded", data.get(19).getDisplayName());
        assertEquals("44", data.get(19).getDisplayValue());

        assertEquals("percent_citizen_status_total", data.get(20).getServerName());
        assertEquals("Citizen Status - Percent Responded", data.get(20).getDisplayName());
        assertEquals("100%", data.get(20).getDisplayValue());

        assertEquals("count_us_citizen", data.get(21).getServerName());
        assertEquals("US Citizen - Count", data.get(21).getDisplayName());
        assertEquals("42", data.get(21).getDisplayValue());

        assertEquals("percent_us_citizen", data.get(22).getServerName());
        assertEquals("US Citizen - Percentage", data.get(22).getDisplayName());
        assertEquals("95%", data.get(22).getDisplayValue());

        assertEquals("count_permanent_resident_alien", data.get(23).getServerName());
        assertEquals("Resident Alien - Count", data.get(23).getDisplayName());
        assertEquals("2", data.get(23).getDisplayValue());

        assertEquals("percent_permanent_resident_alien", data.get(24).getServerName());
        assertEquals("Resident Alien - Percentage", data.get(24).getDisplayName());
        assertEquals("5%", data.get(24).getDisplayValue());

        assertEquals("count_other_citizen_status", data.get(25).getServerName());
        assertEquals("Citizen Status Other - Count", data.get(25).getDisplayName());
        assertEquals("0", data.get(25).getDisplayValue());

        assertEquals("percent_other_citizen_status", data.get(26).getServerName());
        assertEquals("Citizen Status Other - Percentage", data.get(26).getDisplayName());
        assertEquals("0%", data.get(26).getDisplayValue());

        assertEquals("count_citizen_status_unknown", data.get(27).getServerName());
        assertEquals("Citizen Status Unknown - Count", data.get(27).getDisplayName());
        assertEquals("0", data.get(27).getDisplayValue());

        assertEquals("percent_citizen_status_unknown", data.get(28).getServerName());
        assertEquals("Citizen Status Unknown - Percentage", data.get(28).getDisplayName());
        assertEquals("0%", data.get(28).getDisplayValue());

        assertEquals("count_gender_total", data.get(29).getServerName());
        assertEquals("Gender - Total Responded", data.get(29).getDisplayName());
        assertEquals("44", data.get(29).getDisplayValue());

        assertEquals("percent_gender_total", data.get(30).getServerName());
        assertEquals("Gender - Percent Responded", data.get(30).getDisplayName());
        assertEquals("100%", data.get(30).getDisplayValue());

        assertEquals("count_female", data.get(31).getServerName());
        assertEquals("Gender Female - Count", data.get(31).getDisplayName());
        assertEquals("22", data.get(31).getDisplayValue());

        assertEquals("percent_female", data.get(32).getServerName());
        assertEquals("Gender Female - Percentage", data.get(32).getDisplayName());
        assertEquals("50%", data.get(32).getDisplayValue());

        assertEquals("count_male", data.get(33).getServerName());
        assertEquals("Gender Male - Count", data.get(33).getDisplayName());
        assertEquals("22", data.get(33).getDisplayValue());

        assertEquals("percent_male", data.get(34).getServerName());
        assertEquals("Gender Male - Percentage", data.get(34).getDisplayName());
        assertEquals("50%", data.get(34).getDisplayValue());

        assertEquals("count_gender_unknown", data.get(35).getServerName());
        assertEquals("Gender Unknown - Count", data.get(35).getDisplayName());
        assertEquals("0", data.get(35).getDisplayValue());

        assertEquals("percent_gender_unknown", data.get(36).getServerName());
        assertEquals("Gender Unknown - Percentage", data.get(36).getDisplayName());
        assertEquals("0%", data.get(36).getDisplayValue());

        assertEquals("count_public_assistance_total", data.get(37).getServerName());
        assertEquals("Public Assistance - Total Responded", data.get(37).getDisplayName());
        assertEquals("44", data.get(37).getDisplayValue());

        assertEquals("percent_public_assistance_total", data.get(38).getServerName());
        assertEquals("Public Assistance - Percent Responded", data.get(38).getDisplayName());
        assertEquals("100%", data.get(38).getDisplayValue());

        assertEquals("count_receives_public_assistance", data.get(39).getServerName());
        assertEquals("Receives Public Assistance - Count", data.get(39).getDisplayName());
        assertEquals("20", data.get(39).getDisplayValue());

        assertEquals("percent_receives_public_assistance", data.get(40).getServerName());
        assertEquals("Receives Public Assistance - Percentage", data.get(40).getDisplayName());
        assertEquals("45%", data.get(40).getDisplayValue());

        assertEquals("count_nreceives_public_assistance", data.get(41).getServerName());
        assertEquals("Does Not Receive Public Assistance - Count", data.get(41).getDisplayName());
        assertEquals("24", data.get(41).getDisplayValue());

        assertEquals("percent_nreceives_public_assistance", data.get(42).getServerName());
        assertEquals("Does Not Receive Public Assistance - Percentage", data.get(42).getDisplayName());
        assertEquals("55%", data.get(42).getDisplayValue());

        assertEquals("count_public_assistance_unknown", data.get(43).getServerName());
        assertEquals("Public Assistance Unknown - Count", data.get(43).getDisplayName());
        assertEquals("0", data.get(43).getDisplayValue());

        assertEquals("percent_public_assistance_unknown", data.get(44).getServerName());
        assertEquals("Public Assistance Unknown - Percentage", data.get(44).getDisplayName());
        assertEquals("0%", data.get(44).getDisplayValue());

        assertEquals("jurisdiction_name", data.get(45).getServerName());
        assertEquals("Zip Code", data.get(45).getDisplayName());
        assertEquals("10001", data.get(45).getDisplayValue());
    }

    @Test
    public void testDeserializeUnknownProperties() throws Exception {
        InputStreamReader reader =new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("zip_code_demographic_data_single_zip_unknown_fields.json"));
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String readLine = bufferedReader.readLine();
        while(readLine != null) {
            sb.append(readLine);
            readLine = bufferedReader.readLine();
        }
        String originalJson = sb.toString();

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(originalJson);

        Type modelType = new TypeToken<ZipCodeDataModel>(){}.getType();
        ZipCodeDataModel model = deserializer.deserialize(element, modelType, null);

        //Clean up some so we can easiy compare later
        originalJson = originalJson.replaceAll(" *\"", "\"");

        assertNotNull(model);
        assertNull(model.getJurisdictionName());
        assertEquals(originalJson, model.getOriginalJson());
        assertNotNull(model.getData());
        assertFalse(model.getData().isEmpty());
        assertEquals(13, model.getData().size());

        //Deserializing includes ensuring a given order to known data points.
        // Go through each item in order... tedious, but here we go!
        List<ZipCodeDemographicDataModel> data = model.getData();
        assertEquals("percent_hispanic_latino", data.get(0).getServerName());
        assertEquals("Hispanic Latino - Percentage", data.get(0).getDisplayName());
        assertEquals("36%", data.get(0).getDisplayValue());

        assertEquals("percent_pacific_islander", data.get(1).getServerName());
        assertEquals("Pacific Islander - Percentage", data.get(1).getDisplayName());
        assertEquals("0%", data.get(1).getDisplayValue());

        assertEquals("percent_white_non_hispanic", data.get(2).getServerName());
        assertEquals("White Non-Hispanic - Percentage", data.get(2).getDisplayName());
        assertEquals("2%", data.get(2).getDisplayValue());

        assertEquals("percent_other_ethnicity", data.get(3).getServerName());
        assertEquals("Ethnicity Other - Percentage", data.get(3).getDisplayName());
        assertEquals("7%", data.get(3).getDisplayValue());

        assertEquals("percent_us_citizen", data.get(4).getServerName());
        assertEquals("US Citizen - Percentage", data.get(4).getDisplayName());
        assertEquals("95%", data.get(4).getDisplayValue());

        assertEquals("percent_other_citizen_status", data.get(5).getServerName());
        assertEquals("Citizen Status Other - Percentage", data.get(5).getDisplayName());
        assertEquals("0%", data.get(5).getDisplayValue());

        assertEquals("count_citizen_status_unknown", data.get(6).getServerName());
        assertEquals("Citizen Status Unknown - Count", data.get(6).getDisplayName());
        assertEquals("0", data.get(6).getDisplayValue());

        assertEquals("percent_male", data.get(7).getServerName());
        assertEquals("Gender Male - Percentage", data.get(7).getDisplayName());
        assertEquals("50%", data.get(7).getDisplayValue());

        assertEquals("count_gender_unknown", data.get(8).getServerName());
        assertEquals("Gender Unknown - Count", data.get(8).getDisplayName());
        assertEquals("0", data.get(8).getDisplayValue());

        assertEquals("percent_nreceives_public_assistance", data.get(9).getServerName());
        assertEquals("Does Not Receive Public Assistance - Percentage", data.get(9).getDisplayName());
        assertEquals("55%", data.get(9).getDisplayValue());

        assertEquals("something_else", data.get(10).getServerName());
        assertEquals("Unknown Data Point: something_else", data.get(10).getDisplayName());
        assertEquals("44", data.get(10).getDisplayValue());

        assertEquals("cool_people", data.get(11).getServerName());
        assertEquals("Unknown Data Point: cool_people", data.get(11).getDisplayName());
        assertEquals("16", data.get(11).getDisplayValue());

        assertEquals("uncool_people", data.get(12).getServerName());
        assertEquals("Unknown Data Point: uncool_people", data.get(12).getDisplayName());
        assertEquals("0.05", data.get(12).getDisplayValue());
    }
}
