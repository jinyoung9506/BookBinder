package com.kamizos.bookbinder;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class WebConnection {

    public JSONArray JsonAPost(String path, JSONObject obj, Context context) throws IOException, JSONException {
        String targetURL = context.getResources().getString(R.string.URL);
        String finalurl = targetURL + path;
        URL url = new URL(finalurl);

        HttpURLConnection conn = null;
        OutputStream os = null; InputStream is = null;
        ByteArrayOutputStream baos = null;
        conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5*100);
        conn.setReadTimeout(5*100);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type","application/json; utf-8");
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);

        os = conn.getOutputStream();
        os.write(obj.toString().getBytes());
        os.flush();

        String response = "";

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            is = conn.getInputStream();
            baos = new ByteArrayOutputStream();
            byte[] byteBuffer = new byte[1024];
            byte[] byteData = null;
            int nLength = 0;
            while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                baos.write(byteBuffer, 0, nLength);
            }
            byteData = baos.toByteArray();

            response = new String(byteData);

            JSONArray responseJson = new JSONArray(response);
            return  responseJson;
        }
        else {
            JSONArray responseJson = new JSONArray(response);
            return  responseJson;
        }

    }

    public JSONObject JsonPost(String path, JSONObject obj, Context context) throws IOException, JSONException {
        String targetURL = context.getResources().getString(R.string.URL);;
        String finalurl = targetURL + path;
        URL url = new URL(finalurl);

        HttpURLConnection conn = null;
        OutputStream os = null; InputStream is = null;
        ByteArrayOutputStream baos = null;
        conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(50000);
        conn.setReadTimeout(50000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type","application/json; utf-8");
        conn.setRequestProperty("Cache-Control", "no-cache");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);

        os = conn.getOutputStream();
        os.write(obj.toString().getBytes());
        os.flush();

        String response = "";

        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            is = conn.getInputStream();
            baos = new ByteArrayOutputStream();
            byte[] byteBuffer = new byte[25];
            byte[] byteData = null;
            int nLength = 0;
            while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                baos.write(byteBuffer, 0, nLength);
            }
            byteData = baos.toByteArray();

            response = new String(byteData);

            JSONObject responseJson = new JSONObject(response);
            return  responseJson;
        }
        else {
            JSONObject responseJson = new JSONObject(response);
            return  responseJson;
        }

    }

    public String connection(String iD, String pW, String url, Context context) {
        String finalurl = null;


        String targetURL = context.getResources().getString(R.string.URL);

        finalurl = targetURL + url;

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("username", iD);
        requestHeaders.put("password", pW);


        String responseBody = get(finalurl, requestHeaders);

        return responseBody;
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);


        try {
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

            StringBuffer sbParams = new StringBuffer();

            boolean isAnd = false;
            for(String key: requestHeaders.keySet()){
                if(isAnd)
                    sbParams.append("&");
                sbParams.append(key).append("=").append(requestHeaders.get(key));
                if(!isAnd)
                    if(requestHeaders.size() >= 2)
                        isAnd = true;
            }

            wr.write(sbParams.toString());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}
