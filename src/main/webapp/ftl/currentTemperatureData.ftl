<!DOCTYPE html>
<html>

<head>
    <script type="text/javascript" src="/temperature-data-application/static/js/d3/d3.js"></script>
</head>
<body>

    <#if sensorID == "1">
        <h2>Data for Sensor 1</h2>
        Date:   ${date}<br>
        Time:   ${time}<br>
        Temperature: ${temperature}<sup>o</sup>C<br>
        humidity: ${humidity} %RH<br>
        Heat Index:  ${heatIndex} <sup>o</sup>C<br><hr>
        <#else>
            <h2>Data for Sensor 2</h2><br>
            Date:   ${date}<br>
            Time:   ${time}<br>
            Temperature: ${temperature}<sup>o</sup>C<br>
            humidity: ${humidity} %RH<br>
            Heat Index:  ${heatIndex} <sup>o</sup>C<br>
    </#if>

    <script type="text/javascript">
        var dataset = [${temperature}];
        //create the svg element
                var svg = d3.select("body").append("svg")
                            .attr("width", 190)
                            .attr("height", 100);
                //for the sensor1
                svg.selectAll("rect").data(dataset).enter().append("rect")
                    .attr("x", 20)
                    .attr("y", 20)
                    .attr("width", 80)
                    .attr("height", 80)
                    .attr("fill", function(d){
                        if(d>=28){
                            return "rgb(255,0,0)";
                        }else if(d>=24){
                            return "rgb(0,255,0)";
                        }else{
                            return "rgb(0,255,255)";
                        }
                    });
				svg.selectAll("text")
					.data(dataset)
					.enter()
					.append("text")
					.text(function(d){return d;})
					.attr("x",40)
					.attr("y", 19);

    </script>
    <hr>
</body>

</html>
