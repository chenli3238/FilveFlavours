package suzhou.dataup.cn.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import suzhou.dataup.cn.myapplication.adputer.SimpleFragmentPagerAdapter;
import suzhou.dataup.cn.myapplication.utiles.LogUtil;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.sliding_tabs)
    TabLayout mSlidingTabs;
    @InjectView(R.id.viewpager)
    ViewPager mViewpager;
    @InjectView(R.id.appbar)
    AppBarLayout mAppbar;
    @InjectView(R.id.navigationView)
    NavigationView mNavigationView;
    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    private SimpleFragmentPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("简约");
        toolbar.setTitleTextColor(Color.WHITE);
        initCount();
        // initObse();

    }

    private void initObse() {
        //目前不知道原因，如果这里不写成链式结构设置在子线程无效！
        Observable<String> stringObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello, world!");
                subscriber.onCompleted();
                LogUtil.e("mySubscribersssss" + Thread.currentThread().getName());
            }
            //设置运行执行逻辑的时候在Io线程可以执行耗时的操作,回显结果在主线程！
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                System.out.println(s);
                LogUtil.e("mySubscriber" + s + Thread.currentThread().getName());
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }
        };
        //myObservable订阅mySubscriber
        stringObservable.subscribe(mySubscriber);
    }

    private void initCount() {
        //实现和侧滑栏联动效果
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        //==================================================================
        //加上这个放置报错android.os.networkonmainthreadexception
        //   StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        // StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        pagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        //
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    //监听侧滑栏的点击条目位置
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();
        //我的博客
        if (id == R.id.nav_blog) {
            Uri uri = Uri.parse("http://l123456789jy.github.io/blog/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            //版本信息
        } else if (id == R.id.nav_ver) {
            //关于我
        } else if (id == R.id.nav_about) {
            Uri uri = Uri.parse("https://github.com/l123456789jy");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            //退出应用
        } else if (id == R.id.sub_exit) {
            System.exit(0);
            //切换主题
        } else if (id == R.id.sub_switch) {


        }
        //关闭侧滑栏
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
