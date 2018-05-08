/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.app.proactif.vue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modele.Client;
import modele.Intervention;
import modele.InterventionAnimal;
import modele.InterventionIncident;
import modele.InterventionLivraison;
import modele.Personne;
import service.ServiceConciergerie;

/**
 *
 * @author o0dem
 */
public class VueIntervsClient {
    private HttpServletRequest request;
    private HttpServletResponse response;

    public VueIntervsClient(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    public void exec()
    {
        System.out.println("vue");
        HttpSession sess = request.getSession(true);
        Personne p = (Personne) sess.getAttribute("personne");
        JsonArray listeIntervs = new JsonArray();
        ServiceConciergerie srv = new ServiceConciergerie();
        List<Intervention> intervsClient = srv.obtenirInterventionsParClient((Client)p);
        String all = request.getParameter("all");
        int bound = 3;
        if(all.equals("true"))
            bound = intervsClient.size();
        for(int nbInterv = 0; nbInterv < intervsClient.size() && nbInterv < bound; nbInterv++)
        {
            Intervention i = intervsClient.get(nbInterv);
            JsonObject joInterv = new JsonObject();
            joInterv.addProperty("type", i.getTypeLabel());
            joInterv.addProperty("début", i.getDebut().toString());
            String status = null;
            if(i.getStatus() == 'E'){
                status = "En cours";
            }else if(i.getStatus() == 'R'){
                status = "Terminée";
            }else if(i.getStatus() == 'P'){
                status = "Problème";
            }
            joInterv.addProperty("status", status);
            joInterv.addProperty("description", i.getDescription());
            joInterv.addProperty("employéNom", i.getEmploye().getNom());
            joInterv.addProperty("employéPrénom", i.getEmploye().getPrenom());
            joInterv.addProperty("com", i.getCommentaire());
            if(i.getFin() != null)
                joInterv.addProperty("fin", i.getFin().toString());
            if(i.getClass() == InterventionLivraison.class)
            {
                InterventionLivraison iL = (InterventionLivraison)i;
                joInterv.addProperty("typeLiv", iL.getTypeLivraison());
                joInterv.addProperty("compLiv", iL.getEntrepriseLivraison());
            }else if(i.getClass() == InterventionAnimal.class)
            {
                InterventionAnimal iA = (InterventionAnimal)i;
                joInterv.addProperty("typeAnimal", iA.getTypeAnimal());
            }else if(i.getClass() == InterventionIncident.class)
            {
                InterventionIncident iI = (InterventionIncident)i;
//                        RAS
            }
            listeIntervs.add(joInterv);
        }
        JsonObject container = new JsonObject();
        container.add("listeIntervs", listeIntervs);
        try {
            response.getWriter().println(container);
        } catch (IOException ex) {
            Logger.getLogger(VueIntervsClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("la");
    }
}
