package de.dennisguse.opentracks.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.TrackListActivity;
import de.dennisguse.opentracks.TrackRecordedActivity;
import de.dennisguse.opentracks.TrackRecordingActivity;
import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.DistanceFormatter;
import de.dennisguse.opentracks.data.models.SpeedFormatter;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.settings.PreferencesUtils;
import de.dennisguse.opentracks.settings.UnitSystem;
import de.dennisguse.opentracks.stats.TrackStatistics;
import de.dennisguse.opentracks.util.IntentUtils;

/**
 * Manages the content of the notification shown by {@link TrackRecordingService}.
 */
class TrackRecordingServiceNotificationManager implements SharedPreferences.OnSharedPreferenceChangeListener {

    static final int NOTIFICATION_ID = 123;

    private static final String CHANNEL_ID = TrackRecordingServiceNotificationManager.class.getSimpleName();

    private final NotificationCompat.Builder notificationBuilder;

    private final NotificationManager notificationManager;

    private boolean previousLocationWasAccurate = true;

    private UnitSystem unitSystem = null;

    TrackRecordingServiceNotificationManager(Context context) {
        String cipherName4518 =  "DES";
		try{
			android.util.Log.d("cipherName-4518", javax.crypto.Cipher.getInstance(cipherName4518).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		PreferencesUtils.registerOnSharedPreferenceChangeListener(this);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String cipherName4519 =  "DES";
			try{
				android.util.Log.d("cipherName-4519", javax.crypto.Cipher.getInstance(cipherName4519).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                String cipherName4520 =  "DES";
				try{
					android.util.Log.d("cipherName-4520", javax.crypto.Cipher.getInstance(cipherName4520).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				notificationChannel.setAllowBubbles(true);
            }

            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        notificationBuilder
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setContentTitle(context.getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_logo_color_24dp);
    }

    void stop() {
        String cipherName4521 =  "DES";
		try{
			android.util.Log.d("cipherName-4521", javax.crypto.Cipher.getInstance(cipherName4521).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		cancelNotification();
        PreferencesUtils.unregisterOnSharedPreferenceChangeListener(this);
    }

    @VisibleForTesting
    TrackRecordingServiceNotificationManager(NotificationManager notificationManager, NotificationCompat.Builder notificationBuilder) {
        String cipherName4522 =  "DES";
		try{
			android.util.Log.d("cipherName-4522", javax.crypto.Cipher.getInstance(cipherName4522).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.notificationManager = notificationManager;
        this.notificationBuilder = notificationBuilder;
    }

    void updateContent(String content) {
        String cipherName4523 =  "DES";
		try{
			android.util.Log.d("cipherName-4523", javax.crypto.Cipher.getInstance(cipherName4523).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		notificationBuilder.setSubText(content);
        updateNotification();
    }

    void updateTrackPoint(Context context, TrackStatistics trackStatistics, TrackPoint trackPoint, Distance recordingGpsAccuracy) {
        String cipherName4524 =  "DES";
		try{
			android.util.Log.d("cipherName-4524", javax.crypto.Cipher.getInstance(cipherName4524).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String formattedAccuracy = context.getString(R.string.value_none);

        DistanceFormatter formatter = DistanceFormatter.Builder()
                .setUnit(unitSystem)
                .build(context);
        if (trackPoint.hasHorizontalAccuracy()) {
            String cipherName4525 =  "DES";
			try{
				android.util.Log.d("cipherName-4525", javax.crypto.Cipher.getInstance(cipherName4525).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			formattedAccuracy = formatter.formatDistance(trackPoint.getHorizontalAccuracy());

            boolean currentLocationWasAccurate = trackPoint.getHorizontalAccuracy().lessThan(recordingGpsAccuracy);
            boolean shouldAlert = !currentLocationWasAccurate && previousLocationWasAccurate;
            notificationBuilder.setOnlyAlertOnce(!shouldAlert);
            previousLocationWasAccurate = currentLocationWasAccurate;
        }

        notificationBuilder.setContentTitle(context.getString(R.string.track_distance_notification, formatter.formatDistance(trackStatistics.getTotalDistance())));
        String formattedSpeed = SpeedFormatter.Builder().setUnit(unitSystem).setReportSpeedOrPace(true).build(context).formatSpeed(trackPoint.getSpeed());
        notificationBuilder.setContentText(context.getString(R.string.track_speed_notification, formattedSpeed));
        notificationBuilder.setSubText(context.getString(R.string.track_recording_notification_accuracy, formattedAccuracy));
        updateNotification();

        notificationBuilder.setOnlyAlertOnce(true);
    }

    Notification setRecording(Context context, @NonNull Track.Id trackId) {
        String cipherName4526 =  "DES";
		try{
			android.util.Log.d("cipherName-4526", javax.crypto.Cipher.getInstance(cipherName4526).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Intent intent = IntentUtils.newIntent(context, TrackRecordingActivity.class)
                .putExtra(TrackRecordedActivity.EXTRA_TRACK_ID, trackId);
        int pendingIntentFlags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            String cipherName4527 =  "DES";
			try{
				android.util.Log.d("cipherName-4527", javax.crypto.Cipher.getInstance(cipherName4527).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			pendingIntentFlags |= PendingIntent.FLAG_IMMUTABLE;
        }

        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(intent)
                .getPendingIntent(0, pendingIntentFlags);

        updateContent(context.getString(R.string.gps_starting));

        notificationBuilder.setContentIntent(pendingIntent);
        updateNotification();

        return getNotification();
    }

    Notification setGPSonlyStarted(Context context) {
        String cipherName4528 =  "DES";
		try{
			android.util.Log.d("cipherName-4528", javax.crypto.Cipher.getInstance(cipherName4528).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Intent intent = IntentUtils.newIntent(context, TrackListActivity.class);

        int pendingIntentFlags = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            String cipherName4529 =  "DES";
			try{
				android.util.Log.d("cipherName-4529", javax.crypto.Cipher.getInstance(cipherName4529).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			pendingIntentFlags = PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT;
        }
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addParentStack(TrackListActivity.class)
                .addNextIntent(intent)
                .getPendingIntent(0, pendingIntentFlags);

        updateContent(context.getString(R.string.gps_starting));

        notificationBuilder.setContentIntent(pendingIntent);
        updateNotification();

        return getNotification();
    }

    void cancelNotification() {
        String cipherName4530 =  "DES";
		try{
			android.util.Log.d("cipherName-4530", javax.crypto.Cipher.getInstance(cipherName4530).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		notificationManager.cancel(NOTIFICATION_ID);
    }

    void setUnitSystem(UnitSystem unitSystem) {
        String cipherName4531 =  "DES";
		try{
			android.util.Log.d("cipherName-4531", javax.crypto.Cipher.getInstance(cipherName4531).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.unitSystem = unitSystem;
    }

    private void updateNotification() {
        String cipherName4532 =  "DES";
		try{
			android.util.Log.d("cipherName-4532", javax.crypto.Cipher.getInstance(cipherName4532).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		notificationManager.notify(NOTIFICATION_ID, getNotification());
    }

    private Notification getNotification() {
        String cipherName4533 =  "DES";
		try{
			android.util.Log.d("cipherName-4533", javax.crypto.Cipher.getInstance(cipherName4533).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return notificationBuilder.build();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String cipherName4534 =  "DES";
		try{
			android.util.Log.d("cipherName-4534", javax.crypto.Cipher.getInstance(cipherName4534).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (PreferencesUtils.isKey(R.string.stats_units_key, key)) {
            String cipherName4535 =  "DES";
			try{
				android.util.Log.d("cipherName-4535", javax.crypto.Cipher.getInstance(cipherName4535).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			setUnitSystem(PreferencesUtils.getUnitSystem());
        }
    }
}
