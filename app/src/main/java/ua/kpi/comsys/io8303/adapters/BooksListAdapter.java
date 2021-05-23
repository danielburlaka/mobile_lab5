package ua.kpi.comsys.io8303.adapters;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.lab5.R;
import ua.kpi.comsys.io8303.model.BookItem;

import java.util.ArrayList;
import java.util.List;

public class BooksListAdapter extends ArrayAdapter<String> {
    private final String TAG = "Adapter";
    private final Fragment context;
    private final ArrayList<BookItem> books;

    public BooksListAdapter(Fragment context, ArrayList<BookItem> books, List<String> textViewResourceId) {

        super(context.getContext(), R.layout.display_book_item, textViewResourceId);

        this.context=context;
        this.books = books;
        Log.i(TAG, "Size: " + String.valueOf(textViewResourceId.size()));
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View rowView=inflater.inflate(
                R.layout.display_book_item, null,true);

        ImageView image = (ImageView) rowView.findViewById(R.id.poster);
        ImageView deleteButton = (ImageView) rowView.findViewById(R.id.deleteButton);
        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        TextView yearText = (TextView) rowView.findViewById(R.id.year);
        TextView typeText = (TextView) rowView.findViewById(R.id.type);


        int drawableResourceId = this.getContext().getResources().getIdentifier(
                books.get(position).getImage().toLowerCase().replace(".jpg", ""),
                "drawable", this.getContext().getPackageName());
        if (drawableResourceId == 0)
            image.setImageResource(R.drawable.no_poster);
        else
            image.setImageResource(drawableResourceId);

        titleText.setText(books.get(position).getTitle());
        yearText.setText(books.get(position).getSubtitle());
        typeText.setText(books.get(position).getPrice());
        Log.i(TAG, String.valueOf(position));

        Bundle bundle = new Bundle();
        bundle.putString("delete", books.get(position).getTitle());

        deleteButton.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position);
            }
        });

        return rowView;
    };

    private void deleteItem(int position) {

        books.remove(position);
        notifyDataSetChanged();
    }

}


