package com.master.audioplayer.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.master.audioplayer.Adapter.AlbumAdapter;
import com.master.audioplayer.R;

import static com.master.audioplayer.MainActivity.albums;
import static com.master.audioplayer.MainActivity.musicFilesArrayList;


public class AlbumFragment extends Fragment {

    RecyclerView recyclerView;
    AlbumAdapter albumAdapter;
    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater ,ViewGroup container ,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album ,container ,false);
        recyclerView = view.findViewById(R.id.recyclerAlbum);
        recyclerView.setHasFixedSize(true);
        if(!(albums.size() <1))
        {
            albumAdapter= new AlbumAdapter(getContext(),albums);
            recyclerView.setAdapter(albumAdapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
