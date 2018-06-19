package com.byethost12.kitm.mobiliaplikacija.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.byethost12.kitm.mobiliaplikacija.Model.DatabaseSQLite;
import com.byethost12.kitm.mobiliaplikacija.Model.Klientas;
import com.byethost12.kitm.mobiliaplikacija.R;

import static com.byethost12.kitm.mobiliaplikacija.View.KlientasAdapter.ENTRY_ID;

public class EntryActivity extends AppCompatActivity {

    Button btnSubmit, btnUpdate, btnDelete;
    EditText name, telefonas, metai;
    EditText etName, etTelefonas, etMetai;
    RadioGroup rbGroup;
    //RadioButton rbSwarovski, rbKeramika, rbPerlas;
    CheckBox cbNaujas, cbLojalus, cbIprastas;
    Spinner spinner;
    ArrayAdapter<String> adapter;

    Klientas pradinisKlientas;
    Klientas galutinisKlientas;

    DatabaseSQLite db;

   // String items[] = {"Apyrankė", "Auskarai", "Vėrinys", "Rinkinys"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseSQLite(EntryActivity.this);

        int entryID = -1;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (!extras.isEmpty()) {
                entryID = extras.getInt(ENTRY_ID);
            }
        } else { // jeigu yra naujas irasas, id = -1, jeigu egzistuojantis, bus teigiamas
            entryID = (Integer) savedInstanceState.getSerializable(ENTRY_ID);
        }

        if (entryID == -1) {
            setTitle(R.string.new_entry_label);
        } else {
            setTitle(R.string.entry_update_label);
        }

        pradinisKlientas = new Klientas();
        if (entryID == -1) { //naujas irasas
            pradinisKlientas.setId(-1);
            pradinisKlientas.setName("");
            pradinisKlientas.setTelefonas(0);
            pradinisKlientas.setMetai(0);
            pradinisKlientas.setTipas("Naujas");
            //pradinisKlientas.setHeight(0);
            // pradinisKlientas.setWeight(0);
        } else { // egzistuojantis irasas
            pradinisKlientas = db.getKlientas(entryID);
        }
        galutinisKlientas = new Klientas();

        btnSubmit = (Button) findViewById(R.id.btnAdd);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        if (entryID == -1) { //naujas irasas - disable update button
            btnUpdate.setEnabled(false);
            btnSubmit.setEnabled(true);
            btnDelete.setEnabled(false);
        } else { // egzistuojantis irasas - disable submit
            btnUpdate.setEnabled(true);
            btnSubmit.setEnabled(false);
        }

        etName = (EditText) findViewById(R.id.etName);
        etTelefonas = (EditText) findViewById(R.id.etTelefonas);
        etMetai = (EditText) findViewById(R.id.etMetai);

        /*rbGroup = (RadioGroup) findViewById(R.id.rbGroup);
        rbSwarovski = (RadioButton) findViewById(R.id.rbSwarovski);
        rbKeramika = (RadioButton) findViewById(R.id.rbKeramika);
        rbPerlas = (RadioButton) findViewById(R.id.rbPerlas);
*/
        cbNaujas = (CheckBox) findViewById(R.id.cbNaujas);
        cbLojalus = (CheckBox) findViewById(R.id.cbLojalus);
        cbIprastas = (CheckBox) findViewById(R.id.cbIprastas);

        /*spinner = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
*/
        fillFields(pradinisKlientas);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getFields()){

                db.addKlientas(galutinisKlientas);

                Intent goToSearchActivity = new Intent(EntryActivity.this, SearchActivity.class);
                startActivity(goToSearchActivity);}
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFields()) {

                db.updateKlientas(galutinisKlientas);

                Intent goToSearchActivity = new Intent(EntryActivity.this, SearchActivity.class);
                startActivity(goToSearchActivity);}
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.deleteKlientas(pradinisKlientas.getId());

                Intent goToSearchActivity = new Intent(EntryActivity.this, SearchActivity.class);
                startActivity(goToSearchActivity);
            }
        });
    }

    private boolean getFields() {

        if (etName.getText().toString().isEmpty()) {
            Toast.makeText(EntryActivity.this, "Įvesk vardą ir pavardę!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!(cbNaujas.isChecked() || cbLojalus.isChecked() || cbIprastas.isChecked())) {
            Toast.makeText(EntryActivity.this, "Pasirinkite tipą!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etMetai.getText().toString().isEmpty()) {
            Toast.makeText(EntryActivity.this, "Įveskite metus!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etTelefonas.getText().toString().isEmpty()) {
            Toast.makeText(EntryActivity.this, "Telefono numerį!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
// TO DO: ismeta pranesima, kad nepasinkta, bet issaugoja su klaidomis
            String name = etName.getText().toString();
            double telefonas = Double.parseDouble(etTelefonas.getText().toString());
            double metai = Double.parseDouble(etMetai.getText().toString());
       /*     String rb = "";
            String spinnerText = "";

            if (rbSwarovski.isChecked()) {
                rb = rbSwarovski.getText().toString();
            } else if (rbKeramika.isChecked()) {
                rb = rbKeramika.getText().toString();
            } else {
                rb = rbPerlas.getText().toString();
            }
*/
            String checkboxText = "";

            if (cbNaujas.isChecked()) {
                checkboxText = checkboxText + "Naujas ";
            }

            if (cbLojalus.isChecked()) {
                checkboxText = checkboxText + "Lojalus ";
            }

            if (cbIprastas.isChecked()) {
                checkboxText = checkboxText + "Iprastas ";
            }

           // spinnerText = spinner.getSelectedItem().toString();

            galutinisKlientas.setId(pradinisKlientas.getId());
            galutinisKlientas.setName(name);
            //galutinisKlientas.setHeight(height);
            //galutinisKlientas.setWeight(weight);
            galutinisKlientas.setTelefonas(telefonas);
            galutinisKlientas.setMetai(metai);
            galutinisKlientas.setTipas(checkboxText);
        }
        return true;
    }

    private void fillFields(Klientas klientas) {
        etName.setText(klientas.getName());
        //etHeight.setText(String.valueOf(klientas.getHeight()));
        //etWeight.setText(String.valueOf(klientas.getWeight()));

        cbLojalus.setChecked(klientas.getTipas().contains("Lojalus"));
        cbNaujas.setChecked(klientas.getTipas().contains("Naujas"));
        cbIprastas.setChecked(klientas.getTipas().contains("Iprastas"));

       /* rbKeramika.setChecked(klientas.getMetai().equals("Keramika"));
        rbSwarovski.setChecked(klientas.getMetai().equals("Swarovski kristalas"));
        rbPerlas.setChecked(klientas.getMetai().equals("Perlas"));
*/
        spinner.setSelection(adapter.getPosition(klientas.getTipas()));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getFields();
                if (pradinisKlientas.equals(galutinisKlientas)) { //Nebuvo pakeistas
                    finish();
                } else {  //Buvo pakeistas
                    showDialog();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                EntryActivity.this);

        // set title
        alertDialogBuilder.setTitle("Įspėjimas");

        // set dialog message
        alertDialogBuilder
                .setMessage("Išsaugoti pakeitimus?")
                .setCancelable(false)
                .setPositiveButton("Taip", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                })
                .setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        EntryActivity.this.finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    }

