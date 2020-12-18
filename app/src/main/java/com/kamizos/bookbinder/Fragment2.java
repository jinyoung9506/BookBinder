package com.kamizos.bookbinder;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment2 extends Fragment {

    static EditText title;
    static ImageView image;
    static TextView author;
    static TextView publisher;
    static TextView price;
    static TextView date;
    static TextView link;
    static EditText memo;
    static DBHelper dbHelper;
    private static Book bookpage = null;



    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bookpage = null;
        dbHelper = DBHelper.getInstance(getActivity().getApplicationContext());
        title = getActivity().findViewById(R.id.book_title_edit);
        image = getActivity().findViewById(R.id.book_image_view);
        author = getActivity().findViewById(R.id.book_author_edit);
        author.setSelected(true);
        publisher = getActivity().findViewById(R.id.book_publisher_view);
        publisher.setSelected(true);
        price = getActivity().findViewById(R.id.book_price_view);
        price.setSelected(true);
        date = getActivity().findViewById(R.id.book_date_view);
        date.setSelected(true);
        link = getActivity().findViewById(R.id.book_url_view);
        memo = getActivity().findViewById(R.id.book_memo_edit);
    }

    public static void setPage(Book book) {
        bookpage = book;
        title.setText(bookpage.getTitle());
        image.setImageBitmap(bookpage.getBitmapImage());
        author.setText(bookpage.getAuthor());
        publisher.setText(bookpage.getPublisher());
        price.setText(Integer.toString(bookpage.getPrice()) + "원");
        date.setText(bookpage.getPubdate());
        link.setText(bookpage.getLink());
        memo.setText(bookpage.getMemo());
    }

    public static void savePage(ArrayList<Book> booklist) throws Exception {

        if (bookpage.getISBN().length() >= 10) {
            bookpage.setTitle(title.getText().toString());
            bookpage.setMemo(memo.getText().toString());

            for (int i = 0; i < booklist.size(); i++) {
                if (bookpage.getISBN().equals(booklist.get(i).getISBN())) {
                    booklist.remove(i);
                }
            }
            dbHelper.delete(bookpage.getISBN());
            dbHelper.insert(bookpage);
            booklist.add(bookpage);

        }else {
            throw new Exception("저장 할 데이터가 없습니다.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_2, container, false);
    }
}
