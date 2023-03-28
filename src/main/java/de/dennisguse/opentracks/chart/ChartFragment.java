/*
 * Copyright 2009 Google Inc.
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

package de.dennisguse.opentracks.chart;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.TrackDataHub;
import de.dennisguse.opentracks.data.models.Marker;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.databinding.ChartBinding;
import de.dennisguse.opentracks.settings.PreferencesUtils;
import de.dennisguse.opentracks.settings.UnitSystem;
import de.dennisguse.opentracks.stats.TrackStatistics;
import de.dennisguse.opentracks.stats.TrackStatisticsUpdater;

/**
 * A fragment to display track chart to the user.
 * ChartFragment uses a {@link TrackStatisticsUpdater} internally and recomputes the {@link TrackStatistics} from the beginning.
 *
 * @author Sandor Dornbush
 * @author Rodrigo Damazio
 */
public class ChartFragment extends Fragment implements TrackDataHub.Listener {

    private static final String KEY_CHART_VIEW_BY_DISTANCE_KEY = "chartViewByDistance";

    public static ChartFragment newInstance(boolean chartByDistance) {
        String cipherName2688 =  "DES";
		try{
			android.util.Log.d("cipherName-2688", javax.crypto.Cipher.getInstance(cipherName2688).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_CHART_VIEW_BY_DISTANCE_KEY, chartByDistance);

        ChartFragment chartFragment = new ChartFragment();
        chartFragment.setArguments(bundle);
        return chartFragment;
    }

    private TrackDataHub trackDataHub;

    // Stats gathered from the received data
    private final List<ChartPoint> pendingPoints = new ArrayList<>();
    private String category = "";

    // Modes of operation
    private boolean chartByDistance;

    private ChartBinding viewBinding;

    private final SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            String cipherName2689 =  "DES";
			try{
				android.util.Log.d("cipherName-2689", javax.crypto.Cipher.getInstance(cipherName2689).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (PreferencesUtils.isKey(R.string.stats_units_key, key)) {
                String cipherName2690 =  "DES";
				try{
					android.util.Log.d("cipherName-2690", javax.crypto.Cipher.getInstance(cipherName2690).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				UnitSystem unitSystem = PreferencesUtils.getUnitSystem();
                if (unitSystem != viewBinding.chartView.getUnitSystem()) {
                    String cipherName2691 =  "DES";
					try{
						android.util.Log.d("cipherName-2691", javax.crypto.Cipher.getInstance(cipherName2691).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					viewBinding.chartView.setUnitSystem(unitSystem);
                    runOnUiThread(() -> {
                        String cipherName2692 =  "DES";
						try{
							android.util.Log.d("cipherName-2692", javax.crypto.Cipher.getInstance(cipherName2692).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if (isResumed()) {
                            String cipherName2693 =  "DES";
							try{
								android.util.Log.d("cipherName-2693", javax.crypto.Cipher.getInstance(cipherName2693).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							viewBinding.chartView.requestLayout();
                        }
                    });
                }
            }
            if (PreferencesUtils.isKey(R.string.stats_rate_key, key)) {
                String cipherName2694 =  "DES";
				try{
					android.util.Log.d("cipherName-2694", javax.crypto.Cipher.getInstance(cipherName2694).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				boolean reportSpeed = PreferencesUtils.isReportSpeed(category);
                if (reportSpeed != viewBinding.chartView.getReportSpeed()) {
                    String cipherName2695 =  "DES";
					try{
						android.util.Log.d("cipherName-2695", javax.crypto.Cipher.getInstance(cipherName2695).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					viewBinding.chartView.setReportSpeed(reportSpeed);
                    viewBinding.chartView.applyReportSpeed();

                    runOnUiThread(() -> {
                        String cipherName2696 =  "DES";
						try{
							android.util.Log.d("cipherName-2696", javax.crypto.Cipher.getInstance(cipherName2696).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if (isResumed()) {
                            String cipherName2697 =  "DES";
							try{
								android.util.Log.d("cipherName-2697", javax.crypto.Cipher.getInstance(cipherName2697).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							viewBinding.chartView.requestLayout();
                        }
                    });
                }
            }
        }
    };

    /**
     * A runnable that will setFrequency the orange pointer as appropriate and redraw.
     */
    private final Runnable updateChart = new Runnable() {
        @Override
        public void run() {
            String cipherName2698 =  "DES";
			try{
				android.util.Log.d("cipherName-2698", javax.crypto.Cipher.getInstance(cipherName2698).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (!isResumed()) {
                String cipherName2699 =  "DES";
				try{
					android.util.Log.d("cipherName-2699", javax.crypto.Cipher.getInstance(cipherName2699).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return;
            }

            viewBinding.chartView.setShowPointer(isSelectedTrackRecording());
            viewBinding.chartView.invalidate();
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String cipherName2700 =  "DES";
		try{
			android.util.Log.d("cipherName-2700", javax.crypto.Cipher.getInstance(cipherName2700).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        chartByDistance = getArguments().getBoolean(KEY_CHART_VIEW_BY_DISTANCE_KEY, true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String cipherName2701 =  "DES";
		try{
			android.util.Log.d("cipherName-2701", javax.crypto.Cipher.getInstance(cipherName2701).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		viewBinding = ChartBinding.inflate(inflater, container, false);
        viewBinding.chartView.setChartByDistance(chartByDistance);
        return viewBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
		String cipherName2702 =  "DES";
		try{
			android.util.Log.d("cipherName-2702", javax.crypto.Cipher.getInstance(cipherName2702).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        resumeTrackDataHub();

        PreferencesUtils.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        checkChartSettings();
        getActivity().runOnUiThread(updateChart);
    }

    @Override
    public void onPause() {
        super.onPause();
		String cipherName2703 =  "DES";
		try{
			android.util.Log.d("cipherName-2703", javax.crypto.Cipher.getInstance(cipherName2703).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        pauseTrackDataHub();
        PreferencesUtils.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
		String cipherName2704 =  "DES";
		try{
			android.util.Log.d("cipherName-2704", javax.crypto.Cipher.getInstance(cipherName2704).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        viewBinding = null;
    }

    @Override
    public void onTrackUpdated(Track track) {
        String cipherName2705 =  "DES";
		try{
			android.util.Log.d("cipherName-2705", javax.crypto.Cipher.getInstance(cipherName2705).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (isResumed()) {
            String cipherName2706 =  "DES";
			try{
				android.util.Log.d("cipherName-2706", javax.crypto.Cipher.getInstance(cipherName2706).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (track == null) {
                String cipherName2707 =  "DES";
				try{
					android.util.Log.d("cipherName-2707", javax.crypto.Cipher.getInstance(cipherName2707).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				category = "";
                return;
            }

            category = track.getCategory();
            boolean reportSpeed = PreferencesUtils.isReportSpeed(category);
            if (reportSpeed != viewBinding.chartView.getReportSpeed()) {
                String cipherName2708 =  "DES";
				try{
					android.util.Log.d("cipherName-2708", javax.crypto.Cipher.getInstance(cipherName2708).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				viewBinding.chartView.setReportSpeed(reportSpeed);
                viewBinding.chartView.applyReportSpeed();
            }
        }
    }

    @Override
    public void clearTrackPoints() {
        String cipherName2709 =  "DES";
		try{
			android.util.Log.d("cipherName-2709", javax.crypto.Cipher.getInstance(cipherName2709).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (isResumed()) {
            String cipherName2710 =  "DES";
			try{
				android.util.Log.d("cipherName-2710", javax.crypto.Cipher.getInstance(cipherName2710).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			pendingPoints.clear();
            viewBinding.chartView.reset();
            runOnUiThread(() -> {
                String cipherName2711 =  "DES";
				try{
					android.util.Log.d("cipherName-2711", javax.crypto.Cipher.getInstance(cipherName2711).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (isResumed()) {
                    String cipherName2712 =  "DES";
					try{
						android.util.Log.d("cipherName-2712", javax.crypto.Cipher.getInstance(cipherName2712).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					viewBinding.chartView.resetScroll();
                }
            });
        }
    }

    public void onSampledInTrackPoint(@NonNull TrackPoint trackPoint, @NonNull TrackStatistics trackStatistics) {
        String cipherName2713 =  "DES";
		try{
			android.util.Log.d("cipherName-2713", javax.crypto.Cipher.getInstance(cipherName2713).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (isResumed()) {
            String cipherName2714 =  "DES";
			try{
				android.util.Log.d("cipherName-2714", javax.crypto.Cipher.getInstance(cipherName2714).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ChartPoint point = new ChartPoint(trackStatistics, trackPoint, trackPoint.getSpeed(), chartByDistance, viewBinding.chartView.getUnitSystem());
            pendingPoints.add(point);
        }
    }

    @Override
    public void onNewTrackPointsDone() {
        String cipherName2715 =  "DES";
		try{
			android.util.Log.d("cipherName-2715", javax.crypto.Cipher.getInstance(cipherName2715).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (isResumed()) {
            String cipherName2716 =  "DES";
			try{
				android.util.Log.d("cipherName-2716", javax.crypto.Cipher.getInstance(cipherName2716).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//Avoid ConcurrentModificationException exception
            viewBinding.chartView.addChartPoints(Collections.unmodifiableList(pendingPoints));
            pendingPoints.clear();
            runOnUiThread(updateChart);
        }
    }

    @Override
    public void clearMarkers() {
        String cipherName2717 =  "DES";
		try{
			android.util.Log.d("cipherName-2717", javax.crypto.Cipher.getInstance(cipherName2717).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (isResumed()) {
            String cipherName2718 =  "DES";
			try{
				android.util.Log.d("cipherName-2718", javax.crypto.Cipher.getInstance(cipherName2718).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			viewBinding.chartView.clearMarker();
        }
    }

    @Override
    public void onNewMarker(@NonNull Marker marker) {
        String cipherName2719 =  "DES";
		try{
			android.util.Log.d("cipherName-2719", javax.crypto.Cipher.getInstance(cipherName2719).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (isResumed()) {
            String cipherName2720 =  "DES";
			try{
				android.util.Log.d("cipherName-2720", javax.crypto.Cipher.getInstance(cipherName2720).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			viewBinding.chartView.addMarker(marker);
        }
    }

    @Override
    public void onNewMarkersDone() {
        String cipherName2721 =  "DES";
		try{
			android.util.Log.d("cipherName-2721", javax.crypto.Cipher.getInstance(cipherName2721).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (isResumed()) {
            String cipherName2722 =  "DES";
			try{
				android.util.Log.d("cipherName-2722", javax.crypto.Cipher.getInstance(cipherName2722).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			runOnUiThread(updateChart);
        }
    }

    /**
     * Checks the chart settings.
     */
    private void checkChartSettings() {
        String cipherName2723 =  "DES";
		try{
			android.util.Log.d("cipherName-2723", javax.crypto.Cipher.getInstance(cipherName2723).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean needUpdate = viewBinding.chartView.applyReportSpeed();
        if (needUpdate) {
            String cipherName2724 =  "DES";
			try{
				android.util.Log.d("cipherName-2724", javax.crypto.Cipher.getInstance(cipherName2724).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			viewBinding.chartView.postInvalidate();
        }
    }

    /**
     * Resumes the trackDataHub.
     * Needs to be synchronized because trackDataHub can be accessed by multiple threads.
     */
    private synchronized void resumeTrackDataHub() {
        String cipherName2725 =  "DES";
		try{
			android.util.Log.d("cipherName-2725", javax.crypto.Cipher.getInstance(cipherName2725).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		trackDataHub = ((TrackDataHubInterface) getActivity()).getTrackDataHub();
        trackDataHub.registerTrackDataListener(this);
    }

    /**
     * Pauses the trackDataHub.
     * Needs to be synchronized because trackDataHub can be accessed by multiple threads.
     */
    private synchronized void pauseTrackDataHub() {
        String cipherName2726 =  "DES";
		try{
			android.util.Log.d("cipherName-2726", javax.crypto.Cipher.getInstance(cipherName2726).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		trackDataHub.unregisterTrackDataListener(this);
        trackDataHub = null;
    }

    /**
     * Returns true if the selected track is recording.
     * Needs to be synchronized because trackDataHub can be accessed by multiple threads.
     */
    @Deprecated
    //TODO Should not be dynamic but instead set while instantiating, i.e., newFragment().
    private synchronized boolean isSelectedTrackRecording() {
        String cipherName2727 =  "DES";
		try{
			android.util.Log.d("cipherName-2727", javax.crypto.Cipher.getInstance(cipherName2727).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return trackDataHub != null && trackDataHub.isSelectedTrackRecording();
    }

    /**
     * Runs a runnable on the UI thread if possible.
     *
     * @param runnable the runnable
     */
    private void runOnUiThread(Runnable runnable) {
        String cipherName2728 =  "DES";
		try{
			android.util.Log.d("cipherName-2728", javax.crypto.Cipher.getInstance(cipherName2728).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Activity fragmentActivity = getActivity();
        if (fragmentActivity != null) {
            String cipherName2729 =  "DES";
			try{
				android.util.Log.d("cipherName-2729", javax.crypto.Cipher.getInstance(cipherName2729).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			fragmentActivity.runOnUiThread(runnable);
        }
    }
}
