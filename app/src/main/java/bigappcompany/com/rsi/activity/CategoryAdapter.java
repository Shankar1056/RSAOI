package bigappcompany.com.rsi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import bigappcompany.com.rsi.R;
import bigappcompany.com.rsi.network.JsonParser;

import static android.content.Context.MODE_PRIVATE;

public class CategoryAdapter extends BaseAdapter {
    int[] result;
    Context context;
    String[] values;
    int selected=-1;
    private static LayoutInflater inflater=null;
    private MainActivity activity;

    public void setData(Activity mainActivity, int[] ids, String[] values) {
        // TODO Auto-generated constructor stub
        result=ids;
        context=mainActivity;
        this.values =values;
        activity=(MainActivity) mainActivity;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {

        //ImageView img;
        TextView tv;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.row_catg, null);

        //holder.img=(ImageView) rowView.findViewById(R.id.img_catg);
        holder.tv=(TextView)rowView.findViewById(R.id.textView_catg);

        holder.tv.setText(values[position]);
        holder.tv.setClickable(true);
        if(activity.catg_id==result[position])
            selected=position;
        if(selected==position)
            rowView.setBackgroundColor(Color.parseColor("#00796A"));
        else
            rowView.setBackgroundColor(Color.parseColor("#00ffffff"));
        //holder.img.setImageResource(imageId[position]);
        final View view=rowView;
        holder.tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(context, "You Clicked " + result.get(position), Toast.LENGTH_LONG).show();

                try {
                    //activity.choosen(result.get(position),JsonData,position);
                    //if(selected!=position)
                    //{
                        view.setBackgroundColor(Color.parseColor("#00796A"));
                        selected=position;
                        activity.catg_id=result[position];
                        Log.e("position",selected+" ");
                        if(position==0)
                        {
                            activity.onBackPressed();
                        }
                        if(position==1)
                        {
                            activity.animateView(true);
                            activity.startActivity(new Intent(activity,About.class));
                        }
                        
                        else if(position==2)
                        {
                            activity.animateView(true);
                            activity.startActivity(new Intent(activity,Contact.class));
                        }
                        else if(position==3)
                        {
                            new AlertDialog.Builder(activity)
                                .setTitle(R.string.title_logout)
                                .setMessage(R.string.msg_logout)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        activity.getSharedPreferences(JsonParser.APP_NAME,MODE_PRIVATE).edit().clear().apply();
                                            FirebaseAuth.getInstance().signOut();
                                        Toast.makeText(activity,R.string.logout_success,Toast.LENGTH_LONG).show();
                                        activity.finish();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, null)
                                .create()
                                .show();
                            //activity.animateView(true);
                            
                        }
                    /*}
                    else {
                        view.setBackgroundColor(Color.parseColor("#00ffffff"));
                        selected=-1;
                    }*/

                    notifyDataSetChanged();

                }
                catch (Exception e)
                {
                    Log.e("Error",e.getMessage());
                }


            }
        });
        return rowView;
    }

}