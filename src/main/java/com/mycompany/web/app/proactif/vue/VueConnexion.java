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
public class VueConnexion {
    private HttpServletRequest request;
    private HttpServletResponse response;

    public VueConnexion(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    public void exec()
    {
        JsonObject jo = new JsonObject();
        jo.addProperty("connected", request.getAttribute("connected").toString());
        if(request.getAttribute("connected").toString().equals("true"))
        {
            jo.addProperty("type", request.getAttribute("type").toString());
        }
        try {
            System.out.println(jo);
            response.getWriter().println(jo);
        } catch (IOException ex) {
            Logger.getLogger(VueConnexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
