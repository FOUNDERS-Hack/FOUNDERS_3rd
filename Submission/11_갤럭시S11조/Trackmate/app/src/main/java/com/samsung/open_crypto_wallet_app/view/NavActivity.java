package com.samsung.open_crypto_wallet_app.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.samsung.open_crypto_wallet_app.DBManager;
import com.samsung.open_crypto_wallet_app.R;
import com.samsung.open_crypto_wallet_app.SharedPreferenceManager;
import com.samsung.open_crypto_wallet_app.Util;
import com.samsung.open_crypto_wallet_app.model.AccountModel;
import com.samsung.open_crypto_wallet_app.model.AccountModelAdapter;
import com.samsung.open_crypto_wallet_app.view_model.AccountViewModel;

import java.util.ArrayList;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AccountModelAdapter mAccountModelAdapter;
    private Spinner mAccountSpinner;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = this;

        // Initial UI Task
        setContentView(R.layout.activity_nav);
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.menu_dashboard);
        setSupportActionBar(mToolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Sending Activity Instance to Classes that require Activity to Perform Operations
        AlertUtil.setNavActivityInstance(this);
        DBManager.setNavActivityInstance(this);
        AccountViewModel.setNavActivityInstance(this);
        SettingsFragment.setNavActivityInstance(this);

        //Spinner for Account Switch, Initiating with Blank Account Model
        View headerView = navigationView.getHeaderView(0);
        mAccountSpinner = headerView.findViewById(R.id.accountChooserSpinner);
        addAccountSpinnerOnItemSelectedListener();
        mAccountModelAdapter = new AccountModelAdapter(headerView.getContext(), new ArrayList<AccountModel>());
        mAccountSpinner.setAdapter(mAccountModelAdapter);           //Blank Array list to avoid null pointer exception

        Log.i(Util.LOG_TAG, "Nav Activity UI Load Successful.");

        /*Populate Account On Activity Launch.
          This Method Initiates Operations:
          - Checks SeedHash, Retrieves account (Public Address) from SBK
          - Stores Account to DB and fetch
          - Updates Account Dashboard */
        AccountViewModel.populateAllAccounts();

        launchDefaultFragment();
    }

    public void addAccountSpinnerOnItemSelectedListener() {
        mAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AccountModel selectedAccount = (AccountModel) parent.getItemAtPosition(position);
                SharedPreferenceManager.setDefaultAccountID(mActivity, selectedAccount.getAccountId());
                AccountViewModel.setDefaultAccount(selectedAccount);
                closeDrawer();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void refreshUIList(ArrayList<AccountModel> accountList) {
        // Refreshes Account Spinner Based on Update
        runOnUiThread(() -> {
            mAccountModelAdapter.clear();
            mAccountModelAdapter.addAll(accountList);
            mAccountModelAdapter.notifyDataSetChanged();
            //<Array Selection's indexing starts from 0> where as <DB and SharedPreference's indexing starts from 1>
            mAccountSpinner.setSelection(SharedPreferenceManager.getDefaultAccountID(mActivity) - 1);
            closeDrawer();
        });
    }

    private void closeDrawer() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawers();
        }
    }

    public void launchFragment(Fragment fragmentToLaunch) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentToLaunch).commit();
    }

    private void launchDefaultFragment() {
        Fragment fragmentToLaunch = new DashboardFragment();
        launchFragment(fragmentToLaunch);
    }

    public void launchTransactionHistoryFragment() {
        mToolbar.setTitle(R.string.menu_transaction_history);
        launchFragment(new TransactionHistoryFragment());
    }

    public void launchSendEtherFragment() {
        mToolbar.setTitle(R.string.menu_send_ether);
        launchFragment(new SendEtherFragment());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (currentFragment.getClass().equals(DashboardFragment.class)) super.onBackPressed();
            else
                launchDefaultFragment();   // Back Press from 'Send Ether'/'Transaction History'/'Settings' will reload Dashboard
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragmentToLaunch = null;

        if (id == R.id.nav_dashboard) {
            mToolbar.setTitle(R.string.menu_dashboard);
            fragmentToLaunch = new DashboardFragment();
        } else if (id == R.id.nav_send_ether) {
            mToolbar.setTitle(R.string.menu_send_ether);
            fragmentToLaunch = new SendEtherFragment();

        } else if (id == R.id.nav_transaction_history) {
            mToolbar.setTitle(R.string.menu_transaction_history);
            fragmentToLaunch = new TransactionHistoryFragment();
        } else if (id == R.id.nav_settings) {
            mToolbar.setTitle(R.string.menu_settings);
            fragmentToLaunch = new SettingsFragment();
        }

        if (fragmentToLaunch != null) {
            launchFragment(fragmentToLaunch);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Button Actions for Buttons on NavActivity
    public void onClickTransferFunds(View view) {
        launchSendEtherFragment();
    }

    public void onClickTransactionHistory(View view) {
        launchTransactionHistoryFragment();
    }

    public void onClickAddAccount(View view) {

        if(!SharedPreferenceManager.getUseOtherKeyManager(mActivity)) {
            AlertUtil.addNewAccountDialog(this);
        } else {
            AlertUtil.addAccountNotSupportedDialog(this );
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }
}