package com.beini.ui.activity;

import com.beini.base.BaseActivity;
import com.beini.ui.fragment.home.HomeFragment;

/**
 * Created by beini on 2017/2/9.
 */

public class IndexActivity extends BaseActivity {

    @Override
    public void initView() {
//        SqlScoutServer.create(this, getPackageName());
//        replaceFragment(LoginFragment.class);
        replaceFragment(HomeFragment.class);
    }

}