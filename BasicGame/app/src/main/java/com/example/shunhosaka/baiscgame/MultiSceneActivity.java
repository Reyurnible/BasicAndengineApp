package com.example.shunhosaka.baiscgame;

import org.andengine.ui.activity.SimpleLayoutGameActivity;

import java.util.ArrayList;

/**
 * Created by shunhosaka on 2015/03/29.
 */
public abstract class MultiSceneActivity extends SimpleLayoutGameActivity {

    // ResourceUtilのインスタンス
    private ResourceUtil mResourceUtil;
    // 起動済みのSceneの配列
    private ArrayList<KeyListenScene> mSceneArray;

    @Override
    protected void onCreateResources() {
        mResourceUtil = ResourceUtil.getInstance(this);
        mSceneArray = new ArrayList<>();
    }

    public ResourceUtil getResourceUtil() {
        return mResourceUtil;
    }

    public ArrayList<KeyListenScene> getSceneArray() {
        return mSceneArray;
    }

    // 起動済みのKeyListenSceneを格納する配列
    public abstract void appendScene(KeyListenScene scene);
    // 最初のシーンに戻るための関数
    public abstract void backToInitial();
    // シーンとシーン格納配列を更新する関数
    public abstract void refreshRenningScene(KeyListenScene scene);

}
