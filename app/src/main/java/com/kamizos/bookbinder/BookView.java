package com.kamizos.bookbinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.CollationElementIterator;
import java.util.ArrayList;

import static com.kamizos.bookbinder.MainActivity.tabs;

public class BookView extends RecyclerView.Adapter<BookView.ViewHolder> implements Filterable {

    Context context;
    private ArrayList<Book> unFilteredlist;
    private ArrayList<Book> filteredlist;
    DBHelper dbHelper;
    int selectedposition;


    public BookView(Context context, ArrayList<Book> list) {
        super();
        this.context = context;
        this.unFilteredlist = list;
        this.filteredlist = list;
    }

    public void setbData(ArrayList<Book> bData) {
        this.filteredlist = bData;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filteredlist = unFilteredlist;
                }else {
                    ArrayList<Book> filteringList = new ArrayList<Book>();
                    for (Book name : unFilteredlist) {
                        if (name.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteringList.add(name);
                        }
                    }
                    filteredlist = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredlist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredlist = (ArrayList<Book>) results.values;
                BookView.super.notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView textView;
        ImageButton imgbtn;
        AlertDialog.Builder dialog;


        ViewHolder(View itemView){
            super(itemView);

            textView = itemView.findViewById(R.id.booktitle);
            textView.setSelected(true);
            imgbtn = itemView.findViewById(R.id.bookdeletebutton);
            imgbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    dialog = new AlertDialog.Builder(v.getContext());
                    final int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

                        dialog.setTitle("삭제 하시겠습니까?");
                        dialog.setMessage(filteredlist.get(pos).getTitle());
                        dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str = filteredlist.get(pos).getISBN();
                                dbHelper.delete(str);
                                for (int i = 0; i < filteredlist.size(); i++) {
                                    if (filteredlist.get(i).getISBN().equals(str)) {
                                        filteredlist.remove(i);
                                    }
                                }
                                for (int i = 0; i < unFilteredlist.size(); i++) {
                                    if (unFilteredlist.get(i).getISBN().equals(str)) {
                                        unFilteredlist.remove(i);
                                    }
                                }

                                BookView.super.notifyDataSetChanged();
                                Toast.makeText(v.getContext(),"삭제 완료",Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(v.getContext(),"삭제 취소",Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.create().show();
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedposition = getAdapterPosition();

                    if (selectedposition == RecyclerView.NO_POSITION) {
                        return;
                    }else {
                        Book temp = new Book();
                        temp = filteredlist.get(selectedposition);
                        Fragment2.setPage(temp);
                        Toast.makeText(context, temp.getTitle().toString() + " 열람", Toast.LENGTH_LONG ).show();
                        tabs.selectTab(tabs.getTabAt(1));
                    }
                }
            });
        }
    }


    @Override
    public BookView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.book_view, parent, false);
        BookView.ViewHolder vh = new BookView.ViewHolder(view);

        dbHelper = DBHelper.getInstance(parent.getContext().getApplicationContext());

        return vh;
    }

    @Override
    public void onBindViewHolder(BookView.ViewHolder holder, int position) {
        String text = filteredlist.get(position).getTitle();
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return filteredlist.size();
    }
}
