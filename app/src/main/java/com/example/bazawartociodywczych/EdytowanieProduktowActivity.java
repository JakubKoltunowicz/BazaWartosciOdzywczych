package com.example.bazawartociodywczych;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EdytowanieProduktowActivity extends AppCompatActivity {

    private Button mBtnZapisz, mBtnUsun;
    private EditText mNowaNazwa;
    private int mWybraneID;
    private BazaDanychZDnia mBazaDanychZDnia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edytowanie_produktow_layout);

        mBtnZapisz = (Button)findViewById(R.id.btnZapisz);
        mBtnUsun = (Button)findViewById(R.id.btnUsun);
        mNowaNazwa = (EditText)findViewById(R.id.nowaNazwa);
        mBazaDanychZDnia = new BazaDanychZDnia(this);

        Intent intent = getIntent();

        mWybraneID = intent.getIntExtra("ID", -1);

        mBtnZapisz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = mNowaNazwa.getText().toString();
                if(!item.equals("")){
                    mBazaDanychZDnia.zaaktualizujNazwe(item, mWybraneID);
                }
                else {
                    wyswietlWiadomosc("Musisz wprowadzic nazwe");
                }
            }
        });

        mBtnUsun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBazaDanychZDnia.usunProdukt(mWybraneID);
                mNowaNazwa.setText("");
                wyswietlWiadomosc("Usunieto z listy");
            }
        });
    }

    private void wyswietlWiadomosc(String wiadomosc) {
        Toast.makeText(this, wiadomosc, Toast.LENGTH_SHORT).show();
    }
}
