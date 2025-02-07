package com.example.tapasparquesol_zhurongsen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var dbHandler: DatabaseHelper
    private lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerView)
        val boton = findViewById<Button>(R.id.button)
        val botonAcercaDe = findViewById<Button>(R.id.buttonAcercaDe)

        viewBares()

        boton.setOnClickListener(){
            addBar()

        }

        botonAcercaDe.setOnClickListener(){
            Toast.makeText(this,"Creado por Zhu Rong Sen",Toast.LENGTH_SHORT).show()
        }

    }

    private fun addBar() {
        val nombrePrueba = "bar1"
        val direccionPrueba = "C/sajdhaskjfh"
        val valoracionPrueba = 3
        val latitudPrueba = 123.32
        val longitudPrueba = 123.12
        val webPrueba = "dfsdfjsdlfjsdkf.com"

        val bar = Bar(nombre = nombrePrueba, direccion = direccionPrueba, valoracion = valoracionPrueba,
            latitud = latitudPrueba,longitud = longitudPrueba, web = webPrueba)
        val status = dbHandler.addBar(bar)
        if(status > -1){

            viewBares()
        }else{
            Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
        }

    }

    private fun viewBares(){
        val barList = dbHandler.getAllBares()
        val adapter = BarAdapter(barList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}