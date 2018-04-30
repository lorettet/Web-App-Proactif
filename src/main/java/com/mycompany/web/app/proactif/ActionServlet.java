/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.app.proactif;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dao.JpaUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modele.Adresse;
import modele.Client;
import modele.Employe;
import modele.Personne;
import modele.Client;
import modele.Intervention;
import modele.InterventionAnimal;
import modele.InterventionIncident;
import modele.InterventionLivraison;
import service.ServiceConciergerie;
import service.ServiceException;

/**
 *
 * @author tlorettefr
 */
@WebServlet(name = "ActionServlet", urlPatterns = {"/ActionServlet"})
public class ActionServlet extends HttpServlet {

    ServiceConciergerie srv = new ServiceConciergerie();
    
    @Override
    public void init()
    {
        JpaUtil.init();
        try {
            srv.creerEmployes();
        } catch (ServiceException ex) {
            Logger.getLogger(ActionServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void destroy()
    {
        JpaUtil.destroy();
    }
    
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
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject joSess = new JsonObject();
        
        String todo = request.getParameter("action");
        
        HttpSession sess = request.getSession(true);
        Personne p = (Personne) sess.getAttribute("personne");
        //Vérification que l individu est connecté, sinon redirection vers la page de connexion
        if(p==null && !todo.equals("signup") && !todo.equals("login"))
        {
            System.out.println("sess pas ok"+p);
            joSess.addProperty("sess", "pasok");
            response.getWriter().print(gson.toJson(joSess));
//            getServletContext().getRequestDispatcher("/connexion.html").forward(request, response);
            return;
        }

        switch(todo)
        {
            case "deconnection" :
                System.out.println("log out");
                sess.setAttribute("personne", null);
                break;
                
            case "login" :
                System.out.println("login"+p);
                Personne newP = srv.authentifierPersonne(request.getParameter("mail"), request.getParameter("password"));
                if(newP == null)//authentication failed
                {
                    System.out.println("log failed");
                    joSess.addProperty("error", "Les identifiants entrés sont incorrects");
                    response.getWriter().println(gson.toJson(joSess));
                }else if(p == null || (p != null && !newP.getEmail().equals(p.getEmail())))//authentication succeeded
                {
                    System.out.println("new personne");
                    sess.setAttribute("personne", newP);
                    if(newP.getClass() == Client.class)
                    {
                        joSess.addProperty("log", "okClient");
                        response.getWriter().println(gson.toJson(joSess));
//                        getServletContext().getRequestDispatcher("/index.html").forward(request, response);
                    }
                    else if(newP.getClass() == Employe.class)
                    {
                        joSess.addProperty("log", "okEmployé");
                        response.getWriter().println(gson.toJson(joSess));
//                        getServletContext().getRequestDispatcher("/index.html").forward(request, response);
                    }
                }else//already registered
                {
                    System.out.println("already registered");
                    joSess.addProperty("error", "Vous êtes déjà connecté");
                    response.getWriter().println(gson.toJson(joSess));
                }
                break;
                
            case "signup" :
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date d = new Date();
                try
                {
                    d = sdf.parse(request.getParameter("DateNaissance"));
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                Adresse adrr = new Adresse(request.getParameter("Ville"), Integer.parseInt(request.getParameter("CodePostal")), request.getParameter("Adresse"), request.getParameter("ComplementAdresse"));
                Client cli = new Client(request.getParameter("Nom"), request.getParameter("Prenom"), request.getParameter("Civilite").charAt(0), 
                                        request.getParameter("Mail"), request.getParameter("MotDePasse"), d, request.getParameter("Telephone"), adrr);
                try
                {
                    srv.inscrireClient(cli);
//                    sess.setAttribute("personne", (Personne)cli);
                    joSess.addProperty("inscrit", "ok");
                    response.getWriter().println(gson.toJson(joSess));
//                    getServletContext().getRequestDispatcher("/index.html").forward(request, response);
                }catch(ServiceException e)
                {
//                    getServletContext().getRequestDispatcher("/inscription.html").forward(request, response);
                    joSess.addProperty("error", e.getMessage());
                    response.getWriter().println(gson.toJson(joSess));
                }
                break;
                
            case "AccueilClient" :
                JsonObject jo = new JsonObject();
                jo.addProperty("prénom", p.getPrenom());
                jo.addProperty("nom", p.getNom());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().println(gson.toJson(jo));
                break;
            case "AccueilEmploye":
                List<Intervention> interventions = srv.obtenirToutesInterventionsDuJour();
                Intervention theIntervention = null;
                for(Intervention inter : interventions)
                {
                    if(inter.getEmploye().getId() == p.getId())
                    {
                        if(inter.getStatus()=='E')
                        {
                            theIntervention = inter;
                            break;
                        }
                    }
                }
                JsonObject joMain = new JsonObject();
                JsonObject joInter = new JsonObject();
                if(theIntervention != null)
                {
                    joInter.addProperty("type",theIntervention.getTypeLabel());
                    joInter.addProperty("status", theIntervention.getStatus());
                    joInter.addProperty("description",theIntervention.getDescription());
                    joInter.addProperty("dateDebut", theIntervention.getDebut().toString());
                    if(theIntervention.getClass() == InterventionAnimal.class)
                    {
                        String typeAnimal = ((InterventionAnimal)theIntervention).getTypeAnimal();
                        joInter.addProperty("typeAnimal",typeAnimal);
                    }
                    else if(theIntervention.getClass() == InterventionLivraison.class)
                    {
                        
                    }
                    else if(theIntervention.getClass() == InterventionIncident.class)
                    {
                        
                    }
                    Client client = theIntervention.getClient();
                    JsonObject joCli = new JsonObject();
                    joCli.addProperty("id", client.getId());
                    joCli.addProperty("nom",client.getNom());
                    joCli.addProperty("prenom",client.getPrenom());
                    JsonObject joAdresse = new JsonObject();
                    Adresse adresse = client.getAdresse();
                    joAdresse.addProperty("rue", adresse.getRue());
                    joAdresse.addProperty("codePostal", adresse.getCodePostal());
                    joAdresse.addProperty("ville", adresse.getVille());
                    joAdresse.addProperty("complement", adresse.getComplement());
                    joCli.add("adresse", joAdresse);
                    joInter.add("client",joCli);
                    joMain.add("intervention", joInter);
                }
                
                
                JsonObject joEmpInf = new JsonObject();
                joEmpInf.addProperty("prénom", p.getPrenom());
                joEmpInf.addProperty("nom", p.getNom());
                joMain.add("employe", joEmpInf);
                response.setContentType("application/json");
                response.getWriter().println(joMain.toString());

                break;
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
        processRequest(request, response);
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
        processRequest(request, response);
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
