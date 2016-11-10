package com.example.chain;

//import com.example.download.httpDownloader;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;


public class activity_Main extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//       tabhost标签引导
        TabHost tabHost1=getTabHost();
        Intent intent1=new Intent();
        intent1.setClass(this, MyQQContact.class);
        TabHost.TabSpec spec1=tabHost1.newTabSpec("数据采集");
        Resources resources1=getResources();
        View v3 = LayoutInflater.from(this).inflate(R.layout.tab_main3,null);
        spec1.setIndicator(v3);
        spec1.setContent(intent1);
        tabHost1.addTab(spec1);
        
        TabHost tabHost2=getTabHost();
        Intent intent2=new Intent();
        intent2.setClass(this, MyQQNews.class);
        TabHost.TabSpec spec2=tabHost2.newTabSpec("主页");
        Resources resources2=getResources();
        View v = LayoutInflater.from(this).inflate(R.layout.tab_main,null);
         
        spec2.setIndicator(v);
        spec2.setContent(intent2);
        tabHost2.addTab(spec2);
        
        TabHost tabHost3=getTabHost();
        Intent intent3=new Intent();
        intent3.setClass(this, MyQQDynamic.class);
        TabHost.TabSpec spec3=tabHost2.newTabSpec("数据分析");
        Resources resources3=getResources();
        View v2 = LayoutInflater.from(this).inflate(R.layout.tab_main2,null);
        spec3.setIndicator(v2);
        spec3.setContent(intent3);
        tabHost2.addTab(spec3);

    }  
}
