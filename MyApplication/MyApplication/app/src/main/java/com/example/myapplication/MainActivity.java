package com.example.myapplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v4.widget.DrawerLayout;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.Menu1Fragment;
import com.example.myapplication.Menu2Fragment;
import com.example.myapplication.Menu3Fragment;
import com.example.myapplication.SessionManager;
import com.example.myapplication.SignupActivity;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 3개의 메뉴에 들어갈 Fragment들
    private Menu1Fragment menu1Fragment = new Menu1Fragment();
    private Menu2Fragment menu2Fragment = new Menu2Fragment();
    private Menu3Fragment menu3Fragment = new Menu3Fragment();

    //private DrawerLayout mDrawerLayout;

    //------------------------------------

    public static final String LOG_TAG = "LOGMainActivity";



    private final int ACTIVITY_LOGIN = 100;
    private final int ACTIVITY_SIGNUP = 101;


    protected SessionManager mSession = null;

    //-----------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //------
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //-------


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        //------
        navigationView.setNavigationItemSelectedListener(this);
        mSession = SessionManager.getInstance(this);
        if (mSession.isLogin()) {
            setNavEmail(mSession.getEmail());
        }
        //------


        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, menu1Fragment).commitAllowingStateLoss();

        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_home: {
                        transaction.replace(R.id.frame_layout, menu1Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_dashboard: {
                        transaction.replace(R.id.frame_layout, menu2Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.navigation_notifications: {
                        transaction.replace(R.id.frame_layout, menu3Fragment).commitAllowingStateLoss();
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        int id = item.getItemId();


        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            //case R.id.action_settings:
                //return true;
        }

        return super.onOptionsItemSelected(item);
    }



    //-------
    public void setNavEmail(String email) {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView tv = (TextView) headerView.findViewById(R.id.textNavEmail);
        tv.setText(email);
    }
    @Override
    protected void onStop() {
        super.onStop();

        mSession.cancelQueue();
    }
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signup)
        {
            if (mSession.isLogin() == false)
            {

                Intent intent = new Intent(this, SignupActivity.class);
                startActivityForResult(intent, ACTIVITY_SIGNUP);
            }
            else {
                Toast.makeText(this, "이미 로그인되어 있습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else if (id == R.id.nav_login)
        {
            if (mSession.isLogin() == false) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, ACTIVITY_LOGIN);
            }
            else {
                Toast.makeText(this, "이미 로그인되어 있습니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else if (id == R.id.nav_logout)
        {
            mSession.Logout();
        }
        else if (id == R.id.nav_info)
        {
            // 서버에 웹 페이지 만들어서 연결할 것
        } else if (id == R.id.nav_copyright)
        {
            // 서버에 웹 페이지 만들어서 연결할 것
            // 활용한 소스코드나 이미지 등 저작권 관련 정보
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        switch ((requestCode)) {
            case ACTIVITY_LOGIN:
                Log.i(LOG_TAG, "ACTIVITY_LOGIN");

                if (mSession.isLogin()) { // 로그인 성공하고 리턴 경우
                    Log.i(LOG_TAG, "LOGIN");
                    Toast.makeText(this, "로그인되었습니다.",
                            Toast.LENGTH_SHORT).show();
                    TextView tv = (TextView)findViewById(R.id.textNavEmail);
                    tv.setText(mSession.getEmail());
                }
                else {
                    Log.i(LOG_TAG, "Fail");
                    Toast.makeText(this, "로그인에 실패했습니다.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case ACTIVITY_SIGNUP:
                Log.i(LOG_TAG, "ACTIVITY_SIGNUP");

                if (mSession.isLogin()) { // 회원가입 성공하고 리턴 경우
                    Log.i(LOG_TAG, "SIGNUP");
                    Toast.makeText(this, "회원 가입되었습니다.",
                            Toast.LENGTH_SHORT).show();
                    TextView tv = (TextView)findViewById(R.id.textNavEmail);
                    tv.setText(mSession.getEmail());
                }
                else {

                    Log.i(LOG_TAG, "Fail");
                    Toast.makeText(this, "회원 가입에 실패했습니다.",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}