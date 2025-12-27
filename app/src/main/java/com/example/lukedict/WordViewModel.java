package com.example.lukedict;

import android.content.Context;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WordViewModel extends ViewModel {
    private WordRepository repository;
    private MutableLiveData<Word> wordLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();

    public WordViewModel(Context context) {
        repository = WordRepository.getInstance(context);
    }

    public LiveData<Word> getWordLiveData() {
        return wordLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public void queryWord(String word) {
        if (TextUtils.isEmpty(word)) {
            errorLiveData.postValue("请输入单词");
            return;
        }

        loadingLiveData.postValue(true);
        repository.getWordTranslation(word, new WordRepository.Callback<Word>() {
            @Override
            public void onSuccess(Word result) {
                loadingLiveData.postValue(false);
                wordLiveData.postValue(result);
            }

            @Override
            public void onFailure(Throwable e) {
                loadingLiveData.postValue(false);
                errorLiveData.postValue(e.getMessage());
            }
        });
    }
}