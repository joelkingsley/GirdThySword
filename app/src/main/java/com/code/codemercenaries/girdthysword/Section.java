package com.code.codemercenaries.girdthysword;

/**
 * Created by Joel Kingsley on 17-10-2017.
 */

public class Section {

    long _id;
    String _book_name;
    int _chap_num;
    int _start_verse_num;
    int _end_verse_num;
    int _sec_id;

    public Section(String _book_name, int _chap_num, int _start_verse_num, int _end_verse_num, int _sec_id) {
        this._book_name = _book_name;
        this._chap_num = _chap_num;
        this._start_verse_num = _start_verse_num;
        this._end_verse_num = _end_verse_num;
        this._sec_id = _sec_id;
    }

    public Section() {
        this._book_name = "BN NA";
        this._chap_num = -123;
        this._start_verse_num = -1;
        this._end_verse_num = -1;
        this._sec_id = -1;
    }

    public Section(String _book_name, int _chap_num, int _start_verse_num, int _end_verse_num) {
        this._book_name = _book_name;
        this._chap_num = _chap_num;
        this._start_verse_num = _start_verse_num;
        this._end_verse_num = _end_verse_num;
        this._sec_id = -1;
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

    public int get_start_verse_num() {
        return _start_verse_num;
    }

    public void set_start_verse_num(int _start_verse_num) {
        this._start_verse_num = _start_verse_num;
    }

    public int get_end_verse_num() {
        return _end_verse_num;
    }

    public void set_end_verse_num(int _end_verse_num) {
        this._end_verse_num = _end_verse_num;
    }


    public int get_sec_id() {
        return _sec_id;
    }

    public void set_sec_id(int _sec_id) {
        this._sec_id = _sec_id;
    }

    @Override
    public String toString() {
        String string = this.get_book_name() + " " + this.get_chap_num() + ":" + this.get_start_verse_num() + "-" + this.get_end_verse_num();
        return string;
    }
}
