package com.master.audioplayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;

import static com.master.audioplayer.Adapter.AlbumDetailsAdapter.albumFiles;
import static com.master.audioplayer.Adapter.MusicAdapter.mFiles;
import static com.master.audioplayer.MainActivity.musicFilesArrayList;
import static com.master.audioplayer.MainActivity.repear_bool;
import static com.master.audioplayer.MainActivity.shuffle_bool;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener
{

    TextView nombreCancion, nombreArtista, duracionCancion,duracionTotal;
    ImageView caratula, nextBtn, prevBtn, shuffleBtn ,repeatBtn;
    FloatingActionButton playPauseBtn;
    SeekBar seekBar;
    int posicion = -1;
    static ArrayList<MusicFiles> listaCanciones= new ArrayList <>();
    static Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread, prevThread, nextThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        iniciarVista();
        getIntentMetodo();
        nombreCancion.setText(listaCanciones.get(posicion).getTitulo());
        nombreArtista.setText(listaCanciones.get(posicion).getArtista());
        mediaPlayer.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar ,int progress ,boolean fromUser) {
                if(mediaPlayer !=null && fromUser){
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(mCurrentPosition);
                    duracionCancion.setText(formattedTime(mCurrentPosition));
                }
                handler.postDelayed(this,1000);
            }
        });
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shuffle_bool){
                    shuffle_bool = false;
                    shuffleBtn.setImageResource(R.drawable.ic_shuffle);
                }else {
                    shuffle_bool= true;
                    shuffleBtn.setImageResource(R.drawable.ic_shuffle);
                }
            }
        });

        repeatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repear_bool){
                    repear_bool=false;
                    repeatBtn.setImageResource(R.drawable.ic_repeat);
                }else {
                    repear_bool=true;
                    repeatBtn.setImageResource(R.drawable.ic_repeat);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        playThreadbtn();
        nextThreadbtn();
        prevThreadbtn();
        super.onResume();
    }

    private void prevThreadbtn() {
        prevThread = new Thread(){
            @Override public void run() {
                super.run();
                prevBtn.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        prevBtnCLick();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void prevBtnCLick() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffle_bool && !repear_bool){
                posicion = getRandom(listaCanciones.size()-1);//aleatorio
            }
            else if(!shuffle_bool && repear_bool){
                posicion =((posicion-1)<0 ? (listaCanciones.size()-1):(posicion-1));
            }
            uri = Uri.parse(listaCanciones.get(posicion).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            nombreCancion.setText(listaCanciones.get(posicion).getTitulo());
            nombreArtista.setText(listaCanciones.get(posicion).getArtista());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }else {
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffle_bool && !repear_bool){
                posicion = getRandom(listaCanciones.size()-1);//aleatorio
            }
            else if(!shuffle_bool && repear_bool){
                posicion =((posicion-1)<0 ? (listaCanciones.size()-1):(posicion-1));
            }
            uri = Uri.parse(listaCanciones.get(posicion).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            nombreCancion.setText(listaCanciones.get(posicion).getTitulo());
            nombreArtista.setText(listaCanciones.get(posicion).getArtista());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.ic_play);
        }
    }

    private void nextThreadbtn() {
        nextThread = new Thread(){
            @Override public void run() {
                super.run();
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                       nextBtnCLick();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextBtnCLick() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffle_bool && !repear_bool){
                posicion = getRandom(listaCanciones.size()-1);//aleatorio
            }
            else if(!shuffle_bool && repear_bool){
                posicion= ((posicion+1)%listaCanciones.size());
            }
            uri = Uri.parse(listaCanciones.get(posicion).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            nombreCancion.setText(listaCanciones.get(posicion).getTitulo());
            nombreArtista.setText(listaCanciones.get(posicion).getArtista());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            playPauseBtn.setBackgroundResource(R.drawable.ic_pause);
            mediaPlayer.start();
        }else {
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shuffle_bool && !repear_bool){
                posicion = getRandom(listaCanciones.size()-1);//aleatorio
            }
            else if(!shuffle_bool && repear_bool){
                posicion= ((posicion+1)%listaCanciones.size());
            }
            uri = Uri.parse(listaCanciones.get(posicion).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            nombreCancion.setText(listaCanciones.get(posicion).getTitulo());
            nombreArtista.setText(listaCanciones.get(posicion).getArtista());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playPauseBtn.setBackgroundResource(R.drawable.ic_play);
        }

    }

    private int getRandom(int size) {
        Random random = new Random();
        Log.d("TAG","random " + random.nextInt(size+1));
        return random.nextInt(size+1);
    }

    private void playThreadbtn() {
        playThread = new Thread(){
            @Override public void run() {
                super.run();
                playPauseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        playPauseBtnCLick();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playPauseBtnCLick() {
        if(mediaPlayer.isPlaying()){
            playPauseBtn.setImageResource(R.drawable.ic_play);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                     }
                    handler.postDelayed(this,1000);
                }
            });
        }else {
            playPauseBtn.setImageResource(R.drawable.ic_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mCurrentPosition);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
    }

    private String formattedTime(int mCurPos){
        String totalout="";
        String totalnew="";
        String second= String.valueOf(mCurPos%60);
        String minute= String.valueOf(mCurPos/60);
        totalout = minute +":"+ second;
        totalnew = minute +":"+ "0"+ second;
        if(second.length()==1){
            return totalnew;
        }else {
           return totalout;
        }

    }

    private void getIntentMetodo() {
    posicion = getIntent().getIntExtra("posicion",-1);
    String sender = getIntent().getStringExtra("sender");
    if(sender != null && sender.equals("albumDetails")){
        listaCanciones = albumFiles;
    }else {
        listaCanciones = mFiles;
    }

    Log.d("TAG:","posicion playerAct " +posicion);

    if(listaCanciones !=null){
        playPauseBtn.setImageResource(R.drawable.ic_pause);
        uri = Uri.parse(listaCanciones.get(posicion).getPath());
    }
    if(mediaPlayer != null){
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
    }
    else {
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
    }
    seekBar.setMax(mediaPlayer.getDuration()/1000);
    metaData(uri);

    }

    private void iniciarVista(){
        nombreCancion = findViewById(R.id.txt_song);
        nombreArtista = findViewById(R.id.txt_artist);
        duracionCancion = findViewById(R.id.duracion);
        duracionTotal = findViewById(R.id.duracion_total);
        caratula = findViewById(R.id.cover);
        nextBtn = findViewById(R.id.id_next);
        prevBtn = findViewById(R.id.id_prev);
        shuffleBtn = findViewById(R.id.id_shufle);
        repeatBtn = findViewById(R.id.id_repeat);
        playPauseBtn = findViewById(R.id.play_pause);
        seekBar = findViewById(R.id.seekBAr) ;
    }

    private void metaData(Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int duracion_Total = Integer.parseInt(listaCanciones.get(posicion).getDuracion())/1000;
        duracionTotal.setText(formattedTime(duracion_Total));
        byte[] art = retriever.getEmbeddedPicture();
        Bitmap bitmap;
        if(art != null){

            Log.d("TAG","art byt" +art.length);
            /**/
            bitmap =BitmapFactory.decodeByteArray(art,0,art.length);
            ImageAnimation(this,caratula,bitmap);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if(swatch != null){
                        ImageView gradientes = findViewById(R.id.image_gradiente);
                        RelativeLayout contenedor = findViewById(R.id.mContainer);
                        gradientes.setBackgroundResource(R.drawable.gradiente_bg);
                        contenedor.setBackgroundResource(R.drawable.main_bg);
                        GradientDrawable gradientDrawableBG = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                                                                   new int[]{swatch.getRgb(), 0x00000000});
                        gradientes.setBackground(gradientDrawableBG);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                                                                 new int[]{swatch.getRgb(), 0x00000000});
                        gradientes.setBackground(gradientDrawable);
                        nombreCancion.setTextColor(swatch.getTitleTextColor());
                        nombreArtista.setTextColor(swatch.getBodyTextColor());
                    }
                    else {
                        ImageView gradientes = findViewById(R.id.image_gradiente);
                        RelativeLayout contenedor = findViewById(R.id.mContainer);
                        gradientes.setBackgroundResource(R.drawable.gradiente_bg);
                        contenedor.setBackgroundResource(R.drawable.main_bg);
                        GradientDrawable gradientDrawableBG = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                                                                   new int[]{0xff0000, 0x00000000});
                        gradientes.setBackground(gradientDrawableBG);
                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                                                                 new int[]{0xff0000, 0x00000000});
                        gradientes.setBackground(gradientDrawable);
                        nombreCancion.setTextColor(Color.WHITE);
                        nombreArtista.setTextColor(Color.DKGRAY);

                    }
                }
            });
        }
        else {
            Glide.with(this).asBitmap().load(R.drawable.play).into(caratula);
            ImageView gradientes = findViewById(R.id.image_gradiente);
            RelativeLayout contenedor = findViewById(R.id.mContainer);
            gradientes.setBackgroundResource(R.drawable.gradiente_bg);
            contenedor.setBackgroundResource(R.drawable.main_bg);
            nombreCancion.setTextColor(Color.WHITE);
            nombreArtista.setTextColor(Color.DKGRAY);
        }
    }

    public void ImageAnimation(final Context context,final ImageView imageView,final Bitmap bitmap){
        Animation aniOut =AnimationUtils.loadAnimation(context,android.R.anim.fade_out);
        final Animation aniIn = AnimationUtils.loadAnimation(context,android.R.anim.fade_in);
        aniOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                aniIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(aniIn);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(aniOut);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextBtnCLick();
        if(mediaPlayer !=null){
            mediaPlayer= MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }

    }
}
