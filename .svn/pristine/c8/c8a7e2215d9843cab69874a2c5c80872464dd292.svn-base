package com.smarcom.smarpay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.smarcom.smarpay.R;
import com.smarcom.smarpay.helper.FontHelper;
import com.smarcom.smarpay.helper.PayButtonListener;
import com.smarcom.smarpay.cloudapi.model.PaymentChannel;

import java.util.ArrayList;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>  {

    private LayoutInflater layoutInflater;
    private List<PaymentChannel> data = new ArrayList<>();
    private PayButtonListener payButtonListener;

    public PaymentAdapter(Context context, PayButtonListener payButtonListener) {
        layoutInflater = LayoutInflater.from(context);
        this.payButtonListener = payButtonListener;
    }

    public List<PaymentChannel> getData() {
        return this.data;
    }

    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.operator_list_item, viewGroup, false);
        return new PaymentViewHolder(view, payButtonListener);
    }

    @Override
    public void onBindViewHolder(PaymentViewHolder viewHolder, int i) {
        PaymentChannel current = data.get(i);

        switch (current.getChannelIconId()) {
            case 1:
                viewHolder.imageView.setImageResource(R.drawable.smarpay_btn);
                viewHolder.smsNumber.setVisibility(View.VISIBLE);
                viewHolder.smsNumber.setText(Integer.toString(current.getSmsNumber()));
                break;
            case 2:
                viewHolder.imageView.setImageResource(R.drawable.post_finance_icon);
                viewHolder.smsNumber.setVisibility(View.GONE);
                break;
            case 3:
                viewHolder.imageView.setImageResource(R.drawable.swisscom_sunrise_salt);
                viewHolder.smsNumber.setVisibility(View.GONE);
                break;
            default:
                break;
        }

        viewHolder.channelDescription.setVisibility(View.VISIBLE);
        viewHolder.channelDescription.setText(current.getText());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private ImageButton button;
        private PayButtonListener payButtonListener;
        private TextView channelDescription;
        private TextView smsNumber;

        public PaymentViewHolder(View itemView, PayButtonListener payButtonListener) {
            super(itemView);
            this.payButtonListener = payButtonListener;
            imageView = (ImageView) itemView.findViewById(R.id.mobile_operator);
            button = (ImageButton) itemView.findViewById(R.id.operator_pay_btn);

            smsNumber = (TextView) itemView.findViewById(R.id.sms_number);
            smsNumber.setTypeface(FontHelper.getBoldFont(itemView.getContext()));

            channelDescription = (TextView) itemView.findViewById(R.id.channelDescription);
            channelDescription.setTypeface(FontHelper.getBoldFont(itemView.getContext()));

            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            payButtonListener.onPayHandler(getData().get(getAdapterPosition()).getSmsNumber());
        }
    }
}