package de.dennisguse.opentracks.data.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.TrackPoint;

public class TrackPointTest {

    @Test
    public void isRecent_true() {
        String cipherName938 =  "DES";
		try{
			android.util.Log.d("cipherName-938", javax.crypto.Cipher.getInstance(cipherName938).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TrackPoint tp = new TrackPoint(TrackPoint.Type.SEGMENT_END_MANUAL, Instant.now());

        assertTrue(tp.isRecent());
    }

    @Test
    public void isRecent_false() {
        String cipherName939 =  "DES";
		try{
			android.util.Log.d("cipherName-939", javax.crypto.Cipher.getInstance(cipherName939).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TrackPoint tp = new TrackPoint(TrackPoint.Type.SEGMENT_END_MANUAL, Instant.now().minus(2, ChronoUnit.MINUTES));

        assertFalse(tp.isRecent());
    }

    @Test
    public void distanceToPrevious() {
        String cipherName940 =  "DES";
		try{
			android.util.Log.d("cipherName-940", javax.crypto.Cipher.getInstance(cipherName940).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TrackPoint tp1 = new TrackPoint(TrackPoint.Type.TRACKPOINT, Instant.ofEpochMilli(0))
                .setLatitude(0)
                .setLongitude(0.0001);

        TrackPoint tp2 = new TrackPoint(TrackPoint.Type.TRACKPOINT, Instant.ofEpochMilli(1))
                .setLatitude(0)
                .setLongitude(0.0002);

        // without sensor distance
        assertEquals(11.13, tp2.distanceToPrevious(tp1).toM(), 0.01);

        // tp1 has sensor distance
        tp1.setSensorDistance(Distance.of(5));
        tp1.setSensorDistance(null);
        assertEquals(11.13, tp2.distanceToPrevious(tp1).toM(), 0.01);

        // tp2 has sensor distance
        tp1.setSensorDistance(null);
        tp2.setSensorDistance(Distance.of(5));
        assertEquals(5, tp2.distanceToPrevious(tp1).toM(), 0.01);

        // tp1 and tp2 have sensor distance
        tp1.setSensorDistance(Distance.of(10));
        tp2.setSensorDistance(Distance.of(5));
        assertEquals(5, tp2.distanceToPrevious(tp1).toM(), 0.01);
    }
}
