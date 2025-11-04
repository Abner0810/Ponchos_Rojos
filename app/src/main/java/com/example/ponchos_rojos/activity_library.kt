package com.example.ponchos_rojos

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ponchos_rojos.DataClass.GameInfo
import com.example.ponchos_rojos.adapters.AdapterRecyclerLibrary
import com.example.ponchos_rojos.databinding.ActivityLibraryBinding
import org.json.JSONArray

class activity_library : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding
    private val context: Context = this
    private lateinit var sharedPreferencesLibrary: SharedPreferences
    private var ownedGamesList: MutableList<GameInfo> = mutableListOf()
    private lateinit var libraryAdapter: AdapterRecyclerLibrary
    private var currentSearchQuery = ""

    // opciones de ordenación
    private val SORT_BY_NAME = "Name"
    private val SORT_BY_RECENTLY_PLAYED = "Date"
    private val SORT_BY_TIME_PLAYED = "Time Played"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadOwnedGames()

        setupRecyclerView()

        if (ownedGamesList.isNotEmpty()) {
            setupSortSpinner()
            setupSearchByText()
            setupSearchIconVisibility()
        } else {
            binding.yourLibraryIsemptyTitle.visibility = View.VISIBLE
            binding.SortSpinner.visibility = View.GONE
        }

        setUpIntents()
    }

    private fun loadOwnedGames() {
        sharedPreferencesLibrary = getSharedPreferences("JuegosLibrary", Context.MODE_PRIVATE)
        val allGames = loadGamesFromJson() // Carga todos los juegos del JSON

        // Filtra para obtener solo los que el usuario posee
        ownedGamesList = allGames.filter { game ->
            sharedPreferencesLibrary.contains("idGames_${game.name}")
        }.toMutableList()

    }

    private fun setupRecyclerView() {
        libraryAdapter = AdapterRecyclerLibrary(this, mutableListOf()) // Inicia vacío
        binding.recyclerGames.layoutManager = LinearLayoutManager(this)
        binding.recyclerGames.adapter = libraryAdapter
    }

    private fun setupSortSpinner() {
        val sortOptions = listOf(SORT_BY_NAME, SORT_BY_RECENTLY_PLAYED, SORT_BY_TIME_PLAYED)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.SortSpinner.adapter = spinnerAdapter

        binding.SortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val filteredList = if (currentSearchQuery.isBlank()) {
                    ownedGamesList
                } else {
                    ownedGamesList.filter { game ->
                        game.name.contains(currentSearchQuery, ignoreCase = true)
                    }
                }
                val selectedSortOption = binding.SortSpinner.selectedItem?.toString() ?: SORT_BY_NAME

                val sortedList = when (selectedSortOption) {
                    SORT_BY_NAME -> filteredList.sortedBy { it.name }
                    SORT_BY_RECENTLY_PLAYED -> filteredList.sortedByDescending { it.lastPlayedDate }
                    SORT_BY_TIME_PLAYED -> filteredList.sortedByDescending { it.timePlayed }
                    else -> filteredList
                }

                libraryAdapter.updateList(sortedList, selectedSortOption)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupSearchByText() {
        binding.searchBar.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {

                currentSearchQuery = v.text.toString()

                val filteredList = if (currentSearchQuery.isBlank()) {
                    ownedGamesList
                } else {
                    ownedGamesList.filter { game ->
                        game.name.contains(currentSearchQuery, ignoreCase = true)
                    }
                }

                val selectedSortOption = binding.SortSpinner.selectedItem?.toString() ?: SORT_BY_NAME

                val sortedList = when (selectedSortOption) {
                    SORT_BY_NAME -> filteredList.sortedBy { it.name }
                    SORT_BY_RECENTLY_PLAYED -> filteredList.sortedByDescending { it.lastPlayedDate }
                    SORT_BY_TIME_PLAYED -> filteredList.sortedByDescending { it.timePlayed }
                    else -> filteredList
                }

                libraryAdapter.updateList(sortedList, selectedSortOption)

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun setUpIntents() {
        binding.buttonimageTag.setOnClickListener {
            startActivity(Intent(context, TiendaActivity::class.java))
        }
        binding.buttonimageCart.setOnClickListener {
            startActivity(Intent(context, activity_cart::class.java))
        }
        binding.imageProfile.setOnClickListener {
            startActivity(Intent(context, MainPerfilActivity::class.java))
        }
    }

    private fun setupSearchIconVisibility() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.imageview1.isVisible = s.isNullOrEmpty()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun loadGamesFromJson(): MutableList<GameInfo> {
        val gameList = mutableListOf<GameInfo>()
        val jsonString: String = assets.open("games.json").bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val tagsJsonArray = jsonObject.getJSONArray("tags")
            val tagsList = mutableListOf<String>()
            for (j in 0 until tagsJsonArray.length()) {
                tagsList.add(tagsJsonArray.getString(j))
            }
            val game = GameInfo(
                id = jsonObject.getInt("id"),
                name = jsonObject.getString("name"),
                developer = jsonObject.getString("developer"),
                releasedDate = jsonObject.getString("releasedDate"),
                description = jsonObject.getString("description"),
                url = jsonObject.getString("url"),
                tags = tagsList,
                imageName = jsonObject.getString("imageName"),
                price = jsonObject.getString("price"),
                so = jsonObject.getString("so"),
                processor = jsonObject.getString("processor"),
                memory = jsonObject.getString("memory"),
                graphics = jsonObject.getString("graphics"),
                storage = jsonObject.getString("storage"),
                timePlayed = jsonObject.getLong("timePlayed"),
                lastPlayedDate = jsonObject.getLong("lastPlayedDate")
            )
            gameList.add(game)
        }
        return gameList
    }
}
