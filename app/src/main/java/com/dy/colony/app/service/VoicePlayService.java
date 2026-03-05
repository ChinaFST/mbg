package com.dy.colony.app.service;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.jess.arms.base.BaseService;

public class VoicePlayService extends BaseService {
    private MediaPlayer mPlayer;

    public VoicePlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
       return new VoicePlayBinder();
    }

    @Override
    public void init() {

    }


    public class VoicePlayBinder extends Binder {
        public VoicePlayService getService(){
            return VoicePlayService.this;
        }
    }

    /**
     * @param index 播放语音的位置 1-7
     */
   public void playVoice(int index){

   }
}