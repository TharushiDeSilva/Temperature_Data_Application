<!DOCTYPE HTML>
<html>
<head>
    <title>Error Page</title>
</head>
<body>
    <#if message=="missing_parameters">
        Username and Password Should not be empty!<br>
        Please complete the login operation.<br>
        <#elseif message=="password_mismatch">
            Password Mismatch.<br>
            Please login again.<br>
        <#elseif message=="unregistered_user">
            Unregistered User<br>
            You don't have access<br>
    </#if>
    <a href="/temperature-data-application/test">Click to Login</a><br>

</body>
</html>