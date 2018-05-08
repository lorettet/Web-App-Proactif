/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.app.proactif.vue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author o0dem
 */
public class VueInscription {
    private HttpServletRequest request;
    private HttpServletResponse response;

    public VueInscription(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    public void exec()
    {
        JsonObject jo = new JsonObject();
        jo.addProperty("inscrit", request.getAttribute("inscrit").toString());
        if(request.getAttribute("inscrit").toString().equals("false"))
        {
            jo.addProperty("error", request.getAttribute("error").toString());
        }
        try {
            System.out.println(jo);
            response.getWriter().println(jo);
        } catch (IOException ex) {
            Logger.getLogger(VueInscription.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
