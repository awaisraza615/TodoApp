package com.awais.raza.todoapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.awais.raza.todoapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class MainActivity : AppCompatActivity() ,OnClickListener{

    private lateinit var binding: ActivityMainBinding

    private val PREFS_TAG = "SharedPrefs"
    private val PRODUCT_TAG = "MyProduct"

    lateinit var setSharedPreferences : SharedPreferences

    private lateinit var arrayList : ArrayList<Notes?>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSharedPreferences = getApplicationContext().getSharedPreferences(PRODUCT_TAG, Context.MODE_PRIVATE);

        arrayList = ArrayList()
        getCompaniesList(PREFS_TAG)?.let {  arrayList.addAll(it)}

       val adapter = NotesAdapter(
            notes = arrayList, onClickListener = this
        )
        binding.recycler.adapter = adapter


        binding.actionBtn.setOnClickListener {

            showForgotDialog()
        }
    }

    fun notifyData(){
        setList(PREFS_TAG,arrayList)
        binding.recycler.adapter?.notifyDataSetChanged()
    }

    private fun showForgotDialog() {
        val taskEditText = EditText(this@MainActivity)
        val dialog = AlertDialog.Builder(this@MainActivity)
            .setTitle("Task")
            .setMessage("Enter your Task")
            .setView(taskEditText)
            .setPositiveButton(
                "Save"
            ) { dialog, which -> val task = taskEditText.text.toString()
                arrayList.add(Notes(false,task))

                notifyData()
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    fun <T> setList(key: String?, list: ArrayList<T>?) {
        val gson = Gson()
        val json = gson.toJson(list)
        set(key, json)
    }

    operator fun set(key: String?, value: String?) {
        if (setSharedPreferences != null) {
            val prefsEditor: SharedPreferences.Editor = setSharedPreferences.edit()
            prefsEditor.putString(key, value)
            prefsEditor.commit()
        }
    }


    fun getCompaniesList(key: String): ArrayList<Notes>? {
        val gson = Gson()
        val companyList: ArrayList<Notes>
        val string: String? = setSharedPreferences.getString(key, null)
        val type: Type = object : TypeToken<ArrayList<Notes?>?>() {}.type
        string?.let {
            companyList = gson.fromJson(string, type)
            return companyList
        }
        return null
    }


    override fun onIsCheckedClick(pos: Int, note: Notes) {
        arrayList[pos]?.isChecked= true
        Handler().postDelayed({
            notifyData()
        }, 1500)

    }

    override fun onIsUnCheckedClick(pos: Int, note: Notes) {
        arrayList[pos]?.isChecked= false
        Handler().postDelayed({
            notifyData()
        }, 1500)
    }

    override fun onDeleteClick(pos: Int, note: Notes) {
        arrayList.remove(note)
        notifyData()
    }
}