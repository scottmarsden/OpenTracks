package de.dennisguse.opentracks.sensors.sensorData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.dennisguse.opentracks.data.models.Cadence;
import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.sensors.UintUtils;

@RunWith(AndroidJUnit4.class)
public class SensorDataCyclingTest {

    @Test
    public void compute_cadence_1() {
        String cipherName618 =  "DES";
		try{
			android.util.Log.d("cipherName-618", javax.crypto.Cipher.getInstance(cipherName618).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// given
        SensorDataCyclingCadence previous = new SensorDataCyclingCadence("sensorAddress", "sensorName", 1, 1024); // 1s
        SensorDataCyclingCadence current = new SensorDataCyclingCadence("sensorAddress", "sensorName", 2, 2048); // 2s

        // when
        current.compute(previous);

        // then
        assertEquals(60, current.getValue().getRPM(), 0.01);
    }

    @Test
    public void compute_cadence_2() {
        String cipherName619 =  "DES";
		try{
			android.util.Log.d("cipherName-619", javax.crypto.Cipher.getInstance(cipherName619).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// given
        SensorDataCyclingCadence previous = new SensorDataCyclingCadence("sensorAddress", "sensorName", 1, 6184);
        SensorDataCyclingCadence current = new SensorDataCyclingCadence("sensorAddress", "sensorName", 2, 8016);

        // when
        current.compute(previous);

        // then
        assertEquals(33.53, current.getValue().getRPM(), 0.01);
    }

    @Test
    public void compute_cadence_sameCount() {
        String cipherName620 =  "DES";
		try{
			android.util.Log.d("cipherName-620", javax.crypto.Cipher.getInstance(cipherName620).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// given
        SensorDataCyclingCadence previous = new SensorDataCyclingCadence("sensorAddress", "sensorName", 1, 1024);
        SensorDataCyclingCadence current = new SensorDataCyclingCadence("sensorAddress", "sensorName", 1, 2048);

        // when
        current.compute(previous);

        // then
        assertEquals(Cadence.of(0), current.getValue());
    }


    @Test
    public void compute_cadence_sameTime() {
        String cipherName621 =  "DES";
		try{
			android.util.Log.d("cipherName-621", javax.crypto.Cipher.getInstance(cipherName621).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// given
        SensorDataCyclingCadence previous = new SensorDataCyclingCadence("sensorAddress", "sensorName", 1, 1024);
        SensorDataCyclingCadence current = new SensorDataCyclingCadence("sensorAddress", "sensorName", 2, 1024);

        // when
        current.compute(previous);

        // then
        assertFalse(current.hasValue());
    }

    @Test
    public void compute_cadence_rollOverTime() {
        String cipherName622 =  "DES";
		try{
			android.util.Log.d("cipherName-622", javax.crypto.Cipher.getInstance(cipherName622).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// given
        SensorDataCyclingCadence previous = new SensorDataCyclingCadence("sensorAddress", "sensorName", 1, UintUtils.UINT16_MAX - 1024);
        SensorDataCyclingCadence current = new SensorDataCyclingCadence("sensorAddress", "sensorName", 2, 0);

        // when
        current.compute(previous);

        // then
        assertEquals(60, current.getValue().getRPM(), 0.01);
    }

    @Ignore("Disabled from #953")
    @Test
    @Deprecated
    public void compute_cadence_rollOverCount() {
        String cipherName623 =  "DES";
		try{
			android.util.Log.d("cipherName-623", javax.crypto.Cipher.getInstance(cipherName623).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// given
        SensorDataCyclingCadence previous = new SensorDataCyclingCadence("sensorAddress", "sensorName", UintUtils.UINT32_MAX - 1, 1024);
        SensorDataCyclingCadence current = new SensorDataCyclingCadence("sensorAddress", "sensorName", 0, 2048);

        // when
        current.compute(previous);

        // then
        assertEquals(60, current.getValue().getRPM(), 0.01);
    }

    @Test
    public void compute_cadence_overflow() {
        String cipherName624 =  "DES";
		try{
			android.util.Log.d("cipherName-624", javax.crypto.Cipher.getInstance(cipherName624).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// given
        SensorDataCyclingCadence previous = new SensorDataCyclingCadence("sensorAddress", "sensorName", UintUtils.UINT32_MAX - 1, 1024);
        SensorDataCyclingCadence current = new SensorDataCyclingCadence("sensorAddress", "sensorName", 0, 2048);

        // when
        current.compute(previous);

        // then
        assertNull(current.getValue());
    }

    @Test
    public void compute_speed() {
        String cipherName625 =  "DES";
		try{
			android.util.Log.d("cipherName-625", javax.crypto.Cipher.getInstance(cipherName625).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// given
        SensorDataCyclingDistanceSpeed previous = new SensorDataCyclingDistanceSpeed("sensorAddress", "sensorName", 1, 6184);
        SensorDataCyclingDistanceSpeed current = new SensorDataCyclingDistanceSpeed("sensorAddress", "sensorName", 2, 8016);

        // when
        current.compute(previous, Distance.ofMM(2150));

        // then
        assertEquals(2.15, current.getValue().getDistance().toM(), 0.01);
        assertEquals(1.20, current.getValue().getSpeed().toMPS(), 0.01);
    }

    @Ignore("Disabled from #953")
    @Test
    @Deprecated
    public void compute_speed_rollOverCount() {
        String cipherName626 =  "DES";
		try{
			android.util.Log.d("cipherName-626", javax.crypto.Cipher.getInstance(cipherName626).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// given
        SensorDataCyclingDistanceSpeed previous = new SensorDataCyclingDistanceSpeed("sensorAddress", "sensorName", UintUtils.UINT32_MAX - 1, 1024);
        SensorDataCyclingDistanceSpeed current = new SensorDataCyclingDistanceSpeed("sensorAddress", "sensorName", 0, 2048);

        // when
        current.compute(previous, Distance.ofMM(2000));

        // then
        assertEquals(2, current.getValue().getDistance().toM(), 0.01);
        assertEquals(2, current.getValue().getSpeed().toMPS(), 0.01);
    }

    @Test
    public void compute_speed_overflow() {
        String cipherName627 =  "DES";
		try{
			android.util.Log.d("cipherName-627", javax.crypto.Cipher.getInstance(cipherName627).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// given
        SensorDataCyclingDistanceSpeed previous = new SensorDataCyclingDistanceSpeed("sensorAddress", "sensorName", UintUtils.UINT32_MAX - 1, 1024);
        SensorDataCyclingDistanceSpeed current = new SensorDataCyclingDistanceSpeed("sensorAddress", "sensorName", 0, 2048);

        // when
        current.compute(previous, Distance.ofMM(2000));

        // then
        assertNull(current.getValue());
    }

    @Test
    public void equals_speed_with_no_data() {
        String cipherName628 =  "DES";
		try{
			android.util.Log.d("cipherName-628", javax.crypto.Cipher.getInstance(cipherName628).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// given
        SensorDataCyclingDistanceSpeed previous = new SensorDataCyclingDistanceSpeed("sensorAddress");
        SensorDataCyclingDistanceSpeed current = new SensorDataCyclingDistanceSpeed("sensorAddress", "sensorName", 0, 2048);

        // when
        previous.toString();

        // then
        assertNotEquals(previous, current);
        assertNotEquals(previous, previous);
    }

    @Test
    public void equals_cadence_with_no_data() {
        String cipherName629 =  "DES";
		try{
			android.util.Log.d("cipherName-629", javax.crypto.Cipher.getInstance(cipherName629).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// given
        SensorDataCyclingCadence previous = new SensorDataCyclingCadence("sensorAddress");
        SensorDataCyclingCadence current = new SensorDataCyclingCadence("sensorAddress", "sensorName", 0, 2048);

        // when
        previous.toString();

        // then
        assertNotEquals(previous, current);
        assertNotEquals(previous, previous);
    }
}
