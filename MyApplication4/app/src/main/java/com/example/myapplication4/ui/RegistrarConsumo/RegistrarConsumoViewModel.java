package com.example.myapplication4.ui.RegistrarConsumo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegistrarConsumoViewModel extends ViewModel {

    private final MutableLiveData<String> mtext;

    public RegistrarConsumoViewModel(){
        mtext = new MutableLiveData<>();
    }
    public LiveData<String> getText(){return mtext; }
}
