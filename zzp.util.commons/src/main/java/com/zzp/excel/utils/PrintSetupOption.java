package com.zzp.excel.utils;

import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @Description 打印参数
 * @Author Garyzeng
 * @since 2019.10.15
 **/
public class PrintSetupOption {

    /**
     * 打印方向，true横向，false纵向
     */
    private Boolean landscape;

    /**
     * 打印纸张大小
     */
    private Short paperSize;

    /**
     * 页边距（上）
     */
    private Double topMargin;

    /**
     * 页边距（下）
     */
    private Double bottomMargin;

    /**
     * 页边距（左）
     */
    private Double leftMargin;

    /**
     * 页边距（右）
     */
    private Double rightMargin;

    /**
     * 页眉
     */
    private Double headerMargin;

    /**
     * 页脚
     */
    private Double footerMargin;

    /**
     * 打印页面为水平居中，true是，false否
     */
    private Boolean horizontallyCenter;

    /**
     * 打印页面为垂直居中，true是，false否
     */
    private Boolean verticallyCenter;

    /**
     * 设置打印标题
     */
    private CellRangeAddress repeatingRows;

    public Boolean getLandscape() {
        return landscape;
    }

    public void setLandscape(Boolean landscape) {
        this.landscape = landscape;
    }

    public Short getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(Short paperSize) {
        this.paperSize = paperSize;
    }

    public Double getTopMargin() {
        return topMargin;
    }

    public void setTopMargin(Double topMargin) {
        this.topMargin = topMargin;
    }

    public Double getBottomMargin() {
        return bottomMargin;
    }

    public void setBottomMargin(Double bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    public Double getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(Double leftMargin) {
        this.leftMargin = leftMargin;
    }

    public Double getRightMargin() {
        return rightMargin;
    }

    public void setRightMargin(Double rightMargin) {
        this.rightMargin = rightMargin;
    }

    public Double getHeaderMargin() {
        return headerMargin;
    }

    public void setHeaderMargin(Double headerMargin) {
        this.headerMargin = headerMargin;
    }

    public Double getFooterMargin() {
        return footerMargin;
    }

    public void setFooterMargin(Double footerMargin) {
        this.footerMargin = footerMargin;
    }

    public Boolean getHorizontallyCenter() {
        return horizontallyCenter;
    }

    public void setHorizontallyCenter(Boolean horizontallyCenter) {
        this.horizontallyCenter = horizontallyCenter;
    }

    public Boolean getVerticallyCenter() {
        return verticallyCenter;
    }

    public void setVerticallyCenter(Boolean verticallyCenter) {
        this.verticallyCenter = verticallyCenter;
    }

    public CellRangeAddress getRepeatingRows() {
        return repeatingRows;
    }

    public void setRepeatingRows(CellRangeAddress repeatingRows) {
        this.repeatingRows = repeatingRows;
    }
}
