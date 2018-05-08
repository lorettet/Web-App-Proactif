/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.app.proactif.action;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modele.Employe;
import modele.Intervention;
import service.ServiceConciergerie;
import service.ServiceException;

/**
 *
 * @author o0dem
 */
public class ActionValiderIntervention {
    private HttpServletRequest request;

    public ActionValiderIntervention(HttpServletRequest request) {
        this.request = request;
    }
    
    public void exec() {
        ServiceConciergerie srv = new ServiceConciergerie();
        HttpSession sess = request.getSession(false);
        Intervention interv = ((Employe) sess.getAttribute("personne")).getInterventionEnCours();
        System.out.println(interv);
        interv.setCommentaire(request.getParameter("commentaire"));
        if(request.getParameter("etat").equals("ok"))
        {
            interv.setStatus(Intervention.STATUS_REALISEE);
        }
        else if(request.getParameter("etat").equals("nok"))
        {
            interv.setStatus(Intervention.STATUS_PROBLEME);
        }
        try {
            srv.cloturerIntervention(interv);
            request.setAttribute("error","no");
        } catch (ServiceException ex) {
            Logger.getLogger(ActionValiderIntervention.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("error", "exceptionCaught");
        }
    }
    
}
