package com.kamizos.bookbinder;

public class ISBN {
    private String isbn;

    public ISBN(String code) {
        this.isbn = code.substring(0,13);
    }

    public String getISBN() {
        return isbn;
    }
}
