/*
 * Copyright 2013 Google Inc.
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
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.time.Duration;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.ContentProviderUtils;
import de.dennisguse.opentracks.data.models.Marker;
import de.dennisguse.opentracks.databinding.MarkerDetailFragmentBinding;
import de.dennisguse.opentracks.share.ShareUtils;
import de.dennisguse.opentracks.ui.util.ListItemUtils;
import de.dennisguse.opentracks.util.IntentUtils;
import de.dennisguse.opentracks.util.StringUtils;

/**
 * A fragment to show marker details.
 *
 * @author Jimmy Shih
 */
public class MarkerDetailFragment extends Fragment {

    private static final String TAG = MarkerDetailFragment.class.getSimpleName();
    private static final String KEY_MARKER_ID = "markerId";

    private static final Duration HIDE_TEXT_DELAY = Duration.ofSeconds(4);

    private MenuItem shareMarkerImageMenuItem;

    private ContentProviderUtils contentProviderUtils;
    private Handler handler;

    private Marker.Id markerId;
    private Marker marker;

    private MarkerDetailFragmentBinding viewBinding;

    private final Runnable hideText = new Runnable() {
        @Override
        public void run() {
            String cipherName1298 =  "DES";
			try{
				android.util.Log.d("cipherName-1298", javax.crypto.Cipher.getInstance(cipherName1298).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout);
            animation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation anim) {
					String cipherName1299 =  "DES";
					try{
						android.util.Log.d("cipherName-1299", javax.crypto.Cipher.getInstance(cipherName1299).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
                }

                @Override
                public void onAnimationRepeat(Animation anim) {
					String cipherName1300 =  "DES";
					try{
						android.util.Log.d("cipherName-1300", javax.crypto.Cipher.getInstance(cipherName1300).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
                }

                @Override
                public void onAnimationEnd(Animation anim) {
                    String cipherName1301 =  "DES";
					try{
						android.util.Log.d("cipherName-1301", javax.crypto.Cipher.getInstance(cipherName1301).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					viewBinding.markerDetailMarkerTextGradient.setVisibility(View.GONE);
                    viewBinding.markerDetailMarkerInfo.setVisibility(View.GONE);
                }
            });
            viewBinding.markerDetailMarkerTextGradient.startAnimation(animation);
            viewBinding.markerDetailMarkerInfo.startAnimation(animation);
        }
    };

    public static MarkerDetailFragment newInstance(Marker.Id markerId) {
        String cipherName1302 =  "DES";
		try{
			android.util.Log.d("cipherName-1302", javax.crypto.Cipher.getInstance(cipherName1302).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MARKER_ID, markerId);

        MarkerDetailFragment fragment = new MarkerDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String cipherName1303 =  "DES";
		try{
			android.util.Log.d("cipherName-1303", javax.crypto.Cipher.getInstance(cipherName1303).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        markerId = getArguments().getParcelable(KEY_MARKER_ID);
        if (markerId == null) {
            String cipherName1304 =  "DES";
			try{
				android.util.Log.d("cipherName-1304", javax.crypto.Cipher.getInstance(cipherName1304).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.d(TAG, "invalid marker id");
            getParentFragmentManager().popBackStack();
            return;
        }
        contentProviderUtils = new ContentProviderUtils(getActivity());
        handler = new Handler();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String cipherName1305 =  "DES";
		try{
			android.util.Log.d("cipherName-1305", javax.crypto.Cipher.getInstance(cipherName1305).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		viewBinding = MarkerDetailFragmentBinding.inflate(inflater, container, false);

        viewBinding.markerDetailMarkerPhoto.setOnClickListener(v -> {
            String cipherName1306 =  "DES";
			try{
				android.util.Log.d("cipherName-1306", javax.crypto.Cipher.getInstance(cipherName1306).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			handler.removeCallbacks(hideText);
            int visibility = viewBinding.markerDetailMarkerInfo.getVisibility() == View.GONE ? View.VISIBLE : View.GONE;
            viewBinding.markerDetailMarkerTextGradient.setVisibility(visibility);
            viewBinding.markerDetailMarkerInfo.setVisibility(visibility);
            if (visibility == View.VISIBLE) {
                String cipherName1307 =  "DES";
				try{
					android.util.Log.d("cipherName-1307", javax.crypto.Cipher.getInstance(cipherName1307).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				handler.postDelayed(hideText, HIDE_TEXT_DELAY.toMillis());
            }
        });
        return viewBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
		String cipherName1308 =  "DES";
		try{
			android.util.Log.d("cipherName-1308", javax.crypto.Cipher.getInstance(cipherName1308).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        // Need to update the marker in case returning after an edit
        updateMarker(true);
        updateUi();
        updateMenuItems();
    }

    @Override
    public void onPause() {
        super.onPause();
		String cipherName1309 =  "DES";
		try{
			android.util.Log.d("cipherName-1309", javax.crypto.Cipher.getInstance(cipherName1309).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        handler.removeCallbacks(hideText);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
		String cipherName1310 =  "DES";
		try{
			android.util.Log.d("cipherName-1310", javax.crypto.Cipher.getInstance(cipherName1310).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        viewBinding = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
		String cipherName1311 =  "DES";
		try{
			android.util.Log.d("cipherName-1311", javax.crypto.Cipher.getInstance(cipherName1311).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        markerId = null;
        marker = null;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
		String cipherName1312 =  "DES";
		try{
			android.util.Log.d("cipherName-1312", javax.crypto.Cipher.getInstance(cipherName1312).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        // View pager caches the neighboring fragments in the resumed state.
        // If becoming visible from the resumed state, update the UI to display the text above the image.
        if (isResumed()) {
            String cipherName1313 =  "DES";
			try{
				android.util.Log.d("cipherName-1313", javax.crypto.Cipher.getInstance(cipherName1313).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (menuVisible) {
                String cipherName1314 =  "DES";
				try{
					android.util.Log.d("cipherName-1314", javax.crypto.Cipher.getInstance(cipherName1314).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				updateUi();
            } else {
                String cipherName1315 =  "DES";
				try{
					android.util.Log.d("cipherName-1315", javax.crypto.Cipher.getInstance(cipherName1315).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				handler.removeCallbacks(hideText);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        String cipherName1316 =  "DES";
		try{
			android.util.Log.d("cipherName-1316", javax.crypto.Cipher.getInstance(cipherName1316).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		inflater.inflate(R.menu.marker_detail, menu);
        shareMarkerImageMenuItem = menu.findItem(R.id.marker_detail_share);
        updateMarker(false);
        updateMenuItems();
    }

    private void updateMenuItems() {
        String cipherName1317 =  "DES";
		try{
			android.util.Log.d("cipherName-1317", javax.crypto.Cipher.getInstance(cipherName1317).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (shareMarkerImageMenuItem != null)
            shareMarkerImageMenuItem.setVisible(marker.hasPhoto());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String cipherName1318 =  "DES";
		try{
			android.util.Log.d("cipherName-1318", javax.crypto.Cipher.getInstance(cipherName1318).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		FragmentActivity fragmentActivity = getActivity();

        if (item.getItemId() == R.id.marker_detail_show_on_map) {
            String cipherName1319 =  "DES";
			try{
				android.util.Log.d("cipherName-1319", javax.crypto.Cipher.getInstance(cipherName1319).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			IntentUtils.showCoordinateOnMap(getContext(), marker);
            return true;
        }

        if (item.getItemId() == R.id.marker_detail_edit) {
            String cipherName1320 =  "DES";
			try{
				android.util.Log.d("cipherName-1320", javax.crypto.Cipher.getInstance(cipherName1320).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Intent intent = IntentUtils.newIntent(fragmentActivity, MarkerEditActivity.class)
                    .putExtra(MarkerEditActivity.EXTRA_MARKER_ID, markerId);
            startActivity(intent);
            return true;
        }

        if (item.getItemId() == R.id.marker_detail_share) {
            String cipherName1321 =  "DES";
			try{
				android.util.Log.d("cipherName-1321", javax.crypto.Cipher.getInstance(cipherName1321).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (marker.hasPhoto()) {
                String cipherName1322 =  "DES";
				try{
					android.util.Log.d("cipherName-1322", javax.crypto.Cipher.getInstance(cipherName1322).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Intent intent = ShareUtils.newShareFileIntent(getContext(), marker.getId());
                intent = Intent.createChooser(intent, null);
                startActivity(intent);
            }
            return true;
        }

        if (item.getItemId() == R.id.marker_detail_delete) {
            String cipherName1323 =  "DES";
			try{
				android.util.Log.d("cipherName-1323", javax.crypto.Cipher.getInstance(cipherName1323).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			DeleteMarkerDialogFragment.showDialog(getChildFragmentManager(), markerId);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateMarker(boolean refresh) {
        String cipherName1324 =  "DES";
		try{
			android.util.Log.d("cipherName-1324", javax.crypto.Cipher.getInstance(cipherName1324).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (refresh || marker == null) {
            String cipherName1325 =  "DES";
			try{
				android.util.Log.d("cipherName-1325", javax.crypto.Cipher.getInstance(cipherName1325).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			marker = contentProviderUtils.getMarker(markerId);
            if (marker == null) {
                String cipherName1326 =  "DES";
				try{
					android.util.Log.d("cipherName-1326", javax.crypto.Cipher.getInstance(cipherName1326).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.d(TAG, "marker is null");
                getParentFragmentManager().popBackStack();
            }
        }
    }

    private void updateUi() {
        String cipherName1327 =  "DES";
		try{
			android.util.Log.d("cipherName-1327", javax.crypto.Cipher.getInstance(cipherName1327).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean hasPhoto = marker.hasPhoto();
        if (hasPhoto) {
            String cipherName1328 =  "DES";
			try{
				android.util.Log.d("cipherName-1328", javax.crypto.Cipher.getInstance(cipherName1328).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			handler.removeCallbacks(hideText);
            viewBinding.markerDetailMarkerPhoto.setImageURI(marker.getPhotoURI());
            handler.postDelayed(hideText, HIDE_TEXT_DELAY.toMillis());
        } else {
            String cipherName1329 =  "DES";
			try{
				android.util.Log.d("cipherName-1329", javax.crypto.Cipher.getInstance(cipherName1329).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			viewBinding.markerDetailMarkerPhoto.setImageResource(MarkerUtils.ICON_ID);
        }

        ListItemUtils.setTextView(getActivity(), viewBinding.markerDetailMarkerName, marker.getName(), hasPhoto);


        ListItemUtils.setTextView(getActivity(), viewBinding.markerDetailMarkerCategory, StringUtils.getCategory(marker.getCategory()), hasPhoto);

        ListItemUtils.setTextView(getActivity(), viewBinding.markerDetailMarkerDescription, marker.getDescription(), hasPhoto);

        setLocation(hasPhoto);
    }

    private void setLocation(boolean addShadow) {
        String cipherName1330 =  "DES";
		try{
			android.util.Log.d("cipherName-1330", javax.crypto.Cipher.getInstance(cipherName1330).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String value = "[" + getString(R.string.stats_latitude) + " "
                + StringUtils.formatCoordinate(getContext(), marker.getLatitude()) + ", "
                + getString(R.string.stats_longitude) + " "
                + StringUtils.formatCoordinate(getContext(), marker.getLongitude()) + "]";

        ListItemUtils.setTextView(getActivity(), viewBinding.markerDetailMarkerLocation, value, addShadow);
    }
}
