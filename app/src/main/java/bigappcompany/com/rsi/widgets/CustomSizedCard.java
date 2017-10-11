package bigappcompany.com.rsi.widgets;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.FrameLayout;


public class CustomSizedCard extends FrameLayout {

    public CustomSizedCard(final Context context) {
        super(context);
    }

    public CustomSizedCard(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSizedCard(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);

        /*if (width > height) {
            setMeasuredDimension(width, width);
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else {*/
            setMeasuredDimension(width, width);
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        //}
    }
}
