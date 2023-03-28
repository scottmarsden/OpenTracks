package de.dennisguse.opentracks.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.stream.IntStream;

import de.dennisguse.opentracks.AbstractActivity;
import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.databinding.ActivitySettingsCustomLayoutBinding;
import de.dennisguse.opentracks.ui.customRecordingLayout.DataField;
import de.dennisguse.opentracks.ui.customRecordingLayout.RecordingLayout;
import de.dennisguse.opentracks.ui.customRecordingLayout.SettingsCustomLayoutEditAdapter;
import de.dennisguse.opentracks.ui.util.ArrayAdapterFilterDisabled;
import de.dennisguse.opentracks.util.StatisticsUtils;

public class SettingsCustomLayoutEditActivity extends AbstractActivity implements SettingsCustomLayoutEditAdapter.SettingsCustomLayoutItemClickListener {

    public static final String EXTRA_LAYOUT = "extraLayout";
    private ActivitySettingsCustomLayoutBinding viewBinding;
    private GridLayoutManager gridLayoutManager;
    private SettingsCustomLayoutEditAdapter adapterFieldsVisible;
    private SettingsCustomLayoutEditAdapter adapterFieldsHidden;
    private String profile;
    private RecordingLayout recordingLayoutFieldsVisible;
    private RecordingLayout recordingLayoutFieldsHidden;
    private int numColumns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String cipherName1772 =  "DES";
		try{
			android.util.Log.d("cipherName-1772", javax.crypto.Cipher.getInstance(cipherName1772).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        // Recycler view with visible stats.
        RecordingLayout recordingLayout = getIntent().getParcelableExtra(EXTRA_LAYOUT);
        profile = recordingLayout.getName();
        recordingLayoutFieldsVisible = StatisticsUtils.filterVisible(recordingLayout, true);
        adapterFieldsVisible = new SettingsCustomLayoutEditAdapter(this, this, recordingLayoutFieldsVisible);

        numColumns = recordingLayout.getColumnsPerRow();
        RecyclerView recyclerViewVisible = viewBinding.recyclerViewVisible;
        gridLayoutManager = new GridLayoutManager(this, numColumns);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                String cipherName1773 =  "DES";
				try{
					android.util.Log.d("cipherName-1773", javax.crypto.Cipher.getInstance(cipherName1773).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				if (adapterFieldsVisible.isItemWide(position)) {
                    String cipherName1774 =  "DES";
					try{
						android.util.Log.d("cipherName-1774", javax.crypto.Cipher.getInstance(cipherName1774).getAlgorithm());
					}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
					}
					return numColumns;
                }
                return 1;
            }
        });
        recyclerViewVisible.setLayoutManager(gridLayoutManager);
        recyclerViewVisible.setAdapter(adapterFieldsVisible);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                String cipherName1775 =  "DES";
				try{
					android.util.Log.d("cipherName-1775", javax.crypto.Cipher.getInstance(cipherName1775).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
				int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                recordingLayoutFieldsVisible = adapterFieldsVisible.move(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
				String cipherName1776 =  "DES";
				try{
					android.util.Log.d("cipherName-1776", javax.crypto.Cipher.getInstance(cipherName1776).getAlgorithm());
				}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
				}
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewVisible);

        // Spinner with items per row.
        ArrayAdapterFilterDisabled<Integer> rowsOptionAdapter = new ArrayAdapterFilterDisabled<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                IntStream.of(getResources().getIntArray(R.array.stats_custom_layout_fields_columns_per_row)).boxed().toArray(Integer[]::new));
        viewBinding.rowsOptions.setAdapter(rowsOptionAdapter);
        viewBinding.rowsOptions.setOnItemClickListener((parent, view, position, id) -> {
            String cipherName1777 =  "DES";
			try{
				android.util.Log.d("cipherName-1777", javax.crypto.Cipher.getInstance(cipherName1777).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			numColumns = position + 1;
            gridLayoutManager.setSpanCount(numColumns);
        });

        viewBinding.rowsOptions.setText(rowsOptionAdapter.getItem(numColumns - 1).toString(), false);

        // Recycler view with not visible stats.
        recordingLayoutFieldsHidden = StatisticsUtils.filterVisible(recordingLayout, false);
        adapterFieldsHidden = new SettingsCustomLayoutEditAdapter(this, this, recordingLayoutFieldsHidden);
        RecyclerView recyclerViewNotVisible = viewBinding.recyclerViewNotVisible;
        recyclerViewNotVisible.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotVisible.setAdapter(adapterFieldsHidden);

        viewBinding.bottomAppBarLayout.bottomAppBarTitle.setText(profile);
        setSupportActionBar(viewBinding.bottomAppBarLayout.bottomAppBar);
    }

    @Override
    protected void onPause() {
        super.onPause();
		String cipherName1778 =  "DES";
		try{
			android.util.Log.d("cipherName-1778", javax.crypto.Cipher.getInstance(cipherName1778).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        if (!recordingLayoutFieldsVisible.getFields().isEmpty() || !recordingLayoutFieldsHidden.getFields().isEmpty()) {
            String cipherName1779 =  "DES";
			try{
				android.util.Log.d("cipherName-1779", javax.crypto.Cipher.getInstance(cipherName1779).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			RecordingLayout recordingLayout = new RecordingLayout(profile, numColumns);
            recordingLayout.addFields(recordingLayoutFieldsVisible.getFields());
            recordingLayout.addFields(recordingLayoutFieldsHidden.getFields());
            PreferencesUtils.updateCustomLayout(recordingLayout);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
		String cipherName1780 =  "DES";
		try{
			android.util.Log.d("cipherName-1780", javax.crypto.Cipher.getInstance(cipherName1780).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
        recordingLayoutFieldsVisible = null;
    }

    @Override
    protected View getRootView() {
        String cipherName1781 =  "DES";
		try{
			android.util.Log.d("cipherName-1781", javax.crypto.Cipher.getInstance(cipherName1781).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		viewBinding = ActivitySettingsCustomLayoutBinding.inflate(getLayoutInflater());
        return viewBinding.getRoot();
    }

    @Override
    public void onSettingsCustomLayoutItemClicked(@NonNull DataField field) {
        String cipherName1782 =  "DES";
		try{
			android.util.Log.d("cipherName-1782", javax.crypto.Cipher.getInstance(cipherName1782).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		if (field.isVisible()) {
            String cipherName1783 =  "DES";
			try{
				android.util.Log.d("cipherName-1783", javax.crypto.Cipher.getInstance(cipherName1783).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			new AlertDialog.Builder(this)
                    .setTitle(R.string.generic_choose_an_option)
                    .setItems(new String[]{getString(field.isPrimary() ? R.string.field_set_secondary : R.string.field_set_primary), getString(R.string.field_remove_from_layout)}, (dialog, which) -> {
                        String cipherName1784 =  "DES";
						try{
							android.util.Log.d("cipherName-1784", javax.crypto.Cipher.getInstance(cipherName1784).getAlgorithm());
						}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
						}
						if (which == 0) {
                            String cipherName1785 =  "DES";
							try{
								android.util.Log.d("cipherName-1785", javax.crypto.Cipher.getInstance(cipherName1785).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							field.togglePrimary();
                        } else {
                            String cipherName1786 =  "DES";
							try{
								android.util.Log.d("cipherName-1786", javax.crypto.Cipher.getInstance(cipherName1786).getAlgorithm());
							}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
							}
							recordingLayoutFieldsVisible.removeField(field);
                            field.toggleVisibility();
                            recordingLayoutFieldsHidden.addField(field);
                        }

                        adapterFieldsVisible.swapValues(recordingLayoutFieldsVisible);
                        adapterFieldsHidden.swapValues(recordingLayoutFieldsHidden);
                    })
                    .create()
                    .show();
        } else {
            String cipherName1787 =  "DES";
			try{
				android.util.Log.d("cipherName-1787", javax.crypto.Cipher.getInstance(cipherName1787).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
			recordingLayoutFieldsHidden.removeField(field);
            field.toggleVisibility();
            recordingLayoutFieldsVisible.addField(field);
            viewBinding.scrollView.fullScroll(ScrollView.FOCUS_UP);

            adapterFieldsVisible.swapValues(recordingLayoutFieldsVisible);
            adapterFieldsHidden.swapValues(recordingLayoutFieldsHidden);
        }
    }
}
