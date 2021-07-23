package com.example.excursionstest.ui.step_list

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.excursionstest.databinding.FragmentStepListBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class StepListFragment : BottomSheetDialogFragment() {

    private var _binding : FragmentStepListBinding? = null
    private val binding
    get() = _binding!!

    private val viewModel by viewModel<StepListViewModel>()

    private lateinit var adapter: StepListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentStepListBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.stepListExcursionNameText.text = viewModel.excursion.name

        adapter = StepListAdapter()
        binding.stepListList.adapter = adapter
        adapter.submitList(viewModel.excursion.steps)

        binding.stepListBackButton.setOnClickListener{
            dismiss()
        }

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener{dialogInterface ->

            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val parentLayout = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { view ->
                val behavior = BottomSheetBehavior.from(view)
                val layoutParams = view.layoutParams
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                view.layoutParams = layoutParams
                behavior.state = BottomSheetBehavior.STATE_EXPANDED

                behavior.skipCollapsed = true
            }
        }

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}