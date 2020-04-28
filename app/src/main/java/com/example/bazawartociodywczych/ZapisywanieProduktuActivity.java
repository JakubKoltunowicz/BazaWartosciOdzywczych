package com.example.bazawartociodywczych;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ZapisywanieProduktuActivity extends AppCompatActivity {

    private Button mBtnDodajProdukt;
    private EditText mMiaraProduktu;
    private String mWybranaData, mMiara1, mWybranaNazwa;
    private int mMiara, mKalorycznosc, mBialko, mWeglowodany, mTluszcze, mWaga;
    private TextView mNazwaProduktu, mWagaProduktu;
    private BazaDanychProduktow mBazaDanychProduktow;
    private BazaDanychZDnia mBazaDanychZDnia;
    private Cursor mProdukt;
    private Produkt produkt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zapisywanie_produktu_layout);

        mBtnDodajProdukt = (Button)findViewById(R.id.btnDodajProdukt);
        mMiaraProduktu = (EditText)findViewById(R.id.miaraProduktu);
        mNazwaProduktu = (TextView)findViewById(R.id.nazwaProduktu);
        mWagaProduktu = (TextView)findViewById(R.id.wagaProduktu);
        mBazaDanychProduktow = new BazaDanychProduktow(this);
        mBazaDanychZDnia = new BazaDanychZDnia(this);
        produkt = new Produkt();

        Intent intent = getIntent();
        mWybranaNazwa = intent.getStringExtra("Nazwa");
        mWybranaData = intent.getStringExtra("Data");

        mProdukt = mBazaDanychProduktow.uzyskajKonkretnyProdukt(mWybranaNazwa);
        while(mProdukt.moveToNext()) {
            produkt.setID(mProdukt.getInt(0));
            produkt.setNazwaProduktu(mProdukt.getString(1));
            produkt.setKalorycznosc(mProdukt.getInt(2));
            produkt.setBialko(mProdukt.getInt(3));
            produkt.setWeglowodany(mProdukt.getInt(4));
            produkt.setTluszcze(mProdukt.getInt(5));
            produkt.setWagaProduktu(mProdukt.getInt(6));
        }
        mNazwaProduktu.setText(produkt.getNazwaProduktu());
        mWagaProduktu.setText(produkt.getWagaProduktu() + "g");
        mMiaraProduktu.setText(produkt.getWagaProduktu() + "");

        mBtnDodajProdukt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMiara1 = mMiaraProduktu.getText().toString();
                mMiara = Integer.parseInt(mMiara1);
                if(!mMiara1.equals("")){
                    produkt.setWagaProduktu(mMiara);
                    mBazaDanychZDnia.dodajProduktDoBazy(produkt, mWybranaData);
                    Intent intent = new Intent(ZapisywanieProduktuActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    wyswietlWiadomosc("Musisz wprowadzic miarę");
                }
            }
        });

    }

    private void wyswietlWiadomosc(String wiadomosc) {
        Toast.makeText(this, wiadomosc, Toast.LENGTH_SHORT).show();
    }

}
