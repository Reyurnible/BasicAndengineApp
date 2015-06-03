package com.andengine.hosshan.basicgame.scene;

import android.view.KeyEvent;

import com.andengine.hosshan.basicgame.activity.MultiSceneActivity;

/**
 * Created by shunhosaka on 2015/03/29.
 */
public class MainScene extends KeyListenScene {

    // Sceneを管理するActivityのインスタンスを保持 // アプリの場合のContextと同じように利用できる private BaseGameActivity baseActivity;
    public MainScene(MultiSceneActivity baseActivity) {
        super(baseActivity);
        init();
    }

    public void init() {

    }

    @Override
    public void prepareSoundAndMusic() {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        return false;
    }

}
