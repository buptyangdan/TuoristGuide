package org.me.tuoristguide.ui.fragment;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.eftimoff.viewpagertransformers.FlipVerticalTransformer;
import com.eftimoff.viewpagertransformers.RotateDownTransformer;

import org.me.tuoristguide.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

import static com.google.android.gms.internal.zzir.runOnUiThread;


/**
 * Created by caoyi on 16/4/28.
 */
public class DefaultFragment extends Fragment {

     private AutoScrollViewPager mViewPager;
     private int[] mImgIds=new int[]{R.drawable.travel1, R.drawable.travel2,R.drawable.travel4, R.drawable.travel5};
     private List<ImageView> mImages=new ArrayList<ImageView>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          return inflater.inflate(R.layout.fragment_default, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
         mViewPager=(AutoScrollViewPager)view.findViewById(R.id.default_fragment);

         mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % mImgIds.length);

         mViewPager.setAdapter(new PagerAdapter() {


             @Override
             public Object instantiateItem(ViewGroup container, int position) {
                 ImageView imageView = new ImageView(getActivity());
                 imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                 imageView.setAlpha(Float.valueOf(0.7 + ""));
                 if (mImgIds.length > 0) {
                     //position % view.size()是指虚拟的position会在[0，view.size()）之间循环
                     imageView.setImageResource(mImgIds[position % mImgIds.length]);
                     if (container.equals(imageView.getParent())) {
                         container.removeView(imageView);
                     }
                     container.addView(imageView);
                     return imageView;
                 }
                 return null;
             }

             @Override
             public void destroyItem(ViewGroup container, int position, Object object) {
                 // container.removeView(mImages.get(position));
             }

             @Override
             public int getCount() {
                 return Integer.MAX_VALUE;
             }

             @Override
             public boolean isViewFromObject(View view, Object object) {
                 return view == object;
             }
         });
           mViewPager.setPageTransformer(true,new FlipVerticalTransformer());
           mViewPager.setInterval(4000);
           mViewPager.startAutoScroll();

    }


}
