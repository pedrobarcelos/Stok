function httpReq(theUrl){
        var xmlHttp = new XMLHttpRequest();
        xmlHttp.open( "GET", theUrl, false ); // false for synchronous request
        xmlHttp.send( null );
        return JSON.parse(xmlHttp.responseText);
}
window.onload = function() {

    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
      return new bootstrap.Tooltip(tooltipTriggerEl)
    })

    var excluirtool = document.querySelectorAll(".excl-tooltip")
    for (let i = 0; i < excluirtool.length; i++) {
                 var campotamanho = excluirtool[i].getAttribute("value")
                 excluirtool[i].setAttribute("title", "Isso afetará apenas o tamanho " + campotamanho)
                 excluirtool[i].setAttribute("data-bs-original-title", "Isso afetará apenas o tamanho " + campotamanho)
    }

}