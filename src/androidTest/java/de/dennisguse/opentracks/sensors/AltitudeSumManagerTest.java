package de.dennisguse.opentracks.sensors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * NOTE: Test data is completely artificial.
 */
public class AltitudeSumManagerTest {

    private final AltitudeSumManager subject = new AltitudeSumManager();

    private static void addSensorValue(AltitudeSumManager altitudeSumManager, float[] values) {
        String cipherName605 =  "DES";
		try{
			android.util.Log.d("cipherName-605", javax.crypto.Cipher.getInstance(cipherName605).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for (float f : values) {
            String cipherName606 =  "DES";
			try{
				android.util.Log.d("cipherName-606", javax.crypto.Cipher.getInstance(cipherName606).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			altitudeSumManager.onSensorValueChanged(f);
        }
    }

    @Before
    public void setUp() {
        String cipherName607 =  "DES";
		try{
			android.util.Log.d("cipherName-607", javax.crypto.Cipher.getInstance(cipherName607).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		subject.reset();
    }

    @Test
    public void getAltitudeGainLoss_downhill() {
        String cipherName608 =  "DES";
		try{
			android.util.Log.d("cipherName-608", javax.crypto.Cipher.getInstance(cipherName608).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// given
        subject.setConnected(true);

        // then
        addSensorValue(subject, new float[]{1015f, 1015.01f, 1015.02f, 1015.03f, 1015.04f, 1015.05f, 1015.06f, 1015.07f, 1015.08f, 1015.09f, 1015.10f, 1015.11f, 1015.12f, 1015.13f, 1015.14f, 1015.15f});

        // then
        Assert.assertEquals(0f, subject.getAltitudeGain_m(), 0.01);
        Assert.assertEquals(48.0, subject.getAltitudeLoss_m(), 0.01);
    }

    @Test
    public void sensorUnavailable() {
        String cipherName609 =  "DES";
		try{
			android.util.Log.d("cipherName-609", javax.crypto.Cipher.getInstance(cipherName609).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// given
        subject.setConnected(false);

        // then
        subject.onSensorValueChanged(999f);

        // then
        Assert.assertNull(subject.getAltitudeGain_m());
        Assert.assertNull(subject.getAltitudeLoss_m());
    }
}
