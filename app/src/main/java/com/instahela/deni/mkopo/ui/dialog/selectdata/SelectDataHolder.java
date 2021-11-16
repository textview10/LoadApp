package com.instahela.deni.mkopo.ui.dialog.selectdata;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.instahela.deni.mkopo.R;

import org.jetbrains.annotations.NotNull;

public class SelectDataHolder extends RecyclerView.ViewHolder {

    public TextView tvSelectData;

    public SelectDataHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        tvSelectData = itemView.findViewById(R.id.tv_select_data);
    }
}
