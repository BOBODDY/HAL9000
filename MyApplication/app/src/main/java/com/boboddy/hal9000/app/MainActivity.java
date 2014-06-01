package com.boboddy.hal9000.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;


public class MainActivity extends Activity {

    ImageView image;
    Button chooser;

    final static int REQUEST_IMAGE = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image = (ImageView) findViewById(R.id.image);

        chooser = (Button) findViewById(R.id.chooseImage);
        chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ACTION_OPEN_DOCUMENT is how you open a file via the system's
                // file broswer
                Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                // Limit the shown items to things which can actually be opened
                i.addCategory(Intent.CATEGORY_OPENABLE);

                // Only want to look at images
                i.setType("image/*");

                startActivityForResult(i, REQUEST_IMAGE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == REQUEST_IMAGE) {

                new BitmapTask().execute(data.getData());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class BitmapTask extends AsyncTask<Uri, Void, Bitmap> {
        protected Bitmap doInBackground(Uri... uris) {
            Bitmap bm = null;

            try {
                ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uris[0], "r");
                FileDescriptor fd = pfd.getFileDescriptor();

                bm = BitmapFactory.decodeFileDescriptor(fd);
                bm = Bitmap.createScaledBitmap(bm, 360, 0, false);
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }

            return bm;
        }

        protected void onPostExecute(Bitmap result) {
            if(result != null) {
                image.setImageBitmap(result);
            }
        }
    }
}