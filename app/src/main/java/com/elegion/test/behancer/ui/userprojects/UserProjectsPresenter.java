package com.elegion.test.behancer.ui.userprojects;

import com.arellomobile.mvp.InjectViewState;
import com.elegion.test.behancer.common.BasePresenter;
import com.elegion.test.behancer.data.Storage;
import com.elegion.test.behancer.utils.ApiUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class UserProjectsPresenter extends BasePresenter<UserProjectsView> {

    private Storage mStorage;

    public UserProjectsPresenter(Storage storage) {
        mStorage = storage;
    }

    public void getUserProjects(String username) {
        mCompositeDisposable.add( ApiUtils.getApiService().getUserProjects(username)
                .doOnSuccess(response -> mStorage.insertProjects(response))
                .onErrorReturn(throwable ->
                        ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass()) ? mStorage.getProjects() : null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> getViewState().showRefresh())
                .doFinally(() -> getViewState().hideRefresh())
                .subscribe(
                        response -> getViewState().showProjects(response.getProjects()),
                        throwable -> getViewState().showError()));
    }

/*    public void openProfileFragment(String username) {
        mView.openProfileFragment(username);
    }*/
}
