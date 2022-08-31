package com.bonepeople.android.localbroadcastutil.simple;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bonepeople.android.localbroadcastutil.LocalBroadcastHelper;

public class FragmentB extends Fragment {
    private TextView textView_number;
    private final Receiver receiver = new Receiver();
    private int number;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_b, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        textView_number = view.findViewById(R.id.textView_number);
        new LocalBroadcastHelper().setLifecycleOwner(getViewLifecycleOwner()).setReceiver(receiver).addAction(Constants.BROADCAST_INCREASE, Constants.BROADCAST_REDUCE).register();
        updateNumber();
    }

    private void updateNumber() {
        if (textView_number != null)
            textView_number.setText(String.valueOf(number));
    }

    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null)
                return;
            switch (action) {
                case Constants.BROADCAST_INCREASE:
                    number++;
                    updateNumber();
                    break;
                case Constants.BROADCAST_REDUCE:
                    number--;
                    updateNumber();
                    break;
            }
        }
    }
}
