package com.master.audioplayer;

public class MusicFiles {

    private String path;
    private String titulo;
    private String artista;
    private String album;
    private String duracion;
    private String id;

    public MusicFiles(String path ,String titulo ,String artista ,String album ,String duracion ,String id) {
        this.path=path;
        this.titulo=titulo;
        this.artista=artista;
        this.album=album;
        this.duracion=duracion;
        this.id=id;
    }

    public MusicFiles() { }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path=path;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo=titulo;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista=artista;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album=album;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion=duracion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id=id;
    }
}
