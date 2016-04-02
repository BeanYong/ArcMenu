package com.arcmenubymyself.beanyong.arcmenubymyself;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;


public class MainActivity extends Activity implements ArcMenu.OnArcMenuItemClick {

    private ArcMenu mArcMenu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mArcMenu = (ArcMenu) findViewById(R.id.id_arcMenu);
        mArcMenu.setmOnArcMenuItemClick(this);
    }

    @Override
    public void onArcMenuItemClick(View child) {
        switch(child.getId()){
            case R.id.id_camera:
                Toast.makeText(MainActivity.this,"camera",Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_music:
                Toast.makeText(MainActivity.this,"music",Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_place:
                Toast.makeText(MainActivity.this,"place",Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_thought:
                Toast.makeText(MainActivity.this,"thought",Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_with:
                Toast.makeText(MainActivity.this,"with",Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_sleep:
                Toast.makeText(MainActivity.this,"sleep",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
