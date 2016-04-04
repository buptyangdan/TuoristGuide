package org.me.tuoristguide.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.me.tuoristguide.R;

public class ExploreFragment extends Fragment {
	
	public ExploreFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
         
        return rootView;
    }
}
