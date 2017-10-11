package bigappcompany.com.rsi.Adapter;

import android.content.Context;
import android.content.Intent;
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
import bigappcompany.com.rsi.activity.PhotosActivity;
import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.activity.RippleView;
import bigappcompany.com.rsi.network.PicassoTrustAll;


public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private ArrayList<ImageTypeModel> models;
    private int lastPosition;


    public PhotoAdapter(ArrayList<ImageTypeModel> models) {
        this.models = models;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ImageTypeModel model = this.models.get(position);


        holder.descriptionTV.setText(model.getDate());
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
                PicassoTrustAll.getInstance(holder.thumbnailTV.getContext()).load(model.getPhoto()).placeholder(R.drawable.placeholder).resize(Math.min(finalHeight,finalWidth),Math.min(finalHeight,finalWidth)).centerCrop().into(holder.thumbnailTV);
                return true;
            }
        });
        holder.itemRV.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                holder.itemRV.getContext().startActivity(new Intent(holder.itemRV.getContext(), PhotosActivity.class).putExtra("id",model.getId()));
            }
        });
        setAnimation(holder.itemView,position);
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

            thumbnailTV = (ImageView) itemView.findViewById(R.id.audioimg);
            titleTV = (TextView) itemView.findViewById(R.id.audiotitle);
            descriptionTV = (TextView) itemView.findViewById(R.id.description);
            itemRV = (RippleView) itemView.findViewById(R.id.rp_row);



            itemRV.setOnRippleCompleteListener(this);
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

