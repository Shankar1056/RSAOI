package bigappcompany.com.rsi.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import bigappcompany.com.rsi.Fragment.BottomFragment;
import bigappcompany.com.rsi.R;

public class BaseActivity extends AppCompatActivity {
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void showFragment(int position)
    {
        if(fragment==null) {
            fragment = new BottomFragment();
            if(position>=0)
                ((BottomFragment)fragment).setReference(position);
            getSupportFragmentManager().beginTransaction().add(R.id.bottombar, fragment).commit();
        }
    }
    public void closeFragment()
    {
        if(fragment!=null)
        {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }


    ProgressDialog dialog;
    public void showDailog(String msg)
    {
        dialog=new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(msg);
        dialog.show();
    }
    public void closeDialog()
    {
        if(dialog!=null && dialog.isShowing())
            dialog.cancel();
    }
}
