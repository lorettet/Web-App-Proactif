/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var allInterventions;
        function histo(){
            $('#interventions').remove();
            $('#histoDiv').append("<div id='interventions'></div>")
            $('#histoDiv')[0].style.display = 'inline';
            $('#newDemandeDiv')[0].style.display = 'none';
            $('#accueilDiv')[0].style.display = 'none';
            
            $.ajax({
                url: './ActionServlet',
                type: 'POST',
                data: {action : 'clientIntervs', all : 'true'},
                dataType: 'json',
            }).done(function(data){
                displayInterv(data);
                //allInterventions = $('#interventions fieldset');
            })
        }
        
        $('#filterForm').submit( function(event){
            event.preventDefault();
            var dataArray = $(this).serializeArray(),
            dataObj = {};

            $(dataArray).each(function(i, field){
                dataObj[field.name] = field.value;
            });
            var data = filterIntervs(dataObj);
            $('#interventions').pagination({
                    dataSource : data,
                    pageSize : 3,
                    callback: function(data, pagination) {
                    $('#data').html(data);
                    }
                });
        });
        
        function filterIntervs(filterChoice){
            var result = [];
            var resultType = [];
            var resultDate = [];
            var resultStatus = [];
            var intervs = allInterventions;
            var filter = _.filter(_.keys(filterChoice), function (key) {
                if(filterChoice[key] === "1") return true;
            });
            console.log(filter);
            if(filter.indexOf("Tous") > -1){
                return intervs;
            }
            for(var i = 0; i<intervs.length ; i++){
                if(filter.indexOf("Type") > -1){
                    if(filter.indexOf($($(intervs[i]).find('#type')).attr('name')) > -1){
                        resultType.push(intervs[i]);
                    }
                }
                if(filter.indexOf("Date") > -1){
                    var dateFilter = new Date($('#calendrier').val());
                    var date = new Date($($(intervs[i]).find('#date')).attr('name'));
                    dateFilter.setHours(0,0,0,0);
                    date.setHours(0,0,0,0);
                    if(date.getTime() === dateFilter.getTime()){
                        resultDate.push(intervs[i]);
                    }
                }
                if(filter.indexOf("Status") > -1){
                    if(filter.indexOf($($(intervs[i]).find('#status')).attr('name')) > -1){
                        resultStatus.push(intervs[i]);
                    }
                }
            }
            result = result.concat(resultType);
            result = result.concat(resultDate);
            result = result.concat(resultStatus);
            result = _.uniq(result);
            /*if(filter.indexOf("Type") > -1 && filter.indexOf("Date") > -1){
                for(var i=0;i<resultDate.length;i++){
                    if(resultType.indexOf(resultDate[i]) > -1){
                        result.push(resultDate[i]);
                    }
                }
                console.log('f');
            }else if(filter.indexOf("Type") > -1){
                result = resultType;
                console.log('ff');
            }else if(filter.indexOf("Date") > -1){
                console.log('fff');
                result = resultDate;
            }else{
                console.log('ffff');
                result = allInterventions;
            }*/
            console.log(result);
            return result;
            //console.log($(result[0]).find('#type').html());
        }
        
        
        
        function typeBoxChecked(element){
            if($(element).is(':checked')){
                $('#chkTous').attr('checked', false);
                document.getElementById('types').style.display = 'block';
            }else{
                document.getElementById('types').style.display = 'none'
            }
        }
        
        function dateBoxChecked(element){
            if($(element).is(':checked')){
                $('#chkTous').attr('checked', false);
                document.getElementById('calendrier').style.display = 'block';
            }else{
                document.getElementById('calendrier').style.display = 'none'
            }
        }
        
        function statusBoxChecked(element){
            if($(element).is(':checked')){
                $('#chkTous').attr('checked', false);
                document.getElementById('status').style.display = 'block';
            }else{
                document.getElementById('status').style.display = 'none'
            }
        }
        
        function tousBoxChecked(element){
            if($(element).is(':checked')){
                $('#chkType').attr('checked', false);
                $('#chkDate').attr('checked', false);
                $('#chkStatus').attr('checked', false);
                document.getElementById('types').style.display = 'none';
                document.getElementById('calendrier').style.display = 'none'
                document.getElementById('status').style.display = 'none'
            }
        }
