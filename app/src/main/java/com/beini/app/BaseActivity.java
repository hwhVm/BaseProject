package com.beini.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beini.R;
import com.beini.bind.ContentView;
import com.beini.bind.ViewInject;
import com.beini.bind.ViewInjectorImpl;
import com.beini.ui.fragment.home.HomeFragment;
import com.beini.ui.fragment.home.Rb2Fragment;
import com.beini.ui.fragment.home.Rb3Fragment;
import com.beini.util.BLog;
import com.beini.util.FragmentHelper;
import com.beini.util.ObjectUtil;
import com.beini.util.WindowUtils;
import com.beini.util.listener.ActivityResultListener;
import com.beini.util.listener.KeyBackListener;
import com.beini.util.listener.OnTouchEventListener;

/**
 * Created by beini on 2017/2/8.
 */

@ContentView(R.layout.activity_base)
public abstract class BaseActivity extends AppCompatActivity implements BaseImpl {
    @ViewInject(R.id.toolbar)
    public Toolbar toolbar;
    @ViewInject(R.id.layout_coor)
    LinearLayout layout_coor;
    @ViewInject(R.id.top_bar_title)
    TextView top_bar_title;
    @ViewInject(R.id.navigation)
    BottomNavigationView navigation;

    private FragmentManager fragmentManager;
    private KeyBackListener keyBackListener;
    private ActivityResultListener activityResultListener;

    private OnTouchEventListener onTouchEventListener;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            WindowUtils.setHide(this.getWindow().getDecorView());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        fragmentManager = getFragmentManager();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);  //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);  //透明导航栏
        ViewInjectorImpl.registerInstance(this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        this.initView();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            BLog.d(" item.getItemId()="+item.getItemId());
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(HomeFragment.class);
                    FragmentHelper.homeTag = 0;
                    return true;
                case R.id.navigation_dashboard:
                    replaceFragment(Rb2Fragment.class);
                    FragmentHelper.homeTag = 1;
                    return true;
                case R.id.navigation_notifications:
                    replaceFragment(Rb3Fragment.class);
                    FragmentHelper.homeTag = 2;
                    return true;
            }
            return false;
        }

    };


    public abstract void initView();

    @Override
    public void replaceFragment(Class<?> fragment) {
        BaseFragment baseFragment = (BaseFragment) ObjectUtil.createInstance(fragment);
        if (!(baseFragment instanceof HomeFragment || baseFragment instanceof Rb2Fragment || baseFragment instanceof Rb3Fragment)) {
            this.setBottom(View.GONE);
        }
        Fragment newFragment = fragmentManager.findFragmentByTag(fragment.getName());
        if (newFragment != null) {
            FragmentHelper.showFragment(fragmentManager, newFragment);
        } else {
            FragmentHelper.hideAllFragment(fragmentManager);
            FragmentHelper.addFragment(fragmentManager, baseFragment, fragment.getName());
        }
    }
    @Override
    public void replaceFragment(Class<?> fragment, Bundle args) {
        BaseFragment baseFragment = (BaseFragment) ObjectUtil.createInstance(fragment);
        if (!(baseFragment instanceof HomeFragment || baseFragment instanceof Rb2Fragment || baseFragment instanceof Rb3Fragment)) {
            this.setBottom(View.GONE);
        }
        Fragment newFragment = fragmentManager.findFragmentByTag(fragment.getName());
        if (newFragment != null) {
            FragmentHelper.showFragment(fragmentManager, newFragment);
        } else {
            baseFragment.setArguments(args);
            FragmentHelper.hideAllFragment(fragmentManager);
            FragmentHelper.addFragment(fragmentManager, baseFragment, fragment.getName());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyBackListener != null) {
                keyBackListener.keyBack();
            } else {
                onBackPressed();
            }
            return true;
        } else {
            if (keyBackListener != null) {
                keyBackListener.onKeyDown(event);
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BLog.d("   onActivityResult ");
        if (activityResultListener != null) {
            BLog.d("   ----dd------------> ");
            activityResultListener.resultCallback(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentHelper.removePreFragment(layout_coor, fragmentManager, this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (onTouchEventListener != null) {
            onTouchEventListener.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void back() {
        FragmentHelper.removePreFragment(layout_coor, fragmentManager, this);
    }

    @Override
    public void remove(Fragment fragment) {
        FragmentHelper.removeFragment(fragmentManager, fragment);
    }

    @Override
    public void setBottom(int isHide) {
        navigation.setVisibility(isHide);
    }

    @Override
    public void setTopBar(int isHide) {
        toolbar.setVisibility(isHide);
    }

    @Override
    public void setTopBarTitle(String title) {
        top_bar_title.setText(title);
    }


    public KeyBackListener getKeyBackListener() {
        return keyBackListener;
    }

    public void setKeyBackListener(KeyBackListener keyBackListener) {
        this.keyBackListener = keyBackListener;
    }

    public OnTouchEventListener getOnTouchEventListener() {
        return onTouchEventListener;
    }

    public void setOnTouchEventListener(OnTouchEventListener onTouchEventListener) {
        this.onTouchEventListener = onTouchEventListener;
    }

    public ActivityResultListener getActivityResultListener() {
        return activityResultListener;
    }

    public void setActivityResultListener(ActivityResultListener activityResultListener) {
        this.activityResultListener = activityResultListener;
    }

    /**
     * 释放activity的资源，例如释放网络连接，注销监听广播接收者
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * UI已经隐藏
     *
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            //释放UI资源
        }
    }
}