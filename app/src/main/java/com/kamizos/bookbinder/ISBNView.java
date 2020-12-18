package com.kamizos.bookbinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ISBNView extends RecyclerView.Adapter<ISBNView.ViewHolder> {

    private ArrayList<ISBN> iData = null;
    DBHelper dbHelper;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageButton imgbtn;

        ViewHolder(View isbnView){
            super(isbnView);

            textView = isbnView.findViewById(R.id.isbninsert);
            imgbtn = isbnView.findViewById(R.id.isbndeletebutton);

            imgbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        dbHelper.idelete(iData.get(pos).getISBN());
                        iData.remove(pos);
                        ISBNView.super.notifyDataSetChanged();
                    }
                }
            });

        }
    }

    ISBNView(ArrayList<ISBN> list) {
        iData = list;
    }

    public void setiData(ArrayList<ISBN> iData) {
        this.iData = iData;
    }

    @Override
    public ISBNView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.isbn_view, parent, false);
        ISBNView.ViewHolder vh = new ISBNView.ViewHolder(view);

        dbHelper = DBHelper.getInstance(parent.getContext().getApplicationContext());

        return vh;
    }


    @Override
    public void onBindViewHolder(ISBNView.ViewHolder holder, int position) {
        String text = iData.get(position).getISBN();
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return iData.size();
    }
}
