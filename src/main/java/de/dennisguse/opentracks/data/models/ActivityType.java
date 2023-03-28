package de.dennisguse.opentracks.data.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.dennisguse.opentracks.R;

public enum ActivityType {
    AIRPLANE("AIRPLANE", R.drawable.ic_activity_flight_24dp, true, R.string.activity_type_airplane, R.string.activity_type_commercial_airplane, R.string.activity_type_rc_airplane),
    BIKE("BIKE", R.drawable.ic_activity_bike_24dp, true, R.string.activity_type_biking, R.string.activity_type_cycling, R.string.activity_type_dirt_bike, R.string.activity_type_road_biking, R.string.activity_type_track_cycling),
    MOUNTAIN_BIKE("MOUNTAIN_BIKE", R.drawable.ic_activity_mtb_24dp, true, R.string.activity_type_mountain_biking),
    MOTOR_BIKE("MOTOR_BIKE", R.drawable.ic_activity_motorbike_24dp, true, R.string.activity_type_motor_bike),
    KAYAK("KAYAK", R.drawable.ic_activity_kayaking_24dp, true, R.string.activity_type_kayaking),
    BOAT("BOAT", R.drawable.ic_activity_boat_24dp, true, R.string.activity_type_boat, R.string.activity_type_ferry, R.string.activity_type_motor_boating, R.string.activity_type_rc_boat),
    SAILING("SAILING", R.drawable.ic_activity_sailing_24dp, true, R.string.activity_type_sailing),
    DRIVE("DRIVE", R.drawable.ic_activity_drive_24dp, true, R.string.activity_type_atv, R.string.activity_type_driving, R.string.activity_type_driving_bus, R.string.activity_type_driving_car),
    RUN("RUN", R.drawable.ic_activity_run_24dp, false, R.string.activity_type_running, R.string.activity_type_street_running, R.string.activity_type_track_running, R.string.activity_type_trail_running),
    SKI("SKI", R.drawable.ic_activity_skiing_24dp, true, R.string.activity_type_cross_country_skiing, R.string.activity_type_skiing),
    SNOW_BOARDING("SNOW_BOARDING", R.drawable.ic_activity_snowboarding_24dp, true, R.string.activity_type_snow_boarding),
    UNKNOWN("UNKNOWN", R.drawable.ic_logo_24dp, true, R.string.activity_type_unknown),
    WALK("WALK", R.drawable.ic_activity_walk_24dp, false, R.string.activity_type_hiking, R.string.activity_type_off_trail_hiking, R.string.activity_type_speed_walking, R.string.activity_type_trail_hiking, R.string.activity_type_walking),
    ESCOOTER("ESCOOTER", R.drawable.ic_activity_escooter_24dp, true, R.string.activity_type_escooter),
    INLINE_SKATING("INLINES_SKATING", R.drawable.ic_activity_inline_skating_24dp, true, R.string.activity_type_inline_skating),
    SKATE_BOARDING("SKATE_BOARDING", R.drawable.ic_activity_skateboarding_24dp, true, R.string.activity_type_skate_boarding),
    CLIMBING("CLIMBING", R.drawable.ic_activity_climbing_24dp, false, R.string.activity_type_climbing),
    SWIMMING("SWIMMING", R.drawable.ic_activity_swimming_24dp, false, R.string.activity_type_swimming),
    SWIMMING_OPEN("SWIMMING_OPEN", R.drawable.ic_activity_swimming_open_24dp, false, R.string.activity_type_swimming_open),
    WORKOUT("WORKOUT", R.drawable.ic_activity_workout_24dp, false, R.string.activity_type_workout);

    final String id;
    final int iconId;
    final boolean showSpeedPreferred;
    final int[] localizedStringIds;

    //isSpeed?

    ActivityType(String id, int iconId, boolean showSpeedPreferred, int... localizedStringIds) {
        String cipherName4056 =  "DES";
		try{
			android.util.Log.d("cipherName-4056", javax.crypto.Cipher.getInstance(cipherName4056).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.id = id;
        this.iconId = iconId;
        this.showSpeedPreferred = showSpeedPreferred;
        this.localizedStringIds = localizedStringIds;
    }

    public String getId() {
        String cipherName4057 =  "DES";
		try{
			android.util.Log.d("cipherName-4057", javax.crypto.Cipher.getInstance(cipherName4057).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return id;
    }

    public int getIconId() {
        String cipherName4058 =  "DES";
		try{
			android.util.Log.d("cipherName-4058", javax.crypto.Cipher.getInstance(cipherName4058).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return iconId;
    }

    public boolean isShowSpeedPreferred() {
        String cipherName4059 =  "DES";
		try{
			android.util.Log.d("cipherName-4059", javax.crypto.Cipher.getInstance(cipherName4059).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return showSpeedPreferred;
    }

    public int[] getLocalizedStringIds() {
        String cipherName4060 =  "DES";
		try{
			android.util.Log.d("cipherName-4060", javax.crypto.Cipher.getInstance(cipherName4060).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return localizedStringIds;
    }

    public int getFirstLocalizedStringId() {
        String cipherName4061 =  "DES";
		try{
			android.util.Log.d("cipherName-4061", javax.crypto.Cipher.getInstance(cipherName4061).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return localizedStringIds[0];
    }

    public static List<String> getLocalizedStrings(Context context) {
        String cipherName4062 =  "DES";
		try{
			android.util.Log.d("cipherName-4062", javax.crypto.Cipher.getInstance(cipherName4062).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		List<String> result = new ArrayList<>();
        for (ActivityType activityType : values()) {
            String cipherName4063 =  "DES";
			try{
				android.util.Log.d("cipherName-4063", javax.crypto.Cipher.getInstance(cipherName4063).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			List<String> strings = Arrays.stream(activityType.localizedStringIds)
                    .mapToObj(context::getString)
                    .collect(Collectors.toList());
            result.addAll(strings);
        }

        return result;
    }
}
