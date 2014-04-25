package ustc.sse.datamining;

import java.util.Arrays;
import java.util.LinkedList;

public class ItemWithUsersData {
	private int productID = 0;
	private double click2purchase = 0;
	private int itemActionCount[] = { 0, 0, 0, 0 };
	private int weight = 0;

	private LinkedList<User> users = new LinkedList<User>();


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(click2purchase);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Arrays.hashCode(itemActionCount);
		result = prime * result + productID;
		result = prime * result + ((users == null) ? 0 : users.hashCode());
		result = prime * result + weight;
		return result;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemWithUsersData other = (ItemWithUsersData) obj;
		if (Double.doubleToLongBits(click2purchase) != Double
				.doubleToLongBits(other.click2purchase))
			return false;
		if (!Arrays.equals(itemActionCount, other.itemActionCount))
			return false;
		if (productID != other.productID)
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}



	/**
	 * @return the productID
	 */
	public int getProductID() {
		return productID;
	}



	/**
	 * @return the click2purchase
	 */
	public double getClick2purchase() {
		return click2purchase;
	}



	/**
	 * @return the itemActionCount
	 */
	public int[] getItemActionCount() {
		return itemActionCount;
	}



	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}



	/**
	 * @return the users
	 */
	public LinkedList<User> getUsers() {
		return users;
	}



	/**
	 * @param productID the productID to set
	 */
	public void setProductID(int productID) {
		this.productID = productID;
	}



	/**
	 * @param click2purchase the click2purchase to set
	 */
	public void setClick2purchase(double click2purchase) {
		this.click2purchase = click2purchase;
	}



	/**
	 * @param itemActionCount the itemActionCount to set
	 */
	public void setItemActionCount(int[] itemActionCount) {
		this.itemActionCount = itemActionCount;
	}



	/**
	 * @param weight the weight to set
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}



	/**
	 * @param users the users to set
	 */
	public void addUserToList(User user) {
		this.users.add(user);
	}


	public void setUsers(LinkedList<User> users) {
		this.users = users;
	}


	// inner class.
	class User {
		private int userID = 0;
		private int type = 0;
		private java.sql.Date visitDaytime;
		/**
		 * @return the userID
		 */
		public int getUserID() {
			return userID;
		}
		/**
		 * @return the type
		 */
		public int getType() {
			return type;
		}
		/**
		 * @return the visitDaytime
		 */
		public java.sql.Date getVisitDaytime() {
			return visitDaytime;
		}
		/**
		 * @param userID the userID to set
		 */
		public void setUserID(int userID) {
			this.userID = userID;
		}
		/**
		 * @param type the type to set
		 */
		public void setType(int type) {
			this.type = type;
		}
		/**
		 * @param visitDaytime the visitDaytime to set
		 */
		public void setVisitDaytime(java.sql.Date visitDaytime) {
			this.visitDaytime = visitDaytime;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + type;
			result = prime * result + userID;
			result = prime * result
					+ ((visitDaytime == null) ? 0 : visitDaytime.hashCode());
			return result;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			User other = (User) obj;
			if (type != other.type)
				return false;
			if (userID != other.userID)
				return false;
			if (visitDaytime == null) {
				if (other.visitDaytime != null)
					return false;
			} else if (!visitDaytime.equals(other.visitDaytime))
				return false;
			return true;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "User [userID=" + userID + ", type=" + type
					+ ", visitDaytime=" + visitDaytime + "]";
		}



	}// end of inner class Product



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Item [productID=" + productID + ", click2purchase="
				+ click2purchase + ", itemActionCount="
				+ Arrays.toString(itemActionCount) + ", weight=" + weight
				+ ", users=" + users + "]";
	}



}
