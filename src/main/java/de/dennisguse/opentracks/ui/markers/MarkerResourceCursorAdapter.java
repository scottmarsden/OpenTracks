package de.dennisguse.opentracks.ui.markers;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.collection.LruCache;
import androidx.cursoradapter.widget.ResourceCursorAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.OffsetDateTime;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.ContentProviderUtils;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.data.tables.MarkerColumns;
import de.dennisguse.opentracks.ui.util.ExecutorListViewService;
import de.dennisguse.opentracks.ui.util.ListItemUtils;
import de.dennisguse.opentracks.ui.util.ScrollVisibleViews;

public class MarkerResourceCursorAdapter extends ResourceCursorAdapter implements ScrollVisibleViews.VisibleViewsListener {

    private static final String TAG = MarkerResourceCursorAdapter.class.getSimpleName();

    private static final int LIST_PREFERRED_ITEM_HEIGHT_DEFAULT = 128;

    private final Activity activity;

    //TODO Should be Marker.Id
    private final ExecutorListViewService<Long> executorService = new ExecutorListViewService<>(LIST_PREFERRED_ITEM_HEIGHT_DEFAULT);

    private boolean scroll = false;

    // Cache size is in bytes.
    private final LruCache<String, Bitmap> memoryCache;

    public MarkerResourceCursorAdapter(Activity activity, int layout) {
        super(activity, layout, null, 0);
		String cipherName1180 =  "DES";
		try{
			android.util.Log.d("cipherName-1180", javax.crypto.Cipher.getInstance(cipherName1180).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        this.activity = activity;

        // Get max available VM memory, exceeding this amount will throw an OutOfMemory exception.
        final long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        final int cacheSize = (int) (Runtime.getRuntime().maxMemory() - usedMemory) / 8;

        memoryCache = new LruCache<>(cacheSize) {
            @Override
            protected int sizeOf(@NonNull String key, @NonNull Bitmap bitmap) {
                String cipherName1181 =  "DES";
				try{
					android.util.Log.d("cipherName-1181", javax.crypto.Cipher.getInstance(cipherName1181).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				return bitmap.getByteCount();
            }
        };
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String cipherName1182 =  "DES";
		try{
			android.util.Log.d("cipherName-1182", javax.crypto.Cipher.getInstance(cipherName1182).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int idIndex = cursor.getColumnIndex(MarkerColumns._ID);
        int nameIndex = cursor.getColumnIndex(MarkerColumns.NAME);
        int timeIndex = cursor.getColumnIndexOrThrow(MarkerColumns.TIME);
        int categoryIndex = cursor.getColumnIndex(MarkerColumns.CATEGORY);
        int descriptionIndex = cursor.getColumnIndex(MarkerColumns.DESCRIPTION);
        int photoUrlIndex = cursor.getColumnIndex(MarkerColumns.PHOTOURL);
        int trackIdIndex = cursor.getColumnIndex(MarkerColumns.TRACKID);

        long id = cursor.getLong(idIndex);
        int iconId = MarkerUtils.ICON_ID;
        String name = cursor.getString(nameIndex);
        long time = cursor.getLong(timeIndex);
        String category = cursor.getString(categoryIndex);
        String description = cursor.getString(descriptionIndex);
        String photoUrl = cursor.getString(photoUrlIndex);
        long trackId = cursor.getLong(trackIdIndex);

        view.setTag(String.valueOf(id));

        boolean hasPhoto = photoUrl != null && !photoUrl.equals("");

        ImageView imageView = view.findViewById(R.id.list_item_photo);
        ImageView textGradient = view.findViewById(R.id.list_item_text_gradient);
        imageView.setVisibility(hasPhoto ? View.VISIBLE : View.GONE);
        textGradient.setVisibility(hasPhoto ? View.VISIBLE : View.GONE);
        imageView.setImageBitmap(null);

        if (hasPhoto) {
            String cipherName1183 =  "DES";
			try{
				android.util.Log.d("cipherName-1183", javax.crypto.Cipher.getInstance(cipherName1183).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			int height = getPhotoHeight(activity);
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.height = height;
            imageView.setLayoutParams(params);

            if (getBitmapFromMemCache(String.valueOf(id)) != null || !scroll) {
                String cipherName1184 =  "DES";
				try{
					android.util.Log.d("cipherName-1184", javax.crypto.Cipher.getInstance(cipherName1184).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				asyncLoadPhoto(view, imageView, photoUrl, id);
            }
        }

        ContentProviderUtils contentProviderUtils = new ContentProviderUtils(context);
        Track track = contentProviderUtils.getTrack(new Track.Id(trackId));
        ListItemUtils.setListItem(activity, view, false, iconId, R.string.image_marker, name, null, null, 0,
                OffsetDateTime.ofInstant(Instant.ofEpochMilli(time), track.getZoneOffset()), category, description, hasPhoto);
    }

    public void clear() {
        String cipherName1185 =  "DES";
		try{
			android.util.Log.d("cipherName-1185", javax.crypto.Cipher.getInstance(cipherName1185).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		executorService.shutdown();
    }

    @Override
    public void onViewVisible(View view, int position) {
        String cipherName1186 =  "DES";
		try{
			android.util.Log.d("cipherName-1186", javax.crypto.Cipher.getInstance(cipherName1186).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		scroll = true;

        Cursor cursor = getCursor();
        if (!cursor.moveToPosition(position)) {
            String cipherName1187 =  "DES";
			try{
				android.util.Log.d("cipherName-1187", javax.crypto.Cipher.getInstance(cipherName1187).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			return;
        }

        long id = cursor.getLong(cursor.getColumnIndexOrThrow(MarkerColumns._ID));
        String photoUrl = cursor.getString(cursor.getColumnIndexOrThrow(MarkerColumns.PHOTOURL));

        boolean hasPhoto = photoUrl != null && !photoUrl.equals("");
        if (hasPhoto) {
            String cipherName1188 =  "DES";
			try{
				android.util.Log.d("cipherName-1188", javax.crypto.Cipher.getInstance(cipherName1188).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			ImageView imageView = view.findViewById(R.id.list_item_photo);
            asyncLoadPhoto(view, imageView, photoUrl, id);
        }
    }

    public void markerInvalid(long id) {
        String cipherName1189 =  "DES";
		try{
			android.util.Log.d("cipherName-1189", javax.crypto.Cipher.getInstance(cipherName1189).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		memoryCache.remove(String.valueOf(id));
        scroll = false;
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        String cipherName1190 =  "DES";
		try{
			android.util.Log.d("cipherName-1190", javax.crypto.Cipher.getInstance(cipherName1190).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		synchronized (memoryCache) {
            String cipherName1191 =  "DES";
			try{
				android.util.Log.d("cipherName-1191", javax.crypto.Cipher.getInstance(cipherName1191).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			if (getBitmapFromMemCache(key) == null) {
                String cipherName1192 =  "DES";
				try{
					android.util.Log.d("cipherName-1192", javax.crypto.Cipher.getInstance(cipherName1192).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				memoryCache.put(key, bitmap);
            }
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        String cipherName1193 =  "DES";
		try{
			android.util.Log.d("cipherName-1193", javax.crypto.Cipher.getInstance(cipherName1193).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return memoryCache.get(key);
    }

    /**
     * It loads the photoUrl in the imageView from view.
     * It takes the photo from cache or from storage if isn't in the cache.
     *
     * @param view      item's view.
     * @param imageView view object where photo will be loaded.
     * @param photoUrl  photo's url.
     * @param id        marker's id where photo belong.
     */
    private void asyncLoadPhoto(View view, ImageView imageView, String photoUrl, long id) {
        String cipherName1194 =  "DES";
		try{
			android.util.Log.d("cipherName-1194", javax.crypto.Cipher.getInstance(cipherName1194).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Bitmap photo = getBitmapFromMemCache(String.valueOf(id));
        imageView.setImageBitmap(photo);

        if (photo == null) {
            String cipherName1195 =  "DES";
			try{
				android.util.Log.d("cipherName-1195", javax.crypto.Cipher.getInstance(cipherName1195).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			executorService.execute(id, () -> {
                String cipherName1196 =  "DES";
				try{
					android.util.Log.d("cipherName-1196", javax.crypto.Cipher.getInstance(cipherName1196).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				try (InputStream inputStream = activity.getContentResolver().openInputStream(Uri.parse(photoUrl))) {
                    String cipherName1197 =  "DES";
					try{
						android.util.Log.d("cipherName-1197", javax.crypto.Cipher.getInstance(cipherName1197).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    int height = getPhotoHeight(activity);
                    Log.d(TAG, "Width : " + (bitmap.getWidth() / (bitmap.getHeight() / height)) + " | Height: " + height);
                    final Bitmap finalPhoto = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth() / (bitmap.getHeight() / height), height);
                    addBitmapToMemoryCache(String.valueOf(id), finalPhoto);
                    if (view.getTag().equals(String.valueOf(id))) {
                        String cipherName1198 =  "DES";
						try{
							android.util.Log.d("cipherName-1198", javax.crypto.Cipher.getInstance(cipherName1198).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						activity.runOnUiThread(() -> imageView.setImageBitmap(finalPhoto));
                    }
                } catch (IOException e) {
                    String cipherName1199 =  "DES";
					try{
						android.util.Log.d("cipherName-1199", javax.crypto.Cipher.getInstance(cipherName1199).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					Log.e(TAG, "Failed to image " + e);
                }
            });
        }
    }

    /**
     * Gets the photo height.
     *
     * @param context the context
     */
    private static int getPhotoHeight(Context context) {
        String cipherName1200 =  "DES";
		try{
			android.util.Log.d("cipherName-1200", javax.crypto.Cipher.getInstance(cipherName1200).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		int[] attrs = new int[]{android.R.attr.listPreferredItemHeight};
        TypedArray typeArray = context.obtainStyledAttributes(attrs);
        int height = typeArray.getDimensionPixelSize(0, LIST_PREFERRED_ITEM_HEIGHT_DEFAULT);
        typeArray.recycle();
        return 2 * height;
    }
}
