package com.code.codemercenaries.girdthysword;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Joel Kingsley on 17-10-2017.
 */

public class DBHandler extends SQLiteAssetHelper {
    public static final String B_KEY_ID = "id";
    public static final String B_KEY_BOOK_NAME = "book_name";
    public static final String B_KEY_CHAP_NUM = "chapter_num";
    public static final String B_KEY_VERSE_NUM = "verse_num";
    public static final String B_KEY_VERSE_TEXT = "verse_text";
    public static final String B_KEY_MEMORY = "memory";
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
    public static final String S_KEY_ID = "id";
    public static final String S_KEY_BOOK_NAME = "book_name";
    public static final String S_KEY_CHAP_NUM = "chapter_num";
    public static final String S_KEY_START_VERSE_NUM = "start_verse_num";
    public static final String S_KEY_END_VERSE_NUM = "end_verse_num";
    public static final String S_KEY_SEC_ID = "sec_id";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "main.db";
    private static final String TABLE_BIBLE = "bible";
    private static final String TABLE_CHUNK = "chunk";
    private static final String TABLE_SECTION = "section";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //ReadableVerse Functions
    //-----------------------
    public List<String> getBookNames(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + B_KEY_BOOK_NAME + " FROM " + TABLE_BIBLE;
        Cursor cursor = db.rawQuery(query,null);
        List<String> bookNames = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do{
                bookNames.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        Log.d("Function:","getBookNames");
        db.close();
        return bookNames;
    }

    public int getNumOfVerse(String bookName, int chapNum){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_BIBLE + " WHERE "
                + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM + " = " + chapNum;
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();
        Log.d("getNumOfVerse:",bookName + " " + chapNum + ", " +count);
        return count;
    }

    public int getNumofChap(String bookName){
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT DISTINCT " + B_KEY_CHAP_NUM + " FROM " + TABLE_BIBLE + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"';
        Cursor cursor = db.rawQuery(selectQuery,null);
        int count = cursor.getCount();
        cursor.close();
        Log.d("getNumOfChap:",bookName + ", " +count);
        return count;
    }

    public String getVerse(String bookName, int chapNum, int verseNum) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + B_KEY_VERSE_TEXT + " FROM " + TABLE_BIBLE + " WHERE "
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
        db.close();
        return verse;
    }

    public List<String> getChapter(String bookName,int chapNo){
        List<String> chapter = new ArrayList<String>();

        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT " + B_KEY_VERSE_TEXT + " FROM " + TABLE_BIBLE + " WHERE "
                + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM
                + " = " + chapNo + " ORDER BY " + B_KEY_VERSE_NUM;
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                chapter.add((cursor.getString(0)));
            }while(cursor.moveToNext());
        }
        return chapter;
    }

    public List<ReadableVerse> getChapterWithMemory(String bookName,int chapNo){
        List<ReadableVerse> chapter = new ArrayList<ReadableVerse>();

        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT " + B_KEY_VERSE_TEXT + "," + B_KEY_MEMORY + " FROM " + TABLE_BIBLE + " WHERE "
                + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM
                + " = " + chapNo + " ORDER BY " + B_KEY_VERSE_NUM;
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                ReadableVerse readableVerse = new ReadableVerse();
                readableVerse.set_verse_text(cursor.getString(0));
                readableVerse.set_memory(Integer.parseInt(cursor.getString(1)));
                readableVerse.set_chap_num(-1);
                readableVerse.set_book_name("");
                readableVerse.set_verse_num(-1);
                readableVerse.set_id(-1);
                chapter.add(readableVerse);
            }while(cursor.moveToNext());
        }
        return chapter;
    }

    public boolean addedBook(String s) {
        SQLiteDatabase db = getWritableDatabase();
        String selectQueryTotal = "SELECT " + B_KEY_MEMORY + " FROM " + TABLE_BIBLE + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + s + '"';
        String selectQueryAdded = "SELECT " + B_KEY_MEMORY + " FROM " + TABLE_BIBLE + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + s + '"' + " AND " + B_KEY_MEMORY + ">" + "0";
        Cursor cursorTotal = db.rawQuery(selectQueryTotal,null);
        Cursor cursorAdded = db.rawQuery(selectQueryAdded,null);
        Log.d("addedBook():",s);
        if(cursorTotal.getCount() == cursorAdded.getCount()){
            cursorAdded.close();
            cursorTotal.close();
            return true;
        }
        else{
            cursorAdded.close();
            cursorTotal.close();
            return false;
        }
    }

    public boolean addedChapter(String bookName, int chapNo){
        SQLiteDatabase db = getWritableDatabase();
        String selectQueryTotal = "SELECT " + B_KEY_MEMORY + " FROM " + TABLE_BIBLE + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM + "=" + chapNo;

        Cursor cursorTotal = db.rawQuery(selectQueryTotal,null);

        Log.d("addedChapter():",bookName + " " + chapNo);

        if(cursorTotal.moveToFirst()){
            do{
                if(Integer.parseInt(cursorTotal.getString(0)) <= 0){
                    Log.d("addedChapter():","False");
                    return false;
                }
            }while(cursorTotal.moveToNext());
        }

        cursorTotal.close();
        Log.d("addedChapter():","True");
        return true;
    }

    public List<Integer> getAvailableVersesOfChap(String bookName,int chapNum){
        List<Integer> availVerses = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT " + B_KEY_VERSE_NUM + " FROM " + TABLE_BIBLE + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM + "=" + chapNum + " AND " + B_KEY_MEMORY + "=" + "0" + " ORDER BY " + B_KEY_VERSE_NUM;
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                availVerses.add(Integer.parseInt(cursor.getString(0)));
            }while(cursor.moveToNext());
        }
        return availVerses;
    }

    public List<Integer> getMemorizedVerses(String bookName, int chapNum) {
        List<Integer> memorizedVerses = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT " + B_KEY_VERSE_NUM + " FROM " + TABLE_BIBLE + " WHERE " + B_KEY_BOOK_NAME + " LIKE " + '"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM + "=" + chapNum + " AND " + B_KEY_MEMORY + "=" + "2" + " ORDER BY " + B_KEY_VERSE_NUM;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                memorizedVerses.add(Integer.parseInt(cursor.getString(0)));
            } while (cursor.moveToNext());

        }
        return memorizedVerses;
    }

    public ReadableVerse getReadableVerse(String bookName,int chapNum,int verseNum){
        ReadableVerse readableVerse = new ReadableVerse();
        String selectQuery = "SELECT * FROM " + TABLE_BIBLE + " WHERE " + B_KEY_BOOK_NAME + " LIKE " +'"' + bookName + '"' + " AND " + B_KEY_CHAP_NUM + "=" + chapNum + " AND " + B_KEY_VERSE_NUM + "=" + verseNum;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        Log.d("getReadableVerse:",selectQuery);
        if(cursor.moveToFirst()){
            do{
                readableVerse.set_id(Long.parseLong(cursor.getString(0)));
                readableVerse.set_book_name(cursor.getString(1));
                readableVerse.set_chap_num(Integer.parseInt(cursor.getString(2)));
                readableVerse.set_verse_num(Integer.parseInt(cursor.getString(3)));
                readableVerse.set_verse_text(cursor.getString(4));
                readableVerse.set_memory(Integer.parseInt(cursor.getString(5)));
            }while(cursor.moveToNext());
        }

        return readableVerse;
    }

    public void setMemoryToAdded(Section section){
        String bookName = section.get_book_name();
        int chapNum = section.get_chap_num();

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        Log.d("setMemoryToAdded:",section.toString());
        for(int i=section.get_start_verse_num();i<=section.get_end_verse_num();i++){
            ReadableVerse readableVerse = getReadableVerse(bookName,chapNum,i);

            cv.put(B_KEY_BOOK_NAME,bookName);
            cv.put(B_KEY_CHAP_NUM,chapNum);
            cv.put(B_KEY_VERSE_NUM,i);
            cv.put(B_KEY_VERSE_TEXT,readableVerse._verse_text);
            cv.put(B_KEY_MEMORY,1);

            db.update(TABLE_BIBLE,cv,"id="+readableVerse.get_id(),null);
        }
    }

    public void setMemoryToNotAdded(Section section){
        String bookName = section.get_book_name();
        int chapNum = section.get_chap_num();

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        Log.d("setMemoryToAdded:",section.toString());
        for(int i=section.get_start_verse_num();i<=section.get_end_verse_num();i++){
            ReadableVerse readableVerse = getReadableVerse(bookName,chapNum,i);

            cv.put(B_KEY_BOOK_NAME,bookName);
            cv.put(B_KEY_CHAP_NUM,chapNum);
            cv.put(B_KEY_VERSE_NUM,i);
            cv.put(B_KEY_VERSE_TEXT,readableVerse._verse_text);
            cv.put(B_KEY_MEMORY,0);

            db.update(TABLE_BIBLE,cv,"id="+readableVerse.get_id(),null);
        }
    }

    public void setSectionToMemorized(Section section) {
        String bookName = section.get_book_name();
        int chapNum = section.get_chap_num();

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        Log.d("setSectionToMemorized:", section.toString());
        for (int i = section.get_start_verse_num(); i <= section.get_end_verse_num(); i++) {
            ReadableVerse readableVerse = getReadableVerse(bookName, chapNum, i);

            cv.put(B_KEY_BOOK_NAME, bookName);
            cv.put(B_KEY_CHAP_NUM, chapNum);
            cv.put(B_KEY_VERSE_NUM, i);
            cv.put(B_KEY_VERSE_TEXT, readableVerse._verse_text);
            cv.put(B_KEY_MEMORY, 2);

            db.update(TABLE_BIBLE, cv, "id=" + readableVerse.get_id(), null);
        }
    }

    public void setChunkToMemorized(Chunk chunk) {
        String bookName = chunk.getBookName();
        int chapNum = chunk.getChapNum();

        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        Log.d("setChunkToMemorized:", chunk.toString());
        for (int i = chunk.getStartVerseNum(); i <= chunk.getEndVerseNum(); i++) {
            ReadableVerse readableVerse = getReadableVerse(bookName, chapNum, i);

            cv.put(B_KEY_BOOK_NAME, bookName);
            cv.put(B_KEY_CHAP_NUM, chapNum);
            cv.put(B_KEY_VERSE_NUM, i);
            cv.put(B_KEY_VERSE_TEXT, readableVerse._verse_text);
            cv.put(B_KEY_MEMORY, 2);
            db.update(TABLE_BIBLE, cv, "id=" + readableVerse.get_id(), null);
        }
    }


    //Section Functions
    //-----------------
    public void addSection(Section section){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(S_KEY_BOOK_NAME,section.get_book_name());
        values.put(S_KEY_CHAP_NUM,section.get_chap_num());
        values.put(S_KEY_START_VERSE_NUM,section.get_start_verse_num());
        values.put(S_KEY_END_VERSE_NUM,section.get_end_verse_num());
        values.put(S_KEY_SEC_ID,section.get_sec_id());

        db.insert(TABLE_SECTION,null,values);
        db.close();
        Log.d("Function:","Add Section " + section.toString());
    }

    public void deleteSection(long secId){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_SECTION,S_KEY_SEC_ID + "=" + secId,null);
        db.delete(TABLE_CHUNK,C_KEY_SEC_ID + "=" + secId,null);
        Log.d("Function:","Deleted " + secId);
    }

    public void deleteAllSections(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SECTION,null,null);
        Log.d("Function:","Delete All Sections");

    }

    public int getMaxSecId(){
        int maxSecId=-1;
        String selectQuery = "SELECT " + C_KEY_SEC_ID + " FROM " + TABLE_SECTION + " ORDER BY " + C_KEY_SEC_ID;
        Log.d("Select:",selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToLast()){
            maxSecId = Integer.parseInt(cursor.getString(0));
        }
        Log.d("MaxSecId:","MaxSecId=" + maxSecId);
        cursor.close();
        return maxSecId;
    }

    public Section retSection(int secId){
        Section s = new Section();
        String selectQuery = "SELECT * FROM " + TABLE_SECTION + " WHERE " + S_KEY_SEC_ID + "=" + secId;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                s.set_id(Long.parseLong(cursor.getString(0)));
                s.set_book_name(cursor.getString(1));
                s.set_chap_num(Integer.parseInt(cursor.getString(2)));
                s.set_start_verse_num(Integer.parseInt(cursor.getString(3)));
                s.set_end_verse_num(Integer.parseInt(cursor.getString(4)));
                s.set_sec_id(secId);
                Log.d("Ret Section:", s.toString());
            }while(cursor.moveToNext());
        }
        cursor.close();
        return s;
    }

    public List<Integer> retSectionIds(){
        List<Integer> sections = new ArrayList<Integer>();

        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT DISTINCT " + C_KEY_SEC_ID + " FROM " + TABLE_CHUNK;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                Integer id = Integer.parseInt(cursor.getString(0));
                sections.add(id);
                Log.d("Ret SecID:", id.toString());
            }while(cursor.moveToNext());
        }
        return sections;
    }

    public void mergeChunksInSection(int secId){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CHUNK,C_KEY_SEC_ID + "=" + secId,null);
        Log.d("Function:","Deleted all chunks of " + secId);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE,2);
        String currDate = df.format(ca.getTime());

        String selectQuery = "SELECT * FROM " + TABLE_SECTION + " WHERE " + S_KEY_SEC_ID + "=" + secId;
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            addChunk(new Chunk(0,cursor.getString(1),Integer.parseInt(cursor.getString(2)),
                    Integer.parseInt(cursor.getString(3)),Integer.parseInt(cursor.getString(4)),currDate,2,
                    secId,false));
            Log.d("Function:","Added Master Chunk");
        }
        db.close();
        Log.d("Function:","Add " + secId);
    }

    public boolean checkIfMasteredSection(int secId){
        boolean isMastered;
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT " + C_KEY_MASTERED + " FROM " + TABLE_CHUNK + " WHERE " + C_KEY_SEC_ID + "=" + secId;
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                int flag = Integer.parseInt(cursor.getString(0));
                if(flag == 0){
                    Log.d("Function:","checkIfMasteredSection False " + flag + ": " + secId);
                    return false;
                }
            }while(cursor.moveToNext());
            Log.d("Function:","checkIfMasteredSection True " + ": " + secId);
        }
        return true;
    }

    public List<Chunk> getChunksOfSection(String sec_id) {
        List<Chunk> chunks = new ArrayList<Chunk>();

        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_CHUNK + " WHERE " + C_KEY_SEC_ID + "=" + sec_id;
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                Chunk chunk = new Chunk();
                chunk.setId(Long.parseLong(cursor.getString(0)));
                chunk.setSeq(Integer.parseInt(cursor.getString(1)));
                chunk.setBookName(cursor.getString(2));
                chunk.setChapNum(Integer.parseInt(cursor.getString(3)));
                chunk.setStartVerseNum(Integer.parseInt(cursor.getString(4)));
                chunk.setEndVerseNum(Integer.parseInt(cursor.getString(5)));
                chunk.setNextDateOfReview(cursor.getString(6));
                chunk.setSpace(Integer.parseInt(cursor.getString(7)));
                chunk.setSecId(Integer.parseInt(cursor.getString(8)));
                chunk.setMastered(Boolean.parseBoolean(cursor.getString(9)));

                chunks.add(chunk);
            }while(cursor.moveToNext());
        }
        return chunks;
    }

    //Chunk Functions
    //---------------
    public void addChunk(Chunk chunk){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(C_KEY_SEQ,chunk.getSeq());
        values.put(C_KEY_BOOK_NAME,chunk.getBookName());
        values.put(C_KEY_CHAP_NUM,chunk.getChapNum());
        values.put(C_KEY_START_VERSE_NUM,chunk.getStartVerseNum());
        values.put(C_KEY_END_VERSE_NUM,chunk.getEndVerseNum());
        values.put(C_KEY_NEXT_DATE_OF_REVIEW,chunk.getNextDateOfReview());
        values.put(C_KEY_SPACE,chunk.getSpace());
        values.put(C_KEY_SEC_ID,chunk.getSecId());
        values.put(C_KEY_MASTERED,chunk.isMastered());

        db.insert(TABLE_CHUNK,null,values);
        db.close();
        Log.d("Function:","Add Chunk");

    }

    public List<Chunk> getAllChunks(){
        List<Chunk> chunkList = new ArrayList<Chunk>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CHUNK;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Chunk chunk = new Chunk();
                chunk.setId(Long.parseLong(cursor.getString(0)));
                chunk.setSeq(Integer.parseInt(cursor.getString(1)));
                chunk.setBookName(cursor.getString(2));
                chunk.setChapNum(Integer.parseInt(cursor.getString(3)));
                chunk.setStartVerseNum(Integer.parseInt(cursor.getString(4)));
                chunk.setEndVerseNum(Integer.parseInt(cursor.getString(5)));
                chunk.setNextDateOfReview(cursor.getString(6));
                chunk.setSpace(Integer.parseInt(cursor.getString(7)));
                chunk.setSecId(Integer.parseInt(cursor.getString(8)));
                chunk.setMastered(Boolean.parseBoolean(cursor.getString(9)));

                // Adding contact to list
                chunkList.add(chunk);
            } while (cursor.moveToNext());
        }
        Log.d("Function:","Get All Chunks");
        // return contact list
        return chunkList;
    }

    public Chunk getChunk(long id){
        Chunk chunk = new Chunk();
        String selectQuery = "SELECT * FROM " + TABLE_CHUNK + " WHERE " + C_KEY_ID + " = " + id + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            chunk.setId(Long.parseLong(cursor.getString(0)));
            chunk.setSeq(Integer.parseInt(cursor.getString(1)));
            chunk.setBookName(cursor.getString(2));
            chunk.setChapNum(Integer.parseInt(cursor.getString(3)));
            chunk.setStartVerseNum(Integer.parseInt(cursor.getString(4)));
            chunk.setEndVerseNum(Integer.parseInt(cursor.getString(5)));
            chunk.setNextDateOfReview(cursor.getString(6));
            chunk.setSpace(Integer.parseInt(cursor.getString(7)));
            chunk.setSecId(Integer.parseInt(cursor.getString(8)));
            chunk.setMastered(Boolean.parseBoolean(cursor.getString(9)));
        }

        Log.d("Function:","Get Chunk");
        cursor.close();
        db.close();
        return chunk;
    }

    public void deleteAllChunks(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHUNK,null,null);
        Log.d("Function:","Delete All Chunks");

    }

    public int getChunksCount(){
        String countQuery = "SELECT * FROM " + TABLE_CHUNK;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        Log.d("Function:","Get Chunks Count");
        return count;
    }

    public void updateChunk(Chunk c, boolean b) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        Log.d("Update:","Updating Chunk " + c.toString());
        cv.put(C_KEY_BOOK_NAME,c.getBookName());
        cv.put(C_KEY_CHAP_NUM,c.getChapNum());
        cv.put(C_KEY_START_VERSE_NUM,c._start_verse_num);
        cv.put(C_KEY_END_VERSE_NUM,c.getEndVerseNum());
        cv.put(C_KEY_NEXT_DATE_OF_REVIEW,c.getNextDateOfReview());
        cv.put(C_KEY_SEQ,c.getSeq());
        cv.put(C_KEY_SPACE,c.getSpace());
        cv.put(C_KEY_SEC_ID,c.getSecId());
        if(b==true){
            cv.put(C_KEY_MASTERED,true);
        }
        else{
            cv.put(C_KEY_MASTERED,c.isMastered());
        }

        db.update(TABLE_CHUNK, cv, "id=" + c.getId(), null);
    }

    public Chunk getNextChunk(long id){
        Chunk chunk = new Chunk();
        String selectQuery = "SELECT * FROM " + TABLE_CHUNK + " WHERE " + C_KEY_ID + " = " + id + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            chunk.setId(Long.parseLong(cursor.getString(0)));
            chunk.setSeq(Integer.parseInt(cursor.getString(1)));
            chunk.setBookName(cursor.getString(2));
            chunk.setChapNum(Integer.parseInt(cursor.getString(3)));
            chunk.setStartVerseNum(Integer.parseInt(cursor.getString(4)));
            chunk.setEndVerseNum(Integer.parseInt(cursor.getString(5)));
            chunk.setNextDateOfReview(cursor.getString(6));
            chunk.setSpace(Integer.parseInt(cursor.getString(7)));
            chunk.setSecId(Integer.parseInt(cursor.getString(8)));
            chunk.setMastered(Boolean.parseBoolean(cursor.getString(9)));
        }

        Log.d("Function:","Get Chunk");
        cursor.close();
        return chunk;
    }

    public void updateSiblingChunks(Chunk c){
        int seq = c.getSeq() + 1;
        String selectQuery = "SELECT " + C_KEY_ID + "," + C_KEY_NEXT_DATE_OF_REVIEW + " FROM " + TABLE_CHUNK + " WHERE "
                + C_KEY_SEC_ID + "=" + c.getSecId() + " AND " + C_KEY_SEQ + "=" + seq;
        Log.d("Select:",selectQuery);
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            ContentValues cv = new ContentValues();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Calendar ca = Calendar.getInstance();
            //ca.add(Calendar.DATE,1);
            String initDOR = df.format(ca.getTime());
            Log.d("Update:","Going to update " + cursor.getString(0) + " to " + cursor.getString(1));
            if(cursor.getString(1).equals("NA")){
                cv.put(C_KEY_NEXT_DATE_OF_REVIEW,initDOR);
                cv.put(C_KEY_SPACE,1);
                db.update(TABLE_CHUNK,cv,"id="+Long.parseLong(cursor.getString(0)),null);
                Log.d("Update:","Updated " + cursor.getString(0) + " to " + initDOR);
            }
        }
        db.close();
    }

    public boolean checkIfMasteredChunk(long id){
        boolean isMastered;
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "SELECT " + C_KEY_MASTERED + " FROM " + TABLE_CHUNK + " WHERE " + C_KEY_ID + "=" + id;
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            int flag = Integer.parseInt(cursor.getString(0));
            if(flag == 0){
                Log.d("Function:","checkIfMasteredChunk False " + flag + ": " + id);
                return false;
            }
        }
        return true;
    }


}
