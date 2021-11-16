package com.instahela.deni.mkopo.ui.guide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.instahela.deni.mkopo.R;


/**
 * @author xu.wang
 * @date 2019/7/30 17:32
 * @desc
 */
public class GuideAdapter extends PagerAdapter {
    private static final int INVALID_RES = -1;

    private int[] drawableBgRes = {
            R.drawable.ic_guide_1,
            R.drawable.ic_guide_2,
            R.drawable.ic_guide_3,
    };


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_viewpager_guide, container, false);
        ImageView ivIcon = view.findViewById(R.id.iv_guide_icon);
        ivIcon.setImageResource(drawableBgRes[position]);
        container.addView(view);
        view.setTag(position);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
