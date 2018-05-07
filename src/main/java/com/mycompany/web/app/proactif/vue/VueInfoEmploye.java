/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.app.proactif.vue;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modele.Adresse;
import modele.Employe;

/**
 *
 * @author o0dem
 */
public class VueInfoEmploye {
    private HttpServletRequest request;
    private HttpServletResponse response;

    public VueInfoEmploye(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    public void exec()
    {
        JsonObject joEmp = new JsonObject();
        if(request.getAttribute("error") != null)
        {
       
            joEmp.addProperty("error", request.getAttribute("error").toString());
            return;
        }
        Employe emp = (Employe)request.getAttribute("employe");
        System.out.println(emp);
        joEmp.addProperty("id", emp.getId());
        joEmp.addProperty("nom", emp.getNom());
        joEmp.addProperty("prenom", emp.getPrenom());
        joEmp.addProperty("heureDebut", emp.getHeureDebutTravail());
        joEmp.addProperty("heureFin", emp.getHeureFinTravail());
        
        Adresse adresse = emp.getAdresse();
        JsonObject joAdresse = new JsonObject();
        joAdresse.addProperty("ville", adresse.getVille());
        joAdresse.addProperty("codePostal", adresse.getCodePostal());
        joAdresse.addProperty("rue", adresse.getRue());
        joAdresse.addProperty("complement", adresse.getComplement());
        joAdresse.addProperty("latitude", adresse.getLatitude());
        joAdresse.addProperty("longitude", adresse.getLongitude());
        joEmp.add("adresse", joAdresse);
        
        JsonObject result = new JsonObject();
        result.add("employe", joEmp);
        
        try {
            response.getWriter().println(result);
        } catch (IOException ex) {
            Logger.getLogger(VueInfoEmploye.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         
    }
    
}
