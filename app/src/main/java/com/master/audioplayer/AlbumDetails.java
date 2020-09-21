package com.master.audioplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.master.audioplayer.Adapter.AlbumDetailsAdapter;

import java.util.ArrayList;

import static com.master.audioplayer.MainActivity.musicFilesArrayList;

public class AlbumDetails extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView albumPhto;
    String albumname;
    ArrayList<MusicFiles> albumSong = new ArrayList <>();
    AlbumDetailsAdapter albumDetailsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details);
        recyclerView = findViewById(R.id.RValbumDetail);
        albumPhto = findViewById(R.id.albumPhoto);
        albumname = getIntent().getStringExtra("albumName");
        ListaCancionesAlbum();
    }

    private void ListaCancionesAlbum() {
        int j =0;
        for (int i=0; i<musicFilesArrayList.size(); i++){
            if(albumname.equals(musicFilesArrayList.get(i).getAlbum())){
              albumSong.add(j,musicFilesArrayList.get(i));
              j++;
            }
        }
        byte[] image = getAlbumArt(albumSong.get(0).getPath());
        if(image !=null){
            Glide.with(this).load(image).into(albumPhto);
        }else {
            Glide.with(this).load(R.drawable.icono_foreground).into(albumPhto);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!(albumSong.size()<1)){
            albumDetailsAdapter = new AlbumDetailsAdapter(this,albumSong);
            recyclerView.setAdapter(albumDetailsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        }
    }

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
