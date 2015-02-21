package tunnlesnakes.com.contactviewer;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import tunnlesnakes.com.contactviewer.database.SQLiteHelper;
import tunnlesnakes.com.contactviewer.models.Contact;


public class Contacts extends ActionBarActivity {

    private static final String DB_NAME = "Contacts.db";

    private TextView contactsTextView;
    private ListView contactsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contactsTextView = (TextView) findViewById(R.id.contacts_text_view);
        contactsListView = (ListView) findViewById(R.id.contacts_list_view);

        SQLiteHelper sqlHelper = new SQLiteHelper(this, DB_NAME);
        ArrayList<Contact> contacts = sqlHelper.getContacts();

        ArrayAdapter<Contact> adapter = new ArrayAdapter<Contact>(this, R.layout.contacts_item, contacts);

       contactsListView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
