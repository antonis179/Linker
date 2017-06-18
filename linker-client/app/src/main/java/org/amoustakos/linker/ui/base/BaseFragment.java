package org.amoustakos.linker.ui.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.amoustakos.linker.LinkerApplication;
import org.amoustakos.linker.injection.component.ConfigPersistentComponent;
import org.amoustakos.linker.injection.component.DaggerConfigPersistentComponent;
import org.amoustakos.linker.injection.component.FragmentComponent;
import org.amoustakos.linker.injection.module.FragmentModule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import timber.log.Timber;


public class BaseFragment extends Fragment{

    private static final String KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();

    private FragmentComponent mFragmentComponent;
    private long mFragmentId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_FRAGMENT_ID) : NEXT_ID.getAndIncrement();

        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(mFragmentId)) {
            Timber.d("Creating new ConfigPersistentComponent id=%d", mFragmentId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(LinkerApplication.get(getActivity()).getComponent())
                    .build();
            sComponentsMap.put(mFragmentId, configPersistentComponent);
        } else {
            Timber.d("Reusing ConfigPersistentComponent id=%d", mFragmentId);
            configPersistentComponent = sComponentsMap.get(mFragmentId);
        }
        mFragmentComponent = configPersistentComponent.fragmentComponent(new FragmentModule(this));
    }

    @Override
    public void onDestroy() {
        Timber.d("Clearing ConfigPersistentComponent id=%d", mFragmentId);
        sComponentsMap.remove(mFragmentId);
        super.onDestroy();
    }

    public FragmentComponent fragmentComponent() {
        return mFragmentComponent;
    }



//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//    }
//
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
}
