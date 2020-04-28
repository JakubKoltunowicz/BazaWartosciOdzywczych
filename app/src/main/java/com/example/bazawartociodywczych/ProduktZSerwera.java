package com.example.bazawartociodywczych;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ProduktZSerwera {

    private String nazwa = "Papryka";

    public Produkt wyslijZdjecie() {
        Produkt mProdukt = new Produkt();
        mProdukt.setNazwaProduktu(nazwa);
        mProdukt.setKalorycznosc(31);
        mProdukt.setBialko(2);
        mProdukt.setWeglowodany(9);
        mProdukt.setTluszcze(1);
        mProdukt.setWagaProduktu(200);
        return mProdukt;
    }
}
