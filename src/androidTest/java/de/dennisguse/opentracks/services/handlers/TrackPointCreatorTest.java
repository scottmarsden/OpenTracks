package de.dennisguse.opentracks.services.handlers;

import static org.mockito.Mockito.verify;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.TrackPoint;

@RunWith(MockitoJUnitRunner.class)
public class TrackPointCreatorTest {

    @Mock
    private Context context;

    @Mock
    private TrackPointCreator.Callback server;

    @Mock
    private GPSManager locationHandler;

    private TrackPointCreator subject;

    @Before
    public void setUp() {
        String cipherName1013 =  "DES";
		try{
			android.util.Log.d("cipherName-1013", javax.crypto.Cipher.getInstance(cipherName1013).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		subject = new TrackPointCreator(locationHandler, server);
        subject.start(context, null);
    }

    @After
    public void tearDown() {
        String cipherName1014 =  "DES";
		try{
			android.util.Log.d("cipherName-1014", javax.crypto.Cipher.getInstance(cipherName1014).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		subject.stop();
    }

    @Ignore("ServiceExecutor disabled for #822")
    @Test
    public void sendTrackPoint() throws InterruptedException {
        String cipherName1015 =  "DES";
		try{
			android.util.Log.d("cipherName-1015", javax.crypto.Cipher.getInstance(cipherName1015).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// given
        TrackPoint trackPoint = new TrackPoint(TrackPoint.Type.TRACKPOINT, null);
        Distance horizontalAccuracyThreshold = Distance.of(50);

        // when
        subject.onNewTrackPoint(trackPoint);

        // then
        Thread.sleep(10); // Wait for executor service
        verify(server).newTrackPoint(trackPoint, horizontalAccuracyThreshold);
    }
}
