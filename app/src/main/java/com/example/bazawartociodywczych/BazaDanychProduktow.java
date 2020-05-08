package com.example.bazawartociodywczych;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BazaDanychProduktow extends SQLiteOpenHelper {

    private static final String NAZWA_TABELI = "Produkty";
    private static final String KOL1 = "ID";
    private static final String KOL2 = "nazwaProduktu";
    private static final String KOL3 = "kalorycznosc";
    private static final String KOL4 = "bialko";
    private static final String KOL5 = "weglowodany";
    private static final String KOL6 = "tluszcze";
    private static final String KOL7 = "wagaProduktu";

    public BazaDanychProduktow(Context context) {
        super(context, NAZWA_TABELI, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + NAZWA_TABELI + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + KOL2 + " TEXT, "
                + KOL3 + " INTEGER, " + KOL4 + " INTEGER, " + KOL5 + " INTEGER, " + KOL6 + " INTEGER, " + KOL7 + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + NAZWA_TABELI);
        onCreate(db);
    }

    public void dodajProduktDoBazy(Produkt produkt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues wartosci = new ContentValues();
        wartosci.put(KOL2, produkt.getNazwaProduktu());
        wartosci.put(KOL3, produkt.getKalorycznosc());
        wartosci.put(KOL4, produkt.getBialko());
        wartosci.put(KOL5, produkt.getWeglowodany());
        wartosci.put(KOL6, produkt.getTluszcze());
        wartosci.put(KOL7, produkt.getWagaProduktu());

        db.insert(NAZWA_TABELI, null, wartosci);
    }

    public Cursor uzyskajProduktZBazy() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + NAZWA_TABELI;
        Cursor produkt = db.rawQuery(query, null);
        return produkt;
    }

    public Cursor uzyskajKonkretnyProdukt(String nazwa) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + NAZWA_TABELI + " WHERE " + KOL2 + " = '" + nazwa + "'";
        Cursor produkt = db.rawQuery(query, null);
        return produkt;
    }

    public Cursor uzyskajIDProduktu(String nazwa) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + KOL1 + " FROM " + NAZWA_TABELI + " WHERE " + KOL2 + " = '" + nazwa + "'";
        Cursor produkt = db.rawQuery(query, null);
        return produkt;
    }
}