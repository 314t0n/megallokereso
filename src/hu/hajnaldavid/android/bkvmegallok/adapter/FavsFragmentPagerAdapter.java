package hu.hajnaldavid.android.bkvmegallok.adapter;

import hu.hajnaldavid.android.bkvmegallok.fragment.FavsFragment;
import hu.hajnaldavid.android.bkvmegallok.fragment.RouteFragment;
import hu.hajnaldavid.android.bkvmegallok.fragment.StopFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FavsFragmentPagerAdapter extends FragmentPagerAdapter {

	public FavsFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	protected StopFragment stopFragment;
	protected RouteFragment routeFragment;
	private Fragment myFragment;

	public StopFragment getStopFragment() {
		if (stopFragment == null)
			stopFragment = new StopFragment();
		return stopFragment;
	}

	public RouteFragment getRouteFragment() {
		if (routeFragment == null)
			routeFragment = new RouteFragment();
		return routeFragment;
	}

	@Override
	public Fragment getItem(int arg0) {
		myFragment = null;
		switch (arg0) {
		case 0:
			myFragment = new FavsFragment();
			break;
		case 1:
			stopFragment = new StopFragment();
			myFragment = stopFragment;
			break;
		case 2:
			routeFragment = new RouteFragment();
			myFragment = routeFragment;
			break;
		}
		return myFragment;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
