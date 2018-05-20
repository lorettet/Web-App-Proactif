/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function newDemande(){
            $('#histoDiv')[0].style.display = 'none';
            $('#newDemandeDiv')[0].style.display = 'inline';
            $('#accueilDiv')[0].style.display = 'none';
            
            document.getElementById('typeInterv').options[0].selected = 'selected';
            
            $('#formulaire').submit( function(event){
                    $.ajax({
                    url: $('#formulaire').attr('action'),
                    type: 'POST',
                    data: $('#formulaire').serialize(), 
                    dataType: 'json'
                }).done(function(data){
                    if(data.demandeInterv === 'ok'){
                        alert('Demande effectuée avec succès');
                        $('#accueilBut').click();
                    }else{
                        alert(data.error);
                    }
                });
                event.preventDefault();
            });
            
            $('#typeInterv').change(function(){
                changeForm($('#typeInterv option:selected').text());
            });            
            
            function changeForm(typeInterv){
                if(typeInterv === 'Incident'){
                    $('#moreFields').html('');
                }else if(typeInterv === 'Animal'){
                    $('#moreFields').html("<label for='espece'>Espèce de l'animal : </label><input type='text' id='espece' name='espèce' required='required'><br/>");
                }else if(typeInterv === 'Livraison'){
                    $('#moreFields').html("<label for='typeLiv'>Type de livraison : </label><input type='text' id='typeLiv' name='typeLiv' required='required'><br/>\n\
                                            <label for='compLiv'>Entreprise de livraison : </label><input type='text' id='compLiv' name='compLiv' required='required'><br/>"
                                        );
                }
            }
        }
