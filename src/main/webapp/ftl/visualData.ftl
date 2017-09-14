<! DOCTYPE html>
<html>
<head>
    <title>Sensor Data</title>
    <link rel="stylesheet" href="/temperature-data-application/static/css/styleSheet.css"/>
    <script src="/temperature-data-application/static/js/loginScript.js" type="text/javascript"></script>
</head>

<body>
<h1>Temperature and Humidity Data</h1>

<table>
    <tr class="even">
        <th></th>
        <th scope="col">Temperature</th>
        <th scope="col">Humidity</th>
    </tr>
    <tr class="odd">
        <th scope="row">${timeStamp}</th>
        <td>${temperature}<sup>o</sup>C</td>
        <td>${humidity}%</td>
    </tr>
    <tr class="even"></tr>
    <tr class="odd"></tr>
    <tr class="even"></tr>
    <tr class="odd"></tr>
</table>

</body>
</html>