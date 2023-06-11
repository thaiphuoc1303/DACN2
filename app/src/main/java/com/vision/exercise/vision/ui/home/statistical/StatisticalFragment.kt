package com.vision.exercise.vision.ui.home.statistical

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.enums.HoverMode
import com.anychart.enums.TooltipPositionMode
import com.vision.exercise.databinding.FragmentStatisticalBinding
import com.vision.exercise.vision.base.BaseFragment
import com.vision.exercise.vision.extension.toChartData1
import com.vision.exercise.vision.ui.home.HomeViewModel
import com.anychart.core.cartesian.series.Column
import com.anychart.enums.Anchor
import com.anychart.enums.Position
import com.vision.exercise.vision.model.Practice

class StatisticalFragment: BaseFragment<FragmentStatisticalBinding, HomeViewModel>() {
    private lateinit var mAdapter: PracticeAdapter
    companion object{
        fun newInstance() = StatisticalFragment()
    }

    override fun getLazyBinding() = lazy { FragmentStatisticalBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeViewModel>()

    override fun setupInit() {
//        TODO("Not yet implemented")
        initView()
        observe()
    }

    private fun initView() {
        mAdapter = PracticeAdapter(arrayListOf())
        binding.apply {
            rcPractice.adapter = mAdapter
            rcPractice.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun observe() {
        observePractice()
    }

    private fun observePractice() {
        viewModel.getPractice().observe(this) {
            mAdapter.setData(it)

            val listCalo = viewModel.getListTotalCalo(it as ArrayList<Practice>)
            val cartesian: Cartesian = AnyChart.column()

            val data: MutableList<DataEntry> = ArrayList()

            listCalo.forEach { item ->
                data.add(ValueDataEntry(item.first, item.second))
            }

            val column: Column = cartesian.column(data)

            column.tooltip()
                .titleFormat("{%Title}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(5.0)
                .format("{%Value}{groupsSeparator: } Calo")

            cartesian.animation(true)
            cartesian.title("1 Week")

            cartesian.yScale().minimum(0.0)

            cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
            cartesian.interactivity().hoverMode(HoverMode.BY_X)

            cartesian.xAxis(0).title("")
            cartesian.yAxis(0).title("Calo")
            binding.chart.setChart(cartesian)
        }
    }
}