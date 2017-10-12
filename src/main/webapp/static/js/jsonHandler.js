
function setDataArray() {
		var array = JSON.parse(data);
		return array;
}


 function sortArray() {
    var array = JSON.parse(data);
    for(j =1; j<array.length; j++){
        var key = array[j].dateTime;
        var i = j-1;
        while(i>=0 && (array[i].dateTime > key)){
            array[i+1] = array[i];
            i--;
        }
        array[i+1] = array[j];
    }
    return array;

 }

function processNumberForDisplay(n){
	var numStr = n.toString();
	if(numStr.length == 1){
		return "0"+numStr;
	}else{
		return numStr;
	}
}
 function getDateToday() {
 var dateString = "";
 var dateObject = new Date();
    dateString += dateObject.getFullYear();
    dateString += "-";
    dateString += processNumberForDisplay(dateObject.getMonth()+1);
    dateString += "-";
    dateString += processNumberForDisplay(dateObject.getDate());

    return dateString;
 }

  function getTimeNow() {
    var timeString = "";
    var dateObject = new Date();
    timeString += processNumberForDisplay(dateObject.getHours());
    timeString += "-";
    timeString += processNumberForDisplay(dateObject.getMinutes());
    timeString += "-";
    timeString += processNumberForDisplay(dateObject.getSeconds());
    return timeString;
 }

 function getHourNow(){
    var dateObject = new Date();
    return processNumberForDisplay(dateObject.getHours());
 }
 function getHourNext(){
    var next = 0;
     var dateObject = new Date();
     var hour = dateObject.getHours();
     if(hour != 23){
        next = hour+1;
     }
     return processNumberForDisplay(next);
  }

 function dateTimeStamp() {
	 var dateTime = "";
	 dateTime += getDateToday();
	 dateTime += " ";
	 dateTime += getTimeNow();
	 return dateTime;
 }

 function getDailyData(referenceDate){ //give data in the format "yyyy-mm-dd" or take it from the function getDateToday() if checking the current day's results.
    var dataArray = setDataArray();
    var dailyDataArray =[];
    for(i =0; i < dataArray.length; i=i+2){
		var temp1 = (dataArray[i].dateTime).slice(0, 10);
        if(i+1 == dataArray.length){
			if(temp1 == referenceDate){
				dailyDataArray.push(dataArray[i]);
			}
		}else{
			var temp2 = (dataArray[i+1].dateTime).slice(0, 10);

			if(temp1 != referenceDate && temp2 != referenceDate){
				continue;
			}else if (temp1 != referenceDate && temp2 == referenceDate){
				dailyDataArray.push(dataArray[i+1]);
			}
			else if(temp1 == referenceDate && temp2 == referenceDate){
				dailyDataArray.push(dataArray[i]);
				dailyDataArray.push(dataArray[i+1]);
			}else{
				dailyDataArray.push(dataArray[i]);
				break;
			}
		}
    }

	return dailyDataArray;
 }

 /*function getHourlyData(referenceDate, referenceHour){ //give data in the format "yyyy-mm-dd" or take it from the function getDateToday() if checking the current day's results.
     var dataArray = getDailyData(referenceDate);
     var hourlyDataArray =[];
     for(i =0; i < dataArray.length; i=i+2){
 		var temp1 = (dataArray[i].dateTime).slice(11, 13);
         if(i+1 == dataArray.length){
 			if(temp1 == referenceHour){
 				hourlyDataArray.push(dataArray[i]);
 			}
 		}else{
 			var temp2 = (dataArray[i+1].dateTime).slice(11, 13);

 			if(temp1 != referenceHour && temp2 != referenceHour){
 				continue;
 			}else if (temp1 != referenceHour && temp2 == referenceHour){
 				hourlyDataArray.push(dataArray[i+1]);
 			}
 			else if(temp1 == referenceHour && temp2 == referenceHour){
 				hourlyDataArray.push(dataArray[i]);
 				hourlyDataArray.push(dataArray[i+1]);
 			}else{
 				hourlyDataArray.push(dataArray[i]);
 				break;
 			}
 		}
     }*/

     function getHourlyData(){ //give data in the format "yyyy-mm-dd" or take it from the function getDateToday() if checking the current day's results.
          var referenceHour = getHourNow()
          var dataArray = setDataArray();
          var hourlyDataArray =[];
          for(i =0; i < dataArray.length; i=i+2){
      		var temp1 = (dataArray[i].dateTime).slice(11, 13);
              if(i+1 == dataArray.length){
      			if(temp1 == referenceHour){
      				hourlyDataArray.push(dataArray[i]);
      			}
      		}else{
      			var temp2 = (dataArray[i+1].dateTime).slice(11, 13);

      			if(temp1 != referenceHour && temp2 != referenceHour){
      				continue;
      			}else if (temp1 != referenceHour && temp2 == referenceHour){
      				hourlyDataArray.push(dataArray[i+1]);
      			}
      			else if(temp1 == referenceHour && temp2 == referenceHour){
      				hourlyDataArray.push(dataArray[i]);
      				hourlyDataArray.push(dataArray[i+1]);
      			}else{
      				hourlyDataArray.push(dataArray[i]);
      				break;
      			}
      		}
          }

 	    return hourlyDataArray;
    }


  function hourlyGraphData(array){      //the array in the format of {{datetime:"2017-09-22", sensorID: 1, temperature: 27}, {}, {}  }
     graphData=[];
     for(i=0; i<array.length; i++){
        var temp = [];
        temp.push(array[i].sensorID);
        var minute = parseInt((array[i].dateTime).slice(14,16));
        var second = parseInt((array[i].dateTime).slice(17,19));
        temp.push(minute);
        temp.push(second);
        temp.push(array[i].temperature);
        graphData.push(temp);
     }
     return graphData;
  }

 function sortDataForSensor(array ,i){
	 var dataArray = [];
	 for(j=0; j<array.length; j++){
		 if(array[j].sensorID == i){
			 dataArray.push(array[j]);
		 }
	 }
	 return dataArray;
 }

 function setDailyGraphData(array) {
	 returnArray = [];
	 for(i=0; i<array.length; i++){
		 tempArray= [];
		 var id = array[i].sensorID;
		 var time = (array[i].dateTime).slice(11,19);
		 var temperature = array[i].temperature;
		 tempArray.push(id);
		 tempArray.push(time);
		 tempArray.push(temperature);
		 returnArray.push(tempArray);
	 }
	 return returnArray;
 }

 function createTemperatureArray(array){
	 returnArray = [];
	 for(i=0; i<array.length; i++){
		 returnArray.push(array[i].temperature);
	 }
	 return returnArray;
 }

 //Most Recent Data
 function setMostRecentDataArray() {
 		var array = JSON.parse(displayData);
 		return array;
 }

 function visualDataArray(){        //to display the most recent value of each sensor.
        var array = JSON.parse(displayData);
        returnArray = [];
        for(i=0; i<array.length; i++){
            temp = [];
            if(array[i].sensorID == 1){
                temp.push(60);
                temp.push(60);
            }else if(array[i].sensorID == 2){
                temp.push(450);
                temp.push(250);
            }
            temp.push(array[i].temperature);
            returnArray.push(temp);
        }
        return returnArray;
 }

