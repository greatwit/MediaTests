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

import com.forsafe.devicecontrol.VideoWorker;



@SuppressLint("NewApi")
public class DecodeActivity extends Activity 
{
    private SurfaceView mSurface = null;
    private SurfaceHolder mSurfaceHolder;
    private Thread mDecodeThread;
    private MediaCodec mCodec;
    private boolean mStopFlag = false;
    private DataInputStream mInputStream;

    private static final int VIDEO_WIDTH 	= 1280;
    private static final int VIDEO_HEIGHT 	= 720;
    private int FrameRate = 30;
    private Boolean UseSPSandPPS = false;
    private String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/avctest.h264";

    VideoWorker mVideo   				= new VideoWorker();
    
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
            Toast.makeText(this, "è§†é¢‘æ–‡ä»¶ä¸�å­˜åœ¨", Toast.LENGTH_LONG).show();
            return;
        }
        try 
        {
            //èŽ·å�–æ–‡ä»¶è¾“å…¥æµ�
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

                 try {
					mCodec = MediaCodec.createDecoderByType("video/avc");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                //åˆ�å§‹åŒ–ç¼–ç �å™¨
                final MediaFormat mediaformat = MediaFormat.createVideoFormat("video/avc", VIDEO_WIDTH, VIDEO_HEIGHT);
                //èŽ·å�–h264ä¸­çš„ppså�Šspsæ•°æ�®
                if (UseSPSandPPS) {
                    byte[] header_sps = {0, 0, 0, 1, 103, 66, 0, 42, (byte) 149, (byte) 168, 30, 0, (byte) 137, (byte) 249, 102, (byte) 224, 32, 32, 32, 64};
                    byte[] header_pps = {0, 0, 0, 1, 104, (byte) 206, 60, (byte) 128, 0, 0, 0, 1, 6, (byte) 229, 1, (byte) 151, (byte) 128};
                    mediaformat.setByteBuffer("csd-0", ByteBuffer.wrap(header_sps));
                    mediaformat.setByteBuffer("csd-1", ByteBuffer.wrap(header_pps));
                }
                //è®¾ç½®å¸§çŽ‡
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
     * @description è§£ç �çº¿ç¨‹
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
            //å­˜æ”¾ç›®æ ‡æ–‡ä»¶çš„æ•°æ�®
            ByteBuffer[] inputBuffers = mCodec.getInputBuffers();
            //è§£ç �å�Žçš„æ•°æ�®ï¼ŒåŒ…å�«æ¯�ä¸€ä¸ªbufferçš„å…ƒæ•°æ�®ä¿¡æ�¯ï¼Œä¾‹å¦‚å��å·®ï¼Œåœ¨ç›¸å…³è§£ç �å™¨ä¸­æœ‰æ•ˆçš„æ•°æ�®å¤§å°�
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
                        //åœ¨ç»™æŒ‡å®šIndexçš„inputbuffer[]å¡«å……æ•°æ�®å�Žï¼Œè°ƒç”¨è¿™ä¸ªå‡½æ•°æŠŠæ•°æ�®ä¼ ç»™è§£ç �å™¨
                        mCodec.queueInputBuffer(inIndex, 0, nextFrameStart - startIndex, 0, 0);
                        startIndex = nextFrameStart;
                    } else {
                        continue;
                    }

                    int outIndex = mCodec.dequeueOutputBuffer(info, timeoutUs);
                    if (outIndex >= 0) 
                    {
                        //å¸§æŽ§åˆ¶æ˜¯ä¸�åœ¨è¿™ç§�æƒ…å†µä¸‹å·¥ä½œï¼Œå› ä¸ºæ²¡æœ‰PTS H264æ˜¯å�¯ç”¨çš„
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
                        //å¯¹outputbufferçš„å¤„ç�†å®Œå�Žï¼Œè°ƒç”¨è¿™ä¸ªå‡½æ•°æŠŠbufferé‡�æ–°è¿”å›žç»™codecç±»ã€‚
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