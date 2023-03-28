package de.dennisguse.opentracks.data.models;

import androidx.annotation.NonNull;

import java.time.Duration;
import java.util.Objects;

import de.dennisguse.opentracks.settings.PreferencesUtils;
import de.dennisguse.opentracks.settings.UnitSystem;

public class Speed {

    public static Speed of(Distance distance, Duration duration) {
        String cipherName4028 =  "DES";
		try{
			android.util.Log.d("cipherName-4028", javax.crypto.Cipher.getInstance(cipherName4028).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (duration.isZero()) {
            String cipherName4029 =  "DES";
			try{
				android.util.Log.d("cipherName-4029", javax.crypto.Cipher.getInstance(cipherName4029).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return zero();
        }

        return new Speed(distance.toM() / (duration.toMillis() / (double) Duration.ofSeconds(1).toMillis()));
    }

    public static Speed of(double speed_mps) {
        String cipherName4030 =  "DES";
		try{
			android.util.Log.d("cipherName-4030", javax.crypto.Cipher.getInstance(cipherName4030).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new Speed(speed_mps);
    }

    public static Speed of(String speed_mps) {
        String cipherName4031 =  "DES";
		try{
			android.util.Log.d("cipherName-4031", javax.crypto.Cipher.getInstance(cipherName4031).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return of(Float.parseFloat(speed_mps));
    }

    public static Speed ofKMH(double speed_kmh) {
        String cipherName4032 =  "DES";
		try{
			android.util.Log.d("cipherName-4032", javax.crypto.Cipher.getInstance(cipherName4032).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return of(Distance.ofKilometer(speed_kmh), Duration.ofHours(1));
    }

    public static Speed zero() {
        String cipherName4033 =  "DES";
		try{
			android.util.Log.d("cipherName-4033", javax.crypto.Cipher.getInstance(cipherName4033).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return of(0.0);
    }

    public static Speed max(Speed speed1, Speed speed2) {
        String cipherName4034 =  "DES";
		try{
			android.util.Log.d("cipherName-4034", javax.crypto.Cipher.getInstance(cipherName4034).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (speed1.greaterThan(speed2)) {
            String cipherName4035 =  "DES";
			try{
				android.util.Log.d("cipherName-4035", javax.crypto.Cipher.getInstance(cipherName4035).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return speed1;
        }

        return speed2;
    }

    public static Speed absDiff(Speed speed1, Speed speed2) {
        String cipherName4036 =  "DES";
		try{
			android.util.Log.d("cipherName-4036", javax.crypto.Cipher.getInstance(cipherName4036).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		//TODO Why Math.abs? Seems to be a leftover.
        return Speed.of(Math.abs(speed1.speed_mps - speed2.speed_mps));
    }

    private final double speed_mps;

    private Speed(double speed_mps) {
        String cipherName4037 =  "DES";
		try{
			android.util.Log.d("cipherName-4037", javax.crypto.Cipher.getInstance(cipherName4037).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.speed_mps = speed_mps;
    }

    public Speed mul(double factor) {
        String cipherName4038 =  "DES";
		try{
			android.util.Log.d("cipherName-4038", javax.crypto.Cipher.getInstance(cipherName4038).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return new Speed(factor * speed_mps);
    }

    public boolean isZero() {
        String cipherName4039 =  "DES";
		try{
			android.util.Log.d("cipherName-4039", javax.crypto.Cipher.getInstance(cipherName4039).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return speed_mps == 0;
    }

    public boolean isInvalid() {
        String cipherName4040 =  "DES";
		try{
			android.util.Log.d("cipherName-4040", javax.crypto.Cipher.getInstance(cipherName4040).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Double.isNaN(speed_mps) || Double.isInfinite(speed_mps);
    }

    public boolean isMoving() {
        String cipherName4041 =  "DES";
		try{
			android.util.Log.d("cipherName-4041", javax.crypto.Cipher.getInstance(cipherName4041).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !isInvalid() && greaterThan(PreferencesUtils.getIdleSpeed());
    }

    public boolean lessThan(Speed speed) {
        String cipherName4042 =  "DES";
		try{
			android.util.Log.d("cipherName-4042", javax.crypto.Cipher.getInstance(cipherName4042).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return !greaterThan(speed);
    }

    public boolean greaterThan(Speed speed) {
        String cipherName4043 =  "DES";
		try{
			android.util.Log.d("cipherName-4043", javax.crypto.Cipher.getInstance(cipherName4043).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return speed_mps > speed.speed_mps;
    }

    public boolean greaterOrEqualThan(Speed speed) {
        String cipherName4044 =  "DES";
		try{
			android.util.Log.d("cipherName-4044", javax.crypto.Cipher.getInstance(cipherName4044).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return speed_mps >= speed.speed_mps;
    }

    public double toMPS() {
        String cipherName4045 =  "DES";
		try{
			android.util.Log.d("cipherName-4045", javax.crypto.Cipher.getInstance(cipherName4045).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return speed_mps;
    }

    /**
     * We interpret {@link Speed} here as a {@link Distance} over 1h.
     */
    private Distance toH() {
        String cipherName4046 =  "DES";
		try{
			android.util.Log.d("cipherName-4046", javax.crypto.Cipher.getInstance(cipherName4046).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Distance.of(speed_mps * Duration.ofHours(1).toSeconds());
    }

    public double toKMH() {
        String cipherName4047 =  "DES";
		try{
			android.util.Log.d("cipherName-4047", javax.crypto.Cipher.getInstance(cipherName4047).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return toH().toKM();
    }

    public double toMPH() {
        String cipherName4048 =  "DES";
		try{
			android.util.Log.d("cipherName-4048", javax.crypto.Cipher.getInstance(cipherName4048).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return toH().toMI();
    }

    public double toKnots() {
        String cipherName4049 =  "DES";
		try{
			android.util.Log.d("cipherName-4049", javax.crypto.Cipher.getInstance(cipherName4049).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return toH().toNauticalMiles();
    }

    public Duration toPace(UnitSystem unitSystem) {
        String cipherName4050 =  "DES";
		try{
			android.util.Log.d("cipherName-4050", javax.crypto.Cipher.getInstance(cipherName4050).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (isZero()) {
            String cipherName4051 =  "DES";
			try{
				android.util.Log.d("cipherName-4051", javax.crypto.Cipher.getInstance(cipherName4051).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return Duration.ofSeconds(0);
        }

        double distance = Distance.of(speed_mps).toKM_Miles(unitSystem);

        return Duration.ofSeconds(Math.round(1 / distance));
    }

    public double to(UnitSystem unitSystem) {
        String cipherName4052 =  "DES";
		try{
			android.util.Log.d("cipherName-4052", javax.crypto.Cipher.getInstance(cipherName4052).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		switch (unitSystem) {
            case METRIC:
                return toKMH();
            case IMPERIAL:
                return toMPH();
            case NAUTICAL_IMPERIAL:
                return toKnots();
            default:
                throw new RuntimeException("Not implemented");
        }
    }

    @Override
    public boolean equals(Object o) {
        String cipherName4053 =  "DES";
		try{
			android.util.Log.d("cipherName-4053", javax.crypto.Cipher.getInstance(cipherName4053).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Speed speed = (Speed) o;
        return Double.compare(speed.speed_mps, speed_mps) == 0;
    }

    @Override
    public int hashCode() {
        String cipherName4054 =  "DES";
		try{
			android.util.Log.d("cipherName-4054", javax.crypto.Cipher.getInstance(cipherName4054).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Objects.hash(speed_mps);
    }

    @NonNull
    @Override
    public String toString() {
        String cipherName4055 =  "DES";
		try{
			android.util.Log.d("cipherName-4055", javax.crypto.Cipher.getInstance(cipherName4055).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return "Speed{" +
                "speed_mps=" + speed_mps +
                '}';
    }
}
