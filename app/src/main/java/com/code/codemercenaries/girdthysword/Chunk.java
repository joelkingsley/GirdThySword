package com.code.codemercenaries.girdthysword;

/**
 * Created by Joel Kingsley on 17-10-2017.
 */

public class Chunk {
    long _id;
    int _seq;
    int _sec_id;
    String _book_name;
    int _chapter_num;
    int _start_verse_num;
    int _end_verse_num;
    String _next_date_of_review;
    int _space;
    boolean _mastered;


    public Chunk(long _id, int _seq, String _book_name, int _chapter_num,
                 int _start_verse_num, int _end_verse_num,
                 String _next_date_of_review, int space, int _sec_id, boolean _mastered) {
        this._id = -1;
        this._seq = _seq;
        this._book_name = _book_name;
        this._chapter_num = _chapter_num;
        this._start_verse_num = _start_verse_num;
        this._end_verse_num = _end_verse_num;
        this._next_date_of_review = _next_date_of_review;
        this._space = space;
        this._sec_id = _sec_id;
        this._mastered = _mastered;
    }

    public Chunk(int _seq, String _book_name, int _chapter_num,
                 int _start_verse_num, int _end_verse_num,
                 String _next_date_of_review, int _space, int _sec_id, boolean _mastered) {
        this._seq = _seq;
        this._book_name = _book_name;
        this._chapter_num = _chapter_num;
        this._start_verse_num = _start_verse_num;
        this._end_verse_num = _end_verse_num;
        this._next_date_of_review = _next_date_of_review;
        this._space = _space;
        this._sec_id = _sec_id;
        this._mastered = _mastered;
    }

    public Chunk() {
        this._id = -1;
        this._seq = 0;
        this._book_name = "";
        this._chapter_num = 0;
        this._start_verse_num = 0;
        this._end_verse_num = 0;
        this._next_date_of_review = "";
        this._space = 0;
        this._sec_id = -1;
        this._mastered = false;
    }

    public long getId() {
        return _id;
    }

    public void setId(long _id) {
        this._id = _id;
    }

    public int getSecId() {
        return _sec_id;
    }

    public void setSecId(int _sec_id) {
        this._sec_id = _sec_id;
    }

    public int getSeq() {
        return _seq;
    }

    public void setSeq(int _seq) {
        this._seq = _seq;
    }

    public String getBookName() {
        return _book_name;
    }

    public void setBookName(String _book_name) {
        this._book_name = _book_name;
    }

    public int getChapNum() {
        return _chapter_num;
    }

    public void setChapNum(int _chapter_num) {
        this._chapter_num = _chapter_num;
    }

    public int getStartVerseNum() {
        return _start_verse_num;
    }

    public void setStartVerseNum(int _start_verse_num) {
        this._start_verse_num = _start_verse_num;
    }

    public int getEndVerseNum() {
        return _end_verse_num;
    }

    public void setEndVerseNum(int _end_verse_num) {
        this._end_verse_num = _end_verse_num;
    }

    public String getNextDateOfReview() {
        return _next_date_of_review;
    }

    public void setNextDateOfReview(String _next_date_of_review) {
        this._next_date_of_review = _next_date_of_review;
    }

    public int getSpace() {
        return _space;
    }

    public void setSpace(int space) {
        this._space = space;
    }

    public boolean isMastered() {
        return _mastered;
    }

    public void setMastered(boolean _mastered) {
        this._mastered = _mastered;
    }

    @Override
    public String toString() {
        String text = this.getBookName() + " " + this.getChapNum() + ":" + this.getStartVerseNum()
                + "-" + this.getEndVerseNum();
        return text;
    }
}
