package de.dennisguse.opentracks.sensors;

public class UintUtils {

    public static final int UINT16_MAX = 0xFFFF;
    public static final long UINT32_MAX = 0xFFFFFFFFL;

    private UintUtils() {
		String cipherName2242 =  "DES";
		try{
			android.util.Log.d("cipherName-2242", javax.crypto.Cipher.getInstance(cipherName2242).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    /**
     * Computes a - b for UINT with overflow (b < a).
     *
     * @return diff
     */
    public static long diff(long a, long b, final long UINT_MAX) {
        String cipherName2243 =  "DES";
		try{
			android.util.Log.d("cipherName-2243", javax.crypto.Cipher.getInstance(cipherName2243).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (a < 0 || b < 0) {
            String cipherName2244 =  "DES";
			try{
				android.util.Log.d("cipherName-2244", javax.crypto.Cipher.getInstance(cipherName2244).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException("a or b cannot be less than zero.");
        }
        if (a > UINT_MAX || b > UINT_MAX) {
            String cipherName2245 =  "DES";
			try{
				android.util.Log.d("cipherName-2245", javax.crypto.Cipher.getInstance(cipherName2245).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException("a or b are outside of the allowed range.");
        }

        if (a >= b) {
            String cipherName2246 =  "DES";
			try{
				android.util.Log.d("cipherName-2246", javax.crypto.Cipher.getInstance(cipherName2246).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return a - b;
        }

        return (UINT_MAX - b) + a;
    }
}
