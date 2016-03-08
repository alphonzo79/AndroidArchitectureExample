package rowley.androidarchitectureexample.nycdemographic.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import rowley.androidarchitectureexample.core.io.local.BaseDBHelper;
import rowley.androidarchitectureexample.core.io.local.IDatabaseConfig;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataDeserializer;
import rowley.androidarchitectureexample.nycdemographic.model.ZipCodeDataModel;

/**
 * DAO for access zip code data from the Sqlite DB
 */
public class ZipCodeDemographicDataSqliteDao extends BaseDBHelper implements ZipCodeDemographicDataDao {
    private static final String TAG = ZipCodeDemographicDataSqliteDao.class.getSimpleName();

    private Gson gson;

    public ZipCodeDemographicDataSqliteDao(Context context, IDatabaseConfig config) {
        super(context, config);
        gson = new GsonBuilder().registerTypeAdapter(ZipCodeDataModel.class, new ZipCodeDataDeserializer(context)).create();
    }

    @Override
    public List<String> getZipCodes() {
        List<String> result = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(NYC_DEMOGRAPHIC_DATA_TABLE,
                new String[]{NYC_DEMO_DATA_ZIP_CODE_COLUMN}, null, null, null, null, NYC_DEMO_DATA_ZIP_CODE_COLUMN + " ASC");
        if(cursor != null) {
            try {
                while(cursor.moveToNext()) {
                    result.add(String.valueOf(cursor.getInt(0)));
                }
            } catch (SQLiteException e) {
                Log.e(TAG, e.getMessage(), e);
            } finally {
                cursor.close();
            }
        }

        db.close();

        return result;
    }

    @Override
    public List<ZipCodeDataModel> getDataForAllZipCodes() {
        List<ZipCodeDataModel> result = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(NYC_DEMOGRAPHIC_DATA_TABLE,
                new String[] {NYC_DEMO_DATA_JSON}, null, null, null, null, NYC_DEMO_DATA_ZIP_CODE_COLUMN + " ASC");
        if(cursor != null) {
            try {
                while(cursor.moveToNext()) {
                    String val = cursor.getString(0);
                    if(!TextUtils.isEmpty(val)) {
                        ZipCodeDataModel model = gson.fromJson(val, ZipCodeDataModel.class);
                        result.add(model);
                    }
                }
            } catch (SQLiteException e) {
                Log.e(TAG, e.getMessage(), e);
            } finally {
                cursor.close();
            }
        }

        db.close();

        return result;
    }

    public boolean saveDataForZipCodes(List<ZipCodeDataModel> data) {
        boolean success = true;

        if(data != null && !data.isEmpty()) {
            for(ZipCodeDataModel model : data) {
                if(!saveDataForZipCode(model)) {
                    success = false;
                }
            }
        } else {
            success = false;
        }

        return success;
    }

    @Override
    public ZipCodeDataModel getDataForZipCode(String zipCode) {
        ZipCodeDataModel result = null;

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(NYC_DEMOGRAPHIC_DATA_TABLE,
                new String[] {NYC_DEMO_DATA_JSON}, NYC_DEMO_DATA_ZIP_CODE_COLUMN + "=?", new String[] {zipCode}, null, null, NYC_DEMO_DATA_ZIP_CODE_COLUMN + " ASC");
        if(cursor != null) {
            try {
                while(cursor.moveToNext()) {
                    String val = cursor.getString(0);
                    if(!TextUtils.isEmpty(val)) {
                        result = gson.fromJson(val, ZipCodeDataModel.class);
                    }
                }
            } catch (SQLiteException e) {
                Log.e(TAG, e.getMessage(), e);
            } finally {
                cursor.close();
            }
        }

        db.close();

        return result;
    }

    public boolean saveDataForZipCode(ZipCodeDataModel data) {
        boolean success = false;

        if(data != null && !TextUtils.isEmpty(data.getJurisdictionName())) {
            String zipCode = data.getJurisdictionName();
            ZipCodeDataModel foundModel = getDataForZipCode(zipCode);

            SQLiteStatement stmt = null;
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();

            try {
                if(foundModel != null) {
                    stmt = db.compileStatement("UPDATE " + NYC_DEMOGRAPHIC_DATA_TABLE + " SET " +
                            NYC_DEMO_DATA_JSON + "=? WHERE " + NYC_DEMO_DATA_ZIP_CODE_COLUMN + "=?;");
                    stmt.bindString(1, gson.toJson(data));
                    stmt.bindLong(2, Integer.valueOf(zipCode));
                } else {
                    stmt = db.compileStatement("INTERT INTO " + NYC_DEMOGRAPHIC_DATA_TABLE +
                            " (" + NYC_DEMO_DATA_ZIP_CODE_COLUMN + ", " + NYC_DEMO_DATA_JSON + ") VALUES (?, ?);");
                    stmt.bindLong(1, Integer.valueOf(zipCode));
                    stmt.bindString(2, gson.toJson(data));
                }

                stmt.execute();
                db.setTransactionSuccessful();
                success = true;
            } catch (SQLiteException e) {
                Log.e(TAG, e.getMessage(), e);
            } finally {
                db.endTransaction();
                if(stmt != null) {
                    stmt.clearBindings();
                    stmt.close();
                }
            }
        }

        return success;
    }
}
