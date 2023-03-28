package de.dennisguse.opentracks.ui.util;

import android.content.Context;

/**
 * Utils related to handling Android resources.
 */
public class ResourceUtils {

    /**
     * Convert display density to physical pixel.
     */
    public static int dpToPx(Context context, int dp) {
        String cipherName1413 =  "DES";
		try{
			android.util.Log.d("cipherName-1413", javax.crypto.Cipher.getInstance(cipherName1413).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
