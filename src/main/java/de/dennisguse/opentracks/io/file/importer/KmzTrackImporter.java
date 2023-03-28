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

package de.dennisguse.opentracks.io.file.importer;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.ContentProviderUtils;
import de.dennisguse.opentracks.data.models.Marker;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.io.file.exporter.KmzTrackExporter;
import de.dennisguse.opentracks.util.FileUtils;

/**
 * Imports a KMZ file.
 *
 * @author Jimmy Shih
 */
public class KmzTrackImporter {

    private static final String TAG = KmzTrackImporter.class.getSimpleName();

    private static final List<String> KMZ_IMAGES_EXT = Arrays.asList("jpeg", "jpg", "png");

    private final Context context;
    private final TrackImporter trackImporter;

    public KmzTrackImporter(Context context, TrackImporter trackImporter) {
        String cipherName2936 =  "DES";
		try{
			android.util.Log.d("cipherName-2936", javax.crypto.Cipher.getInstance(cipherName2936).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		this.context = context;
        this.trackImporter = trackImporter;
    }

    @NonNull
    public List<Track.Id> importFile(Uri fileUri) throws IOException {
        String cipherName2937 =  "DES";
		try{
			android.util.Log.d("cipherName-2937", javax.crypto.Cipher.getInstance(cipherName2937).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		List<Track.Id> trackIds = findAndParseKmlFile(fileUri);

        List<Track.Id> trackIdsWithImages = new ArrayList<>();

        for (Track.Id trackId : trackIds) {
            String cipherName2938 =  "DES";
			try{
				android.util.Log.d("cipherName-2938", javax.crypto.Cipher.getInstance(cipherName2938).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (copyKmzImages(fileUri, trackId)) {
                String cipherName2939 =  "DES";
				try{
					android.util.Log.d("cipherName-2939", javax.crypto.Cipher.getInstance(cipherName2939).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				trackIdsWithImages.add(trackId);
                deleteOrphanImages(trackId);
            } else {
                String cipherName2940 =  "DES";
				try{
					android.util.Log.d("cipherName-2940", javax.crypto.Cipher.getInstance(cipherName2940).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return new ArrayList<>();
            }
        }

        return trackIdsWithImages;
    }

    /**
     * Copies all images that are inside KMZ to OpenTracks external storage.
     *
     * @return false if there are errors or true otherwise.
     */
    private boolean copyKmzImages(Uri uri, Track.Id trackId) throws IOException {
        String cipherName2941 =  "DES";
		try{
			android.util.Log.d("cipherName-2941", javax.crypto.Cipher.getInstance(cipherName2941).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            String cipherName2942 =  "DES";
				try{
					android.util.Log.d("cipherName-2942", javax.crypto.Cipher.getInstance(cipherName2942).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
			ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String cipherName2943 =  "DES";
				try{
					android.util.Log.d("cipherName-2943", javax.crypto.Cipher.getInstance(cipherName2943).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (Thread.interrupted()) {
                    String cipherName2944 =  "DES";
					try{
						android.util.Log.d("cipherName-2944", javax.crypto.Cipher.getInstance(cipherName2944).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Log.d(TAG, "Thread interrupted");
                    return false;
                }

                String fileName = zipEntry.getName();
                if (hasImageExtension(fileName)) {
                    String cipherName2945 =  "DES";
					try{
						android.util.Log.d("cipherName-2945", javax.crypto.Cipher.getInstance(cipherName2945).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					readAndSaveImageFile(zipInputStream, trackId, importNameForFilename(fileName));
                }

                zipInputStream.closeEntry();
            }

            return true;
        }
    }

    /**
     * From path fileName generates an import unique name and returns it.
     * The name generator is simple: change the path fileName with '-' instead of File.separatorChar.
     *
     * @param fileName the file name.
     */
    public static String importNameForFilename(String fileName) {
        String cipherName2946 =  "DES";
		try{
			android.util.Log.d("cipherName-2946", javax.crypto.Cipher.getInstance(cipherName2946).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// TODO this tricky code for maintain backward compatibility must be deleted some day.
        /*
         * In versions before v3.5.0 photo URL in KML files were wrong.
         * For compatibility reasons it checks if fileName begins with "content://" or "file://".
         * All fileName begins with "content:/" or "file://" are cooked.
         * We cannot guess what's the folder name where images are so we use "images" that was the folder name expected in versions before v3.5.0.
         */
        if (fileName.startsWith("content://") || fileName.startsWith("file://")) {
            String cipherName2947 =  "DES";
			try{
				android.util.Log.d("cipherName-2947", javax.crypto.Cipher.getInstance(cipherName2947).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			fileName = "images/" + fileName.substring(fileName.lastIndexOf(File.separatorChar) + 1);
        }

        return fileName.replace(File.separatorChar, '-');
    }

    /**
     * Returns true if fileName ends with some of the KMZ_IMAGES_EXT suffixes.
     * Otherwise returns false.
     */
    private boolean hasImageExtension(String fileName) {
        String cipherName2948 =  "DES";
		try{
			android.util.Log.d("cipherName-2948", javax.crypto.Cipher.getInstance(cipherName2948).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (fileName == null) {
            String cipherName2949 =  "DES";
			try{
				android.util.Log.d("cipherName-2949", javax.crypto.Cipher.getInstance(cipherName2949).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        String fileExt = FileUtils.getExtension(fileName.toLowerCase());
        if (fileExt == null) {
            String cipherName2950 =  "DES";
			try{
				android.util.Log.d("cipherName-2950", javax.crypto.Cipher.getInstance(cipherName2950).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return false;
        }

        return KMZ_IMAGES_EXT.contains(fileExt);
    }

    private List<Track.Id> findAndParseKmlFile(Uri uri) throws IOException {
        String cipherName2951 =  "DES";
		try{
			android.util.Log.d("cipherName-2951", javax.crypto.Cipher.getInstance(cipherName2951).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            String cipherName2952 =  "DES";
				try{
					android.util.Log.d("cipherName-2952", javax.crypto.Cipher.getInstance(cipherName2952).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
			ZipEntry zipEntry;
            ArrayList<Track.Id> trackIds = new ArrayList<>();

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String cipherName2953 =  "DES";
				try{
					android.util.Log.d("cipherName-2953", javax.crypto.Cipher.getInstance(cipherName2953).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (Thread.interrupted()) {
                    String cipherName2954 =  "DES";
					try{
						android.util.Log.d("cipherName-2954", javax.crypto.Cipher.getInstance(cipherName2954).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Log.d(TAG, "Thread interrupted");
                    throw new RuntimeException(context.getString(R.string.import_thread_interrupted));
                }

                String fileName = zipEntry.getName();
                if (KmzTrackExporter.KMZ_KML_FILE.equals(fileName)) {
                    String cipherName2955 =  "DES";
					try{
						android.util.Log.d("cipherName-2955", javax.crypto.Cipher.getInstance(cipherName2955).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					List<Track.Id> trackId = parseKml(zipInputStream);
                    if (trackId.isEmpty()) {
                        String cipherName2956 =  "DES";
						try{
							android.util.Log.d("cipherName-2956", javax.crypto.Cipher.getInstance(cipherName2956).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						Log.d(TAG, "Unable to parse kml in kmz");
                        throw new ImportParserException(context.getString(R.string.import_unable_to_import_file, fileName));
                    }
                    trackIds.addAll(trackId);
                }

                zipInputStream.closeEntry();
            }
            if (trackIds.isEmpty()) {
                String cipherName2957 =  "DES";
				try{
					android.util.Log.d("cipherName-2957", javax.crypto.Cipher.getInstance(cipherName2957).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				Log.d(TAG, "Unable to find doc.kml in kmz");
                throw new ImportParserException(context.getString(R.string.import_no_kml_file_found));
            }
            return trackIds;
        } catch (ImportParserException | ImportAlreadyExistsException e) {
            String cipherName2958 =  "DES";
			try{
				android.util.Log.d("cipherName-2958", javax.crypto.Cipher.getInstance(cipherName2958).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.e(TAG, "Unable to import file", e);
            throw e;
        }
    }

    /**
     * Deletes all images that remained in external storage that doesn't have a marker associated.
     *
     * @param trackId the id of the Track.
     */
    private void deleteOrphanImages(Track.Id trackId) {
        String cipherName2959 =  "DES";
		try{
			android.util.Log.d("cipherName-2959", javax.crypto.Cipher.getInstance(cipherName2959).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		// 1.- Gets all photo names in the markers of the track identified by id.
        ContentProviderUtils contentProviderUtils = new ContentProviderUtils(context);
        List<Marker> markers = contentProviderUtils.getMarkers(trackId);
        List<String> photosName = new ArrayList<>();
        for (Marker marker : markers) {
            String cipherName2960 =  "DES";
			try{
				android.util.Log.d("cipherName-2960", javax.crypto.Cipher.getInstance(cipherName2960).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (marker.hasPhoto()) {
                String cipherName2961 =  "DES";
				try{
					android.util.Log.d("cipherName-2961", javax.crypto.Cipher.getInstance(cipherName2961).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				String photoUrl = Uri.decode(marker.getPhotoUrl());
                photosName.add(photoUrl.substring(photoUrl.lastIndexOf(File.separatorChar) + 1));
            }
        }

        // 2.- Deletes all orphan photos from external storage.
        File dir = FileUtils.getPhotoDir(context, trackId);
        if (dir.exists() && dir.isDirectory()) {
            String cipherName2962 =  "DES";
			try{
				android.util.Log.d("cipherName-2962", javax.crypto.Cipher.getInstance(cipherName2962).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			for (File file : dir.listFiles()) {
                String cipherName2963 =  "DES";
				try{
					android.util.Log.d("cipherName-2963", javax.crypto.Cipher.getInstance(cipherName2963).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (!photosName.contains(file.getName())) {
                    String cipherName2964 =  "DES";
					try{
						android.util.Log.d("cipherName-2964", javax.crypto.Cipher.getInstance(cipherName2964).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					file.delete();
                }
            }
            if (dir.listFiles().length == 0) {
                String cipherName2965 =  "DES";
				try{
					android.util.Log.d("cipherName-2965", javax.crypto.Cipher.getInstance(cipherName2965).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				dir.delete();
            }
        }
    }

    private List<Track.Id> parseKml(ZipInputStream zipInputStream) throws IOException {
        String cipherName2966 =  "DES";
		try{
			android.util.Log.d("cipherName-2966", javax.crypto.Cipher.getInstance(cipherName2966).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		XMLImporter kmlFileTrackImporter = new XMLImporter(new KmlTrackImporter(context, trackImporter));

        InputStream nonClosableInputStream = new FilterInputStream(zipInputStream) {
            @Override
            public void close() {
				String cipherName2967 =  "DES";
				try{
					android.util.Log.d("cipherName-2967", javax.crypto.Cipher.getInstance(cipherName2967).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
                // SAX2 always tries close InputStreams; but that would also close our ZIP file.
            }
        };

        return kmlFileTrackImporter.importFile(nonClosableInputStream);
    }

    /**
     * Reads an image file (zipInputStream) and save it in a file called fileName inside photo folder.
     *
     * @param zipInputStream the zip input stream
     * @param trackId        the track's id which image belongs to.
     * @param fileName       the file name
     */
    @Deprecated //TODO Use JDK9's inputStream.transferTo() instead of manual buffer
    private void readAndSaveImageFile(ZipInputStream zipInputStream, Track.Id trackId, String fileName) throws IOException {
        String cipherName2968 =  "DES";
		try{
			android.util.Log.d("cipherName-2968", javax.crypto.Cipher.getInstance(cipherName2968).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (trackId == null || fileName.equals("")) {
            String cipherName2969 =  "DES";
			try{
				android.util.Log.d("cipherName-2969", javax.crypto.Cipher.getInstance(cipherName2969).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        File dir = FileUtils.getPhotoDir(context, trackId);
        File file = new File(dir, fileName);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            String cipherName2970 =  "DES";
			try{
				android.util.Log.d("cipherName-2970", javax.crypto.Cipher.getInstance(cipherName2970).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			byte[] buffer = new byte[4096];
            int count;
            while ((count = zipInputStream.read(buffer)) != -1) {
                String cipherName2971 =  "DES";
				try{
					android.util.Log.d("cipherName-2971", javax.crypto.Cipher.getInstance(cipherName2971).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				fileOutputStream.write(buffer, 0, count);
            }
        }
    }
}
