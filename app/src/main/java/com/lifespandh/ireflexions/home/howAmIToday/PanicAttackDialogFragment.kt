package com.lifespandh.ireflexions.home.howAmIToday

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifespandh.ireflexions.R
import com.lifespandh.ireflexions.base.BaseDialogFragment
import com.lifespandh.ireflexions.home.howAmIToday.adapters.PanicAttackSymptomsAdapter
import com.lifespandh.ireflexions.home.howAmIToday.adapters.PanicAttackTriggersAdapter
import com.lifespandh.ireflexions.home.howAmIToday.network.HowAmITodayViewModel
import com.lifespandh.ireflexions.models.howAmIToday.PanicAttack
import com.lifespandh.ireflexions.models.howAmIToday.PanicSymptom
import com.lifespandh.ireflexions.models.howAmIToday.PanicTrigger
import com.lifespandh.ireflexions.utils.date.DATE_TIME_MILLI_LONG_FORMAT
import com.lifespandh.ireflexions.utils.date.TIME_FORMAT
import com.lifespandh.ireflexions.utils.date.changeDateTimeFormat
import com.lifespandh.ireflexions.utils.date.getDateTimeInFormat
import com.lifespandh.ireflexions.utils.date.getTimeInFormat
import com.lifespandh.ireflexions.utils.livedata.observeFreshly
import com.lifespandh.ireflexions.utils.logs.logE
import com.lifespandh.ireflexions.utils.ui.makeGone
import com.lifespandh.ireflexions.utils.ui.toast
import com.lifespandh.ireflexions.utils.ui.trimString
import kotlinx.android.synthetic.main.fragment_edit_support_contact.view.save_button
import kotlinx.android.synthetic.main.fragment_panic_attack.edt_time
import kotlinx.android.synthetic.main.fragment_panic_attack.intensitySlider
import kotlinx.android.synthetic.main.fragment_panic_attack.view.btn_discard
import kotlinx.android.synthetic.main.fragment_panic_attack.view.btn_save
import kotlinx.android.synthetic.main.fragment_panic_attack.view.edt_time
import kotlinx.android.synthetic.main.fragment_panic_attack.view.img_close
import kotlinx.android.synthetic.main.fragment_panic_attack.view.intensitySlider
import kotlinx.android.synthetic.main.fragment_panic_attack.view.symptomsView
import kotlinx.android.synthetic.main.fragment_panic_attack.view.triggersView
import kotlinx.android.synthetic.main.fragment_panic_attack_dialog.edt_time
import java.time.Instant
import java.util.Date

class PanicAttackDialogFragment : BaseDialogFragment(), PanicAttackTriggersAdapter.OnItemClicked, PanicAttackSymptomsAdapter.OnItemClicked {

    private lateinit var view: View

    private var panicAttack: PanicAttack? = null

    private val panicAttackTriggersAdapter by lazy { PanicAttackTriggersAdapter(mutableListOf(), this, howAmITodayViewModel) }
    private val panicAttackSymptomsAdapter by lazy { PanicAttackSymptomsAdapter(mutableListOf(), this, howAmITodayViewModel) }
    private val args by navArgs<PanicAttackDialogFragmentArgs>()
    private val howAmITodayViewModel by activityViewModels<HowAmITodayViewModel> { viewModelFactory }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.fragment_panic_attack, null)
            builder.setView(view)

            val dialog = builder.create()
            dialog.setCanceledOnTouchOutside(false)

            this.view = view
            logE("called view create")

            dialog
        } ?: throw IllegalStateException("Activity cannot be null.")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        getBundleValues()
        setViews()
        setObservers()
        setListeners()

        panicAttack?.let {
            panicAttackSymptomsAdapter.setList(it.symptoms)
            panicAttackTriggersAdapter.setList(it.triggers)

            view.btn_save.makeGone()
            view.edt_time.text = it.time.changeDateTimeFormat(DATE_TIME_MILLI_LONG_FORMAT, TIME_FORMAT)
            view.intensitySlider.progress = it.intensity.toFloat()
        }
    }

    private fun getBundleValues() {
        panicAttack = args.panicAttack
    }

    private fun setListeners() {
        view.img_close.setOnClickListener {
            dismiss()
        }

        view.btn_discard.setOnClickListener {
            dismiss()
        }

        view.btn_save.setOnClickListener {
            val panicAttack = PanicAttack(
                time = getDateTimeInFormat(),
                intensity = view.intensitySlider.intensity.value ?: 0,
                triggers = howAmITodayViewModel.selectedPanicTriggers,
                symptoms = howAmITodayViewModel.selectedPanicSymptoms
            )

            howAmITodayViewModel.selectedPanicAttack.value = panicAttack
            toast("Panic attack added")
            dismiss()
        }
    }

    private fun setViews() {
        view.edt_time.text = getTimeInFormat()

        view.triggersView.apply {
            adapter = panicAttackTriggersAdapter
            layoutManager =
                object : GridLayoutManager(context, 2, VERTICAL, false) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                    lp.width = width / spanCount
                    return true
                }
            }
        }

        view.symptomsView.apply {
            adapter = panicAttackSymptomsAdapter
            layoutManager =
                object : GridLayoutManager(context, 2, VERTICAL, false) {
                override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                    lp.width = width / spanCount
                    return true
                }
            }
        }
    }

    private fun setObservers() {
        howAmITodayViewModel.howAmITodayLiveData.observe(viewLifecycleOwner) {
            val symptoms = it.panicSymptoms.toMutableList()
            symptoms.add(PanicSymptom.other())
            panicAttackSymptomsAdapter.setList(symptoms)

            val triggers = it.panicTriggers.toMutableList()
            triggers.add(PanicTrigger.other())
            panicAttackTriggersAdapter.setList(triggers)
        }

        howAmITodayViewModel.newPanicTrigger.observeFreshly(this) {
            it?.let {
                panicAttackTriggersAdapter.addUserCreated(it)
            }
        }

        howAmITodayViewModel.newPanicSymptom.observeFreshly(this) {
            it?.let {
                panicAttackSymptomsAdapter.addUserCreated(it)
            }
        }
    }

    override fun onItemClicked(panicAttackTrigger: PanicTrigger) {

    }

    override fun onItemClicked(panicAttackSymptom: PanicSymptom) {

    }

    override fun onCustomSymptomClicked() {
        val action = PanicAttackDialogFragmentDirections.actionPanicAttackDialogFragmentToCustomHappeningFragment(DIALOG_FOR.PANIC_SYMPTOM)
        findNavController().navigate(action)
    }

    override fun onCustomTriggerClicked() {
        val action = PanicAttackDialogFragmentDirections.actionPanicAttackDialogFragmentToCustomHappeningFragment(DIALOG_FOR.PANIC_TRIGGER)
        findNavController().navigate(action)
    }
}
