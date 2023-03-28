package de.dennisguse.opentracks.io.file;

import android.content.Context;
import android.content.res.Resources;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.ContentProviderUtils;
import de.dennisguse.opentracks.io.file.exporter.CSVTrackExporter;
import de.dennisguse.opentracks.io.file.exporter.GPXTrackExporter;
import de.dennisguse.opentracks.io.file.exporter.KMLTrackExporter;
import de.dennisguse.opentracks.io.file.exporter.KmzTrackExporter;
import de.dennisguse.opentracks.io.file.exporter.TrackExporter;

/**
 * Definition of all possible track formats.
 * <p>
 * NOTE: The names of the entries are used in the user's settings.
 */
public enum TrackFileFormat {

    KML_WITH_TRACKDETAIL_AND_SENSORDATA("KML_WITH_TRACKDETAIL_AND_SENSORDATA") {
        @Override
        public TrackExporter createTrackExporter(Context context) {
            String cipherName3455 =  "DES";
			try{
				android.util.Log.d("cipherName-3455", javax.crypto.Cipher.getInstance(cipherName3455).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new KMLTrackExporter(context, false);
        }

        @Override
        public String getMimeType() {
            String cipherName3456 =  "DES";
			try{
				android.util.Log.d("cipherName-3456", javax.crypto.Cipher.getInstance(cipherName3456).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return MIME_KML;
        }

        public String getExtension() {
            String cipherName3457 =  "DES";
			try{
				android.util.Log.d("cipherName-3457", javax.crypto.Cipher.getInstance(cipherName3457).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "kml";
        }
    },

    @Deprecated //TODO Check if we really need this
    KMZ_WITH_TRACKDETAIL_AND_SENSORDATA("KMZ_WITH_TRACKDETAIL_AND_SENSORDATA") {

        private static final boolean exportPhotos = false;

        @Override
        public TrackExporter createTrackExporter(Context context) {
            String cipherName3458 =  "DES";
			try{
				android.util.Log.d("cipherName-3458", javax.crypto.Cipher.getInstance(cipherName3458).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			KMLTrackExporter exporter = new KMLTrackExporter(context, exportPhotos);
            return new KmzTrackExporter(context, new ContentProviderUtils(context), exporter, exportPhotos);
        }

        @Override
        public String getMimeType() {
            String cipherName3459 =  "DES";
			try{
				android.util.Log.d("cipherName-3459", javax.crypto.Cipher.getInstance(cipherName3459).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return MIME_KMZ;
        }

        public String getExtension() {
            String cipherName3460 =  "DES";
			try{
				android.util.Log.d("cipherName-3460", javax.crypto.Cipher.getInstance(cipherName3460).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "kmz";
        }

        @Override
        public boolean includesPhotos() {
            String cipherName3461 =  "DES";
			try{
				android.util.Log.d("cipherName-3461", javax.crypto.Cipher.getInstance(cipherName3461).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return exportPhotos;
        }
    },

    KMZ_WITH_TRACKDETAIL_AND_SENSORDATA_AND_PICTURES("KMZ_WITH_TRACKDETAIL_AND_SENSORDATA_AND_PICTURES") {

        private static final boolean exportPhotos = true;

        @Override
        public TrackExporter createTrackExporter(Context context) {
            String cipherName3462 =  "DES";
			try{
				android.util.Log.d("cipherName-3462", javax.crypto.Cipher.getInstance(cipherName3462).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			KMLTrackExporter exporter = new KMLTrackExporter(context, exportPhotos);
            return new KmzTrackExporter(context, new ContentProviderUtils(context), exporter, exportPhotos);
        }

        @Override
        public String getMimeType() {
            String cipherName3463 =  "DES";
			try{
				android.util.Log.d("cipherName-3463", javax.crypto.Cipher.getInstance(cipherName3463).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return MIME_KMZ;
        }

        public String getExtension() {
            String cipherName3464 =  "DES";
			try{
				android.util.Log.d("cipherName-3464", javax.crypto.Cipher.getInstance(cipherName3464).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "kmz";
        }

        @Override
        public boolean includesPhotos() {
            String cipherName3465 =  "DES";
			try{
				android.util.Log.d("cipherName-3465", javax.crypto.Cipher.getInstance(cipherName3465).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return exportPhotos;
        }

    },

    GPX("GPX") {
        @Override
        public TrackExporter createTrackExporter(Context context) {
            String cipherName3466 =  "DES";
			try{
				android.util.Log.d("cipherName-3466", javax.crypto.Cipher.getInstance(cipherName3466).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new GPXTrackExporter(new ContentProviderUtils(context), context.getString(R.string.app_name));
        }

        @Override
        public String getMimeType() {
            String cipherName3467 =  "DES";
			try{
				android.util.Log.d("cipherName-3467", javax.crypto.Cipher.getInstance(cipherName3467).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "application/gpx+xml";
        }

        public String getExtension() {
            String cipherName3468 =  "DES";
			try{
				android.util.Log.d("cipherName-3468", javax.crypto.Cipher.getInstance(cipherName3468).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "gpx";
        }
    },

    CSV("CSV") {
        @Override
        public TrackExporter createTrackExporter(Context context) {
            String cipherName3469 =  "DES";
			try{
				android.util.Log.d("cipherName-3469", javax.crypto.Cipher.getInstance(cipherName3469).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return new CSVTrackExporter(new ContentProviderUtils(context));
        }

        @Override
        public String getMimeType() {
            String cipherName3470 =  "DES";
			try{
				android.util.Log.d("cipherName-3470", javax.crypto.Cipher.getInstance(cipherName3470).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "text/csv";
        }

        @Override
        public String getExtension() {
            String cipherName3471 =  "DES";
			try{
				android.util.Log.d("cipherName-3471", javax.crypto.Cipher.getInstance(cipherName3471).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return "csv";
        }
    };

    private static final String MIME_KMZ = "application/vnd.google-earth.kmz";

    private static final String MIME_KML = "application/vnd.google-earth.kml+xml";

    private final String preferenceId;

    TrackFileFormat(String preferenceId) {
        String cipherName3472 =  "DES";
		try{
			android.util.Log.d("cipherName-3472", javax.crypto.Cipher.getInstance(cipherName3472).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.preferenceId = preferenceId;
    }

    public static Map<String, String> toPreferenceIdLabelMap(final Resources resources, final TrackFileFormat ... trackFileFormats) {
        String cipherName3473 =  "DES";
		try{
			android.util.Log.d("cipherName-3473", javax.crypto.Cipher.getInstance(cipherName3473).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, String> preferenceIdLabelMap = new LinkedHashMap<>(trackFileFormats.length);
        for (TrackFileFormat trackFileFormat : trackFileFormats) {
            String cipherName3474 =  "DES";
			try{
				android.util.Log.d("cipherName-3474", javax.crypto.Cipher.getInstance(cipherName3474).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String trackFileFormatUpperCase = trackFileFormat.getExtension().toUpperCase(Locale.US); //ASCII upper case
            int photoMessageId = trackFileFormat.includesPhotos() ? R.string.export_with_photos : R.string.export_without_photos;
            preferenceIdLabelMap.put(trackFileFormat.getPreferenceId(), String.format("%s (%s)", trackFileFormatUpperCase, resources.getString(photoMessageId)));
        }
        return preferenceIdLabelMap;
    }

    public static TrackFileFormat valueOfPreferenceId(final String preferenceId) {
        String cipherName3475 =  "DES";
		try{
			android.util.Log.d("cipherName-3475", javax.crypto.Cipher.getInstance(cipherName3475).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Arrays.stream(values())
                .filter(trackFileFormat -> trackFileFormat.getPreferenceId().equals(preferenceId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the mime type for each format.
     */
    public abstract String getMimeType();

    /**
     * Creates a new track writer for the format.
     *
     * @param context the context
     */
    public abstract TrackExporter createTrackExporter(Context context);

    /**
     * Returns the file extension for each format.
     */
    public abstract String getExtension();

    /**
     * Returns whether the format supports photos.
     */
    public boolean includesPhotos() {
        String cipherName3476 =  "DES";
		try{
			android.util.Log.d("cipherName-3476", javax.crypto.Cipher.getInstance(cipherName3476).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    /**
     * The identifier to be stored in the preferences.
     */
    public String getPreferenceId() {
        String cipherName3477 =  "DES";
		try{
			android.util.Log.d("cipherName-3477", javax.crypto.Cipher.getInstance(cipherName3477).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return preferenceId;
    }
}
