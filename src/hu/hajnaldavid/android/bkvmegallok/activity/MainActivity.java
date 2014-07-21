package hu.hajnaldavid.android.bkvmegallok.activity;

import hu.hajnaldavid.android.bkvmegallok.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TableRow;

public class MainActivity extends Activity implements View.OnClickListener {

	private TableRow btnFavs, btnStops;

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.menuFavs:
			startActivity(new Intent(this, FavsFragmentActivity.class));
			break;
		case R.id.menuStops:
			startActivity(new Intent(this, StopsFragmentActivity.class));
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (!isOnline()) {
			showSettingsAlert();
		}

		setContentView(R.layout.activity_main);

		btnStops = (TableRow) findViewById(R.id.menuStops);
		btnFavs = (TableRow) findViewById(R.id.menuFavs);

		btnFavs.setOnClickListener(this);
		btnStops.setOnClickListener(this);

	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		alertDialog.setTitle(getResources().getString(
				R.string.dialog_wifi_settings));

		alertDialog.setMessage(getResources().getString(
				R.string.dialog_wifi_setup));

		alertDialog.setPositiveButton(
				getResources().getString(R.string.dialog_yes),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_WIFI_SETTINGS);
						startActivity(intent);
					}
				});

		alertDialog.setNegativeButton(
				getResources().getString(R.string.dialog_no),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		alertDialog.show();
	}
}
