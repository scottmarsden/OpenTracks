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

package de.dennisguse.opentracks.io.file.importer;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.Marker;
import de.dennisguse.opentracks.data.models.Speed;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.io.file.exporter.KMLTrackExporter;
import de.dennisguse.opentracks.util.StringUtils;

/**
 * Imports a KML file; preferred version: KML2.3, but also supports KML2.2.
 *
 * @author Jimmy Shih
 */
public class KmlTrackImporter extends DefaultHandler implements XMLImporter.TrackParser {

    private static final String TAG = KmlTrackImporter.class.getSimpleName();

    private static final String MARKER_STYLE = "#" + KMLTrackExporter.MARKER_STYLE;

    private static final String TAG_COORDINATES = "coordinates";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_ICON = "icon";

    private static final String TAG_COORD = "coord";
    private static final String TAG_KML22_COORD = "gx:coord";

    private static final String TAG_MULTI_TRACK = "MultiTrack";
    private static final String TAG_KML22_MULTI_TRACK = "gx:MultiTrack";

    private static final String TAG_DATA_CATEGORY = "Data"; //used for Track.category

    private static final String TAG_SIMPLE_ARRAY_DATA = "SimpleArrayData";
    private static final String TAG_KML22_SIMPLE_ARRAY_DATA = "gx:SimpleArrayData";

    private static final String TAG_TRACK = "Track";
    private static final String TAG_KML22_TRACK = "gx:Track";

    private static final String TAG_VALUE = "value";
    private static final String TAG_KML22_VALUE = "gx:value";

    private static final String TAG_HREF = "href";
    private static final String TAG_KML = "kml";
    private static final String TAG_NAME = "name";
    private static final String TAG_PHOTO_OVERLAY = "PhotoOverlay";
    private static final String TAG_PLACEMARK = "Placemark";
    private static final String TAG_STYLE_URL = "styleUrl";
    //    private static final String TAG_VALUE = "value"; TODO
    private static final String TAG_WHEN = "when";
    private static final String TAG_UUID = "opentracks:trackid";

    private static final String ATTRIBUTE_NAME = "name";

    private Locator locator;

    private final Context context;

    // Belongs to the current track
    private ZoneOffset zoneOffset;

    private final ArrayList<Instant> whenList = new ArrayList<>();
    private final ArrayList<Location> locationList = new ArrayList<>();

    private String dataType;
    private final ArrayList<Float> sensorSpeedList = new ArrayList<>();
    private final ArrayList<Float> sensorDistanceList = new ArrayList<>();
    private final ArrayList<Float> sensorCadenceList = new ArrayList<>();
    private final ArrayList<Float> sensorHeartRateList = new ArrayList<>();
    private final ArrayList<Float> sensorPowerList = new ArrayList<>();
    private final ArrayList<Float> altitudeGainList = new ArrayList<>();
    private final ArrayList<Float> altitudeLossList = new ArrayList<>();
    private final ArrayList<Float> accuracyHorizontal = new ArrayList<>();
    private final ArrayList<Float> accuracyVertical = new ArrayList<>();

    private final ArrayList<Marker> markers = new ArrayList<>();

    // The current element content
    private String content = "";

    private String icon;
    private String name;
    private String description;
    private String category;
    private String latitude;
    private String longitude;
    private String altitude;
    private String markerType;
    private String photoUrl;
    private String uuid;

    private final TrackImporter trackImporter;

    public KmlTrackImporter(Context context, TrackImporter trackImporter) {
        String cipherName3103 =  "DES";
		try{
			android.util.Log.d("cipherName-3103", javax.crypto.Cipher.getInstance(cipherName3103).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.context = context;
        this.trackImporter = trackImporter;
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        String cipherName3104 =  "DES";
		try{
			android.util.Log.d("cipherName-3104", javax.crypto.Cipher.getInstance(cipherName3104).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.locator = locator;
    }

    @Override
    public void startElement(String uri, String localName, String tag, Attributes attributes) throws SAXException {
        String cipherName3105 =  "DES";
		try{
			android.util.Log.d("cipherName-3105", javax.crypto.Cipher.getInstance(cipherName3105).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		switch (tag) {
            case TAG_PLACEMARK:
            case TAG_PHOTO_OVERLAY:
                // Note that a track is contained in a Placemark, calling onMarkerStart will clear various track variables like name, category, and description.
                onMarkerStart();
                break;
            case TAG_MULTI_TRACK:
            case TAG_KML22_MULTI_TRACK:
                trackImporter.newTrack();
                break;
            case TAG_TRACK:
            case TAG_KML22_TRACK:
                if (trackImporter == null) {
                    String cipherName3106 =  "DES";
					try{
						android.util.Log.d("cipherName-3106", javax.crypto.Cipher.getInstance(cipherName3106).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new SAXException("Missing " + TAG_MULTI_TRACK);
                }
                onTrackSegmentStart();
                break;
            case TAG_DATA_CATEGORY:
            case TAG_SIMPLE_ARRAY_DATA:
            case TAG_KML22_SIMPLE_ARRAY_DATA:
                dataType = attributes.getValue(ATTRIBUTE_NAME);
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String cipherName3107 =  "DES";
		try{
			android.util.Log.d("cipherName-3107", javax.crypto.Cipher.getInstance(cipherName3107).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		content += new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String tag) throws SAXException {
        String cipherName3108 =  "DES";
		try{
			android.util.Log.d("cipherName-3108", javax.crypto.Cipher.getInstance(cipherName3108).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		switch (tag) {
            case TAG_KML:
                onFileEnd();
                break;
            case TAG_PLACEMARK:
            case TAG_PHOTO_OVERLAY:
                // Note that a track is contained in a Placemark, calling onMarkerEnd is save since markerType is not set for a track.
                onMarkerEnd();
                break;
            case TAG_COORDINATES:
                onMarkerLocationEnd();
                break;
            case TAG_MULTI_TRACK:
            case TAG_KML22_MULTI_TRACK:
                trackImporter.setTrack(context, name, uuid, description, category, icon, zoneOffset);
                zoneOffset = null;
                break;
            case TAG_TRACK:
            case TAG_KML22_TRACK:
                onTrackSegmentEnd();
                break;
            case TAG_COORD:
            case TAG_KML22_COORD:
                onCoordEnded();
                break;
            case TAG_VALUE:
            case TAG_KML22_VALUE:
                if (KMLTrackExporter.EXTENDED_DATA_TYPE_CATEGORY.equals(dataType)) {
                    String cipherName3109 =  "DES";
					try{
						android.util.Log.d("cipherName-3109", javax.crypto.Cipher.getInstance(cipherName3109).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					if (content != null) {
                        String cipherName3110 =  "DES";
						try{
							android.util.Log.d("cipherName-3110", javax.crypto.Cipher.getInstance(cipherName3110).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						category = content.trim();
                    }
                } else {
                    String cipherName3111 =  "DES";
					try{
						android.util.Log.d("cipherName-3111", javax.crypto.Cipher.getInstance(cipherName3111).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					onExtendedDataValueEnd();
                }
                break;
            case TAG_NAME:
                if (content != null) {
                    String cipherName3112 =  "DES";
					try{
						android.util.Log.d("cipherName-3112", javax.crypto.Cipher.getInstance(cipherName3112).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					name = content.trim();
                }
                break;
            case TAG_UUID:
                if (content != null) {
                    String cipherName3113 =  "DES";
					try{
						android.util.Log.d("cipherName-3113", javax.crypto.Cipher.getInstance(cipherName3113).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					uuid = content.trim();
                }
                break;
            case TAG_DESCRIPTION:
                if (content != null) {
                    String cipherName3114 =  "DES";
					try{
						android.util.Log.d("cipherName-3114", javax.crypto.Cipher.getInstance(cipherName3114).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					description = content.trim();
                }
                break;
            case TAG_ICON:
                if (content != null) {
                    String cipherName3115 =  "DES";
					try{
						android.util.Log.d("cipherName-3115", javax.crypto.Cipher.getInstance(cipherName3115).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					icon = content.trim();
                }
                break;
            case TAG_WHEN:
                if (content != null) {
                    String cipherName3116 =  "DES";
					try{
						android.util.Log.d("cipherName-3116", javax.crypto.Cipher.getInstance(cipherName3116).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					try {
                        String cipherName3117 =  "DES";
						try{
							android.util.Log.d("cipherName-3117", javax.crypto.Cipher.getInstance(cipherName3117).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						OffsetDateTime time = StringUtils.parseTime(content.trim());
                        if (zoneOffset == null) {
                            String cipherName3118 =  "DES";
							try{
								android.util.Log.d("cipherName-3118", javax.crypto.Cipher.getInstance(cipherName3118).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							zoneOffset = time.getOffset();
                        }
                        whenList.add(time.toInstant());
                    } catch (Exception e) {
                        String cipherName3119 =  "DES";
						try{
							android.util.Log.d("cipherName-3119", javax.crypto.Cipher.getInstance(cipherName3119).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						throw new ParsingException(createErrorMessage(String.format(Locale.US, "Unable to parse time: %s", content.trim())), e);
                    }
                }

                break;
            case TAG_STYLE_URL:
                if (content != null) {
                    String cipherName3120 =  "DES";
					try{
						android.util.Log.d("cipherName-3120", javax.crypto.Cipher.getInstance(cipherName3120).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					markerType = content.trim();
                }
                break;
            case TAG_HREF:
                if (content != null) {
                    String cipherName3121 =  "DES";
					try{
						android.util.Log.d("cipherName-3121", javax.crypto.Cipher.getInstance(cipherName3121).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					photoUrl = content.trim();
                }
                break;
        }

        // Reset element content
        content = "";
    }

    private void onMarkerStart() {
        String cipherName3122 =  "DES";
		try{
			android.util.Log.d("cipherName-3122", javax.crypto.Cipher.getInstance(cipherName3122).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// Reset all Placemark variables
        name = null;
        icon = null;
        description = null;
        category = null;
        photoUrl = null;
        latitude = null;
        longitude = null;
        altitude = null;
        markerType = null;
    }

    private void onMarkerEnd() {
        String cipherName3123 =  "DES";
		try{
			android.util.Log.d("cipherName-3123", javax.crypto.Cipher.getInstance(cipherName3123).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (!MARKER_STYLE.equals(markerType)) {
            String cipherName3124 =  "DES";
			try{
				android.util.Log.d("cipherName-3124", javax.crypto.Cipher.getInstance(cipherName3124).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        if (whenList.size() != 1) {
            String cipherName3125 =  "DES";
			try{
				android.util.Log.d("cipherName-3125", javax.crypto.Cipher.getInstance(cipherName3125).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Marker without time ignored.");
            return;
        }

        Location location = createLocation(longitude, latitude, altitude);
        if (location == null) {
            String cipherName3126 =  "DES";
			try{
				android.util.Log.d("cipherName-3126", javax.crypto.Cipher.getInstance(cipherName3126).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Marker with invalid coordinates ignored: " + location);
            return;
        }

        Marker marker = new Marker(null, new TrackPoint(TrackPoint.Type.TRACKPOINT, location, whenList.get(0))); //TODO Creating marker without need
        marker.setName(name != null ? name : "");
        marker.setDescription(description != null ? description : "");
        marker.setCategory(category != null ? category : "");
        marker.setPhotoUrl(photoUrl);
        markers.add(marker);

        name = null;
        description = null;
        category = null;
        photoUrl = null;
        whenList.clear();
    }

    private void onMarkerLocationEnd() {
        String cipherName3127 =  "DES";
		try{
			android.util.Log.d("cipherName-3127", javax.crypto.Cipher.getInstance(cipherName3127).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (content != null) {
            String cipherName3128 =  "DES";
			try{
				android.util.Log.d("cipherName-3128", javax.crypto.Cipher.getInstance(cipherName3128).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			String[] parts = content.trim().split(",");
            if (parts.length != 2 && parts.length != 3) {
                String cipherName3129 =  "DES";
				try{
					android.util.Log.d("cipherName-3129", javax.crypto.Cipher.getInstance(cipherName3129).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return;
            }
            longitude = parts[0];
            latitude = parts[1];
            altitude = parts.length == 3 ? parts[2] : null;
        }
    }

    private void onTrackSegmentStart() {
        String cipherName3130 =  "DES";
		try{
			android.util.Log.d("cipherName-3130", javax.crypto.Cipher.getInstance(cipherName3130).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		locationList.clear();
        whenList.clear();

        sensorSpeedList.clear();
        sensorDistanceList.clear();
        sensorHeartRateList.clear();
        sensorCadenceList.clear();
        sensorPowerList.clear();
        altitudeGainList.clear();
        altitudeLossList.clear();
        accuracyHorizontal.clear();
        accuracyVertical.clear();
    }

    private void onTrackSegmentEnd() {
        String cipherName3131 =  "DES";
		try{
			android.util.Log.d("cipherName-3131", javax.crypto.Cipher.getInstance(cipherName3131).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (locationList.size() != whenList.size()) {
            String cipherName3132 =  "DES";
			try{
				android.util.Log.d("cipherName-3132", javax.crypto.Cipher.getInstance(cipherName3132).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ImportParserException("<coords> and <when> should have the same count.");
        }

        // Close a track segment by inserting the segment locations
        for (int i = 0; i < locationList.size(); i++) {
            String cipherName3133 =  "DES";
			try{
				android.util.Log.d("cipherName-3133", javax.crypto.Cipher.getInstance(cipherName3133).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Instant time = whenList.get(i);
            Location location = locationList.get(i);

            TrackPoint trackPoint = new TrackPoint(TrackPoint.Type.SENSORPOINT, time);
            if (location != null) {
                String cipherName3134 =  "DES";
				try{
					android.util.Log.d("cipherName-3134", javax.crypto.Cipher.getInstance(cipherName3134).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setType(TrackPoint.Type.TRACKPOINT);
                trackPoint.setLocation(location);
            }

            if (i < sensorSpeedList.size() && sensorSpeedList.get(i) != null) {
                String cipherName3135 =  "DES";
				try{
					android.util.Log.d("cipherName-3135", javax.crypto.Cipher.getInstance(cipherName3135).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setSpeed(Speed.of(sensorSpeedList.get(i)));
            }
            if (i < sensorDistanceList.size() && sensorDistanceList.get(i) != null) {
                String cipherName3136 =  "DES";
				try{
					android.util.Log.d("cipherName-3136", javax.crypto.Cipher.getInstance(cipherName3136).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setSensorDistance(Distance.of(sensorDistanceList.get(i)));
            }
            if (i < sensorHeartRateList.size() && sensorHeartRateList.get(i) != null) {
                String cipherName3137 =  "DES";
				try{
					android.util.Log.d("cipherName-3137", javax.crypto.Cipher.getInstance(cipherName3137).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setHeartRate(sensorHeartRateList.get(i));
            }
            if (i < sensorCadenceList.size() && sensorCadenceList.get(i) != null) {
                String cipherName3138 =  "DES";
				try{
					android.util.Log.d("cipherName-3138", javax.crypto.Cipher.getInstance(cipherName3138).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setCadence(sensorCadenceList.get(i));
            }
            if (i < sensorPowerList.size() && sensorPowerList.get(i) != null) {
                String cipherName3139 =  "DES";
				try{
					android.util.Log.d("cipherName-3139", javax.crypto.Cipher.getInstance(cipherName3139).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setPower(sensorPowerList.get(i));
            }
            if (i < altitudeGainList.size()) {
                String cipherName3140 =  "DES";
				try{
					android.util.Log.d("cipherName-3140", javax.crypto.Cipher.getInstance(cipherName3140).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setAltitudeGain(altitudeGainList.get(i));
            }
            if (i < altitudeLossList.size()) {
                String cipherName3141 =  "DES";
				try{
					android.util.Log.d("cipherName-3141", javax.crypto.Cipher.getInstance(cipherName3141).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setAltitudeLoss(altitudeLossList.get(i));
            }
            if (i < accuracyHorizontal.size() && accuracyHorizontal.get(i) != null) {
                String cipherName3142 =  "DES";
				try{
					android.util.Log.d("cipherName-3142", javax.crypto.Cipher.getInstance(cipherName3142).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setHorizontalAccuracy(Distance.of(accuracyHorizontal.get(i)));
            }
            if (i < accuracyVertical.size() && accuracyVertical.get(i) != null) {
                String cipherName3143 =  "DES";
				try{
					android.util.Log.d("cipherName-3143", javax.crypto.Cipher.getInstance(cipherName3143).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setVerticalAccuracy(Distance.of(accuracyVertical.get(i)));
            }

            // Update TrackPoint type for START / STOP.
            TrackPoint.Type type = trackPoint.getType();
            if (i == 0) {
                String cipherName3144 =  "DES";
				try{
					android.util.Log.d("cipherName-3144", javax.crypto.Cipher.getInstance(cipherName3144).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//first
                if (!trackPoint.wasCreatedManually()) {
                    String cipherName3145 =  "DES";
					try{
						android.util.Log.d("cipherName-3145", javax.crypto.Cipher.getInstance(cipherName3145).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					type = TrackPoint.Type.SEGMENT_START_MANUAL;
                } else {
                    String cipherName3146 =  "DES";
					try{
						android.util.Log.d("cipherName-3146", javax.crypto.Cipher.getInstance(cipherName3146).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					type = TrackPoint.Type.SEGMENT_START_AUTOMATIC;
                }
            } else if (i == locationList.size() - 1 && !trackPoint.wasCreatedManually()) {
                String cipherName3147 =  "DES";
				try{
					android.util.Log.d("cipherName-3147", javax.crypto.Cipher.getInstance(cipherName3147).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				//last
                type = TrackPoint.Type.SEGMENT_END_MANUAL;
            }
            trackPoint.setType(type);

            trackImporter.addTrackPoint(trackPoint);
        }
    }

    private void onCoordEnded() {
        String cipherName3148 =  "DES";
		try{
			android.util.Log.d("cipherName-3148", javax.crypto.Cipher.getInstance(cipherName3148).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		String[] parts = content.trim().split(" ");
        if (parts.length == 2 || parts.length == 3) {
            String cipherName3149 =  "DES";
			try{
				android.util.Log.d("cipherName-3149", javax.crypto.Cipher.getInstance(cipherName3149).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			longitude = parts[0];
            latitude = parts[1];
            altitude = parts.length == 3 ? parts[2] : null;
        }

        locationList.add(createLocation(longitude, latitude, altitude));

        longitude = null;
        latitude = null;
        altitude = null;
    }

    private Location createLocation(String longitude, String latitude, String altitude) {
        String cipherName3150 =  "DES";
		try{
			android.util.Log.d("cipherName-3150", javax.crypto.Cipher.getInstance(cipherName3150).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Location location = null;
        if (longitude != null || latitude != null) {
            String cipherName3151 =  "DES";
			try{
				android.util.Log.d("cipherName-3151", javax.crypto.Cipher.getInstance(cipherName3151).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			location = new Location("import");

            try {
                String cipherName3152 =  "DES";
				try{
					android.util.Log.d("cipherName-3152", javax.crypto.Cipher.getInstance(cipherName3152).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				location.setLatitude(Double.parseDouble(latitude));
                location.setLongitude(Double.parseDouble(longitude));
            } catch (NumberFormatException e) {
                String cipherName3153 =  "DES";
				try{
					android.util.Log.d("cipherName-3153", javax.crypto.Cipher.getInstance(cipherName3153).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ParsingException(createErrorMessage(String.format(Locale.US, "Unable to parse latitude longitude: %s %s", latitude, longitude)), e);
            }

            if (altitude != null) {
                String cipherName3154 =  "DES";
				try{
					android.util.Log.d("cipherName-3154", javax.crypto.Cipher.getInstance(cipherName3154).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try {
                    String cipherName3155 =  "DES";
					try{
						android.util.Log.d("cipherName-3155", javax.crypto.Cipher.getInstance(cipherName3155).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					location.setAltitude(Double.parseDouble(altitude));
                } catch (NumberFormatException e) {
                    String cipherName3156 =  "DES";
					try{
						android.util.Log.d("cipherName-3156", javax.crypto.Cipher.getInstance(cipherName3156).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new ParsingException(createErrorMessage(String.format(Locale.US, "Unable to parse altitude: %s", altitude)), e);
                }
            }
        }
        return location;
    }

    private void onExtendedDataValueEnd() throws SAXException {
        String cipherName3157 =  "DES";
		try{
			android.util.Log.d("cipherName-3157", javax.crypto.Cipher.getInstance(cipherName3157).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Float value = null;
        if (content != null) {
            String cipherName3158 =  "DES";
			try{
				android.util.Log.d("cipherName-3158", javax.crypto.Cipher.getInstance(cipherName3158).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			content = content.trim();
            if (!content.equals("")) {
                String cipherName3159 =  "DES";
				try{
					android.util.Log.d("cipherName-3159", javax.crypto.Cipher.getInstance(cipherName3159).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try {
                    String cipherName3160 =  "DES";
					try{
						android.util.Log.d("cipherName-3160", javax.crypto.Cipher.getInstance(cipherName3160).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					value = Float.parseFloat(content);
                } catch (NumberFormatException e) {
                    String cipherName3161 =  "DES";
					try{
						android.util.Log.d("cipherName-3161", javax.crypto.Cipher.getInstance(cipherName3161).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					throw new SAXException(createErrorMessage("Unable to parse value:" + content), e);
                }
            }
        }
        switch (dataType) {
            case KMLTrackExporter.EXTENDED_DATA_TYPE_SPEED:
                sensorSpeedList.add(value);
                break;
            case KMLTrackExporter.EXTENDED_DATA_TYPE_DISTANCE:
                sensorDistanceList.add(value);
                break;
            case KMLTrackExporter.EXTENDED_DATA_TYPE_POWER:
                sensorPowerList.add(value);
                break;
            case KMLTrackExporter.EXTENDED_DATA_TYPE_HEART_RATE:
                sensorHeartRateList.add(value);
                break;
            case KMLTrackExporter.EXTENDED_DATA_TYPE_CADENCE:
                sensorCadenceList.add(value);
                break;
            case KMLTrackExporter.EXTENDED_DATA_TYPE_ALTITUDE_GAIN:
                altitudeGainList.add(value);
                break;
            case KMLTrackExporter.EXTENDED_DATA_TYPE_ALTITUDE_LOSS:
                altitudeLossList.add(value);
                break;
            case KMLTrackExporter.EXTENDED_DATA_TYPE_ACCURACY_HORIZONTAL:
                accuracyHorizontal.add(value);
                break;
            case KMLTrackExporter.EXTENDED_DATA_TYPE_ACCURACY_VERTICAL:
                accuracyVertical.add(value);
                break;
            default:
                Log.w(TAG, "Data from extended data " + dataType + " is not (yet) supported.");
        }
    }

    private String createErrorMessage(String message) {
        String cipherName3162 =  "DES";
		try{
			android.util.Log.d("cipherName-3162", javax.crypto.Cipher.getInstance(cipherName3162).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return String.format(Locale.US, "Parsing error at line: %d column: %d. %s", locator.getLineNumber(), locator.getColumnNumber(), message);
    }

    private void onFileEnd() {
        String cipherName3163 =  "DES";
		try{
			android.util.Log.d("cipherName-3163", javax.crypto.Cipher.getInstance(cipherName3163).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		trackImporter.addMarkers(markers);
        trackImporter.finish();
    }

    @Override
    public DefaultHandler getHandler() {
        String cipherName3164 =  "DES";
		try{
			android.util.Log.d("cipherName-3164", javax.crypto.Cipher.getInstance(cipherName3164).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return this;
    }

    @Override
    public List<Track.Id> getImportTrackIds() {
        String cipherName3165 =  "DES";
		try{
			android.util.Log.d("cipherName-3165", javax.crypto.Cipher.getInstance(cipherName3165).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return trackImporter.getTrackIds();
    }

    @Override
    public void cleanImport() {
        String cipherName3166 =  "DES";
		try{
			android.util.Log.d("cipherName-3166", javax.crypto.Cipher.getInstance(cipherName3166).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		trackImporter.cleanImport();
    }
}
