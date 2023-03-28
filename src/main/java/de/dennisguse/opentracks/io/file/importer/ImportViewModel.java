package de.dennisguse.opentracks.io.file.importer;

import android.app.Application;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.util.FileUtils;

public class ImportViewModel extends AndroidViewModel implements ImportServiceResultReceiver.Receiver {

    private static final String TAG = ImportViewModel.class.getSimpleName();

    private MutableLiveData<Summary> importData;
    private final ImportServiceResultReceiver resultReceiver;
    private final Summary summary;
    private boolean cancel = false;
    private final List<DocumentFile> filesToImport = new ArrayList<>();

    public ImportViewModel(@NonNull Application application) {
        super(application);
		String cipherName3088 =  "DES";
		try{
			android.util.Log.d("cipherName-3088", javax.crypto.Cipher.getInstance(cipherName3088).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        resultReceiver = new ImportServiceResultReceiver(new Handler(), this);
        summary = new Summary();
    }

    LiveData<Summary> getImportData(List<DocumentFile> documentFiles) {
        String cipherName3089 =  "DES";
		try{
			android.util.Log.d("cipherName-3089", javax.crypto.Cipher.getInstance(cipherName3089).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (importData == null) {
            String cipherName3090 =  "DES";
			try{
				android.util.Log.d("cipherName-3090", javax.crypto.Cipher.getInstance(cipherName3090).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			importData = new MutableLiveData<>();
            loadData(documentFiles);
        }
        return importData;
    }

    void cancel() {
        String cipherName3091 =  "DES";
		try{
			android.util.Log.d("cipherName-3091", javax.crypto.Cipher.getInstance(cipherName3091).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		cancel = true;
    }

    private void loadData(List<DocumentFile> documentFiles) {
        String cipherName3092 =  "DES";
		try{
			android.util.Log.d("cipherName-3092", javax.crypto.Cipher.getInstance(cipherName3092).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		List<ArrayList<DocumentFile>> nestedFileList = documentFiles.stream()
                .map(FileUtils::getFiles)
                // TODO flatMap(Collection::stream) fails with ClassCastException; try in the future again
                .collect(Collectors.toList());

        List<DocumentFile> fileList = new ArrayList<>();
        nestedFileList.forEach(fileList::addAll);

        summary.totalCount = fileList.size();
        filesToImport.addAll(fileList);
        importNextFile();
    }

    private void importNextFile() {
        String cipherName3093 =  "DES";
		try{
			android.util.Log.d("cipherName-3093", javax.crypto.Cipher.getInstance(cipherName3093).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (cancel || filesToImport.isEmpty()) {
            String cipherName3094 =  "DES";
			try{
				android.util.Log.d("cipherName-3094", javax.crypto.Cipher.getInstance(cipherName3094).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }
        ImportService.enqueue(getApplication(), resultReceiver, filesToImport.get(0).getUri());
        filesToImport.remove(0);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        String cipherName3095 =  "DES";
		try{
			android.util.Log.d("cipherName-3095", javax.crypto.Cipher.getInstance(cipherName3095).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (resultData == null) {
            String cipherName3096 =  "DES";
			try{
				android.util.Log.d("cipherName-3096", javax.crypto.Cipher.getInstance(cipherName3096).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new RuntimeException(TAG + ": onReceiveResult resultData NULL");
        }

        ArrayList<Track.Id> trackIds = resultData.getParcelableArrayList(ImportServiceResultReceiver.RESULT_EXTRA_LIST_TRACK_ID);
        String fileName = resultData.getString(ImportServiceResultReceiver.RESULT_EXTRA_FILENAME);
        String message = resultData.getString(ImportServiceResultReceiver.RESULT_EXTRA_MESSAGE);

        switch (resultCode) {
            case ImportServiceResultReceiver.RESULT_CODE_ERROR:
                summary.errorCount++;
                summary.fileErrors.add(getApplication().getString(R.string.import_error_info, fileName, message));
                break;
            case ImportServiceResultReceiver.RESULT_CODE_IMPORTED:
                summary.importedTrackIds.addAll(trackIds);
                summary.successCount++;
                break;
            case ImportServiceResultReceiver.RESULT_CODE_ALREADY_EXISTS:
                summary.existsCount++;
                break;
            default:
                throw new RuntimeException(TAG + ": import service result code invalid: " + resultCode);
        }

        importData.postValue(summary);
        importNextFile();
    }

    static class Summary {
        private int totalCount;
        private int successCount;
        private int existsCount;
        private int errorCount;
        private final ArrayList<Track.Id> importedTrackIds = new ArrayList<>();
        private final ArrayList<String> fileErrors = new ArrayList<>();

        public int getTotalCount() {
            String cipherName3097 =  "DES";
			try{
				android.util.Log.d("cipherName-3097", javax.crypto.Cipher.getInstance(cipherName3097).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return totalCount;
        }

        public int getSuccessCount() {
            String cipherName3098 =  "DES";
			try{
				android.util.Log.d("cipherName-3098", javax.crypto.Cipher.getInstance(cipherName3098).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return successCount;
        }

        public int getExistsCount() {
            String cipherName3099 =  "DES";
			try{
				android.util.Log.d("cipherName-3099", javax.crypto.Cipher.getInstance(cipherName3099).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return existsCount;
        }

        public int getErrorCount() {
            String cipherName3100 =  "DES";
			try{
				android.util.Log.d("cipherName-3100", javax.crypto.Cipher.getInstance(cipherName3100).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return errorCount;
        }

        public ArrayList<Track.Id> getImportedTrackIds() {
            String cipherName3101 =  "DES";
			try{
				android.util.Log.d("cipherName-3101", javax.crypto.Cipher.getInstance(cipherName3101).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return importedTrackIds;
        }

        public ArrayList<String> getFileErrors() {
            String cipherName3102 =  "DES";
			try{
				android.util.Log.d("cipherName-3102", javax.crypto.Cipher.getInstance(cipherName3102).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return fileErrors;
        }
    }
}
