package com.example.othregensburg.zapp;

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.pixplicity.easyprefs.library.Prefs
import java.util.*



class FacultyStatisticActivity : AppCompatActivity() {

    private var keyfacAmount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faculty_statistic)

        fetchUsedKeys(0)

        val barChart = findViewById<View>(R.id.barchart) as BarChart
        val entries = ArrayList<BarEntry>()

        entries.add(BarEntry(3f, 0)) //value hold by chart mapped to index
        entries.add(BarEntry(2f, 1))
        entries.add(BarEntry(5f, 2))
        entries.add(BarEntry(20f, 3))
        entries.add(BarEntry(15f, 4))
        entries.add(BarEntry(19f, 5))
        entries.add(BarEntry(5f, 6))
        entries.add(BarEntry(8f,7))

        val bardataset = BarDataSet(entries, "Cells")
        val labels = ArrayList<String>()

        labels.add("Nat u. Kult")
        labels.add("Archi.")
        labels.add("Bauing.")
        labels.add("Betriebsw.")
        labels.add("E-u Itechnik")
        labels.add("Info u Mathe")
        labels.add("Maschinenbau")
        labels.add("Soz- u Geswissen.")


        val data = BarData(labels, bardataset)

        barChart.data = data // set the data and list of labels into chart
        barChart.setDescription("Rallyefortschritt") // set the description
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS)
        barChart.animateY(5000)

    }



    private fun fetchUsedKeys(facnumber: Int) : Int {

        val strfacnumber = facnumber.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/faculty_st/$strfacnumber")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<FacultyModel>()
                if (post == null) {
                    Toast.makeText(
                        baseContext, "Fail to getUsedKeys.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val tempfacKeys = post.fackeys
                keyfacAmount = tempfacKeys!!.toInt()

                Toast.makeText(
                    baseContext, "Grabbed key amount: " + keyfacAmount.toInt(),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    baseContext, "Fail2 to getUsedKeys.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    return 0
    }
}
