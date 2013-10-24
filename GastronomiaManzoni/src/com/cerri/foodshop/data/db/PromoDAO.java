package com.cerri.foodshop.data.db;

import com.cerri.foodshop.data.model.ModelEntity;

public class PromoDAO extends DAO {

	public static final String PROMO_TABLE = "PROMO";
	
	public static final String PROMO_ID_PROMO = "id_PROMO";
	public static final String PROMO_START_DATE = "start_date";
	public static final String PROMO_END_DATE = "end_date";
	public static final String PROMO_DESCRIPTION = "description";
	public static final String PROMO_ID_MEAL = "id_meal";
	
//	long selectedImageUri = ContentUris.parseId(Uri.parse(anniEntry.getUri()));
//	 Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(
//	                    mContext.getContentResolver(), selectedImageUri,MediaStore.Images.Thumbnails.MICRO_KIND,
//	                    null );
	
	public static final String PROMO_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " 
			+ PROMO_TABLE + " ("
			+ PROMO_ID_PROMO + " integer primary key autoincrement, "
			+ PROMO_START_DATE + " text, "
			+ PROMO_END_DATE + " text, "
			+ PROMO_DESCRIPTION + " text, "
			+ PROMO_ID_MEAL + " text ) ";

	public static long persist(DbManager db, ModelEntity entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	public static ModelEntity findById(DbManager db, int id) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
