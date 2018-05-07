/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.app.proactif.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modele.Client;
import modele.Employe;
import modele.Personne;
import service.ServiceConciergerie;

/**
 *
 * @author o0dem
 */
public class ActionConnexion {
    private HttpServletRequest request;

    public ActionConnexion(HttpServletRequest request) {
        this.request = request;
    }

    public void exec()
    {
                HttpSession sess = request.getSession(true);
                Personne p = (Personne) sess.getAttribute("personne");
                ServiceConciergerie srv = new ServiceConciergerie();
                System.out.println("login"+p);
                Personne newP = srv.authentifierPersonne(request.getParameter("mail"), request.getParameter("password"));
                if(newP == null)//authentication failed
                {
                    System.out.println("log failed");
                    request.setAttribute("connected", false);
                }else if(p == null || (p != null && !newP.getEmail().equals(p.getEmail())))//authentication succeeded
                {
                    System.out.println("new personne");
                    sess.setAttribute("personne", newP);
                    request.setAttribute("connected", true);
                    if(newP.getClass() == Client.class)
                    {
                        request.setAttribute("type", "client");
                    }
                    else if(newP.getClass() == Employe.class)
                    {
                        request.setAttribute("type", "employe");
                    }
                }else//already registered
                {
                    System.out.println("already registered");
                    request.setAttribute("connected", "already");
                }
    }
}
