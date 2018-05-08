/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.app.proactif.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modele.Adresse;
import modele.Client;
import modele.Employe;
import modele.Personne;
import service.ServiceConciergerie;
import service.ServiceException;

/**
 *
 * @author o0dem
 */
public class ActionInscription {
    private HttpServletRequest request;

    public ActionInscription(HttpServletRequest request) {
        this.request = request;
    }

    public void exec()
    {
        ServiceConciergerie srv = new ServiceConciergerie();
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
//            joSess.addProperty("inscrit", "ok");
            request.setAttribute("inscrit", true);
//            response.getWriter().println(gson.toJson(joSess));
//                    getServletContext().getRequestDispatcher("/index.html").forward(request, response);
        }catch(ServiceException e)
        {
//                    getServletContext().getRequestDispatcher("/inscription.html").forward(request, response);
//            joSess.addProperty("error", e.getMessage());
            request.setAttribute("inscrit", false);
            request.setAttribute("error", e.getMessage());
//            response.getWriter().println(gson.toJson(joSess));
        }
    }
}
