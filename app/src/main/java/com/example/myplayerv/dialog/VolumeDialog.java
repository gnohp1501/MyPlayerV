package com.example.myplayerv.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.myplayerv.R;

public class VolumeDialog extends AppCompatDialogFragment {

    private ImageView cross;
    private TextView volume_no;
    private SeekBar seekBar;
    AudioManager audioManager;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.volumn_dialog,null);
        alert.setView(view);
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
        cross = view.findViewById(R.id.iv_close_dialog_volume);
        volume_no= view.findViewById(R.id.number_volume);
        seekBar = view.findViewById(R.id.seekbar_duration);
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBar.setProgress(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);
                int mediaVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                double volPer = Math.ceil((((double) mediaVol / (double) maxVol)* (double) 100));
                String volume = String.valueOf(volPer).replace(".0","");
                volume_no.setText(volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //
        return alert.create();
    }
}
