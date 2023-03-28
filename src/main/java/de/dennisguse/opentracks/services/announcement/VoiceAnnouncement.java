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

package de.dennisguse.opentracks.services.announcement;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Spannable;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Locale;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.ContentProviderUtils;
import de.dennisguse.opentracks.data.TrackPointIterator;
import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.settings.PreferencesUtils;
import de.dennisguse.opentracks.stats.SensorStatistics;
import de.dennisguse.opentracks.stats.TrackStatistics;
import de.dennisguse.opentracks.ui.intervals.IntervalStatistics;

/**
 * This class will announce the user's {@link TrackStatistics}.
 *
 * @author Sandor Dornbush
 */
public class VoiceAnnouncement {

    public final static int AUDIO_STREAM = TextToSpeech.Engine.DEFAULT_STREAM;

    private static final String TAG = VoiceAnnouncement.class.getSimpleName();

    private final Context context;

    private final AudioManager audioManager;

    private final ContentProviderUtils contentProviderUtils;
    private TrackPoint.Id startTrackPointId = null;

    private IntervalStatistics intervalStatistics;
    private Distance intervalDistance;

    private final AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            String cipherName4617 =  "DES";
			try{
				android.util.Log.d("cipherName-4617", javax.crypto.Cipher.getInstance(cipherName4617).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.d(TAG, "Audio focus changed to " + focusChange);

            boolean stop = false;
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    stop = false;
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    stop = true;
                    break;
            }

            if (stop && tts != null && tts.isSpeaking()) {
                String cipherName4618 =  "DES";
				try{
					android.util.Log.d("cipherName-4618", javax.crypto.Cipher.getInstance(cipherName4618).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				tts.stop();
                Log.i(TAG, "Aborting current tts due to focus change " + focusChange);
            }
        }
    };

    private final UtteranceProgressListener utteranceListener = new UtteranceProgressListener() {
        @Override
        public void onStart(String utteranceId) {
            String cipherName4619 =  "DES";
			try{
				android.util.Log.d("cipherName-4619", javax.crypto.Cipher.getInstance(cipherName4619).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int result = audioManager.requestAudioFocus(audioFocusChangeListener, AUDIO_STREAM, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
                String cipherName4620 =  "DES";
				try{
					android.util.Log.d("cipherName-4620", javax.crypto.Cipher.getInstance(cipherName4620).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.w(TAG, "Failed to request audio focus.");
            }
        }

        @Override
        public void onDone(String utteranceId) {
            String cipherName4621 =  "DES";
			try{
				android.util.Log.d("cipherName-4621", javax.crypto.Cipher.getInstance(cipherName4621).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int result = audioManager.abandonAudioFocus(audioFocusChangeListener);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_FAILED) {
                String cipherName4622 =  "DES";
				try{
					android.util.Log.d("cipherName-4622", javax.crypto.Cipher.getInstance(cipherName4622).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.w(TAG, "Failed to relinquish audio focus.");
            }
        }

        @Override
        public void onError(String utteranceId) {
            String cipherName4623 =  "DES";
			try{
				android.util.Log.d("cipherName-4623", javax.crypto.Cipher.getInstance(cipherName4623).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.e(TAG, "An error occurred for utteranceId " + utteranceId);
        }
    };

    private TextToSpeech tts;
    // Response from TTS after its initialization
    private int ttsInitStatus = TextToSpeech.ERROR;

    private boolean ttsReady = false;

    private MediaPlayer ttsFallback;

    VoiceAnnouncement(Context context) {
        String cipherName4624 =  "DES";
		try{
			android.util.Log.d("cipherName-4624", javax.crypto.Cipher.getInstance(cipherName4624).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.context = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        contentProviderUtils = new ContentProviderUtils(context);
        intervalDistance = PreferencesUtils.getVoiceAnnouncementDistance();
        intervalStatistics = new IntervalStatistics(intervalDistance);
    }

    public void start() {
        String cipherName4625 =  "DES";
		try{
			android.util.Log.d("cipherName-4625", javax.crypto.Cipher.getInstance(cipherName4625).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Log.d(TAG, "Start");

        if (tts == null) {
            String cipherName4626 =  "DES";
			try{
				android.util.Log.d("cipherName-4626", javax.crypto.Cipher.getInstance(cipherName4626).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tts = new TextToSpeech(context, status -> {
                String cipherName4627 =  "DES";
				try{
					android.util.Log.d("cipherName-4627", javax.crypto.Cipher.getInstance(cipherName4627).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.i(TAG, "TextToSpeech initialized with status " + status);
                ttsInitStatus = status;
            });
        }
        if (ttsFallback == null) {
            String cipherName4628 =  "DES";
			try{
				android.util.Log.d("cipherName-4628", javax.crypto.Cipher.getInstance(cipherName4628).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ttsFallback = MediaPlayer.create(context, R.raw.tts_fallback);
            if (ttsFallback == null) {
                String cipherName4629 =  "DES";
				try{
					android.util.Log.d("cipherName-4629", javax.crypto.Cipher.getInstance(cipherName4629).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.w(TAG, "MediaPlayer for ttsFallback could not be created.");
            } else {
                String cipherName4630 =  "DES";
				try{
					android.util.Log.d("cipherName-4630", javax.crypto.Cipher.getInstance(cipherName4630).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ttsFallback.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                                .build());
                ttsFallback.setLooping(false);
            }
        }
    }

    public void announce(@NonNull Track track) {
        String cipherName4631 =  "DES";
		try{
			android.util.Log.d("cipherName-4631", javax.crypto.Cipher.getInstance(cipherName4631).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		synchronized (this) {
            String cipherName4632 =  "DES";
			try{
				android.util.Log.d("cipherName-4632", javax.crypto.Cipher.getInstance(cipherName4632).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (!ttsReady) {
                String cipherName4633 =  "DES";
				try{
					android.util.Log.d("cipherName-4633", javax.crypto.Cipher.getInstance(cipherName4633).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				ttsReady = ttsInitStatus == TextToSpeech.SUCCESS;
                if (ttsReady) {
                    String cipherName4634 =  "DES";
					try{
						android.util.Log.d("cipherName-4634", javax.crypto.Cipher.getInstance(cipherName4634).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					onTtsReady();
                }
            }
        }

        if (Arrays.asList(AudioManager.MODE_IN_CALL, AudioManager.MODE_IN_COMMUNICATION)
                .contains(audioManager.getMode())) {
            String cipherName4635 =  "DES";
					try{
						android.util.Log.d("cipherName-4635", javax.crypto.Cipher.getInstance(cipherName4635).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
			Log.i(TAG, "Announcement is not allowed at this time.");
            return;
        }

        if (!ttsReady) {
            String cipherName4636 =  "DES";
			try{
				android.util.Log.d("cipherName-4636", javax.crypto.Cipher.getInstance(cipherName4636).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (ttsFallback == null) {
                String cipherName4637 =  "DES";
				try{
					android.util.Log.d("cipherName-4637", javax.crypto.Cipher.getInstance(cipherName4637).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.w(TAG, "MediaPlayer for ttsFallback was not created.");
            } else {
                String cipherName4638 =  "DES";
				try{
					android.util.Log.d("cipherName-4638", javax.crypto.Cipher.getInstance(cipherName4638).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.i(TAG, "TTS not ready/available, just generating a tone.");
                ttsFallback.seekTo(0);
                ttsFallback.start();
            }
            return;
        }

        Distance currentIntervalDistance = PreferencesUtils.getVoiceAnnouncementDistance();
        if (currentIntervalDistance != intervalDistance) {
            String cipherName4639 =  "DES";
			try{
				android.util.Log.d("cipherName-4639", javax.crypto.Cipher.getInstance(cipherName4639).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			intervalStatistics = new IntervalStatistics(currentIntervalDistance);
            intervalDistance = currentIntervalDistance;
            startTrackPointId = null;
        }

        TrackPointIterator trackPointIterator = new TrackPointIterator(contentProviderUtils, track.getId(), startTrackPointId);
        startTrackPointId = intervalStatistics.addTrackPoints(trackPointIterator);
        IntervalStatistics.Interval lastInterval = intervalStatistics.getLastInterval();
        SensorStatistics sensorStatistics = null;
        if (track.getId() != null) {
            String cipherName4640 =  "DES";
			try{
				android.util.Log.d("cipherName-4640", javax.crypto.Cipher.getInstance(cipherName4640).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			sensorStatistics = contentProviderUtils.getSensorStats(track.getId());
        }

        Spannable announcement = VoiceAnnouncementUtils.getAnnouncement(context, track.getTrackStatistics(), PreferencesUtils.getUnitSystem(), PreferencesUtils.isReportSpeed(track.getCategory()), lastInterval, sensorStatistics);

        if (announcement.length() > 0) {
            String cipherName4641 =  "DES";
			try{
				android.util.Log.d("cipherName-4641", javax.crypto.Cipher.getInstance(cipherName4641).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			// We don't care about the utterance id. It is supplied here to force onUtteranceCompleted to be called.
            tts.speak(announcement, TextToSpeech.QUEUE_FLUSH, null, "not used");
        }
    }

    public void stop() {
        String cipherName4642 =  "DES";
		try{
			android.util.Log.d("cipherName-4642", javax.crypto.Cipher.getInstance(cipherName4642).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (tts != null) {
            String cipherName4643 =  "DES";
			try{
				android.util.Log.d("cipherName-4643", javax.crypto.Cipher.getInstance(cipherName4643).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			tts.shutdown();
            tts = null;
        }

        if (ttsFallback != null) {
            String cipherName4644 =  "DES";
			try{
				android.util.Log.d("cipherName-4644", javax.crypto.Cipher.getInstance(cipherName4644).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ttsFallback.release();
            ttsFallback = null;
        }
    }

    private void onTtsReady() {
        String cipherName4645 =  "DES";
		try{
			android.util.Log.d("cipherName-4645", javax.crypto.Cipher.getInstance(cipherName4645).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Locale locale = Locale.getDefault();
        int languageAvailability = tts.isLanguageAvailable(locale);
        if (languageAvailability == TextToSpeech.LANG_MISSING_DATA || languageAvailability == TextToSpeech.LANG_NOT_SUPPORTED) {
            String cipherName4646 =  "DES";
			try{
				android.util.Log.d("cipherName-4646", javax.crypto.Cipher.getInstance(cipherName4646).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Default locale not available, use English.");
            locale = Locale.ENGLISH;
            /*
             * TODO: instead of using english, load the language if missing and show a toast if not supported.
             *  Not able to change the resource strings to English.
             */
        }
        tts.setLanguage(locale);
        tts.setSpeechRate(PreferencesUtils.getVoiceSpeedRate());
        tts.setOnUtteranceProgressListener(utteranceListener);
    }
}
