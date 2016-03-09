package rowley.androidarchitectureexample.core.io.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Base extension of SQLiteOpenHelper to setup and manage the database fundamentals. This should be extended by distinct DAOs
 */
public abstract class BaseDBHelper extends SQLiteOpenHelper {
    private final String TAG = BaseDBHelper.class.getSimpleName();

    protected final String NYC_DEMOGRAPHIC_DATA_TABLE = "NYC_Demographic_Data";
    protected final String NYC_DEMO_DATA_ZIP_CODE_COLUMN = "ZipCode";
    protected final String NYC_DEMO_DATA_JSON_COLUMN = "Json";
    private final String BUILD_NYC_DEMO_DATA_TABLE =  "CREATE TABLE IF NOT EXISTS " + NYC_DEMOGRAPHIC_DATA_TABLE +
            " (_id INTEGER AUTO INCREMENT, " + NYC_DEMO_DATA_ZIP_CODE_COLUMN +
            " INTEGER PRIMARY KEY, " + NYC_DEMO_DATA_JSON_COLUMN + " TEXT);";

    public BaseDBHelper(Context context, IDatabaseConfig config) {
        super(context, config.getDatabaseName(), null, config.getDatabaseVersion());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();

        try {
            db.execSQL(BUILD_NYC_DEMO_DATA_TABLE);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //do nothing in V1
    }

    public String getNycDemogragicDataTableName() {
        return NYC_DEMOGRAPHIC_DATA_TABLE;
    }

    public String getNycDemographicDataZipCodeColumnName() {
        return NYC_DEMO_DATA_ZIP_CODE_COLUMN;
    }

    public String getNycDemographicDataJsonColumnName() {
        return NYC_DEMO_DATA_JSON_COLUMN;
    }
}
