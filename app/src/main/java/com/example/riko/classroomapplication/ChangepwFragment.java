package com.example.riko.classroomapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangepwFragment extends Fragment implements View.OnClickListener {

    private View view;
    private EditText editextPassword;
    private EditText editextNewPassword;
    private EditText editextConfirmPassword;
    private Button buttonChangepw;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_changepw, container, false);
        initInstance();
        return view;

    }

    private void initInstance() {
        editextPassword = view.findViewById(R.id.editextPassword);
        editextNewPassword = view.findViewById(R.id.editextNewPassword);
        editextConfirmPassword = view.findViewById(R.id.editextConfirmPassword);
        buttonChangepw = view.findViewById(R.id.buttonChangepw);
        buttonChangepw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonChangepw){
            Toast.makeText(getContext(), "Already changed your password!", Toast.LENGTH_SHORT).show();
        }
    }
}
