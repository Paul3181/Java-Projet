/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.DAO;
import model.DataSourceFactory;
import model.ProductEntity;


/**
 *
 * @author pauld
 */
@WebServlet(name = "login", urlPatterns = {"/login"})
public class Login extends HttpServlet {
    
    
    //private final DataSource myDataSource;
    /**
	 * Construit le AO avec sa source de données
	 * @param dataSource la source de données à utiliser
	 */
	//public Login(DataSource dataSource) {
	//	this.myDataSource = dataSource;
	//}
        

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
            response.setContentType("text/html;charset=UTF-8");
            
            String product = request.getParameter("product");
            String price = request.getParameter("price");
            String qte = request.getParameter("qte");
            String rate = request.getParameter("rate");
            String id = request.getParameter("id");
                
            String numP = request.getParameter("numP");
            String customerP = request.getParameter("customerP");
            String productP = request.getParameter("productP");
            String priceP = request.getParameter("priceP");
            String qteP = request.getParameter("qteP");
            String costP = request.getParameter("costP");
            String salesP = request.getParameter("salesP");
            String shippingP = request.getParameter("shippingP");
            String companyP = request.getParameter("companyP");
                
            //current date
            java.sql.Date date = new java.sql.Date(Calendar.getInstance().getTime().getTime());
            
            //Quelle action a appellée le service ?
            String action = request.getParameter("action");
            action = (action == null) ? "" : action; // Pour le switch qui n'aime pas les null
            switch (action) {
                case "login":
                    DAO dao = new DAO(DataSourceFactory.getDataSource());
                    dao.checkUser(request);
                    break;
                case "Logout":
                    doLogout(request);
                    break;
                            
            }
            
                 
            //On cherche a redirigé l'utilisateur 
            String userName = findUserInSession(request);
            String jspView;
            
            //On choisi la page en fonction du statut de l'utilisateur 
                        
            if (userName == null){
                jspView = "login.jsp";
                //out.println("testici");
                //On redirige vers cette page 
                request.getRequestDispatcher(jspView).forward(request, response);
            }
            
            if (userName.equals("admin")){
                jspView = "admin.jsp";
                //On redirige vers cette page 
                request.getRequestDispatcher(jspView).forward(request, response);
            }
            
            else{
                jspView = "client.jsp";
                try (PrintWriter out = response.getWriter()) {
                    DAO dao = new DAO(DataSourceFactory.getDataSource()); 
                    
                    // Id du customer
                    int customer = dao.customerId(userName);
                    
                    List<String> categories = dao.existingCategory();
                    String category = request.getParameter("category");
                    // On n'a pas forcément le paramètre
                    if (null == category) {
                        category = categories.get(1);
                    }
                        
                    List<ProductEntity> products = dao.productsInCategory(category);
                    request.setAttribute("products", products);
                    request.setAttribute("existingCategories", categories);
                    request.setAttribute("purchase", dao.allPurchase(customer));
                    request.setAttribute("selectedCategory", category);
                    
                       
                    switch (action) {
                        case "Add": // Requête d'ajout (vient du formulaire de saisie)
                            String product1 = request.getParameter("product1");
                            String price1 = request.getParameter("price1");
                            String quantity1 = request.getParameter("quantity1");
                            String qte1 = request.getParameter("qte1");
                            String id1 = request.getParameter("id1");
                                
                            float price2 = Float.parseFloat(price1);
                            int quantity2 = Integer.parseInt(quantity1);
                            int qte2 = Integer.parseInt(qte1);
                            int id2 = Integer.parseInt(id1);
                            
                            try{
                                if(quantity2<=qte2){
                                    dao.insertPurchase(dao.maxId(), customer, id2, quantity2, 0, date, date, "Poney Express");
                                    request.setAttribute("purchase", dao.allPurchase(customer));
                                }else{
                                    request.setAttribute("message3", "Quantity not available !");
                                }
                                request.setAttribute("purchase", dao.allPurchase(customer));
                            }catch(NullPointerException e){
                                request.setAttribute("message2", "Impossible to add !");
                                request.setAttribute("purchase", dao.allPurchase(customer));
                            }
                            break;
                            
                        case "Delete": // Requête de suppression (vient du lien hypertexte)
                            String numD = request.getParameter("numD");
                            System.out.println(numD);
                            try {
                                dao.deleteOrder(numD);
                                request.setAttribute("message", "Order " + numD + " has been deleted");
                                request.setAttribute("purchase", dao.allPurchase(customer));								
                            } catch (SQLIntegrityConstraintViolationException e) {
                            }
                            break;
                                
                        case "Modify": // Requête de modification
                            String qte3 = request.getParameter("qte3");
                            String numD1 = request.getParameter("numD");
                            int qte4 = Integer.parseInt(qte3);
                            int numD2 = Integer.parseInt(numD1);
                            dao.modifyQte(qte4, numD2);
                            request.setAttribute("message4", "Quantity modified");
                            request.setAttribute("purchase", dao.allPurchase(customer));
                            break;
			}
                    //On redirige vers cette page 
                    request.getRequestDispatcher(jspView).forward(request, response);
                }
            }
            
            
            
             
        }
 
            
	private void doLogout(HttpServletRequest request) {
		// On termine la session
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	private String findUserInSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return (session == null) ? null : (String) session.getAttribute("userName");
	}
    

    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
