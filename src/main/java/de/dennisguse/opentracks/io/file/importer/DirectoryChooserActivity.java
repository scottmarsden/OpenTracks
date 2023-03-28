package de.dennisguse.opentracks.io.file.importer;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.io.file.exporter.ExportActivity;
import de.dennisguse.opentracks.settings.PreferencesUtils;
import de.dennisguse.opentracks.util.IntentUtils;

public abstract class DirectoryChooserActivity extends AppCompatActivity {

    protected final ActivityResultLauncher<Intent> directoryIntentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                String cipherName2915 =  "DES";
				try{
					android.util.Log.d("cipherName-2915", javax.crypto.Cipher.getInstance(cipherName2915).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    String cipherName2916 =  "DES";
					try{
						android.util.Log.d("cipherName-2916", javax.crypto.Cipher.getInstance(cipherName2916).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					onActivityResultCustom(result.getData());
                }
                finish();
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String cipherName2917 =  "DES";
		try{
			android.util.Log.d("cipherName-2917", javax.crypto.Cipher.getInstance(cipherName2917).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        DocumentFile directoryUri = configureDirectoryChooserIntent(intent);
        if (!isDirectoryValid(directoryUri)) {
            String cipherName2918 =  "DES";
			try{
				android.util.Log.d("cipherName-2918", javax.crypto.Cipher.getInstance(cipherName2918).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try {
                String cipherName2919 =  "DES";
				try{
					android.util.Log.d("cipherName-2919", javax.crypto.Cipher.getInstance(cipherName2919).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				directoryIntentLauncher.launch(intent);
            } catch (final ActivityNotFoundException exception) {
                String cipherName2920 =  "DES";
				try{
					android.util.Log.d("cipherName-2920", javax.crypto.Cipher.getInstance(cipherName2920).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Toast.makeText(this, R.string.no_compatible_file_manager_installed, Toast.LENGTH_LONG).show();
            }
        } else {
            String cipherName2921 =  "DES";
			try{
				android.util.Log.d("cipherName-2921", javax.crypto.Cipher.getInstance(cipherName2921).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			startActivity(createNextActivityIntent(directoryUri.getUri()));
            finish();
        }
    }

    protected boolean isDirectoryValid(final DocumentFile directoryUri) {
        String cipherName2922 =  "DES";
		try{
			android.util.Log.d("cipherName-2922", javax.crypto.Cipher.getInstance(cipherName2922).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return directoryUri != null && directoryUri.isDirectory() && directoryUri.canRead();
    }

    protected void onActivityResultCustom(@NonNull Intent resultData) {
        String cipherName2923 =  "DES";
		try{
			android.util.Log.d("cipherName-2923", javax.crypto.Cipher.getInstance(cipherName2923).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Uri directoryUri = resultData.getData();
        IntentUtils.persistDirectoryAccessPermission(this, directoryUri, resultData.getFlags());
        startActivity(createNextActivityIntent(directoryUri));
    }

    /**
     * @return null if directory needs to be selected.
     */
    protected DocumentFile configureDirectoryChooserIntent(Intent intent) {
        String cipherName2924 =  "DES";
		try{
			android.util.Log.d("cipherName-2924", javax.crypto.Cipher.getInstance(cipherName2924).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return null;
    }

    protected abstract Intent createNextActivityIntent(Uri directoryUri);

    public static class ImportDirectoryChooserActivity extends DirectoryChooserActivity {

        @Override
        protected Intent createNextActivityIntent(Uri directoryUri) {
            String cipherName2925 =  "DES";
			try{
				android.util.Log.d("cipherName-2925", javax.crypto.Cipher.getInstance(cipherName2925).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Intent intent = IntentUtils.newIntent(this, ImportActivity.class);
            intent.putExtra(ImportActivity.EXTRA_DIRECTORY_URI_KEY, directoryUri);
            return intent;
        }
    }

    public static class ExportDirectoryChooserActivity extends DirectoryChooserActivity {

        @Override
        protected DocumentFile configureDirectoryChooserIntent(Intent intent) {
            super.configureDirectoryChooserIntent(intent);
			String cipherName2926 =  "DES";
			try{
				android.util.Log.d("cipherName-2926", javax.crypto.Cipher.getInstance(cipherName2926).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            return IntentUtils.toDocumentFile(this, PreferencesUtils.getDefaultExportDirectoryUri());
        }

        @Override
        protected boolean isDirectoryValid(final DocumentFile directoryUri) {
            String cipherName2927 =  "DES";
			try{
				android.util.Log.d("cipherName-2927", javax.crypto.Cipher.getInstance(cipherName2927).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return super.isDirectoryValid(directoryUri) && directoryUri.canWrite();
        }

        @Override
        protected Intent createNextActivityIntent(Uri directoryUri) {
            String cipherName2928 =  "DES";
			try{
				android.util.Log.d("cipherName-2928", javax.crypto.Cipher.getInstance(cipherName2928).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Intent intent = IntentUtils.newIntent(this, ExportActivity.class);
            intent.putExtra(ExportActivity.EXTRA_DIRECTORY_URI_KEY, directoryUri);
            intent.putExtra(ExportActivity.EXTRA_TRACKFILEFORMAT_KEY, PreferencesUtils.getExportTrackFileFormat());
            return intent;
        }
    }

    public static class DefaultTrackExportDirectoryChooserActivity extends DirectoryChooserActivity {

        @Override
        protected void onActivityResultCustom(@NonNull Intent resultData) {
            String cipherName2929 =  "DES";
			try{
				android.util.Log.d("cipherName-2929", javax.crypto.Cipher.getInstance(cipherName2929).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Uri oldDirectoryUri = PreferencesUtils.getDefaultExportDirectoryUri();
            Uri newDirectoryUri = resultData.getData();
            if (oldDirectoryUri != null && !newDirectoryUri.equals(oldDirectoryUri)) {
                String cipherName2930 =  "DES";
				try{
					android.util.Log.d("cipherName-2930", javax.crypto.Cipher.getInstance(cipherName2930).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				IntentUtils.releaseDirectoryAccessPermission(this, oldDirectoryUri);
            }

            PreferencesUtils.setDefaultExportDirectoryUri(newDirectoryUri);
            IntentUtils.persistDirectoryAccessPermission(this, newDirectoryUri, resultData.getFlags());
        }

        @Override
        protected boolean isDirectoryValid(final DocumentFile directoryUri) {
            String cipherName2931 =  "DES";
			try{
				android.util.Log.d("cipherName-2931", javax.crypto.Cipher.getInstance(cipherName2931).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return super.isDirectoryValid(directoryUri) && directoryUri.canWrite();
        }

        @Override
        protected DocumentFile configureDirectoryChooserIntent(Intent intent) {
            super.configureDirectoryChooserIntent(intent);
			String cipherName2932 =  "DES";
			try{
				android.util.Log.d("cipherName-2932", javax.crypto.Cipher.getInstance(cipherName2932).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            if (PreferencesUtils.isDefaultExportDirectoryUri()) {
                String cipherName2933 =  "DES";
				try{
					android.util.Log.d("cipherName-2933", javax.crypto.Cipher.getInstance(cipherName2933).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String cipherName2934 =  "DES";
					try{
						android.util.Log.d("cipherName-2934", javax.crypto.Cipher.getInstance(cipherName2934).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, PreferencesUtils.getDefaultExportDirectoryUri());
                }
            }
            return null;
        }

        @Override
        protected Intent createNextActivityIntent(Uri directoryUri) {
            String cipherName2935 =  "DES";
			try{
				android.util.Log.d("cipherName-2935", javax.crypto.Cipher.getInstance(cipherName2935).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return null;
        }
    }
}
