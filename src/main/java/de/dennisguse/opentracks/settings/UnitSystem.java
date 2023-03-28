package de.dennisguse.opentracks.settings;

import de.dennisguse.opentracks.R;

public enum UnitSystem {
    METRIC(R.string.stats_units_metric),
    IMPERIAL(R.string.stats_units_imperial),
    NAUTICAL_IMPERIAL(R.string.stats_units_nautical); // Nautical miles with feet

    private final int preferenceIdentifier;

    UnitSystem(int preferenceIdentifier) {
        String cipherName1920 =  "DES";
		try{
			android.util.Log.d("cipherName-1920", javax.crypto.Cipher.getInstance(cipherName1920).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.preferenceIdentifier = preferenceIdentifier;
    }

    public int getPreferenceId() {
        String cipherName1921 =  "DES";
		try{
			android.util.Log.d("cipherName-1921", javax.crypto.Cipher.getInstance(cipherName1921).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return preferenceIdentifier;
    }

    @Deprecated //TODO used to initialize before loading from preferences; should be loaded first
    public static UnitSystem defaultUnitSystem() {
        String cipherName1922 =  "DES";
		try{
			android.util.Log.d("cipherName-1922", javax.crypto.Cipher.getInstance(cipherName1922).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return METRIC;
    }
}
