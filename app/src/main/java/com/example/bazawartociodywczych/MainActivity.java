package com.example.bazawartociodywczych;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button mDataPrzycisk, mSpozyteProdukty;
    private ListView mWidokListy;
    private String mObecnaData;
    private TextView mBialko, mWeglowowany, mTluszcze, mKCAL;
    private int sumaBialka, sumaWeglowodanow, sumaTluszczy, sumaKCAL;
    private List<Integer> mlistaID;
    private ArrayList<Produkt> mlistaProduktow;
    private Cursor mProdukt;
    private static String mWybranaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        przyznanieDostepu();

        mBialko = (TextView)findViewById(R.id.bialko);
        mWeglowowany = (TextView)findViewById(R.id.weglowodany);
        mTluszcze = (TextView)findViewById(R.id.tluszcze);
        mKCAL = (TextView)findViewById(R.id.kcal);
        mDataPrzycisk = (Button)findViewById(R.id.btnDataPrzycisk);
        mSpozyteProdukty = (Button)findViewById(R.id.btnSpozyteProdukty);
        mWidokListy = (ListView)findViewById(R.id.widokListy);
        mBazaDanychZDnia = new BazaDanychZDnia(this);
        sumaBialka = 0;
        sumaWeglowodanow = 0;
        sumaTluszczy = 0;
        sumaKCAL = 0;

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

        mSpozyteProdukty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, WyszukiwanieProduktowActivity.class);
                intent1.putExtra("Data", mWybranaData);
                startActivity(intent1);
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
        mlistaID = new ArrayList<Integer>();
        mlistaProduktow = new ArrayList();
        while(mProdukt.moveToNext()) {
            int a, b, c, d, e;
            Produkt produkt = new Produkt();
            produkt.setID(mProdukt.getInt(0));
            mlistaID.add(mProdukt.getInt(0));
            produkt.setNazwaProduktu(mProdukt.getString(1));
            a=mProdukt.getInt(2);
            produkt.setKalorycznosc(a);
            b=mProdukt.getInt(3);
            produkt.setBialko(b);
            c=mProdukt.getInt(4);
            produkt.setWeglowodany(c);
            d=mProdukt.getInt(5);
            produkt.setTluszcze(d);
            e=mProdukt.getInt(6);
            produkt.setWagaProduktu(e);
            mlistaProduktow.add(produkt);
            sumaBialka = sumaBialka + ((b * e)/100);
            sumaWeglowodanow = sumaWeglowodanow + ((c * e)/100);
            sumaTluszczy = sumaTluszczy + ((d * e)/100);
            sumaKCAL = sumaKCAL + ((a * e)/100);
        }

        ProduktAdapter adapter = new ProduktAdapter(this, R.layout.adapter_layout, mlistaProduktow);
        mWidokListy.setAdapter(adapter);

        mBialko.setText(sumaBialka + " g  ");
        mTluszcze.setText(sumaTluszczy + " g  ");
        mWeglowowany.setText(sumaWeglowodanow + " g  ");
        mKCAL.setText(sumaKCAL + " kcal  ");

        mWidokListy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                int ID = (int)mlistaID.get(i);
                Cursor produkt = mBazaDanychZDnia.uzyskajNazweMiareProduktu(ID);
                String nazwa = null;
                int miara = 0;
                while(produkt.moveToNext()) {
                    nazwa = produkt.getString(0);
                    miara = produkt.getInt(1);
                }
                if(nazwa != null) {
                    Intent intent2 = new Intent(MainActivity.this, EdytowanieProduktowActivity.class);
                    intent2.putExtra("ID", ID);
                    intent2.putExtra("Nazwa", nazwa);
                    intent2.putExtra("Miara", miara);
                    startActivity(intent2);
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
