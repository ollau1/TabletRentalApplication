package com.example.tabletrentalapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.tabletrentalapplication.models.RentalRequest;
import android.util.Log;
import java.util.ArrayList;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "rental_requests.db";
    private static final int DATABASE_VERSION = 1;

    // Table and Columns
    private static final String TABLE_NAME = "rental_requests";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TABLET_BRAND = "tablet_brand";
    private static final String COLUMN_INCLUDE_CABLE = "include_cable";
    private static final String COLUMN_CABLE_TYPE = "cable_type";
    private static final String COLUMN_LENDER_NAME = "lender_name";
    private static final String COLUMN_CONTACT_NUMBER = "contact_number";
    private static final String COLUMN_SUBMISSION_DATE = "submission_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TABLET_BRAND + " TEXT, " +
                COLUMN_INCLUDE_CABLE + " INTEGER, " +
                COLUMN_CABLE_TYPE + " TEXT, " +
                COLUMN_LENDER_NAME + " TEXT, " +
                COLUMN_CONTACT_NUMBER + " TEXT, " +
                COLUMN_SUBMISSION_DATE + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert a RentalRequest
    public long insertRentalRequest(RentalRequest rentalRequest) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TABLET_BRAND, rentalRequest.getTabletBrand());
        values.put(COLUMN_INCLUDE_CABLE, rentalRequest.isIncludeCable() ? 1 : 0);
        values.put(COLUMN_CABLE_TYPE, rentalRequest.getCableType());
        values.put(COLUMN_LENDER_NAME, rentalRequest.getLenderName());
        values.put(COLUMN_CONTACT_NUMBER, rentalRequest.getContactNumber());
        values.put(COLUMN_SUBMISSION_DATE, rentalRequest.getSubmissionDate());

        // Insert row
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    // Retrieve all RentalRequests
    public List<RentalRequest> getAllRentalRequests() {
        List<RentalRequest> rentalRequests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                RentalRequest rentalRequest = new RentalRequest(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TABLET_BRAND)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INCLUDE_CABLE)) == 1,
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CABLE_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LENDER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTACT_NUMBER))
                );
                rentalRequest.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))); // Set the ID
                rentalRequest.setSubmissionDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUBMISSION_DATE)));
                rentalRequests.add(rentalRequest);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return rentalRequests;
    }

    public void deleteRentalRequest(RentalRequest rentalRequest) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(
                TABLE_NAME,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(rentalRequest.getId())}
        );
        db.close();

        // Debugging: Log the deletion result
        Log.d("DatabaseHelper", "Rows deleted: " + rowsDeleted);
    }
}