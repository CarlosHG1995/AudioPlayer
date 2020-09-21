package com.master.audioplayer.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.master.audioplayer.Adapter.MusicAdapter;
import com.master.audioplayer.R;

import static com.master.audioplayer.Adapter.MusicAdapter.mFiles;
import static com.master.audioplayer.MainActivity.musicFilesArrayList;


public class CancionesFragment extends Fragment {

    RecyclerView recyclerView;
    public static MusicAdapter musicAdapter;

    public CancionesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater ,ViewGroup container ,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_canciones ,container ,false);
        recyclerView = view.findViewById(R.id.RV_canciones);
        recyclerView.setHasFixedSize(true);
        if(!(musicFilesArrayList.size() <1))
        {
            musicAdapter = new MusicAdapter(getContext(),musicFilesArrayList) ;
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
