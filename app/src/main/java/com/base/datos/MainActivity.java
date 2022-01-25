package com.base.datos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText etCodigo, etDescripcion, etPrecio;
    private Button btnRegistrar, btnBuscar, btnModificar, btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCodigo = (EditText)findViewById(R.id.etCodigo);
        etDescripcion = (EditText)findViewById(R.id.etDescripcion);
        etPrecio = (EditText)findViewById(R.id.etPrecio);
        btnRegistrar = (Button)findViewById(R.id.btnRegistrar);
        btnBuscar = (Button)findViewById(R.id.btnBuscar);
        btnModificar = (Button)findViewById(R.id.btnModificar);
        btnEliminar = (Button)findViewById(R.id.btnEliminar);

        //Dar de alta productos
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(MainActivity.this, "administracion", null, 1);
                //Abrir base de datos modo lectura y escritura
                SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

                String codigo = etCodigo.getText().toString();
                String descripcion = etDescripcion.getText().toString();
                String precio = etPrecio.getText().toString();

                //validar que todos los campos esten llenos y registro de datos
                if (!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){
                    ContentValues registro = new ContentValues();

                    registro.put("codigo", codigo);
                    registro.put("descripcion", descripcion);
                    registro.put("precio", precio);

                    //insertar datos a base
                    BaseDeDatos.insert("articulos", null, registro);
                    // cerrar base
                    BaseDeDatos.close();
                    etCodigo.setText("");
                    etDescripcion.setText("");
                    etPrecio.setText("");

                    Toast.makeText(MainActivity.this, "Se registraron los datos correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Buscar/Consultar un producto
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(MainActivity.this, "administracion", null, 1);
                SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

                String codigo = etCodigo.getText().toString();

                //validar que todos los campos esten llenos y busqueda de datos
                if (!codigo.isEmpty()){
                    //query
                    Cursor fila = BaseDeDatos.rawQuery("select descripcion, precio from articulos where codigo =" + codigo, null);

                    //validar si hay info o no y mostrarla en et
                    if (fila.moveToFirst()){
                        etDescripcion.setText(fila.getString(0));
                        etPrecio.setText(fila.getString(1));
                        BaseDeDatos.close();
                    } else {
                        Toast.makeText(MainActivity.this, "No existe registro del articulo", Toast.LENGTH_SHORT).show();
                        BaseDeDatos.close();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Debes introducir el código del articulo a buscar", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Eliminar producto
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper (MainActivity.this, "administracion", null, 1);
                SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

                String codigo = etCodigo.getText().toString();

                //validar si usuario escribio codigo
                if (!codigo.isEmpty()){
                    //Borrar info
                    int cantidad = BaseDeDatos.delete("articulos", "codigo=" + codigo, null);
                    BaseDeDatos.close();

                    etCodigo.setText("");
                    etDescripcion.setText("");
                    etPrecio.setText("");

                    if (cantidad == 1){
                        Toast.makeText(MainActivity.this, "Artículo eliminado exitosamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "El artículo no existe", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Debes introducir el código del articulo a eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Modificar producto
        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(MainActivity.this, "administracion", null, 1);
                SQLiteDatabase BaseDeDatos = admin.getWritableDatabase();

                String codigo = etCodigo.getText().toString();
                String descripcion = etDescripcion.getText().toString();
                String precio = etPrecio.getText().toString();

                //validar que haya info para actualizar
                if (!codigo.isEmpty() && !descripcion.isEmpty() && !precio.isEmpty()){

                    ContentValues registro = new ContentValues();
                    registro.put("codigo", codigo);
                    registro.put("descripcion", descripcion);
                    registro.put("precio", precio);
                    //Modificar valores en base de datos
                    int cantidad = BaseDeDatos.update("articulos", registro, "codigo=" + codigo, null);
                    BaseDeDatos.close();

                    if (cantidad == 1){
                        Toast.makeText(MainActivity.this, "Artículo modificado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "El artículo no existe", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}