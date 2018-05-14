/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function submit()
{   
    if(!$('#ok').prop('checked') && !$('#nok').prop('checked'))
    {
         $('#form').append("<span class='yellow cdr-message'>Veuillez cocher un état</span>")
         return;
    }
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
        if(data.error==="exceptionCaught")
        {
            $('#form').append("<span class='red cdr-message'>Une erreur s'est produite</span>")
        }
        else if(data.error==='no')
        {
            $('#info').html("<h2>Aucune intervention en cours</h2>"+
                            "<span class='green cdr-message'>Validation OK</span>")
        }
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
    $('#mapLink').attr('href','https://www.google.com/maps?q='+adresse.rue+" "+adresse.codePostal)
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
            return;
        }
        initInterv(data.intervention)
    });


});
