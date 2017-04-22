package org.amoustakos.linker.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import org.amoustakos.linker.LinkerApplication;
import org.amoustakos.linker.injection.component.ActivityComponent;
import org.amoustakos.linker.injection.component.ConfigPersistentComponent;
import org.amoustakos.linker.injection.component.DaggerConfigPersistentComponent;
import org.amoustakos.linker.injection.module.ActivityModule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();

    private ActivityComponent mActivityComponent;
    private long mActivityId;

    private ViewGroup rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();

        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(mActivityId)) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(LinkerApplication.get(this).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = sComponentsMap.get(mActivityId);
        }
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        rootView = (ViewGroup)
                ((ViewGroup) ButterKnife.findById(this, android.R.id.content))
                        .getChildAt(0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId);
            sComponentsMap.remove(mActivityId);
        }
        super.onDestroy();
    }

    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }

    public ViewGroup getRootView() {
        return rootView;
    }
}
