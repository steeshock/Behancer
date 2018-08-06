package com.elegion.test.behancer.ui.profile;


import com.elegion.test.behancer.common.BasePresenter;
import com.elegion.test.behancer.data.Storage;
import com.elegion.test.behancer.utils.ApiUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfilePresenter extends BasePresenter {

    private ProfileView mView;
    private Storage mStorage;

    public ProfilePresenter(ProfileView view, Storage storage) {
        mView = view;
        mStorage = storage;
    }

    public void getProfile(String username) {
        mCompositeDisposable.add(ApiUtils.getApiService().getUserInfo(username)
                .subscribeOn(Schedulers.io())
                .doOnSuccess(response -> mStorage.insertUser(response))
                .onErrorReturn(throwable ->
                        ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass()) ?
                                mStorage.getUser(username) :
                                null)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mView.showRefresh())
                .doFinally(() -> mView.hideRefresh())
                .subscribe(
                        response -> {
                            mView.showProfile();
                            mView.bindProfile(response.getUser());
                        },
                        throwable -> {
                            mView.showError();
                        }));
    }


}
