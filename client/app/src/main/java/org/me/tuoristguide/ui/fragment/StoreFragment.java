package org.me.tuoristguide.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yelp.clientlib.entities.Business;

import org.me.tuoristguide.R;
import org.w3c.dom.Text;

import roboguice.inject.InjectView;

/**
 * Created by zy on 4/23/16.
 */
public class StoreFragment extends Fragment{

    private static final String RECOMMENDATION_ARG = "RECOMMENDATION";
    private static final String LOCATION_ARG = "LOCATION";

    private ImageView categoryImageView;
    private Button buildRouteButton;
    private TextView storeRatingTextView;
    private TextView storeNameTextView;
    private TextView reasonTextView;

    private Business business;

    public StoreFragment() {
    }

    public static StoreFragment newInstance(Business business) {
        StoreFragment fragment = new StoreFragment();
        fragment.business = business;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.store_item, container, false);
        categoryImageView = (ImageView) view.findViewById(R.id.category_image);
        buildRouteButton = (Button) view.findViewById(R.id.build_route);
        storeRatingTextView = (TextView) view.findViewById(R.id.poi_rating);
        storeNameTextView = (TextView) view.findViewById(R.id.poi_name);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storeRatingTextView.setText(business.rating().toString());
        storeNameTextView.setText(business.name());

    }

}
