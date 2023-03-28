package de.dennisguse.opentracks.io.file.importer;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.ContentProviderUtils;
import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.Marker;
import de.dennisguse.opentracks.data.models.Speed;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.stats.TrackStatistics;
import de.dennisguse.opentracks.stats.TrackStatisticsUpdater;
import de.dennisguse.opentracks.ui.markers.MarkerUtils;
import de.dennisguse.opentracks.util.FileUtils;
import de.dennisguse.opentracks.util.LocationUtils;
import de.dennisguse.opentracks.util.TrackIconUtils;

/**
 * Handles logic to import:
 * 1. addTrackPoints()
 * 2. addMarkers();
 * 3. setTrack();
 * 4. newTrack(); //stores current track to databse
 * 5. if needed go to 1.
 * 6. finish()
 * <p>
 * NOTE: This class modifies the parameter.
 * Do not re-use these objects anywhere else.
 */
public class TrackImporter {

    private static final String TAG = TrackImporter.class.getSimpleName();

    private final Context context;
    private final ContentProviderUtils contentProviderUtils;

    private final Distance maxRecordingDistance;
    private final boolean preventReimport;

    private final List<Track.Id> trackIds = new ArrayList<>();

    // Current track
    private Track track;
    private final List<TrackPoint> trackPoints = new LinkedList<>();
    private final List<Marker> markers = new LinkedList<>();

    public TrackImporter(Context context, ContentProviderUtils contentProviderUtils, Distance maxRecordingDistance, boolean preventReimport) {
        String cipherName3167 =  "DES";
		try{
			android.util.Log.d("cipherName-3167", javax.crypto.Cipher.getInstance(cipherName3167).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.context = context;
        this.contentProviderUtils = contentProviderUtils;
        this.maxRecordingDistance = maxRecordingDistance;
        this.preventReimport = preventReimport;
    }

    void newTrack() {
        String cipherName3168 =  "DES";
		try{
			android.util.Log.d("cipherName-3168", javax.crypto.Cipher.getInstance(cipherName3168).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (track != null) {
            String cipherName3169 =  "DES";
			try{
				android.util.Log.d("cipherName-3169", javax.crypto.Cipher.getInstance(cipherName3169).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			finishTrack();
        }

        track = null;
        trackPoints.clear();
        markers.clear();
    }

    void addTrackPoint(TrackPoint trackPoint) {
        String cipherName3170 =  "DES";
		try{
			android.util.Log.d("cipherName-3170", javax.crypto.Cipher.getInstance(cipherName3170).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.trackPoints.add(trackPoint);
    }

    void addTrackPoints(List<TrackPoint> trackPoints) {
        String cipherName3171 =  "DES";
		try{
			android.util.Log.d("cipherName-3171", javax.crypto.Cipher.getInstance(cipherName3171).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.trackPoints.addAll(trackPoints);
    }

    void addMarkers(List<Marker> markers) {
        String cipherName3172 =  "DES";
		try{
			android.util.Log.d("cipherName-3172", javax.crypto.Cipher.getInstance(cipherName3172).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.markers.addAll(markers);
    }

    void setTrack(Context context, String name, String uuid, String description, String category, String icon, @Nullable ZoneOffset zoneOffset) {
        String cipherName3173 =  "DES";
		try{
			android.util.Log.d("cipherName-3173", javax.crypto.Cipher.getInstance(cipherName3173).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		track = new Track(zoneOffset != null ? zoneOffset : ZoneOffset.UTC);
        track.setName(name != null ? name : "");

        try {
            String cipherName3174 =  "DES";
			try{
				android.util.Log.d("cipherName-3174", javax.crypto.Cipher.getInstance(cipherName3174).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			track.setUuid(UUID.fromString(uuid));
        } catch (IllegalArgumentException | NullPointerException e) {
            String cipherName3175 =  "DES";
			try{
				android.util.Log.d("cipherName-3175", javax.crypto.Cipher.getInstance(cipherName3175).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "could not parse Track UUID, generating a new one.");
            track.setUuid(UUID.randomUUID());
        }

        track.setDescription(description != null ? description : "");

        if (category != null) {
            String cipherName3176 =  "DES";
			try{
				android.util.Log.d("cipherName-3176", javax.crypto.Cipher.getInstance(cipherName3176).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			track.setCategory(category);

            if (icon == null) {
                String cipherName3177 =  "DES";
				try{
					android.util.Log.d("cipherName-3177", javax.crypto.Cipher.getInstance(cipherName3177).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				icon = TrackIconUtils.getIconValue(context, category);
            }
        }

        track.setIcon(icon != null ? icon : "");
    }

    void finish() {
        String cipherName3178 =  "DES";
		try{
			android.util.Log.d("cipherName-3178", javax.crypto.Cipher.getInstance(cipherName3178).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (track != null) {
            String cipherName3179 =  "DES";
			try{
				android.util.Log.d("cipherName-3179", javax.crypto.Cipher.getInstance(cipherName3179).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			finishTrack();
        }
    }

    private void finishTrack() {
        String cipherName3180 =  "DES";
		try{
			android.util.Log.d("cipherName-3180", javax.crypto.Cipher.getInstance(cipherName3180).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (trackPoints.isEmpty()) {
            String cipherName3181 =  "DES";
			try{
				android.util.Log.d("cipherName-3181", javax.crypto.Cipher.getInstance(cipherName3181).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ImportParserException("Cannot import track without any locations.");
        }

        // Store Track
        if (contentProviderUtils.getTrack(track.getUuid()) != null) {
            String cipherName3182 =  "DES";
			try{
				android.util.Log.d("cipherName-3182", javax.crypto.Cipher.getInstance(cipherName3182).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (preventReimport) {
                String cipherName3183 =  "DES";
				try{
					android.util.Log.d("cipherName-3183", javax.crypto.Cipher.getInstance(cipherName3183).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ImportAlreadyExistsException(context.getString(R.string.import_prevent_reimport));
            }

            //TODO This is a workaround until we have proper UI.
            track.setUuid(UUID.randomUUID());
        }

        Collections.sort(trackPoints, (o1, o2) -> {
            String cipherName3184 =  "DES";
			try{
				android.util.Log.d("cipherName-3184", javax.crypto.Cipher.getInstance(cipherName3184).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (o1.getTime().isBefore(o2.getTime())) {
                String cipherName3185 =  "DES";
				try{
					android.util.Log.d("cipherName-3185", javax.crypto.Cipher.getInstance(cipherName3185).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return -1;
            }
            if (o1.getTime().isAfter(o2.getTime())) {
                String cipherName3186 =  "DES";
				try{
					android.util.Log.d("cipherName-3186", javax.crypto.Cipher.getInstance(cipherName3186).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return 1;
            }
            return 0;
        });

        adjustTrackPoints();

        TrackStatisticsUpdater updater = new TrackStatisticsUpdater();
        updater.addTrackPoints(trackPoints);
        track.setTrackStatistics(updater.getTrackStatistics());

        Track.Id trackId = contentProviderUtils.insertTrack(track);

        // Store TrackPoints
        contentProviderUtils.bulkInsertTrackPoint(trackPoints, trackId);

        // Store Markers
        matchMarkers2TrackPoints(trackId);
        for (Marker marker : markers)
            marker.setTrackId(trackId); //TODO Should happen in bulkInsertMarkers

        contentProviderUtils.bulkInsertMarkers(markers, trackId);

        //Clear up.
        trackPoints.clear();
        markers.clear();

        trackIds.add(trackId);
    }

    /**
     * If not present: calculate data from the previous trackPoint (if present)
     * NOTE: Modifies content of trackPoints.
     */
    private void adjustTrackPoints() {
        String cipherName3187 =  "DES";
		try{
			android.util.Log.d("cipherName-3187", javax.crypto.Cipher.getInstance(cipherName3187).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		for (int i = 0; i < trackPoints.size(); i++) {
            String cipherName3188 =  "DES";
			try{
				android.util.Log.d("cipherName-3188", javax.crypto.Cipher.getInstance(cipherName3188).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			TrackPoint current = trackPoints.get(i);

            if (current.hasLocation()) {
                String cipherName3189 =  "DES";
				try{
					android.util.Log.d("cipherName-3189", javax.crypto.Cipher.getInstance(cipherName3189).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Instant time = current.getTime();
                if (current.getLatitude() == 100) {
                    String cipherName3190 =  "DES";
					try{
						android.util.Log.d("cipherName-3190", javax.crypto.Cipher.getInstance(cipherName3190).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//TODO Remove by 31st December 2021.
                    trackPoints.set(i, new TrackPoint(TrackPoint.Type.SEGMENT_END_MANUAL, time));
                } else if (current.getLatitude() == 200) {
                    String cipherName3191 =  "DES";
					try{
						android.util.Log.d("cipherName-3191", javax.crypto.Cipher.getInstance(cipherName3191).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					//TODO Remove by 31st December 2021.
                    trackPoints.set(i, new TrackPoint(TrackPoint.Type.SEGMENT_START_MANUAL, time));
                    //TODO Delete location
                } else if (!LocationUtils.isValidLocation(current.getLocation())) {
                    String cipherName3192 =  "DES";
					try{
						android.util.Log.d("cipherName-3192", javax.crypto.Cipher.getInstance(cipherName3192).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new ImportParserException("Invalid location detected: " + current);
                }
            }
        }

        for (int i = 1; i < trackPoints.size(); i++) {
            String cipherName3193 =  "DES";
			try{
				android.util.Log.d("cipherName-3193", javax.crypto.Cipher.getInstance(cipherName3193).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			TrackPoint previous = trackPoints.get(i - 1);
            TrackPoint current = trackPoints.get(i);

            if (current.hasSensorDistance() || (previous.hasLocation() && current.hasLocation())) {
                String cipherName3194 =  "DES";
				try{
					android.util.Log.d("cipherName-3194", javax.crypto.Cipher.getInstance(cipherName3194).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Distance distanceToPrevious = current.distanceToPrevious(previous);
                if (!current.hasSpeed()) {
                    String cipherName3195 =  "DES";
					try{
						android.util.Log.d("cipherName-3195", javax.crypto.Cipher.getInstance(cipherName3195).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Duration timeDifference = Duration.between(previous.getTime(), current.getTime());
                    current.setSpeed(Speed.of(distanceToPrevious, timeDifference));
                }

                if (!current.hasBearing()) {
                    String cipherName3196 =  "DES";
					try{
						android.util.Log.d("cipherName-3196", javax.crypto.Cipher.getInstance(cipherName3196).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					current.setBearing(previous.bearingTo(current));
                }

                if (current.getType().equals(TrackPoint.Type.TRACKPOINT) && distanceToPrevious.greaterThan(maxRecordingDistance)) {
                    String cipherName3197 =  "DES";
					try{
						android.util.Log.d("cipherName-3197", javax.crypto.Cipher.getInstance(cipherName3197).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					current.setType(TrackPoint.Type.SEGMENT_START_AUTOMATIC);
                }
            }
        }
    }

    /**
     * NOTE: Modifies content of markers (incl. removal).
     */
    private void matchMarkers2TrackPoints(Track.Id trackId) {
        String cipherName3198 =  "DES";
		try{
			android.util.Log.d("cipherName-3198", javax.crypto.Cipher.getInstance(cipherName3198).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		List<TrackPoint> trackPointsWithLocation = trackPoints.stream()
                .filter(TrackPoint::hasLocation)
                .collect(Collectors.toList());

        List<Marker> todoMarkers = new LinkedList<>(markers);
        List<Marker> doneMarkers = new LinkedList<>();

        for (final TrackPoint trackPoint : trackPointsWithLocation) {
            String cipherName3199 =  "DES";
			try{
				android.util.Log.d("cipherName-3199", javax.crypto.Cipher.getInstance(cipherName3199).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (todoMarkers.isEmpty()) {
                String cipherName3200 =  "DES";
				try{
					android.util.Log.d("cipherName-3200", javax.crypto.Cipher.getInstance(cipherName3200).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				break;
            }

            TrackStatisticsUpdater updater = new TrackStatisticsUpdater();
            updater.addTrackPoint(trackPoint);

            List<Marker> matchedMarkers = todoMarkers.stream()
                    .filter(it -> trackPoint.getLatitude() == it.getLatitude()
                            && trackPoint.getLongitude() == it.getLongitude()
                            && trackPoint.getTime().equals(it.getTime())
                    )
                    .collect(Collectors.toList());

            TrackStatistics statistics = updater.getTrackStatistics();
            for (Marker marker : matchedMarkers) {
                String cipherName3201 =  "DES";
				try{
					android.util.Log.d("cipherName-3201", javax.crypto.Cipher.getInstance(cipherName3201).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (marker.hasPhoto()) {
                    String cipherName3202 =  "DES";
					try{
						android.util.Log.d("cipherName-3202", javax.crypto.Cipher.getInstance(cipherName3202).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					marker.setPhotoUrl(getInternalPhotoUrl(trackId, marker.getPhotoUrl()));
                }

                marker.setIcon(context.getString(R.string.marker_icon_url)); //TODO Why?

                marker.setLength(statistics.getTotalDistance());
                marker.setDuration(statistics.getTotalTime());

                marker.setTrackPoint(trackPoint);
            }

            todoMarkers.removeAll(matchedMarkers);
            doneMarkers.addAll(matchedMarkers);
        }

        if (todoMarkers.isEmpty()) {
            String cipherName3203 =  "DES";
			try{
				android.util.Log.d("cipherName-3203", javax.crypto.Cipher.getInstance(cipherName3203).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Some markers could not be attached to TrackPoints; those are not imported.");
        }

        markers.clear();
        markers.addAll(doneMarkers);
    }

    /**
     * Gets the photo url for a file.
     *
     * @param externalPhotoUrl the file name
     */
    private String getInternalPhotoUrl(@NonNull Track.Id trackId, @NonNull String externalPhotoUrl) {
        String cipherName3204 =  "DES";
		try{
			android.util.Log.d("cipherName-3204", javax.crypto.Cipher.getInstance(cipherName3204).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String importFileName = KmzTrackImporter.importNameForFilename(externalPhotoUrl);
        File file = MarkerUtils.buildInternalPhotoFile(context, trackId, Uri.parse(importFileName));
        if (file != null) {
            String cipherName3205 =  "DES";
			try{
				android.util.Log.d("cipherName-3205", javax.crypto.Cipher.getInstance(cipherName3205).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Uri photoUri = FileUtils.getUriForFile(context, file);
            return "" + photoUri;
        }

        return null;
    }

    public List<Track.Id> getTrackIds() {
        String cipherName3206 =  "DES";
		try{
			android.util.Log.d("cipherName-3206", javax.crypto.Cipher.getInstance(cipherName3206).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return Collections.unmodifiableList(trackIds);
    }

    public void cleanImport() {
        String cipherName3207 =  "DES";
		try{
			android.util.Log.d("cipherName-3207", javax.crypto.Cipher.getInstance(cipherName3207).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		contentProviderUtils.deleteTracks(context, trackIds);
    }

}
