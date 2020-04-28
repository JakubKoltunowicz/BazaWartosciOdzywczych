package com.example.bazawartociodywczych;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DodawanieProduktowActivity extends AppCompatActivity {
    BazaDanychProduktow mBazaDanychProduktow;
    private Button mBtnDodaj;
    private EditText mTekst1, mTekst2, mTekst3, mTekst4, mTekst5, mTekst6;
    private String mWejscie1, mWejscie2, mWejscie3, mWejscie4, mWejscie5, mWejscie6;
    private int mWejscie21, mWejscie31, mWejscie41, mWejscie51, mWejscie61;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodawanie_produktow_layout);

        mTekst1 = (EditText)findViewById(R.id.Tekst1);
        mTekst2 = (EditText)findViewById(R.id.Tekst2);
        mTekst3 = (EditText)findViewById(R.id.Tekst3);
        mTekst4 = (EditText)findViewById(R.id.Tekst4);
        mTekst5 = (EditText)findViewById(R.id.Tekst5);
        mTekst6 = (EditText)findViewById(R.id.Tekst6);
        mBtnDodaj = (Button)findViewById(R.id.btnDodaj);;
        mBazaDanychProduktow = new BazaDanychProduktow(this);

        mBtnDodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Produkt produkt = new Produkt();
                mWejscie1 = mTekst1.getText().toString();
                mWejscie2 = mTekst2.getText().toString();
                mWejscie21 = Integer.parseInt(mWejscie2);
                mWejscie3 = mTekst3.getText().toString();
                mWejscie31 = Integer.parseInt(mWejscie3);
                mWejscie4 = mTekst4.getText().toString();
                mWejscie41 = Integer.parseInt(mWejscie4);
                mWejscie5 = mTekst5.getText().toString();
                mWejscie51 = Integer.parseInt(mWejscie5);
                mWejscie6 = mTekst6.getText().toString();
                mWejscie61 = Integer.parseInt(mWejscie6);
                if((mTekst1.length() != 0)&&(mTekst2.length() != 0)&&(mTekst3.length() != 0)&&(mTekst4.length() != 0)
                        &&(mTekst5.length() != 0)&&(mTekst6.length() != 0)) {
                    produkt.setNazwaProduktu(mWejscie1);
                    produkt.setKalorycznosc(mWejscie21);
                    produkt.setBialko(mWejscie31);
                    produkt.setWeglowodany(mWejscie41);
                    produkt.setTluszcze(mWejscie51);
                    produkt.setWagaProduktu(mWejscie61);
                    mBazaDanychProduktow.dodajProduktDoBazy(produkt);
                    mTekst1.setText("");
                    mTekst2.setText("");
                    mTekst3.setText("");
                    mTekst4.setText("");
                    mTekst5.setText("");
                    mTekst6.setText("");
                }
                else {
                    wyswietlWiadomosc("Musisz wpisac tekst");
                }
            }
        });
    }

    public void wyswietlWiadomosc(String wiadomosc) {
        Toast.makeText(this, wiadomosc, Toast.LENGTH_SHORT).show();
    }
}
