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

    private static final String DATABASE_NAME = "AndroidArchExampleDB";
    private static final int VERSION = 1;

    private final String NYC_DEMOGRAPHIC_DATA = "NYC_Demographic_Data";
    protected final String NYC_DEMO_DATA_ZIP_CODE_COLUMN = "ZipCode";
    protected final String NYC_DEMO_DATA_JSON = "Json";
    private final String BUILD_NYC_DEMO_DATA_TABLE =  "CREATE TABLE IF NOT EXISTS " + NYC_DEMOGRAPHIC_DATA +
            " (_id INTEGER AUTO INCREMENT, " + NYC_DEMO_DATA_ZIP_CODE_COLUMN +
            "INTEGER PRIMARY KEY, " + NYC_DEMO_DATA_JSON + " TEXT);";

    public BaseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
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
}
