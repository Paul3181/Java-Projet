package model;

/**
 * Un enregistrement de la table PRODUCT
 * @author Thida
 */
public class ProductEntity {

	private String product;
	private float price;
	private int qte;
	private float rate;
        private int id;

	public ProductEntity(String product, float price, int qte, float rate, int id) {
		this.product = product;
		this.price = price;
		this.qte = qte;
		this.rate = rate;
                this.id = id;
	}


	public String getProduct() {
		return product;
	}

	public float getPrice() {
		return price;
	}
        
        public int getQte() {
		return qte;
	}
        
        public float getRate() {
		return rate;
	}
        
        public int getId() {
            return id;
        }

}
