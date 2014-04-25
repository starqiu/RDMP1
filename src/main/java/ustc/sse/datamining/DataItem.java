package ustc.sse.datamining;

import java.util.Date;

public class DataItem {
    private int userid;
    private int brandid;
    private int type;
    private Date date;
    private int preference;
    
    /**
     * @return the preference
     */
    public int getPreference() {
        return preference;
    }

    /**
     * @param preference the preference to set
     */
    public void setPreference(int preference) {
        this.preference = preference;
    }

    /**
     * @return the userid
     */
    public int getUserid() {
        return userid;
    }
    
    /**
     * @param userid the userid to set
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
     * @param brandid the brandid to set
     */
    public void setBrandid(int brandid) {
        this.brandid = brandid;
    }
    
    /**
     * @return the type
     */
    public int getType() {
        return type;
    }
    
    /**
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }
    
    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + brandid;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + preference;
        result = prime * result + type;
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
        if (!(obj instanceof DataItem)) return false;
        DataItem other = (DataItem) obj;
        if (brandid != other.brandid) return false;
        if (userid != other.userid) return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DataItem [userid=" + userid
               + ", brandid="
               + brandid
               + ", type="
               + type
               + ", date="
               + date
               + ", preference="
               + preference
               + "]";
    }
    
}
