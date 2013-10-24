package com.cerri.foodshop.data.db;

import com.cerri.foodshop.data.model.ModelEntity;

public class NewsDAO extends DAO {

	public static final String NEWS_TABLE = "news";
	
	public static final String NEWS_ID_NEWS = "id_news";
	public static final String NEWS_DATE = "date";
	public static final String NEWS_CAPTION = "caption";
	public static final String NEWS_DESCRIPTION = "description";
	public static final String NEWS_PIC = "pic";
	
//	long selectedImageUri = ContentUris.parseId(Uri.parse(anniEntry.getUri()));
//	 Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(
//	                    mContext.getContentResolver(), selectedImageUri,MediaStore.Images.Thumbnails.MICRO_KIND,
//	                    null );
	
	public static final String NEWS_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " 
			+ NEWS_TABLE + " ("
			+ NEWS_ID_NEWS + " integer primary key autoincrement, "
			+ NEWS_DATE + " text, "
			+ NEWS_CAPTION + " text, "
			+ NEWS_DESCRIPTION + " text, "
			+ NEWS_PIC + " text ) ";

	public static long persist(DbManager db, ModelEntity entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	public static ModelEntity findById(DbManager db, int id) {
		// TODO Auto-generated method stub
		return null;
	}

}
