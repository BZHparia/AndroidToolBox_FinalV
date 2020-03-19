package fr.isen.dufaza.androidtoolbox


import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_formulaire.*
import java.text.SimpleDateFormat
import java.util.*

class FormulaireActivity : AppCompatActivity()  {

    private val myFormat = "dd/MM/yyyy"
    private val gson = Gson()
    private val jsonObject:jsonObject = jsonObject()
    private var json: String = gson.toJson(jsonObject)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulaire)

        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                updateDateInView(cal)

                val today = Calendar.getInstance()
                var age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR)
                if (today.get(Calendar.DAY_OF_YEAR) < cal.get(Calendar.DAY_OF_YEAR)){
                    age--
                }

                idSaveForm.setOnClickListener {
                    jsonObject.firstName = idnomFormulaire.text.toString()
                    jsonObject.lastName = idPrenomFormulaire.text.toString()
                    jsonObject.Age = age.toString()
                    json = gson.toJson(jsonObject)
                    Toast.makeText(this, "Informations Saved", Toast.LENGTH_SHORT).show()
                }

                idLectureForm.setOnClickListener {
                    val jsonResponse = gson.fromJson(json, jsonObject::class.java)
                    val name = jsonResponse.firstName
                    val lName = jsonResponse.lastName
                    val age = jsonResponse.Age
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("JSON")
                    builder.setMessage("First Name : $name\n" +
                            "Last Name : $lName\n" +
                            "Age : $age")
                    builder.show()
                }
            }



        idShowDate.setOnClickListener {
            DatePickerDialog(this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun updateDateInView(cal:Calendar) {
        val sdf = SimpleDateFormat(myFormat, Locale.FRANCE)
        idShowDate?.text = sdf.format(cal.time)
    }
}

class jsonObject {
    var firstName: String = ""
    var lastName: String = ""
    var Age: String = ""
}