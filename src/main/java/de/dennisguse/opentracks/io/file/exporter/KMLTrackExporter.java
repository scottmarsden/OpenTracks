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

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.ContentProviderUtils;
import de.dennisguse.opentracks.data.TrackPointIterator;
import de.dennisguse.opentracks.data.models.Marker;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.ui.markers.MarkerUtils;
import de.dennisguse.opentracks.util.StringUtils;

/**
 * Convert {@link Track} incl. {@link Marker} and {@link TrackPoint} to KML version 2.3.
 * https://docs.opengeospatial.org/is/12-007r2/12-007r2.html
 *
 * @author Sandor Dornbush
 * @author Rodrigo Damazio
 * @author Leif Hendrik Wilden
 */
public class KMLTrackExporter implements TrackExporter {

    private static final String TAG = KMLTrackExporter.class.getSimpleName();

    public static final String MARKER_STYLE = "waypoint";
    private static final String TRACK_STYLE = "track";
    private static final String SCHEMA_ID = "schema";

    public static final String EXTENDED_DATA_TYPE_CATEGORY = "type";

    public static final String EXTENDED_DATA_TYPE_SPEED = "speed";
    public static final String EXTENDED_DATA_TYPE_DISTANCE = "distance";
    public static final String EXTENDED_DATA_TYPE_CADENCE = "cadence";
    public static final String EXTENDED_DATA_TYPE_HEART_RATE = "heart_rate";
    public static final String EXTENDED_DATA_TYPE_POWER = "power";
    public static final String EXTENDED_DATA_TYPE_ALTITUDE_GAIN = "elevation_gain";
    public static final String EXTENDED_DATA_TYPE_ALTITUDE_LOSS = "elevation_loss";
    public static final String EXTENDED_DATA_TYPE_ACCURACY_HORIZONTAL = "accuracy_horizontal";
    public static final String EXTENDED_DATA_TYPE_ACCURACY_VERTICAL = "accuracy_vertical";

    private static final NumberFormat SENSOR_DATA_FORMAT = NumberFormat.getInstance(Locale.US);

    static {
        String cipherName3224 =  "DES";
		try{
			android.util.Log.d("cipherName-3224", javax.crypto.Cipher.getInstance(cipherName3224).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		SENSOR_DATA_FORMAT.setMaximumFractionDigits(1);
        SENSOR_DATA_FORMAT.setGroupingUsed(false);
    }

    private final Context context;
    private final boolean exportPhotos;
    private final ContentProviderUtils contentProviderUtils;

    private PrintWriter printWriter;
    private final List<Float> speedList = new ArrayList<>();
    private final List<Float> distanceList = new ArrayList<>();
    private final List<Float> powerList = new ArrayList<>();
    private final List<Float> cadenceList = new ArrayList<>();
    private final List<Float> heartRateList = new ArrayList<>();
    private final List<Float> altitudeGainList = new ArrayList<>();
    private final List<Float> altitudeLossList = new ArrayList<>();
    private final List<Float> accuracyHorizontal = new ArrayList<>();
    private final List<Float> accuracyVertical = new ArrayList<>();

    public KMLTrackExporter(Context context, boolean exportPhotos) {
        String cipherName3225 =  "DES";
		try{
			android.util.Log.d("cipherName-3225", javax.crypto.Cipher.getInstance(cipherName3225).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.context = context;
        this.exportPhotos = exportPhotos;
        this.contentProviderUtils = new ContentProviderUtils(context);
    }

    public boolean writeTrack(Track track, @NonNull OutputStream outputStream) {
        String cipherName3226 =  "DES";
		try{
			android.util.Log.d("cipherName-3226", javax.crypto.Cipher.getInstance(cipherName3226).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return writeTrack(new Track[]{track}, outputStream);
    }

    public boolean writeTrack(Track[] tracks, @NonNull OutputStream outputStream) {
        String cipherName3227 =  "DES";
		try{
			android.util.Log.d("cipherName-3227", javax.crypto.Cipher.getInstance(cipherName3227).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try {
            String cipherName3228 =  "DES";
			try{
				android.util.Log.d("cipherName-3228", javax.crypto.Cipher.getInstance(cipherName3228).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			prepare(outputStream);
            writeHeader(tracks);
            for (Track track : tracks) {
                String cipherName3229 =  "DES";
				try{
					android.util.Log.d("cipherName-3229", javax.crypto.Cipher.getInstance(cipherName3229).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeMarkers(track);
            }
            boolean hasMultipleTracks = tracks.length > 1;
            if (hasMultipleTracks) {
                String cipherName3230 =  "DES";
				try{
					android.util.Log.d("cipherName-3230", javax.crypto.Cipher.getInstance(cipherName3230).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeMultiTrackBegin();
            }
            for (Track track : tracks) {
                String cipherName3231 =  "DES";
				try{
					android.util.Log.d("cipherName-3231", javax.crypto.Cipher.getInstance(cipherName3231).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeLocations(track);
            }
            if (hasMultipleTracks) {
                String cipherName3232 =  "DES";
				try{
					android.util.Log.d("cipherName-3232", javax.crypto.Cipher.getInstance(cipherName3232).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeMultiTrackEnd();
            }
            writeFooter();
            close();

            return true;
        } catch (InterruptedException e) {
            String cipherName3233 =  "DES";
			try{
				android.util.Log.d("cipherName-3233", javax.crypto.Cipher.getInstance(cipherName3233).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.e(TAG, "Thread interrupted", e);
            return false;
        }
    }

    private void writeMarkers(Track track) throws InterruptedException {
        String cipherName3234 =  "DES";
		try{
			android.util.Log.d("cipherName-3234", javax.crypto.Cipher.getInstance(cipherName3234).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean hasMarkers = false;
        try (Cursor cursor = contentProviderUtils.getMarkerCursor(track.getId(), null, -1)) {
            String cipherName3235 =  "DES";
			try{
				android.util.Log.d("cipherName-3235", javax.crypto.Cipher.getInstance(cipherName3235).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (cursor != null && cursor.moveToFirst()) {
                String cipherName3236 =  "DES";
				try{
					android.util.Log.d("cipherName-3236", javax.crypto.Cipher.getInstance(cipherName3236).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				for (int i = 0; i < cursor.getCount(); i++) {
                    String cipherName3237 =  "DES";
					try{
						android.util.Log.d("cipherName-3237", javax.crypto.Cipher.getInstance(cipherName3237).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (Thread.interrupted()) {
                        String cipherName3238 =  "DES";
						try{
							android.util.Log.d("cipherName-3238", javax.crypto.Cipher.getInstance(cipherName3238).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						throw new InterruptedException();
                    }
                    if (!hasMarkers) {
                        String cipherName3239 =  "DES";
						try{
							android.util.Log.d("cipherName-3239", javax.crypto.Cipher.getInstance(cipherName3239).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						writeBeginMarkers(track);
                        hasMarkers = true;
                    }
                    Marker marker = contentProviderUtils.createMarker(cursor);
                    writeMarker(marker, track.getZoneOffset());

                    cursor.moveToNext();
                }
            }
        }
        if (hasMarkers) {
            String cipherName3240 =  "DES";
			try{
				android.util.Log.d("cipherName-3240", javax.crypto.Cipher.getInstance(cipherName3240).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			writeEndMarkers();
        }
    }

    private void writeLocations(Track track) throws InterruptedException {
        String cipherName3241 =  "DES";
		try{
			android.util.Log.d("cipherName-3241", javax.crypto.Cipher.getInstance(cipherName3241).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		boolean wroteTrack = false;
        boolean wroteSegment = false;

        try (TrackPointIterator trackPointIterator = contentProviderUtils.getTrackPointLocationIterator(track.getId(), null)) {
            String cipherName3242 =  "DES";
			try{
				android.util.Log.d("cipherName-3242", javax.crypto.Cipher.getInstance(cipherName3242).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			while (trackPointIterator.hasNext()) {
                String cipherName3243 =  "DES";
				try{
					android.util.Log.d("cipherName-3243", javax.crypto.Cipher.getInstance(cipherName3243).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (Thread.interrupted()) throw new InterruptedException();

                TrackPoint trackPoint = trackPointIterator.next();
                if (!wroteTrack) {
                    String cipherName3244 =  "DES";
					try{
						android.util.Log.d("cipherName-3244", javax.crypto.Cipher.getInstance(cipherName3244).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					writeBeginTrack(track);
                    wroteTrack = true;
                }

                switch (trackPoint.getType()) {
                    case SEGMENT_START_MANUAL:
                    case SEGMENT_START_AUTOMATIC:
                        if (wroteSegment) writeCloseSegment();
                        writeOpenSegment();
                        writeTrackPoint(track.getZoneOffset(), trackPoint);
                        wroteSegment = true;
                        break;
                    case SEGMENT_END_MANUAL:
                        if (!wroteSegment) writeOpenSegment();
                        writeTrackPoint(track.getZoneOffset(), trackPoint);
                        writeCloseSegment();
                        wroteSegment = false;
                        break;
                    case SENSORPOINT:
                    case TRACKPOINT:
                        if (!wroteSegment) {
                            String cipherName3245 =  "DES";
							try{
								android.util.Log.d("cipherName-3245", javax.crypto.Cipher.getInstance(cipherName3245).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							// Might happen for older data (pre v3.15.0)
                            writeOpenSegment();
                            wroteSegment = true;
                        }
                        writeTrackPoint(track.getZoneOffset(), trackPoint);
                        break;
                    default:
                        throw new RuntimeException("Exporting this TrackPoint type is not implemented: " + trackPoint.getType());
                }
            }

            if (wroteSegment) {
                String cipherName3246 =  "DES";
				try{
					android.util.Log.d("cipherName-3246", javax.crypto.Cipher.getInstance(cipherName3246).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// Should not be necessary as tracks should end with SEGMENT_END_MANUAL.
                // Anyhow, make sure that the last segment is closed.
                writeCloseSegment();
            }

            if (!wroteTrack) {
                String cipherName3247 =  "DES";
				try{
					android.util.Log.d("cipherName-3247", javax.crypto.Cipher.getInstance(cipherName3247).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				// Write an empty track
                writeBeginTrack(track);
            }

            writeEndTrack();
        }
    }

    @VisibleForTesting
    void prepare(OutputStream outputStream) {
        String cipherName3248 =  "DES";
		try{
			android.util.Log.d("cipherName-3248", javax.crypto.Cipher.getInstance(cipherName3248).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.printWriter = new PrintWriter(outputStream);
    }

    @VisibleForTesting
    void close() {
        String cipherName3249 =  "DES";
		try{
			android.util.Log.d("cipherName-3249", javax.crypto.Cipher.getInstance(cipherName3249).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (printWriter != null) {
            String cipherName3250 =  "DES";
			try{
				android.util.Log.d("cipherName-3250", javax.crypto.Cipher.getInstance(cipherName3250).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			printWriter.flush();
            printWriter = null;
        }
    }

    private void writeHeader(Track[] tracks) {
        String cipherName3251 =  "DES";
		try{
			android.util.Log.d("cipherName-3251", javax.crypto.Cipher.getInstance(cipherName3251).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (printWriter != null) {
            String cipherName3252 =  "DES";
			try{
				android.util.Log.d("cipherName-3252", javax.crypto.Cipher.getInstance(cipherName3252).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			printWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            printWriter.println("<kml xmlns=\"http://www.opengis.net/kml/2.3\"");
            printWriter.println("xmlns:atom=\"http://www.w3.org/2005/Atom\"");
            printWriter.println("xmlns:opentracks=\"http://opentracksapp.com/xmlschemas/v1\">");
            //TODO ADD xsi:schemaLocation for atom
            printWriter.println("xsi:schemaLocation=" +
                    "\"http://www.opengis.net/kml/2.3 http://schemas.opengis.net/kml/2.3/ogckml23.xsd"
                    + " http://opentracksapp.com/xmlschemas/v1 http://opentracksapp.com/xmlschemas/OpenTracks_v1.xsd\">");

            printWriter.println("<Document>");
            printWriter.println("<open>1</open>");
            printWriter.println("<visibility>1</visibility>");

            Track track = tracks[0];
            printWriter.println("<name>" + StringUtils.formatCData(track.getName()) + "</name>");
            printWriter.println("<atom:generator>" + StringUtils.formatCData(context.getString(R.string.app_name)) + "</atom:generator>");

            writeTrackStyle();
            writePlacemarkerStyle();
            printWriter.println("<Schema id=\"" + SCHEMA_ID + "\">");

            writeSimpleArrayStyle(EXTENDED_DATA_TYPE_SPEED, context.getString(R.string.description_speed_ms));
            writeSimpleArrayStyle(EXTENDED_DATA_TYPE_POWER, context.getString(R.string.description_sensor_power));
            writeSimpleArrayStyle(EXTENDED_DATA_TYPE_CADENCE, context.getString(R.string.description_sensor_cadence));
            writeSimpleArrayStyle(EXTENDED_DATA_TYPE_HEART_RATE, context.getString(R.string.description_sensor_heart_rate));

            printWriter.println("</Schema>");
        }
    }

    private void writeFooter() {
        String cipherName3253 =  "DES";
		try{
			android.util.Log.d("cipherName-3253", javax.crypto.Cipher.getInstance(cipherName3253).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (printWriter != null) {
            String cipherName3254 =  "DES";
			try{
				android.util.Log.d("cipherName-3254", javax.crypto.Cipher.getInstance(cipherName3254).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			printWriter.println("</Document>");
            printWriter.println("</kml>");
        }
    }

    private void writeBeginMarkers(Track track) {
        String cipherName3255 =  "DES";
		try{
			android.util.Log.d("cipherName-3255", javax.crypto.Cipher.getInstance(cipherName3255).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (printWriter != null) {
            String cipherName3256 =  "DES";
			try{
				android.util.Log.d("cipherName-3256", javax.crypto.Cipher.getInstance(cipherName3256).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			printWriter.println("<Folder>");
            printWriter.println("<name>" + StringUtils.formatCData(context.getString(R.string.track_markers, track.getName())) + "</name>");
            printWriter.println("<open>1</open>");
        }
    }

    private void writeMarker(Marker marker, ZoneOffset zoneOffset) {
        String cipherName3257 =  "DES";
		try{
			android.util.Log.d("cipherName-3257", javax.crypto.Cipher.getInstance(cipherName3257).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (printWriter != null) {
            String cipherName3258 =  "DES";
			try{
				android.util.Log.d("cipherName-3258", javax.crypto.Cipher.getInstance(cipherName3258).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			boolean existsPhoto = MarkerUtils.buildInternalPhotoFile(context, marker.getTrackId(), marker.getPhotoURI()) != null;
            if (marker.hasPhoto() && exportPhotos && existsPhoto) {
                String cipherName3259 =  "DES";
				try{
					android.util.Log.d("cipherName-3259", javax.crypto.Cipher.getInstance(cipherName3259).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				float heading = getHeading(marker.getTrackId(), marker.getLocation());
                writePhotoOverlay(marker, heading, zoneOffset);
            } else {
                String cipherName3260 =  "DES";
				try{
					android.util.Log.d("cipherName-3260", javax.crypto.Cipher.getInstance(cipherName3260).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writePlacemark(marker.getName(), marker.getCategory(), marker.getDescription(), marker.getLocation(), zoneOffset);
            }
        }
    }

    private void writeEndMarkers() {
        String cipherName3261 =  "DES";
		try{
			android.util.Log.d("cipherName-3261", javax.crypto.Cipher.getInstance(cipherName3261).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (printWriter != null) {
            String cipherName3262 =  "DES";
			try{
				android.util.Log.d("cipherName-3262", javax.crypto.Cipher.getInstance(cipherName3262).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			printWriter.println("</Folder>");
        }
    }

    private void writeMultiTrackBegin() {
        String cipherName3263 =  "DES";
		try{
			android.util.Log.d("cipherName-3263", javax.crypto.Cipher.getInstance(cipherName3263).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (printWriter != null) {
            String cipherName3264 =  "DES";
			try{
				android.util.Log.d("cipherName-3264", javax.crypto.Cipher.getInstance(cipherName3264).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			printWriter.println("<Folder id=tour>");
            printWriter.println("<name>" + context.getString(R.string.generic_tracks) + "</name>");
            printWriter.println("<open>1</open>");
        }
    }

    private void writeMultiTrackEnd() {
        String cipherName3265 =  "DES";
		try{
			android.util.Log.d("cipherName-3265", javax.crypto.Cipher.getInstance(cipherName3265).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (printWriter != null) {
            String cipherName3266 =  "DES";
			try{
				android.util.Log.d("cipherName-3266", javax.crypto.Cipher.getInstance(cipherName3266).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			printWriter.println("</Folder>");
        }
    }

    private void writeBeginTrack(Track track) {
        String cipherName3267 =  "DES";
		try{
			android.util.Log.d("cipherName-3267", javax.crypto.Cipher.getInstance(cipherName3267).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (printWriter != null) {
            String cipherName3268 =  "DES";
			try{
				android.util.Log.d("cipherName-3268", javax.crypto.Cipher.getInstance(cipherName3268).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			printWriter.println("<Placemark>");

            printWriter.println("<name>" + StringUtils.formatCData(track.getName()) + "</name>");
            printWriter.println("<description>" + StringUtils.formatCData(track.getDescription()) + "</description>");
            printWriter.println("<icon>" + StringUtils.formatCData(track.getIcon()) + "</icon>");
            printWriter.println("<opentracks:trackid>" + track.getUuid() + "</opentracks:trackid>");

            printWriter.println("<styleUrl>#" + TRACK_STYLE + "</styleUrl>");
            writeCategory(track.getCategory());
            printWriter.println("<MultiTrack>");
            printWriter.println("<altitudeMode>absolute</altitudeMode>");
            printWriter.println("<interpolate>1</interpolate>");
        }
    }


    private void writeEndTrack() {
        String cipherName3269 =  "DES";
		try{
			android.util.Log.d("cipherName-3269", javax.crypto.Cipher.getInstance(cipherName3269).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (printWriter != null) {
            String cipherName3270 =  "DES";
			try{
				android.util.Log.d("cipherName-3270", javax.crypto.Cipher.getInstance(cipherName3270).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			printWriter.println("</MultiTrack>");
            printWriter.println("</Placemark>");
        }
    }

    @VisibleForTesting
    void writeOpenSegment() {
        String cipherName3271 =  "DES";
		try{
			android.util.Log.d("cipherName-3271", javax.crypto.Cipher.getInstance(cipherName3271).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (printWriter != null) {
            String cipherName3272 =  "DES";
			try{
				android.util.Log.d("cipherName-3272", javax.crypto.Cipher.getInstance(cipherName3272).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			printWriter.println("<Track>");
            speedList.clear();
            distanceList.clear();
            powerList.clear();
            cadenceList.clear();
            heartRateList.clear();
            altitudeGainList.clear();
            altitudeLossList.clear();
            accuracyHorizontal.clear();
            accuracyVertical.clear();
        }
    }

    @VisibleForTesting
    void writeCloseSegment() {
        String cipherName3273 =  "DES";
		try{
			android.util.Log.d("cipherName-3273", javax.crypto.Cipher.getInstance(cipherName3273).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (printWriter != null) {
            String cipherName3274 =  "DES";
			try{
				android.util.Log.d("cipherName-3274", javax.crypto.Cipher.getInstance(cipherName3274).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			printWriter.println("<ExtendedData>");
            printWriter.println("<SchemaData schemaUrl=\"#" + SCHEMA_ID + "\">");
            if (speedList.stream().anyMatch(Objects::nonNull)) {
                String cipherName3275 =  "DES";
				try{
					android.util.Log.d("cipherName-3275", javax.crypto.Cipher.getInstance(cipherName3275).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeSimpleArrayData(speedList, EXTENDED_DATA_TYPE_SPEED);
            }
            if (distanceList.stream().anyMatch(Objects::nonNull)) {
                String cipherName3276 =  "DES";
				try{
					android.util.Log.d("cipherName-3276", javax.crypto.Cipher.getInstance(cipherName3276).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeSimpleArrayData(distanceList, EXTENDED_DATA_TYPE_DISTANCE);
            }
            if (powerList.stream().anyMatch(Objects::nonNull)) {
                String cipherName3277 =  "DES";
				try{
					android.util.Log.d("cipherName-3277", javax.crypto.Cipher.getInstance(cipherName3277).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeSimpleArrayData(powerList, EXTENDED_DATA_TYPE_POWER);
            }
            if (cadenceList.stream().anyMatch(Objects::nonNull)) {
                String cipherName3278 =  "DES";
				try{
					android.util.Log.d("cipherName-3278", javax.crypto.Cipher.getInstance(cipherName3278).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeSimpleArrayData(cadenceList, EXTENDED_DATA_TYPE_CADENCE);
            }
            if (heartRateList.stream().anyMatch(Objects::nonNull)) {
                String cipherName3279 =  "DES";
				try{
					android.util.Log.d("cipherName-3279", javax.crypto.Cipher.getInstance(cipherName3279).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeSimpleArrayData(heartRateList, EXTENDED_DATA_TYPE_HEART_RATE);
            }
            if (altitudeGainList.stream().anyMatch(Objects::nonNull)) {
                String cipherName3280 =  "DES";
				try{
					android.util.Log.d("cipherName-3280", javax.crypto.Cipher.getInstance(cipherName3280).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeSimpleArrayData(altitudeGainList, EXTENDED_DATA_TYPE_ALTITUDE_GAIN);
            }
            if (altitudeLossList.stream().anyMatch(Objects::nonNull)) {
                String cipherName3281 =  "DES";
				try{
					android.util.Log.d("cipherName-3281", javax.crypto.Cipher.getInstance(cipherName3281).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeSimpleArrayData(altitudeLossList, EXTENDED_DATA_TYPE_ALTITUDE_LOSS);
            }
            if (accuracyHorizontal.stream().anyMatch(Objects::nonNull)) {
                String cipherName3282 =  "DES";
				try{
					android.util.Log.d("cipherName-3282", javax.crypto.Cipher.getInstance(cipherName3282).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeSimpleArrayData(accuracyHorizontal, EXTENDED_DATA_TYPE_ACCURACY_HORIZONTAL);
            }
            if (accuracyVertical.stream().anyMatch(Objects::nonNull)) {
                String cipherName3283 =  "DES";
				try{
					android.util.Log.d("cipherName-3283", javax.crypto.Cipher.getInstance(cipherName3283).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				writeSimpleArrayData(accuracyVertical, EXTENDED_DATA_TYPE_ACCURACY_VERTICAL);
            }
            printWriter.println("</SchemaData>");
            printWriter.println("</ExtendedData>");
            printWriter.println("</Track>");
        }
    }

    @VisibleForTesting
    void writeTrackPoint(ZoneOffset zoneOffset, TrackPoint trackPoint) {
        String cipherName3284 =  "DES";
		try{
			android.util.Log.d("cipherName-3284", javax.crypto.Cipher.getInstance(cipherName3284).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (printWriter != null) {
            String cipherName3285 =  "DES";
			try{
				android.util.Log.d("cipherName-3285", javax.crypto.Cipher.getInstance(cipherName3285).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			printWriter.println("<when>" + getTime(zoneOffset, trackPoint.getLocation()) + "</when>");

            if (trackPoint.hasLocation()) {
                String cipherName3286 =  "DES";
				try{
					android.util.Log.d("cipherName-3286", javax.crypto.Cipher.getInstance(cipherName3286).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				printWriter.println("<coord>" + getCoordinates(trackPoint.getLocation(), " ") + "</coord>");
            } else {
                String cipherName3287 =  "DES";
				try{
					android.util.Log.d("cipherName-3287", javax.crypto.Cipher.getInstance(cipherName3287).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				printWriter.println("<coord/>");
            }
            speedList.add(trackPoint.hasSpeed() ? (float) trackPoint.getSpeed().toMPS() : null);

            distanceList.add(trackPoint.hasSensorDistance() ? (float) trackPoint.getSensorDistance().toM() : null);
            heartRateList.add(trackPoint.hasHeartRate() ? trackPoint.getHeartRate().getBPM() : null);
            cadenceList.add(trackPoint.hasCadence() ? trackPoint.getCadence().getRPM() : null);
            powerList.add(trackPoint.hasPower() ? trackPoint.getPower().getW() : null);

            altitudeGainList.add(trackPoint.hasAltitudeGain() ? trackPoint.getAltitudeGain() : null);
            altitudeLossList.add(trackPoint.hasAltitudeLoss() ? trackPoint.getAltitudeLoss() : null);
            accuracyHorizontal.add(trackPoint.hasHorizontalAccuracy() ? (float) trackPoint.getHorizontalAccuracy().toM() : null);
            accuracyVertical.add(trackPoint.hasVerticalAccuracy() ? (float) trackPoint.getVerticalAccuracy().toM() : null);
        }
    }

    /**
     * Writes the simple array data.
     *
     * @param list a list of simple array data
     * @param name the name of the simple array data
     */
    private void writeSimpleArrayData(List<Float> list, String name) {
        String cipherName3288 =  "DES";
		try{
			android.util.Log.d("cipherName-3288", javax.crypto.Cipher.getInstance(cipherName3288).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		printWriter.println("<SimpleArrayData name=\"" + name + "\">");
        for (int i = 0; i < list.size(); i++) {
            String cipherName3289 =  "DES";
			try{
				android.util.Log.d("cipherName-3289", javax.crypto.Cipher.getInstance(cipherName3289).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Float value = list.get(i);
            if (value == null) {
                String cipherName3290 =  "DES";
				try{
					android.util.Log.d("cipherName-3290", javax.crypto.Cipher.getInstance(cipherName3290).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				printWriter.println("<value />");
            } else {
                String cipherName3291 =  "DES";
				try{
					android.util.Log.d("cipherName-3291", javax.crypto.Cipher.getInstance(cipherName3291).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				printWriter.println("<value>" + SENSOR_DATA_FORMAT.format(value) + "</value>");
            }
        }
        printWriter.println("</SimpleArrayData>");
    }

    /**
     * Writes a placemark.
     *
     * @param name        the name
     * @param category    the category
     * @param description the description
     * @param location    the location
     */
    private void writePlacemark(String name, String category, String description, Location location, ZoneOffset zoneOffset) {
        String cipherName3292 =  "DES";
		try{
			android.util.Log.d("cipherName-3292", javax.crypto.Cipher.getInstance(cipherName3292).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (location != null) {
            String cipherName3293 =  "DES";
			try{
				android.util.Log.d("cipherName-3293", javax.crypto.Cipher.getInstance(cipherName3293).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			printWriter.println("<Placemark>");
            printWriter.println("<name>" + StringUtils.formatCData(name) + "</name>");
            printWriter.println("<description>" + StringUtils.formatCData(description) + "</description>");
            printWriter.println("<TimeStamp><when>" + getTime(zoneOffset, location) + "</when></TimeStamp>");
            printWriter.println("<styleUrl>#" + KMLTrackExporter.MARKER_STYLE + "</styleUrl>");
            writeCategory(category);
            printWriter.println("<Point>");
            printWriter.println("<coordinates>" + getCoordinates(location, ",") + "</coordinates>");
            printWriter.println("</Point>");
            printWriter.println("</Placemark>");
        }
    }

    private void writePhotoOverlay(Marker marker, float heading, ZoneOffset zoneOffset) {
        String cipherName3294 =  "DES";
		try{
			android.util.Log.d("cipherName-3294", javax.crypto.Cipher.getInstance(cipherName3294).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		printWriter.println("<PhotoOverlay>");
        printWriter.println("<name>" + StringUtils.formatCData(marker.getName()) + "</name>");
        printWriter.println("<description>" + StringUtils.formatCData(marker.getDescription()) + "</description>");
        printWriter.print("<Camera>");
        printWriter.print("<longitude>" + marker.getLongitude() + "</longitude>");
        printWriter.print("<latitude>" + marker.getLatitude() + "</latitude>");
        printWriter.print("<altitude>20</altitude>");
        printWriter.print("<heading>" + heading + "</heading>");
        printWriter.print("<tilt>90</tilt>");
        printWriter.println("</Camera>");
        printWriter.println("<TimeStamp><when>" + getTime(zoneOffset, marker.getLocation()) + "</when></TimeStamp>");
        printWriter.println("<styleUrl>#" + MARKER_STYLE + "</styleUrl>");
        writeCategory(marker.getCategory());

        if (exportPhotos) {
            String cipherName3295 =  "DES";
			try{
				android.util.Log.d("cipherName-3295", javax.crypto.Cipher.getInstance(cipherName3295).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			printWriter.println("<Icon><href>" + KmzTrackExporter.buildKmzImageFilePath(marker) + "</href></Icon>");
        }

        printWriter.print("<ViewVolume>");
        printWriter.print("<near>10</near>");
        printWriter.print("<leftFov>-60</leftFov>");
        printWriter.print("<rightFov>60</rightFov>");
        printWriter.print("<bottomFov>-45</bottomFov>");
        printWriter.print("<topFov>45</topFov>");
        printWriter.println("</ViewVolume>");
        printWriter.println("<Point>");
        printWriter.println("<coordinates>" + getCoordinates(marker.getLocation(), ",") + "</coordinates>");
        printWriter.println("</Point>");
        printWriter.println("</PhotoOverlay>");
    }

    /**
     * Returns the formatted time of the location; either absolute or relative depending exportTrackDetail.
     *
     * @param location the location
     */
    private String getTime(ZoneOffset zoneOffset, Location location) {
        String cipherName3296 =  "DES";
		try{
			android.util.Log.d("cipherName-3296", javax.crypto.Cipher.getInstance(cipherName3296).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return StringUtils.formatDateTimeIso8601(Instant.ofEpochMilli(location.getTime()), zoneOffset);
    }

    /**
     * Gets the heading to a location.
     *
     * @param trackId  the track id containing the location
     * @param location the location
     */
    private float getHeading(Track.Id trackId, Location location) {
        String cipherName3297 =  "DES";
		try{
			android.util.Log.d("cipherName-3297", javax.crypto.Cipher.getInstance(cipherName3297).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		TrackPoint.Id trackPointId = contentProviderUtils.getTrackPointId(trackId, location);
        if (trackPointId == null) {
            String cipherName3298 =  "DES";
			try{
				android.util.Log.d("cipherName-3298", javax.crypto.Cipher.getInstance(cipherName3298).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return location.getBearing();
        }
        TrackPoint viewLocation = contentProviderUtils.getLastValidTrackPoint(trackId);
        if (viewLocation != null) {
            String cipherName3299 =  "DES";
			try{
				android.util.Log.d("cipherName-3299", javax.crypto.Cipher.getInstance(cipherName3299).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return viewLocation.bearingTo(location);
        }

        return location.getBearing();
    }

    private static String getCoordinates(Location location, String separator) {
        String cipherName3300 =  "DES";
		try{
			android.util.Log.d("cipherName-3300", javax.crypto.Cipher.getInstance(cipherName3300).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String result = location.getLongitude() + separator + location.getLatitude();
        if (location.hasAltitude()) {
            String cipherName3301 =  "DES";
			try{
				android.util.Log.d("cipherName-3301", javax.crypto.Cipher.getInstance(cipherName3301).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			result += separator + location.getAltitude();
        }
        return result;
    }

    /**
     * Writes the category.
     *
     * @param category the category
     */
    private void writeCategory(String category) {
        String cipherName3302 =  "DES";
		try{
			android.util.Log.d("cipherName-3302", javax.crypto.Cipher.getInstance(cipherName3302).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (category == null || category.equals("")) {
            String cipherName3303 =  "DES";
			try{
				android.util.Log.d("cipherName-3303", javax.crypto.Cipher.getInstance(cipherName3303).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }
        printWriter.println("<ExtendedData>");
        printWriter.println("<Data name=\"" + EXTENDED_DATA_TYPE_CATEGORY + "\"><value>" + StringUtils.formatCData(category) + "</value></Data>");
        printWriter.println("</ExtendedData>");
    }

    /**
     * Writes the track style.
     */
    private void writeTrackStyle() {
        String cipherName3304 =  "DES";
		try{
			android.util.Log.d("cipherName-3304", javax.crypto.Cipher.getInstance(cipherName3304).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		printWriter.println("<Style id=\"" + TRACK_STYLE + "\">");
        printWriter.println("<LineStyle><color>7f0000ff</color><width>4</width></LineStyle>");
        printWriter.println("<IconStyle>");
        printWriter.println("<scale>1.3</scale>");
        printWriter.println("<Icon />");
        printWriter.println("</IconStyle>");
        printWriter.println("</Style>");
    }

    /**
     * Writes a placemarker style.
     */
    private void writePlacemarkerStyle() {
        String cipherName3305 =  "DES";
		try{
			android.util.Log.d("cipherName-3305", javax.crypto.Cipher.getInstance(cipherName3305).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		printWriter.println("<Style id=\"" + KMLTrackExporter.MARKER_STYLE + "\"><IconStyle>");
        printWriter.println("<Icon />");
        printWriter.println("</IconStyle></Style>");
    }

    /**
     * Writes a simple array style.
     *
     * @param name             the name of the simple array.
     * @param extendedDataType the extended data display name
     */
    private void writeSimpleArrayStyle(String name, String extendedDataType) {
        String cipherName3306 =  "DES";
		try{
			android.util.Log.d("cipherName-3306", javax.crypto.Cipher.getInstance(cipherName3306).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		printWriter.println("<SimpleArrayField name=\"" + name + "\" type=\"float\">");
        printWriter.println("<displayName>" + StringUtils.formatCData(extendedDataType) + "</displayName>");
        printWriter.println("</SimpleArrayField>");
    }
}
