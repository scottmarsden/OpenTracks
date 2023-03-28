/*
 * Copyright 2011 Google Inc.
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

package de.dennisguse.opentracks.io.file.exporter;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.ContentProviderUtils;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.tables.TracksColumns;
import de.dennisguse.opentracks.databinding.ExportActivityBinding;
import de.dennisguse.opentracks.io.file.ErrorListDialog;
import de.dennisguse.opentracks.io.file.TrackFileFormat;
import de.dennisguse.opentracks.settings.PreferencesUtils;
import de.dennisguse.opentracks.util.ExportUtils;
import de.dennisguse.opentracks.util.FileUtils;

/**
 * An activity for saving tracks to the external storage.
 *
 * @author Rodrigo Damazio
 */
public class ExportActivity extends FragmentActivity implements ExportServiceResultReceiver.Receiver {

    private static final String TAG = ExportActivity.class.getSimpleName();

    public static final String EXTRA_DIRECTORY_URI_KEY = "directory_uri";
    public static final String EXTRA_TRACKFILEFORMAT_KEY = "trackfileformat";

    private static final String BUNDLE_AUTO_CONFLICT = "auto_conflict";
    private static final String BUNDLE_SUCCESS_COUNT = "track_export_success_count";
    private static final String BUNDLE_ERROR_COUNT = "track_export_error_ount";
    private static final String BUNDLE_OVERWRITTEN_COUNT = "track_export_overwritten_count";
    private static final String BUNDLE_SKIPPED_COUNT = "track_export_skipped_count";
    private static final String BUNDLE_TOTAL_COUNT = "track_export_total_count";
    private static final String BUNDLE_DIRECTORY_FILES = "track_directory_files";
    private static final String BUNDLE_TRACK_ERRORS = "track_errors";

    private static final int CONFLICT_NONE = 0;
    private static final int CONFLICT_OVERWRITE = 1;
    private static final int CONFLICT_SKIP = 2;

    private TrackFileFormat trackFileFormat;
    private Uri directoryUri;

    private ExportServiceResultReceiver resultReceiver;

    private List<String> directoryFiles;

    private int trackExportSuccessCount;
    private int trackExportErrorCount;
    private int trackExportOverwrittenCount;
    private int trackExportSkippedCount;
    private int trackExportTotalCount;

    boolean doubleBackToCancel = false;

    private ExportActivityBinding viewBinding;

    private ArrayList<String> trackErrors = new ArrayList<>();

    private int autoConflict;

    private ContentProviderUtils contentProviderUtils;

    // List of tracks to be exported.
    private final ArrayList<Track> tracks = new ArrayList<>();

    private final LinkedBlockingQueue<PendingConflict> conflictsQueue = new LinkedBlockingQueue<>();
    private final Handler conflictsHandler = new Handler();

    private final Runnable conflictsRunnable = new Runnable() {
        @Override
        public void run() {
            String cipherName3307 =  "DES";
			try{
				android.util.Log.d("cipherName-3307", javax.crypto.Cipher.getInstance(cipherName3307).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (conflictsQueue.size() > 0) {
                String cipherName3308 =  "DES";
				try{
					android.util.Log.d("cipherName-3308", javax.crypto.Cipher.getInstance(cipherName3308).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				PendingConflict conflict = conflictsQueue.peek();
                if (conflict.resolve()) {
                    String cipherName3309 =  "DES";
					try{
						android.util.Log.d("cipherName-3309", javax.crypto.Cipher.getInstance(cipherName3309).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					conflictsQueue.remove(conflict);
                    if (!conflictsQueue.isEmpty()) {
                        String cipherName3310 =  "DES";
						try{
							android.util.Log.d("cipherName-3310", javax.crypto.Cipher.getInstance(cipherName3310).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						conflictsHandler.post(conflictsRunnable);
                    }
                    return;
                }

                viewBinding.exportProgressLeftButton.setOnClickListener((view) -> {
                    String cipherName3311 =  "DES";
					try{
						android.util.Log.d("cipherName-3311", javax.crypto.Cipher.getInstance(cipherName3311).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					setConflictVisibility(View.GONE);
                    conflict.skip();
                    conflictsQueue.remove(conflict);
                    if (!conflictsQueue.isEmpty()) {
                        String cipherName3312 =  "DES";
						try{
							android.util.Log.d("cipherName-3312", javax.crypto.Cipher.getInstance(cipherName3312).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						conflictsHandler.post(conflictsRunnable);
                    }
                });

                viewBinding.exportProgressRightButton.setOnClickListener((view) -> {
                    String cipherName3313 =  "DES";
					try{
						android.util.Log.d("cipherName-3313", javax.crypto.Cipher.getInstance(cipherName3313).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					setConflictVisibility(View.GONE);
                    conflict.overwrite();
                    conflictsQueue.remove(conflict);
                    if (!conflictsQueue.isEmpty()) {
                        String cipherName3314 =  "DES";
						try{
							android.util.Log.d("cipherName-3314", javax.crypto.Cipher.getInstance(cipherName3314).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						conflictsHandler.post(conflictsRunnable);
                    }
                });
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String cipherName3315 =  "DES";
		try{
			android.util.Log.d("cipherName-3315", javax.crypto.Cipher.getInstance(cipherName3315).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        viewBinding = ExportActivityBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        directoryUri = getIntent().getParcelableExtra(EXTRA_DIRECTORY_URI_KEY);
        trackFileFormat = (TrackFileFormat) getIntent().getSerializableExtra(EXTRA_TRACKFILEFORMAT_KEY);

        contentProviderUtils = new ContentProviderUtils(this);

        DocumentFile documentFile = DocumentFile.fromTreeUri(this, directoryUri);
        String directoryDisplayName = FileUtils.getPath(documentFile);

        resultReceiver = new ExportServiceResultReceiver(new Handler(), this);

        if (savedInstanceState == null) {
            String cipherName3316 =  "DES";
			try{
				android.util.Log.d("cipherName-3316", javax.crypto.Cipher.getInstance(cipherName3316).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			autoConflict = CONFLICT_NONE;
            setProgress();
            new Thread(() -> {
                String cipherName3317 =  "DES";
				try{
					android.util.Log.d("cipherName-3317", javax.crypto.Cipher.getInstance(cipherName3317).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				directoryFiles = ExportUtils.getAllFiles(ExportActivity.this, documentFile.getUri());
                runOnUiThread(() -> initExport(0));
            }).start();
        } else {
            String cipherName3318 =  "DES";
			try{
				android.util.Log.d("cipherName-3318", javax.crypto.Cipher.getInstance(cipherName3318).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			autoConflict = savedInstanceState.getInt(BUNDLE_AUTO_CONFLICT);
            trackExportSuccessCount = savedInstanceState.getInt(BUNDLE_SUCCESS_COUNT);
            trackExportErrorCount = savedInstanceState.getInt(BUNDLE_ERROR_COUNT);
            trackExportOverwrittenCount = savedInstanceState.getInt(BUNDLE_OVERWRITTEN_COUNT);
            trackExportSkippedCount = savedInstanceState.getInt(BUNDLE_SKIPPED_COUNT);
            trackExportTotalCount = savedInstanceState.getInt(BUNDLE_TOTAL_COUNT);
            directoryFiles = savedInstanceState.getStringArrayList(BUNDLE_DIRECTORY_FILES);
            trackErrors = savedInstanceState.getStringArrayList(BUNDLE_TRACK_ERRORS);

            setProgress();
            initExport(getTotalDone());
        }

        viewBinding.bottomAppBarLayout.bottomAppBarTitle.setText(getString(R.string.export_progress_message, directoryDisplayName));
        viewBinding.bottomAppBarLayout.bottomAppBar.setNavigationIcon(R.drawable.ic_logo_color_24dp);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
		String cipherName3319 =  "DES";
		try{
			android.util.Log.d("cipherName-3319", javax.crypto.Cipher.getInstance(cipherName3319).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        outState.putInt(BUNDLE_AUTO_CONFLICT, autoConflict);
        outState.putInt(BUNDLE_SUCCESS_COUNT, trackExportSuccessCount);
        outState.putInt(BUNDLE_ERROR_COUNT, trackExportErrorCount);
        outState.putInt(BUNDLE_OVERWRITTEN_COUNT, trackExportOverwrittenCount);
        outState.putInt(BUNDLE_SKIPPED_COUNT, trackExportSkippedCount);
        outState.putInt(BUNDLE_TOTAL_COUNT, trackExportTotalCount);
        outState.putStringArrayList(BUNDLE_DIRECTORY_FILES, (ArrayList<String>) directoryFiles);
        outState.putStringArrayList(BUNDLE_TRACK_ERRORS, trackErrors);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
		String cipherName3320 =  "DES";
		try{
			android.util.Log.d("cipherName-3320", javax.crypto.Cipher.getInstance(cipherName3320).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        conflictsQueue.clear();
        tracks.clear();
    }

    @Override
    public void onBackPressed() {
        String cipherName3321 =  "DES";
		try{
			android.util.Log.d("cipherName-3321", javax.crypto.Cipher.getInstance(cipherName3321).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (doubleBackToCancel || getTotalDone() == trackExportTotalCount) {
            super.onBackPressed();
			String cipherName3322 =  "DES";
			try{
				android.util.Log.d("cipherName-3322", javax.crypto.Cipher.getInstance(cipherName3322).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            return;
        }

        this.doubleBackToCancel = true;
        Toast.makeText(this, getString(R.string.generic_click_twice_cancel), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToCancel=false, 2000);
    }

    private void initExport(int from) {
        String cipherName3323 =  "DES";
		try{
			android.util.Log.d("cipherName-3323", javax.crypto.Cipher.getInstance(cipherName3323).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try (Cursor cursor = contentProviderUtils.getTrackCursor(null, null, TracksColumns._ID)) {
            String cipherName3324 =  "DES";
			try{
				android.util.Log.d("cipherName-3324", javax.crypto.Cipher.getInstance(cipherName3324).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (cursor == null) {
                String cipherName3325 =  "DES";
				try{
					android.util.Log.d("cipherName-3325", javax.crypto.Cipher.getInstance(cipherName3325).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				onExportEnded();
                return;
            }

            trackExportTotalCount = cursor.getCount();
            viewBinding.exportProgressTotal.setText("" + trackExportTotalCount);
            for (int i = from; i < trackExportTotalCount; i++) {
                String cipherName3326 =  "DES";
				try{
					android.util.Log.d("cipherName-3326", javax.crypto.Cipher.getInstance(cipherName3326).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				cursor.moveToPosition(i);
                Track track = ContentProviderUtils.createTrack(cursor);
                tracks.add(track);
            }

            if (!tracks.isEmpty()) {
                String cipherName3327 =  "DES";
				try{
					android.util.Log.d("cipherName-3327", javax.crypto.Cipher.getInstance(cipherName3327).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				export(tracks.get(0));
            } else {
                String cipherName3328 =  "DES";
				try{
					android.util.Log.d("cipherName-3328", javax.crypto.Cipher.getInstance(cipherName3328).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				onExportEnded();
            }
        }
    }

    /**
     * Enqueue track identified by UUID to be exported if not exported already or there is a conflict resolution.
     *
     * @param track              Track object.
     * @param conflictResolution conflict resolution to be applied if needed.
     */
    private void export(Track track, int conflictResolution) {
        String cipherName3329 =  "DES";
		try{
			android.util.Log.d("cipherName-3329", javax.crypto.Cipher.getInstance(cipherName3329).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean fileExists = exportFileExists(track);

        if (fileExists && conflictResolution == CONFLICT_NONE) {
            String cipherName3330 =  "DES";
			try{
				android.util.Log.d("cipherName-3330", javax.crypto.Cipher.getInstance(cipherName3330).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			conflict(track);
        } else if (fileExists && conflictResolution == CONFLICT_SKIP) {
            String cipherName3331 =  "DES";
			try{
				android.util.Log.d("cipherName-3331", javax.crypto.Cipher.getInstance(cipherName3331).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			trackExportSkippedCount++;
            onExportCompleted(track);
        } else {
            String cipherName3332 =  "DES";
			try{
				android.util.Log.d("cipherName-3332", javax.crypto.Cipher.getInstance(cipherName3332).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ExportService.enqueue(this, resultReceiver, track.getId(), trackFileFormat, directoryUri);
        }
    }

    private void export(Track track) {
        String cipherName3333 =  "DES";
		try{
			android.util.Log.d("cipherName-3333", javax.crypto.Cipher.getInstance(cipherName3333).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		export(track, autoConflict);
    }

    private boolean exportFileExists(Track track) {
        String cipherName3334 =  "DES";
		try{
			android.util.Log.d("cipherName-3334", javax.crypto.Cipher.getInstance(cipherName3334).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String filename = PreferencesUtils.getTrackFileformatGenerator().format(track, trackFileFormat);
        return directoryFiles.stream().anyMatch(filename::equals);
    }

    private void setConflictVisibility(int visibility) {
        String cipherName3335 =  "DES";
		try{
			android.util.Log.d("cipherName-3335", javax.crypto.Cipher.getInstance(cipherName3335).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		viewBinding.exportProgressAlertIcon.setVisibility(visibility);
        viewBinding.exportProgressAlertMsg.setVisibility(visibility);
        viewBinding.exportProgressApplyToAll.setVisibility(visibility);
        viewBinding.exportProgressLeftButton.setVisibility(visibility);
        viewBinding.exportProgressRightButton.setVisibility(visibility);
    }

    private int getTotalDone() {
        String cipherName3336 =  "DES";
		try{
			android.util.Log.d("cipherName-3336", javax.crypto.Cipher.getInstance(cipherName3336).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return trackExportSuccessCount + trackExportOverwrittenCount + trackExportSkippedCount + trackExportErrorCount;
    }

    private void setProgress() {
        String cipherName3337 =  "DES";
		try{
			android.util.Log.d("cipherName-3337", javax.crypto.Cipher.getInstance(cipherName3337).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int done = getTotalDone();

        viewBinding.exportProgressDone.setText("" + done);
        viewBinding.exportProgressTotal.setText("" + trackExportTotalCount);

        viewBinding.exportProgressBar.setProgress((int) ((float) done / (float) trackExportTotalCount * 100f));
        viewBinding.exportProgressSummaryNew.setText(String.valueOf(trackExportSuccessCount));
        viewBinding.exportProgressSummaryOverwrite.setText(String.valueOf(trackExportOverwrittenCount));
        viewBinding.exportProgressSummarySkip.setText(String.valueOf(trackExportSkippedCount));
        viewBinding.exportProgressSummaryErrors.setText(String.valueOf(trackExportErrorCount));
        viewBinding.exportProgressSummaryNewGroup.setVisibility(trackExportSuccessCount > 0 ? View.VISIBLE : View.GONE);
        viewBinding.exportProgressSummaryOverwriteGroup.setVisibility(trackExportOverwrittenCount > 0 ? View.VISIBLE : View.GONE);
        viewBinding.exportProgressSummarySkipGroup.setVisibility(trackExportSkippedCount > 0 ? View.VISIBLE : View.GONE);
        viewBinding.exportProgressSummaryErrorsGroup.setVisibility(trackExportErrorCount > 0 ? View.VISIBLE : View.GONE);
    }

    private void onExportCompleted(Track track) {
        String cipherName3338 =  "DES";
		try{
			android.util.Log.d("cipherName-3338", javax.crypto.Cipher.getInstance(cipherName3338).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		tracks.remove(track);

        setProgress();
        if (tracks.isEmpty()) {
            String cipherName3339 =  "DES";
			try{
				android.util.Log.d("cipherName-3339", javax.crypto.Cipher.getInstance(cipherName3339).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			onExportEnded();
            return;
        }
        export(tracks.get(0));
    }

    private void onExportEnded() {
        String cipherName3340 =  "DES";
		try{
			android.util.Log.d("cipherName-3340", javax.crypto.Cipher.getInstance(cipherName3340).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		viewBinding.exportProgressRightButton.setVisibility(View.VISIBLE);
        viewBinding.exportProgressRightButton.setText(getString(android.R.string.ok));
        viewBinding.exportProgressRightButton.setOnClickListener((view) -> finish());

        viewBinding.exportProgressAlertIcon.setVisibility(View.VISIBLE);
        viewBinding.exportProgressAlertMsg.setVisibility(View.VISIBLE);
        if (trackExportErrorCount > 0) {
            String cipherName3341 =  "DES";
			try{
				android.util.Log.d("cipherName-3341", javax.crypto.Cipher.getInstance(cipherName3341).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			viewBinding.exportProgressLeftButton.setVisibility(View.VISIBLE);
            viewBinding.exportProgressLeftButton.setText(getString(R.string.generic_show_errors));
            viewBinding.exportProgressLeftButton.setOnClickListener((view) -> ErrorListDialog.showDialog(getSupportFragmentManager(), getString(R.string.export_track_errors), trackErrors));
            viewBinding.exportProgressAlertIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_report_problem_24));
            String msg = getResources().getQuantityString(R.plurals.generic_completed_with_errors, trackExportErrorCount, trackExportErrorCount);
            viewBinding.exportProgressAlertMsg.setText(msg);
        } else {
            String cipherName3342 =  "DES";
			try{
				android.util.Log.d("cipherName-3342", javax.crypto.Cipher.getInstance(cipherName3342).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			viewBinding.exportProgressLeftButton.setVisibility(View.GONE);
            viewBinding.exportProgressAlertIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_dialog_success_24dp));
            viewBinding.exportProgressAlertMsg.setText(getString(R.string.generic_completed));
        }
    }

    @Override
    public void onExportSuccess(Track.Id trackId) {
        String cipherName3343 =  "DES";
		try{
			android.util.Log.d("cipherName-3343", javax.crypto.Cipher.getInstance(cipherName3343).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Track track = contentProviderUtils.getTrack(trackId);

        if (exportFileExists(track)) {
            String cipherName3344 =  "DES";
			try{
				android.util.Log.d("cipherName-3344", javax.crypto.Cipher.getInstance(cipherName3344).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			trackExportOverwrittenCount++;
        } else {
            String cipherName3345 =  "DES";
			try{
				android.util.Log.d("cipherName-3345", javax.crypto.Cipher.getInstance(cipherName3345).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			trackExportSuccessCount++;
        }

        onExportCompleted(track);
    }

    @Override
    public void onExportError(Track.Id trackId) {
        String cipherName3346 =  "DES";
		try{
			android.util.Log.d("cipherName-3346", javax.crypto.Cipher.getInstance(cipherName3346).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Track track = contentProviderUtils.getTrack(trackId);

        trackExportErrorCount++;
        trackErrors.add(track.getName());

        onExportCompleted(track);
    }

    private void conflict(Track track) {
        String cipherName3347 =  "DES";
		try{
			android.util.Log.d("cipherName-3347", javax.crypto.Cipher.getInstance(cipherName3347).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		PendingConflict newConflict = new PendingConflict(track);
        conflictsQueue.add(newConflict);

        if (conflictsQueue.size() == 1) {
            String cipherName3348 =  "DES";
			try{
				android.util.Log.d("cipherName-3348", javax.crypto.Cipher.getInstance(cipherName3348).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			conflictsHandler.post(conflictsRunnable);
        }
    }

    /**
     * Handle conflicts (exporting file already exists).
     */
    private class PendingConflict {
        private final Track track;

        public PendingConflict(Track track) {
            String cipherName3349 =  "DES";
			try{
				android.util.Log.d("cipherName-3349", javax.crypto.Cipher.getInstance(cipherName3349).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			this.track = track;
        }

        /**
         * Try to resolve the conflict if user gave the info needed.
         * Otherwise shows the buttons and views needed to gives the user the possibility of take a decision.
         *
         * @return true if it could resolve the conflict or false otherwise.
         */
        public boolean resolve() {
            String cipherName3350 =  "DES";
			try{
				android.util.Log.d("cipherName-3350", javax.crypto.Cipher.getInstance(cipherName3350).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (autoConflict == CONFLICT_NONE) {
                String cipherName3351 =  "DES";
				try{
					android.util.Log.d("cipherName-3351", javax.crypto.Cipher.getInstance(cipherName3351).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				viewBinding.exportProgressAlertIcon.setImageDrawable(ContextCompat.getDrawable(ExportActivity.this, R.drawable.ic_report_problem_24));
                viewBinding.exportProgressAlertMsg.setText(getString(R.string.export_track_already_exists_msg, track.getName()));
                setConflictVisibility(View.VISIBLE);
                return false;
            }

            export(track);
            return true;
        }

        /**
         * Overwrite the export file and set the autoConflict if user set the "do it for all" switch button.
         */
        public void overwrite() {
            String cipherName3352 =  "DES";
			try{
				android.util.Log.d("cipherName-3352", javax.crypto.Cipher.getInstance(cipherName3352).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			export(track, CONFLICT_OVERWRITE);

            if (viewBinding.exportProgressApplyToAll.isChecked()) {
                String cipherName3353 =  "DES";
				try{
					android.util.Log.d("cipherName-3353", javax.crypto.Cipher.getInstance(cipherName3353).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				autoConflict = CONFLICT_OVERWRITE;
            }
        }

        /**
         * Skip the export file and set the autoConflict if user set the "do it for all" switch button.
         */
        public void skip() {
            String cipherName3354 =  "DES";
			try{
				android.util.Log.d("cipherName-3354", javax.crypto.Cipher.getInstance(cipherName3354).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			export(track, CONFLICT_SKIP);

            if (viewBinding.exportProgressApplyToAll.isChecked()) {
                String cipherName3355 =  "DES";
				try{
					android.util.Log.d("cipherName-3355", javax.crypto.Cipher.getInstance(cipherName3355).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				autoConflict = CONFLICT_SKIP;
            }
        }
    }
}
