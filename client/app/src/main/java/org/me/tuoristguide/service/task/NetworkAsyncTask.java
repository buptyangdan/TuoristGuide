package org.me.tuoristguide.service.task;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.me.tuoristguide.R;

/**
 * Created by zy on 4/30/16.
 */

public abstract class NetworkAsyncTask<T> extends BaseAsyncTask<T> {

    protected NetworkAsyncTask(Context context) {
        super(context);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void request() {
        if (isConnected()) execute();
        else {
            setActive(false);
            Toast.makeText(getContext(), R.string.network_unavailable, Toast.LENGTH_LONG).show();
        }
    }
}
