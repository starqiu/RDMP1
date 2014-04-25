package ustc.sse.datamining;

public class userItemScalar {

 int userId;
 int itemId;
 double scalar;
 double numDay;
 
public int getUserId() {
	return userId;
}
public void setUserId(int userId) {
	this.userId = userId;
}
public int getItemId() {
	return itemId;
}
public void setItemId(int itemId) {
	this.itemId = itemId;
}
public double getScalar() {
	return scalar;
}
public void setScalar(double scalar) {
	this.scalar = scalar;
}

public double getNumDay() {
	return numDay;
}
public void setNumDay(double numDay) {
	this.numDay = numDay;
}
@Override
public String toString() {
	return userId + "\t" + itemId
			+ "\t" + scalar+ "\t"+ numDay ;
}	

}
