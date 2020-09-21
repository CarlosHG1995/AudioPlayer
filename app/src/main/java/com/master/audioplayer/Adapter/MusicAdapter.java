package com.master.audioplayer.Adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.master.audioplayer.MusicFiles;
import com.master.audioplayer.PlayerActivity;
import com.master.audioplayer.R;

import java.io.File;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder> {

    private Context mContext;
    public static ArrayList<MusicFiles> mFiles;


    public MusicAdapter(Context mcontext, ArrayList<MusicFiles> musicFiles){
        this.mFiles= musicFiles;
        this.mContext= mcontext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup ,int i) {
        View view =LayoutInflater.from(mContext).inflate(R.layout.music_items,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder ,final int i) {
        myViewHolder.nombre.setText(mFiles.get(i).getTitulo());
        byte[] image = getAlbumArt(mFiles.get(i).getPath());
        if(image != null){
            Glide.with(mContext).asBitmap()
                 .load(image)
                 .into(myViewHolder.album_img);
        }
        else {Glide.with(mContext).asBitmap()
                .load(R.drawable.icono_foreground)
                .into(myViewHolder.album_img);
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent = new Intent(mContext,PlayerActivity.class);

                int posicion=i;
                intent.putExtra("posicion",posicion);
                Log.d("TAG","posicion cancion " +i);
                mContext.startActivity(intent);
            }
        });
        myViewHolder.more_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()){
                            case  R.id.delete:
                                deleteFile(i,v);
                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }

    private void deleteFile(int posicion, View view){
        Uri contentUri =ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        Long.parseLong(mFiles.get(posicion).getId()));

        File file = new File(mFiles.get(posicion).getPath());
        boolean eliminado = file.delete();
        if(eliminado){
            mContext.getContentResolver().delete(contentUri,null,null);
            mFiles.remove(posicion);
            notifyItemRemoved(posicion);
            notifyItemRangeChanged(posicion,mFiles.size());
        }
        else {
            Snackbar.make(view,"No se puede eliminar", Snackbar.LENGTH_LONG).show();
        }

    }
    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nombre;
        ImageView album_img, more_menu;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.music_nombre);
            album_img = itemView.findViewById(R.id.music_img);
            more_menu = itemView.findViewById(R.id.more);
        }
    }

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    public void updateList(ArrayList<MusicFiles> musicFilesArrayList){
        mFiles = new ArrayList <>();
        mFiles.addAll(musicFilesArrayList);
        notifyDataSetChanged();
    }
}
