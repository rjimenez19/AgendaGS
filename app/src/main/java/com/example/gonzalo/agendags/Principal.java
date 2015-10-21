package com.example.gonzalo.agendags;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.ContextMenu;
import java.util.ArrayList;
import java.util.Collections;


public class Principal extends AppCompatActivity {

    public ArrayList<Contacto> contactos;
    public Adaptador adap;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        iniciar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void iniciar(){

        GestionAg ga= new GestionAg(this);
        contactos = ga.getGestion();

        for(Contacto aux:contactos){
            aux.setTelefonos((ArrayList<String>) GestionAg.getListaTelefonos(this,aux.getId()));
        }

        Collections.sort(contactos);

        for(int z =0; z<contactos.size(); z++ ){
            contactos.get(z).setId(z);
        }

        ListView lv= (ListView)findViewById(R.id.lvMostrar);
        registerForContextMenu(lv);
        adap= new Adaptador(this, R.layout.listact, contactos);

        lv.setAdapter(adap);
        lv.setTag(contactos);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menucontextual, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo vistaInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int posicion=vistaInfo.position;

        switch(item.getItemId()){
            case R.id.menu_editar:
                editar(posicion);
                adap.notifyDataSetChanged();
                return true;

            case R.id.menu_borrar:
                Toast.makeText(Principal.this, "El contacto "+contactos.get(posicion).getId() +"ha sido eliminado", Toast.LENGTH_SHORT).show();
                contactos.remove(posicion);
                adap.notifyDataSetChanged();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void editar(final int posicion){

        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle("Editar");
        LayoutInflater inflater= LayoutInflater.from(this);

        final View vista = inflater.inflate(R.layout.editar, null);

        String ap=""+posicion;
        final int p =Integer.parseInt(ap);

        EditText et1, et2, et3, et4;
        et1 = (EditText) vista.findViewById(R.id.etNombre);
        et2 = (EditText) vista.findViewById(R.id.editText2);
        et3 = (EditText) vista.findViewById(R.id.editText3);
        et4 = (EditText) vista.findViewById(R.id.editText4);
        et1.setText(contactos.get(p).getNombre());

        if(contactos.get(p).size()==1) {
            et2.setText(contactos.get(p).getTelefono(0));
        }else if(contactos.get(p).size()==2){
            et2.setText(contactos.get(p).getTelefono(0));
            et3.setText(contactos.get(p).getTelefono(1));
        }else if(contactos.get(p).size()==3){
            et2.setText(contactos.get(p).getTelefono(0));
            et3.setText(contactos.get(p).getTelefono(1));
            et4.setText(contactos.get(p).getTelefono(2));
        }else{
            et2.setText("");
            et3.setText("");
            et4.setText("");
        }

        alert.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        EditText et1, et2, et3, et4;
                        et1 = (EditText) vista.findViewById(R.id.etNombre);
                        et2 = (EditText) vista.findViewById(R.id.editText2);
                        et3 = (EditText) vista.findViewById(R.id.editText3);
                        et4 = (EditText) vista.findViewById(R.id.editText4);

                        contactos.get(p).setNombre(et1.getText().toString());
                        ArrayList<String> telf = new ArrayList<>();
                        telf.add(et2.getText().toString());
                        telf.add(et3.getText().toString());
                        telf.add(et4.getText().toString());

                        contactos.get(p).setTelefonos(telf);
                        adap.notifyDataSetChanged();

                        Collections.sort(contactos, new OrdenarLista());
                        adap = new Adaptador(Principal.this, R.layout.listact, contactos);
                        ListView lv = (ListView) findViewById(R.id.lvMostrar);
                        lv.setAdapter(adap);
                    }
                });
        alert.setView(vista);
        alert.setNegativeButton("cerrar", null);
        alert.show();
    }

    public void nuevoCont(View v){
    AlertDialog.Builder alert= new AlertDialog.Builder(this);
    alert.setTitle("Añadir");
    LayoutInflater inflater= LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.editar, null);

    alert.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int whichButton) {

            int id = contactos.size()-1;

            EditText et1, et2, et3, et4;

            et1 = (EditText) vista.findViewById(R.id.etNombre);
            et2 = (EditText) vista.findViewById(R.id.editText2);
            et3 = (EditText) vista.findViewById(R.id.editText3);
            et4 = (EditText) vista.findViewById(R.id.editText4);


            String nombre= et1.getText().toString();
            ArrayList<String> telf = new ArrayList<>();
            telf.add(et2.getText().toString());
            telf.add(et3.getText().toString());
            telf.add(et4.getText().toString());

            Contacto c = new Contacto(id,nombre, telf );

            contactos.add(c);

            adap.notifyDataSetChanged();

            Collections.sort(contactos, new OrdenarLista());
            adap = new Adaptador(Principal.this, R.layout.listact, contactos);
            ListView lv = (ListView) findViewById(R.id.lvMostrar);
            lv.setAdapter(adap);
        }
    });
    alert.setView(vista);
    alert.setNegativeButton("cerrar", null);
    alert.show();
}

}
