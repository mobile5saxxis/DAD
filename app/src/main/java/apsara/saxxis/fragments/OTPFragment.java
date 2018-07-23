package apsara.saxxis.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import apsara.saxxis.R;

public class OTPFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private EditText et_otp;
    private ISetPhoneVerification iSetPhoneVerification;

    public void setISetPhoneVerification(ISetPhoneVerification iSetPhoneVerification) {
        this.iSetPhoneVerification = iSetPhoneVerification;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_otp, null);

        et_otp = contentView.findViewById(R.id.et_otp);
        TextView tv_otp = contentView.findViewById(R.id.tv_otp);
        tv_otp.setOnClickListener(this);

        dialog.setContentView(contentView);
    }

    @Override
    public void onClick(View v) {
        String otp = et_otp.getText().toString().trim();

        if (otp.isEmpty()) {
            Toast.makeText(getContext(), R.string.enter_otp, Toast.LENGTH_SHORT).show();
        } else {
            iSetPhoneVerification.onPhoneVerified(otp);
            dismiss();
        }
    }

    public interface ISetPhoneVerification {
        void onPhoneVerified(String otp);
    }
}

