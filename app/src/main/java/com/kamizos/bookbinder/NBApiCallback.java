package com.kamizos.bookbinder;

import java.util.ArrayList;

public interface NBApiCallback {
    void onSuccess(ArrayList<Book> booklistResult);
    void onFailure(Exception e);
}
