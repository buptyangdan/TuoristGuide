package org.me.tuoristguide.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.util.Attributes;
import com.squareup.picasso.Picasso;
import com.yelp.clientlib.entities.Business;

import org.me.tuoristguide.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by zy on 4/30/16.
 */
public class BusinessRouteAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {

    private static final String TAG = "businessRouteAdapter";

    private enum ViewType {TOP, MIDDLE, BOTTOM}

    private final List<Business> businessList;
    private final Location currentLocation;
    private OnDeleteItemListener onDeleteItemListener;

    public BusinessRouteAdapter(Location currentLocation){
        this.currentLocation = currentLocation;
        businessList = new ArrayList<>();
        setMode(Attributes.Mode.Single);
    }

    public void setOnDeleteItemListener(OnDeleteItemListener onDeleteItemListener) {
        this.onDeleteItemListener = onDeleteItemListener;
    }

    public void add(Business... businesses) {
        businessList.addAll(Arrays.asList(businesses));
    }

    public void set(List<Business> businesses) {
        businessList.clear();
        businessList.addAll(businesses);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (ViewType.values()[viewType]) {
            case TOP:
                View topView = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_top_item, parent, false);
                return new TopViewHolder(topView);
            case MIDDLE:
                View middleView = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_middle_item, parent, false);
                return new MiddleViewHolder(middleView);
            case BOTTOM:
                View bottomView = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_bottom_item, parent, false);
                return new BottomViewHolder(bottomView);
            default: return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Context context = holder.itemView.getContext();
        switch (ViewType.values()[getItemViewType(position)]) {
            case TOP:
                TopViewHolder topViewHolder = (TopViewHolder) holder;
                topViewHolder.locationDescriptionTextView.setText(getAddress(context, currentLocation));
                break;
            case MIDDLE:
                final Business business = businessList.get(position-1);
                final MiddleViewHolder middleViewHolder = (MiddleViewHolder) holder;
                middleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
                middleViewHolder.swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);
                middleViewHolder.businessNameView.setText(business.name());
                middleViewHolder.locationDescriptionTextView.setText(business.rating().toString() + "  " +
                getAddress(context, business));
                middleViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mItemManger.removeShownLayouts(middleViewHolder.swipeLayout);
                        businessList.remove(position-1);
                        notifyDataSetChanged();
                        mItemManger.closeAllItems();
                        if (onDeleteItemListener != null) onDeleteItemListener.onDelete(business, position);
                    }
                });
                break;
            case BOTTOM:
                final Business business1 = businessList.get(position-1);
                final BottomViewHolder bottomViewHolder = (BottomViewHolder) holder;
                bottomViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
                bottomViewHolder.locationDescriptionTextView.setText(business1.rating().toString()
                 + "  " + getAddress(context, business1));
                bottomViewHolder.swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);
                bottomViewHolder.businessNameView.setText(business1.name());
                bottomViewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mItemManger.removeShownLayouts(bottomViewHolder.swipeLayout);
                        businessList.remove(position-1);
                        notifyDataSetChanged();
                        mItemManger.closeAllItems();
                        if (onDeleteItemListener != null) onDeleteItemListener.onDelete(business1, position);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return businessList.size() + 1;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return ViewType.TOP.ordinal();
        if (position == getItemCount()-1) return ViewType.BOTTOM.ordinal();
        return ViewType.MIDDLE.ordinal();
    }

    private String getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses == null || addresses.isEmpty()) return null;
            Address address = addresses.get(0);
            StringBuilder addressBuilder = new StringBuilder();
            String prefix = "";
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressBuilder.append(prefix).append(address.getAddressLine(i));
                prefix = "/";
            }
            return addressBuilder.toString();
        } catch (IOException ex) {
            Log.e(TAG, "Service unavailable", ex);
            return null;
        } catch (Exception ex) {
            Log.e(TAG, "Error while retrieving address", ex);
            return null;
        }

    }

    private String getAddress(Context context, Business business) {
        return getAddress(context, business.location().coordinate().latitude(),
                business.location().coordinate().longitude());
    }

    private String getAddress(Context context, Location location) {
        return getAddress(context, location.getLatitude(), location.getLongitude());
    }

    public interface OnDeleteItemListener {
        void onDelete(Business business, int position);
    }
    private static class TopViewHolder extends RecyclerView.ViewHolder {

        private ImageView markerImage;
        private TextView locationDescriptionTextView;

        public TopViewHolder(View itemView) {
            super(itemView);
            markerImage = (ImageView) itemView.findViewById(R.id.marker_image);
            locationDescriptionTextView = (TextView) itemView.findViewById(R.id.location_description);
        }
    }

    private static class MiddleViewHolder extends RecyclerView.ViewHolder {

        private SwipeLayout swipeLayout;
        private ImageView categoryImageView;
        private TextView businessNameView;
        private TextView categoryNameView;
        private ImageButton deleteButton;
        private TextView locationDescriptionTextView;

        public MiddleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            categoryImageView = (ImageView) itemView.findViewById(R.id.category_image);
            businessNameView = (TextView) itemView.findViewById(R.id.business_name);
            deleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);
            locationDescriptionTextView = (TextView) itemView.findViewById(R.id.location_description);
        }
    }

    private static class BottomViewHolder extends RecyclerView.ViewHolder {

        private SwipeLayout swipeLayout;
        private ImageView categoryImageView;
        private TextView businessNameView;
        private TextView categoryNameView;
        private ImageButton deleteButton;
        private TextView locationDescriptionTextView;

        public BottomViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            categoryImageView = (ImageView) itemView.findViewById(R.id.category_image);
            businessNameView = (TextView) itemView.findViewById(R.id.business_name);
            deleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);
            locationDescriptionTextView = (TextView) itemView.findViewById(R.id.location_description);
        }
    }
}

