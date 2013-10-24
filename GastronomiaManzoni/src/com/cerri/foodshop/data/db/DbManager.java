package com.cerri.foodshop.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbManager {

	public static final String TAG = "DB MANAGER";
	
	SQLiteDatabase mDb;
	DbHelper mDbHelper;
	Context mContext;
	private static final String DB_NAME = "gmdb";
	private static final int DB_VERSION = 1;

	public DbManager(Context ctx){
		mContext = ctx;
		mDbHelper = new DbHelper(ctx, DB_NAME, null, DB_VERSION);
		Log.v(DbManager.TAG, "Db helper created");
	}

	/**
	 * Open the db in write mode
	 */
	public void open(){
		mDb = mDbHelper.getWritableDatabase();
		Log.v(DbManager.TAG, "Connection to db opened");
	}

	/**
	 * Close the db
	 */
	public void close(){
		mDb.close();
	}
	
	public long insert(String table, String nullColumnHack, ContentValues values) {
//		Log.v(TAG , "OPEN: " + mDb.isOpen());
//		Log.v(TAG , "LOCKED: " + mDb.isDbLockedByCurrentThread());
		return mDb.insertOrThrow(table, null, values);
	}
	
	public Cursor findById(String table, String[] columns, String selection, 
			String[] selectionArgs) {
		return mDb.query(table, columns, selection, selectionArgs, null, null, null);
	}
	
	public Cursor findAll(String table, String[] columns) {
		return mDb.query(table, columns, null, null, null, null, null);
	}
	
	public Cursor executeQueryForOneResult(String table, String[] columns, String selection, 
			String[] selectionArgs, String groupBy, String having, String orderBy) {
		return mDb.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, "1");
	}
	
	public int delete(String table,String whereClause, String[] whereArgs) {
		return mDb.delete(table, whereClause, whereArgs);
	}
	
	public void executeInTransaction(String sql) throws SQLException{
		mDb.beginTransaction();
		mDb.execSQL(sql);
		mDb.endTransaction();
		
	}

	/**
	 * Inner class that manage database creation and upgrade
	 * 
	 * @author cerri
	 *
	 */
	private class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DayMealDAO.DAYMEAL_CREATE_TABLE);
			_db.execSQL(NewsDAO.NEWS_CREATE_TABLE);
			_db.execSQL(PromoDAO.PROMO_CREATE_TABLE);
			
			Log.v(DbManager.TAG, "Database created");
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {

		}

	}

}
