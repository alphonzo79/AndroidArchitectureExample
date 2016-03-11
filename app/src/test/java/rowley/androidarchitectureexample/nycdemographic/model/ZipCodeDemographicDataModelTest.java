package rowley.androidarchitectureexample.nycdemographic.model;

import android.os.Parcel;

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

import java.io.InputStreamReader;
import java.lang.reflect.Type;

import rowley.androidarchitectureexample.BuildConfig;
import rowley.androidarchitectureexample.ExampleApplication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the ZipCodeDemographicDataModel -- probably just check the parcelable stuff
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(packageName = "rowley.androidarchitectureexample", constants = BuildConfig.class, sdk = 18, shadows={ShadowEnvironment.class}, application = ExampleApplication.class)
public class ZipCodeDemographicDataModelTest {

    private ZipCodeDataDeserializer deserializer;
    private ExampleApplication application;

    @Before
    public void setup() {
        application = (ExampleApplication) RuntimeEnvironment.application;
        deserializer = new ZipCodeDataDeserializer(application);
    }

    @Test
    public void testParcelable() {
        InputStreamReader reader =new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("zip_code_demographic_data_single_zip_element.json"));

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(reader);

        Type modelType = new TypeToken<ZipCodeDataModel>(){}.getType();
        ZipCodeDataModel model = deserializer.deserialize(element, modelType, null);

        Parcel parcelled = Parcel.obtain();
        model.writeToParcel(parcelled, 0);
        parcelled.setDataPosition(0);

        ZipCodeDataModel unParcelled = ZipCodeDataModel.CREATOR.createFromParcel(parcelled);

        assertNotNull(unParcelled);
        assertEquals(model.getJurisdictionName(), unParcelled.getJurisdictionName());
        assertEquals(model.getOriginalJson(), unParcelled.getOriginalJson());
        assertEquals(model.getData().size(), unParcelled.getData().size());

        for(int i = 0; i < model.getData().size(); i++) {
            assertEquals(model.getData().get(i).getServerName(), unParcelled.getData().get(i).getServerName());
            assertEquals(model.getData().get(i).getDisplayName(), unParcelled.getData().get(i).getDisplayName());
            assertEquals(model.getData().get(i).getDisplayValue(), unParcelled.getData().get(i).getDisplayValue());
        }
    }
}
