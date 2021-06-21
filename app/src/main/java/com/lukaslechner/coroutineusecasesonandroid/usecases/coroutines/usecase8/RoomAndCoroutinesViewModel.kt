package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase8

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.launch
import java.lang.Exception

class RoomAndCoroutinesViewModel(
    private val api: MockApi,
    private val database: AndroidVersionDao
) : BaseViewModel<UiState>() {

    fun loadData() {
        uiState.value=UiState.Loading.LoadFromDb
        viewModelScope.launch{
            val localVersions = database.getAndroidVersions()
            if(localVersions.isEmpty()){
                uiState.value=UiState.Error(DataSource.DATABASE, "Empty database")
            }else{
                uiState.value=UiState.Success(DataSource.DATABASE, localVersions.mapToUiModelList())
            }

            uiState.value=UiState.Loading.LoadFromNetwork
            try {
                val recentVersionNetwork = api.getRecentAndroidVersions()
                for (version in recentVersionNetwork) {
                    database.insert(version.mapToEntity())
                }
                uiState.value = UiState.Success(DataSource.NETWORK, recentVersionNetwork)
            }catch(exception: Exception){
                uiState.value = UiState.Error(DataSource.NETWORK, "Erro em conexão com a internet")
            }
        }
    }

    fun clearDatabase() {
        viewModelScope.launch{
            database.clear()
        }
    }
}

enum class DataSource(val dataSourceName: String) {
    DATABASE("Database"),
    NETWORK("Network")
}