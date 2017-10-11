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

import java.util.ArrayList;

import bigappcompany.com.rsi.Model.ImageTypeModel;
import bigappcompany.com.rsi.Model.PhotoModel;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.activity.RippleView;
import bigappcompany.com.rsi.network.PicassoTrustAll;


public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.ViewHolder> {
    private ArrayList<PhotoModel> models;
    int lastPosition;
    OnFacility listener;

    public interface OnFacility{
        void onClicked(PhotoModel model);
    }

    public FacilityAdapter(ArrayList<PhotoModel> models,OnFacility listener) {
        this.models = models;
        this.listener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_facility, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final PhotoModel model = this.models.get(position);

        holder.descriptionTV.setText(model.getDescription());
        holder.titleTV.setText(model.getTitle());
        holder.itemRV.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                listener.onClicked(model);
            }
        });
        ViewTreeObserver vto = holder.thumbnailTV.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int finalHeight, finalWidth;

                holder.thumbnailTV.getViewTreeObserver().removeOnPreDrawListener(this);

                finalHeight = holder.thumbnailTV.getMeasuredHeight();
                finalWidth = holder.thumbnailTV.getMeasuredWidth();


                Log.e("height,width",finalHeight+","+finalWidth);
                //Picasso.with(holder.thumbnailTV.getContext()).load(model.getPhoto()).resize(Math.max(finalHeight,finalWidth),Math.max(finalHeight,finalWidth)).centerCrop().into(holder.thumbnailTV);
                PicassoTrustAll.getInstance(holder.thumbnailTV.getContext()).load(model.getPhoto()).placeholder(R.drawable.placeholder).resize(finalWidth,finalHeight).centerCrop().into(holder.thumbnailTV);
                return true;
            }
        });
        //setAnimation(holder.itemView,position);
        //Picasso.with(holder.thumbnailTV.getContext()).load(model.getPhoto()).resize(holder.thumbnailTV.getMeasuredWidth(),holder.thumbnailTV.getMeasuredHeight()).centerCrop().into(holder.thumbnailTV);

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public interface OnMusicItemListener {
        void onMusicItemClick(ImageTypeModel model, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements RippleView.OnRippleCompleteListener {
        TextView titleTV, descriptionTV;
        ImageView thumbnailTV;

        RippleView itemRV;

        ViewHolder(View itemView) {
            super(itemView);

            thumbnailTV = (ImageView) itemView.findViewById(R.id.img_ph);
            titleTV = (TextView) itemView.findViewById(R.id.title_tv);
            descriptionTV = (TextView) itemView.findViewById(R.id.des_tv);
            itemRV = (RippleView) itemView.findViewById(R.id.rp_fac_item);



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

