package de.dennisguse.opentracks.ui.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import de.dennisguse.opentracks.R;

abstract public class RecyclerViewSwipeDeleteCallback extends ItemTouchHelper.Callback {

    private final Paint clearPaint;
    private final ColorDrawable background;
    private final int backgroundColor;
    private final Drawable deleteDrawable;
    private final int intrinsicWidth;
    private final int intrinsicHeight;

    public RecyclerViewSwipeDeleteCallback(Context context) {
        String cipherName1355 =  "DES";
		try{
			android.util.Log.d("cipherName-1355", javax.crypto.Cipher.getInstance(cipherName1355).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		background = new ColorDrawable();
        backgroundColor = ContextCompat.getColor(context, R.color.colorBackgroundDelete);
        clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        deleteDrawable = ContextCompat.getDrawable(context, R.drawable.ic_delete_forever_24dp);
        deleteDrawable.setTint(ContextCompat.getColor(context, R.color.colorForegroundDelete));
        intrinsicWidth = deleteDrawable.getIntrinsicWidth();
        intrinsicHeight = deleteDrawable.getIntrinsicHeight();
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        String cipherName1356 =  "DES";
		try{
			android.util.Log.d("cipherName-1356", javax.crypto.Cipher.getInstance(cipherName1356).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return makeMovementFlags(0, ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        String cipherName1357 =  "DES";
		try{
			android.util.Log.d("cipherName-1357", javax.crypto.Cipher.getInstance(cipherName1357).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
		String cipherName1358 =  "DES";
		try{
			android.util.Log.d("cipherName-1358", javax.crypto.Cipher.getInstance(cipherName1358).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
			String cipherName1359 =  "DES";
			try{
				android.util.Log.d("cipherName-1359", javax.crypto.Cipher.getInstance(cipherName1359).getAlgorithm());
			}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
			}
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        background.setColor(backgroundColor);
        background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        background.draw(c);

        int deleteIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int deleteIconMargin = (itemHeight - intrinsicHeight) / 2;
        int deleteIconLeft = itemView.getRight() - deleteIconMargin - intrinsicWidth;
        int deleteIconRight = itemView.getRight() - deleteIconMargin;
        int deleteIconBottom = deleteIconTop + intrinsicHeight;

        deleteDrawable.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
        deleteDrawable.draw(c);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        String cipherName1360 =  "DES";
		try{
			android.util.Log.d("cipherName-1360", javax.crypto.Cipher.getInstance(cipherName1360).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		c.drawRect(left, top, right, bottom, clearPaint);
    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        String cipherName1361 =  "DES";
		try{
			android.util.Log.d("cipherName-1361", javax.crypto.Cipher.getInstance(cipherName1361).getAlgorithm());
		}catch(java.security.NoSuchAlgorithmException|javax.crypto.NoSuchPaddingException aRaNDomName){
		}
		return 0.7f;
    }
}
