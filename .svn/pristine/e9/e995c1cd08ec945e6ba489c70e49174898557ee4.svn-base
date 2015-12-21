package com.smarcom.smarpay.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.smarcom.smarpay.R;
import com.smarcom.smarpay.helper.FontHelper;

import roboguice.fragment.RoboDialogFragment;

public class ErrorDialogFragment extends RoboDialogFragment {

    public static ErrorDialogFragment newInstance(String message) {
        ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putString("Message", message);
        errorDialogFragment.setArguments(args);
        return errorDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_error_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        ImageButton dismissButton = (ImageButton) dialog.findViewById(R.id.imageButton);
        TextView errorMessage = (TextView) dialog.findViewById(R.id.error_textview);
        errorMessage.setTypeface(FontHelper.getBoldFont(getActivity()));
        errorMessage.setText(getArguments().getString("Message"));
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dismiss();
            }
        });
        return dialog;
    }
}
