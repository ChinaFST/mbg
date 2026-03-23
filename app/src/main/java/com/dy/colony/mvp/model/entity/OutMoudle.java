package com.dy.colony.mvp.model.entity;

/**
 * @author luoyl
 * @desc
 * @date 2026/3/12
 */
public class OutMoudle <V> {
    private V key;

    public OutMoudle(V key) { //泛型构造方法形参key的类型也为T，T的类型由外部指定
        this.key = key;
    }

    public V getKey(){ //泛型方法getKey的返回值类型为T，T的类型由外部指定
        return key;
    }
} 
