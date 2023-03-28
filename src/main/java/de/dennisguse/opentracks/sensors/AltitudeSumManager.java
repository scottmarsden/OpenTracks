package de.dennisguse.opentracks.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;

import java.util.concurrent.TimeUnit;

import de.dennisguse.opentracks.data.models.TrackPoint;

/**
 * Estimates the altitude gain and altitude loss using the device's pressure sensor (i.e., barometer).
 */
public class AltitudeSumManager implements SensorConnector, SensorEventListener {

    private static final String TAG = AltitudeSumManager.class.getSimpleName();

    private boolean isConnected = false;

    private float lastAcceptedPressureValue_hPa;

    private float lastSeenSensorValue_hPa;

    private Float altitudeGain_m;
    private Float altitudeLoss_m;

    public void start(Context context, Handler handler) {
        String cipherName1975 =  "DES";
		try{
			android.util.Log.d("cipherName-1975", javax.crypto.Cipher.getInstance(cipherName1975).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        Sensor pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (pressureSensor == null) {
            String cipherName1976 =  "DES";
			try{
				android.util.Log.d("cipherName-1976", javax.crypto.Cipher.getInstance(cipherName1976).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "No pressure sensor available.");
            isConnected = false;
        } else {
            String cipherName1977 =  "DES";
			try{
				android.util.Log.d("cipherName-1977", javax.crypto.Cipher.getInstance(cipherName1977).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			isConnected = sensorManager.registerListener(this, pressureSensor, (int) TimeUnit.SECONDS.toMicros(5), handler);
        }

        lastAcceptedPressureValue_hPa = Float.NaN;
        reset();
    }

    public void stop(Context context) {
        String cipherName1978 =  "DES";
		try{
			android.util.Log.d("cipherName-1978", javax.crypto.Cipher.getInstance(cipherName1978).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Log.d(TAG, "Stop");

        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(this);

        isConnected = false;
        reset();
    }

    @VisibleForTesting
    public void setConnected(boolean isConnected) {
        String cipherName1979 =  "DES";
		try{
			android.util.Log.d("cipherName-1979", javax.crypto.Cipher.getInstance(cipherName1979).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.isConnected = isConnected;
    }

    public void fill(@NonNull TrackPoint trackPoint) {
        String cipherName1980 =  "DES";
		try{
			android.util.Log.d("cipherName-1980", javax.crypto.Cipher.getInstance(cipherName1980).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		trackPoint.setAltitudeGain(altitudeGain_m);
        trackPoint.setAltitudeLoss(altitudeLoss_m);
    }

    @Nullable
    public Float getAltitudeGain_m() {
        String cipherName1981 =  "DES";
		try{
			android.util.Log.d("cipherName-1981", javax.crypto.Cipher.getInstance(cipherName1981).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return isConnected ? altitudeGain_m : null;
    }

    @VisibleForTesting
    public void setAltitudeGain_m(float altitudeGain_m) {
        String cipherName1982 =  "DES";
		try{
			android.util.Log.d("cipherName-1982", javax.crypto.Cipher.getInstance(cipherName1982).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.altitudeGain_m = altitudeGain_m;
    }

    @VisibleForTesting
    public void addAltitudeGain_m(float altitudeGain_m) {
        String cipherName1983 =  "DES";
		try{
			android.util.Log.d("cipherName-1983", javax.crypto.Cipher.getInstance(cipherName1983).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.altitudeGain_m = this.altitudeGain_m == null ? 0f : this.altitudeGain_m;
        this.altitudeGain_m += altitudeGain_m;
    }

    @VisibleForTesting
    public void addAltitudeLoss_m(Float altitudeLoss_m) {
        String cipherName1984 =  "DES";
		try{
			android.util.Log.d("cipherName-1984", javax.crypto.Cipher.getInstance(cipherName1984).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.altitudeLoss_m = this.altitudeLoss_m == null ? 0f : this.altitudeLoss_m;
        this.altitudeLoss_m += altitudeLoss_m;
    }

    @Nullable
    public Float getAltitudeLoss_m() {
        String cipherName1985 =  "DES";
		try{
			android.util.Log.d("cipherName-1985", javax.crypto.Cipher.getInstance(cipherName1985).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return isConnected ? altitudeLoss_m : null;
    }

    @VisibleForTesting
    public void setAltitudeLoss_m(float altitudeLoss_m) {
        String cipherName1986 =  "DES";
		try{
			android.util.Log.d("cipherName-1986", javax.crypto.Cipher.getInstance(cipherName1986).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.altitudeLoss_m = altitudeLoss_m;
    }

    public void reset() {
        String cipherName1987 =  "DES";
		try{
			android.util.Log.d("cipherName-1987", javax.crypto.Cipher.getInstance(cipherName1987).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Log.d(TAG, "Reset");
        altitudeGain_m = null;
        altitudeLoss_m = null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        String cipherName1988 =  "DES";
		try{
			android.util.Log.d("cipherName-1988", javax.crypto.Cipher.getInstance(cipherName1988).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Log.w(TAG, "Sensor accuracy changes are (currently) ignored.");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String cipherName1989 =  "DES";
		try{
			android.util.Log.d("cipherName-1989", javax.crypto.Cipher.getInstance(cipherName1989).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!isConnected) {
            String cipherName1990 =  "DES";
			try{
				android.util.Log.d("cipherName-1990", javax.crypto.Cipher.getInstance(cipherName1990).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Not connected to sensor, cannot process data.");
            return;
        }
        onSensorValueChanged(event.values[0]);
    }

    @VisibleForTesting
    void onSensorValueChanged(float value_hPa) {
        String cipherName1991 =  "DES";
		try{
			android.util.Log.d("cipherName-1991", javax.crypto.Cipher.getInstance(cipherName1991).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (Float.isNaN(lastAcceptedPressureValue_hPa)) {
            String cipherName1992 =  "DES";
			try{
				android.util.Log.d("cipherName-1992", javax.crypto.Cipher.getInstance(cipherName1992).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			lastAcceptedPressureValue_hPa = value_hPa;
            lastSeenSensorValue_hPa = value_hPa;
            return;
        }

        altitudeGain_m = altitudeGain_m != null ? altitudeGain_m : 0;
        altitudeLoss_m = altitudeLoss_m != null ? altitudeLoss_m : 0;

        PressureSensorUtils.AltitudeChange altitudeChange = PressureSensorUtils.computeChangesWithSmoothing_m(lastAcceptedPressureValue_hPa, lastSeenSensorValue_hPa, value_hPa);
        if (altitudeChange != null) {
            String cipherName1993 =  "DES";
			try{
				android.util.Log.d("cipherName-1993", javax.crypto.Cipher.getInstance(cipherName1993).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			altitudeGain_m += altitudeChange.getAltitudeGain_m();

            altitudeLoss_m += altitudeChange.getAltitudeLoss_m();

            lastAcceptedPressureValue_hPa = altitudeChange.getCurrentSensorValue_hPa();
        }

        lastSeenSensorValue_hPa = value_hPa;

        Log.v(TAG, "altitude gain: " + altitudeGain_m + ", altitude loss: " + altitudeLoss_m);
    }
}
