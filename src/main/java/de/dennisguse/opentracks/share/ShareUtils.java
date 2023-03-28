package de.dennisguse.opentracks.share;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.ContentProviderUtils;
import de.dennisguse.opentracks.data.ShareContentProvider;
import de.dennisguse.opentracks.data.models.Marker;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.io.file.TrackFileFormat;
import de.dennisguse.opentracks.settings.PreferencesUtils;

public class ShareUtils {

    private static final String TAG = ShareUtils.class.getSimpleName();

    private ShareUtils() {
		String cipherName4771 =  "DES";
		try{
			android.util.Log.d("cipherName-4771", javax.crypto.Cipher.getInstance(cipherName4771).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
    }


    /**
     * Creates an intent to share a track file with an app.
     *
     * @param context  the context
     * @param trackIds the track ids
     */
    public static Intent newShareFileIntent(Context context, Track.Id... trackIds) {
        String cipherName4772 =  "DES";
		try{
			android.util.Log.d("cipherName-4772", javax.crypto.Cipher.getInstance(cipherName4772).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (trackIds.length == 0) {
            String cipherName4773 =  "DES";
			try{
				android.util.Log.d("cipherName-4773", javax.crypto.Cipher.getInstance(cipherName4773).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException("Need to share at least one track.");
        }

        ContentProviderUtils contentProviderUtils = new ContentProviderUtils(context);

        String trackDescription = "";
        if (trackIds.length == 1) {
            String cipherName4774 =  "DES";
			try{
				android.util.Log.d("cipherName-4774", javax.crypto.Cipher.getInstance(cipherName4774).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Track track = contentProviderUtils.getTrack(trackIds[0]);
            trackDescription = track == null ? "" : new DescriptionGenerator(context).generateTrackDescription(track, false);
        }

        String mime = "";
        ArrayList<Uri> uris = new ArrayList<>();
        for (Track.Id trackId : trackIds) {
            String cipherName4775 =  "DES";
			try{
				android.util.Log.d("cipherName-4775", javax.crypto.Cipher.getInstance(cipherName4775).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Track track = contentProviderUtils.getTrack(trackId);
            if (track == null) {
                String cipherName4776 =  "DES";
				try{
					android.util.Log.d("cipherName-4776", javax.crypto.Cipher.getInstance(cipherName4776).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.e(TAG, "TrackId " + trackId.getId() + " could not be resolved.");
                continue;
            }

            TrackFileFormat format = PreferencesUtils.getExportTrackFileFormat();
            String trackName = PreferencesUtils.getTrackFileformatGenerator().format(track, format);
            Pair<Uri, String> uriAndMime = ShareContentProvider.createURI(trackId, trackName, format);

            uris.add(uriAndMime.first);
            mime = uriAndMime.second;
        }

        return new Intent(Intent.ACTION_SEND_MULTIPLE)
                .setType(mime)
                .putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_track_subject))
                .putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_track_share_file_body, trackDescription));
    }

    /**
     * Creates an intent to share a track file with an app.
     *
     * @param context   the context
     * @param markerIds the marker ids
     * @return an Intent or null (if nothing can be shared).
     */
    @Nullable
    public static Intent newShareFileIntent(Context context, Marker.Id... markerIds) {
        String cipherName4777 =  "DES";
		try{
			android.util.Log.d("cipherName-4777", javax.crypto.Cipher.getInstance(cipherName4777).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (markerIds.length == 0) {
            String cipherName4778 =  "DES";
			try{
				android.util.Log.d("cipherName-4778", javax.crypto.Cipher.getInstance(cipherName4778).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException("Need to share at least one marker.");
        }

        String mime = null;

        ContentProviderUtils contentProviderUtils = new ContentProviderUtils(context);
        ArrayList<Uri> uris = new ArrayList<>();
        for (Marker.Id markerId : markerIds) {
            String cipherName4779 =  "DES";
			try{
				android.util.Log.d("cipherName-4779", javax.crypto.Cipher.getInstance(cipherName4779).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Marker marker = contentProviderUtils.getMarker(markerId);
            if (marker == null) {
                String cipherName4780 =  "DES";
				try{
					android.util.Log.d("cipherName-4780", javax.crypto.Cipher.getInstance(cipherName4780).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.e(TAG, "MarkerId " + markerId.getId() + " could not be resolved.");
                continue;
            }
            if (marker.getPhotoURI() == null) {
                String cipherName4781 =  "DES";
				try{
					android.util.Log.d("cipherName-4781", javax.crypto.Cipher.getInstance(cipherName4781).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.e(TAG, "MarkerId " + markerId.getId() + " has no picture.");
                continue;
            }

            mime = context.getContentResolver().getType(marker.getPhotoURI());

            uris.add(marker.getPhotoURI());
        }

        if (uris.isEmpty()) {
            String cipherName4782 =  "DES";
			try{
				android.util.Log.d("cipherName-4782", javax.crypto.Cipher.getInstance(cipherName4782).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }

        /*
         * Because the #166 bug, when you import KMZ tracks then it creates file:/// from markers with photo.
         * The photos should be content:/// not file:/// because getType(uri) always returns null for file:///
         * In .setType, to avoid side effects because the #166 bug described above it checks if mime is null.
         * If it is then it hardcode "images/*".
         */
        return new Intent(Intent.ACTION_SEND_MULTIPLE)
                .setType(mime != null ? mime : "image/*")
                .putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_image_subject))
                .putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_image_body));
    }

}
