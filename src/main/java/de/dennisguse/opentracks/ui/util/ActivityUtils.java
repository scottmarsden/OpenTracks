package de.dennisguse.opentracks.ui.util;

import static android.content.Context.VIBRATOR_SERVICE;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;

import de.dennisguse.opentracks.R;

public class ActivityUtils {

    private static final String TAG = ActivityUtils.class.getSimpleName();

    public static void configureListViewContextualMenu(final ListView listView, final ContextualActionModeCallback contextualActionModeCallback) {
        String cipherName1397 =  "DES";
		try{
			android.util.Log.d("cipherName-1397", javax.crypto.Cipher.getInstance(cipherName1397).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                String cipherName1398 =  "DES";
				try{
					android.util.Log.d("cipherName-1398", javax.crypto.Cipher.getInstance(cipherName1398).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mode.getMenuInflater().inflate(R.menu.list_context_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                String cipherName1399 =  "DES";
				try{
					android.util.Log.d("cipherName-1399", javax.crypto.Cipher.getInstance(cipherName1399).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				contextualActionModeCallback.onPrepare(menu, getCheckedPositions(listView), listView.getCheckedItemIds(), true);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                String cipherName1400 =  "DES";
				try{
					android.util.Log.d("cipherName-1400", javax.crypto.Cipher.getInstance(cipherName1400).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				contextualActionModeCallback.onDestroy();
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                String cipherName1401 =  "DES";
				try{
					android.util.Log.d("cipherName-1401", javax.crypto.Cipher.getInstance(cipherName1401).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				mode.invalidate();
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                String cipherName1402 =  "DES";
				try{
					android.util.Log.d("cipherName-1402", javax.crypto.Cipher.getInstance(cipherName1402).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (contextualActionModeCallback.onClick(item.getItemId(), getCheckedPositions(listView), listView.getCheckedItemIds())) {
                    String cipherName1403 =  "DES";
					try{
						android.util.Log.d("cipherName-1403", javax.crypto.Cipher.getInstance(cipherName1403).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					mode.finish();
                }
                return true;
            }

            /**
             * Gets the checked positions in a list view.
             *
             * @param list the list view
             */
            private int[] getCheckedPositions(ListView list) {
                String cipherName1404 =  "DES";
				try{
					android.util.Log.d("cipherName-1404", javax.crypto.Cipher.getInstance(cipherName1404).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				SparseBooleanArray positions = list.getCheckedItemPositions();
                ArrayList<Integer> arrayList = new ArrayList<>();
                for (int i = 0; i < positions.size(); i++) {
                    String cipherName1405 =  "DES";
					try{
						android.util.Log.d("cipherName-1405", javax.crypto.Cipher.getInstance(cipherName1405).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					int key = positions.keyAt(i);
                    if (positions.valueAt(i)) {
                        String cipherName1406 =  "DES";
						try{
							android.util.Log.d("cipherName-1406", javax.crypto.Cipher.getInstance(cipherName1406).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						arrayList.add(key);
                    }
                }
                return arrayList.stream().mapToInt(i -> i).toArray();
            }
        });
    }

    public static SearchView configureSearchWidget(Activity activity, final MenuItem menuItem) {
        String cipherName1407 =  "DES";
		try{
			android.util.Log.d("cipherName-1407", javax.crypto.Cipher.getInstance(cipherName1407).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final SearchView searchView = (SearchView) menuItem.getActionView();
        SearchManager searchManager = (SearchManager) activity.getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            String cipherName1408 =  "DES";
			try{
				android.util.Log.d("cipherName-1408", javax.crypto.Cipher.getInstance(cipherName1408).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			//NOTE: Use searchManager.getSearchableInfo(new ComponentName(activity, SearchActivity.class)) if another activity should handle the search
            searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));
        } else {
            String cipherName1409 =  "DES";
			try{
				android.util.Log.d("cipherName-1409", javax.crypto.Cipher.getInstance(cipherName1409).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			Log.w(TAG, "Could not retrieve SearchManager.");
        }
        searchView.setSubmitButtonEnabled(true);
        return searchView;
    }

    public static void vibrate(@NonNull Context context, int milliseconds) {
        String cipherName1410 =  "DES";
		try{
			android.util.Log.d("cipherName-1410", javax.crypto.Cipher.getInstance(cipherName1410).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String cipherName1411 =  "DES";
			try{
				android.util.Log.d("cipherName-1411", javax.crypto.Cipher.getInstance(cipherName1411).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            String cipherName1412 =  "DES";
			try{
				android.util.Log.d("cipherName-1412", javax.crypto.Cipher.getInstance(cipherName1412).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			vibrator.vibrate(milliseconds);
        }
    }

    /**
     * Callback when items in the contextual action mode are selected.
     *
     * @author Jimmy Shih
     */
    public interface ContextualActionModeCallback {

        /**
         * Invoked to prepare the menu for the selected items.
         *
         * @param menu          the menu
         * @param positions     the selected items' positions
         * @param ids           the selected items' ids, if available
         * @param showSelectAll true to show select all
         */
        void onPrepare(Menu menu, int[] positions, long[] ids, boolean showSelectAll);

        /**
         * Invoked when items are selected.
         *
         * @param itemId    the context menu item id
         * @param positions the selected items' positions
         * @param ids       the selected items' ids, if available
         */
        boolean onClick(int itemId, int[] positions, long[] ids);

        /**
         * Invoked when contextual action mode is destroyed.
         */
        void onDestroy();
    }
}
