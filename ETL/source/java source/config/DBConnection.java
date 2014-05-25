package com.dbs.sg.DTE12.config;

import com.ibm.etools.xmlschema.beans.ComplexType;

/**
* This class represents the complex type <DBConnection>
*/
public class DBConnection extends ComplexType
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -6189980874971779052L;

public DBConnection()
  {
  }
  
  public void setConnectionstring(String connectionstring)
  {
    setElementValue("connectionstring", connectionstring);
  }
  
  public String getConnectionstring()
  {
    return getElementValue("connectionstring");
  }
  
  public boolean removeConnectionstring()
  {
    return removeElement("connectionstring");
  }
  
  public void setUserid(String userid)
  {
    setElementValue("userid", userid);
  }
  
  public String getUserid()
  {
    return getElementValue("userid");
  }
  
  public boolean removeUserid()
  {
    return removeElement("userid");
  }
  
  public void setPasswordurl(String passwordurl)
  {
    setElementValue("passwordurl", passwordurl);
  }
  
  public String getPasswordurl()
  {
    return getElementValue("passwordurl");
  }
  
  public boolean removePasswordurl()
  {
    return removeElement("passwordurl");
  }
  
  public void setDBName(String DBName)
  {
    setElementValue("DBName", DBName);
  }
  
  public String getDBName()
  {
    return getElementValue("DBName");
  }
  
  public boolean removeDBName()
  {
    return removeElement("DBName");
  }

  public void setPageSize(String pageSize)
  {
    setElementValue("pageSize", pageSize);
  }
  
  public String getPageSize()
  {
    return getElementValue("pageSize");
  }
  
  public boolean removePageSize()
  {
    return removeElement("pageSize");
  }
}

