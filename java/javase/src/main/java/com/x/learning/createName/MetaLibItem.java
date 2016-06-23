package com.x.learning.createName;

/**
 * Date : 2016-05-21
 */
/**汉字五行笔画类
 *
 * @author luozhuang 大师♂罗莊
 */
public class MetaLibItem {

    private int bh;//笔画
    private String findStr;
    private int wx_indx;//五行

    /**
     *
     * @param bh 笔画
     * @param wx_indx 五行顺序
     * @param findStr //汉字列
     */
    public MetaLibItem(int bh, int wx_indx, String findStr) {
        this.bh = bh;
        this.wx_indx = wx_indx;
        this.findStr = findStr;
    }

    /**
     *
     * @return 获得笔画
     */
    public int getBh() {
        return this.bh;
    }

    /**
     * 获得汉字列
     *
     * @return
     */
    public String getFindStr() {
        return this.findStr;
    }

    /**
     * 获得汉字是否存在列表中
     *
     * @return
     */
    public Boolean IfStringexist(String findStr) {
        if (this.findStr.indexOf(findStr) == -1) {
            return false;
        }
        return true;
    }

    /**
     * 获得汉字是否存在列表中
     *
     * @return
     */
    public Boolean IfStringexist(char findStr) {
        if (this.findStr.indexOf(findStr) == -1) {
            return false;
        }
        return true;
    }

    /**
     * 获得五行顺序
     *
     * @return
     */
    public int getWx_indx() {
        return this.wx_indx;
    }
}
