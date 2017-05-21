package com.example.gnp.smartwallet;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.net.Uri;

import java.util.ArrayList;

import java.util.List;

public class PermissionsPage extends Fragment {

    private final int zoom = 16;
    LocationManager lm;
    Location location;
    double longitude;
    double latitude;
    private FragmentManager fm;

    private ListView list;
    public static List<ActivityInfo> log;
    private ArrayAdapter<ActivityInfo> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2permissions, container, false);

        lm = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

        //Verifica se o software tem acesso aos servicos de localizacao

        list = (ListView) rootView.findViewById(R.id.lista);
        log =  new ArrayList<ActivityInfo>();

        log.add(new ActivityInfo("Ana", (long)(26916297 * 55555), -20.4487689, 10.78663509));
        log.add(new ActivityInfo("Marion",  (long)(26916297 * 55555), -20.4487689, 10.78663509));
        log.add(new ActivityInfo("Fernando",  (long)(26916297 * 55555), -20.4487689, 10.78663509));

        for (int i =0; i < 10; i++) {
            log.add(new ActivityInfo("Maria", (long)(26916297 * 55555), -20.4487689, 10.78663509));

        }

        //Coloca os valores no Array
        adapter = new ArrayAdapter<ActivityInfo>(getActivity(),
                android.R.layout.simple_list_item_1, log);

        list.setAdapter(adapter);

        return rootView;
    }

    //Adicionar um novo pacote de informacao no log do app
    public void addNewLog(String name)
    {
        log.add(0, new ActivityInfo(name));
        //Tenta pegar a ultima localizacao conhecida
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            // Se conseguiu pegar uma localizacao
            if(location != null){
                longitude = location.getLongitude();
                latitude = location.getLatitude();

                /*String url = "http://maps.google.com/maps/@" + latitude + "," + longitude + "," + zoom + 'z';
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);*/

                log.add(new ActivityInfo(name, (long)26916297 * 55555, latitude, longitude));

            }
        }

    }

    
}
