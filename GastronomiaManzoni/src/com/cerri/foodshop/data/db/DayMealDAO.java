package com.cerri.foodshop.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.cerri.foodshop.data.model.DayMeal;
import com.cerri.foodshop.data.model.ModelEntity;

public class DayMealDAO extends DAO {

	public static final String TAG = "DayMealDAO";

	public static final String DAYMEAL_TABLE = "day_meal";

	public static final String DAYMEAL_ID_MEAL = "id_meal";
	public static final String DAYMEAL_DATE = "date";
	public static final String DAYMEAL_NAME = "name";
	public static final String DAYMEAL_CAPTION = "caption";
	public static final String DAYMEAL_DESCRIPTION = "description";
	public static final String DAYMEAL_PIC = "pic";
	public static final String DAYMEAL_INGREDIENTS = "ingredients";
	public static final String DAYMEAL_VEGAN = "vegan";
	public static final String DAYMEAL_VEGETARIAN = "vegetarian";
	public static final String DAYMEAL_GLUTENFREE = "glutenfree";

	public static final String[] DAYMEAL_COLUMNS = {DAYMEAL_ID_MEAL, DAYMEAL_DATE, DAYMEAL_NAME, DAYMEAL_CAPTION,
		DAYMEAL_DESCRIPTION, DAYMEAL_PIC, DAYMEAL_INGREDIENTS, DAYMEAL_VEGAN,
		DAYMEAL_VEGETARIAN, DAYMEAL_GLUTENFREE};


	//	long selectedImageUri = ContentUris.parseId(Uri.parse(anniEntry.getUri()));
	//	 Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(
	//	                    mContext.getContentResolver(), selectedImageUri,MediaStore.Images.Thumbnails.MICRO_KIND,
	//	                    null );

	public static final String DAYMEAL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " 
			+ DAYMEAL_TABLE + " ("
			+ DAYMEAL_ID_MEAL + " bigint not null, "
			+ DAYMEAL_DATE + " bigint not null, "
			+ DAYMEAL_NAME + " text not null, "
			+ DAYMEAL_CAPTION + " text, "
			+ DAYMEAL_DESCRIPTION + " text, "
			+ DAYMEAL_PIC + " text, "
			+ DAYMEAL_INGREDIENTS + " text, "
			+ DAYMEAL_VEGAN + " integer, "
			+ DAYMEAL_VEGETARIAN + " integer, "
			+ DAYMEAL_GLUTENFREE + " integer ) ";
	
	public static final String DAYMEAL_FIND_LAST_UPDATE = "SELECT ? FROM " + DAYMEAL_TABLE;

	public static long findLastUpdate(DbManager db) {
		Cursor cursor = db.executeQueryForOneResult(DAYMEAL_TABLE, 
				new String[]{"MAX(" + DAYMEAL_DATE + ")"}, null, null, null, null, null);
		cursor.moveToFirst();
		return cursor.getLong(0);
	}

	public static DayMeal[] findAllDayMeals(DbManager db) {
		Cursor cursor = db.findAll(DAYMEAL_TABLE, DAYMEAL_COLUMNS);
		DayMeal[] dayMeals = new DayMeal[cursor.getCount()];
		for(int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			dayMeals[i] = new DayMeal(cursor.getLong(0), cursor.getLong(1), cursor.getString(2), cursor.getString(3),
					cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7) != 0,
					cursor.getInt(8) != 0, cursor.getInt(9) != 0);
		}
		return dayMeals;
	}

	public static DayMeal findById(DbManager db, int id) {
		Cursor cursor = db.findById(DAYMEAL_TABLE, DAYMEAL_COLUMNS, " ROWID = ?", new String[]{String.valueOf(id)});
		if(cursor.getCount() == 0) {
			return null;
		}
		cursor.moveToFirst();
		DayMeal result = new DayMeal(cursor.getLong(0), cursor.getLong(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7) != 0,
				cursor.getInt(8) != 0, cursor.getInt(9) != 0);
		return result;
	}

//	public static DayMeal findByNameAndDate(DbManager db, String name, String date) {
//		Cursor cursor = db.executeQueryForOneResult(DAYMEAL_TABLE, DAYMEAL_COLUMNS, 
//				DAYMEAL_NAME + " = ? and " + DAYMEAL_DAY + " = ?", new String[]{name, date}, null, null, null);
//		cursor.moveToFirst();
//		DayMeal result = new DayMeal(cursor.getInt(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3),
//				cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getInt(7) != 0,
//				cursor.getInt(8) != 0, cursor.getInt(9) != 0);
//		return result;
//	}

//	public static boolean isAlreadyPresent(DbManager db, ModelEntity entity) {
//		Cursor cursor = db.executeQueryForOneResult(DAYMEAL_TABLE, new String[]{DAYMEAL_ID_MEAL}, 
//				DAYMEAL_ID_MEAL + " = ? and " + DAYMEAL_DATE + " = ?", 
//				new String[]{String.valueOf(((DayMeal)entity).getIdMeal()), String.valueOf(((DayMeal)entity).getDay())}, null, null, null);
//		cursor.moveToFirst();
//		if(cursor.getCount() == 0) {
//			return false;
//		}
//		return true;
//	}

	public static long persist(DbManager db, ModelEntity entity) {
		long rowId = -1;
		try {
			Log.v(TAG, "Inserting " + ((DayMeal)entity).getName() + " into db");
			if(isAlreadyPresent(db, entity)) {
				return rowId;
			}
			ContentValues values = new ContentValues();
			values.put(DAYMEAL_ID_MEAL, ((DayMeal)entity).getIdMeal());
			values.put(DAYMEAL_DATE, ((DayMeal)entity).getDate());
			values.put(DAYMEAL_NAME, ((DayMeal)entity).getName());
			values.put(DAYMEAL_CAPTION, ((DayMeal)entity).getCaption());
			values.put(DAYMEAL_DESCRIPTION, ((DayMeal)entity).getDescription());
			values.put(DAYMEAL_PIC, ((DayMeal)entity).getPic());
			values.put(DAYMEAL_INGREDIENTS, ((DayMeal)entity).getIngredients());
			values.put(DAYMEAL_VEGAN, (Integer)(((DayMeal)entity).isVegan() ? 1 : 0));
			values.put(DAYMEAL_VEGETARIAN, (Integer)(((DayMeal)entity).isVegetarian() ? 1 : 0));
			values.put(DAYMEAL_GLUTENFREE, (Integer)(((DayMeal)entity).isGlutenFree() ? 1 : 0));
			rowId = db.insert(DAYMEAL_TABLE, null, values);
			Log.v(TAG, ((DayMeal)entity).getName() + " inserted with rowId " + rowId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowId;
	}
	
	public static long deleteAllDayMeals(DbManager db) {
		long deleted = 0;
		try {
			Log.v(TAG, "Deleting all day meals");
			deleted = db.delete(DAYMEAL_TABLE, "1", null);
			Log.v(TAG, deleted + " rows deleted from table " + DAYMEAL_TABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deleted;
	}
	
//	public static void updateDayMeals(DbManager db, List<DayMeal> dayMeals) throws SQLException {
//		String sql = "DELETE FROM " + DAYMEAL_TABLE + ";";
//		for(DayMeal d : dayMeals) {
//			sql += "INSERT INTO " + DAYMEAL_TABLE + " values ('"
//				+ d.getIdMeal() + "', '" + d.getDate() + "', '" + d.getName() + "', '" + d.getCaption()
//				+ "', '" + d.getDescription() + "', '" + d.getPic() + "', '" + d.getIngredients() 
//				+ "', " + d.isVegan() + ", " + d.isVegetarian() + ", " + d.isGlutenFree() + ");";
//		}
//		db.executeInTransaction(sql);
//	}

	public static void insertValuesForTesting(DbManager db) {
		Log.v(TAG, "Inserting test day meals in database");
		persist(db, new DayMeal(1, 20130601120025L, 
				"Pizza Margherita", 
				"Il piatto forte della cucina napoletana ed italiana. Preparata con ingredienti di prima scelta",
				"Descrizione del piatto. Parole vuote parole vuote parole vuote parole vuote parole vuote",
				"http://upload.wikimedia.org/wikipedia/commons/6/63/Wikipedia-logo.png",
				"Ingredienti ingredienti ingredienti", 
				true, true, false));
		persist(db, new DayMeal(2, 20130601120025L, 
				"Tonnarelli cacio e pepe", 
				"Tonnarelli cacio e pepe",
				"Descrizione del piatto. Parole vuote parole vuote parole vuote parole vuote parole vuote",
				"http://www.terven.altervista.org/images/endSummerFest.png", 
				"Ingredienti ingredienti ingredienti", 
				false, true, false));
		persist(db, new DayMeal(3, 20130601120025L, 
				"Fusi di pollo all'ananas cotti in latte di cocco delle isole Mauritius", 
				"Fusi di pollo all'ananas cotti in latte di cocco delle isole Mauritius",
				"Descrizione del piatto. Parole vuote parole vuote parole vuote parole vuote parole vuote",
				"http://www.terven.altervista.org/images/tervenSelected.png",
				"Ingredienti ingredienti ingredienti", 
				false, false, false));
		persist(db, new DayMeal(4, 20130601120025L, 
				"Cyber egg", 
				"Da assaggiare",
				"Descrizione del piatto. Parole vuote parole vuote parole vuote parole vuote parole vuote",
				"http://www.terven.altervista.org/images/endSummerFest.png",
				"Ingredienti ingredienti ingredienti", 
				false, true, false));
		persist(db, new DayMeal(5, 20130601120025L, 
				"Spaghetti di kamut all'amatriciana", 
				"Buone ma c'è la pancetta",
				"Descrizione del piatto. Parole vuote parole vuote parole vuote parole vuote parole vuote",
				"http://www.terven.altervista.org/images/endSummerFest.png",
				"Ingredienti ingredienti ingredienti", 
				false, false, true));
		persist(db, new DayMeal(6, 20130601120025L, 
				"Pizza Ortolana + ricotta", 
				"Il piatto forte della cucina napoletana ed italiana. Preparata con ingredienti di prima scelta",
				"Descrizione del piatto. Parole vuote parole vuote parole vuote parole vuote parole vuote",
				"http://upload.wikimedia.org/wikipedia/commons/6/63/Wikipedia-logo.png",
				"Ingredienti ingredienti ingredienti", 
				true, true, false));
		persist(db, new DayMeal(7, 20130601120025L, 
				"Tzatziki", 
				"Il piatto forte della cucina napoletana ed italiana. Preparata con ingredienti di prima scelta",
				"Descrizione del piatto. Parole vuote parole vuote parole vuote parole vuote parole vuote",
				"http://upload.wikimedia.org/wikipedia/commons/6/63/Wikipedia-logo.png",
				"Ingredienti ingredienti ingredienti", 
				true, true, false));
		persist(db, new DayMeal(8, 20130601120025L, 
				"Insalata caprese", 
				"Il piatto forte della cucina napoletana ed italiana. Preparata con ingredienti di prima scelta",
				"Descrizione del piatto. Parole vuote parole vuote parole vuote parole vuote parole vuote",
				"http://www.terven.altervista.org/images/tervenSelected.png",
				"Ingredienti ingredienti ingredienti", 
				true, true, false));
		Log.v(DbManager.TAG, "Test day meal rows inserted");
	}

}
