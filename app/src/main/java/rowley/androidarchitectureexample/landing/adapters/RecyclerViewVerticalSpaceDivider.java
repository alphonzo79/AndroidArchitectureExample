package rowley.androidarchitectureexample.landing.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ItemDecoration to put a line between two vertically-stacked RecyclerViewHolders
 */
public class RecyclerViewVerticalSpaceDivider extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    private Drawable dividerDrawable;
    private int color;

    private final int space;

    /**
     * Return an ItemDecoration that will add a bar between vertically-stacked RecyclerViewsHolders
     * @param space - Left and right padding
     */
    public RecyclerViewVerticalSpaceDivider(Context context, int space) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        dividerDrawable = a.getDrawable(0);
        a.recycle();
        color = context.getResources().getColor(android.support.v7.appcompat.R.color.primary_material_dark);
        this.space = space;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + space;
        final int right = parent.getWidth() - parent.getPaddingRight() - space;
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + dividerDrawable.getIntrinsicHeight();
            dividerDrawable.setBounds(left, top, right, bottom);
            dividerDrawable.setColorFilter(color, PorterDuff.Mode.SRC);
            dividerDrawable.draw(c);
        }
    }
}
