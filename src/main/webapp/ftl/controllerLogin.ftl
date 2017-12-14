<!DOCTYPE html>
<html>
<head>
    <title>Controller-Login</title>
    <script src="/temperature-data-application/static/js/loginScript.js" type="text/javascript"></script>
    <link rel="stylesheet" href="/temperature-data-application/static/css/styleSheet.css"/>
</head>

<body>
    <div class="loginBox">
        <fieldset>
            <legend> Please Enter Your Name</legend>

            <label class="loginLabel">USERNAME:</label><input id="username" type="text"><br><br>
            <!--label class="loginLabel">PASSWORD:</label><input id="password" type="password"><br><br-->
            <button type="submit" id="submit" onclick="loginOperation('user-access')">login</button><br>

        </fieldset>
    </div>
</body>

</html>