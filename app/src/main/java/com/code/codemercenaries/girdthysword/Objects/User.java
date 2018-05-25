package com.code.codemercenaries.girdthysword.Objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Joel Kingsley on 20-12-2017.
 */
class Meta {
    public static List<String> bookItems = new ArrayList<>(Arrays.asList("Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy", "Joshua",
            "Judges", "Ruth", "1 Samuel", "2 Samuel", "1 Kings", "2 Kings", "1 Chronicles",
            "2 Chronicles", "Ezra", "Nehemiah", "Esther", "Job", "Psalms", "Proverbs", "Ecclesiastes",
            "Song of Solomon", "Isaiah", "Jeremiah", "Lamentations", "Ezekiel", "Daniel", "Hosea",
            "Joel", "Amos", "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk", "Zephaniah", "Haggai",
            "Zechariah", "Malachi", "Matthew", "Mark", "Luke", "John", "Acts", "Romans",
            "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians", "Philippians", "Colossians",
            "1 Thessalonians", "2 Thessalonians", "1 Timothy", "2 Timothy", "Titus", "Philemon",
            "Hebrews", "James", "1 Peter", "2 Peter", "1 John", "2 John", "3 John", "Jude", "Revelation"));

    public static List<Integer> numOfChap = new ArrayList<>(Arrays.asList(50, 40, 27, 36, 34, 24, 21, 4, 31, 24, 22, 25, 29, 36, 10, 13, 10, 42, 150, 31, 12, 8, 66, 52, 5, 48,
            12, 14, 3, 9, 1, 4, 7, 3, 3, 3, 2, 14, 4, 28, 16, 24, 21, 28, 16, 16, 13, 6, 6, 4, 4, 5, 3, 6, 4, 3, 1, 13, 5, 5, 3, 5, 1, 1, 1, 22));
}


public class User {

    String userId;
    String displayName;
    String email;
    boolean registered;
    long score;
    long verses_memorized;
    List<Chunk> chunks;
    List<Section> sections;
    //Bible bible;
    int chunkSize;
    String theme;

    public User(String userId, String displayName, String email) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.registered = true;
        this.score = 0;
        this.verses_memorized = 0;
        this.chunks = null;
        this.sections = null;
        this.chunkSize = 3;
        //this.bible = new Bible();
        theme = "original";
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getVerses_memorized() {
        return verses_memorized;
    }

    public void setVerses_memorized(long verses_memorized) {
        this.verses_memorized = verses_memorized;
    }

    public List<Chunk> getChunks() {
        return chunks;
    }

    public void setChunks(List<Chunk> chunks) {
        this.chunks = chunks;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    private class Bible {
        List<Book> books;
        boolean memorized;

        public Bible() {
            List<Book> books = new ArrayList<>();
            this.memorized = false;
            for (int i = 0; i < Meta.bookItems.size(); i++) {
                books.add(new Book(Meta.bookItems.get(i)));
            }
        }

        public List<Book> getBooks() {
            return books;
        }

        public void setBooks(List<Book> books) {
            this.books = books;
        }

        public boolean isMemorized() {
            return memorized;
        }

        public void setMemorized(boolean memorized) {
            this.memorized = memorized;
        }

        private class Book {
            List<Chapter> chapters;
            String bookName;
            boolean memorized;

            public Book(String bookName) {
                this.bookName = bookName;
                this.memorized = false;
                List<Chapter> chapters = new ArrayList<>();
                for (int i = 1; i <= Meta.numOfChap.get(Meta.bookItems.indexOf(bookName)); i++) {
                    chapters.add(new Chapter(bookName, i));
                }


            }

            public List<Chapter> getChapters() {
                return chapters;
            }

            public void setChapters(List<Chapter> chapters) {
                this.chapters = chapters;
            }

            public String getBookName() {
                return bookName;
            }

            public void setBookName(String bookName) {
                this.bookName = bookName;
            }

            public boolean isMemorized() {
                return memorized;
            }

            public void setMemorized(boolean memorized) {
                this.memorized = memorized;
            }

            private class Chapter {
                List<Verse> verses;
                int chapNum;
                boolean memorized;

                public Chapter(String bookName, int chapNum) {
                    this.chapNum = chapNum;
                    this.memorized = false;
                    this.verses = null;
                }

                public List<Verse> getVerses() {
                    return verses;
                }

                public void setVerses(List<Verse> verses) {
                    this.verses = verses;
                }

                public int getChapNum() {
                    return chapNum;
                }

                public void setChapNum(int chapNum) {
                    this.chapNum = chapNum;
                }

                public boolean isMemorized() {
                    return memorized;
                }

                public void setMemorized(boolean memorized) {
                    this.memorized = memorized;
                }

                private class Verse {
                    int memory;

                    public Verse() {
                        this.memory = 0;
                    }

                    public int getMemory() {
                        return memory;
                    }

                    public void setMemory(int memory) {
                        this.memory = memory;
                    }
                }
            }
        }
    }

}
