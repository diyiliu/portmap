package com.diyiliu.util;

import javax.swing.*;
import java.awt.*;

/**
 * Description: UIHelper
 * Author: DIYILIU
 * Update: 2017-07-13 09:18
 */
public class UIHelper {


    /**
     * 设置窗口居中显示
     *
     * @param container
     */
    public static void setCenter(Container container){

        //设置窗口居中
        int WIDTH = container.getWidth();
        int HEIGHT = container.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screamSize = kit.getScreenSize();
        container.setBounds((screamSize.width - WIDTH) / 2, (screamSize.height - HEIGHT) / 2, WIDTH, HEIGHT);
    }


    /**
     * 样式美化
     */
    public static void beautify(){
        try {
            // 设置样式
            String ui = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(ui);
            //UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
