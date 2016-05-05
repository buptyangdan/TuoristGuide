package org.me.tuoristguide.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yelp.clientlib.entities.Business;

import org.me.tuoristguide.ui.fragment.StoreFragment;

import java.util.ArrayList;

/**
 * Created by zy on 4/14/16.
 */
public class StoresAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Business> businesses;

    public StoresAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
        this.businesses = new ArrayList<>();
    }

    public void set(ArrayList<Business> businesses){
        this.businesses.clear();
        this.businesses.addAll(businesses);
    }

    @Override
    public StoreFragment getItem(int position) {
        return StoreFragment.newInstance(businesses.get(position));
    }

    @Override
    public int getCount() {
        return this.businesses.size();
    }
}
