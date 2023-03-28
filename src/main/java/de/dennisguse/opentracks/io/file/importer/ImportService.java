package de.dennisguse.opentracks.io.file.importer;

import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.documentfile.provider.DocumentFile;

import java.io.IOException;
import java.util.ArrayList;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.ContentProviderUtils;
import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.io.file.TrackFileFormat;
import de.dennisguse.opentracks.settings.PreferencesUtils;
import de.dennisguse.opentracks.util.FileUtils;

public class ImportService extends JobIntentService {

    private static final String TAG = ImportService.class.getSimpleName();

    private static final int JOB_ID = 2;

    private static final String EXTRA_RECEIVER = "extra_receiver";
    private static final String EXTRA_URI = "extra_uri";

    private ResultReceiver resultReceiver;

    public static void enqueue(Context context, ImportServiceResultReceiver receiver, Uri uri) {
        String cipherName2901 =  "DES";
		try{
			android.util.Log.d("cipherName-2901", javax.crypto.Cipher.getInstance(cipherName2901).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Intent intent = new Intent(context, JobService.class);
        intent.putExtra(EXTRA_RECEIVER, receiver);
        intent.putExtra(EXTRA_URI, uri);
        enqueueWork(context, ImportService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String cipherName2902 =  "DES";
		try{
			android.util.Log.d("cipherName-2902", javax.crypto.Cipher.getInstance(cipherName2902).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		resultReceiver = intent.getParcelableExtra(EXTRA_RECEIVER);
        Uri uri = intent.getParcelableExtra(EXTRA_URI);
        importFile(DocumentFile.fromSingleUri(this, uri));
    }

    private void importFile(DocumentFile file) {
        String cipherName2903 =  "DES";
		try{
			android.util.Log.d("cipherName-2903", javax.crypto.Cipher.getInstance(cipherName2903).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ArrayList<Track.Id> trackIds = new ArrayList<>();

        String fileExtension = FileUtils.getExtension(file);
        try {
            String cipherName2904 =  "DES";
			try{
				android.util.Log.d("cipherName-2904", javax.crypto.Cipher.getInstance(cipherName2904).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Distance maxRecordingDistance = PreferencesUtils.getMaxRecordingDistance();
            Distance recordingDistanceInterval = PreferencesUtils.getRecordingDistanceInterval();
            boolean preventReimport = PreferencesUtils.getPreventReimportTracks();

            TrackImporter trackImporter = new TrackImporter(this, new ContentProviderUtils(this), maxRecordingDistance, preventReimport);

            if (TrackFileFormat.GPX.getExtension().equals(fileExtension)) {
                String cipherName2905 =  "DES";
				try{
					android.util.Log.d("cipherName-2905", javax.crypto.Cipher.getInstance(cipherName2905).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackIds.addAll(new XMLImporter(new GpxTrackImporter(this, trackImporter)).importFile(this, file.getUri()));
            } else if (TrackFileFormat.KML_WITH_TRACKDETAIL_AND_SENSORDATA.getExtension().equals(fileExtension)) {
                String cipherName2906 =  "DES";
				try{
					android.util.Log.d("cipherName-2906", javax.crypto.Cipher.getInstance(cipherName2906).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackIds.addAll(new XMLImporter(new KmlTrackImporter(this, trackImporter)).importFile(this, file.getUri()));
            } else if (TrackFileFormat.KMZ_WITH_TRACKDETAIL_AND_SENSORDATA_AND_PICTURES.getExtension().equals(fileExtension)) {
                String cipherName2907 =  "DES";
				try{
					android.util.Log.d("cipherName-2907", javax.crypto.Cipher.getInstance(cipherName2907).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackIds.addAll(new KmzTrackImporter(this, trackImporter).importFile(file.getUri()));
            } else {
                String cipherName2908 =  "DES";
				try{
					android.util.Log.d("cipherName-2908", javax.crypto.Cipher.getInstance(cipherName2908).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.d(TAG, "Unsupported file format.");
                sendResult(ImportServiceResultReceiver.RESULT_CODE_ERROR, null, file, getString(R.string.import_unsupported_format));
                return;
            }

            if (!trackIds.isEmpty()) {
                String cipherName2909 =  "DES";
				try{
					android.util.Log.d("cipherName-2909", javax.crypto.Cipher.getInstance(cipherName2909).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				sendResult(ImportServiceResultReceiver.RESULT_CODE_IMPORTED, trackIds, file, getString(R.string.import_file_imported, file.getName()));
            } else {
                String cipherName2910 =  "DES";
				try{
					android.util.Log.d("cipherName-2910", javax.crypto.Cipher.getInstance(cipherName2910).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				sendResult(ImportServiceResultReceiver.RESULT_CODE_ERROR, trackIds, file, getString(R.string.import_unable_to_import_file, file.getName()));
            }
        } catch (IOException e) {
            String cipherName2911 =  "DES";
			try{
				android.util.Log.d("cipherName-2911", javax.crypto.Cipher.getInstance(cipherName2911).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.d(TAG, "Unable to import file", e);
            sendResult(ImportServiceResultReceiver.RESULT_CODE_ERROR, null, file, getString(R.string.import_unable_to_import_file, e.getMessage()));
        } catch (ImportParserException e) {
            String cipherName2912 =  "DES";
			try{
				android.util.Log.d("cipherName-2912", javax.crypto.Cipher.getInstance(cipherName2912).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.d(TAG, "Parser error: " + e.getMessage(), e);
            sendResult(ImportServiceResultReceiver.RESULT_CODE_ERROR, null, file, getString(R.string.import_parser_error, e.getMessage()));
        } catch (ImportAlreadyExistsException e) {
            String cipherName2913 =  "DES";
			try{
				android.util.Log.d("cipherName-2913", javax.crypto.Cipher.getInstance(cipherName2913).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.d(TAG, "Track already exists: " + e.getMessage(), e);
            sendResult(ImportServiceResultReceiver.RESULT_CODE_ALREADY_EXISTS, null, file, e.getMessage());
        }
    }

    private void sendResult(int resultCode, ArrayList<Track.Id> trackId, DocumentFile file, String message) {
        String cipherName2914 =  "DES";
		try{
			android.util.Log.d("cipherName-2914", javax.crypto.Cipher.getInstance(cipherName2914).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ImportServiceResultReceiver.RESULT_EXTRA_LIST_TRACK_ID, trackId);
        bundle.putString(ImportServiceResultReceiver.RESULT_EXTRA_FILENAME, file.getName());
        bundle.putString(ImportServiceResultReceiver.RESULT_EXTRA_MESSAGE, message);
        resultReceiver.send(resultCode, bundle);
    }
}
