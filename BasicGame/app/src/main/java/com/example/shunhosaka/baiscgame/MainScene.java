package com.example.shunhosaka.baiscgame;

import android.view.KeyEvent;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseActivity;

import javax.microedition.khronos.opengles.GL10;

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
        attachChild(getBaseActivity().getResourceUtil().getSprite("main_bg.png"));
    }

    @Override
    public void prepareSoundAndMusic() {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        return false;
    }

}
