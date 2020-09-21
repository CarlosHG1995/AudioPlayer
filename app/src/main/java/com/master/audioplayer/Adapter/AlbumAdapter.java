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
import com.master.audioplayer.R;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyHolder> {

    private Context mcontext;
    private ArrayList<MusicFiles> albumFiles;
    View view;

    public AlbumAdapter(Context mcontext ,ArrayList <MusicFiles> albumFiles) {
        this.mcontext=mcontext;
        this.albumFiles=albumFiles;
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        ImageView album_image;
        TextView album_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            album_image = itemView.findViewById(R.id.album_img);
            album_name = itemView.findViewById(R.id.album_name);
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup ,int i) {
        view =LayoutInflater.from(mcontext).inflate(R.layout.album_item,viewGroup,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.MyHolder myViewHolder ,final int i) {
        myViewHolder.album_name.setText(albumFiles.get(i).getAlbum());
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
                Intent intent = new Intent(mcontext,AlbumDetails.class);
                intent.putExtra("albumName",albumFiles.get(i).getAlbum());
                Log.d("TAG","album name clic " +i);
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
