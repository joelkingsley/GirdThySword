package com.code.codemercenaries.girdthysword;

/**
 * Created by Joel Kingsley on 17-10-2017.
 */

public class ReadableVerse {
    long _id;
    String _book_name;
    int _chap_num;
    int _verse_num;
    String _verse_text;
    int _memory;

    public ReadableVerse(String _book_name, int _chap_num, int _verse_num, String _verse_text) {
        this._book_name = _book_name;
        this._chap_num = _chap_num;
        this._verse_num = _verse_num;
        this._verse_text = _verse_text;
    }

    public ReadableVerse(String _book_name, int _chap_num, int _verse_num, String _verse_text, int _memory) {
        this._book_name = _book_name;
        this._chap_num = _chap_num;
        this._verse_num = _verse_num;
        this._verse_text = _verse_text;
        this._memory = _memory;
    }

    public ReadableVerse() {
        this._book_name = "";
        this._chap_num = 0;
        this._verse_num = 0;
        this._verse_text = "";
        this._memory = -1;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String get_book_name() {
        return _book_name;
    }

    public void set_book_name(String _book_name) {
        this._book_name = _book_name;
    }

    public int get_chap_num() {
        return _chap_num;
    }

    public void set_chap_num(int _chap_num) {
        this._chap_num = _chap_num;
    }

    public int get_verse_num() {
        return _verse_num;
    }

    public void set_verse_num(int _verse_num) {
        this._verse_num = _verse_num;
    }

    public String get_verse_text() {
        return _verse_text;
    }

    public void set_verse_text(String _verse_text) {
        this._verse_text = _verse_text;
    }

    public int get_memory() {
        return _memory;
    }

    public void set_memory(int _memory) {
        this._memory = _memory;
    }

    @Override
    public String toString() {
        return _book_name + " " + _chap_num +
                ":" + _verse_num;
    }
}
