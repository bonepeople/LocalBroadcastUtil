package com.bonepeople.android.localbroadcastutil.simple;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bonepeople.android.localbroadcastutil.LocalBroadcastUtil;

public class FragmentA extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_a, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.button_increase).setOnClickListener(this);
        view.findViewById(R.id.button_reduce).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_increase:
                LocalBroadcastUtil.INSTANCE.sendBroadcast(Constants.BROADCAST_INCREASE);
                break;
            case R.id.button_reduce:
                LocalBroadcastUtil.INSTANCE.sendBroadcast(Constants.BROADCAST_REDUCE);
                break;
        }
    }
}
