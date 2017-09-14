function getContextRoot() {
    var pathName = window.location.pathname;
    var count = (pathName.match(new RegExp("/", "g")) || []).length;
    var contextRoot;
    if (count > 2) {
        var indices = [];
        for (var i = 0; i < pathName.length; i++) {
            if (pathName[i] === "/") {
                indices.push(i);
                if (indices.length === 2)
                    break;
            }
        }
        contextRoot = pathName.substring(indices[0], indices[1]) + "/";
    } else {
        var lastIndex = pathName.lastIndexOf("/");
        contextRoot = pathName.substring(0, lastIndex) + "/";
    }
    return contextRoot;
}

var serverPath = window.location.protocol + "//" + window.location.host + getContextRoot();

function loginOperation(targetServlet){
    var url = window.location.href;

    // create a form post manually

    var form = document.createElement("form");
    form.setAttribute("method", "post");
    form.setAttribute("action",  serverPath + targetServlet + window.location.search);

     var nameField = document.createElement("input");
     nameField.setAttribute("type", "hidden");
     nameField.setAttribute("name", "username");
     nameField.setAttribute("value",  document.getElementById("username").value);

     form.appendChild(nameField);
     document.body.appendChild(form);
     form.submit();
}

function viewData(targetServlet) {
    var form = document.createElement("form");
    form.setAttribute("method", "post");
    form.setAttribute("action", serverPath + targetServlet + window.location.search);

    var nameField = document.createElement("input");
    nameField.setAttribute("type", "hidden");
    nameField.setAttribute("name", "accessType");
    nameField.setAttribute("value", "granted");

    form.appendChild(nameField);
    document.body.appendChild(form);
    form.submit();

}

function changeTimeStamp(){

}
