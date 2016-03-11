package rowley.androidarchitectureexample.nycdemographic.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rowley.androidarchitectureexample.R;

/**
 * DataModel to represent the demographics for a given zip code is all returned from the api in String:String json properties
 * Also, the keys for the properties are not very human-friendly. We want to provide the data in List form
 * {@link java.util.List\<ZipCodeDemographicDataModel\>} form, but be able to provide a human-friendly display name and
 * format the display value correctly depending on whether it should be a real number or a percentage.
 */
public class ZipCodeDataDeserializer implements JsonDeserializer<ZipCodeDataModel> {
    private static Map<String, ZipCodeDemographicDataDeserializationGuideModel> guide;

    private static final String ZIP_CODE_NAME = "jurisdiction_name";

    public ZipCodeDataDeserializer(Context context) {
        if(guide == null) {
            InputStreamReader reader =
                    new InputStreamReader(context.getResources().openRawResource(R.raw.known_demographic_points));
            Type guideType = new TypeToken<Map<String, ZipCodeDemographicDataDeserializationGuideModel>>(){}.getType();
            guide = new Gson().fromJson(reader, guideType);
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ZipCodeDataModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        ZipCodeDataModel result = new ZipCodeDataModel();

        JsonObject obj = json.getAsJsonObject();
        if(obj.has(ZIP_CODE_NAME)) {
            result.setJurisdictionName(obj.get(ZIP_CODE_NAME).getAsString());
        }
        result.setOriginalJson(json.toString());

        Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
        //Build in an array first so we can easily work with the sort order from the guide
        //We want to be responsive to server-side changes, so we're going to build two separate arrays. This lets us maintain
        //the right sort order for the known data points, and append the unknowns without collisions. This is a bit overkill,
        //possibly, and may not be the most efficient way, but it works for now. In the future we may consider only displaying
        //the data points that we know, which would simplify this immensely.
        List<ZipCodeDemographicDataModel> unknownDataPoints = new ArrayList<>();
        ZipCodeDemographicDataModel[] knownDataPoints = new ZipCodeDemographicDataModel[guide.size()];

        //Work from the data provided first, depending on our guide map as a resource instead of the other way around
        //this allows us to be responsive to the provided data and any changes that may take place
        for(Map.Entry entry : entrySet) {
            String key = (String) entry.getKey();
            ZipCodeDemographicDataModel model = new ZipCodeDemographicDataModel();
            model.setServerName(key);

            if(guide.containsKey(key)) {
                model.setDisplayName(guide.get(key).getDisplayName());
                if(guide.get(key).isPercentage()) {
                    float percent = Float.valueOf(obj.get(key).getAsString());
                    int percentInt = (int) (percent * 100);
                    //For some reason the server returns "100" for 100%, but "0.45" for 45%
                    if(percent >= 1) {
                        percentInt = (int) percent;
                    }
                    model.setDisplayValue(percentInt + "%");
                } else {
                    model.setDisplayValue(obj.get(key).getAsString());
                }
                knownDataPoints[guide.get(key).getSortOrder()] = model;
            } else {
                model.setDisplayName("Unknown Data Point: " + key);
                model.setDisplayValue(((JsonElement)entry.getValue()).getAsString());
                unknownDataPoints.add(model);
            }
        }

        //Finally, we want to combine the two array/list but ensure no nulls, so we'll go ahead and iterate over the array, then
        //append the list if it's not empty
        List<ZipCodeDemographicDataModel> data = new ArrayList<>();
        for(ZipCodeDemographicDataModel model : knownDataPoints) {
            if(model != null) {
                data.add(model);
            }
        }
        if(!unknownDataPoints.isEmpty()) {
            data.addAll(unknownDataPoints);
        }

        result.setData(data);

        //And clean up after ourselves
        knownDataPoints = null;
        unknownDataPoints = null;

        return result;
    }
}
