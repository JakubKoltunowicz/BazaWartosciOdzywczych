package com.example.bazawartociodywczych;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final int KOD_POZWOLEN = 1;

    private DatePickerDialog mOknoWyboruDaty;
    private BazaDanychZDnia mBazaDanychZDnia;
    private Button mDataPrzycisk, mDodaj, mSpozyteProdukty;
    private ListView mWidokListy;
    private String mObecnaData;
    private static String mWybranaData;
    private List<String> mlistaNazwProduktow;
    private ArrayList<Produkt> mlistaProduktow;
    private Cursor mProdukt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        przyznanieDostepu();

        mDataPrzycisk = (Button)findViewById(R.id.btnDataPrzycisk);
        mDodaj = (Button)findViewById(R.id.btnDodaj);
        mSpozyteProdukty = (Button)findViewById(R.id.btnSpozyteProdukty);
        mWidokListy = (ListView)findViewById(R.id.widokListy);
        mBazaDanychZDnia = new BazaDanychZDnia(this);

        mObecnaData = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        if(mWybranaData==null) {
            mWybranaData = mObecnaData;
        }
        mDataPrzycisk.setText("Data: " + mWybranaData);

        mDataPrzycisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pokazOknoWyboruDaty();
            }
        });

        mDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DodawanieProduktowActivity.class);
                startActivity(intent);
            }
        });

        mSpozyteProdukty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WyszukiwanieProduktowActivity.class);
                intent.putExtra("Data", mWybranaData);
                startActivity(intent);
            }
        });

        wyswietlWidokListy();
    }

    void pokazOknoWyboruDaty() {
        mOknoWyboruDaty = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );
        mOknoWyboruDaty.show();
    }

    private void wyswietlWidokListy() {
        mProdukt = mBazaDanychZDnia.uzyskajProduktZBazy(mWybranaData);
        mlistaNazwProduktow = new ArrayList<String>();
        mlistaProduktow = new ArrayList();
        while(mProdukt.moveToNext()) {
            Produkt produkt = new Produkt();
            produkt.setNazwaProduktu(mProdukt.getString(1));
            mlistaNazwProduktow.add(mProdukt.getString(1));
            produkt.setKalorycznosc(mProdukt.getInt(2));
            produkt.setBialko(mProdukt.getInt(3));
            produkt.setWeglowodany(mProdukt.getInt(4));
            produkt.setTluszcze(mProdukt.getInt(5));
            produkt.setWagaProduktu(mProdukt.getInt(6));
            mlistaProduktow.add(produkt);
        }

        ProduktAdapter adapter = new ProduktAdapter(this, R.layout.adapter_layout, mlistaProduktow);
        mWidokListy.setAdapter(adapter);

        mWidokListy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                String nazwa = (String)mlistaNazwProduktow.get(i);
                Cursor produkt = mBazaDanychZDnia.uzyskajIDProduktu(nazwa);
                int ID = -1;
                while(produkt.moveToNext()) {
                    ID = produkt.getInt(0);
                }
                if(ID > -1) {
                    Intent intent = new Intent(MainActivity.this, EdytowanieProduktowActivity.class);
                    intent.putExtra("ID", ID);
                    startActivity(intent);
                }
                else {
                    wyswietlWiadomosc("Brak ID powiazanego z ta nazwa");
                }
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month + 1;
        String mDzien = "" + dayOfMonth;
        String mMiesiac = "" + month;
        if(Integer.parseInt(mDzien)<10) {
            mDzien = "0" + mDzien;
        }
        if(Integer.parseInt(mMiesiac)<10) {
            mMiesiac  = "0" + mMiesiac;
        }
        String mData = mDzien + "." + mMiesiac + "." + year;
        mWybranaData = mData;
        mDataPrzycisk.setText("Data: " + mWybranaData);

        recreate();
    }

    public void przyznanieDostepu() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) +
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, KOD_POZWOLEN);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == KOD_POZWOLEN) {
            if(grantResults.length > 0 && grantResults[0] + grantResults[1] + grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Przyznano dostep", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Odmowiono dostepu", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void wyswietlWiadomosc(String wiadomosc) {
        Toast.makeText(this, wiadomosc, Toast.LENGTH_SHORT).show();
    }
}
