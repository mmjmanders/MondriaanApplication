package com.example.mondriaanapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import java.util.Arrays;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_mondriaan)
public class MondriaanActivity extends RoboActivity implements DialogInterface.OnClickListener {

    private static final String TAG = "MondriaanActivity";

    @InjectView(R.id.seekBar)
    private SeekBar seekBar;

    @InjectView(R.id.view1)
    private View view1;

    @InjectView(R.id.view2)
    private View view2;

    @InjectView(R.id.view3)
    private View view3;

    @InjectView(R.id.view5)
    private View view5;

    private List<View> views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        views = Arrays.asList(view1, view2, view3, view5);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                for (View view : views) {

                    int origColor = Color.parseColor((String) view.getTag());
                    int invertColor = (0x00FFFFFF - (origColor | 0xFF000000)) | (origColor & 0xFF000000);

                    int origR = (origColor >> 16) & 0x000000FF;
                    int origG = (origColor >> 8) & 0x000000FF;
                    int origB = origColor & 0x000000FF;

                    int invR = (invertColor >> 16) & 0x000000FF;
                    int invG = (invertColor >> 8) & 0x000000FF;
                    int invB = invertColor & 0x000000FF;

                    view.setBackgroundColor(Color.rgb(
                            (int) (origR + (invR - origR) * (progress / 100f)),
                            (int) (origG + (invG - origG) * (progress / 100f)),
                            (int) (origB + (invB - origB) * (progress / 100f))
                    ));
                    view.invalidate();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mondriaan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.more_information:
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.dialog_title)
                        .setPositiveButton(R.string.dialog_visit_moma, this)
                        .setNegativeButton(R.string.dialog_not_now, this)
                        .create();

                alertDialog.show();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (DialogInterface.BUTTON_POSITIVE == which) {
            Intent moma = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.moma_url)));
            startActivity(moma);
        }
    }
}
