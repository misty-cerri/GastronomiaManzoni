package com.cerri.foodshop.ui.activities;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.cerri.foodshop.R;
import com.cerri.foodshop.data.model.DayMeal;
import com.cerri.foodshop.util.BitmapManager;
import com.cerri.foodshop.util.constants.Constants;

public class DayMealDetailActivity extends Activity {

	public static final String TAG = "DayMealDetailActivity";
	
//	private AdView adView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.day_meal_detail_layout);
		
		DayMeal dayMeal = (DayMeal)getIntent().getSerializableExtra(Constants.INTENT_KEY_DAY_MEAL);
		
		TextView mealNameView = (TextView)findViewById(R.id.day_meal_detail_name);
		mealNameView.setText(dayMeal.getName());
		mealNameView.setGravity(Gravity.CENTER_VERTICAL);
		mealNameView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.day_meal_brown, 0, 0, 0);
		
		LinearLayout layout;
		String[] ingredientsArray = dayMeal.getIngredients().split("\\|");
		String ingredients = "";
		for(int i = 0; i < ingredientsArray.length; i++) {
			ingredients += "- " + ingredientsArray[i];
			if(i != ingredientsArray.length - 1) {
				ingredients += "\n";
			}
		}

		((TextView)findViewById(R.id.day_meal_detail_pic_ingredients)).setText(ingredients);
		((TextView)findViewById(R.id.day_meal_detail_pic_ingredients)).setCompoundDrawablePadding(10);
		Drawable d = new BitmapDrawable(getResources(),BitmapManager.INSTANCE.getBitmapOrPlaceholderFromCache(dayMeal.getPic()));
		((TextView)findViewById(R.id.day_meal_detail_pic_ingredients)).setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
		 
		((TextView)findViewById(R.id.day_meal_detail_description)).setText(dayMeal.getDescription());

		if(dayMeal.isGlutenFree()) {
			layout = (LinearLayout)findViewById(R.id.day_meal_detail_layout);
			TextView glutenFreeView = new TextView(this);
			glutenFreeView.setText(getString(R.string.day_meal_gluten_free));
			glutenFreeView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			glutenFreeView.setPadding(5, 5, 5, 5);
			glutenFreeView.setCompoundDrawablePadding(5);
			glutenFreeView.setGravity(Gravity.CENTER_VERTICAL);
			glutenFreeView.setTextColor(getResources().getColor(R.color.caption));
			glutenFreeView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.glutenfree, 0, 0, 0);
			layout.addView(glutenFreeView);
		}
		if(dayMeal.isVegan()) {
			layout = (LinearLayout)findViewById(R.id.day_meal_detail_layout);
			TextView veganView = new TextView(this);
			veganView.setText(getString(R.string.day_meal_vegan));
			veganView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			veganView.setPadding(5, 5, 5, 5);
			veganView.setCompoundDrawablePadding(5);
			veganView.setGravity(Gravity.CENTER_VERTICAL);
			veganView.setTextColor(getResources().getColor(R.color.caption));
			veganView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.vegan, 0, 0, 0);
			layout.addView(veganView);
		}
		if(dayMeal.isVegetarian()) {
			layout = (LinearLayout)findViewById(R.id.day_meal_detail_layout);
			TextView vegetarianView = new TextView(this);
			vegetarianView.setText(getString(R.string.day_meal_vegetarian));
			vegetarianView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			vegetarianView.setPadding(5, 5, 5, 5);
			vegetarianView.setCompoundDrawablePadding(5);
			vegetarianView.setGravity(Gravity.CENTER_VERTICAL);
			vegetarianView.setTextColor(getResources().getColor(R.color.caption));
			vegetarianView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.vegetarian, 0, 0, 0);
			layout.addView(vegetarianView);
		}
		
		/*
		 * Add AdMob view
		 */
//	    adView = new AdView(this, AdSize.SMART_BANNER, getResources().getString(R.string.ad_unit_id));
//	    adView.loadAd(AdMobHelper.adRequest);
	    
//	    LinearLayout layoutOuter = (LinearLayout)findViewById(R.id.day_meal_detail_layout_out);
//	    adView.setGravity(Gravity.BOTTOM);
//	    adView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//	    layoutOuter.addView(adView);

	    
	}
}