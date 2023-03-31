package com.lifespandh.ireflexions.home.howAmIToday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifespandh.ireflexions.R
import com.lifespandh.ireflexions.base.BaseFragment
import com.lifespandh.ireflexions.utils.livedata.observeFreshly
import kotlinx.android.synthetic.main.fragment_how_am_i_no_entry.*
import java.text.SimpleDateFormat
import java.util.*

class HowAmINoEntryFragment : BaseFragment(), WeekAdapter.OnItemClickedListener {

    private var token: String? = null
    private var toDate = Calendar.getInstance().time
    private lateinit var weekAdapter: WeekAdapter
    private val dateBundle = Bundle()
    private lateinit var currentDate: Date
    private val dateFormat = SimpleDateFormat("MM/dd/yyyy")

    private val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())

    private val formatDay = SimpleDateFormat("EEE", Locale.US)
    private val formatMonth = SimpleDateFormat("MMM", Locale.US)
    private val formatDate = SimpleDateFormat("dd", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_how_am_i_no_entry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun setObservers() {
        tokenViewModel.token.observeFreshly(viewLifecycleOwner) {
            token = it
        }
    }

    companion object {
        fun newInstance() = HowAmINoEntryFragment()
    }

    private fun init() {
        setListeners()
        setObservers()
    }

    private fun setListeners() {
        addCircleImageView.setOnClickListener {
            if (System.currentTimeMillis() > toDate.time) {
                dateBundle.putSerializable("DATE", toDate)
                findNavController().navigate(
                    R.id.action_howAmINoEntryFragment_to_howAmICreateEntryFragment,
                    dateBundle
                )
            }
        }

        weekView.setOnClickListener {
            findNavController().navigate(R.id.action_howAmINoEntryFragment_to_weeklyReportFragment2)
        }

        layout_month.setOnClickListener {
            findNavController().navigate(
                R.id.action_howAmINoEntryFragment_to_monthlyReportFragment2,
            )
        }

        addCircleImageViewBig.setOnClickListener {
            if (System.currentTimeMillis() > toDate.time) {
                dateBundle.putSerializable("DATE", toDate)
                findNavController().navigate(
                    R.id.action_howAmINoEntryFragment_to_howAmICreateEntryFragment,
                    dateBundle
                )
            }
        }

        arrow_previous.setOnClickListener {
            val calendarPrevious = Calendar.getInstance().apply {
                time = currentDate
                firstDayOfWeek = Calendar.MONDAY
                add(Calendar.DAY_OF_MONTH, -7)
                this[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
            }
            setAdapter(calendarPrevious)

        }

        arrow_next.setOnClickListener {
            val calendarNext = Calendar.getInstance().apply {
                time = currentDate
                firstDayOfWeek = Calendar.MONDAY
                add(Calendar.DAY_OF_MONTH, 7)
                this[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
            }
            setAdapter(calendarNext)

        }
    }

    private fun setAdapter(calendar: Calendar) {

        currentDate = calendar.time

        var firstDayString = String()
        var lastDayString = String()
        val days = ArrayList<String>()
        val month = ArrayList<String>()
        val date = ArrayList<String>()
        val dateList = ArrayList<String>()

        for (i in 0..6) {
            when (i) {
                0 -> {
                    val fDay = dateFormat.parse(dateFormat.format(calendar.time))
                    firstDayString = parser.format(fDay)
                }
                6 -> {
                    lastDayString = parser.format(calendar.time)
                }
            }
            days.add(formatDay.format(calendar.time))
            month.add(formatMonth.format(calendar.time))
            date.add(formatDate.format(calendar.time))
            dateList.add(parser.format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        dayView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        weekAdapter = WeekAdapter(
            days, month, date, dateList = dateList
        )

        dayView.adapter = weekAdapter
        weekAdapter.setOnItemClickedListener(this)
    }

    override fun onResume() {
        super.onResume()
        val cal = Calendar.getInstance()

        cal.time = toDate
//        viewModel.selectedItem = cal.get(Calendar.DAY_OF_WEEK) - 2

        cal.apply {
            firstDayOfWeek = Calendar.MONDAY
            this[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        }

        setAdapter(cal)
    }

    override fun onItemClick(position: Int, viewHolder: RecyclerView.ViewHolder) {

    }
}