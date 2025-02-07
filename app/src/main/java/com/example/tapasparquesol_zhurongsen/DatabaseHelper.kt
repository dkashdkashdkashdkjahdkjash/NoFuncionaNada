package com.example.tapasparquesol_zhurongsen

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

// Clase DatabaseHelper que extiende SQLiteOpenHelper para manejar la base de datos de la aplicación.
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Bloque companion object para definir constantes que serán usadas en toda la clase.
    // Son como los valores estáticos en Java
    companion object {
        // Nombre de la base de datos.
        private const val DATABASE_NAME = "BarDatabase"
        // Versión de la base de datos, útil para manejar actualizaciones esquemáticas.
        private const val DATABASE_VERSION = 1
        // Nombre de la tabla donde se almacenarán los discos.
        private const val TABLE_BARES = "Bares"
        // Nombres de las columnas de la tabla.
        private const val KEY_ID = "id"
        private const val KEY_NOMBRE = "nombre"
        private const val KEY_DIRECCION = "direccion"
        private const val KEY_VALORACION = "valoracion"
        private const val KEY_LATITUD = "latitud"
        private const val KEY_LONGITUD = "longitud"
        private const val KEY_WEB = "web"
    }

    // Método llamado cuando la base de datos se crea por primera vez.
    override fun onCreate(db: SQLiteDatabase) {
        // Define la sentencia SQL para crear la tabla de discos.
        val createBaresTable = ("CREATE TABLE " + TABLE_BARES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NOMBRE + " TEXT,"
                + KEY_DIRECCION + " TEXT,"
                + KEY_VALORACION + " NUMBER,"
                + KEY_LATITUD + "NUMBER,"
                + KEY_LONGITUD + "NUMBER,"
                + KEY_WEB + "TEXT"
                + ")")
        // Ejecuta la sentencia SQL para crear la tabla.
        db.execSQL(createBaresTable)
    }

    // Método llamado cuando se necesita actualizar la base de datos, por ejemplo, cuando se incrementa DATABASE_VERSION.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Elimina la tabla existente y crea una nueva.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BARES")
        onCreate(db)
    }

    // Método para obtener todos los discos de la base de datos.
    fun getAllBares(): ArrayList<Bar> {
        // Lista para almacenar y retornar los discos.
        val baresList = ArrayList<Bar>()
        // Consulta SQL para seleccionar todos los discos.
        val selectQuery = "SELECT  * FROM $TABLE_BARES"

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            // Ejecuta la consulta SQL.
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            // Maneja la excepción en caso de error al ejecutar la consulta.
            db.execSQL(selectQuery)
            return ArrayList()
        }

        // Variables para almacenar los valores de las columnas.
        var id: Int
        var nombre: String
        var direccion: String
        var valoracion : Int
        var latitud : Double
        var longitud : Double
        var web : String

        // Itera a través del cursor para leer los datos de la base de datos.
        if (cursor.moveToFirst()) {
            do {
                // Obtiene los índices de las columnas.
                val idIndex = cursor.getColumnIndex(KEY_ID)
                val nombreIndex = cursor.getColumnIndex(KEY_NOMBRE)
                val direccionIndex = cursor.getColumnIndex(KEY_DIRECCION)
                val valoracionIndex = cursor.getColumnIndex(KEY_VALORACION)
                val latitudIndex = cursor.getColumnIndex(KEY_LATITUD)
                val longitudIndex = cursor.getColumnIndex(KEY_LONGITUD)
                val webIndex = cursor.getColumnIndex(KEY_WEB)
                // Verifica que los índices sean válidos.
                if (idIndex != -1 && nombreIndex != -1 && direccionIndex != -1 && valoracionIndex != -1 && latitudIndex != -1 && longitudIndex != -1 && webIndex != -1) {
                    // Lee los valores y los añade a la lista de discos.
                    id = cursor.getInt(idIndex)
                    nombre = cursor.getString(nombreIndex)
                    direccion = cursor.getString(direccionIndex)
                    valoracion = cursor.getInt(valoracionIndex)
                    latitud = cursor.getDouble(latitudIndex)
                    longitud = cursor.getDouble(longitudIndex)
                    web = cursor.getString(webIndex)
                    val bar = Bar(id = id, nombre = nombre, direccion = direccion, valoracion = valoracion, latitud = latitud,
                        longitud = longitud, web = web)
                    baresList.add(bar)
                }
            } while (cursor.moveToNext())
        }

        // Cierra el cursor para liberar recursos.
        cursor.close()
        return baresList
    }

    // Método para actualizar un disco en la base de datos.
    fun updateBar(bar: Bar): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        // Prepara los valores a actualizar.
        contentValues.put(KEY_NOMBRE, bar.nombre)
        contentValues.put(KEY_DIRECCION, bar.direccion)
        contentValues.put(KEY_VALORACION,bar.valoracion)
        contentValues.put(KEY_LATITUD,bar.latitud)
        contentValues.put(KEY_LONGITUD,bar.longitud)
        contentValues.put(KEY_WEB,bar.web)

        // Actualiza la fila correspondiente y retorna el número de filas afectadas.
        return db.update(TABLE_BARES, contentValues, "$KEY_ID = ?", arrayOf(bar.id.toString()))
    }

    // Método para eliminar un disco de la base de datos.
    fun deleteBar(bar: Bar): Int {
        val db = this.writableDatabase
        // Elimina la fila correspondiente y retorna el número de filas afectadas.
        val success = db.delete(TABLE_BARES, "$KEY_ID = ?", arrayOf(bar.id.toString()))
        db.close()
        return success
    }

    // Método para añadir un nuevo disco a la base de datos.
    fun addBar(bar: Bar): Long {
        try {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            // Prepara los valores a insertar.
            contentValues.put(KEY_NOMBRE, bar.nombre)
            contentValues.put(KEY_DIRECCION, bar.direccion)
            contentValues.put(KEY_VALORACION,bar.valoracion)
            contentValues.put(KEY_LATITUD,bar.latitud)
            contentValues.put(KEY_LONGITUD,bar.longitud)
            contentValues.put(KEY_WEB,bar.web)

            // Inserta el nuevo disco y retorna el ID del nuevo disco o -1 en caso de error.
            val success = db.insert(TABLE_BARES, null, contentValues)
            db.close()
            return success
        } catch (e: Exception) {
            // Maneja la excepción en caso de error al insertar.
            Log.e("Error", "Error al agregar bar", e)
            return -1
        }
    }
}


