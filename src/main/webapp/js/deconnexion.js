/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function deco()
{
    alert("deco");
    $.ajax({
        url: './ActionServlet',
        type: 'POST',
        data: {action : 'deconnection'},
    }).done(function(){
        window.location = "./connexion.html";
    })
    //window.location = "./connexion.html";
    window.location.replace("./connexion.html");
}

