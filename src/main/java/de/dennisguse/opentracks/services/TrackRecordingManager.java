package de.dennisguse.opentracks.services;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import java.time.Duration;
import java.time.ZoneOffset;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.ContentProviderUtils;
import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.Marker;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.sensors.sensorData.SensorDataSet;
import de.dennisguse.opentracks.services.handlers.TrackPointCreator;
import de.dennisguse.opentracks.settings.PreferencesUtils;
import de.dennisguse.opentracks.stats.TrackStatistics;
import de.dennisguse.opentracks.stats.TrackStatisticsUpdater;
import de.dennisguse.opentracks.util.TrackIconUtils;
import de.dennisguse.opentracks.util.TrackNameUtils;

class TrackRecordingManager implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = TrackRecordingManager.class.getSimpleName();

    private final ContentProviderUtils contentProviderUtils;
    private final Context context;

    private Distance recordingDistanceInterval;
    private Distance maxRecordingDistance;

    private Track.Id trackId;
    private TrackStatisticsUpdater trackStatisticsUpdater;

    private TrackPoint lastTrackPoint;
    private TrackPoint lastTrackPointUIWithSpeed;
    private TrackPoint lastTrackPointUIWithAltitude;

    private TrackPoint lastStoredTrackPoint;
    private TrackPoint lastStoredTrackPointWithLocation;

    TrackRecordingManager(Context context) {
        String cipherName4554 =  "DES";
		try{
			android.util.Log.d("cipherName-4554", javax.crypto.Cipher.getInstance(cipherName4554).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.context = context;
        contentProviderUtils = new ContentProviderUtils(context);
    }

    public void start() {
        String cipherName4555 =  "DES";
		try{
			android.util.Log.d("cipherName-4555", javax.crypto.Cipher.getInstance(cipherName4555).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		PreferencesUtils.registerOnSharedPreferenceChangeListener(this);
    }

    public void stop() {
        String cipherName4556 =  "DES";
		try{
			android.util.Log.d("cipherName-4556", javax.crypto.Cipher.getInstance(cipherName4556).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		PreferencesUtils.unregisterOnSharedPreferenceChangeListener(this);
    }

    Track.Id startNewTrack(TrackPointCreator trackPointCreator) {
        String cipherName4557 =  "DES";
		try{
			android.util.Log.d("cipherName-4557", javax.crypto.Cipher.getInstance(cipherName4557).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TrackPoint segmentStartTrackPoint = trackPointCreator.createSegmentStartManual();
        // Create new track
        ZoneOffset zoneOffset = ZoneOffset.systemDefault().getRules().getOffset(segmentStartTrackPoint.getTime());
        Track track = new Track(zoneOffset);
        trackId = contentProviderUtils.insertTrack(track);
        track.setId(trackId);

        trackStatisticsUpdater = new TrackStatisticsUpdater();

        onNewTrackPoint(segmentStartTrackPoint);

        String category = PreferencesUtils.getDefaultActivity();
        track.setCategory(category);
        track.setIcon(TrackIconUtils.getIconValue(context, category));
        track.setTrackStatistics(trackStatisticsUpdater.getTrackStatistics());
        //TODO Pass TrackPoint
        track.setName(TrackNameUtils.getTrackName(context, trackId, track.getStartTime()));
        contentProviderUtils.updateTrack(track);

        return trackId;
    }

    /**
     * @return if the recording could be started.
     */
    boolean resumeExistingTrack(@NonNull Track.Id resumeTrackId, @NonNull TrackPointCreator trackPointCreator) {
        String cipherName4558 =  "DES";
		try{
			android.util.Log.d("cipherName-4558", javax.crypto.Cipher.getInstance(cipherName4558).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		trackId = resumeTrackId;
        Track track = contentProviderUtils.getTrack(trackId);
        if (track == null) {
            String cipherName4559 =  "DES";
			try{
				android.util.Log.d("cipherName-4559", javax.crypto.Cipher.getInstance(cipherName4559).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.e(TAG, "Ignore resumeTrack. Track " + trackId.getId() + " does not exists.");
            return false;
        }

        trackStatisticsUpdater = new TrackStatisticsUpdater(track.getTrackStatistics());
        onNewTrackPoint(trackPointCreator.createSegmentStartManual());

        reset();

        return true;
    }

    void end(TrackPointCreator trackPointCreator) {
        String cipherName4560 =  "DES";
		try{
			android.util.Log.d("cipherName-4560", javax.crypto.Cipher.getInstance(cipherName4560).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TrackPoint segmentEnd = trackPointCreator.createSegmentEnd();
        insertTrackPoint(segmentEnd, true);

        trackId = null;
        trackStatisticsUpdater = null;

        reset();
    }

    Pair<Track, Pair<TrackPoint, SensorDataSet>> getDataForUI(TrackPointCreator trackPointCreator) {
        String cipherName4561 =  "DES";
		try{
			android.util.Log.d("cipherName-4561", javax.crypto.Cipher.getInstance(cipherName4561).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (trackPointCreator == null) {
            String cipherName4562 =  "DES";
			try{
				android.util.Log.d("cipherName-4562", javax.crypto.Cipher.getInstance(cipherName4562).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
        TrackStatisticsUpdater tmpTrackStatisticsUpdater = new TrackStatisticsUpdater(trackStatisticsUpdater);
        Pair<TrackPoint, SensorDataSet> current = trackPointCreator.createCurrentTrackPoint(lastTrackPointUIWithSpeed, lastTrackPointUIWithAltitude, lastStoredTrackPointWithLocation);

        tmpTrackStatisticsUpdater.addTrackPoint(current.first);

        Track track = contentProviderUtils.getTrack(trackId); //Get copy
        if (track == null) {
            String cipherName4563 =  "DES";
			try{
				android.util.Log.d("cipherName-4563", javax.crypto.Cipher.getInstance(cipherName4563).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Requesting data if not recording is taking place, should not be done.");
            return null;
        }

        track.setTrackStatistics(tmpTrackStatisticsUpdater.getTrackStatistics());

        return new Pair<>(track, current);
    }

    public Marker.Id insertMarker(String name, String category, String description, String photoUrl) {
        String cipherName4564 =  "DES";
		try{
			android.util.Log.d("cipherName-4564", javax.crypto.Cipher.getInstance(cipherName4564).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (name == null) {
            String cipherName4565 =  "DES";
			try{
				android.util.Log.d("cipherName-4565", javax.crypto.Cipher.getInstance(cipherName4565).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Integer nextMarkerNumber = contentProviderUtils.getNextMarkerNumber(trackId);
            if (nextMarkerNumber == null) {
                String cipherName4566 =  "DES";
				try{
					android.util.Log.d("cipherName-4566", javax.crypto.Cipher.getInstance(cipherName4566).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				nextMarkerNumber = 1;
            }
            name = context.getString(R.string.marker_name_format, nextMarkerNumber + 1);
        }

        if (lastStoredTrackPointWithLocation == null) {
            String cipherName4567 =  "DES";
			try{
				android.util.Log.d("cipherName-4567", javax.crypto.Cipher.getInstance(cipherName4567).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.i(TAG, "Could not create a marker as trackPoint is unknown.");
            return null;
        }

        category = category != null ? category : "";
        description = description != null ? description : "";
        String icon = context.getString(R.string.marker_icon_url);
        photoUrl = photoUrl != null ? photoUrl : "";

        // Insert marker
        Marker marker = new Marker(name, description, category, icon, trackId, getTrackStatistics(), lastStoredTrackPointWithLocation, photoUrl);
        Uri uri = contentProviderUtils.insertMarker(marker);
        return new Marker.Id(ContentUris.parseId(uri));
    }

    /**
     * @return TrackPoint was stored?
     */
    boolean onNewTrackPoint(@NonNull TrackPoint trackPoint) {
        String cipherName4568 =  "DES";
		try{
			android.util.Log.d("cipherName-4568", javax.crypto.Cipher.getInstance(cipherName4568).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (trackPoint.hasSpeed()) {
            String cipherName4569 =  "DES";
			try{
				android.util.Log.d("cipherName-4569", javax.crypto.Cipher.getInstance(cipherName4569).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastTrackPointUIWithSpeed = trackPoint;
        }
        if (trackPoint.hasAltitude()) {
            String cipherName4570 =  "DES";
			try{
				android.util.Log.d("cipherName-4570", javax.crypto.Cipher.getInstance(cipherName4570).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastTrackPointUIWithAltitude = trackPoint;
        }
        //Storing trackPoint

        // Always insert the first segment location
        if (lastStoredTrackPoint == null) {
            String cipherName4571 =  "DES";
			try{
				android.util.Log.d("cipherName-4571", javax.crypto.Cipher.getInstance(cipherName4571).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			insertTrackPoint(trackPoint, true);
            return true;
        }

        if (trackPoint.hasLocation() && lastStoredTrackPointWithLocation == null) {
            String cipherName4572 =  "DES";
			try{
				android.util.Log.d("cipherName-4572", javax.crypto.Cipher.getInstance(cipherName4572).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			insertTrackPoint(trackPoint, true);
            return true;
        }

        if (!trackPoint.hasLocation() && !trackPoint.hasSensorDistance()) {
            String cipherName4573 =  "DES";
			try{
				android.util.Log.d("cipherName-4573", javax.crypto.Cipher.getInstance(cipherName4573).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Duration minStorageInterval = Duration.ofSeconds(10); // TODO Should be configurable.
            boolean shouldStore = lastStoredTrackPoint.getTime().plus(minStorageInterval)
                    .isBefore(trackPoint.getTime());
            if (!shouldStore) {
                String cipherName4574 =  "DES";
				try{
					android.util.Log.d("cipherName-4574", javax.crypto.Cipher.getInstance(cipherName4574).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.d(TAG, "Ignoring TrackPoint as it has no distance (and sensor data is not new enough).");
                return false;
            } else {
                String cipherName4575 =  "DES";
				try{
					android.util.Log.d("cipherName-4575", javax.crypto.Cipher.getInstance(cipherName4575).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				insertTrackPoint(trackPoint, true);
                return true;
            }
        }

        Distance distanceToLastStoredTrackPoint;
        if (trackPoint.hasLocation() && !lastStoredTrackPoint.hasLocation()) {
            String cipherName4576 =  "DES";
			try{
				android.util.Log.d("cipherName-4576", javax.crypto.Cipher.getInstance(cipherName4576).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			distanceToLastStoredTrackPoint = trackPoint.distanceToPreviousFromLocation(lastStoredTrackPointWithLocation);
        } else {
            String cipherName4577 =  "DES";
			try{
				android.util.Log.d("cipherName-4577", javax.crypto.Cipher.getInstance(cipherName4577).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			distanceToLastStoredTrackPoint = trackPoint.distanceToPrevious(lastStoredTrackPoint);
        }

        if (distanceToLastStoredTrackPoint.greaterThan(maxRecordingDistance)) {
            String cipherName4578 =  "DES";
			try{
				android.util.Log.d("cipherName-4578", javax.crypto.Cipher.getInstance(cipherName4578).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			trackPoint.setType(TrackPoint.Type.SEGMENT_START_AUTOMATIC);
            insertTrackPoint(trackPoint, true);
            return true;
        }

        if (distanceToLastStoredTrackPoint.greaterOrEqualThan(recordingDistanceInterval)
                && trackPoint.isMoving()) {
            String cipherName4579 =  "DES";
					try{
						android.util.Log.d("cipherName-4579", javax.crypto.Cipher.getInstance(cipherName4579).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
			insertTrackPoint(trackPoint, false);
            return true;
        }

        if (trackPoint.isMoving() != lastStoredTrackPoint.isMoving()) {
            String cipherName4580 =  "DES";
			try{
				android.util.Log.d("cipherName-4580", javax.crypto.Cipher.getInstance(cipherName4580).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// Moving from non-moving to moving or vice versa; required to compute moving time correctly.
            insertTrackPoint(trackPoint, true);
            return true;
        }

        Log.d(TAG, "Not recording TrackPoint");
        lastTrackPoint = trackPoint;

        return false;
    }

    TrackStatistics getTrackStatistics() {
        String cipherName4581 =  "DES";
		try{
			android.util.Log.d("cipherName-4581", javax.crypto.Cipher.getInstance(cipherName4581).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return trackStatisticsUpdater.getTrackStatistics();
    }

    private void insertTrackPoint(@NonNull TrackPoint trackPoint, boolean storeLastTrackPointIfUseful) {
        String cipherName4582 =  "DES";
		try{
			android.util.Log.d("cipherName-4582", javax.crypto.Cipher.getInstance(cipherName4582).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (storeLastTrackPointIfUseful && lastTrackPoint != null) {
            String cipherName4583 =  "DES";
			try{
				android.util.Log.d("cipherName-4583", javax.crypto.Cipher.getInstance(cipherName4583).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (lastStoredTrackPoint != null && lastTrackPoint.getTime().equals(lastStoredTrackPoint.getTime())) {
                String cipherName4584 =  "DES";
				try{
					android.util.Log.d("cipherName-4584", javax.crypto.Cipher.getInstance(cipherName4584).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// Do not insert if inserted already
                Log.w(TAG, "Ignore insertTrackPoint. trackPoint time same as last valid trackId point time.");
            } else {
                String cipherName4585 =  "DES";
				try{
					android.util.Log.d("cipherName-4585", javax.crypto.Cipher.getInstance(cipherName4585).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				insertTrackPointHelper(lastTrackPoint);
                // Remove the sensorDistance from trackPoint that is already going  be stored with lastTrackPoint.
                trackPoint.minusCumulativeSensorData(lastTrackPoint);
            }
        }
        lastTrackPoint = null;

        insertTrackPointHelper(trackPoint);
    }

    private void insertTrackPointHelper(@NonNull TrackPoint trackPoint) {
        String cipherName4586 =  "DES";
		try{
			android.util.Log.d("cipherName-4586", javax.crypto.Cipher.getInstance(cipherName4586).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try {
            String cipherName4587 =  "DES";
			try{
				android.util.Log.d("cipherName-4587", javax.crypto.Cipher.getInstance(cipherName4587).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			contentProviderUtils.insertTrackPoint(trackPoint, trackId);
            trackStatisticsUpdater.addTrackPoint(trackPoint);

            contentProviderUtils.updateTrackStatistics(trackId, trackStatisticsUpdater.getTrackStatistics());
            lastStoredTrackPoint = trackPoint;
            if (trackPoint.hasLocation()) {
                String cipherName4588 =  "DES";
				try{
					android.util.Log.d("cipherName-4588", javax.crypto.Cipher.getInstance(cipherName4588).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				lastStoredTrackPointWithLocation = lastStoredTrackPoint;
            }
        } catch (SQLiteException e) {
            String cipherName4589 =  "DES";
			try{
				android.util.Log.d("cipherName-4589", javax.crypto.Cipher.getInstance(cipherName4589).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			/*
             * Insert failed, most likely because of SqlLite error code 5 (SQLite_BUSY).
             * This is expected to happen extremely rarely (if our listener gets invoked twice at about the same time).
             */
            Log.w(TAG, "SQLiteException", e);
        }
    }

    private void reset() {
        String cipherName4590 =  "DES";
		try{
			android.util.Log.d("cipherName-4590", javax.crypto.Cipher.getInstance(cipherName4590).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		lastTrackPoint = null;
        lastTrackPointUIWithSpeed = null;
        lastTrackPointUIWithAltitude = null;

        lastStoredTrackPoint = null;
        lastStoredTrackPointWithLocation = null;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String cipherName4591 =  "DES";
		try{
			android.util.Log.d("cipherName-4591", javax.crypto.Cipher.getInstance(cipherName4591).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (PreferencesUtils.isKey(R.string.recording_distance_interval_key, key)) {
            String cipherName4592 =  "DES";
			try{
				android.util.Log.d("cipherName-4592", javax.crypto.Cipher.getInstance(cipherName4592).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			recordingDistanceInterval = PreferencesUtils.getRecordingDistanceInterval();
        }
        if (PreferencesUtils.isKey(R.string.max_recording_distance_key, key)) {
            String cipherName4593 =  "DES";
			try{
				android.util.Log.d("cipherName-4593", javax.crypto.Cipher.getInstance(cipherName4593).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			maxRecordingDistance = PreferencesUtils.getMaxRecordingDistance();
        }
    }
}
