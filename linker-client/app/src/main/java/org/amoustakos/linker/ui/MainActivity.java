package org.amoustakos.linker.ui;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.amoustakos.linker.R;
import org.amoustakos.linker.io.DataManager;
import org.amoustakos.linker.io.models.base.BaseResponse;
import org.amoustakos.linker.io.models.request.LinkRequest;
import org.amoustakos.linker.ui.base.BaseActivity;
import org.amoustakos.linker.util.RxUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Inject
    DataManager dataManager;

    ActionBarDrawerToggle mToggle;
    @BindView(R.id.drawer_layout)   DrawerLayout mDrawer;
    @BindView(R.id.fab)             FloatingActionButton fab;
    @BindView(R.id.toolbar)         Toolbar mToolbar;

    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = ButterKnife.findById(this, R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fab.setOnClickListener(v -> {
                    test();
                    Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
        );

        if (savedInstanceState == null) {
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_dashboard));
            //TESTING
//            requestAllPermissions();
//            registerLocationRetriever();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*
     ************ TESTING
     */
    private void test(){
        //SEND LINK
        LinkRequest req = new LinkRequest();
        req.link = "www.google.gr";

        dataManager.sendLink("192.168.1.126", "8090", req)
                    .compose(RxUtil.applyDefaultSchedulers())
                    .doOnNext(baseResponse -> {
                        if (baseResponse == null) return;
                        if(baseResponse.statusCode >= 0)
                            Timber.i(baseResponse.statusMessage);
                        else
                            Timber.e(baseResponse.statusMessage);
                    })
                    .doOnError(Throwable::printStackTrace)
                    .onErrorReturn(throwable -> {
                        BaseResponse br = new BaseResponse();
                        br.statusMessage = throwable.getMessage();
                        br.statusCode = -1;
                        return br;
                    })
                    .subscribe();
    }
    /*
     ************ TESTING
     */

    /*
     * Menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
//                startActivity(new Intent(this, SettingsActivity.class));
//                return true;
                break;
            case R.id.action_exit:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * Navigation
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getFragmentManager();
        switch(item.getItemId()){
            case R.id.nav_dashboard:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new DashboardFragment()).commit();
                break;
            default:
                break;
        }

        item.setChecked(true);
        setTitle(item.getTitle());
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*
     * Generic
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START))
            mDrawer.closeDrawer(GravityCompat.START);
        else {
            if (isDoubleBackToExitPressedOnce()) {
                super.onBackPressed();
                return;
            }
            setDoubleBackToExitPressedOnce(true);
            Toast.makeText(this, getString(R.string.back_to_exit), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(()->setDoubleBackToExitPressedOnce(false), 2000);
        }
    }

    /*
     * Getters - Setters
     */
    private synchronized boolean isDoubleBackToExitPressedOnce() {
        return doubleBackToExitPressedOnce;
    }
    private synchronized void setDoubleBackToExitPressedOnce(boolean doubleBackToExitPressedOnce) {
        this.doubleBackToExitPressedOnce = doubleBackToExitPressedOnce;
    }
}
