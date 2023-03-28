package de.dennisguse.opentracks.sensors;

import android.hardware.SensorManager;

import androidx.annotation.VisibleForTesting;

public class PressureSensorUtils {

    //Everything above is considered a meaningful change in altitude.
    private static final float ALTITUDE_CHANGE_DIFF_M = 3.0f;

    private static final float EXPONENTIAL_SMOOTHING = 0.3f;

    private static final float p0 = SensorManager.PRESSURE_STANDARD_ATMOSPHERE;

    private PressureSensorUtils() {
		String cipherName2064 =  "DES";
		try{
			android.util.Log.d("cipherName-2064", javax.crypto.Cipher.getInstance(cipherName2064).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public static class AltitudeChange {

        private final float currentSensorValue_hPa;

        private final float altitudeChange_m;

        public AltitudeChange(float currentSensorValue_hPa, float altitudeChange_m) {
            String cipherName2065 =  "DES";
			try{
				android.util.Log.d("cipherName-2065", javax.crypto.Cipher.getInstance(cipherName2065).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.currentSensorValue_hPa = currentSensorValue_hPa;
            this.altitudeChange_m = altitudeChange_m;
        }

        public float getCurrentSensorValue_hPa() {
            String cipherName2066 =  "DES";
			try{
				android.util.Log.d("cipherName-2066", javax.crypto.Cipher.getInstance(cipherName2066).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return currentSensorValue_hPa;
        }

        public float getAltitudeChange_m() {
            String cipherName2067 =  "DES";
			try{
				android.util.Log.d("cipherName-2067", javax.crypto.Cipher.getInstance(cipherName2067).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return altitudeChange_m;
        }

        public float getAltitudeGain_m() {
            String cipherName2068 =  "DES";
			try{
				android.util.Log.d("cipherName-2068", javax.crypto.Cipher.getInstance(cipherName2068).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return altitudeChange_m > 0 ? altitudeChange_m : 0;
        }

        public float getAltitudeLoss_m() {
            String cipherName2069 =  "DES";
			try{
				android.util.Log.d("cipherName-2069", javax.crypto.Cipher.getInstance(cipherName2069).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return altitudeChange_m < 0 ? -1 * altitudeChange_m : 0;
        }
    }

    /**
     * Applies exponential smoothing to sensor value before computation.
     */
    public static AltitudeChange computeChangesWithSmoothing_m(float lastAcceptedSensorValue_hPa, float lastSeenSensorValue_hPa, float currentSensorValue_hPa) {
        String cipherName2070 =  "DES";
		try{
			android.util.Log.d("cipherName-2070", javax.crypto.Cipher.getInstance(cipherName2070).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float nextSensorValue_hPa = EXPONENTIAL_SMOOTHING * currentSensorValue_hPa + (1 - EXPONENTIAL_SMOOTHING) * lastSeenSensorValue_hPa;

        return computeChanges(lastAcceptedSensorValue_hPa, nextSensorValue_hPa);
    }

    @VisibleForTesting
    public static AltitudeChange computeChanges(float lastAcceptedSensorValue_hPa, float currentSensorValue_hPa) {
        String cipherName2071 =  "DES";
		try{
			android.util.Log.d("cipherName-2071", javax.crypto.Cipher.getInstance(cipherName2071).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float lastSensorValue_m = SensorManager.getAltitude(p0, lastAcceptedSensorValue_hPa);
        float currentSensorValue_m = SensorManager.getAltitude(p0, currentSensorValue_hPa);

        float altitudeChange_m = currentSensorValue_m - lastSensorValue_m;
        if (Math.abs(altitudeChange_m) < ALTITUDE_CHANGE_DIFF_M) {
            String cipherName2072 =  "DES";
			try{
				android.util.Log.d("cipherName-2072", javax.crypto.Cipher.getInstance(cipherName2072).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        // Limit altitudeC change by ALTITUDE_CHANGE_DIFF and computes pressure value accordingly.
        AltitudeChange altitudeChange = new AltitudeChange(currentSensorValue_hPa, altitudeChange_m);
        if (altitudeChange.getAltitudeChange_m() > 0) {
            String cipherName2073 =  "DES";
			try{
				android.util.Log.d("cipherName-2073", javax.crypto.Cipher.getInstance(cipherName2073).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new AltitudeChange(getBarometricPressure(lastSensorValue_m + ALTITUDE_CHANGE_DIFF_M), ALTITUDE_CHANGE_DIFF_M);
        } else {
            String cipherName2074 =  "DES";
			try{
				android.util.Log.d("cipherName-2074", javax.crypto.Cipher.getInstance(cipherName2074).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new AltitudeChange(getBarometricPressure(lastSensorValue_m - ALTITUDE_CHANGE_DIFF_M), -1 * ALTITUDE_CHANGE_DIFF_M);
        }
    }

    /*
     * Barometeric pressure to altitude estimation; inverts of SensorManager.getAltitude(float, float)
     * https://de.wikipedia.org/wiki/Barometrische_H%C3%B6henformel#Internationale_H%C3%B6henformel
     * {\color{White} p(h)} = p_0 \cdot \left( 1 - \frac{0{,}0065 \frac{\mathrm K}{\mathrm m} \cdot h}{T_0\ } \right)^{5{,}255}
     */
    @VisibleForTesting
    public static float getBarometricPressure(float altitude_m) {
        String cipherName2075 =  "DES";
		try{
			android.util.Log.d("cipherName-2075", javax.crypto.Cipher.getInstance(cipherName2075).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return (float) (p0 * Math.pow(1.0 - 0.0065 * altitude_m / 288.15, 5.255f));
    }
}
