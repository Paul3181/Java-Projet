package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.DAO;
import model.DataSourceFactory;


@WebServlet(name = "Admin", urlPatterns = {"/Admin"})
public class Admin extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
            response.setContentType("text/html;charset=UTF-8");  
            String dateDebut = request.getParameter("dateDebut");
            String dateFin = request.getParameter("dateFin");
            
            try (PrintWriter out = response.getWriter()) {
                    DAO dao = new DAO(DataSourceFactory.getDataSource()); 

                    
                    //On recupere chiffre affaire pour chart 1
                    double sw = dao.CabyCat("SW",dateDebut,dateFin);
                    double hw = dao.CabyCat("HW",dateDebut,dateFin);
                    double fw = dao.CabyCat("FW",dateDebut,dateFin);
                    double bk = dao.CabyCat("BK",dateDebut,dateFin);
                    double cb = dao.CabyCat("CB",dateDebut,dateFin);
                    double ms = dao.CabyCat("MS",dateDebut,dateFin);
                    request.setAttribute("SW", sw);
                    request.setAttribute("HW", hw);
                    request.setAttribute("FW", fw);
                    request.setAttribute("BK", bk);
                    request.setAttribute("CB", cb);
                    request.setAttribute("MS", ms);
                    
                    //On recupere chiffre affaire pour chart 2
                    double fl = dao.CabyState("FL",dateDebut,dateFin);
                    double tx = dao.CabyState("TX",dateDebut,dateFin);
                    double ga = dao.CabyState("GA",dateDebut,dateFin);
                    double ca = dao.CabyState("CA",dateDebut,dateFin);
                    double mi = dao.CabyState("MI",dateDebut,dateFin);
                    double ny = dao.CabyState("NY",dateDebut,dateFin);
                    request.setAttribute("fl", fl);
                    request.setAttribute("tx", tx);
                    request.setAttribute("ga", ga);
                    request.setAttribute("ca", ca);
                    request.setAttribute("mi", mi);
                    request.setAttribute("ny", ny);
                    
                    
                    //On recupere chiffre affaire pour chart 3
                    double cli_1 = dao.CabyCli(1,dateDebut,dateFin);
                    double cli_2 = dao.CabyCli(2,dateDebut,dateFin);
                    double cli_3 = dao.CabyCli(3,dateDebut,dateFin);
                    double cli_25 = dao.CabyCli(25,dateDebut,dateFin);
                    double cli_36 = dao.CabyCli(36,dateDebut,dateFin);
                    double cli_106 = dao.CabyCli(106,dateDebut,dateFin);
                    double cli_149 = dao.CabyCli(149,dateDebut,dateFin);
                    double cli_409 = dao.CabyCli(409,dateDebut,dateFin);
                    double cli_410 = dao.CabyCli(410,dateDebut,dateFin);
                    double cli_722 = dao.CabyCli(722,dateDebut,dateFin);
                    double cli_753 = dao.CabyCli(753,dateDebut,dateFin);
                    double cli_777 = dao.CabyCli(777,dateDebut,dateFin);
                    double cli_863 = dao.CabyCli(863,dateDebut,dateFin);     
                    request.setAttribute("cli_1", cli_1);
                    request.setAttribute("cli_2", cli_2);
                    request.setAttribute("cli_3", cli_3);
                    request.setAttribute("cli_25", cli_25);
                    request.setAttribute("cli_36", cli_36);
                    request.setAttribute("cli_106", cli_106);
                    request.setAttribute("cli_149", cli_149);
                    request.setAttribute("cli_409", cli_409);
                    request.setAttribute("cli_410", cli_410);
                    request.setAttribute("cli_722", cli_722);
                    request.setAttribute("cli_753", cli_753);
                    request.setAttribute("cli_777", cli_777);
                    request.setAttribute("cli_863", cli_863);
                    
                    
                    
                    //On redirige vers cette page 
                    request.getRequestDispatcher("admin.jsp").forward(request, response);
            }catch(Exception e){
                
            }

            

        
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
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
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
