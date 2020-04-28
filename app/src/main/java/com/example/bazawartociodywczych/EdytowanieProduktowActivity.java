package com.example.bazawartociodywczych;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EdytowanieProduktowActivity extends AppCompatActivity {

    private Button mBtnZapiszZmiany, mBtnUsunProdukt;
    private EditText mMiaraProduktu;
    private int mWybraneID, mMiara, mWybranaMiara;
    private BazaDanychZDnia mBazaDanychZDnia;
    private String mMiara1, mWybranaNazwa;
    private TextView mNazwaProduktu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edytowanie_produktow_layout);

        mBtnZapiszZmiany = (Button)findViewById(R.id.btnZapiszZmiany);
        mBtnUsunProdukt = (Button)findViewById(R.id.btnUsunProdukt);
        mMiaraProduktu = (EditText)findViewById(R.id.miaraProduktu);
        mNazwaProduktu = (TextView)findViewById(R.id.nazwaProduktu);
        mBazaDanychZDnia = new BazaDanychZDnia(this);

        Intent intent = getIntent();

        mWybraneID = intent.getIntExtra("ID", -1);
        mWybranaNazwa = intent.getStringExtra("Nazwa");
        mWybranaMiara = intent.getIntExtra("Miara", -1);
        mNazwaProduktu.setText(mWybranaNazwa);
        mMiaraProduktu.setText("" + mWybranaMiara);

        mBtnZapiszZmiany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMiara1 = mMiaraProduktu.getText().toString();
                mMiara = Integer.parseInt(mMiara1);
                if(!mMiara1.equals("")){
                    mBazaDanychZDnia.zaaktualizujMiare(mMiara, mWybraneID);
                    wyswietlWiadomosc("Wprowadzono zmiany");
                }
                else {
                    wyswietlWiadomosc("Musisz wprowadzic miarę");
                }
            }
        });

        mBtnUsunProdukt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBazaDanychZDnia.usunProdukt(mWybraneID);
                Intent intent = new Intent(EdytowanieProduktowActivity.this, MainActivity.class);
                startActivity(intent);
                wyswietlWiadomosc("Usunieto z listy");
            }
        });
    }

    private void wyswietlWiadomosc(String wiadomosc) {
        Toast.makeText(this, wiadomosc, Toast.LENGTH_SHORT).show();
    }
}
