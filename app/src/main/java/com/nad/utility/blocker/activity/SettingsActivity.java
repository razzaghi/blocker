package com.nad.utility.blocker.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nad.utility.blocker.R;
import com.nad.utility.blocker.adapter.PageFragmentAdapter;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    private Handler handler;

    private CoordinatorLayout cl_container;
    private FloatingActionButton fab_add;

    private OnAddListener onAddListener;
    private OnMenuItemClickListener onMenuItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PageFragmentAdapter(getFragmentManager(), SettingsActivity.this));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        cl_container = (CoordinatorLayout) findViewById(R.id.cl_container);
        fab_add = (FloatingActionButton) findViewById(R.id.fab_add);
        fab_add.setOnClickListener(this);

        int position = getIntent().getIntExtra("position", 4);
        if (position > -1) {
            viewPager.setCurrentItem(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_all:
            case R.id.filter_call:
            case R.id.filter_sms:
            case R.id.filter_except:
                if (onMenuItemClickListener != null) {
                    onMenuItemClickListener.onFilter(item);
                }
                break;
            case R.id.export:
                if (onMenuItemClickListener != null) {
                    onMenuItemClickListener.onExport(item);
                }
                break;
            case R.id.import0:
                if (onMenuItemClickListener != null) {
                    onMenuItemClickListener.onImport(item);
                }
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_add:
                if (onAddListener != null) {
                    onAddListener.onAdd();
                }
                break;
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_settings;
    }

    public void showTip(int resId, View.OnClickListener onClickListener) {
        Snackbar snackbar = Snackbar.make(cl_container, resId, Snackbar.LENGTH_SHORT);
        if (onClickListener != null) {
            snackbar.setAction(R.string.snackbar_undo, onClickListener);
        }
        snackbar.show();
    }

    public void showTipInThread(final int resId) {
        if (handler == null) {
            handler = new Handler();
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(cl_container, resId, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setOnAddListener(OnAddListener onAddListener) {
        if (onAddListener != null) {
            this.onAddListener = onAddListener;
            fab_add.show();
        } else {
            this.onAddListener = null;
            fab_add.hide();
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public interface OnAddListener {
        void onAdd();
    }

    public interface OnMenuItemClickListener {
        void onFilter(MenuItem item);

        void onExport(MenuItem item);

        void onImport(MenuItem item);
    }
}
