package org.me.tuoristguide.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.me.tuoristguide.R;

/**
 * Created by zy on 4/12/16.
 */
public class DetailFragment extends Fragment {

    private static View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detail, container, false);

        return view;
    }
}
