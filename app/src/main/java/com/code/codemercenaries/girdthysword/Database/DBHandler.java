package com.code.codemercenaries.girdthysword.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.code.codemercenaries.girdthysword.Objects.Chunk;
import com.code.codemercenaries.girdthysword.Objects.Section;
import com.code.codemercenaries.girdthysword.Objects.Version;
import com.code.codemercenaries.girdthysword.ReadableVerse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Joel Kingsley on 17-10-2017.
 */

public class DBHandler extends SQLiteAssetHelper {

    public static final String V_KEY_ID = "id";
    public static final String V_KEY_NAME = "name";
    public static final String V_KEY_LANG = "lang";

    public static final String B_KEY_ID = "id";
    public static final String B_KEY_BOOK_NAME = "book_name";
    public static final String B_KEY_CHAP_NUM = "chapter_num";
    public static final String B_KEY_VERSE_NUM = "verse_num";
    public static final String B_KEY_VERSE_TEXT = "verse_text";
    public static final String B_KEY_MEMORY = "memory";

    public static final int CODE_NOT_ADDED = 0;
    public static final int CODE_ADDED = 2;
    public static final int CODE_MEMORIZED = 3;

    public static final String C_KEY_ID = "id";
    public static final String C_KEY_SEQ = "seq";
    public static final String C_KEY_BOOK_NAME = "book_name";
    public static final String C_KEY_CHAP_NUM = "chapter_num";
    public static final String C_KEY_START_VERSE_NUM = "start_verse_num";
    public static final String C_KEY_END_VERSE_NUM = "end_verse_num";
    public static final String C_KEY_NEXT_DATE_OF_REVIEW = "next_date_of_review";
    public static final String C_KEY_SPACE = "space";
    public static final String C_KEY_SEC_ID = "sec_id";
    public static final String C_KEY_MASTERED = "mastered";
    public static final String C_KEY_VER_ID = "version";

    public static final String S_KEY_ID = "id";
    public static final String S_KEY_BOOK_NAME = "book_name";
    public static final String S_KEY_CHAP_NUM = "chapter_num";
    public static final String S_KEY_START_VERSE_NUM = "start_verse_num";
    public static final String S_KEY_END_VERSE_NUM = "end_verse_num";
    public static final String S_KEY_VER_ID = "version";

    private static final String DATABASE_NAME = "girdthysword.db";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_EN_KJV = "en_kjv";
    private static final String TABLE_TAM_ORG = "tam_org";

    private static final String TABLE_VERSION = "version";
    private static final String TABLE_CHUNK = "chunk";
    private static final String TABLE_SECTION = "section";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Version Functions
    //-----------------

    public List<Version> getVersions() {
        List<Version> versions = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_VERSION;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                versions.add(new Version(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            } while (cursor.moveToNext());
        }
        return versions;
    }

    //ReadableVerse Functions
    //-----------------------
    public List<String> getBookNames(String version) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT DISTINCT " + B_KEY_BOOK_NAME + " FROM " + version;
        Cursor cursor = db.rawQuery(query, null);
        List<String> bookNames = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                bookNames.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        Log.d("Function:", "getBookNames");

        return bookNames;
    }

    public int getNumOfVerse(String version, String bookName, int chapNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + version + " WHERE "
                + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM + " = " + chapNum;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        Log.d("getNumOfVerse:", bookName + " " + chapNum + ", " + count);

        return count - 1;
    }

    public int getNumofChap(String version, String bookName) {
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT DISTINCT " + B_KEY_CHAP_NUM + " FROM " + version + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"';
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        Log.d("getNumOfChap:", bookName + ", " + count);

        return count - 1;
    }

    public String getVerse(String version, String bookName, int chapNum, int verseNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + B_KEY_VERSE_TEXT + " FROM " + version + " WHERE "
                + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM + " = " + chapNum
                + " AND " + B_KEY_VERSE_NUM + " = " + verseNum;
        Cursor cursor = db.rawQuery(query, null);
        String verse = "";
        if (cursor.moveToFirst()) {
            do {
                verse += cursor.getString(0);
            } while (cursor.moveToNext());
        }
        Log.d("Function:", "getVerse");

        return verse;
    }

    public boolean addedBook(String version, String s) {
        SQLiteDatabase db = getWritableDatabase();
        String selectQueryTotal = "SELECT " + B_KEY_MEMORY + " FROM " + version + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + s + '"';
        String selectQueryAdded = "SELECT " + B_KEY_MEMORY + " FROM " + version + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + s + '"' + " AND " + B_KEY_MEMORY + ">" + "0";
        Cursor cursorTotal = db.rawQuery(selectQueryTotal, null);
        Cursor cursorAdded = db.rawQuery(selectQueryAdded, null);
        Log.d("addedBook():", s);
        if (cursorTotal.getCount() == cursorAdded.getCount()) {
            cursorAdded.close();
            cursorTotal.close();

            return true;
        } else {
            cursorAdded.close();
            cursorTotal.close();

            return false;
        }
    }

    public boolean addedChapter(String version, String bookName, int chapNo) {
        SQLiteDatabase db = getWritableDatabase();
        String selectQueryTotal = "SELECT " + B_KEY_MEMORY + " FROM " + version + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM + "=" + chapNo + " AND " + B_KEY_VERSE_NUM + ">" + 0;

        Cursor cursorTotal = db.rawQuery(selectQueryTotal, null);

        Log.d("addedChapter():", bookName + " " + chapNo);

        if (cursorTotal.moveToFirst()) {
            do {
                if (Integer.parseInt(cursorTotal.getString(0)) <= 0) {
                    Log.d("addedChapter():", "False");
                    return false;
                }
            } while (cursorTotal.moveToNext());
        }

        cursorTotal.close();
        Log.d("addedChapter():", "True");

        return true;
    }

    public int getTotalNumberOfVersesMemorized(String version) {
        int nVerses = -1;
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM " + version + " WHERE " + B_KEY_MEMORY + "=" + CODE_MEMORIZED + " AND " + B_KEY_VERSE_NUM + ">" + 0;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            nVerses = cursor.getInt(0);
        }

        return nVerses;
    }

    public int getTotalNumberOfChaptersMemorized(String version) {
        int nChapters = -1;
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM " + version + " WHERE " + B_KEY_MEMORY + "=" + CODE_MEMORIZED + " AND " + B_KEY_VERSE_NUM + "=" + 0 + " AND " + B_KEY_CHAP_NUM + ">" + 0;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            nChapters = cursor.getInt(0);
        }

        return nChapters;
    }

    public int getTotalNumberOfBooksMemorized(String version) {
        int nBooks = -1;
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT COUNT(*) FROM " + version + " WHERE " + B_KEY_MEMORY + "=" + CODE_MEMORIZED + " AND " + B_KEY_VERSE_NUM + "=" + 0 + " AND " + B_KEY_CHAP_NUM + "=" + 0;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            nBooks = cursor.getInt(0);
        }

        return nBooks;
    }

    public List<Integer> getAvailableVersesOfChap(String version, String bookName, int chapNum) {
        List<Integer> availVerses = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT " + B_KEY_VERSE_NUM + " FROM " + version + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM + "=" + chapNum + " AND " + B_KEY_MEMORY + "=" + CODE_NOT_ADDED + " AND " + B_KEY_VERSE_NUM + ">" + 0 + " ORDER BY " + B_KEY_VERSE_NUM;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                availVerses.add(Integer.parseInt(cursor.getString(0)));
                Log.d("AvailableVerses:", bookName + " " + chapNum + " " + Integer.parseInt(cursor.getString(0)));
            } while (cursor.moveToNext());
        }

        return availVerses;
    }

    public List<Integer> getAvailableChaptersofBook(String version, String bookName) {
        List<Integer> availChapters = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        String selectQuery = "SELECT " + B_KEY_CHAP_NUM + " FROM " + version + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM + ">" + 0 + " AND " + B_KEY_VERSE_NUM + "=" + 0 + " AND " + B_KEY_MEMORY + "=" + CODE_NOT_ADDED + " ORDER BY " + B_KEY_CHAP_NUM;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                availChapters.add(Integer.parseInt(cursor.getString(0)));
                Log.d("AvailableChapters:", bookName + Integer.parseInt(cursor.getString(0)));
            } while (cursor.moveToNext());
        }

        return availChapters;
    }

    public List<String> getAvailableBooks(String version) {
        List<String> availBooks = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        String selectQuery = "SELECT " + B_KEY_BOOK_NAME + " FROM " + version + " WHERE " + B_KEY_CHAP_NUM + "=" + 0 + " AND " + B_KEY_VERSE_NUM + "=" + 0 + " AND " + B_KEY_MEMORY + "=" + CODE_NOT_ADDED;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                availBooks.add(cursor.getString(0));
                Log.d("AvailableBooks:", cursor.getString(0));
            } while (cursor.moveToNext());
        }

        return availBooks;
    }

    public void setNotAvailableBook(String version, String bookName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        String selectQuery = "SELECT " + B_KEY_ID + " FROM " + version + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM + "=" + 0 + " AND " + B_KEY_VERSE_NUM + "=" + 0;
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String id = cursor.getString(0);

        DatabaseReference userBible = FirebaseDatabase.getInstance().getReference("user-bible").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userBible.child(version).child(bookName).child(Integer.toString(0)).child(Integer.toString(0)).setValue(CODE_ADDED);

        Log.d("setNotAvailable:", bookName + " " + 0);
        cv.put(B_KEY_BOOK_NAME, bookName);
        cv.put(B_KEY_CHAP_NUM, 0);
        cv.put(B_KEY_VERSE_NUM, 0);
        cv.put(B_KEY_VERSE_TEXT, "Dummy Text");
        cv.put(B_KEY_MEMORY, CODE_ADDED);
        db.update(version, cv, "id=" + id, null);

    }

    public void setAvailableBook(String version, String bookName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        Log.d("setAvailable:", bookName + " " + 0);

        String selectQuery = "SELECT " + B_KEY_ID + " FROM " + version + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM + "=" + 0 + " AND " + B_KEY_VERSE_NUM + "=" + 0;
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String id = cursor.getString(0);

        DatabaseReference userBible = FirebaseDatabase.getInstance().getReference("user-bible").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userBible.child(version).child(bookName).child(Integer.toString(0)).child(Integer.toString(0)).setValue(CODE_NOT_ADDED);

        cv.put(B_KEY_BOOK_NAME, bookName);
        cv.put(B_KEY_CHAP_NUM, 0);
        cv.put(B_KEY_VERSE_NUM, 0);
        cv.put(B_KEY_VERSE_TEXT, "Dummy Text");
        cv.put(B_KEY_MEMORY, 0);
        db.update(version, cv, "id=" + id, null);

    }

    public void setNotAvailableChap(String version, String bookName, int chapNum) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        Log.d("setNotAvailable:", "book " + bookName + " chap" + chapNum);

        String selectQuery = "SELECT " + B_KEY_ID + " FROM " + version + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM + "=" + chapNum + " AND " + B_KEY_VERSE_NUM + "=" + 0;
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String id = cursor.getString(0);

        DatabaseReference userBible = FirebaseDatabase.getInstance().getReference("user-bible").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userBible.child(version).child(bookName).child(Integer.toString(chapNum)).child(Integer.toString(0)).setValue(CODE_ADDED);

        cv.put(B_KEY_BOOK_NAME, bookName);
        cv.put(B_KEY_CHAP_NUM, chapNum);
        cv.put(B_KEY_VERSE_NUM, 0);
        cv.put(B_KEY_VERSE_TEXT, "Dummy Text");
        cv.put(B_KEY_MEMORY, CODE_ADDED);
        db.update(version, cv, "id=" + id, null);

    }

    public void setAvailableChap(String version, String bookName, int chapNum) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        Log.d("setAvailable:", version + " " + bookName + " " + chapNum);

        String selectQuery = "SELECT " + B_KEY_ID + " FROM " + version + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM + "=" + chapNum + " AND " + B_KEY_VERSE_NUM + "=" + 0;
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String id = cursor.getString(0);

        DatabaseReference userBible = FirebaseDatabase.getInstance().getReference("user-bible").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userBible.child(version).child(bookName).child(Integer.toString(chapNum)).child(Integer.toString(0)).setValue(CODE_NOT_ADDED);

        cv.put(B_KEY_BOOK_NAME, bookName);
        cv.put(B_KEY_CHAP_NUM, chapNum);
        cv.put(B_KEY_VERSE_NUM, 0);
        cv.put(B_KEY_VERSE_TEXT, "Dummy Text");
        cv.put(B_KEY_MEMORY, 0);
        db.update(version, cv, "id=" + id, null);

    }

    public void setVerseMemoryInDBOnly(String version, String bookName, int chapNum, int verseNum, int memory) {
        SQLiteDatabase db = getWritableDatabase();

        ReadableVerse readableVerse = getReadableVerse(version, bookName, chapNum, verseNum);

        ContentValues cv = new ContentValues();
        Log.d("setVerseMemoryInDBOnly:", version + "," + bookName + "," + chapNum + "," + verseNum + "," + memory);

        cv.put(B_KEY_BOOK_NAME, bookName);
        cv.put(B_KEY_CHAP_NUM, chapNum);
        cv.put(B_KEY_VERSE_NUM, verseNum);
        cv.put(B_KEY_VERSE_TEXT, readableVerse.get_verse_text());
        cv.put(B_KEY_MEMORY, memory);

        db.update(version, cv, "id=" + readableVerse.get_id(), null);


    }

    public List<Integer> getMemorizedVerses(String version, String bookName, int chapNum) {
        List<Integer> memorizedVerses = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT " + B_KEY_VERSE_NUM + " FROM " + version + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM + "=" + chapNum + " AND " + B_KEY_MEMORY + "=" + "3" + " ORDER BY " + B_KEY_VERSE_NUM;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                memorizedVerses.add(Integer.parseInt(cursor.getString(0)));
            } while (cursor.moveToNext());

        }

        return memorizedVerses;
    }

    public ReadableVerse getReadableVerse(String version, String bookName, int chapNum, int verseNum) {
        ReadableVerse readableVerse = new ReadableVerse();
        String selectQuery = "SELECT * FROM " + version + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM + "=" + chapNum + " AND " + B_KEY_VERSE_NUM + "=" + verseNum;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("getReadableVerse:", selectQuery);
        if (cursor.moveToFirst()) {
            do {
                readableVerse.set_id(Long.parseLong(cursor.getString(0)));
                readableVerse.set_book_name(cursor.getString(1));
                readableVerse.set_chap_num(Integer.parseInt(cursor.getString(2)));
                readableVerse.set_verse_num(Integer.parseInt(cursor.getString(3)));
                readableVerse.set_verse_text(cursor.getString(4));
                readableVerse.set_memory(Integer.parseInt(cursor.getString(5)));
            } while (cursor.moveToNext());
        }

        return readableVerse;
    }

    public void setMemoryToAdded(Section section) {
        String bookName = section.get_book_name();
        int chapNum = section.get_chapter_num();

        DatabaseReference userBible = FirebaseDatabase.getInstance().getReference("user-bible").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        Log.d("setMemoryToAdded:", section.toString());
        for (int i = section.get_start_verse_num(); i <= section.get_end_verse_num(); i++) {
            ReadableVerse readableVerse = getReadableVerse(section.get_version(), bookName, chapNum, i);

            userBible.child(section.get_version()).child(bookName).child(Integer.toString(chapNum)).child(Integer.toString(i)).setValue(CODE_ADDED);

            cv.put(B_KEY_BOOK_NAME, bookName);
            cv.put(B_KEY_CHAP_NUM, chapNum);
            cv.put(B_KEY_VERSE_NUM, i);
            cv.put(B_KEY_VERSE_TEXT, readableVerse.get_verse_text());
            cv.put(B_KEY_MEMORY, CODE_ADDED);

            db.update(section.get_version(), cv, "id=" + readableVerse.get_id(), null);
        }
    }

    public void setMemoryToNotAdded(Section section) {
        String bookName = section.get_book_name();
        int chapNum = section.get_chapter_num();

        DatabaseReference userBible = FirebaseDatabase.getInstance().getReference("user-bible").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        Log.d("setMemoryToAdded:", section.toString());
        for (int i = section.get_start_verse_num(); i <= section.get_end_verse_num(); i++) {
            ReadableVerse readableVerse = getReadableVerse(section.get_version(), bookName, chapNum, i);

            userBible.child(section.get_version()).child(bookName).child(Integer.toString(chapNum)).child(Integer.toString(i)).setValue(CODE_NOT_ADDED);
            cv.put(B_KEY_BOOK_NAME, bookName);
            cv.put(B_KEY_CHAP_NUM, chapNum);
            cv.put(B_KEY_VERSE_NUM, i);
            cv.put(B_KEY_VERSE_TEXT, readableVerse.get_verse_text());
            cv.put(B_KEY_MEMORY, 0);

            db.update(section.get_version(), cv, "id=" + readableVerse.get_id(), null);
        }
    }

    public void setSectionToMemorized(Section section) {
        String bookName = section.get_book_name();
        int chapNum = section.get_chapter_num();

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        Log.d("setSectionToMemorized:", section.toString());
        for (int i = section.get_start_verse_num(); i <= section.get_end_verse_num(); i++) {
            ReadableVerse readableVerse = getReadableVerse(section.get_version(), bookName, chapNum, i);

            cv.put(B_KEY_BOOK_NAME, bookName);
            cv.put(B_KEY_CHAP_NUM, chapNum);
            cv.put(B_KEY_VERSE_NUM, i);
            cv.put(B_KEY_VERSE_TEXT, readableVerse.get_verse_text());
            cv.put(B_KEY_MEMORY, 3);

            db.update(section.get_version(), cv, "id=" + readableVerse.get_id(), null);
        }

    }

    public void setChunkToMemorized(Chunk chunk) {
        String bookName = chunk.getBookName();
        int chapNum = chunk.getChapNum();

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        DatabaseReference userBible = FirebaseDatabase.getInstance().getReference("user-bible").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Log.d("setChunkToMemorized:", chunk.toString());
        for (int i = chunk.getStartVerseNum(); i <= chunk.getEndVerseNum(); i++) {
            ReadableVerse readableVerse = getReadableVerse(chunk.get_version(), bookName, chapNum, i);

            userBible.child(chunk.get_version()).child(bookName).child(Integer.toString(chapNum)).child(Integer.toString(i)).setValue(CODE_MEMORIZED);
            cv.put(B_KEY_BOOK_NAME, bookName);
            cv.put(B_KEY_CHAP_NUM, chapNum);
            cv.put(B_KEY_VERSE_NUM, i);
            cv.put(B_KEY_VERSE_TEXT, readableVerse.get_verse_text());
            cv.put(B_KEY_MEMORY, CODE_MEMORIZED);
            db.update(chunk.get_version(), cv, "id=" + readableVerse.get_id(), null);
        }

    }


    //Section Functions
    //-----------------
    public void addSection(Section section) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        DatabaseReference sections = FirebaseDatabase.getInstance().getReference("sections").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        String newSectionId = section.get_id();
        sections.child(newSectionId).setValue(section);

        values.put(S_KEY_BOOK_NAME, section.get_book_name());
        values.put(S_KEY_CHAP_NUM, section.get_chapter_num());
        values.put(S_KEY_START_VERSE_NUM, section.get_start_verse_num());
        values.put(S_KEY_END_VERSE_NUM, section.get_end_verse_num());
        values.put(S_KEY_ID, section.get_sec_id());
        values.put(S_KEY_VER_ID, section.get_version());

        db.insert(TABLE_SECTION, null, values);

        Log.d("Function:", "Add Section " + section.toString());
    }

    public void deleteSection(final String secId) {
        SQLiteDatabase db = getWritableDatabase();
        DatabaseReference sections = FirebaseDatabase.getInstance().getReference("sections").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        sections.child(secId).removeValue();

        final List<String> chunksToDelete = new ArrayList<>();

        final DatabaseReference chunks = FirebaseDatabase.getInstance().getReference("chunks").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        chunks.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String secIDField = "_" + C_KEY_SEC_ID;
                        if (child.child(secIDField).exists()) {
                            String childSecId = child.child(secIDField).getValue(String.class);
                            if (childSecId.equals(secId)) {
                                Log.d("Chunks Delete:", child.getKey() + ":" + child.getValue());
                                chunksToDelete.add(child.getKey());
                            }
                        }

                    }
                    for (String id : chunksToDelete) {
                        chunks.child(id).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Delete Section:", databaseError.toString());
            }
        });

        db.delete(TABLE_SECTION, S_KEY_ID + "=" + "'" + secId + "'", null);
        db.delete(TABLE_CHUNK, C_KEY_SEC_ID + "=" + "'" + secId + "'", null);
        Log.d("Function:", "Deleted " + secId);
    }

    public void deleteAllSections() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SECTION, null, null);
        Log.d("Function:", "Delete All Sections");

    }

    public Section retSection(String secId) {
        Section s = new Section();
        String selectQuery = "SELECT * FROM " + TABLE_SECTION + " WHERE " + S_KEY_ID + "=" + "'" + secId + "'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                s.set_sec_id(secId);
                s.set_book_name(cursor.getString(1));
                s.set_chapter_num(Integer.parseInt(cursor.getString(2)));
                s.set_start_verse_num(Integer.parseInt(cursor.getString(3)));
                s.set_end_verse_num(Integer.parseInt(cursor.getString(4)));
                s.set_version(cursor.getString(5));
                Log.d("Ret Section:", s.toString());
            } while (cursor.moveToNext());
        }
        cursor.close();
        return s;
    }

    public List<String> retSectionIds() {
        List<String> sections = new ArrayList<String>();

        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT DISTINCT " + C_KEY_SEC_ID + " FROM " + TABLE_CHUNK;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String id = (cursor.getString(0));
                sections.add(id);
                Log.d("Ret SecID:", id.toString());
            } while (cursor.moveToNext());
        }
        return sections;
    }

    public void mergeChunksInSection(String secId) {
        SQLiteDatabase db = getWritableDatabase();
        DatabaseReference chunks = FirebaseDatabase.getInstance().getReference("chunks").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Chunk chunk;

        String chunksQuery = "SELECT " + C_KEY_ID + " FROM " + TABLE_CHUNK + " WHERE " + C_KEY_SEC_ID + " LIKE " + "'" + secId + "'";
        Cursor cursorChunks = db.rawQuery(chunksQuery, null);
        if (cursorChunks.moveToFirst()) {
            do {
                chunks.child(cursorChunks.getString(0)).removeValue();
                Log.d("DBHandler:", "mergeChunks " + cursorChunks.getString(0));
            } while (cursorChunks.moveToNext());
        }
        db.delete(TABLE_CHUNK, C_KEY_SEC_ID + "=" + "'" + secId + "'", null);
        Log.d("Function:", "Deleted all chunks of " + secId);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, 2);
        String currDate = df.format(ca.getTime());

        String selectQuery = "SELECT * FROM " + TABLE_SECTION + " WHERE " + S_KEY_ID + "=" + "'" + secId + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        String newChunkId = chunks.push().getKey();

        if (cursor.moveToFirst()) {
            addChunk(chunk = new Chunk(newChunkId, 0, cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                    Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), currDate, 2,
                    secId, false, cursor.getString(5)));

            chunks.child(newChunkId).setValue(chunk);
            Log.d("Function:", "Added Master Chunk");
            Log.d("mergeSection:", chunk.toString());
        }

        Log.d("Function:", "Add " + secId);
    }

    public boolean checkIfMasteredSection(String secId) {
        boolean isMastered;
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT " + C_KEY_MASTERED + " FROM " + TABLE_CHUNK + " WHERE " + C_KEY_SEC_ID + "=" + "'" + secId + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int flag = Integer.parseInt(cursor.getString(0));
                if (flag == 0) {
                    Log.d("Function:", "checkIfMasteredSection False " + flag + ": " + secId);
                    return false;
                }
            } while (cursor.moveToNext());
            Log.d("Function:", "checkIfMasteredSection True " + ": " + secId);
        }
        return true;
    }

    public List<Chunk> getChunksOfSection(String sec_id) {
        List<Chunk> chunks = new ArrayList<Chunk>();

        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CHUNK + " WHERE " + C_KEY_SEC_ID + "=" + "'" + sec_id + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Chunk chunk = new Chunk();
                chunk.setId((cursor.getString(0)));
                chunk.setSeq(Integer.parseInt(cursor.getString(1)));
                chunk.setBookName(cursor.getString(2));
                chunk.setChapNum(Integer.parseInt(cursor.getString(3)));
                chunk.setStartVerseNum(Integer.parseInt(cursor.getString(4)));
                chunk.setEndVerseNum(Integer.parseInt(cursor.getString(5)));
                chunk.setNextDateOfReview(cursor.getString(6));
                chunk.setSpace(Integer.parseInt(cursor.getString(7)));
                chunk.setSecId((cursor.getString(8)));
                chunk.setMastered(Boolean.parseBoolean(cursor.getString(9)));
                chunk.set_version(cursor.getString(10));
                chunks.add(chunk);
            } while (cursor.moveToNext());
        }
        return chunks;
    }

    //Chunk Functions
    //---------------
    public void addChunk(Chunk chunk) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        DatabaseReference chunks = FirebaseDatabase.getInstance().getReference("chunks").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        chunks.child(chunk.getId()).setValue(chunk);

        values.put(C_KEY_ID, chunk.getId());
        values.put(C_KEY_SEQ, chunk.getSeq());
        values.put(C_KEY_BOOK_NAME, chunk.getBookName());
        values.put(C_KEY_CHAP_NUM, chunk.getChapNum());
        values.put(C_KEY_START_VERSE_NUM, chunk.getStartVerseNum());
        values.put(C_KEY_END_VERSE_NUM, chunk.getEndVerseNum());
        values.put(C_KEY_NEXT_DATE_OF_REVIEW, chunk.getNextDateOfReview());
        values.put(C_KEY_SPACE, chunk.getSpace());
        values.put(C_KEY_SEC_ID, chunk.getSecId());
        values.put(C_KEY_MASTERED, chunk.isMastered());
        values.put(C_KEY_VER_ID, chunk.get_version());

        db.insert(TABLE_CHUNK, null, values);

        Log.d("Function:", "Add Chunk " + chunk.getSecId() + ": " + chunk.toString());

    }

    public List<Chunk> getAllChunks() {
        List<Chunk> chunkList = new ArrayList<Chunk>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CHUNK;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Chunk chunk = new Chunk();
                chunk.setId((cursor.getString(0)));
                chunk.setSeq(Integer.parseInt(cursor.getString(1)));
                chunk.setBookName(cursor.getString(2));
                chunk.setChapNum(Integer.parseInt(cursor.getString(3)));
                chunk.setStartVerseNum(Integer.parseInt(cursor.getString(4)));
                chunk.setEndVerseNum(Integer.parseInt(cursor.getString(5)));
                chunk.setNextDateOfReview(cursor.getString(6));
                chunk.setSpace(Integer.parseInt(cursor.getString(7)));
                chunk.setSecId((cursor.getString(8)));
                chunk.setMastered(Boolean.parseBoolean(cursor.getString(9)));
                chunk.set_version(cursor.getString(10));

                // Adding contact to list
                chunkList.add(chunk);
            } while (cursor.moveToNext());
        }
        Log.d("Function:", "Get All Chunks");
        // return contact list
        return chunkList;
    }

    public List<Chunk> getAllChunksForToday(String currDate) {
        List<Chunk> chunkList = new ArrayList<Chunk>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CHUNK + " WHERE " + C_KEY_NEXT_DATE_OF_REVIEW + " LIKE " + "'" + currDate + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Chunk chunk = new Chunk();
                chunk.setId((cursor.getString(0)));
                chunk.setSeq(Integer.parseInt(cursor.getString(1)));
                chunk.setBookName(cursor.getString(2));
                chunk.setChapNum(Integer.parseInt(cursor.getString(3)));
                chunk.setStartVerseNum(Integer.parseInt(cursor.getString(4)));
                chunk.setEndVerseNum(Integer.parseInt(cursor.getString(5)));
                chunk.setNextDateOfReview(cursor.getString(6));
                chunk.setSpace(Integer.parseInt(cursor.getString(7)));
                chunk.setSecId((cursor.getString(8)));
                chunk.setMastered(Boolean.parseBoolean(cursor.getString(9)));
                chunk.set_version(cursor.getString(10));
                // Adding contact to list
                chunkList.add(chunk);
            } while (cursor.moveToNext());
        }
        Log.d("Function:", "Get All Chunks");
        // return contact list
        return chunkList;
    }

    public List<Chunk> getAllChunksThatAreOverdue(String currDate) {
        List<Chunk> chunkList = new ArrayList<Chunk>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CHUNK;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Log.d("OverdueChunks:", "CurrDate = " + currDate);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                try {
                    Log.d("OverdueChunks:", "Hmmm");
                    if (!cursor.getString(6).equals("NA")) {
                        if (df.parse(currDate).after(df.parse(cursor.getString(6)))) {
                            Chunk chunk = new Chunk();
                            Log.d("OverdueChunks:", "Entered");
                            chunk.setId((cursor.getString(0)));
                            chunk.setSeq(Integer.parseInt(cursor.getString(1)));
                            chunk.setBookName(cursor.getString(2));
                            chunk.setChapNum(Integer.parseInt(cursor.getString(3)));
                            chunk.setStartVerseNum(Integer.parseInt(cursor.getString(4)));
                            chunk.setEndVerseNum(Integer.parseInt(cursor.getString(5)));
                            chunk.setNextDateOfReview(cursor.getString(6));
                            Log.d("OverdueChunks:", "Date" + chunk.getNextDateOfReview());
                            chunk.setSpace(Integer.parseInt(cursor.getString(7)));
                            chunk.setSecId((cursor.getString(8)));
                            chunk.setMastered(Boolean.parseBoolean(cursor.getString(9)));
                            chunk.set_version(cursor.getString(10));
                            // Adding contact to list
                            chunkList.add(chunk);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        Log.d("Function:", "Get All Chunks");
        // return contact list
        return chunkList;
    }

    public Chunk getChunk(String id) {
        Chunk chunk = new Chunk();
        String selectQuery = "SELECT * FROM " + TABLE_CHUNK + " WHERE " + C_KEY_ID + " = " + "'" + id + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            chunk.setId((cursor.getString(0)));
            chunk.setSeq(Integer.parseInt(cursor.getString(1)));
            chunk.setBookName(cursor.getString(2));
            chunk.setChapNum(Integer.parseInt(cursor.getString(3)));
            chunk.setStartVerseNum(Integer.parseInt(cursor.getString(4)));
            chunk.setEndVerseNum(Integer.parseInt(cursor.getString(5)));
            chunk.setNextDateOfReview(cursor.getString(6));
            chunk.setSpace(Integer.parseInt(cursor.getString(7)));
            chunk.setSecId((cursor.getString(8)));
            chunk.setMastered(Boolean.parseBoolean(cursor.getString(9)));
            chunk.set_version(cursor.getString(10));
        }

        Log.d("Function:", "Get Chunk");
        cursor.close();

        return chunk;
    }

    public void deleteAllChunks() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHUNK, null, null);
        Log.d("Function:", "Delete All Chunks");

    }

    public void updateChunk(Chunk c, boolean b) {
        SQLiteDatabase db = getWritableDatabase();

        DatabaseReference chunks = FirebaseDatabase.getInstance().getReference("chunks").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        String chunkId = c.getId();
        chunks.child(chunkId).setValue(c);

        ContentValues cv = new ContentValues();
        Log.d("Update:", "Updating Chunk " + c.toString());
        cv.put(C_KEY_BOOK_NAME, c.getBookName());
        cv.put(C_KEY_CHAP_NUM, c.getChapNum());
        cv.put(C_KEY_START_VERSE_NUM, c.getStartVerseNum());
        cv.put(C_KEY_END_VERSE_NUM, c.getEndVerseNum());
        cv.put(C_KEY_NEXT_DATE_OF_REVIEW, c.getNextDateOfReview());
        cv.put(C_KEY_SEQ, c.getSeq());
        cv.put(C_KEY_SPACE, c.getSpace());
        cv.put(C_KEY_SEC_ID, c.getSecId());
        cv.put(C_KEY_VER_ID, c.get_version());

        if (b == true) {
            cv.put(C_KEY_MASTERED, true);
        } else {
            cv.put(C_KEY_MASTERED, c.isMastered());
        }

        db.update(TABLE_CHUNK, cv, "id=" + '"' + c.getId() + '"', null);
    }

    public void updateSiblingChunks(Chunk c) {
        int seq = c.getSeq() + 1;
        String selectQuery = "SELECT " + C_KEY_ID + "," + C_KEY_NEXT_DATE_OF_REVIEW + " FROM " + TABLE_CHUNK + " WHERE "
                + C_KEY_SEC_ID + "=" + "'" + c.getSecId() + "'" + " AND " + C_KEY_SEQ + "=" + seq;
        Log.d("Select:", selectQuery);

        DatabaseReference chunks = FirebaseDatabase.getInstance().getReference("chunks").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {

            ContentValues cv = new ContentValues();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Calendar ca = Calendar.getInstance();
            //ca.add(Calendar.DATE,1);
            String initDOR = df.format(ca.getTime());
            Log.d("Update:", "Going to update " + cursor.getString(0) + " to " + cursor.getString(1));
            if (cursor.getString(1).equals("NA")) {
                cv.put(C_KEY_NEXT_DATE_OF_REVIEW, initDOR);
                cv.put(C_KEY_SPACE, 1);
                db.update(TABLE_CHUNK, cv, "id=" + "'" + (cursor.getString(0)) + "'", null);
                Log.d("Update:", "Updated " + cursor.getString(0) + " to " + initDOR);

                String chunkId = cursor.getString(0);
                chunks.child(chunkId).child(C_KEY_SPACE).setValue(1);
                chunks.child(chunkId).child(C_KEY_NEXT_DATE_OF_REVIEW).setValue(initDOR);
            }
        }

    }
}
