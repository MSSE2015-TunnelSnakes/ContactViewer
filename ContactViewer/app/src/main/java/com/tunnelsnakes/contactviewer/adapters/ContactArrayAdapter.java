package com.tunnelsnakes.contactviewer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import com.tunnelsnakes.contactviewer.R;

import com.tunnelsnakes.contactviewer.models.Contact;

public class ContactArrayAdapter extends ArrayAdapter<Contact> {

        private ArrayList<Contact> contacts;

        public ContactArrayAdapter(Context context, int textViewResourceId, ArrayList<Contact> contacts) {
            super(context, textViewResourceId, contacts);

            this.contacts = contacts;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.contacts_item, parent, false);

            Contact currentContact = contacts.get(position);
            if (currentContact != null) {
                TextView firstNameTextView = (TextView) rowView.findViewById(R.id.first_name_textview);
                firstNameTextView.setText(currentContact.getFirstName());
                TextView lastNameTextView = (TextView) rowView.findViewById(R.id.last_name_textview);
                lastNameTextView.setText(currentContact.getLastName());
                TextView titleTextView = (TextView) rowView.findViewById(R.id.title_textview);
                titleTextView.setText(currentContact.getTitle());
                TextView phoneTextView = (TextView) rowView.findViewById(R.id.phone_textview);
                phoneTextView.setText(currentContact.getPhone());
            }

            return rowView;
        }
}
