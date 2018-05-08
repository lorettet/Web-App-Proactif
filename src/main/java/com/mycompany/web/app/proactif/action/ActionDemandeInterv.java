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
import modele.Intervention;
import modele.InterventionAnimal;
import modele.InterventionIncident;
import modele.InterventionLivraison;
import modele.Personne;
import service.ServiceConciergerie;
import service.ServiceException;

/**
 *
 * @author o0dem
 */
public class ActionDemandeInterv {
    private HttpServletRequest request;

    public ActionDemandeInterv(HttpServletRequest request) {
        this.request = request;
    }

    public void exec()
    {
        HttpSession sess = request.getSession(true);
        Personne p = (Personne) sess.getAttribute("personne");
        ServiceConciergerie srv = new ServiceConciergerie();
        String type = request.getParameter("type");
        String desc = request.getParameter("description");
        Intervention interv = null;
        switch(type){
            case "Incident" :
                interv = new InterventionIncident(desc);
                break;

            case "Animal" :
                String espece = request.getParameter("espèce");
                interv = new InterventionAnimal(desc, espece);
                break;
            case "Livraison" :
                String typeLiv = request.getParameter("typeLiv");
                String compLiv = request.getParameter("compLiv");
                interv = new InterventionLivraison(desc, typeLiv, compLiv);
                break;
        }
        if(interv != null){
            try{
                srv.demanderIntervention((Client)p, interv);
                request.setAttribute("demandeInterv", true);
//                joSess.addProperty("demandeInterv", "ok");
//                response.getWriter().println(gson.toJson(joSess));
            }catch(ServiceException e){
                request.setAttribute("demandeInterv", false);
                request.setAttribute("error", e.getMessage());
//                joSess.addProperty("error", e.getMessage());
//                response.getWriter().println(gson.toJson(joSess));
            }
        }
    }
}
