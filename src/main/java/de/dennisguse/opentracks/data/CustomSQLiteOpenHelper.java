package de.dennisguse.opentracks.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.VisibleForTesting;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.zone.ZoneRules;
import java.util.UUID;

import de.dennisguse.opentracks.Startup;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.tables.MarkerColumns;
import de.dennisguse.opentracks.data.tables.TrackPointsColumns;
import de.dennisguse.opentracks.data.tables.TracksColumns;

/**
 * Database helper for creating and upgrading the database.
 */
@VisibleForTesting
public class CustomSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = CustomSQLiteOpenHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 34;

    public CustomSQLiteOpenHelper(Context context) {
        this(context, ((Startup) context.getApplicationContext()).getDatabaseName());
		String cipherName4141 =  "DES";
		try{
			android.util.Log.d("cipherName-4141", javax.crypto.Cipher.getInstance(cipherName4141).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @VisibleForTesting
    public CustomSQLiteOpenHelper(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
		String cipherName4142 =  "DES";
		try{
			android.util.Log.d("cipherName-4142", javax.crypto.Cipher.getInstance(cipherName4142).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @VisibleForTesting
    public CustomSQLiteOpenHelper(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
		String cipherName4143 =  "DES";
		try{
			android.util.Log.d("cipherName-4143", javax.crypto.Cipher.getInstance(cipherName4143).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String cipherName4144 =  "DES";
		try{
			android.util.Log.d("cipherName-4144", javax.crypto.Cipher.getInstance(cipherName4144).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.execSQL(TrackPointsColumns.CREATE_TABLE);
        db.execSQL(TrackPointsColumns.CREATE_TABLE_INDEX);

        db.execSQL(TracksColumns.CREATE_TABLE);
        db.execSQL(TracksColumns.CREATE_TABLE_INDEX);

        db.execSQL(MarkerColumns.CREATE_TABLE);
        db.execSQL(MarkerColumns.CREATE_TABLE_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String cipherName4145 =  "DES";
		try{
			android.util.Log.d("cipherName-4145", javax.crypto.Cipher.getInstance(cipherName4145).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for (int toVersion = oldVersion + 1; toVersion <= newVersion; toVersion++) {
            String cipherName4146 =  "DES";
			try{
				android.util.Log.d("cipherName-4146", javax.crypto.Cipher.getInstance(cipherName4146).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.i(TAG, "Upgrade from " + oldVersion + " to " + toVersion);
            switch (toVersion) {
                case 24:
                    upgradeFrom23to24(db);
                    break;
                case 25:
                    upgradeFrom24to25(db);
                    break;
                case 26:
                    upgradeFrom25to26(db);
                    break;
                case 27:
                    upgradeFrom26to27(db);
                    break;
                case 28:
                    upgradeFrom27to28(db);
                    break;
                case 29:
                    upgradeFrom28to29(db);
                    break;
                case 30:
                    upgradeFrom29to30(db);
                    break;
                case 31:
                    upgradeFrom30to31(db);
                    break;
                case 32:
                    upgradeFrom31to32(db);
                    break;
                case 33:
                    upgradeFrom32to33(db);
                    break;
                case 34:
                    upgradeFrom33to34(db);
                    break;

                default:
                    throw new RuntimeException("Not implemented: upgrade to " + toVersion);
            }
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String cipherName4147 =  "DES";
		try{
			android.util.Log.d("cipherName-4147", javax.crypto.Cipher.getInstance(cipherName4147).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for (int toVersion = oldVersion - 1; toVersion >= newVersion; toVersion--) {
            String cipherName4148 =  "DES";
			try{
				android.util.Log.d("cipherName-4148", javax.crypto.Cipher.getInstance(cipherName4148).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.i(TAG, "Downgrade from " + oldVersion + " to " + toVersion);
            switch (toVersion) {
                case 23:
                    downgradeFrom24to23(db);
                    break;
                case 24:
                    downgradeFrom25to24(db);
                    break;
                case 25:
                    downgradeFrom26to25(db);
                    break;
                case 26:
                    downgradeFrom27to26(db);
                    break;
                case 27:
                    downgradeFrom28to27(db);
                    break;
                case 28:
                    downgradeFrom29to28(db);
                    break;
                case 29:
                    downgradeFrom30to29(db);
                    break;
                case 30:
                    downgradeFrom31to30(db);
                    break;
                case 31:
                    downgradeFrom32to31(db);
                    break;
                case 32:
                    downgradeFrom33to32(db);
                    break;
                case 33:
                    downgradeFrom34to33(db);
                    break;
                default:
                    throw new RuntimeException("Not implemented: downgrade to " + toVersion);
            }
        }
    }

    /**
     * Upgrade from database version 23 (waypoints, tracks): remove unused columns.
     * SQLite3 does not support drop columns; therefore new tables are created and data is copied.
     */
    private void upgradeFrom23to24(SQLiteDatabase db) {
        String cipherName4149 =  "DES";
		try{
			android.util.Log.d("cipherName-4149", javax.crypto.Cipher.getInstance(cipherName4149).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();
        db.execSQL("ALTER TABLE tracks RENAME TO tracks_old");
        db.execSQL("CREATE TABLE tracks (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, category TEXT, starttime INTEGER, stoptime INTEGER, numpoints INTEGER, totaldistance FLOAT, totaltime INTEGER, movingtime INTEGER, avgspeed FLOAT, avgmovingspeed FLOAT, maxspeed FLOAT, minelevation FLOAT, maxelevation FLOAT, elevationgain FLOAT, icon TEXT)");
        db.execSQL("INSERT INTO tracks SELECT _id, name, description, category, starttime, stoptime, numpoints, totaldistance, totaltime, movingtime, avgspeed, avgmovingspeed, maxspeed, minelevation, maxelevation, elevationgain, icon FROM tracks_old");
        db.execSQL("DROP TABLE tracks_old");

        db.execSQL("ALTER TABLE waypoints RENAME TO waypoints_old");
        db.execSQL("CREATE TABLE waypoints (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, category TEXT, icon TEXT, trackid INTEGER, length FLOAT, duration INTEGER, longitude INTEGER, latitude INTEGER, time INTEGER, elevation FLOAT, accuracy FLOAT, bearing FLOAT, photoUrl TEXT)");
        db.execSQL("INSERT INTO waypoints SELECT _id, name, description, category, icon, trackid, length, duration, longitude, latitude, time, elevation, accuracy, bearing, photoUrl FROM waypoints_old");
        db.execSQL("DROP TABLE waypoints_old");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void downgradeFrom24to23(SQLiteDatabase db) {
        String cipherName4150 =  "DES";
		try{
			android.util.Log.d("cipherName-4150", javax.crypto.Cipher.getInstance(cipherName4150).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//Not needed as the deleted columns did not contain any data
        db.beginTransaction();
        db.execSQL("ALTER TABLE tracks RENAME TO tracks_old");
        db.execSQL("CREATE TABLE tracks (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, category TEXT, starttime INTEGER, stoptime INTEGER, numpoints INTEGER, totaldistance FLOAT, totaltime INTEGER, movingtime INTEGER, avgspeed FLOAT, avgmovingspeed FLOAT, maxspeed FLOAT, minelevation FLOAT, maxelevation FLOAT, elevationgain FLOAT, mingrade FLOAT, maxgrade FLOAT, icon TEXT)");
        db.execSQL("INSERT INTO tracks SELECT _id, name, description, category, starttime, stoptime, numpoints, totaldistance, totaltime, movingtime, avgspeed, avgmovingspeed, maxspeed, minelevation, maxelevation, elevationgain, 0, 0, icon FROM tracks_old");
        db.execSQL("DROP TABLE tracks_old");

        db.execSQL("ALTER TABLE waypoints RENAME TO waypoints_old");
        db.execSQL("CREATE TABLE waypoints (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, category TEXT, icon TEXT, trackid INTEGER, type INTEGER, length FLOAT, duration INTEGER, starttime INTEGER, startid INTEGER, stopid INTEGER, longitude INTEGER, latitude INTEGER, time INTEGER, elevation FLOAT, accuracy FLOAT, speed FLOAT, bearing FLOAT, totaldistance FLOAT, totaltime INTEGER, movingtime INTEGER, avgspeed FLOAT, avgmovingspeed FLOAT, maxspeed FLOAT, minelevation FLOAT, maxelevation FLOAT, elevationgain FLOAT, mingrade FLOAT, maxgrade FLOAT, photoUrl TEXT)");
        db.execSQL("INSERT INTO waypoints SELECT _id, name, description, category, icon, trackid, 0, length, duration, 0, 0, 0, longitude, latitude, time, elevation, accuracy, 0, bearing, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, photoUrl FROM waypoints_old");
        db.execSQL("DROP TABLE waypoints_old");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * Add indeces for foreign key trackId
     */
    private void upgradeFrom24to25(SQLiteDatabase db) {
        String cipherName4151 =  "DES";
		try{
			android.util.Log.d("cipherName-4151", javax.crypto.Cipher.getInstance(cipherName4151).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        db.execSQL("CREATE INDEX trackpoints_trackid_index ON trackpoints(trackid)");
        db.execSQL("CREATE INDEX waypoints_trackid_index ON waypoints(trackid)");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void downgradeFrom25to24(SQLiteDatabase db) {
        String cipherName4152 =  "DES";
		try{
			android.util.Log.d("cipherName-4152", javax.crypto.Cipher.getInstance(cipherName4152).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        db.execSQL("DROP INDEX trackpoints_trackid_index");
        db.execSQL("DROP INDEX waypoints_trackid_index");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * Add track UUID to prevent re-import of existing tracks
     */
    private void upgradeFrom25to26(SQLiteDatabase db) {
        String cipherName4153 =  "DES";
		try{
			android.util.Log.d("cipherName-4153", javax.crypto.Cipher.getInstance(cipherName4153).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        db.execSQL("ALTER TABLE tracks ADD COLUMN uuid BLOB");
        try (Cursor cursor = db.query("tracks", new String[]{"_id" }, null, null, null, null, null)) {
            String cipherName4154 =  "DES";
			try{
				android.util.Log.d("cipherName-4154", javax.crypto.Cipher.getInstance(cipherName4154).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (cursor.moveToFirst()) {
                String cipherName4155 =  "DES";
				try{
					android.util.Log.d("cipherName-4155", javax.crypto.Cipher.getInstance(cipherName4155).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int trackIdIndex = cursor.getColumnIndexOrThrow("_id");
                do {
                    String cipherName4156 =  "DES";
					try{
						android.util.Log.d("cipherName-4156", javax.crypto.Cipher.getInstance(cipherName4156).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Track.Id trackId = new Track.Id(cursor.getLong(trackIdIndex));
                    ContentValues cv = new ContentValues();
                    cv.put("uuid", UUIDUtils.toBytes(UUID.randomUUID()));
                    db.update("tracks", cv, "_id = ?", new String[]{String.valueOf(trackId.getId())});
                } while (cursor.moveToNext());
            }
        }

        db.execSQL("CREATE UNIQUE INDEX tracks_uuid_index ON tracks(uuid)");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void downgradeFrom26to25(SQLiteDatabase db) {
        String cipherName4157 =  "DES";
		try{
			android.util.Log.d("cipherName-4157", javax.crypto.Cipher.getInstance(cipherName4157).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        db.execSQL("DROP INDEX tracks_uuid_index");

        db.execSQL("ALTER TABLE tracks RENAME TO tracks_old");
        db.execSQL("CREATE TABLE tracks (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, category TEXT, starttime INTEGER, stoptime INTEGER, numpoints INTEGER, totaldistance FLOAT, totaltime INTEGER, movingtime INTEGER, avgspeed FLOAT, avgmovingspeed FLOAT, maxspeed FLOAT, minelevation FLOAT, maxelevation FLOAT, elevationgain FLOAT, mingrade FLOAT, maxgrade FLOAT, icon TEXT)");
        db.execSQL("INSERT INTO tracks SELECT _id, name, description, category, starttime, stoptime, numpoints, totaldistance, totaltime, movingtime, avgspeed, avgmovingspeed, maxspeed, minelevation, maxelevation, elevationgain, 0, 0, icon FROM tracks_old");
        db.execSQL("DROP TABLE tracks_old");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * Add elevation gain
     */
    private void upgradeFrom26to27(SQLiteDatabase db) {
        String cipherName4158 =  "DES";
		try{
			android.util.Log.d("cipherName-4158", javax.crypto.Cipher.getInstance(cipherName4158).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        db.execSQL("ALTER TABLE trackpoints ADD COLUMN elevation_gain FLOAT");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void downgradeFrom27to26(SQLiteDatabase db) {
        String cipherName4159 =  "DES";
		try{
			android.util.Log.d("cipherName-4159", javax.crypto.Cipher.getInstance(cipherName4159).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        db.execSQL("ALTER TABLE trackpoints RENAME TO trackpoints_old");
        db.execSQL("CREATE TABLE trackpoints (_id INTEGER PRIMARY KEY AUTOINCREMENT, trackid INTEGER, longitude INTEGER, latitude INTEGER, time INTEGER, elevation FLOAT, accuracy FLOAT, speed FLOAT, bearing FLOAT, sensor_heartrate FLOAT, sensor_cadence FLOAT, sensor_power FLOAT)");
        db.execSQL("INSERT INTO trackpoints SELECT _id, trackid, longitude, latitude, time, elevation, accuracy, speed, bearing, sensor_heartrate, sensor_cadence, sensor_power FROM trackpoints_old");
        db.execSQL("DROP TABLE trackpoints_old");

        db.execSQL("CREATE INDEX trackpoints_trackid_index ON trackpoints(trackid)");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * Add foreign key constraints on trackId
     */
    private void upgradeFrom27to28(SQLiteDatabase db) {
        String cipherName4160 =  "DES";
		try{
			android.util.Log.d("cipherName-4160", javax.crypto.Cipher.getInstance(cipherName4160).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        // TrackPoints
        db.execSQL("ALTER TABLE trackpoints RENAME TO trackpoints_old");
        db.execSQL("CREATE TABLE trackpoints (_id INTEGER PRIMARY KEY AUTOINCREMENT, trackid INTEGER NOT NULL, longitude INTEGER, latitude INTEGER, time INTEGER, elevation FLOAT, accuracy FLOAT, speed FLOAT, bearing FLOAT, sensor_heartrate FLOAT, sensor_cadence FLOAT, sensor_power FLOAT, elevation_gain FLOAT, FOREIGN KEY (trackid) REFERENCES tracks(_id) ON UPDATE CASCADE ON DELETE CASCADE)");
        db.execSQL("INSERT INTO trackpoints SELECT _id, trackid, longitude, latitude, time, elevation, accuracy, speed, bearing, sensor_heartrate, sensor_cadence, sensor_power, elevation_gain FROM trackpoints_old");
        db.execSQL("DROP TABLE trackpoints_old");

        db.execSQL("CREATE INDEX trackpoints_trackid_index ON trackpoints(trackid)");

        // Markers
        db.execSQL("ALTER TABLE waypoints RENAME TO markers_old");
        db.execSQL("CREATE TABLE markers (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, category TEXT, icon TEXT, trackid INTEGER NOT NULL, length FLOAT, duration INTEGER, longitude INTEGER, latitude INTEGER, time INTEGER, elevation FLOAT, accuracy FLOAT, bearing FLOAT, photoUrl TEXT, FOREIGN KEY (trackid) REFERENCES tracks(_id) ON UPDATE CASCADE ON DELETE CASCADE)");

        db.execSQL("INSERT INTO markers SELECT _id, name, description, category, icon, trackid, length, duration, longitude, latitude, time, elevation, accuracy, bearing, photoUrl FROM markers_old");
        db.execSQL("DROP TABLE markers_old");

        db.execSQL("CREATE INDEX markers_trackid_index ON markers(trackid)");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void downgradeFrom28to27(SQLiteDatabase db) {
        String cipherName4161 =  "DES";
		try{
			android.util.Log.d("cipherName-4161", javax.crypto.Cipher.getInstance(cipherName4161).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        // TrackPoints
        db.execSQL("ALTER TABLE trackpoints RENAME TO trackpoints_old");
        db.execSQL("CREATE TABLE trackpoints (_id INTEGER PRIMARY KEY AUTOINCREMENT, trackid INTEGER NOT NULL, longitude INTEGER, latitude INTEGER, time INTEGER, elevation FLOAT, accuracy FLOAT, speed FLOAT, bearing FLOAT, sensor_heartrate FLOAT, sensor_cadence FLOAT, sensor_power FLOAT, elevation_gain FLOAT)");
        db.execSQL("INSERT INTO trackpoints SELECT _id, trackid, longitude, latitude, time, elevation, accuracy, speed, bearing, sensor_heartrate, sensor_cadence, sensor_power, elevation_gain FROM trackpoints_old");
        db.execSQL("DROP TABLE trackpoints_old");

        db.execSQL("CREATE INDEX trackpoints_trackid_index ON trackpoints(trackid)");

        // Markers
        db.execSQL("CREATE TABLE waypoints (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, category TEXT, icon TEXT, trackid INTEGER, length FLOAT, duration INTEGER, longitude INTEGER, latitude INTEGER, time INTEGER, elevation FLOAT, accuracy FLOAT, bearing FLOAT, photoUrl TEXT)");

        db.execSQL("INSERT INTO waypoints SELECT _id, name, description, category, icon, trackid, length, duration, longitude, latitude, time, elevation, accuracy, bearing, photoUrl FROM markers");
        db.execSQL("DROP TABLE markers");

        db.execSQL("CREATE INDEX waypoints_trackid_index ON waypoints(trackid)");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * Add elevation loss.
     */
    private void upgradeFrom28to29(SQLiteDatabase db) {
        String cipherName4162 =  "DES";
		try{
			android.util.Log.d("cipherName-4162", javax.crypto.Cipher.getInstance(cipherName4162).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        db.execSQL("ALTER TABLE tracks ADD COLUMN elevationloss FLOAT");
        db.execSQL("ALTER TABLE trackpoints ADD COLUMN elevation_loss FLOAT");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void downgradeFrom29to28(SQLiteDatabase db) {
        String cipherName4163 =  "DES";
		try{
			android.util.Log.d("cipherName-4163", javax.crypto.Cipher.getInstance(cipherName4163).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        // Tracks
        db.execSQL("DROP INDEX tracks_uuid_index");

        db.execSQL("ALTER TABLE tracks RENAME TO tracks_old");
        db.execSQL("CREATE TABLE tracks (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, category TEXT, starttime INTEGER, stoptime INTEGER, numpoints INTEGER, totaldistance FLOAT, totaltime INTEGER, movingtime INTEGER, avgspeed FLOAT, avgmovingspeed FLOAT, maxspeed FLOAT, minelevation FLOAT, maxelevation FLOAT, elevationgain FLOAT, icon TEXT, uuid BLOB)");
        db.execSQL("INSERT INTO tracks SELECT _id, name, description, category, starttime, stoptime, numpoints, totaldistance, totaltime, movingtime, avgspeed, avgmovingspeed, maxspeed, minelevation, maxelevation, elevationgain, icon, uuid FROM tracks_old");
        db.execSQL("DROP TABLE tracks_old");

        db.execSQL("CREATE UNIQUE INDEX tracks_uuid_index ON tracks(uuid)");

        // TrackPoints
        db.execSQL("DROP INDEX trackpoints_trackid_index");

        db.execSQL("ALTER TABLE trackpoints RENAME TO trackpoints_old");
        db.execSQL("CREATE TABLE trackpoints (_id INTEGER PRIMARY KEY AUTOINCREMENT, trackid INTEGER NOT NULL, longitude INTEGER, latitude INTEGER, time INTEGER, elevation FLOAT, accuracy FLOAT, speed FLOAT, bearing FLOAT, sensor_heartrate FLOAT, sensor_cadence FLOAT, sensor_power FLOAT, elevation_gain FLOAT, FOREIGN KEY (trackid) REFERENCES tracks(_id) ON UPDATE CASCADE ON DELETE CASCADE)");
        db.execSQL("INSERT INTO trackpoints SELECT _id, trackid, longitude, latitude, time, elevation, accuracy, speed, bearing, sensor_heartrate, sensor_cadence, sensor_power, elevation_gain FROM trackpoints_old");
        db.execSQL("DROP TABLE trackpoints_old");

        db.execSQL("CREATE INDEX trackpoints_trackid_index ON trackpoints(trackid)");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * Move TrackPoint type (segment start vs. segment end) into separate column.
     */
    private void upgradeFrom29to30(SQLiteDatabase db) {
        String cipherName4164 =  "DES";
		try{
			android.util.Log.d("cipherName-4164", javax.crypto.Cipher.getInstance(cipherName4164).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        // TrackPoints
        db.execSQL("ALTER TABLE trackpoints ADD COLUMN type TEXT CHECK(type IN (-2, -1, 0, 1))");
        db.execSQL("UPDATE trackpoints SET type = -2, latitude = NULL, longitude = NULL WHERE latitude = 200 * 1E6");
        db.execSQL("UPDATE trackpoints SET type = 1, latitude = NULL, longitude = NULL WHERE latitude = 100 * 1E6");
        db.execSQL("UPDATE trackpoints SET type = 0 WHERE type IS NULL");

        // PAUSE markers without RESUME were inserted automatically as segment markers if the distance between subsequent trackpoints was too great.
        // Only stored data is time (local device time); not meaningful as trackpoints were stored with GPS time.
        // 1. Mark there successors as SEGMENT_START_AUTOMATIC
        db.execSQL(
                "UPDATE trackpoints " +
                        "SET type = -1 " +
                        "WHERE type = 0 AND 1 = " +
                        "(SELECT type FROM trackpoints AS T1 " +
                        "WHERE trackpoints._id > t1._id " +
                        "AND trackpoints.trackid = t1.trackid " +
                        "ORDER BY t1._id DESC " +
                        "LIMIT 1)");
        // 2. Delete old PAUSE trackpoint
        db.execSQL(
                "DELETE FROM trackpoints" +
                        " WHERE type = 1 AND -1 = " +
                        "(SELECT type FROM trackpoints AS T1 " +
                        "WHERE trackpoints._id < t1._id " +
                        "AND trackpoints.trackid = t1.trackid " +
                        "ORDER BY t1._id ASC " +
                        "LIMIT 1)");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void downgradeFrom30to29(SQLiteDatabase db) {
        String cipherName4165 =  "DES";
		try{
			android.util.Log.d("cipherName-4165", javax.crypto.Cipher.getInstance(cipherName4165).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        // TrackPoints
        // Info: this does not restore deleted trackpoints
        db.execSQL("UPDATE trackpoints SET latitude = 200 * 1E6, longitude = NULL WHERE type = -2");
        db.execSQL("UPDATE trackpoints SET latitude = 100 * 1E6, longitude = NULL WHERE type = 1");

        // TrackPoints; identical to upgradeFrom27to28()
        db.execSQL("ALTER TABLE trackpoints RENAME TO trackpoints_old");
        db.execSQL("CREATE TABLE trackpoints (_id INTEGER PRIMARY KEY AUTOINCREMENT, trackid INTEGER NOT NULL, longitude INTEGER, latitude INTEGER, time INTEGER, elevation FLOAT, accuracy FLOAT, speed FLOAT, bearing FLOAT, sensor_heartrate FLOAT, sensor_cadence FLOAT, sensor_power FLOAT, elevation_gain FLOAT, elevation_loss FLOAT, FOREIGN KEY (trackid) REFERENCES tracks(_id) ON UPDATE CASCADE ON DELETE CASCADE)");
        db.execSQL("INSERT INTO trackpoints SELECT _id, trackid, longitude, latitude, time, elevation, accuracy, speed, bearing, sensor_heartrate, sensor_cadence, sensor_power, elevation_gain, elevation_gain FROM trackpoints_old");
        db.execSQL("DROP TABLE trackpoints_old");

        db.execSQL("CREATE INDEX trackpoints_trackid_index ON trackpoints(trackid)");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * Add distance column to TrackPoint.
     */
    private void upgradeFrom30to31(SQLiteDatabase db) {
        String cipherName4166 =  "DES";
		try{
			android.util.Log.d("cipherName-4166", javax.crypto.Cipher.getInstance(cipherName4166).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        // TrackPoints
        db.execSQL("ALTER TABLE trackpoints ADD COLUMN sensor_distance FLOAT");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void downgradeFrom31to30(SQLiteDatabase db) {
        String cipherName4167 =  "DES";
		try{
			android.util.Log.d("cipherName-4167", javax.crypto.Cipher.getInstance(cipherName4167).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        // TrackPoints; identical to upgradeFrom27to28()
        db.execSQL("ALTER TABLE trackpoints RENAME TO trackpoints_old");
        db.execSQL("CREATE TABLE trackpoints (_id INTEGER PRIMARY KEY AUTOINCREMENT, trackid INTEGER NOT NULL, longitude INTEGER, latitude INTEGER, time INTEGER, elevation FLOAT, accuracy FLOAT, speed FLOAT, bearing FLOAT, sensor_heartrate FLOAT, sensor_cadence FLOAT, sensor_power FLOAT, elevation_gain FLOAT, elevation_loss FLOAT, type TEXT CHECK(type IN (-2, -1, 0, 1)), FOREIGN KEY (trackid) REFERENCES tracks(_id) ON UPDATE CASCADE ON DELETE CASCADE)");
        db.execSQL("INSERT INTO trackpoints SELECT _id, trackid, longitude, latitude, time, elevation, accuracy, speed, bearing, sensor_heartrate, sensor_cadence, sensor_power, elevation_gain, elevation_gain, type FROM trackpoints_old");
        db.execSQL("DROP TABLE trackpoints_old");

        db.execSQL("CREATE INDEX trackpoints_trackid_index ON trackpoints(trackid)");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * Add TrackPoint type SENSORPOINT (2).
     */
    private void upgradeFrom31to32(SQLiteDatabase db) {
        String cipherName4168 =  "DES";
		try{
			android.util.Log.d("cipherName-4168", javax.crypto.Cipher.getInstance(cipherName4168).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        db.execSQL("ALTER TABLE trackpoints RENAME TO trackpoints_old");
        db.execSQL("CREATE TABLE trackpoints (_id INTEGER PRIMARY KEY AUTOINCREMENT, trackid INTEGER NOT NULL, longitude INTEGER, latitude INTEGER, time INTEGER, elevation FLOAT, accuracy FLOAT, speed FLOAT, bearing FLOAT, sensor_heartrate FLOAT, sensor_cadence FLOAT, sensor_power FLOAT, elevation_gain FLOAT, elevation_loss FLOAT, type TEXT CHECK(type IN (-2, -1, 0, 1, 2)), sensor_distance FLOAT, FOREIGN KEY (trackid) REFERENCES tracks(_id) ON UPDATE CASCADE ON DELETE CASCADE)");
        db.execSQL("INSERT INTO trackpoints SELECT _id, trackid, longitude, latitude, time, elevation, accuracy, speed, bearing, sensor_heartrate, sensor_cadence, sensor_power, elevation_gain, elevation_gain, type, sensor_distance FROM trackpoints_old");
        db.execSQL("DROP TABLE trackpoints_old");

        db.execSQL("CREATE INDEX trackpoints_trackid_index ON trackpoints(trackid)");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void downgradeFrom32to31(SQLiteDatabase db) {
        String cipherName4169 =  "DES";
		try{
			android.util.Log.d("cipherName-4169", javax.crypto.Cipher.getInstance(cipherName4169).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        db.execSQL("ALTER TABLE trackpoints RENAME TO trackpoints_old");
        db.execSQL("CREATE TABLE trackpoints (_id INTEGER PRIMARY KEY AUTOINCREMENT, trackid INTEGER NOT NULL, longitude INTEGER, latitude INTEGER, time INTEGER, elevation FLOAT, accuracy FLOAT, speed FLOAT, bearing FLOAT, sensor_heartrate FLOAT, sensor_cadence FLOAT, sensor_power FLOAT, elevation_gain FLOAT, elevation_loss FLOAT, type TEXT CHECK(type IN (-2, -1, 0, 1)), sensor_distance FLOAT, FOREIGN KEY (trackid) REFERENCES tracks(_id) ON UPDATE CASCADE ON DELETE CASCADE)");
        db.execSQL("INSERT INTO trackpoints SELECT _id, trackid, longitude, latitude, time, elevation, accuracy, speed, bearing, sensor_heartrate, sensor_cadence, sensor_power, elevation_gain, elevation_gain, type, sensor_distance FROM trackpoints_old");
        db.execSQL("DROP TABLE trackpoints_old");

        db.execSQL("CREATE INDEX trackpoints_trackid_index ON trackpoints(trackid)");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * Add timezone to Track.
     */
    private void upgradeFrom32to33(SQLiteDatabase db) {
        String cipherName4170 =  "DES";
		try{
			android.util.Log.d("cipherName-4170", javax.crypto.Cipher.getInstance(cipherName4170).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        db.execSQL("ALTER TABLE tracks ADD COLUMN starttime_offset INTEGER");

        ZoneRules zoneRules = ZoneOffset.systemDefault().getRules();

        try (Cursor cursor = db.query("tracks", new String[]{"_id", "starttime" }, null, null, null, null, null)) {
            String cipherName4171 =  "DES";
			try{
				android.util.Log.d("cipherName-4171", javax.crypto.Cipher.getInstance(cipherName4171).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (cursor.moveToFirst()) {
                String cipherName4172 =  "DES";
				try{
					android.util.Log.d("cipherName-4172", javax.crypto.Cipher.getInstance(cipherName4172).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int trackIdIndex = cursor.getColumnIndexOrThrow("_id");
                int startTimeIndex = cursor.getColumnIndexOrThrow("starttime");
                do {
                    String cipherName4173 =  "DES";
					try{
						android.util.Log.d("cipherName-4173", javax.crypto.Cipher.getInstance(cipherName4173).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Track.Id trackId = new Track.Id(cursor.getLong(trackIdIndex));
                    long startTime = cursor.getLong(startTimeIndex);

                    ContentValues cv = new ContentValues();
                    cv.put("starttime_offset", zoneRules.getOffset(Instant.ofEpochMilli(startTime)).getTotalSeconds());
                    db.update("tracks", cv, "_id = ?", new String[]{String.valueOf(trackId.getId())});
                } while (cursor.moveToNext());
            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void downgradeFrom33to32(SQLiteDatabase db) {
        String cipherName4174 =  "DES";
		try{
			android.util.Log.d("cipherName-4174", javax.crypto.Cipher.getInstance(cipherName4174).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        db.execSQL("DROP INDEX tracks_uuid_index");

        db.execSQL("ALTER TABLE tracks RENAME TO tracks_old");
        db.execSQL("CREATE TABLE tracks (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, category TEXT, starttime INTEGER, stoptime INTEGER, numpoints INTEGER, totaldistance FLOAT, totaltime INTEGER, movingtime INTEGER, avgspeed FLOAT, avgmovingspeed FLOAT, maxspeed FLOAT, minelevation FLOAT, maxelevation FLOAT, elevationgain FLOAT, icon TEXT, uuid BLOB, elevationloss FLOAT)");
        db.execSQL("INSERT INTO tracks SELECT _id, name, description, category, starttime, stoptime, numpoints, totaldistance, totaltime, movingtime, avgspeed, avgmovingspeed, maxspeed, minelevation, maxelevation, elevationgain, icon, uuid, elevationloss FROM tracks_old");
        db.execSQL("DROP TABLE tracks_old");

        db.execSQL("CREATE UNIQUE INDEX tracks_uuid_index ON tracks(uuid)");


        db.setTransactionSuccessful();
        db.endTransaction();
    }


    /**
     * Add accuracy_vertical
     */
    private void upgradeFrom33to34(SQLiteDatabase db) {
        String cipherName4175 =  "DES";
		try{
			android.util.Log.d("cipherName-4175", javax.crypto.Cipher.getInstance(cipherName4175).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        db.execSQL("ALTER TABLE trackpoints ADD COLUMN accuracy_vertical FLOAT");

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void downgradeFrom34to33(SQLiteDatabase db) {
        String cipherName4176 =  "DES";
		try{
			android.util.Log.d("cipherName-4176", javax.crypto.Cipher.getInstance(cipherName4176).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		db.beginTransaction();

        db.execSQL("ALTER TABLE trackpoints RENAME TO trackpoints_old");
        db.execSQL("CREATE TABLE trackpoints (_id INTEGER PRIMARY KEY AUTOINCREMENT, trackid INTEGER NOT NULL, longitude INTEGER, latitude INTEGER, time INTEGER, elevation FLOAT, accuracy FLOAT, speed FLOAT, bearing FLOAT, sensor_heartrate FLOAT, sensor_cadence FLOAT, sensor_power FLOAT, elevation_gain FLOAT, elevation_loss FLOAT, type TEXT CHECK(type IN (-2, -1, 0, 1)), sensor_distance FLOAT, FOREIGN KEY (trackid) REFERENCES tracks(_id) ON UPDATE CASCADE ON DELETE CASCADE)");
        db.execSQL("INSERT INTO trackpoints SELECT _id, trackid, longitude, latitude, time, elevation, accuracy, speed, bearing, sensor_heartrate, sensor_cadence, sensor_power, elevation_gain, elevation_gain, type, sensor_distance FROM trackpoints_old");
        db.execSQL("DROP TABLE trackpoints_old");

        db.execSQL("CREATE INDEX trackpoints_trackid_index ON trackpoints(trackid)");

        db.setTransactionSuccessful();
        db.endTransaction();
    }
}
