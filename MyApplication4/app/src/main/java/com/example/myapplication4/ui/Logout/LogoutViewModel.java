package com.example.myapplication4.ui.Logout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogoutViewModel extends ViewModel {

    private final MutableLiveData<String> text;

    public LogoutViewModel(){
        text = new MutableLiveData<>();
        //text.setValue("This is gallefgdgdfgdfgry fragment");
    }

    public LiveData<String> getText(){ return text;}
}
