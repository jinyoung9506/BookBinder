package com.kamizos.bookbinder;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NaverBookService {



    private String CLIENT_ID;
    private String CLIENT_SEC;
    private Context mcontext;

    public NaverBookService(Context context, String id, String sec) {
        mcontext = context;
        CLIENT_ID = id;
        CLIENT_SEC = sec;
    }

    public ArrayList<Book> searchBook(String isbn, int display, int start) {



        ArrayList<Book> blist = null;
        String text = null;

        try {
            text = URLEncoder.encode(isbn, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패",e);
        }
        String apiURL = "https://openapi.naver.com/v1/search/book.xml?query=" + text + (display != 0 ? "&display=" + display : "") + (start != 0 ? "&start=" + start : "");

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-naver-Client-Id", CLIENT_ID);
        requestHeaders.put("X-naver-Client-Secret", CLIENT_SEC);
        String responseBody = get(apiURL, requestHeaders);
        try {

            XmlPullParserFactory factory;

            factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(new StringReader(responseBody));


            int eventType = parser.getEventType();
            Book b = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.END_DOCUMENT: // 문서의 끝
                        break;
                    case XmlPullParser.START_DOCUMENT:
                        blist = new ArrayList<Book>();
                        break;
                    case XmlPullParser.START_TAG: {
                        String tag = parser.getName();
                        switch (tag) {
                            case "total":
                                if ( Integer.parseInt(parser.nextText()) < 1 ) {
                                    final String msg = isbn;
                                    final Handler mhandler = new Handler(Looper.getMainLooper());
                                    mhandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(mcontext.getApplicationContext(),  "ISBN : " + msg + " 에 대한 검색 결과가 없습니다. 없는 책이거나 등록되지 않은 번호일 수 있습니다.", Toast.LENGTH_LONG).show();
                                        }
                                    }, 0);
                                    eventType = XmlPullParser.END_DOCUMENT;
                                }
                                break;
                            case "item":
                                b = new Book();
                                break;
                            case "title":
                                if (b != null)
                                    b.setTitle(parser.nextText());
                                break;
                            case "link":
                                if (b != null)
                                    b.setLink(parser.nextText());
                                break;
                            case "image":
                                if (b != null)
                                    b.setImageURL(parser.nextText());
                                break;
                            case "author":
                                if (b != null)
                                    b.setAuthor(parser.nextText());
                                break;
                            case "price":
                                if (b != null)
                                    b.setPriceByString(parser.nextText());
                                break;
                            case "publisher":
                                if (b != null)
                                    b.setPublisher(parser.nextText());
                                break;
                            case "pubdate":
                                if (b != null)
                                    b.setPubdate(parser.nextText());
                                break;
                            case "isbn":
                                if (b != null)
                                    b.setISBN(parser.nextText());
                                break;
                            case "description":
                                break;
                        }
                        break;
                    }

                    case XmlPullParser.END_TAG: {
                        String tag = parser.getName();
                        if (tag.equals("item")) {
                            blist.add(b);
                            b = null;
                        }

                    }

                }
                eventType = parser.next();
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return blist;
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
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
