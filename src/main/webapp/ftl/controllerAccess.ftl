<!DOCTYPE>
<html>
<head>
    <title>Controller-Access</title>
    <script src="/temperature-data-application/static/js/loginScript.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/temperature-data-application/static/css/styleSheet.css"/>
</head>

<body>
<fieldset>
    <legend>Data Access Status</legend>
    <#if username == "">
        Welcome Visitor!<br>
        You must Login First<br>
        <a href="/temperature-data-application/user-access"><button id="back-to-home">Click to Continue</button></a>
        <#else>
            Welcome ${username}!<br>
            Permission Status: ${accessType}<br>
            <#if accessType == "granted">
                You have Permission to view the Data.
                <button id="view-data" onclick="viewData('user-access')">Continue</button>
                <#else>
                    You Don't have access to view Data.
            </#if>
    </#if>
</fieldset>
</body>
</html>