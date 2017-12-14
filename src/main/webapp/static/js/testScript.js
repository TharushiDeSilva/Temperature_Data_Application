t = "00.00.00";
$(document).ready(function(){

   graphRectangles = "";             //the global variable that will only add up the elements.

    function drawHeatMap(){
        $.get("/temperature-data-application/static/json/mockData.json", function(responseJson){
        //$.get("/temperature-data-application/cache-handler", function(responseJson){
            var t1 = t;
            var t2 = generateTime();
            var array = responseJson['data'];
            //var array = jsonArray;

            var graphDynamics = "";               //this variable will change its value in every cycle.

            var returnArray = [];           //for the heat map
            var data = []                   //for the graph --------------------------------------***

            for(i=0; i<array.length; i++){
                var temp = [];      //for returnArray[]
                temp.push(array[i]['location']['x']);
                temp.push(array[i]['location']['y']);


                var dataSet = array[i]['values'];
                temp.push(dataSet[dataSet.length-1]['temperature']);
                temp.push(dataSet[dataSet.length-1]['time']);
                temp.push(array[i]['sensorID']);

                returnArray.push(temp);

                for(k=0; k<dataSet.length; k++){                    //------------------------------------***
                    //if((array[i].time).localeCompare(from)>=0 && (array[i].time).localeCompare(to) <= 0){
                    if(dataSet[k]['time'].localeCompare(t1)>=0 && dataSet[k]['time'].localeCompare(t2)<=0){
                        var temp2 = [];     //for data              //------------------------------------***
                        temp2.push(i);           //the y coordinate ------------------------------------***
                        temp2.push(parseInt(dataSet[k]['time'].slice(3,5)));         //------------------------------------***
                        temp2.push(parseInt(dataSet[k]['time'].slice(6,8)));      //------------------------------------***
                        temp2.push(dataSet[k]['temperature']);      //------------------------------------***
                        data.push(temp2);                           //------------------------------------***
                    }
                }

            }


            var currentHour = t2.slice(0,2);


            var htmlMap = "<svg id =\"hm\" width=\"280\" height=\"240\">";
            for(i=0; i<returnArray.length; i++){
                htmlMap += "<rect x=\""+returnArray[i][0]*40 + "\" y=\""+returnArray[i][1]*40 + "\" height=\"40\" width=\"40\" fill=\""+gradientValue(returnArray[i][2])+"\"><title>Last Data Received at:"+returnArray[i][3]+"</title></rect>";
                htmlMap += "<text x=\""+ (returnArray[i][0]*40+5) + "\" y= \""+ (returnArray[i][1]*40 +15) + "\" font-size=\"10px\" fill=\"black\">" + returnArray[i][2]+"</text>";
                htmlMap += "<text x=\""+ (returnArray[i][0]*40+30) + "\" y= \""+ (returnArray[i][1]*40 +35) + "\" font-size=\"10px\" font-weight=\"bold\" fill=\"black\">" + returnArray[i][4]+"</text>";

            }
            htmlMap+="</svg>";
            $("#heatmap_container").html($(htmlMap));


        //the graph
            var numOfSensors = array.length;
            var canvasHeight = numOfSensors*72+19;
            graphDynamics += "<svg id=\"graph_canvas\" width=\"735\" height=\""+ canvasHeight +"\">";
            graphDynamics += "<text x=\"10\" y=\""+(canvasHeight-4)+"\" font-size=\"10px\" font-weight=\"bold\">"+currentHour+":00h</text>";
            graphDynamics += "<text x=\"702\" y=\""+(canvasHeight-4)+"\" font-size=\"10px\" font-weight=\"bold\">"+getNextHour(currentHour)+":00h</text>";
            //minutes labels
            graphDynamics += "<text x=\"130\" y=\""+(canvasHeight-4)+"\" font-size=\"10px\" font-weight=\"bold\">:10</text>";
            graphDynamics += "<text x=\"250\" y=\""+(canvasHeight-4)+"\" font-size=\"10px\" font-weight=\"bold\">:20</text>";
            graphDynamics += "<text x=\"370\" y=\""+(canvasHeight-4)+"\" font-size=\"10px\" font-weight=\"bold\">:30</text>";
            graphDynamics += "<text x=\"490\" y=\""+(canvasHeight-4)+"\" font-size=\"10px\" font-weight=\"bold\">:40</text>";
            graphDynamics += "<text x=\"610\" y=\""+(canvasHeight-4)+"\" font-size=\"10px\" font-weight=\"bold\">:50</text>";

            graphDynamics += "<text x=\"300\" y=\""+(canvasHeight-4)+"\" font-size=\"10px\" font-weight=\"bold\">Time</text>";

            //sensor Labels for graph y axis
            var yStart = 40;
            for(m=0; m<returnArray.length; m++){
                graphDynamics += "<text x=\"2\" y=\""+yStart+"\" font-size=\"10px\" font-weight=\"bold\">"+returnArray[m][4]+"</text>";
                yStart +=72;
            }

            //adding graph axes.
            var line_height = numOfSensors*72+5;
            //htmlGraph += "<line x1=\"3\" y1=\"3\" x2=\"725\" y2=\"3\" stroke-width=\"1\" stroke=\"black\"/>";
            graphDynamics += "<line x1=\"13\" y1=\"3\" x2=\"13\" y2=\""+ line_height+"\" stroke-width=\"1\" stroke=\"black\"/>";

            //adding y axes
            var x = 13;
            for(l=0; l<13; l++){
                graphDynamics += "<line x1=\""+x+"\" y1=\"3\" x2=\""+x+"\" y2=\""+ line_height+"\" stroke-width=\"0.5\" stroke=\"black\"/>";
                x+=60;
            }
            var y=78;       //adding x axes to the graph
            for(m=0; m<numOfSensors; m++){
                graphDynamics += "<line x1=\"13\" y1=\""+y+"\" x2=\"735\" y2=\""+y+"\" stroke-width=\"1\" stroke=\"black\"/>";
                y+=72;
            }

            for(j=0; j<data.length; j++){
                var xCoordinate = data[j][1]*12 + data[j][2]/5 +13;
                var yCoordinate = (data[j][0]+1)*72+5 - data[j][3]*2;
                if(t1 != "00.00.00"){
                    graphRectangles += "<rect x=\""+xCoordinate + "\" y=\"" +yCoordinate+ "\" height=\""+(data[j][3]*2)+"\" width=\"2\" fill=\""+gradientValue(data[j][3])+"\"></rect>";
                }
            }
            if(numOfSensors == 0){
                graphDynamics = "";
            }
            var graphFinalHtml = graphDynamics+graphRectangles+"</svg>";
            console.log(graphFinalHtml.length);
            $("#temperature_graph_container").html($(graphFinalHtml));

        });
    };
    drawHeatMap();
    setInterval(drawHeatMap, 10000);   //update the html content every 20 seconds.
});

function generateTime(){
    var d = new Date();
    var timeStamp = formatIntegers(d.getHours());
    timeStamp += "."+formatIntegers(d.getMinutes());
    timeStamp += "."+formatIntegers(d.getSeconds());
    t = timeStamp;
    return timeStamp;

}

function formatIntegers(number){
    var returnNum = number.toString();
    if (returnNum.length == 1){
        returnNum = "0"+returnNum;
    }
    return returnNum;
}


function gradientValue(rawValue){
    var min = 15.00;
    var max = 35.00;
    var value = (rawValue - min)/(max-min);
    value = Number(value.toFixed(2));

    var numOfColors = 5;
    var colorArray = [[0,0,1],[0,1,1],[0,1,0],[1,1,0],[1,0,0]]; // blue = [0,0,255], cyan = [0,255,255],green=[0,255,0], yellow=[255,255,0], red=[255,0,0]
    var index1;
    var index2;    //the desired color will be between these two indexes.
    var fractBetween = 0;
    if(value<=0){   //for a value less than the range.
        index1 = 0;
        index2 = 0;
    } else if(value>=1){             //for a value above the specified range.
        index1 = numOfColors -1;
        index2 = numOfColors -1;
    }else{
        value = value*(numOfColors-1);  //multiplied the value by 4.
        index1 = Math.floor(value);
        index2 = index1 +1;
        fractBetween = value - index1;      //distance between the two indexes
    }
    fractBetween = Number(fractBetween.toFixed(2));

    var red = 0;
    var green = 0;
    var blue  = 0;
    if((index1==0) && (index2==0)){
        blue = 255;
    }else if((index1==0) && (index2==1)){
        green = 255*fractBetween;
        blue = 255;
    }else if((index1==1) && (index2==2)){
        green = 255;
        blue = 255 - 255*fractBetween;
    }else if((index1==2) && (index2==3)){
        red = 255*fractBetween;
        green = 255;
    }else if((index1==3) && (index2==4)){
        red = 255;
        green = 255-255*fractBetween;
    }else{  // 4,4
        red = 255;
    }
    return "rgb("+red.toString()+","+green.toString()+","+blue.toString()+")";

}

function getNextHour(currentHour){
    var nextHour = parseInt(currentHour)+1;
    if(nextHour == 24){
        nextHour = 0;
    }
    nextHour = nextHour.toString();
    if(nextHour.length == 1){
        nextHour = "0"+nextHour;
    }
    return nextHour;
}