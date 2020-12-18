package com.kamizos.bookbinder;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import static com.kamizos.bookbinder.MainActivity.badapter;

public class Sync {

    static class LoginRequest extends AsyncTask<String, Void, String> {
        String id;
        String pw;
        Context scontext;
        private LoginRequestCallback syncCallback;
        private Exception apiExeption;
        ProgressDialog asyncDialog;
        public String targetURL = "loginfromand/";


        LoginRequest(Context context, LoginRequestCallback callback) {
            scontext = context;
            syncCallback = callback;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String resultstring= "";

            id = params[0];
            pw = params[1];

            /*JSONObject userjson = new JSONObject();
            try {
                userjson.put("username",id);
                userjson.put("password", pw);
            } catch (JSONException e) {
                e.printStackTrace();
            }*/


            WebConnection conn = new WebConnection();
            resultstring = conn.connection(id, pw, targetURL, scontext);



            return resultstring;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if ((syncCallback != null)&&(apiExeption == null)) {
                syncCallback.onSuccess(s, id);
            }else {
                syncCallback.onFailure(apiExeption);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    public interface LoginRequestCallback {
        void onSuccess(String resultstring, String id);
        void onFailure(Exception e);
    }


    static class UploadRequest extends AsyncTask<JSONObject, String, String> {

        private UploadRequestCallback syncCallback;
        private Exception apiExeption;
        Context scontext;
        ProgressDialog asyncDialog;
        int success;
        String id;
        String pw;

        public UploadRequest(Context context,String ID, String PW, UploadRequestCallback callback) {
            scontext = context;
            syncCallback = callback;
            id = ID;
            pw = PW;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncDialog = new ProgressDialog(scontext);
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            asyncDialog.setMessage("업로드 중입니다..");
            asyncDialog.setTitle("Upload");
            asyncDialog.setCancelable(false);
            asyncDialog.show();
        }

        @Override
        protected String doInBackground(JSONObject... json) {

            int size = json[0].length();
            success = 0;
            asyncDialog.setMax(size);
            WebConnection conn = new WebConnection();
            String targetURL = "upfromjson/";
            String resultstring ="";
            JSONObject resultjson = new JSONObject();

            for (int i = 0; i < size; i++) {
                try {
                        resultjson = conn.JsonPost(targetURL, json[i], scontext);
                        asyncDialog.setProgress(i+1);
                        success++;
                        resultstring += resultjson.get("result").toString();
                } catch (Exception e) {
                    apiExeption = e;
                }
            }
            try {
                resultstring = resultjson.get("result").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resultstring;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            asyncDialog.setMessage(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            asyncDialog.dismiss();
            if (syncCallback != null) {
                syncCallback.onSuccess(success, s);
                success = 0;
            }else {
                syncCallback.onFailure(apiExeption);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    public interface UploadRequestCallback {
        void onSuccess(int success, String result);
        void onFailure(Exception e);
    }

    static class DownloadRequest extends AsyncTask<String, String, ArrayList<Book>>{

        private DownloadRequestCallback syncCallback;
        private Exception apiExeption;
        Context scontext;
        ProgressDialog asyncDialog;
        DBHelper dbHelper;



        public DownloadRequest(Context context, DownloadRequestCallback callback) {
            scontext = context;
            syncCallback = callback;
            dbHelper = DBHelper.getInstance(scontext);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncDialog = new ProgressDialog(scontext);
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            asyncDialog.setMessage("다운로드 중입니다..");
            asyncDialog.setTitle("도서정보 검색");
            asyncDialog.setCancelable(false);
            asyncDialog.show();
        }

        @Override
        protected ArrayList<Book> doInBackground(String... params) {
            ArrayList<Book> booklist = new ArrayList<Book>();
            JSONObject Account = new JSONObject();
            try {
                Account.put("username", params[0]);
                Account.put("password", params[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray result = null;
            String targetURL = "downtojson/";
            WebConnection conn = new WebConnection();

            try {
                result = conn.JsonAPost(targetURL, Account, scontext);
                asyncDialog.setMax(result.length());
                for (int i = 0; i < result.length(); i++) {
                    Book book = new Book();
                    JSONObject jsonObject = result.getJSONObject(i).getJSONObject("fields");
                    book.setISBN(jsonObject.getString("isbn"));
                    book.setTitle(jsonObject.getString("title"));
                    publishProgress(jsonObject.getString("title"));
                    book.setAuthor(jsonObject.getString("author"));
                    book.setPrice(jsonObject.getInt("price"));
                    book.setPublisher(jsonObject.getString("publisher"));
                    book.setPubdate(jsonObject.getString("date"));
                    book.setLink(jsonObject.getString("booklink"));
                    book.setImageURL(jsonObject.getString("imagelink"));
                    book.setMemo(jsonObject.getString("memo"));
                    booklist.add(book);
                    MainActivity.booklist.add(book);
                    dbHelper.insert(book);
                    asyncDialog.setProgress(i+1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return booklist;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            asyncDialog.setMessage(values[0]);

            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(ArrayList<Book> s) {
            super.onPostExecute(s);
            asyncDialog.dismiss();
            if ((syncCallback != null)&&(apiExeption == null)) {
                syncCallback.onSuccess(s);
            }else {
                syncCallback.onFailure(apiExeption);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    public interface DownloadRequestCallback {
        void onSuccess(ArrayList<Book> s);
        void onFailure(Exception e);
    }

}
