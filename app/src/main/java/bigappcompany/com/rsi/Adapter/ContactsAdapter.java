package bigappcompany.com.rsi.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;

import bigappcompany.com.rsi.Model.ContactModel;
import bigappcompany.com.rsi.Model.ImageTypeModel;
import bigappcompany.com.rsi.Model.SlotModel;
import bigappcompany.com.rsi.R;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private ArrayList<ContactModel> models;
    int lastPosition;
    onContact listener;
    boolean type=false;

    public void setType(boolean b) {
        this.type=b;
    }

    public interface onContact {
        void onClicked(ContactModel model);
    }

    public ContactsAdapter(ArrayList<ContactModel> models, onContact listener) {
        this.models = models;
        this.listener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);

        return new ViewHolder(itemView);
    }
    int selected=-1;
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ContactModel model = this.models.get(position);

            holder.titleTV.setText(model.getName());
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



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;



        ViewHolder(View itemView) {
            super(itemView);
            titleTV = (TextView) itemView.findViewById(R.id.tv_name);

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

