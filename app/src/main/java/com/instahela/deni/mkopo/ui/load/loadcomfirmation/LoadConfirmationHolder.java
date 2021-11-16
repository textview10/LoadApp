package com.instahela.deni.mkopo.ui.load.loadcomfirmation;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.instahela.deni.mkopo.R;

import org.jetbrains.annotations.NotNull;

public class LoadConfirmationHolder extends RecyclerView.ViewHolder {

    public TextView tvTitle, tvDueDate, tvServiceFee, tvInterest, tvReviewFree, tvTotalPayment, tvRepay;
    public FrameLayout flDesc4;
    public View view;

    public LoadConfirmationHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        view = itemView.findViewById(R.id.view_item_load_confirmation_divider);
        tvTitle = itemView.findViewById(R.id.tv_item_load_confirmation_title);
        tvDueDate = itemView.findViewById(R.id.tv_load_confirmation_due_date);
        tvServiceFee = itemView.findViewById(R.id.tv_load_confirmation_service_fee);
        tvInterest = itemView.findViewById(R.id.tv_load_confirmation_interest);
        tvReviewFree = itemView.findViewById(R.id.tv_load_confirmation_review_free);
        tvTotalPayment = itemView.findViewById(R.id.load_processing_desc_total_payment);
        tvRepay = itemView.findViewById(R.id.tv_item_load_result_repayment);
        flDesc4 = itemView.findViewById(R.id.fl_item_load_confirm_desc_4);
    }
}
