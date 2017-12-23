package com.code.codemercenaries.girdthysword;

/**
 * Created by Joel Kingsley on 20-12-2017.
 */

class ChapterDetail {
    String bookName;
    int chapNum;
    int percentage;
    int totalVerses;
    int versesMemorized;

    public ChapterDetail(String bookName, int chapNum, int totalVerses, int versesMemorized, int percentage) {
        this.bookName = bookName;
        this.chapNum = chapNum;
        this.percentage = percentage;
        this.totalVerses = totalVerses;
        this.versesMemorized = versesMemorized;
    }

    public ChapterDetail(String bookName, int chapNum, int totalVerses) {
        this.bookName = bookName;
        this.chapNum = chapNum;
        this.totalVerses = totalVerses;
        this.percentage = 0;
        this.versesMemorized = 0;
    }

    public ChapterDetail() {
        this.bookName = "";
        this.chapNum = 0;
        this.totalVerses = 1;
        this.percentage = 0;
        this.versesMemorized = 0;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getChapNum() {
        return chapNum;
    }

    public void setChapNum(int chapNum) {
        this.chapNum = chapNum;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int gettotalVerses() {
        return totalVerses;
    }

    public void settotalVerses(int totalVerses) {
        this.totalVerses = totalVerses;
    }

    public int getversesMemorized() {
        return versesMemorized;
    }

    public void setversesMemorized(int versesMemorized) {
        this.versesMemorized = versesMemorized;
    }

    @Override
    public String toString() {
        return "ChapterDetail{" +
                "bookName='" + bookName + '\'' +
                ", chapNum=" + chapNum +
                ", totalVerses=" + totalVerses +
                ", versesMemorized=" + versesMemorized +
                '}';
    }
}
