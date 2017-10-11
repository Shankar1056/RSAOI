package bigappcompany.com.rsi.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bigappcompany.com.rsi.Model.ImageTypeModel;
import bigappcompany.com.rsi.Model.PDFModel;
import bigappcompany.com.rsi.Model.SlotModel;
import bigappcompany.com.rsi.R;


public class PDFAdapter extends RecyclerView.Adapter<PDFAdapter.ViewHolder> {
    private ArrayList<PDFModel> models;
    int lastPosition;
    OnFacility listener;
    boolean type=false;

    public void setType(boolean b) {
        this.type=b;
    }

    public interface OnFacility{
        void onClicked(PDFModel model);
    }

    public PDFAdapter(ArrayList<PDFModel> models, OnFacility listener) {
        this.models = models;
        this.listener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final PDFModel model = this.models.get(position);
        holder.tv_date.setText(model.getDate());
        holder.titleTV.setText(model.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClicked(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public interface OnMusicItemListener {
        void onMusicItemClick(ImageTypeModel model, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV,tv_date;
        CardView card;
        ImageView img_down;



        ViewHolder(View itemView) {
            super(itemView);


            titleTV = (TextView) itemView.findViewById(R.id.tv_name);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            img_down = (ImageView) itemView.findViewById(R.id.img_download);
            card=(CardView)itemView.findViewById(R.id.card);



        }




    }
    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.slidefromdown);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}

