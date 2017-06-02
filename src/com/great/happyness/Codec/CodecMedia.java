package com.great.happyness.Codec;

import java.nio.ByteBuffer;

import android.media.MediaCodec.BufferInfo;
import android.media.MediaCrypto;
import android.util.Log;
import android.view.Surface;



public class CodecMedia 
{
	
    public static CodecMedia createDecoderByType(String type) 
    {
        return new CodecMedia(type, true /* nameIsType */, false /* encoder */);
    } 

    /**
     * Instantiate an encoder supporting output data of the given mime type.
     * @param type The desired mime type of the output data.
     */
    public static CodecMedia createEncoderByType(String type) 
    {
        return new CodecMedia(type, true /* nameIsType */, true /* encoder */);
    }

    /**
     * If you know the exact name of the component you want to instantiate
     * use this method to instantiate it. Use with caution.
     * Likely to be used with information obtained from {@link android.media.MediaCodecList}
     * @param name The name of the codec to be instantiated.
     */
    public static CodecMedia createByCodecName(String name) 
    {
        return new CodecMedia(name, false /* nameIsType */, false /* unused */);
    }

    private CodecMedia(String name, boolean nameIsType, boolean encoder) 
    {
        native_setup(name, nameIsType, encoder);
    }

    @Override
    protected void finalize() 
    {
    	release();
    }
	
    public void configure( String[] keys, Object[] values, Surface surface, MediaCrypto crypto, int flags) 
    {
        native_configure(keys, values, surface, crypto, flags);
    }
    
    /**
     * Call this after start() returns.
     */
    public ByteBuffer[] getInputBuffers() {
        return getBuffers(true /* input */);
    }

    /**
     * Call this after start() returns and whenever dequeueOutputBuffer
     * signals an output buffer change by returning
     * {@link #INFO_OUTPUT_BUFFERS_CHANGED}
     */
    public ByteBuffer[] getOutputBuffers() {
        return getBuffers(false /* input */);
    }
    
    
	static
	{
		try
		{
			Log.e("..", "----------------2");
			System.loadLibrary("stlport");
			System.loadLibrary("great_media");
			
			native_init();
			Log.e("..", "----------------2");
		}
		catch(Throwable e)
		{ 
			Log.e("..", "----------------2e:"+e.toString());
		}

	}

	
	private static native final void native_init();
	private native void native_setup(String name, boolean nameIsType, boolean encoder);
	private native void release();
	private native void native_configure( String[] keys, Object[] values, Surface surface, MediaCrypto crypto, int flags);
	public native final void start();
	public native final void stop();
	public native final void flush();
	public native final void startCodec(BufferInfo info);
	private native ByteBuffer[] getBuffers( boolean input);
	public native final int dequeueInputBuffer( long timeoutUs);
	public native final void queueInputBuffer( int index, int offset, int size, long timestampUs, int flags);
	public native final int dequeueOutputBuffer( BufferInfo info, long timeoutUs);
	public native final void releaseOutputBuffer( int index, boolean render);
}