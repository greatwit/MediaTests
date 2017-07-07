package com.greatmedia;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


/**
 * @description 播放本地H264视频文件
 * @time 2016/12/19 15:22 参考翻译文档：http://www.cnblogs.com/Xiegg/p/3428529.html
 */
@SuppressLint("NewApi")
public class DecodeActivity extends Activity 
{
    private SurfaceView mSurface = null;
    private SurfaceHolder mSurfaceHolder;
    private Thread mDecodeThread;
    private MediaCodec mCodec;
    private boolean mStopFlag = false;
    private DataInputStream mInputStream;

    private static final int VIDEO_WIDTH = 1280;
    private static final int VIDEO_HEIGHT = 720;
    private int FrameRate = 30;
    private Boolean UseSPSandPPS = false;
    private String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/avctest.h264";

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_decode);
        mSurface = (SurfaceView) findViewById(R.id.sfv_video);
        File f = new File(filePath);
        if (null == f || !f.exists() || f.length() == 0) 
        {
            Toast.makeText(this, "视频文件不存在", Toast.LENGTH_LONG).show();
            return;
        }
        try 
        {
            //获取文件输入流
            mInputStream = new DataInputStream(new FileInputStream(new File(filePath)));
        } catch (FileNotFoundException e) 
        {
            e.printStackTrace();
            try {
                mInputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        mSurfaceHolder = mSurface.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() 
        {
            @SuppressLint("NewApi") @Override
            public void surfaceCreated(SurfaceHolder holder) 
            {

                 mCodec = MediaCodec.createDecoderByType("video/avc");

                //初始化编码器
                final MediaFormat mediaformat = MediaFormat.createVideoFormat("video/avc", VIDEO_WIDTH, VIDEO_HEIGHT);
                //获取h264中的pps及sps数据
                if (UseSPSandPPS) {
                    byte[] header_sps = {0, 0, 0, 1, 103, 66, 0, 42, (byte) 149, (byte) 168, 30, 0, (byte) 137, (byte) 249, 102, (byte) 224, 32, 32, 32, 64};
                    byte[] header_pps = {0, 0, 0, 1, 104, (byte) 206, 60, (byte) 128, 0, 0, 0, 1, 6, (byte) 229, 1, (byte) 151, (byte) 128};
                    mediaformat.setByteBuffer("csd-0", ByteBuffer.wrap(header_sps));
                    mediaformat.setByteBuffer("csd-1", ByteBuffer.wrap(header_pps));
                }
                //设置帧率
                mediaformat.setInteger(MediaFormat.KEY_FRAME_RATE, FrameRate);

                mCodec.configure(mediaformat, holder.getSurface(), null, 0);
                startDecodingThread();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCodec.stop();
                mCodec.release();
            }
        });
    }


    private void startDecodingThread() 
    {
        mCodec.start();
        mDecodeThread = new Thread(new decodeThread());
        mDecodeThread.start();
    }

    /**
     * @author ldm
     * @description 解码线程
     * @time 2016/12/19 16:36
     */
    private class decodeThread implements Runnable 
    {
        @Override
        public void run() 
        {
            try {
                decodeLoop();
            } catch (Exception e) 
            {
            }
        }

        private void decodeLoop() 
        {
            //存放目标文件的数据
            ByteBuffer[] inputBuffers = mCodec.getInputBuffers();
            //解码后的数据，包含每一个buffer的元数据信息，例如偏差，在相关解码器中有效的数据大小
            MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
            long startMs = System.currentTimeMillis();
            long timeoutUs = 10000;
            byte[] marker0 = new byte[]{0, 0, 0, 1};
            
            byte[] dummyFrame = new byte[]{0x00, 0x00, 0x00, 0x00};
            byte[] streamBuffer = null;
            try {
                streamBuffer = getBytes(mInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int bytes_cnt = 0;
            while (mStopFlag == false) 
            {
                bytes_cnt = streamBuffer.length;
                if (bytes_cnt == 0) 
                {
                    streamBuffer = dummyFrame;
                }

                int startIndex = 0;
                int remaining = bytes_cnt;
                while (true) 
                {
                    if (remaining == 0 || startIndex >= remaining) 
                    {
                        break;
                    }
                    int nextFrameStart = KMPMatch(marker0, streamBuffer, startIndex + 2, remaining);
                    if (nextFrameStart == -1) 
                    {
                        nextFrameStart = remaining;
                    } else 
                    {
                    }

                    int inIndex = mCodec.dequeueInputBuffer(timeoutUs);
                    if (inIndex >= 0) 
                    {
                        ByteBuffer byteBuffer = inputBuffers[inIndex];
                        byteBuffer.clear();
                        byteBuffer.put(streamBuffer, startIndex, nextFrameStart - startIndex);
                        Log.e("decode", "--------bytecount:" + (nextFrameStart - startIndex) );
                        //在给指定Index的inputbuffer[]填充数据后，调用这个函数把数据传给解码器
                        mCodec.queueInputBuffer(inIndex, 0, nextFrameStart - startIndex, 0, 0);
                        startIndex = nextFrameStart;
                    } else {
                        continue;
                    }

                    int outIndex = mCodec.dequeueOutputBuffer(info, timeoutUs);
                    if (outIndex >= 0) 
                    {
                        //帧控制是不在这种情况下工作，因为没有PTS H264是可用的
                        while (info.presentationTimeUs / 1000 > System.currentTimeMillis() - startMs) 
                        {
                            try 
                            {
                                Thread.sleep(100);
                            } catch (InterruptedException e) 
                            {
                                e.printStackTrace();
                            }
                        }
                        boolean doRender = (info.size != 0);
                        //对outputbuffer的处理完后，调用这个函数把buffer重新返回给codec类。
                        mCodec.releaseOutputBuffer(outIndex, doRender);
                    }
                    
                }
                mStopFlag = true;
            }
        }
    }

    public static byte[] getBytes(InputStream is) throws IOException 
    {
        int len;
        int size = 1024;
        byte[] buf;
        if (is instanceof ByteArrayInputStream) 
        {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } 
        else 
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
        }
        return buf;
    }

    byte[] mbLsp = new byte[]{0, 1, 2, 0};
    
    int KMPMatch(byte[] pattern, byte[] bytes, int start, int remain) 
    {
        try 
        {
            Thread.sleep(50);
        } catch (InterruptedException e) 
        {
            e.printStackTrace();
        }

        int j = 0;  // Number of chars matched in pattern
        for (int i = start; i < remain; i++) 
        {
            while (j > 0 && bytes[i] != pattern[j]) 
            {
                // Fall back in the pattern
                j = mbLsp[j - 1];  // Strictly decreasing
            }
            if (bytes[i] == pattern[j]) 
            {
                // Next char matched, increment position
                j++;
                if (j == pattern.length)
                    return i - (j - 1);
            }
        }

        return -1;  // Not found
    }
/*
    int[] computeLspTable(byte[] pattern) 
    {
        int[] lsp = new int[pattern.length];
        lsp[0] = 0;  // Base case
        for (int i = 1; i < pattern.length; i++) 
        {
            // Start by assuming we're extending the previous LSP
            int j = lsp[i - 1];
            while (j > 0 && pattern[i] != pattern[j])
                j = lsp[j - 1];
            if (pattern[i] == pattern[j])
                j++;
            lsp[i] = j;
            Log.e("..", "========================="+lsp[i]);
        }
        return lsp;
    }
    */
}