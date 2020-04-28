package com.example.bazawartociodywczych;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WyszukiwanieProduktowActivity extends AppCompatActivity {

    private static final int KOD_APARATU = 2;

    private BazaDanychProduktow mBazaDanychProduktow;
    private EditText mWyszukiwarka;
    private Button mZrobZdjecie;
    private ListView mWidokListy;
    private String mWybranaData;
    private Cursor mProdukt;
    private ArrayList<String> mlistaProduktow;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wyszuiwanie_produktow_layout);

        Intent intent = getIntent();
        mWybranaData = intent.getStringExtra("Data");

        mZrobZdjecie = (Button)findViewById(R.id.btnZdjeciePrzycisk);
        mWidokListy = (ListView)findViewById(R.id.widokListy);
        mWyszukiwarka = (EditText)findViewById(R.id.wyszukiwarka);
        mBazaDanychProduktow = new BazaDanychProduktow(this);

        mZrobZdjecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zrobZdjecie(v);
                produktZSerwera();
            }
        });
        wyswietlWidokListy();
    }

    private void wyswietlWidokListy() {
        mProdukt = mBazaDanychProduktow.uzyskajProduktZBazy();
        mlistaProduktow = new ArrayList();
        while(mProdukt.moveToNext()) {
            mlistaProduktow.add(mProdukt.getString(1));
        }

        adapter = new ArrayAdapter(this, R.layout.adapter2_layout, mlistaProduktow);
        mWidokListy.setAdapter(adapter);

        mWyszukiwarka.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (WyszukiwanieProduktowActivity.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mWidokListy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                String nazwa = adapterView.getItemAtPosition(i).toString();
                Cursor produkt = mBazaDanychProduktow.uzyskajIDProduktu(nazwa);
                int ID = -1;
                while(produkt.moveToNext()) {
                    ID = produkt.getInt(0);
                }
                if(ID > -1) {
                    Intent intent = new Intent(WyszukiwanieProduktowActivity.this, ZapisywanieProduktuActivity.class);
                    intent.putExtra("Nazwa", nazwa);
                    intent.putExtra("Data", mWybranaData);
                    startActivity(intent);
                }
                else {
                    wyswietlWiadomosc("Brak ID powiazanego z ta nazwa");
                }
            }
        });
    }

    public void zrobZdjecie(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File katalog = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File zdjecie = new File(katalog, "zdjecie.jpg");

        Uri imageUri = FileProvider.getUriForFile(this, "com.example.android.fileprovider1", zdjecie);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, KOD_APARATU);
    }

    public void wyswietlWiadomosc(String wiadomosc) {
        Toast.makeText(this, wiadomosc, Toast.LENGTH_SHORT).show();
    }

    public void produktZSerwera()
    {
        Produkt mProdukt = new Produkt();
        String nazwa = "Papryka";
        Cursor produkt = mBazaDanychProduktow.uzyskajIDProduktu(nazwa);
        int ID = -1;
        while(produkt.moveToNext()) {
            ID = produkt.getInt(0);
        }
        if(ID == -1) {
            mProdukt.setNazwaProduktu("Papryka");
            mProdukt.setKalorycznosc(10);
            mProdukt.setBialko(20);
            mProdukt.setWeglowodany(30);
            mProdukt.setTluszcze(40);
            mProdukt.setWagaProduktu(100);
            mBazaDanychProduktow.dodajProduktDoBazy(mProdukt);
            Intent intent = new Intent(WyszukiwanieProduktowActivity.this, ZapisywanieProduktuActivity.class);
            intent.putExtra("Nazwa", nazwa);
            intent.putExtra("Data", mWybranaData);
            startActivity(intent);
        }
    }
}
