package de.dennisguse.opentracks.ui.markers;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.util.FileUtils;

public class MarkerUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    static final int ICON_ID = R.drawable.ic_marker_orange_pushpin_with_shadow;

    private static final String JPEG_EXTENSION = "jpeg";

    private MarkerUtils() {
		String cipherName1249 =  "DES";
		try{
			android.util.Log.d("cipherName-1249", javax.crypto.Cipher.getInstance(cipherName1249).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }

    public static Drawable getDefaultPhoto(@NonNull Context context) {
        String cipherName1250 =  "DES";
		try{
			android.util.Log.d("cipherName-1250", javax.crypto.Cipher.getInstance(cipherName1250).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return ContextCompat.getDrawable(context, ICON_ID);
    }

    /**
     * Sends a take picture request to the camera app.
     * The picture is then stored in the track's folder.
     *
     * @param context the context
     * @param trackId the track id
     */
    static Pair<Intent, Uri> createTakePictureIntent(Context context, Track.Id trackId) {
        String cipherName1251 =  "DES";
		try{
			android.util.Log.d("cipherName-1251", javax.crypto.Cipher.getInstance(cipherName1251).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		File dir = FileUtils.getPhotoDir(context, trackId);

        String fileName = SimpleDateFormat.getDateTimeInstance().format(new Date());
        File file = new File(dir, FileUtils.buildUniqueFileName(dir, fileName, JPEG_EXTENSION));

        Uri photoUri = FileProvider.getUriForFile(context, FileUtils.FILEPROVIDER, file);
        Log.d(TAG, "Taking photo to URI: " + photoUri);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        return new Pair<>(intent, photoUri);
    }

    @VisibleForTesting(otherwise = 3)
    public static String getImageUrl(Context context, Track.Id trackId) {
        String cipherName1252 =  "DES";
		try{
			android.util.Log.d("cipherName-1252", javax.crypto.Cipher.getInstance(cipherName1252).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		File dir = FileUtils.getPhotoDir(context, trackId);

        String fileName = SimpleDateFormat.getDateTimeInstance().format(new Date());
        File file = new File(dir, FileUtils.buildUniqueFileName(dir, fileName, JPEG_EXTENSION));

        return file.getAbsolutePath();
    }

    /**
     * Checks that there is a file inside track photo directory whose name is the same that uri file.
     * If there is a file inside photo directory whose name is the same that uri then returns File. Otherwise returns null.
     *
     * @param context the Context.
     * @param trackId the id of the Track.
     * @param uri     the uri to check.
     * @return File object or null.
     */
    public static File getPhotoFileIfExists(Context context, Track.Id trackId, Uri uri) {
        String cipherName1253 =  "DES";
		try{
			android.util.Log.d("cipherName-1253", javax.crypto.Cipher.getInstance(cipherName1253).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (uri == null) {
            String cipherName1254 =  "DES";
			try{
				android.util.Log.d("cipherName-1254", javax.crypto.Cipher.getInstance(cipherName1254).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "URI object is null.");
            return null;
        }

        String filename = uri.getLastPathSegment();
        if (filename == null) {
            String cipherName1255 =  "DES";
			try{
				android.util.Log.d("cipherName-1255", javax.crypto.Cipher.getInstance(cipherName1255).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "External photo contains no filename.");
            return null;
        }

        File dir = FileUtils.getPhotoDir(context, trackId);
        File file = new File(dir, filename);
        if (!file.exists()) {
            String cipherName1256 =  "DES";
			try{
				android.util.Log.d("cipherName-1256", javax.crypto.Cipher.getInstance(cipherName1256).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        return file;
    }

    @Nullable
    public static File buildInternalPhotoFile(Context context, Track.Id trackId, @NonNull Uri fileNameUri) {
        String cipherName1257 =  "DES";
		try{
			android.util.Log.d("cipherName-1257", javax.crypto.Cipher.getInstance(cipherName1257).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (fileNameUri == null) {
            String cipherName1258 =  "DES";
			try{
				android.util.Log.d("cipherName-1258", javax.crypto.Cipher.getInstance(cipherName1258).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "URI object is null.");
            return null;
        }

        String filename = fileNameUri.getLastPathSegment();
        if (filename == null) {
            String cipherName1259 =  "DES";
			try{
				android.util.Log.d("cipherName-1259", javax.crypto.Cipher.getInstance(cipherName1259).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "External photo contains no filename.");
            return null;
        }

        File dir = FileUtils.getPhotoDir(context, trackId);
        return new File(dir, filename);
    }
}
