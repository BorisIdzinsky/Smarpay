package com.smarcom.smarpay.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smarcom.smarpay.R;
import com.smarcom.smarpay.activity.PaymentActivity;
import com.smarcom.smarpay.cloudapi.model.AdvItem;
import com.smarcom.smarpay.helper.FontHelper;

public class WarningFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warning, container, false);
        TextView warningMessage = (TextView) view.findViewById(R.id.warning_textview);
        warningMessage.setTypeface(FontHelper.getBoldFont(getActivity()));
        warningMessage.setText(getActivity().getResources().getString(R.string.send_message));

        View rootView = view.findViewById(R.id.warning_layout_root);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Activity activity = getActivity();

                if (activity instanceof PaymentActivity) {
                    ((PaymentActivity) activity).getSupportFragmentManager().beginTransaction().hide(WarningFragment.this).commitAllowingStateLoss();
                    ((PaymentActivity) activity).showMainActivity(AdvItem.AdvTrigger.PaymentFinished);

                }


            }
        });

        return view;
    }
}
