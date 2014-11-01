package it.rainbowbreeze.picama.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.Date;

import javax.inject.Inject;

import it.rainbowbreeze.picama.R;
import it.rainbowbreeze.picama.common.ILogFacility;
import it.rainbowbreeze.picama.common.MyApp;
import it.rainbowbreeze.picama.data.picture.PictureColumns;
import it.rainbowbreeze.picama.data.picture.PictureContentValues;
import it.rainbowbreeze.picama.data.picture.PictureSelection;
import it.rainbowbreeze.picama.logic.PictureScraperManager;

public class PictureListActivity extends Activity {
    @Inject ILogFacility mLogFacility;
    @Inject PictureScraperManager mPictureScraperManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).inject(this);
        mLogFacility.logStartOfActivity(PictureListActivity.class, savedInstanceState);

        setContentView(R.layout.act_list);

        ListView lst = (ListView) findViewById(R.id.list_lstItems);

        PictureSelection where = new PictureSelection();
        Cursor c = getApplicationContext().getContentResolver().query(PictureColumns.CONTENT_URI, null,
                where.sel(), where.args(), PictureColumns.TITLE + " DESC");
        lst.setAdapter(new PicturesAdapter(getApplicationContext(), c, true));

        Button btnAddItem = (Button) findViewById(R.id.list_btnAddItem);
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timestamp = new Date().getTime() + "";
                PictureContentValues values = new PictureContentValues();
                values.putTitle("Title " + timestamp);
                values.putUrl("http://lorempixel.com/600/400/");
                getApplicationContext().getContentResolver().insert(PictureColumns.CONTENT_URI, values.values());
            }
        });

        Button btnClearAll = (Button) findViewById(R.id.list_btnClear);
        btnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelection where = new PictureSelection();
                getApplicationContext().getContentResolver().delete(PictureColumns.CONTENT_URI, where.sel(), where.args());
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pictures_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.picList_mnuRefresh) {
            mPictureScraperManager.searchForNewImage();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
