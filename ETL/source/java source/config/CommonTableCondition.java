package com.dbs.sg.DTE12.config;

import com.ibm.etools.xmlschema.beans.ComplexType;

/**
* This class represents the complex type <TableList>
*/
public class CommonTableCondition extends ComplexType
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -3909687318115159348L;

public CommonTableCondition()
  {
  }
  
  public String getWhereForReport()
  {
    return getElementValue("whereForReport");
  }

  public String getWhereForDatabox()
  {
    return getElementValue("whereForDatabox");
  }
  
}

