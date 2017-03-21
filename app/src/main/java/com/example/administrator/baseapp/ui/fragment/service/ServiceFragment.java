package com.example.administrator.baseapp.ui.fragment.service;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View;

import com.example.administrator.baseapp.R;
import com.example.administrator.baseapp.base.BaseFragment;
import com.example.administrator.baseapp.bind.ContentView;
import com.example.administrator.baseapp.bind.Event;
import com.example.administrator.baseapp.service.DemoService;
import com.example.administrator.baseapp.utils.BLog;

/**
 * Create by beini  2017/3/21
 */
@ContentView(R.layout.fragment_)
public class ServiceFragment extends BaseFragment {
    private DemoService demoService;

    @Override
    public void initView() {
        BLog.d("     ServiceFragment  ");
        Intent bindIntent = new Intent(baseActivity, DemoService.class);
        baseActivity.bindService(bindIntent, myServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection myServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            demoService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            demoService = ((DemoService.LocalBinder) service).getService();
        }
    };

    @Event({R.id.btn_start_service, R.id.btn_stop_service})
    private void mEvent(View view) {
        switch (view.getId()) {
            case R.id.btn_start_service:
                BLog.d("     btn_start_my_service  ");
                Intent startIntent = new Intent(baseActivity, DemoService.class);
                baseActivity.startService(startIntent);
                break;
            case R.id.btn_stop_service:
                BLog.d("     btn_stop_service  ");
                Intent stopIntent = new Intent(baseActivity, DemoService.class);
                baseActivity.stopService(stopIntent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        baseActivity.unbindService(myServiceConnection);
    }
}