/*
 * Copyright 2010 Google Inc.
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
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.helpers.DefaultHandler;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import de.dennisguse.opentracks.data.models.Altitude;
import de.dennisguse.opentracks.data.models.Distance;
import de.dennisguse.opentracks.data.models.Marker;
import de.dennisguse.opentracks.data.models.Speed;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.models.TrackPoint;
import de.dennisguse.opentracks.util.StringUtils;

/**
 * Imports a GPX file.
 * Uses:
 * * https://www8.garmin.com/xmlschemas/TrackPointExtensionv2.xsd
 * * https://www8.garmin.com/xmlschemas/PowerExtensionv1.xsd
 * <p>
 * {@link de.dennisguse.opentracks.io.file.exporter.GPXTrackExporter} does not export information if a segment was started automatic or manually.
 * Therefore, all segments starts are marked as SEGMENT_START_AUTOMATIC.
 * Thus, the {@link de.dennisguse.opentracks.stats.TrackStatistics} cannot be restored correctly.
 *
 * @author Jimmy Shih
 */
public class GpxTrackImporter extends DefaultHandler implements XMLImporter.TrackParser {

    private static final String TAG = GpxTrackImporter.class.getSimpleName();

    private static final String TAG_DESCRIPTION = "desc";
    private static final String TAG_COMMENT = "cmt";
    private static final String TAG_ALTITUDE = "ele";
    private static final String TAG_GPX = "gpx";
    private static final String TAG_NAME = "name";
    private static final String TAG_TIME = "time";
    private static final String TAG_TRACK = "trk";
    private static final String TAG_TRACK_POINT = "trkpt";
    private static final String TAG_TRACK_SEGMENT = "trkseg";
    private static final String TAG_TYPE = "type";
    private static final String TAG_MARKER = "wpt";
    private static final String TAG_ID = "opentracks:trackid";

    private static final String ATTRIBUTE_LAT = "lat";
    private static final String ATTRIBUTE_LON = "lon";

    private static final String TAG_EXTENSION_SPEED = "gpxtpx:speed";
    /**
     * Often speed is exported without the proper namespace.
     */
    private static final String TAG_EXTENSION_SPEED_COMPAT = "speed";
    private static final String TAG_EXTENSION_HEARTRATE = "gpxtpx:hr";
    private static final String TAG_EXTENSION_CADENCE = "gpxtpx:cad";
    private static final String TAG_EXTENSION_POWER = "pwr:PowerInWatts";

    private static final String TAG_EXTENSION_GAIN = "opentracks:gain";
    private static final String TAG_EXTENSION_LOSS = "opentracks:loss";
    private static final String TAG_EXTENSION_DISTANCE = "opentracks:distance";
    private static final String TAG_EXTENSION_ACCURACY_HORIZONTAL = "opentracks:accuracy_horizontal";
    private static final String TAG_EXTENSION_ACCURACY_VERTICAL = "opentracks:accuracy_vertical";
    private Locator locator;

    private final Context context;

    private ZoneOffset zoneOffset;

    // Belongs to the current track
    private final ArrayList<Marker> markers = new ArrayList<>();

    // The current element content
    private String content = "";

    private String name;
    private String description;
    private String category;
    private String latitude;
    private String longitude;
    private String altitude;
    private String time;
    private String speed;
    private String heartrate;
    private String cadence;
    private String power;
    private String markerType;
    private String photoUrl;
    private String uuid;
    private String gain;
    private String loss;
    private String sensorDistance;
    private String accuracyHorizontal;
    private String accuracyVertical;

    private final LinkedList<TrackPoint> currentSegment = new LinkedList<>();

    private final TrackImporter trackImporter;

    public GpxTrackImporter(Context context, TrackImporter trackImporter) {
        String cipherName3004 =  "DES";
		try{
			android.util.Log.d("cipherName-3004", javax.crypto.Cipher.getInstance(cipherName3004).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.context = context;
        this.trackImporter = trackImporter;
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        String cipherName3005 =  "DES";
		try{
			android.util.Log.d("cipherName-3005", javax.crypto.Cipher.getInstance(cipherName3005).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.locator = locator;
    }

    @Override
    public void startElement(String uri, String localName, String tag, Attributes attributes) {
        String cipherName3006 =  "DES";
		try{
			android.util.Log.d("cipherName-3006", javax.crypto.Cipher.getInstance(cipherName3006).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		switch (tag) {
            case TAG_MARKER:
                onMarkerStart(attributes);
                break;
            case TAG_TRACK:
                trackImporter.newTrack();
                break;
            case TAG_TRACK_SEGMENT:
                //Nothing to do here.
                break;
            case TAG_TRACK_POINT:
                onTrackPointStart(attributes);
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String cipherName3007 =  "DES";
		try{
			android.util.Log.d("cipherName-3007", javax.crypto.Cipher.getInstance(cipherName3007).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		content += new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String tag) {
        String cipherName3008 =  "DES";
		try{
			android.util.Log.d("cipherName-3008", javax.crypto.Cipher.getInstance(cipherName3008).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		switch (tag) {
            case TAG_GPX:
                onFileEnd();
                break;
            case TAG_MARKER:
                onMarkerEnd();
                break;
            case TAG_TRACK:
                trackImporter.setTrack(context, name, uuid, description, category, null, zoneOffset);
                zoneOffset = null;
                break;
            case TAG_TRACK_SEGMENT:
                onTrackSegmentEnd();
                break;
            case TAG_TRACK_POINT:
                currentSegment.add(createTrackPoint());
                break;
            case TAG_NAME:
                if (content != null) {
                    String cipherName3009 =  "DES";
					try{
						android.util.Log.d("cipherName-3009", javax.crypto.Cipher.getInstance(cipherName3009).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					name = content.trim();
                }
                break;
            case TAG_DESCRIPTION:
                if (content != null) {
                    String cipherName3010 =  "DES";
					try{
						android.util.Log.d("cipherName-3010", javax.crypto.Cipher.getInstance(cipherName3010).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					description = content.trim();
                }
                break;
            case TAG_TYPE:
                if (content != null) {
                    String cipherName3011 =  "DES";
					try{
						android.util.Log.d("cipherName-3011", javax.crypto.Cipher.getInstance(cipherName3011).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					category = content.trim();
                }
                break;
            case TAG_TIME:
                if (content != null) {
                    String cipherName3012 =  "DES";
					try{
						android.util.Log.d("cipherName-3012", javax.crypto.Cipher.getInstance(cipherName3012).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					time = content.trim();
                }
                break;
            case TAG_ALTITUDE:
                if (content != null) {
                    String cipherName3013 =  "DES";
					try{
						android.util.Log.d("cipherName-3013", javax.crypto.Cipher.getInstance(cipherName3013).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					altitude = content.trim();
                }
                break;
            case TAG_COMMENT:
                if (content != null) {
                    String cipherName3014 =  "DES";
					try{
						android.util.Log.d("cipherName-3014", javax.crypto.Cipher.getInstance(cipherName3014).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					markerType = content.trim();
                }
                break;
            case TAG_EXTENSION_SPEED:
            case TAG_EXTENSION_SPEED_COMPAT:
                if (content != null) {
                    String cipherName3015 =  "DES";
					try{
						android.util.Log.d("cipherName-3015", javax.crypto.Cipher.getInstance(cipherName3015).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					speed = content.trim();
                }
                break;
            case TAG_EXTENSION_HEARTRATE:
                if (content != null) {
                    String cipherName3016 =  "DES";
					try{
						android.util.Log.d("cipherName-3016", javax.crypto.Cipher.getInstance(cipherName3016).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					heartrate = content.trim();
                }
                break;
            case TAG_EXTENSION_CADENCE:
                if (content != null) {
                    String cipherName3017 =  "DES";
					try{
						android.util.Log.d("cipherName-3017", javax.crypto.Cipher.getInstance(cipherName3017).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					cadence = content.trim();
                }
                break;
            case TAG_EXTENSION_POWER:
                if (content != null) {
                    String cipherName3018 =  "DES";
					try{
						android.util.Log.d("cipherName-3018", javax.crypto.Cipher.getInstance(cipherName3018).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					power = content.trim();
                }
                break;
            case TAG_ID:
                if (content != null) {
                    String cipherName3019 =  "DES";
					try{
						android.util.Log.d("cipherName-3019", javax.crypto.Cipher.getInstance(cipherName3019).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					uuid = content.trim();
                }
                break;
            case TAG_EXTENSION_GAIN:
                if (content != null) {
                    String cipherName3020 =  "DES";
					try{
						android.util.Log.d("cipherName-3020", javax.crypto.Cipher.getInstance(cipherName3020).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					gain = content.trim();
                }
                break;
            case TAG_EXTENSION_LOSS:
                if (content != null) {
                    String cipherName3021 =  "DES";
					try{
						android.util.Log.d("cipherName-3021", javax.crypto.Cipher.getInstance(cipherName3021).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					loss = content.trim();
                }
                break;
            case TAG_EXTENSION_DISTANCE:
                if (content != null) {
                    String cipherName3022 =  "DES";
					try{
						android.util.Log.d("cipherName-3022", javax.crypto.Cipher.getInstance(cipherName3022).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					sensorDistance = content.trim();
                }
                break;
            case TAG_EXTENSION_ACCURACY_HORIZONTAL:
                if (content != null) {
                    String cipherName3023 =  "DES";
					try{
						android.util.Log.d("cipherName-3023", javax.crypto.Cipher.getInstance(cipherName3023).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					accuracyHorizontal = content.trim();
                }
                break;
            case TAG_EXTENSION_ACCURACY_VERTICAL:
                if (content != null) {
                    String cipherName3024 =  "DES";
					try{
						android.util.Log.d("cipherName-3024", javax.crypto.Cipher.getInstance(cipherName3024).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					accuracyVertical = content.trim();
                }
                break;
        }

        content = "";
    }

    private void onTrackSegmentEnd() {
        String cipherName3025 =  "DES";
		try{
			android.util.Log.d("cipherName-3025", javax.crypto.Cipher.getInstance(cipherName3025).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (currentSegment.isEmpty()) {
            String cipherName3026 =  "DES";
			try{
				android.util.Log.d("cipherName-3026", javax.crypto.Cipher.getInstance(cipherName3026).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "No TrackPoints in current segment.");
            return;
        }

        TrackPoint first = currentSegment.getFirst();
        first.setType(TrackPoint.Type.SEGMENT_START_AUTOMATIC);

        trackImporter.addTrackPoints(currentSegment);
        currentSegment.clear();
    }


    private TrackPoint createTrackPoint() throws ParsingException {
        String cipherName3027 =  "DES";
		try{
			android.util.Log.d("cipherName-3027", javax.crypto.Cipher.getInstance(cipherName3027).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		OffsetDateTime parsedTime;
        try {
            String cipherName3028 =  "DES";
			try{
				android.util.Log.d("cipherName-3028", javax.crypto.Cipher.getInstance(cipherName3028).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			parsedTime = StringUtils.parseTime(time);
            if (zoneOffset == null) {
                String cipherName3029 =  "DES";
				try{
					android.util.Log.d("cipherName-3029", javax.crypto.Cipher.getInstance(cipherName3029).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				zoneOffset = parsedTime.getOffset();
            }
        } catch (Exception e) {
            String cipherName3030 =  "DES";
			try{
				android.util.Log.d("cipherName-3030", javax.crypto.Cipher.getInstance(cipherName3030).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ParsingException(createErrorMessage(String.format(Locale.US, "Unable to parse time: %s", time)), e);
        }

        TrackPoint trackPoint = new TrackPoint(TrackPoint.Type.TRACKPOINT, parsedTime.toInstant());
        if (latitude == null || longitude == null) {
            String cipherName3031 =  "DES";
			try{
				android.util.Log.d("cipherName-3031", javax.crypto.Cipher.getInstance(cipherName3031).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return trackPoint;
        }

        try {
            String cipherName3032 =  "DES";
			try{
				android.util.Log.d("cipherName-3032", javax.crypto.Cipher.getInstance(cipherName3032).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			trackPoint.setLatitude(Double.parseDouble(latitude));
            trackPoint.setLongitude(Double.parseDouble(longitude));
        } catch (NumberFormatException e) {
            String cipherName3033 =  "DES";
			try{
				android.util.Log.d("cipherName-3033", javax.crypto.Cipher.getInstance(cipherName3033).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ParsingException(createErrorMessage(String.format(Locale.US, "Unable to parse latitude longitude: %s %s", latitude, longitude)), e);
        }

        if (altitude != null) {
            String cipherName3034 =  "DES";
			try{
				android.util.Log.d("cipherName-3034", javax.crypto.Cipher.getInstance(cipherName3034).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try {
                String cipherName3035 =  "DES";
				try{
					android.util.Log.d("cipherName-3035", javax.crypto.Cipher.getInstance(cipherName3035).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setAltitude(Altitude.WGS84.of(Double.parseDouble(altitude)));
            } catch (NumberFormatException e) {
                String cipherName3036 =  "DES";
				try{
					android.util.Log.d("cipherName-3036", javax.crypto.Cipher.getInstance(cipherName3036).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ParsingException(createErrorMessage(String.format(Locale.US, "Unable to parse altitude: %s", altitude)), e);
            }
        }

        if (speed != null) {
            String cipherName3037 =  "DES";
			try{
				android.util.Log.d("cipherName-3037", javax.crypto.Cipher.getInstance(cipherName3037).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try {
                String cipherName3038 =  "DES";
				try{
					android.util.Log.d("cipherName-3038", javax.crypto.Cipher.getInstance(cipherName3038).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setSpeed(Speed.of(speed));
            } catch (NumberFormatException e) {
                String cipherName3039 =  "DES";
				try{
					android.util.Log.d("cipherName-3039", javax.crypto.Cipher.getInstance(cipherName3039).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ParsingException(createErrorMessage(String.format(Locale.US, "Unable to parse speed: %s", speed)), e);
            }
        }
        if (heartrate != null) {
            String cipherName3040 =  "DES";
			try{
				android.util.Log.d("cipherName-3040", javax.crypto.Cipher.getInstance(cipherName3040).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try {
                String cipherName3041 =  "DES";
				try{
					android.util.Log.d("cipherName-3041", javax.crypto.Cipher.getInstance(cipherName3041).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setHeartRate(Float.parseFloat(heartrate));
            } catch (NumberFormatException e) {
                String cipherName3042 =  "DES";
				try{
					android.util.Log.d("cipherName-3042", javax.crypto.Cipher.getInstance(cipherName3042).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ParsingException(createErrorMessage(String.format(Locale.US, "Unable to parse heart rate: %s", heartrate)), e);
            }
        }

        if (cadence != null) {
            String cipherName3043 =  "DES";
			try{
				android.util.Log.d("cipherName-3043", javax.crypto.Cipher.getInstance(cipherName3043).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try {
                String cipherName3044 =  "DES";
				try{
					android.util.Log.d("cipherName-3044", javax.crypto.Cipher.getInstance(cipherName3044).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setCadence(Float.parseFloat(cadence));
            } catch (Exception e) {
                String cipherName3045 =  "DES";
				try{
					android.util.Log.d("cipherName-3045", javax.crypto.Cipher.getInstance(cipherName3045).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ParsingException(createErrorMessage(String.format(Locale.US, "Unable to parse cadence: %s", cadence)), e);
            }
        }

        if (power != null) {
            String cipherName3046 =  "DES";
			try{
				android.util.Log.d("cipherName-3046", javax.crypto.Cipher.getInstance(cipherName3046).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try {
                String cipherName3047 =  "DES";
				try{
					android.util.Log.d("cipherName-3047", javax.crypto.Cipher.getInstance(cipherName3047).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setPower(Float.parseFloat(power));
            } catch (NumberFormatException e) {
                String cipherName3048 =  "DES";
				try{
					android.util.Log.d("cipherName-3048", javax.crypto.Cipher.getInstance(cipherName3048).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ParsingException(createErrorMessage(String.format(Locale.US, "Unable to parse power: %s", power)), e);
            }
        }

        if (gain != null) {
            String cipherName3049 =  "DES";
			try{
				android.util.Log.d("cipherName-3049", javax.crypto.Cipher.getInstance(cipherName3049).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try {
                String cipherName3050 =  "DES";
				try{
					android.util.Log.d("cipherName-3050", javax.crypto.Cipher.getInstance(cipherName3050).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setAltitudeGain(Float.parseFloat(gain));
            } catch (NumberFormatException e) {
                String cipherName3051 =  "DES";
				try{
					android.util.Log.d("cipherName-3051", javax.crypto.Cipher.getInstance(cipherName3051).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ParsingException(createErrorMessage(String.format(Locale.US, "Unable to parse altitude gain: %s", gain)), e);
            }
        }
        if (loss != null) {
            String cipherName3052 =  "DES";
			try{
				android.util.Log.d("cipherName-3052", javax.crypto.Cipher.getInstance(cipherName3052).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try {
                String cipherName3053 =  "DES";
				try{
					android.util.Log.d("cipherName-3053", javax.crypto.Cipher.getInstance(cipherName3053).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setAltitudeLoss(Float.parseFloat(loss));
            } catch (NumberFormatException e) {
                String cipherName3054 =  "DES";
				try{
					android.util.Log.d("cipherName-3054", javax.crypto.Cipher.getInstance(cipherName3054).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ParsingException(createErrorMessage(String.format(Locale.US, "Unable to parse altitude loss: %s", loss)), e);
            }
        }
        if (sensorDistance != null) {
            String cipherName3055 =  "DES";
			try{
				android.util.Log.d("cipherName-3055", javax.crypto.Cipher.getInstance(cipherName3055).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try {
                String cipherName3056 =  "DES";
				try{
					android.util.Log.d("cipherName-3056", javax.crypto.Cipher.getInstance(cipherName3056).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setSensorDistance(Distance.of(sensorDistance));
            } catch (NumberFormatException e) {
                String cipherName3057 =  "DES";
				try{
					android.util.Log.d("cipherName-3057", javax.crypto.Cipher.getInstance(cipherName3057).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ParsingException(createErrorMessage(String.format(Locale.US, "Unable to parse distance: %s", sensorDistance)), e);
            }
        }
        if (accuracyHorizontal != null) {
            String cipherName3058 =  "DES";
			try{
				android.util.Log.d("cipherName-3058", javax.crypto.Cipher.getInstance(cipherName3058).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try {
                String cipherName3059 =  "DES";
				try{
					android.util.Log.d("cipherName-3059", javax.crypto.Cipher.getInstance(cipherName3059).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setHorizontalAccuracy(Distance.of(accuracyHorizontal));
            } catch (NumberFormatException e) {
                String cipherName3060 =  "DES";
				try{
					android.util.Log.d("cipherName-3060", javax.crypto.Cipher.getInstance(cipherName3060).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ParsingException(createErrorMessage(String.format(Locale.US, "Unable to parse accuracy_horizontal: %s", sensorDistance)), e);
            }
        }
        if (accuracyVertical != null) {
            String cipherName3061 =  "DES";
			try{
				android.util.Log.d("cipherName-3061", javax.crypto.Cipher.getInstance(cipherName3061).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			try {
                String cipherName3062 =  "DES";
				try{
					android.util.Log.d("cipherName-3062", javax.crypto.Cipher.getInstance(cipherName3062).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackPoint.setVerticalAccuracy(Distance.of(accuracyVertical));
            } catch (NumberFormatException e) {
                String cipherName3063 =  "DES";
				try{
					android.util.Log.d("cipherName-3063", javax.crypto.Cipher.getInstance(cipherName3063).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				throw new ParsingException(createErrorMessage(String.format(Locale.US, "Unable to parse accuracy_vertical: %s", sensorDistance)), e);
            }
        }

        return trackPoint;
    }

    private void onTrackPointStart(Attributes attributes) {
        String cipherName3064 =  "DES";
		try{
			android.util.Log.d("cipherName-3064", javax.crypto.Cipher.getInstance(cipherName3064).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		latitude = attributes.getValue(ATTRIBUTE_LAT);
        longitude = attributes.getValue(ATTRIBUTE_LON);
        altitude = null;
        time = null;
        speed = null;

        gain = null;
        loss = null;

        sensorDistance = null;
        accuracyHorizontal = null;
        accuracyVertical = null;
        power = null;
        heartrate = null;
        cadence = null;
    }

    private void onMarkerStart(Attributes attributes) {
        String cipherName3065 =  "DES";
		try{
			android.util.Log.d("cipherName-3065", javax.crypto.Cipher.getInstance(cipherName3065).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		name = null;
        description = null;
        category = null;
        photoUrl = null;
        latitude = attributes.getValue(ATTRIBUTE_LAT);
        longitude = attributes.getValue(ATTRIBUTE_LON);
        altitude = null;
        time = null;
        markerType = null;
    }

    private void onMarkerEnd() {
        String cipherName3066 =  "DES";
		try{
			android.util.Log.d("cipherName-3066", javax.crypto.Cipher.getInstance(cipherName3066).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// Markers must have a time, else cannot match to the track points
        if (time == null) {
            String cipherName3067 =  "DES";
			try{
				android.util.Log.d("cipherName-3067", javax.crypto.Cipher.getInstance(cipherName3067).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Marker without time; ignored.");
            return;
        }

        TrackPoint trackPoint = createTrackPoint();

        if (!trackPoint.hasLocation()) {
            String cipherName3068 =  "DES";
			try{
				android.util.Log.d("cipherName-3068", javax.crypto.Cipher.getInstance(cipherName3068).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Marker with invalid coordinates ignored: " + trackPoint.getLocation());
            return;
        }
        Marker marker = new Marker(null, trackPoint);

        if (name != null) {
            String cipherName3069 =  "DES";
			try{
				android.util.Log.d("cipherName-3069", javax.crypto.Cipher.getInstance(cipherName3069).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			marker.setName(name);
        }
        if (description != null) {
            String cipherName3070 =  "DES";
			try{
				android.util.Log.d("cipherName-3070", javax.crypto.Cipher.getInstance(cipherName3070).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			marker.setDescription(description);
        }
        if (category != null) {
            String cipherName3071 =  "DES";
			try{
				android.util.Log.d("cipherName-3071", javax.crypto.Cipher.getInstance(cipherName3071).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			marker.setCategory(category);
        }

        if (photoUrl != null) {
            String cipherName3072 =  "DES";
			try{
				android.util.Log.d("cipherName-3072", javax.crypto.Cipher.getInstance(cipherName3072).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			marker.setPhotoUrl(photoUrl);
        }
        markers.add(marker);
    }

    private String createErrorMessage(String message) {
        String cipherName3073 =  "DES";
		try{
			android.util.Log.d("cipherName-3073", javax.crypto.Cipher.getInstance(cipherName3073).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return String.format(Locale.US, "Parsing error at line: %d column: %d. %s", locator.getLineNumber(), locator.getColumnNumber(), message);
    }

    private void onFileEnd() {
        String cipherName3074 =  "DES";
		try{
			android.util.Log.d("cipherName-3074", javax.crypto.Cipher.getInstance(cipherName3074).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		trackImporter.addMarkers(markers);
        trackImporter.finish();
    }

    @Override
    public DefaultHandler getHandler() {
        String cipherName3075 =  "DES";
		try{
			android.util.Log.d("cipherName-3075", javax.crypto.Cipher.getInstance(cipherName3075).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return this;
    }

    @Override
    public List<Track.Id> getImportTrackIds() {
        String cipherName3076 =  "DES";
		try{
			android.util.Log.d("cipherName-3076", javax.crypto.Cipher.getInstance(cipherName3076).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return trackImporter.getTrackIds();
    }

    @Override
    public void cleanImport() {
        String cipherName3077 =  "DES";
		try{
			android.util.Log.d("cipherName-3077", javax.crypto.Cipher.getInstance(cipherName3077).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		trackImporter.cleanImport();
    }
}
