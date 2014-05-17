package ustc.sse.datamining;

import java.util.Arrays;
import java.util.LinkedList;

public class UserWithItemsData {
	private int userID = 0;
	private double click2purchase = 0;
	private int userActionCount[] = { 0, 0, 0, 0 };
	private int weight = 0;

	private LinkedList<Product> products = new LinkedList<Product>();

	/**
	 * @return the userID
	 */
	public int getUserID() {
		return userID;
	}

	/**
	 * @return the click2purchase
	 */
	public double getClick2purchase() {
		return click2purchase;
	}

	/**
	 * @return the userActionCount
	 */
	public int[] getUserActionCount() {
		return userActionCount;
	}

	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @return the products
	 */
	public LinkedList<Product> getProducts() {
		return products;
	}

	/**
	 * @param userID
	 *            the userID to set
	 */
	public void setUserID(int userID) {
		this.userID = userID;
	}

	/**
	 * @param click2purchase
	 *            the click2purchase to set
	 */
	public void setClick2purchase(double click2purchase) {
		this.click2purchase = click2purchase;
	}

	/**
	 * @param userActionCount
	 *            the userActionCount to set
	 */
	public void setUserActionCount(int[] userActionCount) {
		this.userActionCount = userActionCount;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * @param products
	 *            the products to set
	 */
	public void addProductToList(Product products) {
		this.products.add(products);
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(click2purchase);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((products == null) ? 0 : products.hashCode());
		result = prime * result + Arrays.hashCode(userActionCount);
		result = prime * result + userID;
		result = prime * result + weight;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserWithItemsData other = (UserWithItemsData) obj;
		if (Double.doubleToLongBits(click2purchase) != Double
				.doubleToLongBits(other.click2purchase))
			return false;
		if (products == null) {
			if (other.products != null)
				return false;
		} else if (!products.equals(other.products))
			return false;
		if (!Arrays.equals(userActionCount, other.userActionCount))
			return false;
		if (userID != other.userID)
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}



	// inner class.
	class Product {
		private int brandID = 0;
		private int type = 0;
		private java.util.Date visitDaytime;

		/**
		 * @return the brandID
		 */
		public int getBrandID() {
			return brandID;
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
		public java.util.Date getVisitDaytime() {
			return visitDaytime;
		}

		/**
		 * @param brandID
		 *            the brandID to set
		 */
		public void setBrandID(int brandID) {
			this.brandID = brandID;
		}

		/**
		 * @param type
		 *            the type to set
		 */
		public void setType(int type) {
			this.type = type;
		}

		/**
		 * @param visitDaytime
		 *            the visitDaytime to set
		 */
		public void setVisitDaytime(java.util.Date visitDaytime) {
			this.visitDaytime = visitDaytime;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + brandID;
			result = prime * result + type;
			result = prime * result
					+ ((visitDaytime == null) ? 0 : visitDaytime.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
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
			Product other = (Product) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (brandID != other.brandID)
				return false;
			if (type != other.type)
				return false;
			if (visitDaytime == null) {
				if (other.visitDaytime != null)
					return false;
			} else if (!visitDaytime.equals(other.visitDaytime))
				return false;
			return true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Product [brandID=" + brandID + ", type=" + type
					+ ", visitDaytime=" + visitDaytime + "]";
		}

		private UserWithItemsData getOuterType() {
			return UserWithItemsData.this;
		}

	}// end of inner class Product

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [userID=" + userID + ", click2purchase=" + click2purchase
				+ ", userActionCount=" + Arrays.toString(userActionCount)
				+ ", weight=" + weight + ", products=" + products + "]";
	}



}
