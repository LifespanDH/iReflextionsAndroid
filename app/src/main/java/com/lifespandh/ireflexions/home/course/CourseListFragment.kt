package com.lifespandh.ireflexions.home.course

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifespandh.ireflexions.R
import com.lifespandh.ireflexions.base.BaseFragment
import com.lifespandh.ireflexions.home.HomeViewModel
import com.lifespandh.ireflexions.home.exercise.ExerciseAdapter
import com.lifespandh.ireflexions.home.exercise.ExerciseFragment
import com.lifespandh.ireflexions.home.exercise.ExerciseFragmentDirections
import com.lifespandh.ireflexions.models.Program
import com.lifespandh.ireflexions.utils.livedata.observeFreshly
import com.lifespandh.ireflexions.utils.logs.logE
import kotlinx.android.synthetic.main.fragment_course_list.*


class CourseListFragment : BaseFragment(), CourseListProgramAdapter.OnItemClicked {

    private val homeViewModel by viewModels<HomeViewModel> { viewModelFactory }
    private val courseListProgramAdapter by lazy { CourseListProgramAdapter(listOf(), this) }
    private var currentProgram: List<Program>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        getPrograms()
        setViews()
        setObservers()
    }

    private fun getPrograms() {
        homeViewModel.getPrograms()
        homeViewModel.getRegisteredProgramList()
    }

    private fun setViews(){
        rvPrograms.apply {
            adapter = courseListProgramAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        if(currentProgram == null)
        {
            browseProgramsTV.visibility = View.VISIBLE
        }
    }


    private fun setObservers() {
        homeViewModel.programsLiveData.observeFreshly(this) {
            courseListProgramAdapter.setList(it)
        }

        homeViewModel.registeredProgramsLiveData.observeFreshly(this){
            currentProgram = it
            updateCurrentProgram()
        }

        tokenViewModel.token.observeFreshly(this) {
            logE("called token $it")
        }

        tokenViewModel.refreshToken.observeFreshly(this) {
            logE("called regresh $it")
        }
    }

    companion object {
        fun newInstance() = CourseListFragment()
    }
    private fun updateCurrentProgram() {
        browseProgramsTV.visibility = View.INVISIBLE
        currentProgramContainer.visibility = View.VISIBLE
    }

    override fun onItemClick(program: Program) {
        val action = CourseListFragmentDirections.actionCourseListFragmentToCourseFragment(parentProgram = program)
        findNavController().navigate(action)
    }

    override fun onProgramEnroll(program: Program) {

    }
}