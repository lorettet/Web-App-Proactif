/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.app.proactif.vue;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modele.Adresse;
import modele.Client;
import modele.Employe;
import modele.Intervention;
import modele.InterventionAnimal;
import modele.InterventionIncident;
import modele.InterventionLivraison;

/**
 *
 * @author o0dem
 */
public class VueCurrentIntervention {
    private HttpServletRequest request;
    private HttpServletResponse response;

    public VueCurrentIntervention(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    public void exec()
    {
        System.out.println("envoie des infos...");
        Employe e = ((Employe)request.getSession(false).getAttribute("personne"));
        Intervention interv = e.getInterventionEnCours();
        if(interv == null)
        {
            JsonObject result = new JsonObject();
            result.addProperty("error", "noInterv");
            try {
                response.getWriter().println(result);
            } catch (IOException ex) {
                Logger.getLogger(VueCurrentIntervention.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
        
        JsonObject joInterv = new JsonObject();
        String typeLabel = interv.getTypeLabel();
        if(typeLabel.equals("Animal"))
        {
            joInterv.addProperty("type", typeLabel);
            joInterv.addProperty("animal", ((InterventionAnimal) interv).getTypeAnimal());
        }
        else if(typeLabel.equals("Livraison"))
        {
            joInterv.addProperty("type", typeLabel);
            joInterv.addProperty("typeLivraison", ((InterventionLivraison)interv).getTypeLivraison());
            joInterv.addProperty("entrepriseLivraison", ((InterventionLivraison)interv).getEntrepriseLivraison());
        }
        else if(typeLabel.equals("Incident"))
        {
            joInterv.addProperty("type", typeLabel);
        }
        
        joInterv.addProperty("date", interv.getDebut().getDate()+"/"+(interv.getDebut().getMonth()+1)+"/"+(interv.getDebut().getYear()+1900));
        joInterv.addProperty("description", interv.getDescription());
        
        System.out.println(joInterv);
        
        Client c = interv.getClient();
        JsonObject joClient = new JsonObject();
        joClient.addProperty("nom", c.getNom());
        joClient.addProperty("prenom", c.getPrenom());
        
        Adresse a = c.getAdresse();
        JsonObject joAdresse = new JsonObject();
        joAdresse.addProperty("ville", a.getVille());
        joAdresse.addProperty("codePostal", a.getCodePostal());
        joAdresse.addProperty("rue", a.getRue());
        joAdresse.addProperty("complement", a.getComplement());
        joClient.add("adresse", joAdresse);
        
        joInterv.add("client", joClient);
        
        JsonObject result = new JsonObject();
        result.add("intervention", joInterv);
        
        try {
            response.getWriter().println(result);
        } catch (IOException ex) {
            Logger.getLogger(VueCurrentIntervention.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
