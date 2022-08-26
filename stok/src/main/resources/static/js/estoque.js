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
                 var campoNome = excluirtool[i].getAttribute("value")
                 excluirtool[i].setAttribute("title", "Isso afetará todos os " + campoNome)
                 excluirtool[i].setAttribute("data-bs-original-title", "Isso afetará todos os " + campoNome)
    }

    var trs = document.querySelectorAll(".nome-produto")
    for (let i = 0; i < trs.length; i++) {
             trs[i].addEventListener("click", function(event){
                 var hrefTr = trs[i].getAttribute("href")
                window.location = hrefTr
             })
    }


    var thNome = document.getElementById("th-nome");
    var thPreco = document.getElementById("th-preco");
    var thQtd = document.getElementById("th-qtd");
    var url_atual = window.location.pathname;

    if(url_atual == "/estoque/filtrar/preco_decrescente"){
       thPreco.setAttribute("style" , "color: green;");
    }else if(url_atual == "/estoque/filtrar/preco_crescente"){
       thPreco.setAttribute("style" , "color: red;");
    }else if(url_atual == "/estoque/filtrar/quantidade_decrescente"){
       thQtd.setAttribute("style" , "color: green;");
    }else if(url_atual == "/estoque/filtrar/quantidade_crescente"){
        thQtd.setAttribute("style" , "color: red;");
    }else if(url_atual == "/estoque/filtrar/alfabetico"){
        thNome.setAttribute("style" , "color: green;");
    }

    var formPesquisar = document.getElementById("formPesquisar")
    var returnHome = "returnBackHomeEstoque"
    formPesquisar.setAttribute("action", "/estoque/pesquisar/" + returnHome)

     if(window.location.search == '?error=produto%20ja%20existe'){
         alert('O produto que vc está tentando cadastrar já existe no sistema. Clique em "incrementar" ou em "novo tamanho" no produto desejado.')
     }

     if(window.location.search == '?error=quantidadeLimite'){
              alert('Quantidade excedida. Operação cancelada.')
          }


    if(window.location.search == '?error=produto%20nao%20existe'){
             alert('Você tentou acessar um produto que não existe. Tente novamente!')
    }

     if(window.location.search == '?error=dados%20incompativeis'){
         alert('Você tentou cadastrar um tamanho novo para um produto que já existe, porém inseriu dados incompatíveis com o mesmo. Reveja os campos na tela de cadastro e tente novamente.')
     }
    var btnIncrementarFora = document.querySelectorAll(".btnIncrementarProduto")
    var btnIncrementarDentro = document.getElementById("btnModalIncrementar")

    var campoDesconto = document.getElementById("campoDesconto")
    campoDesconto.addEventListener("change", function(){
         var formModalDesconto = document.getElementById("formModal3Desconto")
         var hrefD= "/carrinho/alterar/desconto/" + campoDesconto.value
         formModalDesconto.setAttribute("action", hrefD)
        var labelValue = document.getElementById("labelvalue")
        var valor = labelValue.getAttribute("value")
        labelValue.setAttribute("max", valor)
    })

    $("document").ready(function(){
      $("#campoDesconto").attr("max", $("#labelvalue").attr("value"))
    });
    var inptQtd = document.getElementById("modalIncrementarQuantidade")
    inptQtd.addEventListener("change", function(){
            var dentro = document.getElementById("btnModalIncrementar")
            var codeProduto = dentro.getAttribute("value")
            var formModalIncrementar = document.getElementById("formModalIncrementar")
            var qtd = document.getElementById("modalIncrementarQuantidade").value
            var btnHref = "/estoque/produto/incrementar/" + codeProduto + "/" + qtd
            formModalIncrementar.setAttribute("action", btnHref)
    })

    var btnAlterar = document.querySelectorAll(".btnAlterarPoduto")

   var incrmtQtd = document.getElementById("incrementar-qtd")
   var campoTam = document.getElementById("inputGroupSelect01")
   var ModalIncre = document.getElementById("formModal2Incrementar")
   var btnIncrem = document.querySelectorAll(".btnIncrementarProduto")
   for (let i = 0; i < btnIncrem.length; i++) {
           btnIncrem[i].addEventListener("click", function(event){

                var campoNome = document.getElementById("campoNomeIncrementar2")

                campoNome.setAttribute("value", btnIncrem[i].getAttribute("value"))

                var origin = window.location.origin

                var nome = btnIncrem[i].getAttribute("value")

                var searchURL = origin + "/estoque/buscar/tamanhos/" + nome

                var tamanhos = httpReq(searchURL)
                /*  {
                    tam: []
                    }
                */
                var slct = document.getElementById("inputGroupSelect01")
                slct.innerHTML = ""
                for (let i = 0; i < tamanhos.tam.length; i++) {
                    var path =  slct.innerHTML + "<<option value=\"" + tamanhos.tam[i] + "\">"  + tamanhos.tam[i] +  "</option>"
                    slct.innerHTML = path
                }

                campoTam.addEventListener("change", function(){
                    var nome = btnIncrem[i].getAttribute("value")

                    var origin = window.location.origin

                    var searchURL = origin + "/estoque/buscar/" + campoTam.value + "/" + nome

                    var produtoObj = httpReq(searchURL)

                    var btnHref = "/estoque/produto/incrementar/" + produtoObj.code + "/" + incrmtQtd.value

                    ModalIncre.setAttribute("action", btnHref)
                })

                incrmtQtd.addEventListener("change", function(){

                    var nome = btnIncrem[i].getAttribute("value")

                    var origin = window.location.origin

                    var searchURL = origin + "/estoque/buscar/" + campoTam.value + "/" + nome

                    var produtoObj = httpReq(searchURL)

                    var btnHref = "/estoque/produto/incrementar/" + produtoObj.code + "/" + incrmtQtd.value

                    ModalIncre.setAttribute("action", btnHref)

                })



           })
   }


    var btnAlterar = document.querySelectorAll(".btnAlterarProduto")
    for (let i = 0; i < btnAlterar.length; i++) {
        btnAlterar[i].addEventListener("click", function(event){
            document.querySelector("#incrementarModalLabel").innerText = "Alterar produto"
            document.querySelector("#btnModalIncrementar").innerText = "Alterar"

            var codeProduto = btnIncrementarFora[i].getAttribute("value")
            var campoNome = document.querySelector("#campoNomeIncrementar")
            var campoTamanho = document.querySelector("#campoTamanhoIncrementar")
            var campoPreco = document.querySelector("#campoPrecoIncrementar")
            var campoDesc = document.querySelector("#campoDescricaoIncrementar")
            var divQtd = document.querySelector("#divQuantidade")
            var campoQtd = document.querySelector("#modalIncrementarQuantidade")
            divQtd.setAttribute("hidden", "hidden")
            divQtd.setAttribute("disabled", "disabled")

            btnAlterar[i].setAttribute("title", "Isso afetará todos os " + campoNome)
            btnAlterar[i].setAttribute("data-bs-original-title", "Isso afetará todos os " + campoNome)

            campoPreco.removeAttribute("hidden")
            campoDesc.removeAttribute("hidden")
            divPreco.removeAttribute("hidden")
            divDesc.removeAttribute("hidden")

            campoPreco.removeAttribute("disabled")
            campoDesc.removeAttribute("disabled")

            var codeProduto = btnAlterar[i].getAttribute("value")

            var formModalIncrementar = document.getElementById("formModalIncrementar")
            var qtd = document.getElementById("modalIncrementarQuantidade").value
            var btnHref = "/estoque/produto/atualizar/" + codeProduto

            formModalIncrementar.setAttribute("action", btnHref)

            var origin = window.location.origin

            var searchURL = origin + "/estoque/buscar/" + codeProduto

            var produtoObj = httpReq(searchURL)

            campoNome.setAttribute("value", produtoObj.nome)
            campoTamanho.setAttribute("value", produtoObj.tamanho)
            campoPreco.setAttribute("value", produtoObj.preco)
            campoDesc.setAttribute("value", produtoObj.descricao)
            campoQtd.setAttribute("value", produtoObj.quantidade)

        })

    }



    var btnNovoProduto = document.querySelector("#btnAdicionarProduto")
    btnNovoProduto.addEventListener("click", function(event){
        var form = document.querySelector("#formNovoProduto")
        document.querySelector("#exampleModalLabel").innerText = "Novo produto"
        document.querySelector("#btnModalCriar").innerText = "Criar"
        form.setAttribute("action", "/produto/salvar")
        var campoNome = document.querySelector("#editarProdutoNome")
        var campoPreco = document.querySelector("#editarProdutoPreco")
        var campoDesc = document.querySelector("#editarProdutoDesc")
        campoPreco.removeAttribute("disabled")
        campoDesc.removeAttribute("disabled")
        campoNome.removeAttribute("disabled")
    })




    var btnNovoTamanhoProduto = document.querySelectorAll(".btnNovoTamanhoProduto")
        for (let i = 0; i < btnNovoTamanhoProduto.length; i++) {
            btnNovoTamanhoProduto[i].addEventListener("click", function(event){
                document.querySelector("#exampleModalLabel").innerText = "Novo tamanho"
                document.querySelector("#btnModalCriar").innerText = "Criar novo tamanho"
                var form = document.querySelector("#formNovoProduto")

                var codeProduto = btnNovoTamanhoProduto[i].getAttribute("value")
                var campoNome = document.querySelector("#editarProdutoNome")
                var campoPreco = document.querySelector("#editarProdutoPreco")
                var campoDesc = document.querySelector("#editarProdutoDesc")
                campoPreco.setAttribute("disabled", "disabled")
                campoDesc.setAttribute("disabled","disabled")
                campoNome.setAttribute("disabled","disabled")

                var origin = window.location.origin
                var searchURL = origin + "/estoque/buscar/" + codeProduto
                var produtoObj = httpReq(searchURL)

                var btnHref = "/produto/salvar/" + codeProduto

                form.setAttribute("action", btnHref)




                campoNome.setAttribute("value", produtoObj.nome)
                campoPreco.setAttribute("value", produtoObj.preco)
                campoDesc.setAttribute("value", produtoObj.descricao)

            })
        }

    var inptPesq = document.getElementById("inputPesquisar")
        inptPesq.addEventListener("change", function(){
                var formPesquisar = document.getElementById("formPesquisar")
                var pattern = document.getElementById("inputPesquisar").value
                var inptHref = "/estoque/pesquisar/" + pattern
                formPesquisar.setAttribute("action", inptHref)
        })
}
