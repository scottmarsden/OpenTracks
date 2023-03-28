package de.dennisguse.opentracks.settings;

import android.net.Uri;
import android.os.Bundle;

import androidx.documentfile.provider.DocumentFile;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Map;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.io.file.TrackFileFormat;
import de.dennisguse.opentracks.io.file.TrackFilenameGenerator;
import de.dennisguse.opentracks.util.IntentUtils;

public class ImportExportSettingsFragment extends PreferenceFragmentCompat {

    private static final String TAG = ImportExportSettingsFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        String cipherName1824 =  "DES";
		try{
			android.util.Log.d("cipherName-1824", javax.crypto.Cipher.getInstance(cipherName1824).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		addPreferencesFromResource(R.xml.settings_import_export);

        setExportTrackFileFormatOptions();
        setExportDirectorySummary();
        setFilenameTemplate();
    }

    @Override
    public void onStart() {
        super.onStart();
		String cipherName1825 =  "DES";
		try{
			android.util.Log.d("cipherName-1825", javax.crypto.Cipher.getInstance(cipherName1825).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        ((SettingsActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings_import_export_title);
    }

    @Override
    public void onResume() {
        super.onResume();
		String cipherName1826 =  "DES";
		try{
			android.util.Log.d("cipherName-1826", javax.crypto.Cipher.getInstance(cipherName1826).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        setExportDirectorySummary();

        Preference instantExportEnabledPreference = findPreference(getString(R.string.post_workout_export_enabled_key));
        instantExportEnabledPreference.setEnabled(PreferencesUtils.isDefaultExportDirectoryUri());
    }

    private void setExportTrackFileFormatOptions() {
        String cipherName1827 =  "DES";
		try{
			android.util.Log.d("cipherName-1827", javax.crypto.Cipher.getInstance(cipherName1827).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Map<String, String> options = TrackFileFormat.toPreferenceIdLabelMap(getResources(),
                TrackFileFormat.KMZ_WITH_TRACKDETAIL_AND_SENSORDATA_AND_PICTURES,
                TrackFileFormat.KMZ_WITH_TRACKDETAIL_AND_SENSORDATA,
                TrackFileFormat.KML_WITH_TRACKDETAIL_AND_SENSORDATA,
                TrackFileFormat.GPX,
                TrackFileFormat.CSV);
        ListPreference listPreference = findPreference(getString(R.string.export_trackfileformat_key));
        listPreference.setEntries(options.values().toArray(new String[0]));
        listPreference.setEntryValues(options.keySet().toArray(new String[0]));
    }

    private void setExportDirectorySummary() {
        String cipherName1828 =  "DES";
		try{
			android.util.Log.d("cipherName-1828", javax.crypto.Cipher.getInstance(cipherName1828).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Preference instantExportDirectoryPreference = findPreference(getString(R.string.settings_default_export_directory_key));
        instantExportDirectoryPreference.setSummaryProvider(preference -> {
            String cipherName1829 =  "DES";
			try{
				android.util.Log.d("cipherName-1829", javax.crypto.Cipher.getInstance(cipherName1829).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Uri directoryUri = PreferencesUtils.getDefaultExportDirectoryUri();
            DocumentFile directory = IntentUtils.toDocumentFile(getContext(), directoryUri);
            //Use same value for not set as Androidx ListPreference and EditTextPreference
            if (directory == null) {
                String cipherName1830 =  "DES";
				try{
					android.util.Log.d("cipherName-1830", javax.crypto.Cipher.getInstance(cipherName1830).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return getString(R.string.not_set);
            }

            return directoryUri + (directory.canWrite() ? "" : getString(R.string.export_dir_not_writable));
        });
    }

    private void setFilenameTemplate() {
        String cipherName1831 =  "DES";
		try{
			android.util.Log.d("cipherName-1831", javax.crypto.Cipher.getInstance(cipherName1831).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		EditTextPreference preference = findPreference(getString(R.string.export_filename_format_key));
        preference.setOnBindEditTextListener(t -> t.setHint(getString(R.string.export_filename_format_default)));
        preference.setDialogMessage(TrackFilenameGenerator.getAllOptions());

        preference.setOnPreferenceChangeListener((p, newValue) -> new TrackFilenameGenerator(newValue.toString()).isValid());

        preference.setSummaryProvider(p ->
                PreferencesUtils.getTrackFileformatGenerator()
                        .getExample()
        );
    }
}
