package com.example.sem_app.ui.Tournaments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TournamentsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TournamentsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Tournaments fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}