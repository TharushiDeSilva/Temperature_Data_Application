<!DOCTYPE html>
<!--Adds Dynamic colour based on the numeric value of the data array-->
<html>
<head>
    <title>Data Visualization using D3</title>
    <script type="text/javascript" src="/temperature-data-application/static/js/d3/d3.js"></script>
    <script type="text/javascript" src="/temperature-data-application/static/json/cacheDataString.json"></script>
    <script type="text/javascript" src="/temperature-data-application/static/json/mostRecentDataCacheString.json"></script>
    <script type="text/javascript" src="/temperature-data-application/static/js/jsonHandler.js"></script>
    <link rel="stylesheet" href="/temperature-data-application/static/css/styleSheet.css"/>

</head>
<body>
<div id="currentView">
    <h1 id="title1">CURRENT  TEMPERATURE  DATA</h1>
    <div id="currentDateField">
        <img class="timeIcon" src="/temperature-data-application/static/images/calender.png"><br>
        <label class="timeIcon">Date :</label>
        <label class="timeValue">${date}</label>
    </div>
    <div id ="currentTimeField">
        <img class="timeIcon" src="/temperature-data-application/static/images/clock.png">
        <label class="timeIcon">Time :</label>
        <label class="timeValue">${time}</label>
    </div>

    <div id="svgFloor">
        <script type="text/javascript">
                    var recentData = visualDataArray();
					var floor = [[10,10,300,250,"white"],[260,60,250,250,"#ffe6ff"]];

					var svg = d3.select("#svgFloor").append("svg")
								.attr("width",550)
								.attr("height",350);

					svg.selectAll("rect")
						.data(floor)
						.enter()
						.append("rect")
						.attr("x", function(d){return d[0];})
						.attr("y",function(d){return d[1];})
						.attr("height",function(d){return d[2];})
						.attr("width",function(d){return d[3];})
						.attr("fill", function(d){ return d[4];})
						.style("stroke", "#cc0099");

					svg.selectAll("circle")
						.data(recentData)
						.enter()
						.append("circle")
						.attr("cx", function(d){return d[0];})
						.attr("cy",function(d){return d[1];})
						.attr("r",40)
						.attr("fill", function(d){
                            var t = d[2];
                            if(t>=27){
                                return "rgb(255,0,0)";
                            }else if(t>=25){
                                return "rgb(0,255,0)";
                            }else{
                                return "rgb(0,255,255)";
                            }
                        });

                    svg.selectAll("text")
                        .data(recentData)
                        .enter()
                        .append("text")
                        .text(function(d){
                                var temp = d[2]+".00 *C";
                                return temp;
                              })
					    .attr("x",function(d){return d[0] +40;})
					    .attr("y", function(d){return d[1]+40;});

			var text= [[100,270,"Training Room 2"],[350,80,"Training Room 1"]];
					svg.selectAll("text")
					.data(text)
					.enter()
					.append("text")
					.text(function(d){return d[2];})
					.attr("x",function(d){return d[0];})
					.attr("y", function(d){return d[1];});
				</script>

    </div>

</div>

<div id="hourlyGraph">
    <h1 id="title2">HOURLY TEMPERATURE DATA</h1>
    <script type="text/javascript">



				var hourly = getHourlyData();

				var dataset = hourlyGraphData(hourly);   //this is the graph data of a selected hour

                var height = 500;
                var width = 1200;
                //create the svg element
                var svg = d3.select("#hourlyGraph")
                            .append("svg")
                            .attr("width", width)
                            .attr("height", height);
                //for the dataset
                svg.selectAll("rect")
                    .data(dataset)
                    .enter()
                    .append("rect")
                    .attr("x", function(d){
                            var position = d[1]*18 + 60;

                            position += d[2]*3/10;
                            return (position);
                    })
                    .attr("y", function(d){ return d[0]*100; })
                    .attr("height", 98)
                    .attr("width", 3)
                    .attr("fill", function(d){
                        var t = d[3];
                        if(t>=27){
                            return "rgb(255,0,0)";
                        }else if(t>=25){
                            return "rgb(0,255,0)";
                        }else{
                            return "rgb(0,255,255)";
                        }
                    });

                //the axes
                var axes=[[60,320,1150,320],[60,320,60,50],[60,320,60,330],[240,320,240,330],[420,320,420,330],[600,320,600,330],[780,320,780,330],[960,320,960,330],[1140,320,1140,330],[60,225,50,225],[60,125,50,125]];

                svg.selectAll("line")
                    .data(axes)
                    .enter()
                    .append("line")
                    .attr("x1", function(d){return d[0];})
                    .attr("y1", function(d){return d[1];})
                    .attr("x2", function(d){return d[2];})
                    .attr("y2", function(d){return d[3];})
                    .style("stroke","black")
                    .style("stroke-width", 2);

                //grid-lines
                var grid=[];
                for(i=-120; i<=1140; i+=18){
                    temp=[];
                    temp.push(i);
                    temp.push(320);
                    temp.push(i);
                    temp.push(50);
                    grid.push(temp);
                }
                svg.selectAll("line")
                    .data(grid)
                    .enter()
                    .append("line")
                    .attr("x1", function(d){return d[0];})
                    .attr("y1", function(d){return d[1];})
                    .attr("x2", function(d){return d[2];})
                    .attr("y2", function(d){return d[3];})
                    .style("stroke","black")
                    .style("stroke-width", 0.2);


                var hour = getHourNow();
                var next = getHourNext();

                var labels = [[5,200,"Sensor"],[650,360,"Time (h)"],[36,230,"A"],[36,130,"B"],[60,343, hour+":00"],[240,343, hour+":10"],[420,343, hour+":20"],[600,343, hour+":30"],[780,343, hour+":40"],[960,343, hour+":50"],[1140,343, next+":00"]];
				svg.selectAll("text")
					.data(labels)
					.enter()
					.append("text")
					.text(function(d){return d[2];})
					.attr("x", function(d){return d[0];})
					.attr("y", function(d){ return d[1];})
					.style("font-weight","bold");
        </script>
</div>
</body>
</html>