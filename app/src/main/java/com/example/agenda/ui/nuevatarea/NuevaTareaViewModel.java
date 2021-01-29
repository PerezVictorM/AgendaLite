package com.example.agenda.ui.nuevatarea;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NuevaTareaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NuevaTareaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}