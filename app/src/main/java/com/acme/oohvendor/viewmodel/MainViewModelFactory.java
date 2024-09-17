package com.acme.oohvendor.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.acme.oohvendor.repository.CampaignRepository;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    private CampaignRepository repository;

    public MainViewModelFactory(CampaignRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(repository);
        }
//        else if (modelClass.isAssignableFrom(WelcomeActivityViewModel.class)) {
//            return (T) new WelcomeActivityViewModel(repository);
//        }
        else {
            throw new IllegalArgumentException("ViewModel Not Found");
        }
    }
}
