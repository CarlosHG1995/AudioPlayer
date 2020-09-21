package com.master.audioplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.master.audioplayer.Adapter.ViewPagerAdaptador;
import com.master.audioplayer.Fragments.AlbumFragment;
import com.master.audioplayer.Fragments.CancionesFragment;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static final int REQUEST_CODE = 1;
    public static ArrayList<MusicFiles> musicFilesArrayList;
    public static boolean  shuffle_bool = false, repear_bool= false;
    public static ArrayList<MusicFiles>albums = new ArrayList <>();
    private String MY_SORT_PREF = "SortOrder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Permisos();
    }

    private void iniciarComponentes(){
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tableLayout = findViewById(R.id.tab_layout);
        ViewPagerAdaptador viewPagerAdapter = new ViewPagerAdaptador(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new CancionesFragment(),"Canciones");
        viewPagerAdapter.addFragment(new AlbumFragment(),"Álbumes");
        viewPager.setAdapter(viewPagerAdapter);
        tableLayout.setupWithViewPager(viewPager);
    }

    private void Permisos(){
        //pedir permisos necesarios
        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
           !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    ,REQUEST_CODE);
        }
        else
        {
            musicFilesArrayList = getAllAudio(this);
            iniciarComponentes();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode ,@NonNull String[] permissions ,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode ,permissions ,grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //permiso garantizado por el usuario
                 musicFilesArrayList = getAllAudio(this);
                iniciarComponentes();
            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}
                ,REQUEST_CODE);
            }
        }
    }



    public ArrayList<MusicFiles> getAllAudio(Context context){

      SharedPreferences preferences = getSharedPreferences(MY_SORT_PREF,MODE_PRIVATE);
      String sortOrder = preferences.getString("sorting","sortByName");
      ArrayList<String> duplicado = new ArrayList <>();
      ArrayList<MusicFiles> listaAudios = new ArrayList <>();
      albums.clear();
      String order = "";
      Uri uri =MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
      switch(sortOrder){
          case "sortByName":
              order = MediaStore.MediaColumns.DISPLAY_NAME + " ASC";
              Log.d("TAG","order " +order);
              break;
          case "sortByDate":
              order = MediaStore.MediaColumns.DATE_ADDED + " ASC";
              break;
          case "sortBySize":
              order = MediaStore.MediaColumns.SIZE + " DESC";
              break;
      }
      String[] projection = {MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,// PATH
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media._ID

      };
        //el query requiere api mayor a 16
      Cursor cursordb = context.getContentResolver().query(uri,projection,null,null,order);
      if(cursordb != null){
            while (cursordb.moveToNext()){
                String album = cursordb.getString(0);
                String titulo = cursordb.getString(1);
                String duracion = cursordb.getString(2);
                String path = cursordb.getString(3);
                String artista = cursordb.getString(4);
                String id = cursordb.getString(5);
                MusicFiles musicFiles = new MusicFiles(path,titulo,artista,album,duracion,id);
                Log.e("TAG","path: " +path+ " album : "+ album);
                listaAudios.add(musicFiles);
                if(!duplicado.contains(album)){
                    albums.add(musicFiles);
                    duplicado.add(album);
                }
            }
            cursordb.close();
      }

      return listaAudios;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem= menu.findItem(R.id.search);
        SearchView searchView =(SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String userInput = s.toLowerCase();
        ArrayList<MusicFiles> myfiles = new ArrayList <>();
        for(MusicFiles song: musicFilesArrayList){
            if(song.getTitulo().toLowerCase().contains(userInput)){
                myfiles.add(song);
            }
        }
        CancionesFragment.musicAdapter.updateList(myfiles);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences.Editor editor= getSharedPreferences(MY_SORT_PREF,MODE_PRIVATE).edit();
        switch(item.getItemId()){
            case R.id.by_name:
                editor.putString("sorting","sortByName");
                editor.apply();
                this.recreate();
                break;
            case  R.id.by_date:
                editor.putString("sorting","sortByDate");
                editor.apply();
                this.recreate();
                break;
            case R.id.by_size:
                editor.putString("sorting","sortBySize");
                editor.apply();
                this.recreate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
