package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;



public class DAO {

	private final DataSource myDataSource;

	/**
	 * Construit le AO avec sa source de données
	 * @param dataSource la source de données à utiliser
	 */
	public DAO(DataSource dataSource) {
		this.myDataSource = dataSource;
	}
        
        /**
         * Methode permettant de valider ou non les informations de connexion
         * transmises par le formulaire de login
         * checkUser recupère l'EMAIL et le CUSTOMER_ID et donne un username 
	 * @param email
         * @param pass
         * @return
         * @throws SQLException 
	 *
         */
        public void checkUser(HttpServletRequest request) throws SQLException{   
            
                String sql = "SELECT * FROM CUSTOMER WHERE EMAIL=? AND CUSTOMER_ID=?";
                String email = request.getParameter("email");
                String pass = request.getParameter("pass");
                boolean admin = false;
     
                //on teste si c'est un admin
                if(email.equals("admin") && pass.equals("master")) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute("userName", email);
                }
                else{
                    try (Connection connection = myDataSource.getConnection(); 
                        PreparedStatement stmt = connection.prepareStatement(sql)) {
                        boolean check = false;
                        int pass2 = 0;
    
                        //on cast le mot de passe si on peut et on test
                        if(pass.matches("[0-9]+")){
                            pass2 = Integer.parseInt(pass);
                            stmt.setString(1, email);
                            stmt.setInt(2, pass2);
                            ResultSet res = stmt.executeQuery();
                            //check est vrai si la requete retourne 1 resultat faux sinon
                            check = res.next();

                            if (check){
                                    HttpSession session = request.getSession(true);
                                    session.setAttribute("userName", email);
                            }
                            else{
                                request.setAttribute("errorMessage", "Identifiant ou mot de passe incorrect!");
                            }
                        }
                        //sinon mauvais mdp
                        else{
                            request.setAttribute("errorMessage", "Identifiant ou mot de passe incorrect!");
                        }   
                    }         
                }                 
        }
                           
        
        /**
	 * Recupère l'ID du customer dans la table CUSTOMER
	 * @param mail
	 * @return l'id du customer
	 * @throws SQLException
	 */
	public int customerId(String mail) throws SQLException {
		int result = 0;
		String sql = "SELECT DISTINCT CUSTOMER_ID FROM CUSTOMER WHERE EMAIL = ?";
                try (Connection connection = myDataSource.getConnection(); 
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
                        stmt.setString(1, mail);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
                            result = rs.getInt("CUSTOMER_ID");
			}
		}
		return result;
	}
        
        
         /**
	 * Liste des catégories dans la table PRODUCT_CODE
	 * @return la liste des catégories
	 * @throws SQLException
	 */
	public List<String> existingCategory() throws SQLException {
		List<String> result = new LinkedList<>();
		String sql = "SELECT DISTINCT DESCRIPTION FROM PRODUCT_CODE";
                try (Connection connection = myDataSource.getConnection(); 
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String category = rs.getString("DESCRIPTION");
				result.add(category);
			}
		}
		return result;
	}
                
        /**
	 * Liste des produits dans une catégorie
         * @param category
	 * @return Liste des produits
	 * @throws SQLException renvoyées par JDBC
	 */
	public List<ProductEntity> productsInCategory(String category) throws SQLException {
                List<ProductEntity> result = new ArrayList<ProductEntity>();
                //String sql = "SELECT * FROM PRODUCT AS P INNER JOIN PRODUCT_CODE AS PC ON P.PRODUCT_CODE = PC.PROD_CODE WHERE PC.DESCRIPTION = ?";
                String sql = "SELECT * FROM PRODUCT AS P INNER JOIN PRODUCT_CODE AS PC ON P.PRODUCT_CODE = PC.PROD_CODE INNER JOIN DISCOUNT_CODE AS DC ON PC.DISCOUNT_CODE = DC.DISCOUNT_CODE WHERE PC.DESCRIPTION = ?";
                try (Connection connection = myDataSource.getConnection(); 
		    PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, category); // Définir la valeur du paramètre
                    try (ResultSet rs = stmt.executeQuery()){
                        while(rs.next()){
                            String product = rs.getString("DESCRIPTION");
                            float price = rs.getFloat("PURCHASE_COST");
                            int qte = rs.getInt("QUANTITY_ON_HAND");
                            float rate = rs.getFloat("RATE");
                            int id = rs.getInt("PRODUCT_ID");
                            ProductEntity c = new ProductEntity(product, price, qte, rate, id);
                            result.add(c);
                        }
                    }
		}
		return result;
	}
        
        /**
	 * Supprime un enregistrement dans la table ORDERS
	 * @param numP la clé de l'enregistrement à supprimer
	 * @return le nombre d'enregistrements supprimés
	 * @throws java.sql.SQLException renvoyées par JDBC
	 **/
	public int deleteOrder(String numP) throws SQLException {
		int result = 0;
		String sql = "DELETE FROM PURCHASE_ORDER WHERE ORDER_NUM = ?";
		try (Connection connection = myDataSource.getConnection(); 
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, numP);
			result = stmt.executeUpdate();
		}
		return result;
	}
        
        /**
	 * Modifie la quantitée d'une commande dans la table ORDERS
	 * @param qte
         * @param num
	 * @throws java.sql.SQLException renvoyées par JDBC
	 **/
        public void modifyQte(int qte, int num) throws SQLException {

		String sql = "UPDATE PURCHASE_ORDER SET QUANTITY = ? WHERE ORDER_NUM = ?";
		try (Connection connection = myDataSource.getConnection(); 
		    PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, qte);
                    stmt.setInt(2, num);
                    stmt.executeUpdate();
		}
        }
        
	/**
	 * Insert une ligne dans la tanle PURCHASE_ORDER
	 * @param int num
         * @param int customer_id
	 * @param int quantity
	 * @param int cost
	 * @param Date sales
	 * @param Date shipping
	 * @throws java.sql.SQLException renvoyées par JDBC
	 **/
        public void insertPurchase(int num, int customer_id, int product_id, int quantity, int cost, Date sales, Date shipping, String company) {
            String sql = "INSERT INTO PURCHASE_ORDER (ORDER_NUM, CUSTOMER_ID, PRODUCT_ID, QUANTITY, SHIPPING_COST, SALES_DATE, SHIPPING_DATE, FREIGHT_COMPANY) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			Connection connection = myDataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql);
                        stmt.setInt(1, num);
			stmt.setInt(2, customer_id);
			stmt.setInt(3, product_id);
                        stmt.setInt(4, quantity);
			stmt.setInt(5, cost);
			stmt.setDate(6, sales);
                        stmt.setDate(7, shipping);
                        stmt.setString(8, company);
			stmt.executeUpdate();
			System.out.println("INSERT INTO");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}
       	/**
	 * Génère le prochain numero disponible pour une commande en récupérant le numero max de ORDER_NUM dans PURCHASE_ORDER + 1 
	 * @throws java.sql.SQLException renvoyées par JDBC
	 **/
        public int maxId() throws SQLException {
		int result = 0;
		String sql = "SELECT MAX(ORDER_NUM) FROM PURCHASE_ORDER";
                try (Connection connection = myDataSource.getConnection(); 
		     PreparedStatement stmt = connection.prepareStatement(sql)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
                            result = rs.getInt(1)+1;
			}
		}
		return result;
	}
        
  	/**
	 * Renvoie la liste de toutes commandes d'un client 
	 * @param customer
	 * @throws java.sql.SQLException renvoyées par JDBC
	 **/      
	
        public List<PurchaseEntity> allPurchase(int customer) throws SQLException {

		List<PurchaseEntity> result = new LinkedList<>();
		
		String sql = "SELECT * FROM PURCHASE_ORDER AS PO INNER JOIN PRODUCT AS P ON PO.PRODUCT_ID = P.PRODUCT_ID WHERE CUSTOMER_ID = ?";
                try (Connection connection = myDataSource.getConnection(); 
		    PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, customer);
                    ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int numP = rs.getInt("ORDER_NUM");
                                int customerP = rs.getInt("CUSTOMER_ID");
                                String productP = rs.getString("DESCRIPTION");
                                float priceP = rs.getFloat("PURCHASE_COST");
                                int qteP = rs.getInt("QUANTITY");
                                float costP = rs.getFloat("SHIPPING_COST");
				Date salesP = rs.getDate("SALES_DATE");
                                Date shippingP = rs.getDate("SHIPPING_DATE");
                                String companyP = rs.getString("FREIGHT_COMPANY");
				PurchaseEntity c = new PurchaseEntity(numP, customerP, productP, priceP, qteP, costP, salesP, shippingP, companyP);
                                result.add(c);
                        }
		}
		return result;
	}
        
  	/**
	 * Renvoie le chiffre d'affaire en fonction des catégories de produits et dans une période définie.
	 * @param String cat
	 * @param String datedebut
	 * @param String datefin
	 * @throws java.sql.SQLException renvoyées par JDBC
	 **/   	
	
        public double CabyCat(String cat, String datedebut, String datefin)throws SQLException{
            double result = 0;
            boolean reponse = false;
            String sql = "SELECT PRODUCT_CODE,SUM(PURCHASE_COST*QUANTITY) as CHIFFRE FROM APP.PURCHASE_ORDER,APP.PRODUCT WHERE APP.PRODUCT.PRODUCT_ID=APP.PURCHASE_ORDER.PRODUCT_ID and PRODUCT_CODE = ? and APP.PURCHASE_ORDER.SALES_DATE > ? and APP.PURCHASE_ORDER.SALES_DATE < ? GROUP BY PRODUCT_CODE" ;
            try (Connection connection = myDataSource.getConnection(); 
		    PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, cat);
                    stmt.setString(2, datedebut);
                    stmt.setString(3, datefin);
                    
                    ResultSet rs = stmt.executeQuery();
                        while(rs.next()) {
                        reponse = true;
                        result = rs.getDouble("CHIFFRE");
                        }
                    }    
                return result;
            }
        
	/**
	 * Renvoie le chiffre d'affaire en fonction des zones géographique et dans une période définie.
	 * @param String state
	 * @param String datedebut
	 * @param String datefin
	 * @throws java.sql.SQLException renvoyées par JDBC
	 **/   	
        public double CabyState(String state, String datedebut, String datefin)throws SQLException{
            double result = 0; 
            boolean reponse = false;
            String sql = "SELECT CUSTOMER.STATE, SUM(RES.CHIFFRE) as CHIFFRE2 FROM CUSTOMER,(SELECT CUSTOMER_ID,SUM(PURCHASE_COST*QUANTITY) as CHIFFRE FROM APP.PURCHASE_ORDER, APP.PRODUCT WHERE APP.PRODUCT.PRODUCT_ID=APP.PURCHASE_ORDER.PRODUCT_ID and APP.PURCHASE_ORDER.SALES_DATE > ? and APP.PURCHASE_ORDER.SALES_DATE < ? GROUP BY CUSTOMER_ID) as RES WHERE customer.customer_id = res.customer_id and state = ? group by CUSTOMER.STATE" ;
            try (Connection connection = myDataSource.getConnection(); 
		    PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, datedebut);
                    stmt.setString(2, datefin);
                    stmt.setString(3, state);
                    
                    ResultSet rs = stmt.executeQuery();
                        while(rs.next()) {
                        reponse = true;
                        result = rs.getDouble("CHIFFRE2");
                        }
                    }    
                return result;
            }
        /**
	 * Renvoie le chiffre d'affaire en fonction des client et dans une période définie.
	 * @param int customer
	 * @param String datedebut
	 * @param String datefin
	 * @throws java.sql.SQLException renvoyées par JDBC
	 **/   	
         public double CabyCli(int customer, String datedebut, String datefin)throws SQLException{
            double result = 0; 
            boolean reponse = false;
            String sql = "SELECT CUSTOMER_ID,SUM(PURCHASE_COST*QUANTITY) as CHIFFRE FROM APP.PURCHASE_ORDER, APP.PRODUCT WHERE APP.PRODUCT.PRODUCT_ID=APP.PURCHASE_ORDER.PRODUCT_ID and customer_id = ? and APP.PURCHASE_ORDER.SALES_DATE > ? and APP.PURCHASE_ORDER.SALES_DATE < ? GROUP BY CUSTOMER_ID" ;
            try (Connection connection = myDataSource.getConnection(); 
		    PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, customer);
                    stmt.setString(2, datedebut);
                    stmt.setString(3, datefin);
                    
                    ResultSet rs = stmt.executeQuery();
                        while(rs.next()) {
                        reponse = true;
                        result = rs.getDouble("CHIFFRE");
                        }
                    }    
                return result;
            }
        
      
}
