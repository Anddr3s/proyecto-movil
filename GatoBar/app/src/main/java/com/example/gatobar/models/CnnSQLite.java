package com.example.gatobar.models;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;

import androidx.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CnnSQLite extends SQLiteOpenHelper {
    private static final String DATABASE = "platos.db";
    Context context;

    public CnnSQLite(@Nullable Context context) {
        super(context, DATABASE, null, 1);
        this.context = context;

        assert context != null;
        File dbFile = context.getDatabasePath(DATABASE);

        if (!dbFile.exists()) {
            try {
                copyDB(dbFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void copyDB(File db) throws IOException {
        InputStream open = context.getAssets().open(DATABASE);
        OutputStream copy = new FileOutputStream(db);
        byte[] buffer = new byte[1024];
        int length;

        while ((length = open.read(buffer)) > 0) {
            copy.write(buffer, 0, length);
        }
        copy.flush();
        copy.close();
        open.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    //SELECT

    public Cursor selectPlatos() {
        Cursor cursor;
        String sql = "SELECT ROWID as _id, * FROM platos";
        cursor = this.getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

    public Cursor selectPlato(String word) {
        Cursor cursor;
        String sql = "SELECT ROWID as _id, * FROM platos WHERE nombre LIKE '%" + word + "%'";
        cursor = this.getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

    public Cursor selectImg(String id) {
        Cursor cursor;
        String sql = "SELECT ROWID as _id, * FROM images WHERE id = " + id;
        cursor = this.getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

    public Cursor selectImgs() {
        Cursor cursor;
        String sql = "SELECT ROWID as _id, * FROM images";
        cursor = this.getReadableDatabase().rawQuery(sql, null);
        return cursor;
    }

    //DELETE

    public boolean deletePlato(int codigo) {
        String sql = "DELETE FROM platos WHERE plato_id = '" + codigo + "'";
        try {
            this.getWritableDatabase().execSQL(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //UPDATE

    public boolean updatePlato(Plato p) {
        String sql = "UPDATE platos SET nombre = '" + p.getNombre() + "', ingredientes = '" + p.getIngredientes()
                + "', precio = '" + p.getPrecio() + "', categoria_id = '" + p.getCategoria_id() + "' WHERE plato_id = '" + p.getPlato_id() + "'";
        try {
            this.getWritableDatabase().execSQL(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateImg(Images i) {
        String sql = "UPDATE images SET author = '" + i.getAuthor()
                + "', width = '" + i.getWidth() + "', height = '" + i.getHeight() + "', url = '"
                + i.getUrl() + "', , download_url = '" + i.getDownload_url()
                + "'WHERE id = '" + i.getId() + "'";
        try {
            this.getWritableDatabase().execSQL(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //INSERT

    public boolean insertPlato(Plato p) {

        String sql = "INSERT INTO platos (nombre, ingredientes, precio, categoria_id) " +
                "VALUES('" + p.getNombre() + "','" + p.getIngredientes() + "','" + p.getPrecio() + "','" + p.getCategoria_id() + "')";
        try {
            this.getWritableDatabase().execSQL(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean insertImages(Images i) {

        String sql = "INSERT INTO images (id, author, width, height, url, download_url) " +
                "VALUES('" + i.getId() + "','" + i.getAuthor() + "','" + i.getWidth() + "','" + i.getHeight() + "', '" + i.getUrl() + "', '" + i.getDownload_url() + "')";
        try {
            this.getWritableDatabase().execSQL(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}