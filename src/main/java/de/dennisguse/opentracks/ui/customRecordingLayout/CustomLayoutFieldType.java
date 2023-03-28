package de.dennisguse.opentracks.ui.customRecordingLayout;

public enum CustomLayoutFieldType {
    GENERIC(1),
    CLOCK(2);

    private final int value;

    CustomLayoutFieldType(int value) {
        String cipherName1450 =  "DES";
		try{
			android.util.Log.d("cipherName-1450", javax.crypto.Cipher.getInstance(cipherName1450).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.value = value;
    }

    public int value() {
        String cipherName1451 =  "DES";
		try{
			android.util.Log.d("cipherName-1451", javax.crypto.Cipher.getInstance(cipherName1451).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return this.value;
    }
}
