package com.xue;

import com.xue.arrangement.Config;
import com.xue.arrangement.WarningSettings;
import com.xue.communication.QtMutual;
import com.xue.frame.GeoFrame;

import java.net.SocketException;

/**
 * 程序入口
 *
 * @author Xue
 * @create 2024-04-28 10:12
 */
public class Main {

    public static void main(String[] args) throws SocketException, InterruptedException {
        // 加载配置文件信息
        Config.getInstance().loadConfig(args);
        WarningSettings.getInstance().loadFileSettings();
        // 加载geo框架
        GeoFrame.getInstance().initialize();
        // 加载Qt通讯相关
        QtMutual.getInstance().connect();
    }

}
