package model;

import java.sql.Date;

/**
 * Un enregistrement de la table PURCHASE_ORDER
 * @author Thida
 */
public class PurchaseEntity {

	private int numP;
        private int customerP;
        private String productP;
	private float priceP;
	private int qteP;
        private float costP;
	private Date salesP;
	private Date shippingP;
	private String companyP;

	public PurchaseEntity(int numP, int customerP, String productP, float priceP, int qteP, float costP, Date salesP, Date shippingP, String companyP) {
		this.numP = numP;
		this.customerP = customerP;
                this.productP = productP;
		this.priceP = priceP;
		this.qteP = qteP;
                this.costP = costP;
		this.salesP = salesP;
		this.shippingP = shippingP;
		this.companyP = companyP;
	}

	public int getNumP() {
		return numP;
	}
        
        public int getCustomerP() {
		return customerP;
	}
        
        public String getProductP() {
		return productP;
	}

	public float getPriceP() {
		return priceP;
	}
        
        public int getQteP() {
		return qteP;
	}
        
        public float getCostP() {
		return costP;
	}

	public Date getSalesP() {
		return salesP;
	}
        
        public Date getShippingP() {
		return shippingP;
	}  
        
        public String getCompanyP() {
		return companyP;
	}

}
