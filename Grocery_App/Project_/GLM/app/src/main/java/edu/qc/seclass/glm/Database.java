package edu.qc.seclass.glm;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, "Database.db", null, 1);
    }

    // List_Table Create Statements
    private static final String Create_List_Table = "CREATE TABLE List_Table (PK INTEGER PRIMARY KEY, Name varchar(50));";

    private static final String Create_Bridge_Table = "CREATE TABLE Bridge_Table " +
            "(PK INTEGER PRIMARY KEY, List_FK INT, Items_FK INT, Quantity INT, Checked CHAR(1));";

    private static final String Create_Items_Table = "CREATE TABLE Items_Table " +
            "(PK INTEGER PRIMARY KEY, Name varchar(50), Type INT, Quantity varchar(20));";

    private static final String Create_Type_Table = "CREATE TABLE Type_Table (PK INTEGER PRIMARY KEY, Item_Type varchar(25));";

    // Fill Item and Type Tables
    private static final String Insert_Type_Table = "INSERT INTO Type_Table\n" +
            "VALUES (NULL, \"Fruit\"), (NULL, \"Vegetable\"), (NULL, \"Meat\"), (NULL, \"Cereal\"), (NULL, \"Drink\"), \n" +
            "(NULL, \"Frozen Food\"), (NULL, \"Snack\"), (NULL, \"Canned Food\");\n";

    private static final String Insert_Items_Table = "INSERT INTO Items_Table\n" +
            "VALUES (NULL, \"Apple\", 1, \"\"), (NULL, \"Orange\", 1, \"\"), (NULL, \"Beef\", 3, \"lbs\"), " +
            "(NULL, \"Chicken\", 3, \"lbs\"), (NULL, \"Lettuce\", 2, \"leaves\"), (NULL, \"Banana\", 1, \"bunches\"), " +
            "(NULL, \"Potato Chips\", 7, \"bags\"), (NULL, \"Soda\", 5, \"bottles\"), " +
            "(NULL, \"Cocoa Puffs\", 4, \"boxes\"), (NULL, \"Ice Cream\", 6, \"boxes\"), " +
            "(NULL, \"Canned Tuna\", 8, \"cans\"), (NULL, \"Lucky Charms\", 4, \"boxes\"), " +
            "(NULL, \"Water\", 5, \"bottles\"), (NULL, \"Pizza\", 6, \"boxes\"), " +
            "(NULL, \"Canned Beans\", 8, \"cans\"), (NULL, \"Twinkies\", 7, \"packs\");";

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(Create_List_Table);
        db.execSQL(Create_Bridge_Table);
        db.execSQL(Create_Items_Table);
        db.execSQL(Create_Type_Table);
        db.execSQL(Insert_Type_Table);
        db.execSQL(Insert_Items_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS List_Table");
        db.execSQL("DROP TABLE IF EXISTS Bridge_Table");
        db.execSQL("DROP TABLE IF EXISTS Items_Table");
        db.execSQL("DROP TABLE IF EXISTS Type_Table");
        onCreate(db);
    }

    public Boolean insertdata(String name, int type, String quanType) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("Type", type);
        contentValues.put("Quantity", quanType);
        long result = DB.insert("Items_Table", null, contentValues);
        if (result==-1) return false;
        else return true;
    }

    public Cursor getdata () {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Items_Table", null);
        return cursor;
    }

    public Cursor getList() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Type_Table", null);
        return cursor;
    }

    public Cursor getType(int x) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT Item_Type FROM Type_Table WHERE PK = " + x, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getItem(int x) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT Name FROM Type_Table, Items_Table " +
                "WHERE Type_Table.PK = Items_Table.Type AND Type_Table.PK = " + x, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getOffset(int x, int y) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT Name FROM Type_Table, Items_Table " +
                "WHERE Type_Table.PK = Items_Table.Type AND Type_Table.PK = " + x +
                " LIMIT 1 OFFSET " + y, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Boolean insertListName(String listName) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", listName);
        long result = DB.insert("List_Table", null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    public Cursor getListNames () {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from List_Table", null);
        return cursor;
    }

    public Boolean deleteListName (String listName) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from List_Table where name = ?", new String[]{listName});
        if (cursor.getCount() > 0) {
            long result = DB.delete("List_Table", "name=?", new String[]{listName});
            return result != -1;
        } else {
            return false;
        }
    }

    public Cursor getListPK (String listName) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from List_Table where name = ?", new String[]{listName});
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getItemPK (String itemName) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Items_Table where name = ?", new String[]{itemName});
        cursor.moveToFirst();
        return cursor;
    }

    public Boolean doesItemExist(String itemName) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Items_Table where name = ?", new String[]{itemName});
        if (cursor.getCount() == 0) {
            return false;
        }
        return true;
    }

    public Boolean insertItemToList (int listPK, int itemPK, int quantity) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("List_FK", listPK);
        contentValues.put("Items_FK", itemPK);
        contentValues.put("Quantity", quantity);
        contentValues.put("Checked", "F");
        long result = DB.insert("Bridge_Table", null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    public Cursor getItemsInList (String listName) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM Items_Table INNER JOIN Bridge_Table ON Items_Table.PK = Bridge_Table.Items_FK " +
                " INNER JOIN List_Table ON List_Table.PK = Bridge_Table.List_FK WHERE List_Table.Name =? ",  new String[]{listName});
        return cursor;
    }

    public Cursor searchList (String input) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM Items_Table WHERE Name LIKE \'%" + input + "%\'", null);
        return cursor;
    }

    public Boolean changeQuantityForItem (int itemId, int newQuantity) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Quantity", newQuantity);
        Cursor cursor = DB.rawQuery("SELECT * FROM Bridge_Table WHERE PK = " + itemId, null);
        if (cursor.getCount() > 0){
            long result = DB.update("Bridge_Table", contentValues, "PK = " + itemId, null);
            if (result == -1) return false;
        }
        return true;
    }

    public Boolean deleteItem (int itemId) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM Bridge_Table WHERE PK = " + itemId, null);
        if (cursor.getCount() > 0){
            long result = DB.delete("Bridge_Table", "PK= " + itemId, null);
            return result != -1;
        }
        return true;
    }

    public Boolean checkOffItem (int itemId) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM Bridge_Table WHERE PK = " + itemId, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put("Checked", true);
        if (cursor.getCount() > 0){
            long result = DB.update("Bridge_Table", contentValues, "PK = " + itemId, null);
            if (result == -1) return false;
        }
        return true;
    }

    public Boolean checkOnItem(int itemId) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM Bridge_Table WHERE PK = " + itemId, null);
        ContentValues contentValues = new ContentValues();
        contentValues.put("Checked", false);
        if (cursor.getCount() > 0){
            long result = DB.update("Bridge_Table", contentValues, "PK = " + itemId, null);
            if (result == -1) return false;
        }
        return true;
    }

    public Boolean deleteListItems(int id) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM Bridge_Table WHERE List_FK = " + id, null);
        if (cursor.getCount() > 0){
            long result = DB.delete("Bridge_Table", "List_FK = " + id, null);
            return result != -1;
        }
        return true;
    }

    public boolean updateListName(String listName, String newListName) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM List_Table WHERE Name = ?", new String[] {listName});
        if (cursor.getCount() > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("Name", newListName);
            DB.update("List_Table", contentValues, "Name =?", new String[] {listName});
        } else {
            return false;
        }
        return true;
    }
}
