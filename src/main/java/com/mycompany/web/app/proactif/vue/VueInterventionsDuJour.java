/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.app.proactif.vue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modele.Adresse;
import modele.Client;
import modele.Intervention;
import modele.InterventionAnimal;
import modele.InterventionLivraison;

/**
 *
 * @author o0dem
 */
public class VueInterventionsDuJour {
    private HttpServletRequest request;
    private HttpServletResponse response;

    public VueInterventionsDuJour(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    public void exec()
    {
        List<Intervention> intervs = (List<Intervention>)request.getAttribute("intervs");
        JsonArray array = new JsonArray(intervs.size());
        
        for(Intervention interv : intervs)
        {
            JsonObject joInterv = new JsonObject();
            joInterv.addProperty("type", interv.getTypeLabel());
            joInterv.addProperty("dateDebut", interv.getDebut().getDate()+"/"+(interv.getDebut().getMonth()+1)+"/"+(interv.getDebut().getYear()+1900));
            joInterv.addProperty("status", interv.getStatus());
            joInterv.addProperty("description", interv.getDescription());
            
            JsonObject joClient = new JsonObject();
            Client c = interv.getClient();
            joClient.addProperty("nom", c.getNom());
            joClient.addProperty("prenom", c.getPrenom());
            joClient.addProperty("id", c.getId());
            
            JsonObject joAdresse = new JsonObject();
            Adresse a = c.getAdresse();
            joAdresse.addProperty("latitude", a.getLatitude());
            joAdresse.addProperty("longitude", a.getLongitude());
            joAdresse.addProperty("ville", a.getVille());
            joAdresse.addProperty("codePostal", a.getCodePostal());
            joAdresse.addProperty("rue", a.getRue());
            joAdresse.addProperty("complement", a.getComplement());
            joClient.add("adresse", joAdresse);
            
            joInterv.add("client", joClient);
            
            joInterv.addProperty("commentaire", interv.getCommentaire());
            
            JsonObject joEmp = new JsonObject();
            joEmp.addProperty("prenom", interv.getEmploye().getPrenom());
            joEmp.addProperty("nom", interv.getEmploye().getNom());
            joInterv.add("employe", joEmp);
            
            if(interv.getStatus() != Intervention.STATUS_EN_COURS)
            {
                joInterv.addProperty("dateFin", interv.getFin().getDate()+"/"+(interv.getFin().getMonth()+1)+"/"+(interv.getFin().getYear()+1900));
            }
            
            if(interv.getTypeLabel().equals("Animal"))
            {
                joInterv.addProperty("espece", ((InterventionAnimal)interv).getTypeAnimal());
            }
            else if(interv.getTypeLabel().equals("Livraison"))
            {
                joInterv.addProperty("typeLivraison", ((InterventionLivraison)interv).getTypeLivraison());
                joInterv.addProperty("entreprise", ((InterventionLivraison)interv).getEntrepriseLivraison());
            }
            
            array.add(joInterv);
        }
        
        JsonObject result = new JsonObject();
        result.add("interventions",array);
        
        try {
            response.getWriter().println(result);
        } catch (IOException ex) {
            Logger.getLogger(VueInterventionsDuJour.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
