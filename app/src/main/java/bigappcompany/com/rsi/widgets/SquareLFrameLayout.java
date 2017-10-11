package bigappcompany.com.rsi.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;


public class SquareLFrameLayout extends FrameLayout {

    public SquareLFrameLayout(final Context context) {
        super(context);
    }

    public SquareLFrameLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLFrameLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        if (width > height) {
            setMeasuredDimension(height, height);
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension(width, width);
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }
    }
}
