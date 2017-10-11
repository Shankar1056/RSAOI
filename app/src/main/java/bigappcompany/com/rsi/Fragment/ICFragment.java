package bigappcompany.com.rsi.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import bigappcompany.com.rsi.R;


public class ICFragment extends Fragment {
	onClick listener;
	public interface onClick {
		void onClicked();
		void updateUI(String msg);
	}
	String msg;
	public void setMessage(String msg)
	{
		this.msg=msg;
		if(tv_error!=null)
		{
			tv_error.setText(msg);
		}
		else
		{

		}
	}
	TextView tv_error;
	public void setListener(onClick listener)
	{
		this.listener=listener;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.network_error, container, false);
		tv_error=(TextView)rootView.findViewById(R.id.txt_err);
		if(listener!=null)
		{
			listener.updateUI(msg);
		}
		rootView.findViewById(R.id.bt_retry).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener!=null)
				{
					listener.onClicked();
				}
			}
		});
		return rootView;
	}

}


