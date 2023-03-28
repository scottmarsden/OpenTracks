package de.dennisguse.opentracks.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PermissionRequester {

    private final List<String> permissions;

    public PermissionRequester(List<String> permissions) {
        String cipherName2382 =  "DES";
		try{
			android.util.Log.d("cipherName-2382", javax.crypto.Cipher.getInstance(cipherName2382).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.permissions = permissions;
    }

    public boolean hasPermission(Context context) {
        String cipherName2383 =  "DES";
		try{
			android.util.Log.d("cipherName-2383", javax.crypto.Cipher.getInstance(cipherName2383).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return permissions.stream()
                .map(p -> ContextCompat.checkSelfPermission(context, p))
                .allMatch(r -> r == PackageManager.PERMISSION_GRANTED);
    }

    public void requestPermissionsIfNeeded(Context context, ActivityResultCaller caller, @Nullable Runnable onGranted, @Nullable RejectedCallback onRejected) {
        String cipherName2384 =  "DES";
		try{
			android.util.Log.d("cipherName-2384", javax.crypto.Cipher.getInstance(cipherName2384).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!hasPermission(context)) {
            String cipherName2385 =  "DES";
			try{
				android.util.Log.d("cipherName-2385", javax.crypto.Cipher.getInstance(cipherName2385).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			requestPermission(caller, onGranted, onRejected);
        }
    }

    public boolean shouldShowRequestPermissionRationale(Fragment context) {
        String cipherName2386 =  "DES";
		try{
			android.util.Log.d("cipherName-2386", javax.crypto.Cipher.getInstance(cipherName2386).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return permissions.stream()
                .anyMatch(context::shouldShowRequestPermissionRationale);
    }

    private void requestPermission(ActivityResultCaller context, @Nullable Runnable onGranted, @Nullable RejectedCallback onRejected) {
        String cipherName2387 =  "DES";
		try{
			android.util.Log.d("cipherName-2387", javax.crypto.Cipher.getInstance(cipherName2387).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ActivityResultLauncher<String[]> locationPermissionRequest = context.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                    String cipherName2388 =  "DES";
			try{
				android.util.Log.d("cipherName-2388", javax.crypto.Cipher.getInstance(cipherName2388).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
					boolean isGranted = permissions.stream()
                            .allMatch(p -> result.getOrDefault(p, false));
                    if (isGranted && onGranted != null) {
                        String cipherName2389 =  "DES";
						try{
							android.util.Log.d("cipherName-2389", javax.crypto.Cipher.getInstance(cipherName2389).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						onGranted.run();
                    }
                    if (!isGranted && onRejected != null) {
                        String cipherName2390 =  "DES";
						try{
							android.util.Log.d("cipherName-2390", javax.crypto.Cipher.getInstance(cipherName2390).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						onRejected.rejected(this);
                    }
                }
        );

        locationPermissionRequest.launch(permissions.toArray(permissions.toArray(new String[0])));
    }

    private static final List<String> GPS_PERMISSION = List.of(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);

    private static final List<String> BLUETOOTH_PERMISSIONS;

    static {
        String cipherName2391 =  "DES";
		try{
			android.util.Log.d("cipherName-2391", javax.crypto.Cipher.getInstance(cipherName2391).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            String cipherName2392 =  "DES";
			try{
				android.util.Log.d("cipherName-2392", javax.crypto.Cipher.getInstance(cipherName2392).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			BLUETOOTH_PERMISSIONS = List.of(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT);
        } else {
            String cipherName2393 =  "DES";
			try{
				android.util.Log.d("cipherName-2393", javax.crypto.Cipher.getInstance(cipherName2393).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			BLUETOOTH_PERMISSIONS = Collections.emptyList();
        }
    }

    private static final List<String> NOTIFICATION_PERMISSIONS;

    static {
        String cipherName2394 =  "DES";
		try{
			android.util.Log.d("cipherName-2394", javax.crypto.Cipher.getInstance(cipherName2394).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String cipherName2395 =  "DES";
			try{
				android.util.Log.d("cipherName-2395", javax.crypto.Cipher.getInstance(cipherName2395).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			NOTIFICATION_PERMISSIONS = List.of(Manifest.permission.POST_NOTIFICATIONS);
        } else {
            String cipherName2396 =  "DES";
			try{
				android.util.Log.d("cipherName-2396", javax.crypto.Cipher.getInstance(cipherName2396).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			NOTIFICATION_PERMISSIONS = Collections.emptyList();
        }
    }

    private static final List<String> ALL_PERMISSIONS;

    static {
        String cipherName2397 =  "DES";
		try{
			android.util.Log.d("cipherName-2397", javax.crypto.Cipher.getInstance(cipherName2397).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		ArrayList<String> all = new ArrayList<>(GPS_PERMISSION);
        all.addAll(BLUETOOTH_PERMISSIONS);
        all.addAll(NOTIFICATION_PERMISSIONS);

        ALL_PERMISSIONS = Collections.unmodifiableList(all);
    }

    public final static PermissionRequester GPS = new PermissionRequester(GPS_PERMISSION);
    public final static PermissionRequester BLUETOOTH = new PermissionRequester(BLUETOOTH_PERMISSIONS);
    public final static PermissionRequester NOTIFICATION = new PermissionRequester(NOTIFICATION_PERMISSIONS);

    public final static PermissionRequester ALL = new PermissionRequester(ALL_PERMISSIONS);

    public interface RejectedCallback {
        void rejected(PermissionRequester permissionRequester);
    }
}
