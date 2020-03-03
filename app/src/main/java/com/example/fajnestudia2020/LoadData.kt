package com.example.fajnestudia2020

import android.content.Intent
import android.os.Bundle
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.io.File

//Load Data - activity that is responsible for loading and deleting saved data sets
class LoadData : AppCompatActivity() {

    //ListView from layout that will store all saved data sets
    private lateinit var listViewData: ListView
    //Variable that stores data set that was selected by the user
    private var selectedDataSet: String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_data)
        listViewData=findViewById(R.id.listViewData)
        //Update list view
        updateListView()
        //On normal click select data set
        listViewData.onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                selectDataSet(position)
            }
        //On long click remove data set
        listViewData.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { _, _, position, _ ->
                deleteDataSet(position)
            }

    }
    //Function that updates list view with a list of all found files in internal storage
    private fun updateListView(){
        //Check if any files are saved and if there are any create an adapter for the list view
        if(this.fileList().isNotEmpty())
            listViewData.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,this.fileList())
        else
        {
            //If there are none return to main menu
            Toast.makeText(this,"Nie znaleziono żadnych zapisanych danych na urządzeniu, dodaj jakieś dane",Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
    //Function that deletes selected data set.
    //Returns true to indicate that long press event has been consumed.
    private fun deleteDataSet(position: Int): Boolean {
        selectedDataSet = listViewData.getItemAtPosition(position) as String
        //Create a dialog box to confirm user input
        val builder= AlertDialog.Builder(this@LoadData)
        builder.setTitle("Usuń Zbiór Danych")
        builder.setMessage("Czy na pewno chcesz usunąć zbiór danych '$selectedDataSet'?")
        //On confirmation delete selected data set
        builder.setPositiveButton("Tak"){ _, _ ->
            val file= File(filesDir,selectedDataSet)
            if(file.delete())
                Toast.makeText(this,"Plik został poprawnie usunięty",Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this,"Z powodu błedu plik nie został poprawnie usunięty!",Toast.LENGTH_SHORT).show()
            //Remove outdated adapter and update list view
            listViewData.adapter=null
            updateListView()
        }
        builder.setNegativeButton("Nie",null)
        builder.show()
        return true
    }

    //Function that selects data set
    private fun selectDataSet(position: Int)
    {
        selectedDataSet = listViewData.getItemAtPosition(position) as String
        Toast.makeText(applicationContext,"Wybrano zestaw: $selectedDataSet",Toast.LENGTH_SHORT).show()
        val intent = Intent(this,MainActivity::class.java)
        //Add name of selected data set to intent extra field and return to the menu
        intent.putExtra("selectedDataSet",selectedDataSet)
        startActivity(intent)
    }
}
