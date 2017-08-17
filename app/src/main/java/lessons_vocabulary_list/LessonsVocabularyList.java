package lessons_vocabulary_list;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import database_vocabulary.DatabaseColumnNames;
import database_vocabulary.VocabularyDatabase;
import favourite_list.FavouriteList;
import pl.flanelowapopijava.duel_with_english.R;

public class LessonsVocabularyList extends AppCompatActivity {

    private final String PREFERENCES_NAME = "applicationPreferences";
    private final String PREFERENCES_DATABASE_INITDATA = "dataIsInit";

    private LessonsVocabularyListAdapter adapter;
    private VocabularyDatabase database;
    private Cursor cursor;
    private SharedPreferences preferences;
    private ListView LVVocabularyLessons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons_vocabulary_list);

        LVVocabularyLessons = (ListView) findViewById(R.id.lessonsVocabularyListView);
        Intent intent = getIntent();
        int i = intent.getIntExtra("LIST_CHOICE_GROUP", 200);
        int i1 = intent.getIntExtra("LIST_CHOICE_ITEM", 200);

        preferences = getSharedPreferences(PREFERENCES_NAME, Activity.MODE_PRIVATE);

        database = new VocabularyDatabase(getApplicationContext());
        if (!(preferences.getString(PREFERENCES_DATABASE_INITDATA, "").equals("INITED"))) {
            SharedPreferences.Editor preferencesEditor = preferences.edit();
            String initDataOk = "INITED";
            preferencesEditor.putString(PREFERENCES_DATABASE_INITDATA, initDataOk);
            preferencesEditor.apply();
        }
        cursor = database.getSpecificValues(DatabaseColumnNames.TABLE_NAME_A1);
        adapter = new LessonsVocabularyListAdapter(this, cursor, i, i1, LVVocabularyLessons);
        LVVocabularyLessons.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        cursor.close();
        database.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vocabulary_lessons_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.addToFavouriteWordsStar){
            String tag = LVVocabularyLessons.getTag().toString();
            if(tag.equals("0")){
                LVVocabularyLessons.setTag(1);
            } else {
                LVVocabularyLessons.setTag(0);
            }
            adapter.notifyDataSetChanged();
        }
        if(id == R.id.goToFavouriteFromMenu){
            Intent intent = new Intent(this, FavouriteList.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
