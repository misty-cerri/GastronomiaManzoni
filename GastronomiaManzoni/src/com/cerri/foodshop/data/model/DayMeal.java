package com.cerri.foodshop.data.model;

import java.io.Serializable;

public class DayMeal implements ModelEntity, Serializable {
	
	/**
	 * First stable version
	 */
	private static final long serialVersionUID = -5638989651670549537L;
	
	private long id;
	private long idMeal;
	private long date;
	private String name;
	private String caption;
	private String description;
	private String pic;
	private String ingredients;
	private boolean vegan;
	private boolean vegetarian;
	private boolean glutenFree;
	
	public DayMeal(long idMeal, long date, String name, String caption,
			String description, String pic, String ingredients, boolean vegan,
			boolean vegetarian, boolean glutenFree) {
		super();
		this.idMeal = idMeal;
		this.date = date;
		this.name = name;
		this.caption = caption;
		this.description = description;
		this.pic = pic;
		this.ingredients = ingredients;
		this.vegan = vegan;
		this.vegetarian = vegetarian;
		this.glutenFree = glutenFree;
	}
	
	public DayMeal(long id, long idMeal, long date, String name, String caption,
			String description, String pic, String ingredients, boolean vegan,
			boolean vegetarian, boolean glutenFree) {
		super();
		this.idMeal = idMeal;
		this.id = id;
		this.date = date;
		this.name = name;
		this.caption = caption;
		this.description = description;
		this.pic = pic;
		this.ingredients = ingredients;
		this.vegan = vegan;
		this.vegetarian = vegetarian;
		this.glutenFree = glutenFree;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getIdMeal() {
		return idMeal;
	}

	public void setIdMeal(long idMeal) {
		this.idMeal = idMeal;
	}

	public long getDate() {
		return date;
	}

	public void setDte(long date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public boolean isVegan() {
		return vegan;
	}

	public void setVegan(boolean vegan) {
		this.vegan = vegan;
	}

	public boolean isVegetarian() {
		return vegetarian;
	}

	public void setVegetarian(boolean vegetarian) {
		this.vegetarian = vegetarian;
	}

	public boolean isGlutenFree() {
		return glutenFree;
	}

	public void setGlutenFree(boolean glutenFree) {
		this.glutenFree = glutenFree;
	}
	
}
