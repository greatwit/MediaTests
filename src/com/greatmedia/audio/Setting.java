package com.greatmedia.audio;

import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.AudioManager;
import android.util.Log;


public class Setting 
{
		private static String dataBaseName 	= "appIpaddrDatabase";
		private static String tableName 	= "appIpaddrTable";

		
		public String readData(Context context,int keyindex) 
		{
			String Keycounter = "";
			SQLiteDatabase adobeDB = null;
			try 
			{  
				adobeDB = context.openOrCreateDatabase(dataBaseName,Context.MODE_PRIVATE, null);
				adobeDB.execSQL("CREATE TABLE IF NOT EXISTS '" + tableName + "' (" + 
		    						"id integer primary key autoincrement, " +
									    "Keyindex tinyint, " +
									         "Keycounter varchar(20) ); ");
				Cursor mcursor = adobeDB.rawQuery("SELECT * FROM '" + tableName + "' where Keyindex = " + keyindex + "", null);
				//Log.e("..","mcursor count*********:"+mcursor.getCount());
				if (mcursor != null) 
				{
					if(mcursor.moveToFirst())
					    //mcursor.moveToFirst();
						Keycounter=mcursor.getString(mcursor.getColumnIndex("Keycounter"));
 				else 
 				{
						Keycounter="";
						adobeDB.execSQL("INSERT INTO '" + tableName + "' (Keyindex,Keycounter) VALUES (" +
									""+ keyindex +"," +
										 "'" + Keycounter +"');");
 				}
			    }
				if(mcursor!=null)
				mcursor.close();
				return Keycounter;
			} 
			catch (SQLiteException se)
			{
				Log.e(dataBaseName,"Could not create or open the database");
			} 
			finally 
			{
				if (adobeDB != null) 
				{
					adobeDB.close();
				} 
			}
			return Keycounter;
			
		}

		
		public void InsertOrUpdate(Context context,int keyindex, String keycounter) 
		{
			SQLiteDatabase adobeDB = null;
			try 
			{
				adobeDB = context.openOrCreateDatabase(dataBaseName,Context.MODE_PRIVATE, null);
				adobeDB.execSQL("CREATE TABLE IF NOT EXISTS '" + tableName + "' (" + 
	    						"id integer primary key autoincrement, " +
								    "Keyindex tinyint, " +
								         "Keycounter varchar(20) ); ");

				adobeDB.execSQL("UPDATE '" + tableName + "' SET " +
					    "Keycounter = '" + keycounter + "' " + 
					             " WHERE Keyindex = " + keyindex + ";");
			} 
			catch (SQLiteException se) 
			{
				Log.e(dataBaseName,"Could not create or open the database " + se.getMessage());
			} 
			finally 
			{
				if (adobeDB != null)
				{
					adobeDB.close();
				} 
			}  
		}
		
		
		public void setClickSound(Context cont, boolean tick)
		{
			AudioManager mAudioManager = (AudioManager)cont.getSystemService(Context.AUDIO_SERVICE);
			if(tick)
		        mAudioManager.loadSoundEffects();
			else
		        mAudioManager.unloadSoundEffects();
		}

 }


