/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.web.app.proactif.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import modele.Employe;

/**
 *
 * @author o0dem
 */
public class ActionInfoEmploye {
    private HttpServletRequest request;

    public ActionInfoEmploye(HttpServletRequest request) {
        this.request = request;
    }
    
    public void exec()
    {
        HttpSession sess = request.getSession(false);
        if(sess == null)
        {
            request.setAttribute("error", "disconnected");
            return;
        }
        
        Employe emp = (Employe) sess.getAttribute("personne");
        
        request.setAttribute("employe", emp);
    }
}
