package com.andengine.hosshan.basicgame.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by shunhosaka on 2015/03/29.
 */
public class ResourceUtil {

    // 自身のインスタンス
    private static ResourceUtil self;
    // Context
    private static BaseGameActivity gameActivity;
    // TextureRegionの無駄な生成を防ぎ、再利用する為の一時的な格納場所
    private static HashMap<String, ITextureRegion> textureRegionPool;
    // TiledTextureRegionの無駄な生成を防ぎ、再利用する為の一時的な格納場所
    private static HashMap<String, TiledTextureRegion> tiledTextureRegionPool;

    // TiledTextureRegionの無駄な生成を防ぎ、再利用する為の一時的な格納場所 private static HashMap<String, TiledTextureRegion> tiledTextureRegionPool;


    // イニシャライズ
    public static ResourceUtil getInstance(BaseGameActivity gameActivity) {
        if (self == null) {
            self = new ResourceUtil();
            ResourceUtil.gameActivity = gameActivity;
            BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
            textureRegionPool = new HashMap<String, ITextureRegion>();
            tiledTextureRegionPool = new HashMap<String, TiledTextureRegion>();
        }
        return self;
    }

    // ファイル名を与えてSpriteを得る
    public Sprite getSprite(String fileName) {
// 同名のファイルからITextureRegionが生成済みであれば再利用
        if (textureRegionPool.containsKey(fileName)) {
            Sprite s = new Sprite(0, 0, textureRegionPool.get(fileName),
                    gameActivity.getVertexBufferObjectManager());
            s.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            return s;
        }
        // サイズを自動的に取得する為にBitmapとして読み込み InputStream is = null;
        InputStream is = null;
        try {
            is = gameActivity.getResources().getAssets().open("gfx/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(is);
        // Bitmapのサイズを基に2のべき乗の値を取得、BitmapTextureAtlasの生成
        BitmapTextureAtlas bta = new BitmapTextureAtlas(
                gameActivity.getTextureManager(), getTwoPowerSize(bm.getWidth()),
                getTwoPowerSize(bm.getHeight()),
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        gameActivity.getEngine().getTextureManager().loadTexture(bta);
        ITextureRegion btr = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta, gameActivity, fileName, 0, 0);
        Sprite s = new Sprite(0, 0, btr, gameActivity.getVertexBufferObjectManager());
        s.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        // 再生成を防ぐ為、プールの登録 textureRegionPool.put(fileName, btr);
        return s;
    }

    // パラパラアニメのようなSpriteを生成。
    // 画像は一枚にまとめ、マス数と共に引き数とする
    public AnimatedSprite getAnimatedSprite(String fileName, int column, int row) {
        if (tiledTextureRegionPool.containsKey(fileName)) {
            AnimatedSprite s = new AnimatedSprite(
                    0, 0, tiledTextureRegionPool.get(fileName),
                    gameActivity.getVertexBufferObjectManager());
            s.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            return s;
        }
        InputStream is = null;
        try {
            is = gameActivity.getResources().getAssets().open("gfx/" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(is);
        BitmapTextureAtlas bta = new BitmapTextureAtlas(
                gameActivity.getTextureManager(), getTwoPowerSize(bm.getWidth()),
                getTwoPowerSize(bm.getHeight()),
                TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        gameActivity.getTextureManager().loadTexture(bta);
        // TiledTextureRegion(タイル状のTextureRegion)を生成。
        // マス数を与え、同じサイズのTextureRegionを用意
        TiledTextureRegion ttr = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bta, gameActivity, fileName, 0, 0, column, row);
        // AnimatedSpriteを生成
        AnimatedSprite s = new AnimatedSprite(0, 0, ttr, gameActivity.getVertexBufferObjectManager());
        s.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        tiledTextureRegionPool.put(fileName, ttr);
        return s;
    }

    // タップすると画像が切り替わるボタン機能を持つSpriteを生成。
    public ButtonSprite getButtonSprite(String normal, String pressed) {
        if (textureRegionPool.containsKey(normal) && textureRegionPool.containsKey(pressed)) {
            ButtonSprite s = new ButtonSprite(
                    0, 0, textureRegionPool.get(normal),
                    textureRegionPool.get(pressed),
                    gameActivity.getVertexBufferObjectManager());
            s.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            return s;
        }
        InputStream is = null;
        try {
            is = gameActivity.getResources().getAssets().open("gfx/" + normal);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(is);
        // ボタン生成の為のTextureRegion生成。
        // TiledTextureRegionでなく、BuildableBitmapTextureAtlasを利用する.
        BuildableBitmapTextureAtlas bta = new BuildableBitmapTextureAtlas(gameActivity.getTextureManager(), getTwoPowerSize(bm.getWidth() * 2), getTwoPowerSize(bm.getHeight()));
        ITextureRegion trNormal = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta, gameActivity, normal);
        ITextureRegion trPressed = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bta, gameActivity, pressed);
        try {
            bta.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
            bta.load();
        } catch (ITextureAtlasBuilder.TextureAtlasBuilderException e) {
            Debug.e(e);
        }
        textureRegionPool.put(normal, trNormal);
        textureRegionPool.put(pressed, trPressed);
        ButtonSprite s = new ButtonSprite(0, 0, trNormal, trPressed, gameActivity.getVertexBufferObjectManager());
        s.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        return s;
    }


    // プールを開放、シングルトンを削除する為の関数
    public void resetAllTexture() {
        // Activity.finish()たけたとシンクルトンなクラスかnullにならない為
        // 明示的にnullを代入
        self = null;
        textureRegionPool.clear();
        tiledTextureRegionPool.clear();
    }

    // 2のへき乗の値を求める
    public int getTwoPowerSize(float size) {
        int value = (int) (size + 1);
        int pow2value = 64;
        while (pow2value < value) {
            pow2value *= 2;
        }
        return pow2value;
    }
}
