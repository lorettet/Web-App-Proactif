/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function displayInterv(data){
            allInterventions = [];
            $('#interventions').html('');
            $('#interventions').append("<div id='data'><div>");
            for(var i=0; i<data.listeIntervs.length; i++){
                var date = new Date(data.listeIntervs[i].début);
                var dateFormated = ("0" + date.getHours()).slice(-2)  + ":" + 
                                    ("0" + date.getMinutes()).slice(-2);
                    allInterventions.push($('<fieldset></fieldset>').html("<div>"
                        +"<span id='type' name='"+data.listeIntervs[i].type+"'>"+data.listeIntervs[i].type+'</span>'
                        +"<span id='date' name='"+date.toLocaleDateString("en-GB")+"'>"+date.toLocaleDateString("en-GB")+" à "+dateFormated+'</span>'
                        +"<span id='status' name='"+data.listeIntervs[i].status+"'>"+data.listeIntervs[i].status+'</span>'
                        +'</div>'
                        +'<div>Description : '
                        +data.listeIntervs[i].description+' '
                        +'</div>'
                        +'<div>Prise en charge par '
                        +data.listeIntervs[i].employéNom+' '+data.listeIntervs[i].employéPrénom+' '
                        +'<a href=\"#\" id=\"hrefplusdiv'+i+'\" style=\'visibility:visible\'; onclick=\"plusMoins('+i+', \'plus\');\">plus</a>'
                        +'</div>'
                        +'<div id="invisible'+i+'" style="display:none;">'
                        +'<a href=\"#\" id=\"hrefmoinsdiv'+i+'\" onclick=\"plusMoins('+i+', \'moins\');\">moins</a>'
                        +'</div>'
                        +'</fieldset>'));
            $('#interventions').pagination({
                    dataSource : allInterventions,
                    pageSize : 3,
                    callback: function(data, pagination) {
                    $('#data').html(data);
                    }
                });
                caseStatus(data.listeIntervs[i], i);//'invisible'+i);
                caseType(data.listeIntervs[i], i);//'invisible'+i);
            }
            function caseType(data, div){
                if(data.type === 'Livraison'){
                    allInterventions[i].find('#invisible'+i).prepend(
                            '<div>Type de livraison : '+data.typeLiv+'</div>'
                            +'<div>Entreprise de livraison : '+data.compLiv+'</div>'
                            )
                }else if(data.type === 'Animal'){
                    allInterventions[i].find('#invisible'+i).prepend(
                            '<div>Animal : '+data.typeAnimal+'</div>'
                            )
                }
            }
            
            function caseStatus(data, div){
                if(data.status === 'Terminée'){
                    var date = new Date(data.fin);
                    var dateFormated = ("0" + date.getHours()).slice(-2)  + ":" + 
                                    ("0" + date.getMinutes()).slice(-2);
                    allInterventions[i].find('#invisible'+i).prepend(
                            '<div>Terminée le '+date.toLocaleDateString("en-GB")+" à "+dateFormated+'</div>'
                            +'<div>Commentaire : '+data.com+'</div>'
                            )
                }
            }
            
        }
        
        function plusMoins(div, plusOUmoins){
            if(plusOUmoins === "plus"){
                document.getElementById('hrefplusdiv'+div).style.visibility = "hidden";
                document.getElementById('invisible'+div).style.display = "block";
            }else if(plusOUmoins === "moins"){
                document.getElementById('invisible'+div).style.display = "none";
                document.getElementById('hrefplusdiv'+div).style.visibility = "visible"
            }
        }
