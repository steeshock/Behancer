package com.elegion.test.behancer.ui.userprojects;

import com.elegion.test.behancer.BuildConfig;
import com.elegion.test.behancer.common.BasePresenter;
import com.elegion.test.behancer.data.Storage;
import com.elegion.test.behancer.utils.ApiUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserProjectsPresenter extends BasePresenter<UserProjectsView> {

    private UserProjectsView mView;
    private Storage mStorage;

    public UserProjectsPresenter(UserProjectsView view, Storage storage) {
        mView = view;
        mStorage = storage;
    }

    public void getUserProjects(String username) {
        mCompositeDisposable.add( ApiUtils.getApiService().getUserProjects(username)
                .doOnSuccess(response -> mStorage.insertProjects(response))
                .onErrorReturn(throwable ->
                        ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass()) ? mStorage.getProjects() : null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> mView.showRefresh())
                .doFinally(() -> mView.hideRefresh())
                .subscribe(
                        response -> mView.showProjects(response.getProjects()),
                        throwable -> mView.showError()));
    }

/*    public void openProfileFragment(String username) {
        mView.openProfileFragment(username);
    }*/
}
