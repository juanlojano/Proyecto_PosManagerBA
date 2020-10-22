// <![CDATA[
function validarUsuario(e, maxL) {
    if (maxL.maxLength > 100 || maxL.maxLength === -1)
        return true;
    tecla = (document.all) ? e.keyCode : e.which;
    if (tecla === 8 || tecla === 9 || tecla === 16)
        return true;
    if (tecla >= 65 && tecla <= 90)
        tecla = tecla + 32;
    if (tecla >= 97 && tecla <= 122)
        return true;
    if (tecla >= 48 && tecla <= 57)
        return true;
    charVal = String.fromCharCode(tecla);
    listCS = ".-_@#*ñ";
    if (listCS !== "" && listCS.indexOf(charVal) >= 0)
        return true;
    return false;
}
// ]]>

// <![CDATA[
function validarNumeros(evt) {
    var theEvent = evt || window.event;
    var key = theEvent.keyCode || theEvent.which;
    key = String.fromCharCode(key);
    var regex = /([0-9])+/;
    if (!regex.test(key)) {
        theEvent.returnValue = false;
        if (theEvent.preventDefault)
            theEvent.preventDefault();
    }
}
// ]]>

// <![CDATA[
function validarLetras(evt) {
    var theEvent = evt || window.event;
    var key = theEvent.keyCode || theEvent.which;
    key = String.fromCharCode(key);
    var regex = /[a-zA-Z]|ñ|Ñ|\ |\./;
    if (!regex.test(key)) {
        theEvent.returnValue = false;
        if (theEvent.preventDefault)
            theEvent.preventDefault();
    }
}
// ]]>
