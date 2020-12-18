package com.kamizos.bookbinder;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import static com.kamizos.bookbinder.MainActivity.badapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment1 extends Fragment {

    RecyclerView recyclerView;
    EditText editText;


    public Fragment1() {
        /*

        */

        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editText = getActivity().findViewById(R.id.filteredittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                badapter.getFilter().filter(s);
                badapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recyclerView = getActivity().findViewById(R.id.bookrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(badapter);
        badapter.notifyDataSetChanged();

        //


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_1, container, false);

        // Inflate the layout for this fragment
        return view;
    }


}
