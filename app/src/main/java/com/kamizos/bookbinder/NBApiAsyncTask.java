package com.kamizos.bookbinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

public class NBApiAsyncTask extends AsyncTask<ArrayList<ISBN>, String, Void> {

    private NBApiCallback apiCallback;
    private Exception apiExeption;
    ArrayList<Book> blist;
    ProgressDialog asyncDialog;
    Context apicontext;
    private String Id;
    private String Sec;

    public NBApiAsyncTask(Context context, String id, String sec, NBApiCallback callback ) {
        apicontext = context;
        apiCallback = callback;
        Id = id;
        Sec = sec;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        asyncDialog = new ProgressDialog(apicontext);
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        asyncDialog.setMessage("검색중입니다..");
        asyncDialog.setTitle("도서정보 검색");
        asyncDialog.setCancelable(false);
        asyncDialog.show();

    }

    @Override
    protected Void doInBackground(ArrayList<ISBN>... arrayLists) {
        blist = new ArrayList<Book>();
        int size = arrayLists[0].size();
        int now = 0;
        String title = null;

        NaverBookService api = new NaverBookService(apicontext, Id, Sec);

        asyncDialog.setMax(size);


        try {
            for (int i = 0; i < size; i++) {

                blist.addAll(api.searchBook(arrayLists[0].get(i).getISBN(),10,1));
                for (int j = 0; j < blist.size(); j++) {
                    title = blist.get(j).getTitle();
                    publishProgress(title);
                }
                asyncDialog.setProgress(i+1);
            }
        }catch (Exception e) {
            apiExeption = e;
        }

        return null;
    }

    protected void onProgressUpdate(String... values) {

        asyncDialog.setMessage(values[0]);

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void o) {
        super.onPostExecute(o);
        asyncDialog.dismiss();
        if ((apiCallback != null)&&(apiExeption == null)) {
            apiCallback.onSuccess(blist);
        }else {
            apiCallback.onFailure(apiExeption);
        }

    }




}
