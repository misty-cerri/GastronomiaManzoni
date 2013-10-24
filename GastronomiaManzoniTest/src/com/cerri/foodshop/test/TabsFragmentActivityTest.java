package com.cerri.foodshop.test;

import com.cerri.foodshop.ui.activities.DayMealDetailActivity;
import com.cerri.foodshop.ui.activities.TabsFragmentActivity;
import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.GridView;

public class TabsFragmentActivityTest extends
	ActivityInstrumentationTestCase2<TabsFragmentActivity> {

	private Solo solo;
	
	public TabsFragmentActivityTest() {
		super(TabsFragmentActivity.class);
	}

	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testNavigation() throws Exception {
	    
		solo.assertCurrentActivity("Wrong activity active", TabsFragmentActivity.class);
	    assertTrue(solo.waitForText(solo.getString(com.cerri.foodshop.R.string.waiting_connection), 1, 5000));
	    
//	    assertTrue(
//	    		solo.waitForText(solo.getString(com.cerri.foodshop.R.string.error_downloading_day_meals), 1, 20000)
//	    		|| solo.waitForText(solo.getString(com.cerri.foodshop.R.string.error_no_day_meal_downloaded), 1, 20000));
	    
	    solo.waitForDialogToClose();
	    
//	    solo.clickOnText(solo.getString(com.cerri.foodshop.test.R.string.test_daymeal_name));
	    solo.waitForText("Piatto test", 1, 5000);
	    solo.clickOnText("Piatto test");
//	    assertTrue(solo.waitForText(solo.getString(com.cerri.foodshop.test.R.string.test_daymeal_description)));
	    assertTrue(solo.waitForText("Descrizione piatto test"));
	    solo.assertCurrentActivity("Wrong activity active", DayMealDetailActivity.class);
	    solo.assertMemoryNotLow();
	    solo.goBack();
	    solo.clickOnText(solo.getString(com.cerri.foodshop.R.string.tab_shop_info_title));
	    assertTrue(solo.waitForText(solo.getString(com.cerri.foodshop.R.string.shop_info_email), 1, 5000));
	    solo.assertMemoryNotLow();
	    solo.clickOnView(((GridView)solo.getView(com.cerri.foodshop.R.id.shop_info_gallery_gridview)).getChildAt(0));
	    solo.clickOnView(solo.getView(com.cerri.foodshop.R.id.shop_info_expanded_image));
	    solo.assertMemoryNotLow();
	    solo.clickOnText(solo.getString(com.cerri.foodshop.R.string.shop_info_navigate));
	}
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}
