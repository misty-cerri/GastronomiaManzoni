package com.cerri.foodshop.ui.activities;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.cerri.foodshop.R;
import com.cerri.foodshop.batch.receivers.AlarmStarterReceiver;
import com.cerri.foodshop.data.db.DbManager;
import com.cerri.foodshop.ui.fragments.DayMealFragment;
import com.cerri.foodshop.ui.fragments.ShopInfoFragment;

public class TabsFragmentActivity extends FragmentActivity implements OnTabChangeListener {

	public static final String TAG = "TabsFragmentActivity";

	private TabHost mTabHost;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, TabInfo>();
	private TabInfo mLastTab = null;
	private DbManager db;
	
	public static final String TAB_DAY_MEAL = "TAB_DAY_MEAL";
//	public static final String TAB_NEWS = "TAB_NEWS";
//	public static final String TAB_PERSONAL_AREA = "TAB_PERSONAL_AREA";
	public static final String TAB_SHOP_INFO = "TAB_SHOP_INFO";
	public static final String TAB_TO_OPEN = "TAB_TO_OPEN";
	public static String currentTab = TAB_DAY_MEAL;

	@SuppressWarnings("rawtypes")
	private class TabInfo {
		private String tag;
		private Class clss;
		private Bundle args;
		private Fragment fragment;
		TabInfo(String tag, Class clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}
	}

	class TabFactory implements TabContentFactory {

		private final Context mContext;

		public TabFactory(Context context) {
			mContext = context;
		}

		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*
		 * Show status bar (with menu button) only from api level 14 (4.0) 
		 */
//		if(Build.VERSION.SDK_INT <= 13) {
//			requestWindowFeature(Window.FEATURE_NO_TITLE);
//		}
		
		/*
		 * Setup configuration values
		 */
//		ConfigManager.setUpConfiguration(getBaseContext());

		/*
		 * Open connection to the database
		 */
		db = new DbManager(getApplicationContext());
		db.open();

		//		DayMealDAO.insertValuesForTesting(db);

		/*
		 * Initialize layout
		 */
		setContentView(R.layout.tabs_layout);
		initializeTabHost(savedInstanceState);
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString(TAB_TO_OPEN)); //set the tab as per the saved state
		} else  {
			mTabHost.setCurrentTabByTag(currentTab);
		}

		/*
		 * Start the day meal alarm
		 */
		AlarmStarterReceiver.startDayMealAlarm(this);
	}

	private void initializeTabHost(Bundle args) {

		mTabHost = (TabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;

		/*
		 * Day meal tab
		 */
		View dayMealView = LayoutInflater.from(this).inflate(R.layout.tab_day_meal_layout, null);
		TextView tv = (TextView)dayMealView.findViewById(R.id.tab_day_meal_text_text);
		tv.setText(getString(R.string.tab_day_meal_title));
		tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_day_meal_tab, 0, 0, 0);
		TabsFragmentActivity.addTab(this, this.mTabHost, 
				this.mTabHost.newTabSpec(TAB_DAY_MEAL).setIndicator(dayMealView), 
				( tabInfo = new TabInfo(TAB_DAY_MEAL, DayMealFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		/*
		 * Shop info tab
		 */
		View shopInfoView = LayoutInflater.from(this).inflate(R.layout.tab_shop_info_layout, null);
		tv = (TextView)shopInfoView.findViewById(R.id.tab_shop_info_text);
		tv.setText(getString(R.string.tab_shop_info_title));
		tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_shop_info_tab, 0, 0, 0);
		TabsFragmentActivity.addTab(this, this.mTabHost, 
				this.mTabHost.newTabSpec(TAB_SHOP_INFO).setIndicator(shopInfoView), 
				( tabInfo = new TabInfo(TAB_SHOP_INFO, ShopInfoFragment.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		
		/*
		 * Personal area tab
		 */
//		View personalAreaView = LayoutInflater.from(this).inflate(R.layout.tab_personal_area_layout, null);
//		tv = (TextView)personalAreaView.findViewById(R.id.tab_personal_area_text);
//		tv.setText(getString(R.string.tab_personal_area_title));
//		tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_personal_area_tab, 0, 0, 0);
//		TabsFragmentActivity.addTab(this, this.mTabHost, 
//				this.mTabHost.newTabSpec(TAB_PERSONAL_AREA).setIndicator(personalAreaView), 
//				( tabInfo = new TabInfo(TAB_PERSONAL_AREA, PersonalAreaFragment.class, args)));
//		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		/*
		 * Default to first tab
		 */
		if(args != null && args.getString(TAB_TO_OPEN) != null) {
			this.onTabChanged(args.getString(TAB_TO_OPEN));
		} else {
			this.onTabChanged(currentTab);
		}
		mTabHost.setOnTabChangedListener(this);
	}

	private static void addTab(TabsFragmentActivity activity, TabHost tabHost, 
			TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(activity.new TabFactory(activity));
		String tag = tabSpec.getTag();

		// Check to see if we already have a fragment for this tab, probably
		// from a previously saved state.  If so, deactivate it, because our
		// initial state is that a tab isn't shown.
		tabInfo.fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
		if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
			FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
			ft.detach(tabInfo.fragment);
			ft.commit();
			activity.getSupportFragmentManager().executePendingTransactions();
		}
		tabHost.addTab(tabSpec);
	}

	@Override
	public void onTabChanged(String tag) {

		TabInfo newTab = this.mapTabInfo.get(tag);
		if (mLastTab != newTab) {
			FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
			if (mLastTab != null) {
				if (mLastTab.fragment != null) {
					ft.detach(mLastTab.fragment);
				}
			}
			if (newTab != null) {
				if (newTab.fragment == null) {
					newTab.fragment = Fragment.instantiate(this,
							newTab.clss.getName(), newTab.args);
					ft.add(R.id.realtabcontent, newTab.fragment, newTab.tag);
				} else {
					ft.attach(newTab.fragment);
				}
			}

			mLastTab = newTab;
			ft.commit();
			currentTab = newTab.tag;
			this.getSupportFragmentManager().executePendingTransactions();
		}
	}

	@Override        
	protected void onSaveInstanceState(Bundle SavedInstanceState) {
		super.onSaveInstanceState(SavedInstanceState);            
		SavedInstanceState.putString(TAB_TO_OPEN, currentTab);
	}

	public DbManager getDbManager() {
		return this.db;
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_app, menu);
//		return true;
//	}
}
