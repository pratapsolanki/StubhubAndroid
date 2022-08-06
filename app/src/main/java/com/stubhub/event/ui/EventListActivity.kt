package com.stubhub.event.ui

import android.os.Bundle
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.stubhub.event.R
import com.stubhub.event.databinding.ActivityMainBinding
import com.stubhub.event.model.Event
import com.stubhub.event.model.EventManage
import com.stubhub.event.utils.*
import com.stubhub.event.utils.GsonUtil as GsonUtil1


class EventListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventList: MutableList<Event>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        iniRecyclerview()
        fetchDataFromAssetFile()
        radioSelection()
        setListener()
        resetData()
    }

    /**
     * reset/reload data from the asset folder
     */
    private fun resetData() {
        binding.fabReset.setOnClickListener {
            if (::eventList.isInitialized) {
                toast(getString(R.string.reset_data))
                renderEventList(eventList)
            }
        }
    }

    /**
     * selection wise hide show input texts
     */
    private fun radioSelection() {
        binding.radioGroupSelection.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById(checkedId) as RadioButton
            Logger.d(radioButton.text.toString())
            when (radioButton.text.toString()) {
                getString(R.string.city) -> {
                    binding.inputCity.visible()
                    binding.inputPrice.gone()
                }
                getString(R.string.price) -> {
                    binding.inputCity.gone()
                    binding.inputPrice.visible()
                }
                getString(R.string.both) -> {
                    binding.inputCity.visible()
                    binding.inputPrice.visible()
                }
                getString(R.string.both_end) -> {
                    binding.inputCity.visible()
                    binding.inputPrice.visible()
                }
            }
        }
    }

    /**
     *  Handle ok button click listener
     */
    private fun setListener() {
        binding.btnOK.setOnClickListener {
            val selectedId: Int = binding.radioGroupSelection.checkedRadioButtonId
            if (selectedId == -1) {
                toast(getString(R.string.selection_error))
            } else {
                val radioButton =
                    binding.radioGroupSelection.findViewById(selectedId) as RadioButton

                if (radioButton.text == getString(R.string.city)) {
                    if (binding.edtCity.text.toString().trim().isNotEmpty()) {
                        filterByCityName()
                    } else {
                        toast(getString(R.string.enter_city))
                    }
                } else if (radioButton.text == getString(R.string.price)) {
                    if (binding.edtPrice.text.toString().trim().isNotEmpty()) {
                        filterByPrice()
                    } else {
                        toast(getString(R.string.enter_price))
                    }
                } else if (radioButton.text == getString(R.string.both)) {
                    if (binding.edtCity.text.toString().trim()
                            .isNotEmpty() && binding.edtPrice.text.toString().trim().isNotEmpty()
                    ) {
                        filterByCityOrPrice()
                    } else {
                        if (binding.edtCity.text.toString().trim().isEmpty()) {
                            filterByPrice()
                        }
                        if (binding.edtPrice.text.toString().trim().isEmpty()) {
                            filterByCityName()
                        }
                    }
                } else if (radioButton.text == getString(R.string.both_end)) {
                    if (binding.edtCity.text.toString().trim()
                            .isNotEmpty() && binding.edtPrice.text.toString().trim().isNotEmpty()
                    ) {
                        filterByCityAndPrice()
                    } else {
                        toast(getString(R.string.empty_value))
                    }
                }
            }
        }
    }


    /**
     * Filter list data by City and Price
     */
    private fun filterByCityAndPrice() {
        setDefaultValue()
        val filterByCityAndPrice = eventList.filter { event ->
            event isNullOr {
                event.city.lowercase()
                    .contains(binding.edtCity.text!!.toString().trim().lowercase())
            } && event isNullOr {
                event.price <= binding.edtPrice.text.toString().toInt()
            }
        }
        if (filterByCityAndPrice.isNotEmpty()) {
            renderEventList(filterByCityAndPrice as MutableList<Event>)
        } else {
            renderEventList(emptyResults())
        }
    }

    /**
     * set empty list to show result not found text
     */
    private fun emptyResults(): MutableList<Event> {
        return mutableListOf()
    }

    /**
     * filter list data by City or Price
     */
    private fun filterByCityOrPrice() {
        setDefaultValue()
        val filterByCityAnOrPrice = eventList.filter { event ->
            event isNullOr {
                event.city.lowercase()
                    .contains(binding.edtCity.text!!.toString().trim().lowercase())
            } || event isNullOr {
                event.price <= binding.edtPrice.text.toString().toInt()
            }
        }
        if (filterByCityAnOrPrice.isNotEmpty()) {
            renderEventList(filterByCityAnOrPrice as MutableList<Event>)
        } else {
            renderEventList(emptyResults())

        }
    }

    /**
     * Filter list data object by price should be less than or equal to
     */
    private fun filterByPrice() {
        setDefaultValue()
        val filterByCityAnOrPrice = eventList.filter { event ->
            event isNullOr {
                event.price <= binding.edtPrice.text.toString().toInt()
            }
        }
        if (filterByCityAnOrPrice.isNotEmpty()) {
            renderEventList(filterByCityAnOrPrice as MutableList<Event>)
        } else {
            renderEventList(emptyResults())
        }
    }

    /**
     * set default value 0 to price
     */
    private fun setDefaultValue() {
        if (binding.edtPrice.text.toString().trim().isEmpty()) {
            binding.edtPrice.setText("0")
        }
    }

    /**
     * Filter list data object by  city name
     */
    private fun filterByCityName() {
        val filterByCityAnOrPrice = eventList.filter { event ->
            event isNullOr {
                event.city.lowercase()
                    .contains(binding.edtCity.text!!.toString().trim().lowercase())
            }
        }
        if (filterByCityAnOrPrice.isNotEmpty()) {
            renderEventList(filterByCityAnOrPrice as MutableList<Event>)
        } else {
            renderEventList(emptyResults())
        }
    }

    /**
     * Handle null in the filter condition
     */
    inline infix fun <T> T?.isNullOr(predicate: (T) -> Boolean): Boolean =
        if (this != null) predicate(this) else true


    /**
     * Retrieve data from asset folder and pass to the adapter
     */
    private fun fetchDataFromAssetFile() {
        val jsonToString = getJsonDataFromAsset(this@EventListActivity, "eventList.json")
        jsonToString.let {
            Logger.d(it.toString())
            val eventManage = GsonUtil1.fromJson(it.toString(), EventManage::class.java)
            Logger.d(eventManage.toString())
            Logger.d(eventManage.events.size.toString())
            if (eventManage.events.isNotEmpty()) {
                eventList = eventManage.events
                renderEventList(eventManage.events)
            }
        }
    }

    /**
     * Initialize Recyclerview
     */
    private fun iniRecyclerview() {
        eventAdapter = EventAdapter()
        binding.rvEventList.layoutManager = LinearLayoutManager(this)
        binding.rvEventList.adapter = eventAdapter
    }

    /**
     * Pass Data to Recyclerview function
     */
    private fun renderEventList(eventList: MutableList<Event>) {
        Logger.d(eventList.size.toString())
        if (eventList.size > 0) {
            //show list
            binding.rvEventList.visible()
            binding.tvNotFound.gone()
        } else {
            //show not found text
            binding.tvNotFound.visible()
            binding.rvEventList.gone()
        }
        eventAdapter.setData(eventList)
    }


}