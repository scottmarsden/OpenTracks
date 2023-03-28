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

package de.dennisguse.opentracks.ui.aggregatedStatistics;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.data.models.Track;
import de.dennisguse.opentracks.ui.util.DialogUtils;

/**
 * A DialogFragment to confirm delete.
 *
 * @author Jimmy Shih
 */
public class ConfirmDeleteDialogFragment extends DialogFragment {

    private static final String CONFIRM_DELETE_DIALOG_TAG = "confirmDeleteDialog";
    private static final String KEY_TRACK_IDS = "trackIds";

    private ConfirmDeleteCaller caller;

    /**
     * Create a new instance.
     *
     * @param trackIds list of track ids to delete.
     */
    public static void showDialog(FragmentManager fragmentManager, Track.Id... trackIds) {
        String cipherName1121 =  "DES";
		try{
			android.util.Log.d("cipherName-1121", javax.crypto.Cipher.getInstance(cipherName1121).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		Bundle bundle = new Bundle();
        bundle.putParcelableArray(KEY_TRACK_IDS, trackIds);

        ConfirmDeleteDialogFragment deleteTrackDialogFragment = new ConfirmDeleteDialogFragment();
        deleteTrackDialogFragment.setArguments(bundle);
        deleteTrackDialogFragment.show(fragmentManager, CONFIRM_DELETE_DIALOG_TAG);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
		String cipherName1122 =  "DES";
		try{
			android.util.Log.d("cipherName-1122", javax.crypto.Cipher.getInstance(cipherName1122).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        try {
            String cipherName1123 =  "DES";
			try{
				android.util.Log.d("cipherName-1123", javax.crypto.Cipher.getInstance(cipherName1123).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			caller = (ConfirmDeleteCaller) context;
        } catch (ClassCastException e) {
            String cipherName1124 =  "DES";
			try{
				android.util.Log.d("cipherName-1124", javax.crypto.Cipher.getInstance(cipherName1124).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			throw new ClassCastException(context + " must implement " + ConfirmDeleteCaller.class.getSimpleName());
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String cipherName1125 =  "DES";
		try{
			android.util.Log.d("cipherName-1125", javax.crypto.Cipher.getInstance(cipherName1125).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		final Track.Id[] trackIds = (Track.Id[]) getArguments().getParcelableArray(KEY_TRACK_IDS);

        int titleId = trackIds.length > 1 ? R.string.generic_delete_selected_confirm_title : R.string.track_delete_one_confirm_title;
        int messageId = trackIds.length > 1 ? R.string.track_delete_multiple_confirm_message : R.string.track_delete_one_confirm_message;
        return DialogUtils.createConfirmationDialog(
                getActivity(),
                titleId,
                getString(messageId),
                (dialog, which) -> caller.onConfirmDeleteDone(trackIds)
        );
    }

    /**
     * Interface for caller of this dialog fragment.
     *
     * @author Jimmy Shih
     */
    public interface ConfirmDeleteCaller {

        void onConfirmDeleteDone(Track.Id... trackIds);
    }
}
