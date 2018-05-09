/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.app.proactif.action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import modele.Intervention;
import service.ServiceConciergerie;

/**
 *
 * @author o0dem
 */
public class ActionInterventionsDuJour {
    private HttpServletRequest request;

    public ActionInterventionsDuJour(HttpServletRequest request) {
        this.request = request;
    }
    
    public void exec()
    {
        ServiceConciergerie srv = new ServiceConciergerie();
        List<Intervention> intervs = srv.obtenirToutesInterventionsDuJour();
        
        request.setAttribute("intervs", intervs);
    }
}
