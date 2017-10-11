package bigappcompany.com.rsi.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bigappcompany.com.rsi.Model.HistoryModel;
import bigappcompany.com.rsi.Model.ImageTypeModel;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.activity.RippleView;
import bigappcompany.com.rsi.network.PicassoTrustAll;


public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> {
    private ArrayList<HistoryModel> models;
    int lastPosition;
    OnFacility listener;

    public interface OnFacility{
        void onClicked(HistoryModel model);
    }

    public BookingHistoryAdapter(ArrayList<HistoryModel> models, OnFacility listener) {
        this.models = models;
        this.listener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final HistoryModel model = this.models.get(position);

        holder.titleTV.setText(model.getTitle());

        ViewTreeObserver vto = holder.thumbnailTV.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int finalHeight, finalWidth;

                holder.thumbnailTV.getViewTreeObserver().removeOnPreDrawListener(this);

                finalHeight = holder.thumbnailTV.getMeasuredHeight();
                finalWidth = holder.thumbnailTV.getMeasuredWidth();


                Log.e("height,width",finalHeight+","+finalWidth);
                //Picasso.with(holder.thumbnailTV.getContext()).load(model.getPhoto()).resize(Math.max(finalHeight,finalWidth),Math.max(finalHeight,finalWidth)).centerCrop().into(holder.thumbnailTV);
                PicassoTrustAll.getInstance(holder.thumbnailTV.getContext()).load(model.getImg()).resize(finalWidth,finalHeight).centerInside().into(holder.thumbnailTV);
                return true;
            }
        });
        holder.tv_time.setText(model.getTime());
        holder.tv_type.setText(model.getType());
        try {
            Date date=new SimpleDateFormat("MMM dd yyyy").parse(model.getDate());
            holder.tv_month.setText((new SimpleDateFormat("MMM").format(date)));
            holder.tv_dt.setText((new SimpleDateFormat("dd").format(date)));
            holder.tv_year.setText((new SimpleDateFormat("yyyy").format(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    class ViewHolder extends RecyclerView.ViewHolder implements RippleView.OnRippleCompleteListener {
        TextView titleTV,tv_time,tv_dt,tv_month,tv_year,tv_type;
        ImageView thumbnailTV;



        ViewHolder(View itemView) {
            super(itemView);

            thumbnailTV = (ImageView) itemView.findViewById(R.id.img_type);
            titleTV = (TextView) itemView.findViewById(R.id.tv_name);
            tv_dt = (TextView) itemView.findViewById(R.id.txt_date);
            tv_month = (TextView) itemView.findViewById(R.id.txt_month);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_year = (TextView) itemView.findViewById(R.id.txt_year);
            tv_type = (TextView) itemView.findViewById(R.id.txt_type);




            //itemRV.setOnRippleCompleteListener(this);
        }



        @Override
        public void onComplete(RippleView rippleView) {

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

