package com.instahela.deni.mkopo.ui.home.startload;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.instahela.deni.mkopo.R;

import org.jetbrains.annotations.NotNull;

public class HomeHolder extends RecyclerView.ViewHolder {
    public FrameLayout flHome;
    public TextView tvHome;

    public HomeHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        flHome = itemView.findViewById(R.id.fl_item_home);
        tvHome = itemView.findViewById(R.id.tv_item_home);
    }
}
