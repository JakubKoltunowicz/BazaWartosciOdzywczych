package com.example.bazawartociodywczych;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WyszukiwanieProduktowActivity extends AppCompatActivity {

    private static final int KOD_APARATU = 1;
    private static final int KOD_WYBORU = 2;

    private BazaDanychProduktow mBazaDanychProduktow;
    private EditText mWyszukiwarka;
    private Button mZrobZdjecie, mWybierzZdjecie, mDodajRecznie;
    private ListView mWidokListy;
    private String mNazwa1, mNazwa2, ID, mDane, mNazwaZdjecia, mOtrzymanaData;
    private Cursor mProdukt1, mProdukt2, mProdukt3;
    private Produkt mNowyProdukt;
    private ArrayList<String> mlistaProduktow;
    private ArrayAdapter mAdapter;
    private JSONObject mJSONProdukt;
    private AsyncHttpClient mClient1, mClient2;
    private RequestParams mParams1, mParams2;
    private File mKatalog, mZdjecie1, mZdjecie2;
    private Uri mUri;
    private static String mWybranaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wyszuiwanie_produktow_layout);

        Intent intent = getIntent();
        mOtrzymanaData = intent.getStringExtra("Data");
        if(mOtrzymanaData != null)
        {
            mWybranaData = mOtrzymanaData;
        }

        mZrobZdjecie = (Button)findViewById(R.id.btnZdjeciePrzycisk);
        mWybierzZdjecie = (Button)findViewById(R.id.btnWybierzZdjecie);
        mDodajRecznie = (Button)findViewById(R.id.btnDodajRecznie);
        mWidokListy = (ListView)findViewById(R.id.widokListy);
        mWyszukiwarka = (EditText)findViewById(R.id.wyszukiwarka);
        mBazaDanychProduktow = new BazaDanychProduktow(this);

        mZrobZdjecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zrobZdjecie();
            }
        });
        mWybierzZdjecie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wybierzZdjecie(v);
            }
        });
        mDodajRecznie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(WyszukiwanieProduktowActivity.this, ReczneDodawanieActivity.class);
                intent3.putExtra("Data", mWybranaData);
                startActivity(intent3);
            }
        });
        wyswietlWidokListy();
    }

    private void wyswietlWidokListy() {
        mProdukt1 = mBazaDanychProduktow.uzyskajProduktZBazy();
        mlistaProduktow = new ArrayList();
        while(mProdukt1.moveToNext()) {
            mlistaProduktow.add(mProdukt1.getString(1));
        }

        mAdapter = new ArrayAdapter(this, R.layout.adapter2_layout, mlistaProduktow);
        mWidokListy.setAdapter(mAdapter);

        mWyszukiwarka.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (WyszukiwanieProduktowActivity.this).mAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mWidokListy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                mNazwa1 = adapterView.getItemAtPosition(i).toString();
                mProdukt2 = mBazaDanychProduktow.uzyskajIDProduktu(mNazwa1);
                int ID1 = -1;
                while(mProdukt2.moveToNext()) {
                    ID1 = mProdukt2.getInt(0);
                }
                if(ID1 > -1) {
                    Intent intent = new Intent(WyszukiwanieProduktowActivity.this, ZapisywanieProduktuActivity.class);
                    intent.putExtra("Nazwa", mNazwa1);
                    intent.putExtra("Data", mWybranaData);
                    startActivity(intent);
                }
                else {
                    wyswietlWiadomosc("Brak ID powiazanego z ta nazwa");
                }
            }
        });
    }

    public void zrobZdjecie() {
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mKatalog = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        mNazwaZdjecia = new SimpleDateFormat("ss.mm.HH.dd.MM.yyyy", Locale.getDefault()).format(new Date());
        mZdjecie1 = new File(mKatalog, mNazwaZdjecia + ".jpg");

        mUri = FileProvider.getUriForFile(this, "com.example.android.bazawartociodywczych", mZdjecie1);
        intent1.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(intent1, KOD_APARATU);
    }

    public void wybierzZdjecie(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, KOD_WYBORU);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == KOD_APARATU) {
            super.onActivityResult(requestCode, resultCode, data);
            wyslijZdjecie(mZdjecie1);
        }
        if(requestCode == KOD_WYBORU) {
            String sciezka = uzyskajZdjecie(data.getData());
            mZdjecie2 = new File(sciezka);
            wyslijZdjecie(mZdjecie2);
        }
    }

    public String uzyskajZdjecie(Uri uri) {
        String[] projekcja = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projekcja, null, null, null);
        startManagingCursor(cursor);
        int columna = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columna);
    }

    public void wyslijZdjecie(File zdjecie) {
        ConnectivityManager connectManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
        mParams1 = new RequestParams();
        try {
            mParams1.put("file", zdjecie);
        }
        catch (FileNotFoundException e) {
        }
        if(zdjecie.exists()) {
            if(networkInfo != null) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI || networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    mClient1 = new AsyncHttpClient();
                    mClient1.post("http://192.168.0.116:8080/api/file", mParams1, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                            ID = (new String(responseBody)).toString();
                            if(Integer.parseInt(ID) != 0) {
                                odbierzProdukt(Integer.parseInt(ID));
                            }
                            else {
                                wyswietlWiadomosc("Brak Prouduktu w bazie");
                            }
                        }
                        @Override
                        public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                        }
                    });
                }
            }
            else {
                wyswietlWiadomosc("Brak dostępu do internetu, zdjęcie zapisane");
                finish();
            }
        }
    }

    public void odbierzProdukt(int ID) {
        mParams2 = new RequestParams();
        mParams2.put("ID", ID);
        mClient2 = new AsyncHttpClient();
        mClient2.get("http://192.168.0.116:8080/api/produkty", mParams2, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                mDane = (new String(responseBody)).toString();
                dodajProdukt(mDane);
            }
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    public void dodajProdukt(String mDane) {
        mNowyProdukt = new Produkt();
        try {
            mJSONProdukt = new JSONObject(mDane);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            mNowyProdukt.setNazwaProduktu(mJSONProdukt.getString("nazwaProduktu"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            mNowyProdukt.setKalorycznosc(mJSONProdukt.getInt("kalorycznosc"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            mNowyProdukt.setBialko(mJSONProdukt.getInt("bialko"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            mNowyProdukt.setWeglowodany(mJSONProdukt.getInt("weglowodany"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            mNowyProdukt.setTluszcze(mJSONProdukt.getInt("tluszcze"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            mNowyProdukt.setWagaProduktu(mJSONProdukt.getInt("wagaProduktu"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mNazwa2 = mNowyProdukt.getNazwaProduktu();
        mProdukt3 = mBazaDanychProduktow.uzyskajIDProduktu(mNazwa2);
        int ID1 = -1;
        while(mProdukt3.moveToNext()) {
            ID1 = mProdukt3.getInt(0);
        }
        if(ID1 == -1) {
            mBazaDanychProduktow.dodajProduktDoBazy(mNowyProdukt);
        }
        Intent intent2 = new Intent(WyszukiwanieProduktowActivity.this, ZapisywanieProduktuActivity.class);
        intent2.putExtra("Nazwa", mNazwa2);
        intent2.putExtra("Data", mWybranaData);
        startActivity(intent2);
    }

    public void wyswietlWiadomosc(String wiadomosc) {
        Toast.makeText(this, wiadomosc, Toast.LENGTH_SHORT).show();
    }
}
