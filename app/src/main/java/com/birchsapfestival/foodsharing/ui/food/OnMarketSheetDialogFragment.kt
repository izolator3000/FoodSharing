package com.birchsapfestival.foodsharing.ui.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.birchsapfestival.foodsharing.R
import com.birchsapfestival.foodsharing.model.FoodModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OnMarketSheetDialogFragment : BottomSheetDialogFragment() {
    private lateinit var titleView: TextView
    private lateinit var phoneView: TextView

    private val model: FoodModel? by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getSerializable(FOOD_ON_MARKER_INFO_EXTRA) as FoodModel?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        model?.let {
            titleView.text = it.title
            phoneView.text = it.phoneNumber
        }
    }

    private fun initViews(view: View) {
        titleView = view.findViewById(R.id.title)
        phoneView = view.findViewById(R.id.phone_number)
    }

    companion object {
        const val FOOD_ON_MARKER_INFO_EXTRA = "FOOD_ON_MARKER_INFO_EXTRA"
        fun newInstance(model: FoodModel?): OnMarketSheetDialogFragment {
            val fragment = OnMarketSheetDialogFragment()
            val bundle = Bundle().apply {
                putSerializable(FOOD_ON_MARKER_INFO_EXTRA, model)
            }
            fragment.arguments = bundle;
            return fragment
        }
    }
}