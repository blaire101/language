package com.x.learning.createName;

import java.util.ArrayList;

/**取汉字五行笔画演示
 *
 * @author luozhuang 大师♂罗莊
 */
public class Luozhuangnamewuxing {

    BhWxLib myBhWxLib = new BhWxLib();

    /**
     * 取出名字每个字在库中位置
     *
     * @param name
     * @return
     */
    public int[] getnameliborder(String name) {
        char[] namechararray = name.toCharArray();
        int[] returnarray = new int[namechararray.length];
        for (int i = 0; i < namechararray.length; i++) {
            int order = getnameliborder(namechararray[i]);
            returnarray[i] = order;
        }
        return returnarray;

    }

    /**
     * 取出字在库中位置
     *
     * @param name
     * @return
     */
    public int getnameliborder(char name) {
        ArrayList<MetaLibItem> libs = myBhWxLib.getLibs();

        int returnorder = -1;

        returnorder = myBhWxLib.getStringLibs(name);

        return returnorder;

    }

    /**
     * 取出名字笔画
     *
     * @param name
     * @return
     */
    public int getnameBH(int name) {
        ArrayList<MetaLibItem> libs = myBhWxLib.getLibs();

        int returnorder = -1;

        returnorder = libs.get(name).getBh();

        return returnorder;

    }

    /**
     * 取出名字五行
     *
     * @param name
     * @return
     */
    public int getnameWX(int name) {
        ArrayList<MetaLibItem> libs = myBhWxLib.getLibs();

        int returnorder = -1;

        returnorder = libs.get(name).getWx_indx();

        return returnorder;

    }

    /**
     * 取出名字笔画
     *
     * @param name
     * @return
     */
    public int[] getnameBH(int name[]) {
        int[] returnarray = new int[name.length];
        for (int i = 0; i < name.length; i++) {
            returnarray[i] = getnameBH(name[i]);

        }
        return returnarray;
    }

    /**
     * 取出名字五行
     *
     * @param name
     * @return
     */
    public int[] getnameWX(int name[]) {
        int[] returnarray = new int[name.length];
        for (int i = 0; i < name.length; i++) {
            returnarray[i] = getnameWX(name[i]);

        }
        return returnarray;

    }

    /**
     * 取出名字五行汉字
     *
     * @param name
     * @return
     */
    public String[] getnameWXarray(int name[]) {
        String[] returnarray = new String[name.length];
        for (int i = 0; i < name.length; i++) {
            returnarray[i] = getnameWXnaying(name[i]);

        }
        return returnarray;

    }

    /**
     * 取出名字五行汉字
     *
     * @param name
     * @return
     */
    public String getnameWXnaying(int name) {
        switch (name) {
            default:
                return "无";
            case 0:

                return "木";

            case 1:
                return "火";

            case 2:
                return "土";

            case 3:
                return " 金";

            case 4:
                return "水";
        }

    }

    public String pringst(int[] res) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < res.length; i++) {


            result.append(res[i]);
            result.append("   ");
        }

        result.append("\n");
        System.out.println(result);
        return result.toString();
    }

    public String pringst(String[] res) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < res.length; i++) {


            result.append(res[i]);
            result.append("   ");
        }

        result.append("\n");
        System.out.println(result);
        return result.toString();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("五行简介:\n"
                + "\n"
                + "中国五行学说认为宇宙万物，都由金木水火土五种基本物质的运行和变化所构成，所有事物都是随着这五个要素的盛衰，而使得大自然产生变化，不但影响到人的命运，同时也使宇宙万物循环不已。\n"
                + "\n"
                + "其中：\n"
                + "“木曰曲直”，意思是木具有生长、升发的特性；\n"
                + "“火曰炎上”，是火具有发热、向上的特性；\n"
                + "“土爰稼墙”，是指土具有种植庄稼，生化万物的特性；\n"
                + "“金曰从革”，是金具有肃杀、变革的特性；\n"
                + "“水曰润下”，是水具有滋润、向下的特性\n"
                + "五行相生相克\n"
                + "\n"
                + "五行相生：金生水，水生木，木生火，火生土，土生金。\n"
                + "\n"
                + "五行相克：金克木，木克土，土克水，水克火，火克金。 ");
        System.out.println("中国汉字有那五行？\n"
                + "根据中国五行理论，汉字分为金、木、水、火、土共五种五行属性，每个汉字对应一种五行属性。");
        Luozhuangnamewuxing my = new Luozhuangnamewuxing();

        String name = "陈泊羊";

        int[] temp = my.getnameliborder(name);
        int[] wuxing = my.getnameWX(temp);
        int[] BH = my.getnameBH(temp);
        System.out.println(name + "的五行");
        my.pringst(my.getnameWXarray(wuxing));
        System.out.println(name + "的笔画");
        my.pringst(BH);


    }
}