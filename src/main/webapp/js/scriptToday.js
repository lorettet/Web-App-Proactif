/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*var lyon = {lat:45.7547002,long:4.8618123};
var zoom = 13;
var map;
*/

var lyon = {lat:45.7540566,lng:4.8661717};
var map;
var interventions;

function initInfo()
{
    var string = " intervention";
    if(interventions.length>1)
        string=" interventions"
    $('#nbIntervention').html(interventions.length+string)
    var enCours = 0;
    for(var i = 0; i<interventions.length; i++)
    {
        interventions[i].status==='E'?enCours++:enCours;
    }
    $('#nbIntervEnCours').html(enCours);
}

function initMapInfo()
{
    for(var i = 0; i<interventions.length; i++)
    {
        var interv = interventions[i];
        var lat = interv.client.adresse.latitude;
        var lng = interv.client.adresse.longitude;
        var pos = {lat:lat,lng:lng};
        var status;
        if(interv.status==='E')
            status="En cours...";
        else if(interv.status==='P')
            status="Problème"
        else if(interv.status==='R')
            status="Terminée"
        
        var infoContent = "<div id=\"marker"+i+"\">"+
                                "<div id=\"one-line-info\">"+
					"<span>"+interv.type+"</span>"+
					"<span>"+interv.dateDebut+"</span>"+
					"<span>"+status+"</span>"+
				"</div>"+
				"<div id=\"multi-line-info\">"+
					"<div><label>Description : </label><span>"+interv.description+"</span></div>"+
					"<div><label>Client : </label><span>"+interv.client.prenom+" "+interv.client.nom+" (#"+interv.client.id+")</span></div>"+
					"<div><label>Prise en charge par : </label><span>"+interv.employe.prenom+" "+interv.employe.nom+"</span></div>"+
				"</div>"+
                                "<span id=\"button"+i+"\" class=\"expend-button\" onClick=\"expend("+i+")\">Plus</span>"
                            "</div>"
        var infoWindow = new google.maps.InfoWindow({
            content: infoContent
        });
        var marker = new google.maps.Marker({position:pos,map:map,infoWindow:infoWindow});
        marker.addListener('click', function(){
            this.infoWindow.open(map,this);
        });
    }
}

$(function(){
    $.ajax({
            url: './ActionServlet',
            type: 'POST',
            data: {action : "InfoEmp"}, 
        }).done(function(data){
            if(data.sess === "pasok")
            {
                window.location = "./connexion.html";
                return;
            }
            initNom(data)
        });
        
       $.ajax({
       url: './ActionServlet',
       type: 'POST',
       data: {action : "InterventionsDuJour"}, 
   }).done(function(data){
       if(data.sess === "pasok")
       {
           window.location = "./connexion.html";
           return;
       }
       interventions = data.interventions;
       initInfo();
       initMapInfo();
   });
    })
    
function initNom(data)
{
    $('#nomEmp').html(data.employe.prenom+ " "+ data.employe.nom)
}

function expend(num)
{
    var lastDiv = $("#marker"+num).children("div").last();
    var button = $("#button"+num);
    button.html("Moins");
    button.attr("onClick","reduce("+num+")");
    var interv = interventions[num]
    if(interv.status!='E')
    {
        addInfo(lastDiv,"Fin d'intervention",interv.dateFin)
        addInfo(lastDiv,"Commentaire",interv.commentaire)
    }
    var adresse = interv.client.adresse.rue + ", " + interv.client.adresse.codePostal + " " + interv.client.adresse.ville
    addInfo(lastDiv,"Adresse", adresse)
    addInfo(lastDiv,"Complement", interv.client.adresse.complement)
    if(interv.type==='Animal')
    {
        addInfo(lastDiv,"Espèce de l'animal",interv.espece)
    }
    else if(interv.type==='Livraison')
    {
        addInfo(lastDiv,"Type de livraison",interv.typeLivraison)
        addInfo(lastDiv,"Entreprise",interv.entreprise)
    }
}

function reduce(num)
{
    var nbLine = 2;
    var interv = interventions[num]
    if(interv.status!='E')
    {
        nbLine+=2
    }
    if(interv.type==='Animal')
    {
        nbLine++
    }
    else if(interv.type==='Livraison')
    {
        nbLine+=2
    }
    removeInfo(num,nbLine)
    var button = $("#button"+num);
    button.html("Plus");
    button.attr("onClick","expend("+num+")");
}

function addInfo(div, label, text)
{
    div.append('<div><label class="label-multi-line-info">'+label+ ' : </label><span id="compInter">'+text+'</span></div>')
}

function removeInfo(num, nb)
{
    for(var i = 0; i<nb; i++)
    {
        $("#marker"+num).children("div").last().children().last().remove();
    }
}
