/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function submit()
{   
    var etat="text";
    if($('#ok')[0].checked)
        etat="ok";
    else if($('#nok')[0].checked)
        etat="nok";
    $.ajax({
    url: "./ActionServlet",
    type: 'POST',
    data: {
        action:"ValiderIntervention",
        etat:etat,
        commentaire:$('#commentaire')[0].value
    }, 
    dataType: 'json'
    }).done(function(data){
        location.reload();
    });
}
function addInfo(label, text)
{
    var lastChild = $('#multi-line-info div:last-child').last()
    lastChild.append('<div><label class="label-multi-line-info">'+label+ ' : </label><span id="compInter">'+text+'</span></div>')
}
function initNom(data)
{
    $('#nomEmp').html(data.employe.prenom+ " "+ data.employe.nom)

}

function initInterv(data)
{
    $('#type').html(data.type);
    $('#date').html(data.date);
    $('#description').html(data.description);
    $('#client').html(data.client.prenom + " " + data.client.nom)
    var adresse = data.client.adresse;
    $('#adresse').html(adresse.rue + ", " + adresse.codePostal + " " + adresse.ville)
    $('#complement').html(adresse.complement);
    if(data.type==="Animal")
    {
        addInfo("Expèce de l'animal",data.animal)
    }
    else if(data.type==="Livraison")
    {
        addInfo("Type de livraison",data.typeLivraison);
        addInfo("Entreprise",data.entrepriseLivraison);
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
        data: {action : "CurrentInterv"}, 
    }).done(function(data){
        if(data.sess === "pasok")
        {
            window.location = "./connexion.html";
            return;
        }
        if(data.error==="noInterv")
        {
            $('#main').empty();
            $('#main').html("<h2>Aucune intervention en cours</h2>")
        }
        initInterv(data.intervention)
    });


});
