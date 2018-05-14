/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.app.proactif.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mycompany.web.app.proactif.vue.VueConnexion;
import com.mycompany.web.app.proactif.vue.VueCurrentIntervention;
import com.mycompany.web.app.proactif.vue.VueDemandeInterv;
import com.mycompany.web.app.proactif.vue.VueInfoEmploye;
import com.mycompany.web.app.proactif.vue.VueInscription;
import com.mycompany.web.app.proactif.vue.VueIntervsClient;
import com.mycompany.web.app.proactif.vue.VueInfoEmploye;
import com.mycompany.web.app.proactif.vue.VueInterventionsDuJour;
import com.mycompany.web.app.proactif.vue.VueValiderIntervention;
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
            return;
        }

        switch(todo)
        {
            case "deconnection" :
                System.out.println("log out");
                sess.setAttribute("personne", null);
                break;
                
            case "login" :
                new ActionConnexion(request).exec();
                new VueConnexion(request, response).exec();
               break;
                
            case "signup" :
                new ActionInscription(request).exec();
                new VueInscription(request, response).exec();
                break;
                
            case "AccueilClient" :
                JsonObject joNom = new JsonObject();
                joNom.addProperty("prénom", p.getPrenom());
                joNom.addProperty("nom", p.getNom());
                response.getWriter().println(gson.toJson(joNom));
                break;
            
            case "clientIntervs" :
                new VueIntervsClient(request, response).exec();
                break;
            
            case "demandeInterv":
                new ActionDemandeInterv(request).exec();
                new VueDemandeInterv(request, response).exec();
                break;

            case "InfoEmp":
                new ActionInfoEmploye(request).exec();
                new VueInfoEmploye(request, response).exec();
                break;
            case "CurrentInterv":
                new VueCurrentIntervention(request, response).exec();
                break;
            case "ValiderIntervention":
                new ActionValiderIntervention(request).exec();
                new VueValiderIntervention(request, response).exec();
                break;
            case "InterventionsDuJour":
                new ActionInterventionsDuJour(request).exec();
                new VueInterventionsDuJour(request, response).exec();
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
