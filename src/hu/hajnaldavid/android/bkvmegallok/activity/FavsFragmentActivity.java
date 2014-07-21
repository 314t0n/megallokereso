package hu.hajnaldavid.android.bkvmegallok.activity;

import hu.hajnaldavid.android.bkvmegallok.R;
import hu.hajnaldavid.android.bkvmegallok.adapter.FavsFragmentPagerAdapter;
import hu.hajnaldavid.android.bkvmegallok.adapter.FavsAdapter.OnDeleteFavListener;
import hu.hajnaldavid.android.bkvmegallok.adapter.StopAdapter.OnRouteSelectedListener;
import hu.hajnaldavid.android.bkvmegallok.database.Database;
import hu.hajnaldavid.android.bkvmegallok.fragment.FavsFragment.OnFavSelectedListener;
import hu.hajnaldavid.android.bkvmegallok.model.Route;
import hu.hajnaldavid.android.bkvmegallok.model.Stop;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class FavsFragmentActivity extends FragmentActivity implements
		OnFavSelectedListener, OnDeleteFavListener, OnRouteSelectedListener {

	private ViewPager viewPager;
	private FavsFragmentPagerAdapter adapter;
	private Database datasource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.viewpager);
		datasource = new Database(this);
		viewPager = (ViewPager) findViewById(R.id.mainViewPager);
		adapter = new FavsFragmentPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
	}

	@Override
	public void onFavSelected(Stop selectedFav) {
		adapter.getStopFragment().refreshWithStop(selectedFav);
		viewPager.setCurrentItem(1, true);
	}

	@Override
	public void onFavDelete(String stopID) {
		adapter.notifyDataSetChanged();
		datasource.deleteStop(stopID);
	}

	@Override
	public void onRouteSelected(Route selectedRoute, Stop selectedStop) {
		adapter.getRouteFragment()
				.refreshWithRoute(selectedRoute, selectedStop);
		viewPager.setCurrentItem(2, true);
	}

}
