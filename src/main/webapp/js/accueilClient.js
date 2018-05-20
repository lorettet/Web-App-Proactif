/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function accueil(){
            $('#interventions').remove();
            $('#accueilDiv').append("<div id='interventions'></div>")
            $('#histoDiv')[0].style.display = 'none';
            $('#newDemandeDiv')[0].style.display = 'none';
            $('#accueilDiv')[0].style.display = 'inline';
            $.ajax({
                url: './ActionServlet',
                type: 'POST',
                data: {action : 'clientIntervs', all : 'false'},
                dataType: 'json',
            }).done(function(data){
                displayInterv(data);
                $('#interventions').pagination('hide');
            })
        }
