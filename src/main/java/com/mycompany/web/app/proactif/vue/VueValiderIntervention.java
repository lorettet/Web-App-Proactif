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

/**
 *
 * @author o0dem
 */
public class VueValiderIntervention {
    private HttpServletRequest request;
    private HttpServletResponse response;

    public VueValiderIntervention(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public void exec() {
        JsonObject result = new JsonObject();
        result.addProperty("error", request.getAttribute("error").toString());
        try {
            response.getWriter().println(result);
        } catch (IOException ex) {
            Logger.getLogger(VueValiderIntervention.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
