package com.tunnelsnakes.contactviewer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.tunnelsnakes.contactviewer.database.SQLiteHelper;
import com.tunnelsnakes.contactviewer.models.Contact;

public class IndividualContactActivity extends Activity {
    boolean isEditing = false;
    Contact currentContact = null;

    EditText firstNameET;
    EditText lastNameET;
    EditText titleET;
    EditText phoneET;
    EditText emailET;
    EditText twitterET;
    LinearLayout newEditButtonsLL;
    Button saveBtn;
    Button discardBtn;
    LinearLayout currentContactButtonsLL;
    Button editBtn;
    Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_contact);

        // Load all items
        firstNameET = (EditText) findViewById(R.id.first_name_edit_text);
        lastNameET = (EditText) findViewById(R.id.last_name_edit_text);
        titleET = (EditText) findViewById(R.id.title_edit_text);
        phoneET = (EditText) findViewById(R.id.phone_edit_text);
        emailET = (EditText) findViewById(R.id.email_edit_text);
        twitterET = (EditText) findViewById(R.id.twitter_edit_text);
        newEditButtonsLL = (LinearLayout) findViewById(R.id.new_edit_buttons);
        saveBtn = (Button) findViewById(R.id.save_button);
        discardBtn = (Button) findViewById(R.id.discard_button);
        currentContactButtonsLL = (LinearLayout) findViewById(R.id.current_contact_buttons);
        editBtn = (Button) findViewById(R.id.edit_button);
        deleteBtn = (Button) findViewById(R.id.delete_button);

        int contactId = getIntent().getIntExtra(ContactsListActivity.CONTACT_ID_EXTRA, -1);
        if (contactId == -1) { // New contact
            isEditing = true;
        } else { // Existing Contact
            SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
            currentContact = sqLiteHelper.getContact(contactId);
            updateContactUI();
        }

        updateEditability();

        // Handle Button Clicks
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact();
            }
        });

        discardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(IndividualContactActivity.this)
                        .setTitle("Discard Changes")
                        .setMessage("Are you sure you want to discard changes?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                discardChanges();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editContact();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(IndividualContactActivity.this)
                        .setTitle("Delete Contact")
                        .setMessage("Are you sure you want to delete this contact?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteContact();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private void updateContactUI() {
        if (currentContact != null) {
            firstNameET.setText(currentContact.getFirstName());
            lastNameET.setText(currentContact.getLastName());
            titleET.setText(currentContact.getTitle());
            phoneET.setText(currentContact.getPhone());
            emailET.setText(currentContact.getEmail());
            twitterET.setText(currentContact.getTwitterHandle());
        }
    }

    private void updateEditability() {
        // Setup for editing or displaying
        if (isEditing) {
            newEditButtonsLL.setVisibility(View.VISIBLE);
            currentContactButtonsLL.setVisibility(View.GONE);
        } else {
            newEditButtonsLL.setVisibility(View.GONE);
            currentContactButtonsLL.setVisibility(View.VISIBLE);
        }
        // Setting EditText to not focusable or clickable means it cannot be edited.
        firstNameET.setFocusable(isEditing);
        firstNameET.setFocusableInTouchMode(isEditing);
        lastNameET.setFocusable(isEditing);
        lastNameET.setFocusableInTouchMode(isEditing);
        titleET.setFocusable(isEditing);
        titleET.setFocusableInTouchMode(isEditing);
        phoneET.setFocusable(isEditing);
        phoneET.setFocusableInTouchMode(isEditing);
        emailET.setFocusable(isEditing);
        emailET.setFocusableInTouchMode(isEditing);
        twitterET.setFocusable(isEditing);
        twitterET.setFocusableInTouchMode(isEditing);
    }

    private void saveContact() {
        // TODO - Add contact validation
        if (currentContact == null) {
            currentContact = new Contact();
        }
        currentContact.setFirstName(firstNameET.getText().toString());
        currentContact.setLastName(lastNameET.getText().toString());
        currentContact.setTitle(titleET.getText().toString());
        currentContact.setPhone(phoneET.getText().toString());
        currentContact.setEmail(emailET.getText().toString());
        currentContact.setTwitterHandle(twitterET.getText().toString());

        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        sqLiteHelper.addUpdateContact(currentContact);

        isEditing = false;
        updateEditability();
    }

    private void discardChanges() {
        if(currentContact == null) {
            // return to previous screen.
            finishActivity(0);
        } else {
            updateContactUI(); // Reloads contact UI
            isEditing = false;
            updateEditability();
        }
    }

    private void editContact() {
        isEditing = true;
        updateEditability();
    }

    private void deleteContact() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this);
        sqLiteHelper.deleteContact(currentContact.getId());
        finishActivity(0);
    }
}
