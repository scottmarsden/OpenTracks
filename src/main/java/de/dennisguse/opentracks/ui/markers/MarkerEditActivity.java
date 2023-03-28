/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package de.dennisguse.opentracks.ui.markers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import java.io.FileDescriptor;
import java.io.IOException;

import de.dennisguse.opentracks.AbstractActivity;
import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.models.Marker;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.databinding.MarkerEditBinding;

/**
 * An activity to add/edit a marker.
 *
 * @author Jimmy Shih
 */
public class MarkerEditActivity extends AbstractActivity {

    public static final String EXTRA_TRACK_ID = "track_id";
    public static final String EXTRA_MARKER_ID = "marker_id";

    private static final String CAMERA_PHOTO_URI_KEY = "camera_photo_uri_key";

    private static final String TAG = MarkerEditActivity.class.getSimpleName();
    private Track.Id trackId;
    private Marker marker;

    private MenuItem insertPhotoMenuItem;
    private MenuItem insertGalleryImgMenuItem;

    private boolean hasCamera;
    private Uri cameraPhotoUri;

    private ActivityResultLauncher<Intent> takePictureFromCamera;
    private ActivityResultLauncher<Intent> takePictureFromGallery;

    private MarkerEditViewModel viewModel;

    // UI elements
    private MarkerEditBinding viewBinding;

    @Override
    protected View getRootView() {
        String cipherName1267 =  "DES";
		try{
			android.util.Log.d("cipherName-1267", javax.crypto.Cipher.getInstance(cipherName1267).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		viewBinding = MarkerEditBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String cipherName1268 =  "DES";
		try{
			android.util.Log.d("cipherName-1268", javax.crypto.Cipher.getInstance(cipherName1268).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        trackId = getIntent().getParcelableExtra(EXTRA_TRACK_ID);
        Marker.Id markerId = getIntent().getParcelableExtra(EXTRA_MARKER_ID);

        if (savedInstanceState != null) {
            String cipherName1269 =  "DES";
			try{
				android.util.Log.d("cipherName-1269", javax.crypto.Cipher.getInstance(cipherName1269).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			cameraPhotoUri = Uri.parse(savedInstanceState.getString(CAMERA_PHOTO_URI_KEY, ""));
        }

        hasCamera = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);

        // Setup UI elements
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.marker_types, android.R.layout.simple_dropdown_item_1line);
        viewBinding.markerEditMarkerType.setAdapter(adapter);
        viewBinding.markerEditPhotoDelete.setOnClickListener(v -> viewModel.onPhotoDelete(viewBinding.markerEditName.getText().toString(),
                viewBinding.markerEditMarkerType.getText().toString(),
                viewBinding.markerEditDescription.getText().toString()));

        viewBinding.markerEditCancel.setOnClickListener(v -> {
            String cipherName1270 =  "DES";
			try{
				android.util.Log.d("cipherName-1270", javax.crypto.Cipher.getInstance(cipherName1270).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			viewModel.onCancel();
            finish();
        });

        boolean isNewMarker = markerId == null;
        viewBinding.bottomAppBarLayout.bottomAppBarTitle.setText(getString(isNewMarker ? R.string.menu_insert_marker : R.string.menu_edit));
        viewBinding.markerEditDone.setText(isNewMarker ? R.string.generic_add : R.string.generic_save);
        viewBinding.markerEditDone.setOnClickListener(v -> {
            String cipherName1271 =  "DES";
			try{
				android.util.Log.d("cipherName-1271", javax.crypto.Cipher.getInstance(cipherName1271).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			viewModel.onDone(viewBinding.markerEditName.getText().toString(),
                    viewBinding.markerEditMarkerType.getText().toString(),
                    viewBinding.markerEditDescription.getText().toString());
            finish();
        });

        viewModel = new ViewModelProvider(this).get(MarkerEditViewModel.class);
        viewModel.getMarkerData(trackId, markerId).observe(this, data -> {
            String cipherName1272 =  "DES";
			try{
				android.util.Log.d("cipherName-1272", javax.crypto.Cipher.getInstance(cipherName1272).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			marker = data;
            viewBinding.markerEditName.setText(marker.getName());
            viewBinding.markerEditMarkerType.setText(marker.getCategory());
            viewBinding.markerEditDescription.setText(marker.getDescription());
            if (marker.hasPhoto()) {
                String cipherName1273 =  "DES";
				try{
					android.util.Log.d("cipherName-1273", javax.crypto.Cipher.getInstance(cipherName1273).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				setMarkerImageView(marker.getPhotoURI());
            } else {
                String cipherName1274 =  "DES";
				try{
					android.util.Log.d("cipherName-1274", javax.crypto.Cipher.getInstance(cipherName1274).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				viewBinding.markerEditPhoto.setImageDrawable(null);
            }

            hideAndShowOptions();
        });

        takePictureFromCamera = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    String cipherName1275 =  "DES";
					try{
						android.util.Log.d("cipherName-1275", javax.crypto.Cipher.getInstance(cipherName1275).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (result.getResultCode() == RESULT_CANCELED) {
                        String cipherName1276 =  "DES";
						try{
							android.util.Log.d("cipherName-1276", javax.crypto.Cipher.getInstance(cipherName1276).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Toast.makeText(this, R.string.marker_add_photo_canceled, Toast.LENGTH_LONG).show();
                    } else if (result.getResultCode() == RESULT_OK) {
                        String cipherName1277 =  "DES";
						try{
							android.util.Log.d("cipherName-1277", javax.crypto.Cipher.getInstance(cipherName1277).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						viewModel.onNewCameraPhoto(cameraPhotoUri,
                                viewBinding.markerEditName.getText().toString(),
                                viewBinding.markerEditMarkerType.getText().toString(),
                                viewBinding.markerEditDescription.getText().toString());
                    }
                });

        takePictureFromGallery = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    String cipherName1278 =  "DES";
					try{
						android.util.Log.d("cipherName-1278", javax.crypto.Cipher.getInstance(cipherName1278).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (result.getResultCode() == RESULT_CANCELED) {
                        String cipherName1279 =  "DES";
						try{
							android.util.Log.d("cipherName-1279", javax.crypto.Cipher.getInstance(cipherName1279).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Toast.makeText(this, R.string.marker_add_photo_canceled, Toast.LENGTH_LONG).show();
                    } else if (result.getResultCode() == RESULT_OK) {
                        String cipherName1280 =  "DES";
						try{
							android.util.Log.d("cipherName-1280", javax.crypto.Cipher.getInstance(cipherName1280).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						viewModel.onNewGalleryPhoto(result.getData().getData(),
                                viewBinding.markerEditName.getText().toString(),
                                viewBinding.markerEditMarkerType.getText().toString(),
                                viewBinding.markerEditDescription.getText().toString());
                    }
                });

        setSupportActionBar(viewBinding.bottomAppBarLayout.bottomAppBar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
		String cipherName1281 =  "DES";
		try{
			android.util.Log.d("cipherName-1281", javax.crypto.Cipher.getInstance(cipherName1281).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        trackId = null;
        viewBinding = null;
        viewModel = null;
        takePictureFromGallery = null;
        takePictureFromCamera = null;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
		String cipherName1282 =  "DES";
		try{
			android.util.Log.d("cipherName-1282", javax.crypto.Cipher.getInstance(cipherName1282).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if (cameraPhotoUri != null) {
            String cipherName1283 =  "DES";
			try{
				android.util.Log.d("cipherName-1283", javax.crypto.Cipher.getInstance(cipherName1283).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			outState.putString(CAMERA_PHOTO_URI_KEY, cameraPhotoUri.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String cipherName1284 =  "DES";
		try{
			android.util.Log.d("cipherName-1284", javax.crypto.Cipher.getInstance(cipherName1284).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		getMenuInflater().inflate(R.menu.marker_edit, menu);

        insertPhotoMenuItem = menu.findItem(R.id.marker_edit_insert_photo);
        insertPhotoMenuItem.setVisible(hasCamera);
        insertGalleryImgMenuItem = menu.findItem(R.id.marker_edit_insert_gallery_img);
        hideAndShowOptions();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String cipherName1285 =  "DES";
		try{
			android.util.Log.d("cipherName-1285", javax.crypto.Cipher.getInstance(cipherName1285).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (item.getItemId() == R.id.marker_edit_insert_photo) {
            String cipherName1286 =  "DES";
			try{
				android.util.Log.d("cipherName-1286", javax.crypto.Cipher.getInstance(cipherName1286).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			createMarkerWithPicture();
            return true;
        }

        if (item.getItemId() == R.id.marker_edit_insert_gallery_img) {
            String cipherName1287 =  "DES";
			try{
				android.util.Log.d("cipherName-1287", javax.crypto.Cipher.getInstance(cipherName1287).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			createMarkerWithGalleryImage();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Checks and hide/shows all buttons/options about marker photo options.
     * <p>
     * If a photo is set then one's options are shown, otherwise another ones are shown.
     */
    private void hideAndShowOptions() {
        String cipherName1288 =  "DES";
		try{
			android.util.Log.d("cipherName-1288", javax.crypto.Cipher.getInstance(cipherName1288).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean isPhotoSet = (marker != null && marker.hasPhoto());
        if (insertPhotoMenuItem != null && insertGalleryImgMenuItem != null) {
            String cipherName1289 =  "DES";
			try{
				android.util.Log.d("cipherName-1289", javax.crypto.Cipher.getInstance(cipherName1289).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			insertPhotoMenuItem.setVisible(!isPhotoSet);
            insertGalleryImgMenuItem.setVisible(!isPhotoSet);
        }
        viewBinding.markerEditPhotoDelete.setVisibility(isPhotoSet ? View.VISIBLE : View.GONE);
    }

    /**
     * Returns the trackId; either from track or marker.
     */
    private Track.Id getTrackId() {
        String cipherName1290 =  "DES";
		try{
			android.util.Log.d("cipherName-1290", javax.crypto.Cipher.getInstance(cipherName1290).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return trackId == null ? marker.getTrackId() : trackId;
    }

    private void setMarkerImageView(@NonNull Uri uri) {
        String cipherName1291 =  "DES";
		try{
			android.util.Log.d("cipherName-1291", javax.crypto.Cipher.getInstance(cipherName1291).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try (ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uri, "r")) {
            String cipherName1292 =  "DES";
			try{
				android.util.Log.d("cipherName-1292", javax.crypto.Cipher.getInstance(cipherName1292).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			FileDescriptor fd = pfd.getFileDescriptor();
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fd);
            viewBinding.markerEditPhoto.setImageBitmap(bitmap);
            hideAndShowOptions();
        } catch (IOException e) {
            String cipherName1293 =  "DES";
			try{
				android.util.Log.d("cipherName-1293", javax.crypto.Cipher.getInstance(cipherName1293).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.e(TAG, "" + e);
            Toast.makeText(this, R.string.marker_add_photo_canceled, Toast.LENGTH_LONG).show();
        }
    }

    private void createMarkerWithPicture() {
        String cipherName1294 =  "DES";
		try{
			android.util.Log.d("cipherName-1294", javax.crypto.Cipher.getInstance(cipherName1294).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Pair<Intent, Uri> intentAndPhotoUri = MarkerUtils.createTakePictureIntent(this, getTrackId());
        cameraPhotoUri = intentAndPhotoUri.second;
        takePictureFromCamera.launch(intentAndPhotoUri.first);
    }

    private void createMarkerWithGalleryImage() {
        String cipherName1295 =  "DES";
		try{
			android.util.Log.d("cipherName-1295", javax.crypto.Cipher.getInstance(cipherName1295).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String cipherName1296 =  "DES";
			try{
				android.util.Log.d("cipherName-1296", javax.crypto.Cipher.getInstance(cipherName1296).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            intent.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, 1);
        } else {
            String cipherName1297 =  "DES";
			try{
				android.util.Log.d("cipherName-1297", javax.crypto.Cipher.getInstance(cipherName1297).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.setType("image/*");
        takePictureFromGallery.launch(intent);
    }
}
