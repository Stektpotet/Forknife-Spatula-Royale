package com.dunno.recipeassistant;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;


public class ListItemSwipeHelper extends ItemTouchHelper.SimpleCallback {

    public static final String TAG = ListItemSwipeHelper.class.getName();

    static class SwipeBackground {
        static final int TEXT_SIZE = 120;
        private Paint backgroundPainter;
        private Paint textPainter;
        private String text;
        private float textWidth;
        SwipeBackground(String text, int color) {
            this.text = text;
            this.backgroundPainter = new Paint();
            backgroundPainter.setColor(color);
            
            this.textPainter = new Paint();
            textPainter.setColor(Color.WHITE);
            textPainter.setAntiAlias(true);
            textPainter.setTextSize(TEXT_SIZE);
            textWidth = textPainter.measureText(text);
        }

        void draw(Canvas c, RecyclerView.ViewHolder viewHolder, float dX) {
            RectF area = new RectF(
                            viewHolder.itemView.getLeft(),
                            viewHolder.itemView.getTop(),
                            viewHolder.itemView.getWidth(),
                            viewHolder.itemView.getBottom()
            );
            c.drawRect(area, backgroundPainter);
            if(dX > 0) {
                c.drawText(text, area.left+dX-textWidth-48f, area.centerY() + (TEXT_SIZE/2), textPainter);
            } else {
                c.drawText(text, area.right + dX + 48f, area.centerY() + (TEXT_SIZE/2), textPainter);
            }
        }
    }

    private SwipeBackground leftBG;
    private SwipeBackground rightBG;


    ListItemSwipeHelper(int dragDirs, int swipeDirs, SwipeBackground left, SwipeBackground right) {
        super(dragDirs, swipeDirs);
        this.leftBG = left;
        this.rightBG = right;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
        // Reset style for the item to come here
        viewHolder.itemView.setAlpha(1);
        viewHolder.itemView.setTranslationX(0);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            float width = (float) viewHolder.itemView.getWidth();
            float alpha = 1.0f - Math.abs(dX) / width;
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
            if(dX > 0) {
                rightBG.draw(c, viewHolder, dX);
            } else if (dX < 0) {
                leftBG.draw(c, viewHolder, dX);
            }
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                    actionState, isCurrentlyActive);
        }
    }


}
