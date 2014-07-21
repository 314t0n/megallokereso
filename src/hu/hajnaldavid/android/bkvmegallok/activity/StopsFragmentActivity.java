package hu.hajnaldavid.android.bkvmegallok.activity;

import hu.hajnaldavid.android.bkvmegallok.R;
import hu.hajnaldavid.android.bkvmegallok.adapter.StopsFragmentPagerAdapter;
import hu.hajnaldavid.android.bkvmegallok.adapter.StopAdapter.OnRouteSelectedListener;
import hu.hajnaldavid.android.bkvmegallok.adapter.StopsAdapter.OnAddFavListener;
import hu.hajnaldavid.android.bkvmegallok.adapter.StopsAdapter.OnStopSelectedListener;
import hu.hajnaldavid.android.bkvmegallok.database.Database;
import hu.hajnaldavid.android.bkvmegallok.model.Route;
import hu.hajnaldavid.android.bkvmegallok.model.Stop;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

public class StopsFragmentActivity extends FragmentActivity implements
		OnStopSelectedListener, OnRouteSelectedListener, OnAddFavListener {
	private ViewPager viewPager;
	private StopsFragmentPagerAdapter adapter;
	private Database datasource;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);

		datasource = new Database(this);
		FragmentManager manager = getSupportFragmentManager();

		viewPager = (ViewPager) findViewById(R.id.mainViewPager);
		adapter = new StopsFragmentPagerAdapter(manager);
		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(3);

	}

	@Override
	public void onStopSelected(Stop selectedStop) {
		adapter.getStopFragment().refreshWithStop(selectedStop);
		viewPager.setCurrentItem(1, true);
	}

	@Override
	public void onRouteSelected(Route selectedRoute, Stop selectedStop) {
		adapter.getRouteFragment()
				.refreshWithRoute(selectedRoute, selectedStop);
		viewPager.setCurrentItem(2, true);
	}

	@Override
	public void onAddFav(Stop stopID) {
		datasource.saveStop(stopID);
	}

}
