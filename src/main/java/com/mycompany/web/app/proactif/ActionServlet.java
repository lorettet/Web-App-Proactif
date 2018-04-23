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
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modele.Client;
import modele.Employe;
import modele.Personne;
import service.ServiceConciergerie;

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
        String todo = request.getParameter("action");
        
        HttpSession sess = request.getSession(true);
        Personne p = (Personne) sess.getAttribute("personne");
        
        //Vérification que l individu est connecté, sinon redirection vers la page de connexion
        if(p==null)
        {
            getServletContext().getRequestDispatcher("/connexion.html").forward(request, response);
            return;
        }


        if(todo.equals("login"))
        {
            Personne newConn = srv.authentifierPersonne(request.getParameter("mail"), request.getParameter("password"));
            sess.setAttribute("personne", p);
            if(p.getClass() == Client.class)
            {
                getServletContext().getRequestDispatcher("AccueilClient.html").forward(request, response);
            }
            else if(p.getClass() == Employe.class)
            {
                getServletContext().getRequestDispatcher("AccueilEmploye.html").forward(request, response);
            }
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