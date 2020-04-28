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
    int mResource;

    public ProduktAdapter(Context context, int resource, ArrayList<Produkt> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String NazwaProduktu = getItem(position).getNazwaProduktu();
        int Kalorycznosc = getItem(position).getKalorycznosc();
        int Bialko = getItem(position).getBialko();
        int Weglowodany = getItem(position).getWeglowodany();
        int Tluszcze = getItem(position).getTluszcze();
        int WagaProduktu = getItem(position).getWagaProduktu();

        Produkt produkt = new Produkt(NazwaProduktu, Kalorycznosc, Bialko, Weglowodany, Tluszcze, WagaProduktu);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvNazwaProduktu = (TextView)convertView.findViewById((R.id.widok1));
        TextView tvWagaProduktu = (TextView)convertView.findViewById((R.id.widok2));
        TextView tvKalorycznosc = (TextView)convertView.findViewById((R.id.widok3));
        TextView tvBialko = (TextView)convertView.findViewById((R.id.widok4));
        TextView tvWeglowodany = (TextView)convertView.findViewById((R.id.widok5));
        TextView tvTluszcze = (TextView)convertView.findViewById((R.id.widok6));

        tvNazwaProduktu.setText(NazwaProduktu);
        tvWagaProduktu.setText("(" + WagaProduktu + "g)");
        tvKalorycznosc.setText(Kalorycznosc + " kcal   ");
        tvBialko.setText(Bialko + " B   ");
        tvWeglowodany.setText(Weglowodany + " W   ");
        tvTluszcze.setText(Tluszcze + " T   ");

        return convertView;
    }
}
