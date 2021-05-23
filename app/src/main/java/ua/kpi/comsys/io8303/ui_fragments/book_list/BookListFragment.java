package ua.kpi.comsys.io8303.ui_fragments.book_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lab5.R;
import ua.kpi.comsys.io8303.activities.AddBookActivity;
import ua.kpi.comsys.io8303.activities.DisplayBookActivity;
import ua.kpi.comsys.io8303.adapters.BooksListAdapter;
import ua.kpi.comsys.io8303.model.BookItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BookListFragment extends Fragment {
    private String TAG = "MovieList";
    private static final String BOOK = "book";
    private static final String RESULT = "result";
    ArrayList<BookItem> books = new ArrayList<>();
    ArrayList<BookItem> searchedList = new ArrayList<>();
    ListView list;
    BooksListAdapter adapter;
    ArrayList<String> textViewResourceId = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_books_list, container, false);

        String fileName = "MoviesList.json";

        TextView nothingFound = root.findViewById(R.id.nothingFound);
        nothingFound.setVisibility(View.INVISIBLE);

        Gson gson = new Gson();
        Type listOfMoviesItemsType = new TypeToken<ArrayList<BookItem>>() {}.getType();

        try {
            books = gson.fromJson(ReadTextFile(fileName), listOfMoviesItemsType);
            Log.i(TAG, ReadTextFile(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        searchedList = books;
        updateResourseId(searchedList);

        list = root.findViewById(R.id.MoviesListView);
        adapter = new BooksListAdapter(this, searchedList, textViewResourceId);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = list.getItemAtPosition(position);
                openDisplayBookActivity(id);
            }
        });

        SearchView searchView = root.findViewById(R.id.searchView2);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchedList =  filter(newText);
                if (newText.equals(""))
                    searchedList = books;
                if (searchedList.size() > 0)
                    nothingFound.setVisibility(View.INVISIBLE);
                else
                    nothingFound.setVisibility(View.VISIBLE);
                Log.i(TAG, String.valueOf(searchedList.size()));
                refresh();
                return false;
            }
        });

        Button addItemButton = (Button) root.findViewById(R.id.addItem);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    openAddBookActivity();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    public void refresh() {
        updateResourseId(searchedList);
        adapter = new BooksListAdapter(this, searchedList, textViewResourceId);
        list.setAdapter(adapter);
    }

    public void openDisplayBookActivity(long id) {
        Intent intent = new Intent(this.getActivity(), DisplayBookActivity.class);
        intent.putExtra(BOOK, books.get((int) (id)).getIsbn13());
        startActivity(intent);
    }

    public String ReadTextFile(String name) throws IOException {
        StringBuilder string = new StringBuilder();
        String line = "";
        try {
        InputStream is = getContext().getAssets().open(name);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while (true) {
            try {
                if ((line = reader.readLine()) == null) break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            string.append(line);
        }
        is.close();

        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        return string.toString();
    }

    public ArrayList<BookItem> filter(String searchText) {
        ArrayList<BookItem> newList = new ArrayList<>();

        for (BookItem book : books) {
            if (book.getTitle().contains(searchText) ||
                book.getSubtitle().contains(searchText) ||
                book.getPrice().contains(searchText))
                newList.add(book);
        }

        return newList;
    }

    private void updateResourseId (ArrayList<BookItem> list) {
        textViewResourceId.clear();
        for (BookItem book: list) {
            textViewResourceId.add(book.getTitle());
        }
    }

    public void openAddBookActivity() throws IOException {
        Intent intent = new Intent(this.getActivity(), AddBookActivity.class);
        intent.putExtra(BOOK, moviesToString());
        startActivityForResult(intent, 1);
    }

    private void updateJSON(String newData) {
        Gson gson = new Gson();
        Type listOfMoviesItemsType = new TypeToken<ArrayList<BookItem>>() {}.getType();
        Log.i(TAG, moviesToString());
        books = gson.fromJson(newData, listOfMoviesItemsType);
        searchedList = books;
        refresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String returnValue = data.getStringExtra(RESULT);
                updateJSON(returnValue);
            }
        }
    }

    private String moviesToString() {
        StringBuilder result = new StringBuilder("[ ");
        for (int i = 0; i < books.size(); i++) {
            if (i < books.size() - 1) {
                result.append(books.get(i).toString());
                result.append(", ");
            }
            else result.append(books.get(i).toString());

        }
        return result.append(" ]").toString();
    }

    private void deleteItem (String movieTitle) {

    }
}