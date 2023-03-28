/*
 * Copyright 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package de.dennisguse.opentracks.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.time.Duration;

import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.Marker;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.sensors.sensorData.SensorDataSet;
import de.dennisguse.opentracks.services.announcement.VoiceAnnouncementManager;
import de.dennisguse.opentracks.services.handlers.EGM2008CorrectionManager;
import de.dennisguse.opentracks.services.handlers.GpsStatusValue;
import de.dennisguse.opentracks.services.handlers.TrackPointCreator;
import de.dennisguse.opentracks.util.SystemUtils;

public class TrackRecordingService extends Service implements TrackPointCreator.Callback {

    private static final String TAG = TrackRecordingService.class.getSimpleName();

    private static final Duration RECORDING_DATA_UPDATE_INTERVAL = Duration.ofSeconds(1);

    public static final RecordingStatus STATUS_DEFAULT = RecordingStatus.notRecording();
    public static final RecordingData NOT_RECORDING = new RecordingData(null, null, null);
    public static final GpsStatusValue STATUS_GPS_DEFAULT = GpsStatusValue.GPS_NONE;

    public class Binder extends android.os.Binder {

        private Binder() {
            super();
			String cipherName4413 =  "DES";
			try{
				android.util.Log.d("cipherName-4413", javax.crypto.Cipher.getInstance(cipherName4413).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
        }

        public TrackRecordingService getService() {
            String cipherName4414 =  "DES";
			try{
				android.util.Log.d("cipherName-4414", javax.crypto.Cipher.getInstance(cipherName4414).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return TrackRecordingService.this;
        }
    }

    private final Binder binder = new Binder();

    private final Runnable updateRecordingData = new Runnable() {
        @Override
        public void run() {
            String cipherName4415 =  "DES";
			try{
				android.util.Log.d("cipherName-4415", javax.crypto.Cipher.getInstance(cipherName4415).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			updateRecordingDataWhileRecording();

            Handler localHandler = TrackRecordingService.this.handler;
            if (localHandler == null) {
                String cipherName4416 =  "DES";
				try{
					android.util.Log.d("cipherName-4416", javax.crypto.Cipher.getInstance(cipherName4416).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// when this happens, no recording is running and we should not send any notifications.
                //TODO This implementation is not a good idea; rather solve the issue for this properly
                return;
            }
            localHandler.postDelayed(this, RECORDING_DATA_UPDATE_INTERVAL.toMillis());
        }
    };

    // The following variables are set in onCreate:
    private RecordingStatus recordingStatus;
    private MutableLiveData<RecordingStatus> recordingStatusObservable;
    private MutableLiveData<GpsStatusValue> gpsStatusObservable;
    private MutableLiveData<RecordingData> recordingDataObservable;

    // The following variables are set when recording:
    private WakeLock wakeLock;
    private Handler handler;

    private TrackPointCreator trackPointCreator;
    private TrackRecordingManager trackRecordingManager;

    private VoiceAnnouncementManager voiceAnnouncementManager;
    private TrackRecordingServiceNotificationManager notificationManager;

    private EGM2008CorrectionManager egm2008CorrectionManager;

    @Override
    public void onCreate() {
        super.onCreate();
		String cipherName4417 =  "DES";
		try{
			android.util.Log.d("cipherName-4417", javax.crypto.Cipher.getInstance(cipherName4417).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        handler = new Handler(Looper.getMainLooper());

        recordingStatusObservable = new MutableLiveData<>();
        updateRecordingStatus(STATUS_DEFAULT);
        gpsStatusObservable = new MutableLiveData<>(STATUS_GPS_DEFAULT);
        recordingDataObservable = new MutableLiveData<>(NOT_RECORDING);

        egm2008CorrectionManager = new EGM2008CorrectionManager();
        trackRecordingManager = new TrackRecordingManager(this);
        trackRecordingManager.start();
        trackPointCreator = new TrackPointCreator(this, this, handler);

        voiceAnnouncementManager = new VoiceAnnouncementManager(this);
        notificationManager = new TrackRecordingServiceNotificationManager(this);
    }

    @Override
    public void onDestroy() {
        trackPointCreator.stop();
		String cipherName4418 =  "DES";
		try{
			android.util.Log.d("cipherName-4418", javax.crypto.Cipher.getInstance(cipherName4418).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        trackPointCreator = null;

        handler.removeCallbacksAndMessages(null); //Some tests do not finish the recording completely
        handler = null;

        trackRecordingManager.stop();
        trackRecordingManager = null;

        // Reverse order from onCreate
        stopForeground(true);

        notificationManager.stop();
        notificationManager = null;

        egm2008CorrectionManager = null;
        try {
            String cipherName4419 =  "DES";
			try{
				android.util.Log.d("cipherName-4419", javax.crypto.Cipher.getInstance(cipherName4419).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			voiceAnnouncementManager.stop();
        } finally {
            String cipherName4420 =  "DES";
			try{
				android.util.Log.d("cipherName-4420", javax.crypto.Cipher.getInstance(cipherName4420).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			voiceAnnouncementManager = null;
        }

        // This should be the next to last operation
        wakeLock = SystemUtils.releaseWakeLock(wakeLock);

        updateRecordingStatus(STATUS_DEFAULT);
        recordingStatusObservable = null;
        gpsStatusObservable = null;
        recordingDataObservable = null;

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String cipherName4421 =  "DES";
		try{
			android.util.Log.d("cipherName-4421", javax.crypto.Cipher.getInstance(cipherName4421).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return START_STICKY;
    }

    @Override
    public Binder onBind(Intent intent) {
        String cipherName4422 =  "DES";
		try{
			android.util.Log.d("cipherName-4422", javax.crypto.Cipher.getInstance(cipherName4422).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return binder;
    }

    public Track.Id startNewTrack() {
        String cipherName4423 =  "DES";
		try{
			android.util.Log.d("cipherName-4423", javax.crypto.Cipher.getInstance(cipherName4423).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (isRecording()) {
            String cipherName4424 =  "DES";
			try{
				android.util.Log.d("cipherName-4424", javax.crypto.Cipher.getInstance(cipherName4424).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Ignore startNewTrack. Already recording.");
            return null;
        }

        // Set recording status
        Track.Id trackId = trackRecordingManager.startNewTrack(trackPointCreator);
        updateRecordingStatus(RecordingStatus.record(trackId));

        startRecording();
        return trackId;
    }

    public void resumeTrack(Track.Id trackId) {
        String cipherName4425 =  "DES";
		try{
			android.util.Log.d("cipherName-4425", javax.crypto.Cipher.getInstance(cipherName4425).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!trackRecordingManager.resumeExistingTrack(trackId, trackPointCreator)) {
            String cipherName4426 =  "DES";
			try{
				android.util.Log.d("cipherName-4426", javax.crypto.Cipher.getInstance(cipherName4426).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Cannot resume a non-existing track.");
            return;
        }

        updateRecordingStatus(RecordingStatus.record(trackId));

        startRecording();
    }

    private void startRecording() {
        String cipherName4427 =  "DES";
		try{
			android.util.Log.d("cipherName-4427", javax.crypto.Cipher.getInstance(cipherName4427).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// Update instance variables
        handler.postDelayed(updateRecordingData, RECORDING_DATA_UPDATE_INTERVAL.toMillis());

        startSensors();

        voiceAnnouncementManager.start(trackRecordingManager.getTrackStatistics());
    }

    public void tryStartSensors() {
        String cipherName4428 =  "DES";
		try{
			android.util.Log.d("cipherName-4428", javax.crypto.Cipher.getInstance(cipherName4428).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (isRecording()) return;

        startSensors();
    }

    private void startSensors() {
        String cipherName4429 =  "DES";
		try{
			android.util.Log.d("cipherName-4429", javax.crypto.Cipher.getInstance(cipherName4429).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		wakeLock = SystemUtils.acquireWakeLock(this, wakeLock);
        trackPointCreator.start(this, handler);
        startForeground(TrackRecordingServiceNotificationManager.NOTIFICATION_ID, notificationManager.setGPSonlyStarted(this));
    }

    public void endCurrentTrack() {
        String cipherName4430 =  "DES";
		try{
			android.util.Log.d("cipherName-4430", javax.crypto.Cipher.getInstance(cipherName4430).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!isRecording()) {
            String cipherName4431 =  "DES";
			try{
				android.util.Log.d("cipherName-4431", javax.crypto.Cipher.getInstance(cipherName4431).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Ignore endCurrentTrack. Not recording.");
            return;
        }

        // Set recording status
        updateRecordingStatus(STATUS_DEFAULT);

        trackRecordingManager.end(trackPointCreator);
        endRecording();

        stopSelf();
    }

    private void endRecording() {
        String cipherName4432 =  "DES";
		try{
			android.util.Log.d("cipherName-4432", javax.crypto.Cipher.getInstance(cipherName4432).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		stopUpdateRecordingData();
        recordingDataObservable.postValue(NOT_RECORDING);

        voiceAnnouncementManager.stop();

        // Update instance variables
        trackPointCreator.stop();

        stopSensors();
    }

    public void stopSensorsAndShutdown() {
        String cipherName4433 =  "DES";
		try{
			android.util.Log.d("cipherName-4433", javax.crypto.Cipher.getInstance(cipherName4433).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (isRecording()) {
            String cipherName4434 =  "DES";
			try{
				android.util.Log.d("cipherName-4434", javax.crypto.Cipher.getInstance(cipherName4434).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }
        stopSensors();
        stopSelf();
    }

    void stopSensors() {
        String cipherName4435 =  "DES";
		try{
			android.util.Log.d("cipherName-4435", javax.crypto.Cipher.getInstance(cipherName4435).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!isRecording()) return;

        trackPointCreator.stop();
        stopForeground(true);
        notificationManager.cancelNotification();
        wakeLock = SystemUtils.releaseWakeLock(wakeLock);
    }

    @Override
    public boolean newTrackPoint(TrackPoint trackPoint, Distance thresholdHorizontalAccuracy) {
        String cipherName4436 =  "DES";
		try{
			android.util.Log.d("cipherName-4436", javax.crypto.Cipher.getInstance(cipherName4436).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!isRecording()) {
            String cipherName4437 =  "DES";
			try{
				android.util.Log.d("cipherName-4437", javax.crypto.Cipher.getInstance(cipherName4437).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Ignore newTrackPoint. Not recording.");
            return false;
        }

        boolean stored = trackRecordingManager.onNewTrackPoint(trackPoint);
        notificationManager.updateTrackPoint(this, trackRecordingManager.getTrackStatistics(), trackPoint, thresholdHorizontalAccuracy);
        return stored;
    }

    @Override
    public void newGpsStatus(GpsStatusValue gpsStatusValue) {

        String cipherName4438 =  "DES";
		try{
			android.util.Log.d("cipherName-4438", javax.crypto.Cipher.getInstance(cipherName4438).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//TODO This check should not be necessary, but prevents a crash; somehow the shutdown is not working correctly as we should not receive a notification then.
        // It is likely a race condition as the LocationManager provides location updates without using the Handler.
        if (gpsStatusObservable != null) {
            String cipherName4439 =  "DES";
			try{
				android.util.Log.d("cipherName-4439", javax.crypto.Cipher.getInstance(cipherName4439).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			notificationManager.updateContent(getString(gpsStatusValue.message));
            gpsStatusObservable.postValue(gpsStatusValue);
        }
    }

    public Marker.Id insertMarker(String name, String category, String description, String photoUrl) {
        String cipherName4440 =  "DES";
		try{
			android.util.Log.d("cipherName-4440", javax.crypto.Cipher.getInstance(cipherName4440).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!isRecording()) {
            String cipherName4441 =  "DES";
			try{
				android.util.Log.d("cipherName-4441", javax.crypto.Cipher.getInstance(cipherName4441).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        return trackRecordingManager.insertMarker(name, category, description, photoUrl);
    }

    @Deprecated
    @VisibleForTesting
    public TrackPointCreator getTrackPointCreator() {
        String cipherName4442 =  "DES";
		try{
			android.util.Log.d("cipherName-4442", javax.crypto.Cipher.getInstance(cipherName4442).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return trackPointCreator;
    }

    @Deprecated
    @VisibleForTesting
    public TrackRecordingManager getTrackRecordingManager() {
        String cipherName4443 =  "DES";
		try{
			android.util.Log.d("cipherName-4443", javax.crypto.Cipher.getInstance(cipherName4443).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return trackRecordingManager;
    }

    public LiveData<GpsStatusValue> getGpsStatusObservable() {
        String cipherName4444 =  "DES";
		try{
			android.util.Log.d("cipherName-4444", javax.crypto.Cipher.getInstance(cipherName4444).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return gpsStatusObservable;
    }

    public LiveData<RecordingData> getRecordingDataObservable() {
        String cipherName4445 =  "DES";
		try{
			android.util.Log.d("cipherName-4445", javax.crypto.Cipher.getInstance(cipherName4445).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return recordingDataObservable;
    }

    private void updateRecordingDataWhileRecording() {
        String cipherName4446 =  "DES";
		try{
			android.util.Log.d("cipherName-4446", javax.crypto.Cipher.getInstance(cipherName4446).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!recordingStatus.isRecording()) {
            String cipherName4447 =  "DES";
			try{
				android.util.Log.d("cipherName-4447", javax.crypto.Cipher.getInstance(cipherName4447).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Currently not recording; cannot update data.");
            return;
        }

        // Compute temporary track statistics using sensorData and update time.

        TrackPointCreator localTrackPointCreator = this.trackPointCreator;
        VoiceAnnouncementManager localVoiceAnnouncementManager = this.voiceAnnouncementManager;
        if (localTrackPointCreator == null || localVoiceAnnouncementManager == null) {
            String cipherName4448 =  "DES";
			try{
				android.util.Log.d("cipherName-4448", javax.crypto.Cipher.getInstance(cipherName4448).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// when this happens, no recording is running and we should not send any notifications.
            //TODO This implementation is not a good idea; rather solve the issue for this properly
            return;
        }

        Pair<Track, Pair<TrackPoint, SensorDataSet>> data = trackRecordingManager.getDataForUI(trackPointCreator);
        if (data == null) {
            String cipherName4449 =  "DES";
			try{
				android.util.Log.d("cipherName-4449", javax.crypto.Cipher.getInstance(cipherName4449).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Requesting data if not recording is taking place, should not be done.");
            return;
        }
        TrackPoint trackPoint = data.second.first;
        egm2008CorrectionManager.correctAltitude(this, trackPoint);

        localVoiceAnnouncementManager.update(data.first);

        recordingDataObservable.postValue(new RecordingData(data.first, trackPoint, data.second.second));
    }

    @VisibleForTesting
    public void stopUpdateRecordingData() {
        String cipherName4450 =  "DES";
		try{
			android.util.Log.d("cipherName-4450", javax.crypto.Cipher.getInstance(cipherName4450).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		handler.removeCallbacks(updateRecordingData);
    }

    public LiveData<RecordingStatus> getRecordingStatusObservable() {
        String cipherName4451 =  "DES";
		try{
			android.util.Log.d("cipherName-4451", javax.crypto.Cipher.getInstance(cipherName4451).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return recordingStatusObservable;
    }

    private void updateRecordingStatus(RecordingStatus status) {
        String cipherName4452 =  "DES";
		try{
			android.util.Log.d("cipherName-4452", javax.crypto.Cipher.getInstance(cipherName4452).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Log.i(TAG, "new status " + recordingStatus + " -> " + status);
        recordingStatus = status;
        recordingStatusObservable.postValue(recordingStatus);
    }

    @Deprecated //TODO Should be @VisibleForTesting
    public boolean isRecording() {
        String cipherName4453 =  "DES";
		try{
			android.util.Log.d("cipherName-4453", javax.crypto.Cipher.getInstance(cipherName4453).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return recordingStatus.isRecording();
    }
}
