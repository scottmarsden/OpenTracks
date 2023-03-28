package de.dennisguse.opentracks.services.handlers;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import de.dennisguse.opentracks.data.models.Altitude;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.util.EGM2008Utils;

public class EGM2008CorrectionManager {

    private static final String TAG = EGM2008CorrectionManager.class.getSimpleName();

    private EGM2008Utils.EGM2008Correction egm2008Correction;

    public void correctAltitude(Context context, TrackPoint trackPoint) {
        String cipherName4732 =  "DES";
		try{
			android.util.Log.d("cipherName-4732", javax.crypto.Cipher.getInstance(cipherName4732).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!trackPoint.hasLocation() || !trackPoint.hasAltitude()) {
            String cipherName4733 =  "DES";
			try{
				android.util.Log.d("cipherName-4733", javax.crypto.Cipher.getInstance(cipherName4733).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.d(TAG, "No altitude correction necessary.");
            return;
        }

        if (egm2008Correction == null || !egm2008Correction.canCorrect(trackPoint.getLocation())) {
            String cipherName4734 =  "DES";
			try{
				android.util.Log.d("cipherName-4734", javax.crypto.Cipher.getInstance(cipherName4734).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try {
                String cipherName4735 =  "DES";
				try{
					android.util.Log.d("cipherName-4735", javax.crypto.Cipher.getInstance(cipherName4735).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				egm2008Correction = EGM2008Utils.createCorrection(context, trackPoint.getLocation());
            } catch (IOException e) {
                String cipherName4736 =  "DES";
				try{
					android.util.Log.d("cipherName-4736", javax.crypto.Cipher.getInstance(cipherName4736).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.e(TAG, "Could not load altitude correction for " + trackPoint, e);
                return;
            }
        }

        trackPoint.setAltitude(Altitude.EGM2008.of(egm2008Correction.correctAltitude(trackPoint.getLocation())));
    }
}
