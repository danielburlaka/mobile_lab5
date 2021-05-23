package ua.kpi.comsys.io8303.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lab5.R;

public class AddBookActivity extends Activity {
    private static final String fileName = "MoviesList.json";
    private static final String BOOK = "book";
    private static final String RESULT = "result";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        Intent intent = getIntent();
        String filetext = intent.getStringExtra(BOOK);
        TextView title = (TextView) findViewById(R.id.titleText);
        TextView type = (TextView) findViewById(R.id.typeText);
        TextView year = (TextView) findViewById(R.id.yearText);

        Button button = findViewById(R.id.addMovieButton);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newMovie = "";
                newMovie = filetext.replace("]", "");
                newMovie += ", {\"Title\":\"" + title.getText().toString() + "\"," +
                        "\"Year\":\"" + year.getText().toString() + "\"," +
                        "\"imdbID\":\"\"," +
                        "\"Type\":\"" + type.getText().toString() + "\"," +
                        "\"Poster\":\"\"} ]";

                Intent resultIntent = new Intent();
                resultIntent.putExtra(RESULT, newMovie);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}