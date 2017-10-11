package bigappcompany.com.rsi.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import bigappcompany.com.rsi.R;

/**
 * Created by Admin on 18-07-2016.
 */
public class CustomTextViewwox extends TextView {
    public CustomTextViewwox(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),getResources().getString(R.string.wox)));
    }
}
