package com.tunnelsnakes.contactviewer;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.tunnelsnakes.contactviewer.R;
import com.tunnelsnakes.contactviewer.adapters.ContactArrayAdapter;
import com.tunnelsnakes.contactviewer.database.SQLiteHelper;
import com.tunnelsnakes.contactviewer.models.Contact;

import java.util.ArrayList;

public class ContactsListActivity extends ListActivity {
    private static final String TAG = "ContactsListActivity";
    public static final String CONTACT_ID_EXTRA = "contact_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_list);

        // Handle Clicks
        final ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int contactId = ((Contact) lv.getItemAtPosition(position)).getId();
                startIndividualContactActivity(contactId);
            }
        });

        // Handle New Contact Button Click
        Button newContactBtn = (Button) findViewById(R.id.add_contact_button);
        newContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIndividualContactActivity();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Load the List View
        SQLiteHelper sqlHelper = new SQLiteHelper(this);
        ArrayList<Contact> contacts = sqlHelper.getContacts();
        ContactArrayAdapter adapter = new ContactArrayAdapter(this, R.layout.contacts_item, contacts);
        setListAdapter(adapter);

    }

    // Used to create a new contact
    private void startIndividualContactActivity() {
        Intent intent = new Intent(this, IndividualContactActivity.class);
        startActivity(intent);
    }

    // Used to open an existing contact
    private void startIndividualContactActivity(int contactId) {
        Intent intent = new Intent(this, IndividualContactActivity.class);
        intent.putExtra(CONTACT_ID_EXTRA, contactId);
        startActivity(intent);

    }
}
