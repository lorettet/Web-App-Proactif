/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.app.proactif.action;

import com.google.gson.JsonObject;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modele.Adresse;
import modele.Client;
import modele.Employe;
import modele.Intervention;
import modele.InterventionAnimal;
import modele.InterventionIncident;
import modele.InterventionLivraison;
import service.ServiceConciergerie;

/**
 *
 * @author lorettet
 */
public class ActionCurrentIntervention {

    private HttpServletRequest request;

    public ActionCurrentIntervention(HttpServletRequest request) {
        this.request = request;
    }
    
    public void exec()
    {
                ServiceConciergerie srv = new ServiceConciergerie();
                HttpSession sess = request.getSession(false);
                Employe p = (Employe) sess.getAttribute("personne");
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
                
                request.setAttribute("intervention", theIntervention);
    }
}
