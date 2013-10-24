package com.cerri.foodshop.data.db;

import com.cerri.foodshop.data.model.ModelEntity;

public abstract class DAO {

	/**
	 * Persist the entity in the db
	 * @param db
	 * @param entity
	 * @return
	 */
	public static long persist(DbManager db, ModelEntity entity) {
		return -1;
	}
	
	/**
	 * Check if an entity is already in the db
	 * @param db
	 * @param entity
	 * @return
	 */
	public static boolean isAlreadyPresent(DbManager db, ModelEntity entity) {
		return false;
	}

	/**
	 * Delete the entity from the db
	 * @param db
	 * @param id
	 * @return
	 */
	public static boolean delete(DbManager db, int id) {
		return false;
	}
	
	/**
	 * Find the entity by its id
	 * @param db
	 * @param id
	 * @return
	 */
	public static ModelEntity findById(DbManager db, int id) {
		return null;
	}
	
}
