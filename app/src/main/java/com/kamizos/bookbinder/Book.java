package com.kamizos.bookbinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.*;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Book {
    private String ISBN;
    private String Title;
    private String BookLink;
    private String ImageURL;
    private String Author;
    private int Price;
    private String Publisher;
    private String Pubdate;
    private byte[] Image;
    private String Memo;
    private String Before;
    private String After;

    Book(String isbn, String title, String bookLink, String imgurl, String author, int price, String publisher, String date, byte[] img, String memo, String before, String after) {
        ISBN = isbn;
        Title = title;
        BookLink = bookLink;
        ImageURL = imgurl;
        Author = author;
        Price = price;
        Publisher = publisher;
        Pubdate = date;
        Image = img;
        Memo = memo;
        Before = before;
        After = after;
    }

    Book() {}

    public String getBefore() {
        return Before;
    }

    public void setBefore(String before) {
        this.Before = before;
    }

    public String getAfter() {
        return After;
    }

    public void setAfter(String after) {
        this.After = after;
    }

    public String getTitle() {
        return Title;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        this.Memo = memo;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getLink() {
        return BookLink;
    }

    public void setLink(String link) {
        this.BookLink = link;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        this.Publisher = publisher;
    }

    public void setPubdate(String pubdate) {
        this.Pubdate = pubdate;
    }

    public String getPubdate() {
        return Pubdate;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String isbn) {
        if (isbn.length() > 14) {
            this.ISBN = isbn.substring(14, 27);
        }else {
            this.ISBN = isbn;
        }
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        this.Price = price;
    }

    public void setPriceByString(String price) {
        this.Price = Integer.parseInt(price);
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        this.Author = author;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        String temp = imageURL;
        int tempint = temp.indexOf(".jpg");
        if ( tempint >= 0 ) {
            ImageURL = temp.substring(0, tempint + 4);
        }else {
            ImageURL = imageURL;
        }

        setImageByURL(ImageURL);
    }


    public byte[] getImage() {
        return Image;
    }

    public Bitmap getBitmapImage() {
        Bitmap img = BitmapFactory.decodeByteArray(this.Image,0,this.Image.length);
        return img;
    }

    public void setImage(byte[] img) {
        Image = img;
    }

    public void setImageByURL(String str) {

        Bitmap bitmap = null;

        try {
            URL url = new URL(str);
            URLConnection conn = url.openConnection();
            conn.connect();

            int nSize = conn.getContentLength();

            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);

            bitmap = BitmapFactory.decodeStream(bis);

            bis.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        setImageByBitmap(bitmap);
    }

    public void setImageByBitmap(Bitmap img) {
        ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG,100, bytearray);
        this.Image = bytearray.toByteArray();
    }

}
