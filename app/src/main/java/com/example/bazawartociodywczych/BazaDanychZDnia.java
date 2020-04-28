package com.example.bazawartociodywczych;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BazaDanychZDnia extends SQLiteOpenHelper {

    private static final String NAZWA_TABELI = "ListaProduktow";
    private static final String KOL1 = "ID";
    private static final String KOL2 = "nazwaProduktu";
    private static final String KOL3 = "kalorycznosc";
    private static final String KOL4 = "bialko";
    private static final String KOL5 = "weglowodany";
    private static final String KOL6 = "tluszcze";
    private static final String KOL7 = "miara";
    private static final String KOL8 = "data";

    public BazaDanychZDnia(Context context) {
        super(context, NAZWA_TABELI, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + NAZWA_TABELI + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + KOL2 + " TEXT, "
                + KOL3 + " INTEGER, " + KOL4 + " INTEGER, " + KOL5 + " INTEGER, " + KOL6 + " INTEGER, " + KOL7 + " INTEGER, " + KOL8 + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + NAZWA_TABELI);
        onCreate(db);
    }

    public void dodajProduktDoBazy(Produkt produkt, String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues wartosci = new ContentValues();
        wartosci.put(KOL2, produkt.getNazwaProduktu());
        wartosci.put(KOL3, produkt.getKalorycznosc());
        wartosci.put(KOL4, produkt.getBialko());
        wartosci.put(KOL5, produkt.getWeglowodany());
        wartosci.put(KOL6, produkt.getTluszcze());
        wartosci.put(KOL7, produkt.getWagaProduktu());
        wartosci.put(KOL8, data);

        db.insert(NAZWA_TABELI, null, wartosci);
    }

    public Cursor uzyskajProduktZBazy(String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + NAZWA_TABELI + " WHERE " + KOL8 + " = '" + data + "'";
        Cursor produkt = db.rawQuery(query, null);
        return produkt;
    }

    public Cursor uzyskajNazweMiareProduktu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + KOL2 + ", " + KOL7 + " FROM " + NAZWA_TABELI + " WHERE " + KOL1 + " = '" + id + "'";
        Cursor produkt = db.rawQuery(query, null);
        return produkt;
    }

    public void usunProdukt(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + NAZWA_TABELI + " WHERE " + KOL1 + " = '" + id + "'";
        db.execSQL(query);
    }

    public void zaaktualizujMiare(int nowaMiara, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + NAZWA_TABELI + " SET " + KOL7 + " = '" + nowaMiara + "' WHERE " + KOL1 + " = '" + id + "'";
        db.execSQL(query);
    }

}