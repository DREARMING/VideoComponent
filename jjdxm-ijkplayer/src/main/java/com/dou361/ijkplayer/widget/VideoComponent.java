package com.dou361.ijkplayer.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dou361.ijkplayer.R;
import com.dou361.ijkplayer.bean.Position;
import com.dou361.ijkplayer.bean.Resolution;
import com.dou361.ijkplayer.bean.VideoItem;
import com.dou361.ijkplayer.listener.OnShowThumbnailListener;
import com.dou361.ijkplayer.utils.VideoConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class VideoComponent extends FrameLayout {

    private Context context;
    private PlayerView playerView;
    private Activity mActivity;
    private int initHeight;
    private int initWidth;
    private int marginLeft;
    private int marginTop;
    private Executor threadPool;

    public VideoComponent(@NonNull Context context) {
        super(context);
    }

    public VideoComponent(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        this.context = context;
        initView(context);
    }

    public VideoComponent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VideoComponent(@NonNull Activity activity, @NonNull Context context){
        super(context);
        this.mActivity = activity;
        this.context = context;
       // initView(context);
    }

    public void setThreadPool(Executor threadPool){
        this.threadPool = threadPool;
    }

    private VideoConfiguration getConfiguration() {
        VideoConfiguration configuration = new VideoConfiguration();
        configuration.setVideoId("Video1");

        //位置
        Position position = new Position();
        position.setLeft(100);
        position.setTop(50);
        position.setWidth(320);
        position.setHeight(180);

        List<VideoItem> playList = new ArrayList<>(1);
        VideoItem item = new VideoItem();

        Resolution resolutionH = new Resolution();
        resolutionH.setStream("高清");
        resolutionH.setSelect(false);
        resolutionH.setUrl("rtmp://live.hkstv.hk.lxdns.com/live/hks");
        resolutionH.setVideoTitle("香港卫视高清频道");

        Resolution resolutionB = new Resolution();
        resolutionB.setStream("蓝光");
        resolutionB.setSelect(true);
        resolutionB.setUrl("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        resolutionB.setVideoTitle("香港卫视蓝光频道");

        Resolution resolutionB4 = new Resolution();
        resolutionB4.setStream("蓝光4M");
        resolutionB4.setSelect(false);
        resolutionB4.setUrl("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4");
        resolutionB4.setVideoTitle("香港卫视蓝光4M频道");

        List<Resolution> resolutionList = new ArrayList<>();
        resolutionList.add(resolutionH);
        resolutionList.add(resolutionB);
        resolutionList.add(resolutionB4);

        item.setResolutions(resolutionList);

        item.setVideoUrl("rtmp://live.hkstv.hk.lxdns.com/live/hks");
        item.setVideoTitle("香港卫视");
        playList.add(item);

        //属性
        configuration.setPlayList(playList);
        configuration.setPosition(position);

        configuration.setBottombarVisible(true);

        //全屏显示有问题，因为父布局添加View时，约束了宽高。
        configuration.setFullScreenBtVisible(true);

        configuration.setTopbarVisible(true);
        configuration.setBackBtVisible(true);

        configuration.setMultiResolutionEnable(true);

        configuration.setAudioEnable(true);

        //configuration.setForbidHideControlPanl(true);

        configuration.setMobileNetworkEnable(true);

        //scaleType 属性未添加

        //点播的时候才能生效，已进行进行点播测试
        //configuration.setMaxPlayTime(5);

        configuration.setBrightnessControlEnable(true);
        //configuration.setVolumnControlEnable(true);

        //configuration.setOnlyFullScreen(true);

        //当volumn不可以控制的时候，菜单却提供了设置。
        configuration.setMenuBtVisible(true);

        //webview mode 的作用

        //选集

        return configuration;
    }


    public void onConfigurationChanged(Configuration newConfig){
        if(playerView != null) {
            doConfigurationChanged(newConfig);
        }
    }

    private boolean isPortrait = true;

    private LayoutParams originalParams;


    private void doConfigurationChanged(Configuration newConfig){
        isPortrait = newConfig.orientation == Configuration.ORIENTATION_PORTRAIT;
        if(isPortrait){
            LayoutParams params = (LayoutParams) getLayoutParams();

            if(originalParams == null){
                originalParams = new LayoutParams(0,0);
                copyLayoutParms(originalParams, params);
            }

            copyLayoutParms(params,originalParams);

           /* params.height = initHeight;
            params.width = initWidth;
            params.topMargin = marginTop;
            params.leftMargin = marginLeft;*/
            setLayoutParams(params);
        }else{
            LayoutParams params = (LayoutParams) getLayoutParams();
            /*if(initHeight == 0)
                initHeight = params.height;
            if(initWidth == 0)
                initWidth = params.width;
            if(marginLeft == 0)
                marginLeft = params.leftMargin;
            if(marginTop == 0)
                marginTop = params.topMargin;*/
            if(originalParams == null){
                originalParams = new LayoutParams(0,0);
                copyLayoutParms(originalParams,params);
            }

            int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            params.height = Math.min(screenHeight,screenWidth);
            params.width = Math.max(screenHeight,screenWidth);
            params.leftMargin = 0;
            params.topMargin = 0;
            params.rightMargin = 0;
            params.bottomMargin = 0;
            setLayoutParams(params);
        }

        playerView.onConfigurationChanged(newConfig);
    }

    private void copyLayoutParms(LayoutParams originalParams , LayoutParams params){
        originalParams.height = params.height;
        originalParams.width = params.width;
        originalParams.bottomMargin = params.bottomMargin;
        originalParams.topMargin = params.topMargin;
        originalParams.leftMargin = params.leftMargin;
        originalParams.rightMargin = params.rightMargin;
    }

    public void setActivity(Activity activity){
        mActivity = activity;
    }

    public void apply(VideoConfiguration config){
        if(config.getPosition() == null || TextUtils.isEmpty(config.getVideoId())
                || config.getPlayList() == null || config.getPlayList().size()<=0){
            throw new IllegalArgumentException("VideoId、position and playList must not be null by setting VideoComponent!!");
        }
        View videoRootView = LayoutInflater.from(context).inflate(R.layout.view_videocomponent, this);
        playerView = new PlayerView(mActivity,videoRootView,threadPool);

        playerView.audioEnable(config.isAudioEnable());
        playerView.videoEnable(config.isVideoEnable());

        //底部栏是否可见
        playerView.hideBottonBar(!config.isBottombarVisible());

        //顶部栏是否可见
        playerView.hideHideTopBar(!config.isTopbarVisible());

        //滑动控制亮度和声音
        if(config.isForbitTouch()){
            playerView.forbidTouch(true);
        }else{
            playerView.forbidTouch(false);
            playerView.setBrightnessControlEnable(config.isBrightnessControlEnable());
            playerView.setVolumnControlEnable(config.isVolumnControlEnable());
        }

        //分辨率选择
        if(!config.isMultiResolutionEnable()){
            playerView.hideSteam(true);
            playerView.setMultiResolutionEnable(false);
        }else{
            playerView.hideSteam(false);
            playerView.setMultiResolutionEnable(true);
        }

        //注意先设置 multiResolution 后再设置 playSource
        List<VideoItem> playList = config.getPlayList();
        playerView.setPlaySource(playList);

        //全屏按钮是否显示
        if(config.isFullScreenBtVisible()){
            playerView.hideFullscreen(false);
            playerView.setForbidDoulbeUp(false);
        }else{
            playerView.hideFullscreen(true);
            playerView.setForbidDoulbeUp(true);
        }

        playerView.setNetWorkTypeTie(config.isMobileNetworkEnable());
        playerView.hideRotation(!config.isRotateBtVisible());

        playerView.hideMenu(!config.isMenuBtVisible());
        playerView.hideCenterPlayer(true);

        playerView.hideBack(!config.isBackBtVisible());
        playerView.showThumbnail(new OnShowThumbnailListener() {
            @Override
            public void onShowThumbnail(ImageView ivThumbnail) {

            }
        });

        playerView.setForbidHideControlPanl(config.isForbidHideControlPanl());

        if(config.getMaxPlayTime() < 0){
            playerView.setChargeTie(false,0);
        }else{
            playerView.setChargeTie(true, config.getMaxPlayTime());
        }

        playerView.setOnlyFullScreen(config.isOnlyFullScreen());


       // playerView.setChargeTie(true,200);
        // 不提供
        // playerView.setAutoReConnect();

        //暂未实现
        //playerView.setShowSpeed();
    }


    public void startPlay(){
        playerView.startPlay();
    }


    public void switchVideo(VideoItem item){
        playerView.setPlaySource(item);
        playerView.startPlay();
    }

    public void closeVideo(){
        playerView.stopPlay();
    }

    public void pauseVideo(){
        playerView.pausePlay();
    }


    // ---------------- 内部方法 ---------------------------

    //http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4
    //rtmp://live.hkstv.hk.lxdns.com/live/hks

    private void initView(Context context){
       /* List<VideoijkBean> videoList = new ArrayList<>();
        VideoijkBean video1 = new VideoijkBean();
        video1.setUrl("rtmp://live.hkstv.hk.lxdns.com/live/hks");
        video1.setSelect(false);
        video1.setStream("live");

         VideoijkBean video2 = new VideoijkBean();
        video2.setUrl("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4");
        video2.setSelect(true);
        video2.setStream("http");

        videoList.add(video2);
        videoList.add(video1);*/

        //View videoRootView = LayoutInflater.from(context).inflate(R.layout.view_videocomponent, this);
        //playerView = new PlayerView(mActivity, videoRootView, threadPool);

        /*playerView = new PlayerView(mActivity, videoRootView, threadPool)
                .setNetWorkTypeTie(false)
                .setTitle("香港卫视")
                .hideHideTopBar(true)
                .hideFullscreen(true)
                .setForbidDoulbeUp(true)
                .hideSteam(true)
                .hideRotation(true)
                .hideCenterPlayer(true)
                .setPlaySource("rtmp://live.hkstv.hk.lxdns.com/live/hks")
                .startPlay();*/
    }



    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        playerView.stopPlay();
    }
}
