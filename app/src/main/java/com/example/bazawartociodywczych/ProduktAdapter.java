package com.example.bazawartociodywczych;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProduktAdapter extends ArrayAdapter<Produkt> {

    private Context mContext;
    private String mNazwaProduktu;
    private int mResource;
    private int mKalorycznosc;
    private int mBialko;
    private int mWeglowodany;
    private int mTluszcze;
    private int mWagaProduktu;
    private TextView mTvNazwaProduktu;
    private TextView mTvWagaProduktu;
    private TextView mTvKalorycznosc;
    private TextView mTvBialko;
    private TextView mTvWeglowodany;
    private TextView mTvTluszcze;

    public ProduktAdapter(Context context, int resource, ArrayList<Produkt> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mNazwaProduktu = getItem(position).getNazwaProduktu();
        mKalorycznosc = getItem(position).getKalorycznosc();
        mBialko = getItem(position).getBialko();
        mWeglowodany = getItem(position).getWeglowodany();
        mTluszcze = getItem(position).getTluszcze();
        mWagaProduktu = getItem(position).getWagaProduktu();

        Produkt produkt = new Produkt(mNazwaProduktu, mKalorycznosc, mBialko, mWeglowodany, mTluszcze, mWagaProduktu);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        mTvNazwaProduktu = (TextView)convertView.findViewById((R.id.widok1));
        mTvWagaProduktu = (TextView)convertView.findViewById((R.id.widok2));
        mTvKalorycznosc = (TextView)convertView.findViewById((R.id.widok3));
        mTvBialko = (TextView)convertView.findViewById((R.id.widok4));
        mTvWeglowodany = (TextView)convertView.findViewById((R.id.widok5));
        mTvTluszcze = (TextView)convertView.findViewById((R.id.widok6));

        mTvNazwaProduktu.setText(mNazwaProduktu);
        mTvWagaProduktu.setText("(" + mWagaProduktu + "g)");
        mTvKalorycznosc.setText("Na 100g:   " + mKalorycznosc + " kcal   ");
        mTvBialko.setText(mBialko + " B   ");
        mTvWeglowodany.setText(mWeglowodany + " W   ");
        mTvTluszcze.setText(mTluszcze + " T   ");

        return convertView;
    }
}
