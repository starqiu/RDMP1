package ustc.sse.datamining;

import ustc.sse.datamining.UserWithItemsData.Product;

import java.util.Arrays;
import java.util.LinkedList;

public class UserWithUniqueBrandId {

	private int userID = 0;
	private LinkedList<ProductWithWeight> productWithWeight = new LinkedList<ProductWithWeight>();

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public LinkedList<ProductWithWeight> getProductWithWeight() {
		return productWithWeight;
	}

	public void setProductWithWeight(ProductWithWeight productWithWeight) {
		this.productWithWeight.add(productWithWeight);
	}

	@Override
	public String toString() {
		return "UserWithUniqueBrandId [userID=" + userID
				+ ", productWithWeight=" + productWithWeight + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((productWithWeight == null) ? 0 : productWithWeight
						.hashCode());
		result = prime * result + userID;
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
		UserWithUniqueBrandId other = (UserWithUniqueBrandId) obj;
		if (productWithWeight == null) {
			if (other.productWithWeight != null)
				return false;
		} else if (!productWithWeight.equals(other.productWithWeight))
			return false;
		if (userID != other.userID)
			return false;
		return true;
	}

	class ProductWithWeight {
		private int brandId = 0;
		private float weight = 0;

		public int getBrandId() {
			return brandId;
		}

		public void setBrandId(int brandId) {
			this.brandId = brandId;
		}

		public float getWeight() {
			return weight;
		}

		public void setWeight(float weight) {
			this.weight = weight;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + brandId;
			result = prime * result + Float.floatToIntBits(weight);
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
			ProductWithWeight other = (ProductWithWeight) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (brandId != other.brandId)
				return false;
			if (Float.floatToIntBits(weight) != Float
					.floatToIntBits(other.weight))
				return false;
			return true;
		}

		private UserWithUniqueBrandId getOuterType() {
			return UserWithUniqueBrandId.this;
		}

		@Override
		public String toString() {
			return "ProductWithWeight [brandId=" + brandId + ", weight="
					+ weight + "]";
		}

	}
}
