package com.dbs.sg.DTE12.config;

import com.ibm.etools.xmlschema.beans.ComplexType;

/**
* This class represents the complex type <Batchlist>
*/
public class Batchlist extends ComplexType
{
  /**
	 * 
	 */
	private static final long serialVersionUID = -4295254110056258364L;

public Batchlist()
  {
  }
  
  /**
  * This inner class represents the anonymous type <batch>
  * within the complex type <Batchlist>
  */
  static public class batch extends ComplexType
  {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5526935400652419807L;

	public batch()
    {
    }
    public void setId(String id)
    {
      setElementValue("id", id);
    }
    
    public String getId()
    {
      return getElementValue("id");
    }
    
    public boolean removeId()
    {
      return removeElement("id");
    }
    
    public void setOutputXmlFileList(OutputXMLFileList outputXmlFileList)
    {
      setElementValue("outputXmlFileList", outputXmlFileList);
    }
    
    public OutputXMLFileList getOutputXmlFileList()
    {
      return (OutputXMLFileList) getElementValue("outputXmlFileList", "OutputXMLFileList");
    }
    
    public boolean removeOutputXmlFileList()
    {
      return removeElement("outputXmlFileList");
    }
    
    public void setInputFileList(InputFileList inputFileList)
    {
      setElementValue("inputFileList", inputFileList);
    }
    
    public InputFileList getInputFileList()
    {
      return (InputFileList) getElementValue("inputFileList", "InputFileList");
    }
    
    public boolean removeInputFileList()
    {
      return removeElement("inputFileList");
    }
    
    public void setTableList(TableList tableList)
    {
      setElementValue("tableList", tableList);
    }
    
    public TableList getTableList()
    {
      return (TableList) getElementValue("tableList", "TableList");
    }
    
    public boolean removeTableList()
    {
      return removeElement("tableList");
    }
    
    public void setTargetTableList(TargetTableList targetTableList)
    {
      setElementValue("targetTableList", targetTableList);
    }
    
    public TargetTableList getTargetTableList()
    {
      return (TargetTableList) getElementValue("targetTableList", "TargetTableList");
    }
    
    public boolean removeTargetTableList()
    {
      return removeElement("targetTableList");
    }
    
    public CommonTableCondition getCommonTableCondition()
    {
      return (CommonTableCondition) getElementValue("commonTableCondition", "CommonTableCondition");
    }
    
  }
  public void setBatch(int index, Batchlist.batch batch)
  {
    setElementValue(index, "batch", batch);
  }
  
  public Batchlist.batch getBatch(int index)
  {
    return (Batchlist.batch) getElementValue("batch", "Batchlist$batch", index);
  }
  
  public int getBatchCount()
  {
    return sizeOfElement("batch");
  }
  
  public boolean removeBatch(int index)
  {
    return removeElement(index, "batch");
  }
  
}

