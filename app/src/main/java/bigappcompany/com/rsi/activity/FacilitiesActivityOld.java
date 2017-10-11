package bigappcompany.com.rsi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import bigappcompany.com.rsi.Model.Constants;
import bigappcompany.com.rsi.R;

public class FacilitiesActivityOld extends BaseActivity implements RippleView.OnRippleCompleteListener{
    private static final String TAG_BOTTOM = "bottom_fragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilitiesold);
        overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        showFragment(2);
        ((RippleView)findViewById(R.id.rp_banquets)).setOnRippleCompleteListener(this);
        //((RippleView)findViewById(R.id.rp_bar)).setOnRippleCompleteListener(this);
        //((RippleView)findViewById(R.id.rp_rest)).setOnRippleCompleteListener(this);
        ((RippleView)findViewById(R.id.rp_guest)).setOnRippleCompleteListener(this);
        //((RippleView)findViewById(R.id.rp_sports)).setOnRippleCompleteListener(this);
    }
    @Override
    public void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                startActivity(new Intent(FacilitiesActivityOld.this,MainActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
    @Override
    public void onComplete(RippleView rippleView) {
        String title="";
        switch (rippleView.getId())
        {
            case R.id.rp_banquets:
                title="Banquets";
                break;
            /*case R.id.rp_bar:
                title="Bars";
                break;*/
            case R.id.rp_guest:
                title="Guest";
                break;
            /*case R.id.rp_rest:
                title="Restaurants";
                break;
            case R.id.rp_sports:
                title="Sports";
                break;*/
        }
        startActivity(new Intent(FacilitiesActivityOld.this,FacilitiesSubList.class).putExtra(Constants.TITILE,title).putExtra(Constants.TAG,rippleView.getId()));
    }
}
