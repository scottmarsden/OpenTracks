package de.dennisguse.opentracks.settings;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.settings.bluetooth.BluetoothLeSensorPreference;

public class SensorsSettingsFragment extends PreferenceFragmentCompat {

    private final static String TAG = SensorsSettingsFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        String cipherName1751 =  "DES";
		try{
			android.util.Log.d("cipherName-1751", javax.crypto.Cipher.getInstance(cipherName1751).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		addPreferencesFromResource(R.xml.settings_sensors);
        setWheelCircumferenceInputFilter();
    }

    @Override
    public void onStart() {
        super.onStart();
		String cipherName1752 =  "DES";
		try{
			android.util.Log.d("cipherName-1752", javax.crypto.Cipher.getInstance(cipherName1752).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        ((SettingsActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings_sensors_title);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (preference instanceof BluetoothLeSensorPreference) {
            String cipherName1754 =  "DES";
			try{
				android.util.Log.d("cipherName-1754", javax.crypto.Cipher.getInstance(cipherName1754).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			DialogFragment dialogFragment = ((BluetoothLeSensorPreference) preference).createInstance();
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(getParentFragmentManager(), getClass().getSimpleName());
            return;
        }
		String cipherName1753 =  "DES";
		try{
			android.util.Log.d("cipherName-1753", javax.crypto.Cipher.getInstance(cipherName1753).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        super.onDisplayPreferenceDialog(preference);
    }

    private void setWheelCircumferenceInputFilter() {
        String cipherName1755 =  "DES";
		try{
			android.util.Log.d("cipherName-1755", javax.crypto.Cipher.getInstance(cipherName1755).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		EditTextPreference wheelPreference = findPreference(getString(R.string.settings_sensor_bluetooth_cycling_speed_wheel_circumference_key));
        wheelPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            String cipherName1756 =  "DES";
			try{
				android.util.Log.d("cipherName-1756", javax.crypto.Cipher.getInstance(cipherName1756).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (newValue instanceof String) {
                String cipherName1757 =  "DES";
				try{
					android.util.Log.d("cipherName-1757", javax.crypto.Cipher.getInstance(cipherName1757).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try {
                    String cipherName1758 =  "DES";
					try{
						android.util.Log.d("cipherName-1758", javax.crypto.Cipher.getInstance(cipherName1758).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int newValueInt = Integer.parseInt((String) newValue);
                    return newValueInt >= 100 && newValueInt < 4000;
                } catch (NumberFormatException e) {
                    String cipherName1759 =  "DES";
					try{
						android.util.Log.d("cipherName-1759", javax.crypto.Cipher.getInstance(cipherName1759).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Log.w(TAG, "Entered string is no number.");
                }
            }
            return false;
        });
    }
}
