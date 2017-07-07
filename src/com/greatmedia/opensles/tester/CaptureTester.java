package com.greatmedia.opensles.tester;

import android.os.Environment;



import java.io.IOException;

import com.greatmedia.opensles.api.AudioCapturer;
import com.greatmedia.opensles.api.WavFileWriter;

public class CaptureTester extends Tester implements AudioCapturer.OnAudioFrameCapturedListener {

    private static final String DEFAULT_TEST_FILE = Environment.getExternalStorageDirectory() + "/test.wav";

    private AudioCapturer mAudioCapturer;
    private WavFileWriter mWavFileWirter;

    @Override
    public boolean startTesting() {
        mAudioCapturer = new AudioCapturer();
        mWavFileWirter = new WavFileWriter();
        try {
            mWavFileWirter.openFile(DEFAULT_TEST_FILE, 44100, 16, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mAudioCapturer.setOnAudioFrameCapturedListener(this);
        mAudioCapturer.startCapture();
        return true;
    }

    @Override
    public boolean stopTesting() {
        mAudioCapturer.stopCapture();
        try {
            mWavFileWirter.closeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onAudioFrameCaptured(byte[] audioData) {
        mWavFileWirter.writeData(audioData, 0, audioData.length);
    }
}
