package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.databinding.ActivityAddCourseBinding
import com.dicoding.courseschedule.ui.list.ListViewModelFactory
import com.dicoding.courseschedule.util.TimePickerFragment
import java.text.SimpleDateFormat
import java.util.*

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {
    private lateinit var binding: ActivityAddCourseBinding
    private lateinit var viewModel: AddCourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ListViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory).get(AddCourseViewModel::class.java)
    }

    fun onClickStartTime(view: View) {
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, "startTime")
    }
    fun onClickEndTime(view: View) {
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, "end")
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        if (tag == "startTime") {
            binding.tvStartTimeClock.text = dateFormat.format(calendar.time)
        } else {
            binding.tvEndTimeClock.text = dateFormat.format(calendar.time)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                val etCourseName = binding.etCourseName
                val spinnerDay = binding.spinnerDay
                val etLecturer = binding.etLecturer
                val etNote = binding.etNote

                val course = etCourseName.text.toString().trim()
                val day = spinnerDay.selectedItemPosition
                val startTime = binding.tvStartTimeClock.text.toString()
                val endTime = binding.tvEndTimeClock.text.toString()
                val lecturer = etLecturer.text.toString().trim()
                val note = etNote.text.toString().trim()

                if (course.isNotEmpty() && lecturer.isNotEmpty() && note.isNotEmpty()) {
                    viewModel.insertCourse(
                        courseName = course,
                        day = day,
                        startTime = startTime,
                        endTime = endTime,
                        lecturer = lecturer,
                        note = note
                    )
                    finish()
                } else {
                    Toast.makeText(this, getString(R.string.empty_list_message), Toast.LENGTH_SHORT)
                        .show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}