package com.smarcom.smarpay.cloudapi.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

public class FileHelper {

    private static final String JSON_FILE_EXTENSION = "json";

    public static Object readFromFile(Context context, String fileName, Type dataType) {
        BufferedReader bufferedReader = null;
        Object result = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader((context.getAssets().open(fileName))));
            JsonObjectParser parser = new JacksonFactory().createJsonObjectParser();
            result = parser.parseAndClose(bufferedReader, dataType);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public static Bitmap readBitmapFromFile(Context context, String fileName) throws IOException {
        return BitmapFactory.decodeStream(context.getAssets().open(fileName));
    }

    public static String getJsonFullFileName(String fileName) {
        return fileName + "." + JSON_FILE_EXTENSION;
    }
}
