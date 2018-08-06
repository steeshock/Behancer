package com.elegion.test.behancer.common;

import android.support.v4.app.Fragment;

public abstract class PresenterFragment<P extends BasePresenter> extends Fragment {

    public abstract P getPresenter();

    @Override
    public void onDetach() {
        if (getPresenter() != null) {
            getPresenter().disposeAll();
        }
        super.onDetach();
    }
}
