package com.master.audioplayer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.master.audioplayer.AlbumDetails;
import com.master.audioplayer.MusicFiles;
import com.master.audioplayer.PlayerActivity;
import com.master.audioplayer.R;

import java.util.ArrayList;

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.MyHolder> {

    private Context mcontext;
    public static ArrayList <MusicFiles> albumFiles;
    View view;

    public AlbumDetailsAdapter(Context mcontext ,ArrayList <MusicFiles> albumFiles) {
        this.mcontext=mcontext;
        this.albumFiles=albumFiles;
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        ImageView album_image;
        TextView album_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            album_image = itemView.findViewById(R.id.music_img);
            album_name = itemView.findViewById(R.id.music_nombre);
        }
    }

    @NonNull
    @Override
    public AlbumDetailsAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup ,int i) {
        view =LayoutInflater.from(mcontext).inflate(R.layout.music_items,viewGroup,false);
        return new AlbumDetailsAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumDetailsAdapter.MyHolder myViewHolder ,final int i) {
        myViewHolder.album_name.setText(albumFiles.get(i).getTitulo());
        byte[] image = getAlbumArt(albumFiles.get(i).getPath());
        if(image != null){
            Glide.with(mcontext).asBitmap()
                    .load(image)
                    .into(myViewHolder.album_image);
        }
        else {Glide.with(mcontext).asBitmap()
                .load(R.drawable.icono_foreground)
                .into(myViewHolder.album_image);
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,PlayerActivity.class);
                intent.putExtra("sender","albumDetails");
                intent.putExtra("posicion",i);
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
