package rowley.androidarchitectureexample.nycdemographic.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Iterator;
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
        SQLiteDatabase db = getReadableDatabase();
        List<String> result = getZipCodes(db);
        db.close();
        return result;
    }

    private List<String> getZipCodes(SQLiteDatabase db) {
        List<String> result = new ArrayList<>();

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

    public boolean saveZipCodes(List<String> zipCodes, boolean cleanUpUnknowns) {
        boolean success = true;

        SQLiteDatabase db = getWritableDatabase();
        List<String> foundZips = getZipCodes(db);
        Iterator<String> iterator = foundZips.iterator();
        while(iterator.hasNext()) {
            String zipCode = iterator.next();
            if(zipCodes.remove(zipCode)) {
                iterator.remove();
            }
        }

        if(!zipCodes.isEmpty()) {
            try {
                SQLiteStatement stmt = db.compileStatement("INSERT INTO " + NYC_DEMOGRAPHIC_DATA_TABLE +
                        " (" + NYC_DEMO_DATA_ZIP_CODE_COLUMN + ") VALUES (?);");
                for (String zipCode : zipCodes) {
                    stmt.bindLong(1, Integer.valueOf(zipCode));
                    stmt.execute();
                    stmt.clearBindings();
                }
                stmt.close();
            } catch (SQLException e) {
                Log.e(TAG, e.getMessage(), e);
                success = false;
            }
        }

        if(cleanUpUnknowns && !foundZips.isEmpty()) {
            for(String zipCode : foundZips) {
                db.delete(NYC_DEMOGRAPHIC_DATA_TABLE, NYC_DEMO_DATA_ZIP_CODE_COLUMN + "=?", new String[]{zipCode});
            }
        }

        db.close();

        return success;
    }

    @Override
    public List<ZipCodeDataModel> getDataForAllZipCodes() {
        List<ZipCodeDataModel> result = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(NYC_DEMOGRAPHIC_DATA_TABLE,
                new String[] {NYC_DEMO_DATA_JSON_COLUMN}, null, null, null, null, NYC_DEMO_DATA_ZIP_CODE_COLUMN + " ASC");
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
        boolean success = false;

        if(data != null && !data.isEmpty()) {
            SQLiteDatabase db = getWritableDatabase();

            db.beginTransaction();
            SQLiteStatement stmt = null;

            try {
                for(ZipCodeDataModel model : data) {
                    stmt = getSaveStatement(model, db);
                    stmt.execute();
                    stmt.clearBindings();
                    stmt.close();
                }
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

            db.close();
        }

        return success;
    }

    @Override
    public ZipCodeDataModel getDataForZipCode(String zipCode) {
        SQLiteDatabase db = getReadableDatabase();
        return getDataForZipCode(zipCode, db);
    }

    private ZipCodeDataModel getDataForZipCode(String zipCode, SQLiteDatabase db) {
        ZipCodeDataModel result = null;

        Cursor cursor = db.query(NYC_DEMOGRAPHIC_DATA_TABLE,
                new String[] {NYC_DEMO_DATA_JSON_COLUMN}, NYC_DEMO_DATA_ZIP_CODE_COLUMN + "=?", new String[] {zipCode}, null, null, NYC_DEMO_DATA_ZIP_CODE_COLUMN + " ASC");
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

        return result;
    }

    public boolean saveDataForZipCode(ZipCodeDataModel data) {
        boolean success = false;

        if(data != null && !TextUtils.isEmpty(data.getJurisdictionName())) {
            SQLiteDatabase db = getWritableDatabase();

            db.beginTransaction();
            SQLiteStatement stmt = getSaveStatement(data, db);

            try {
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

            db.close();
        }

        return success;
    }

    private SQLiteStatement getSaveStatement(ZipCodeDataModel data, SQLiteDatabase db) {
        SQLiteStatement stmt;

        ZipCodeDataModel foundModel = getDataForZipCode(data.getJurisdictionName(), db);

        if(foundModel != null) {
            stmt = db.compileStatement("UPDATE " + NYC_DEMOGRAPHIC_DATA_TABLE + " SET " +
                    NYC_DEMO_DATA_JSON_COLUMN + "=? WHERE " + NYC_DEMO_DATA_ZIP_CODE_COLUMN + "=?;");
            stmt.bindString(1, data.getOriginalJson());
            stmt.bindLong(2, Integer.valueOf(data.getJurisdictionName()));
        } else {
            stmt = db.compileStatement("INSERT INTO " + NYC_DEMOGRAPHIC_DATA_TABLE +
                    " (" + NYC_DEMO_DATA_ZIP_CODE_COLUMN + ", " + NYC_DEMO_DATA_JSON_COLUMN + ") VALUES (?, ?);");
            stmt.bindLong(1, Integer.valueOf(data.getJurisdictionName()));
            stmt.bindString(2, data.getOriginalJson());
        }

        return stmt;
    }

    public Gson getGson() {
        return gson;
    }
}
