package ustc.sse.datamining;

import java.util.Date;

public class DetailsItem {
    private int  userid;
    private int  brandid;
    private Date data;

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    private int clicknum;
    private int marknum;
    private int buynum;

    /**
     * @return the userid
     */
    public int getUserid() {
        return userid;
    }

    /**
     * @param userid
     *            the userid to set
     */
    public void setUserid(int userid) {
        this.userid = userid;
    }

    /**
     * @return the brandid
     */
    public int getBrandid() {
        return brandid;
    }

    /**
     * @param brandid
     *            the brandid to set
     */
    public void setBrandid(int brandid) {
        this.brandid = brandid;
    }

    /**
     * @return the clicknum
     */
    public int getClicknum() {
        return clicknum;
    }

    /**
     * @param clicknum
     *            the clicknum to set
     */
    public void setClicknum(int clicknum) {
        this.clicknum = clicknum;
    }

    /**
     * @return the marknum
     */
    public int getMarknum() {
        return marknum;
    }

    /**
     * @param marknum
     *            the marknum to set
     */
    public void setMarknum(int marknum) {
        this.marknum = marknum;
    }

    /**
     * @return the buynum
     */
    public int getBuynum() {
        return buynum;
    }

    /**
     * @param buynum
     *            the buynum to set
     */
    public void setBuynum(int buynum) {
        this.buynum = buynum;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + brandid;
        result = prime * result + buynum;
        result = prime * result + clicknum;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + marknum;
        result = prime * result + userid;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof DetailsItem)) return false;
        DetailsItem other = (DetailsItem) obj;
        if (brandid != other.brandid) return false;
        if (buynum != other.buynum) return false;
        if (clicknum != other.clicknum) return false;
        if (data == null) {
            if (other.data != null) return false;
        } else if (!data.equals(other.data)) return false;
        if (marknum != other.marknum) return false;
        if (userid != other.userid) return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DetailsItem [userid=" + userid
               + ", brandid=" + brandid
               + ", data=" + data
               + ", clicknum=" + clicknum
               + ", marknum=" + marknum
               + ", buynum=" + buynum + "]";
    }
}
