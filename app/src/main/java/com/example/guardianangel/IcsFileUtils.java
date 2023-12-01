package com.example.mobilecomp1;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IcsFileUtils {
    private static final String TAG = "IcsFileUtils";
    public static void moveIcsFileToStorage(Context context,int rawResourceId, String fileName) throws IOException {
        /*Resources resources = context.getResources();
        InputStream inputStream = resources.openRawResource(rawResourceId);
        FileOutputStream outputStream = null;

        try {
            File file = new File(context.getFilesDir(), fileName);
            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error occurred while saving file: " + e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error occurred while closing streams: " + e.getMessage());
            }
        }*/
        InputStream in = null;
        FileOutputStream fout = null;
        try {
            in = context.getResources().openRawResource(R.raw.temp);
            String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            String filename = "myfile.ics";
            fout = new FileOutputStream(new File(downloadsDirectoryPath + "/"+filename));

            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }
}
