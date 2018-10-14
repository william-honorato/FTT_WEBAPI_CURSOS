/**
 * 
 */

function setSpanData(){
	let span = document.getElementById("spanDataFalta");
	var data = new Date();
	span.innerHTML = 'Hoje: ' + data.toLocaleDateString();
}

window.onload = function() {

	$('.selectpicker').selectpicker();
	setSpanData();
	carregarTabela();
};

function loadDdlFaltas(name,value)
{
	var ddl = "#selectAlunos";
	$(ddl).append('<option value="' + value + '">' + name + "</option>'");
}

function getAllFaltas(idAluno){
	var xhttp = new XMLHttpRequest();
	$("#selectDataFaltas").empty();//Limpar ddl
	
  	xhttp.onreadystatechange = function() {
    	if (this.readyState == 4 && this.status == 200) {//Se status ok, tem falta cadastrada
    			     
    		var JSONObject = JSON.parse(this.responseText);

			$("#selectDataFaltas").append("<option value='0'>Selecione a data da falta</option>");
    		$( "#selectDataFaltas" ).prop( "disabled", false );
    		
		    for(let i = 0; i < JSONObject.length; i++){			    
		    	$("#selectDataFaltas").append('<option value="' + JSONObject[i].idFalta + '">' + JSONObject[i].dataCadastro + "</option>'");     	
		    }
		    //Atualizar selectpicker
		    $('.selectpicker').selectpicker('refresh');
	    }
    	else if(this.status == 404){//Senão não há faltas
			$("#selectDataFaltas").append("<option value='0'>Não há faltas cadastradas</option>");
    		$( "#selectDataFaltas" ).prop( "disabled", true );
    		//Atualizar selectpicker
		    $('.selectpicker').selectpicker('refresh');
		}
	}
	  
	  xhttp.open("GET", "http://localhost:8080/FTT_WEBAPI_CURSOS/FaltasAPI?idAluno="+idAluno, true);
	  xhttp.send();
}
		
function ddlAlunos() {
	let idAluno = document.getElementById("selectAlunos").value;
    getAluno(idAluno);    
}

function ddlFaltaAlunos() {
	let idAluno = document.getElementById("selectFaltaAlunos").value;
	getAllFaltas(idAluno);
}

function loadDropDowns(name,value)
{
   var ddl = "#selectAlunos";
   var ddl2 = "#selectFaltaAlunos";
   var ddl3 = "#selectNotaAlunos";
   $(ddl).append('<option value="' + value + '">' + name + "</option>'");
   $(ddl2).append('<option value="' + value + '">' + name + "</option>'");
   $(ddl3).append('<option value="' + value + '">' + name + "</option>'");
}

function getAluno(idAluno) {
	
	var xhttp = new XMLHttpRequest();
		  
  	xhttp.onreadystatechange = function() {
    	if (this.readyState == 4 && this.status == 200) {
	    			     
    		var JSONObject = JSON.parse(this.responseText);
    		
	     	$('#txtId').val(JSONObject.idAluno);
	     	$('#txtNome').val(JSONObject.nome);
	     	$('#txtEmail').val(JSONObject.email);
	     	$('#txtCurso').val(JSONObject.curso);
	    }
	}
	  
	  xhttp.open("GET", "http://localhost:8080/FTT_WEBAPI_CURSOS/AlunosAPI?idAluno=" + idAluno, true);
	  xhttp.send();
}

function carregarTabela() {
	var xhttp = new XMLHttpRequest();
		  
  	xhttp.onreadystatechange = function() {
    	if (this.readyState == 4 && this.status == 200) {
	    
    		$("#tabelaAlunos").empty();//Limpar table
    		$("#selectAlunos").empty();//Limpar ddl
    		$("#selectAlunos").append("<option value='0'>Selecione um aluno</option>");
    		$("#selectFaltaAlunos").empty();//Limpar ddl
    		$("#selectFaltaAlunos").append("<option value='0'>Selecione um aluno</option>");
    		$("#selectNotaAlunos").empty();//Limpar ddl
    		$("#selectNotaAlunos").append("<option value='0'>Selecione um aluno</option>");
    		$( "#selectDataFaltas" ).prop( "disabled", true );
	     
    		var JSONObject = JSON.parse(this.responseText);
    		
	     			     
		     for(let i = 0; i < JSONObject.length; i++){
			    
		    	 let numFaltas = 0;
		    	 if(JSONObject[i].listaFaltas[0] != null)//Se for diferente de nulo pega o numero de faltas
			     		numFaltas = JSONObject[i].listaFaltas[0].numeroFaltas;
		    	 
		     	$('#tabelaAlunos').append("<tr><td>" + JSONObject[i].idAluno + "</td>" +
		     								  "<td>" + JSONObject[i].nome + "</td>" +
		     								  "<td>" + JSONObject[i].email + "</td>" +
		     								  "<td>" + JSONObject[i].curso + "</td>" +
		     								  "<td>" + JSONObject[i].dataCadastro + "</td>" +
		     								  "<td>" + numFaltas + "</td></tr>");
		     	
		     	
		     	loadDropDowns(JSONObject[i].nome, JSONObject[i].idAluno);		     	
		     }
		     //Atualizar selectpicker
		     $('.selectpicker').selectpicker('refresh');
	    }
	}
	  
	  xhttp.open("GET", "http://localhost:8080/FTT_WEBAPI_CURSOS/AlunosAPI", true);
	  xhttp.send();
}
                                                                        
function salvarAluno() {
	let xhttp = new XMLHttpRequest();
	let parametros = "";
	let idAluno = $("#txtId").val();
	
	if(idAluno != ""){
		parametros = 'idAluno=' + idAluno + 
					 '&nome=' + $("#txtNome").val() + 
				     '&email=' + $("#txtEmail").val() +
				     '&curso=' + $("#txtCurso").val();
	}
	else{
		parametros = 'nome=' + $("#txtNome").val() + 
					 '&email=' + $("#txtEmail").val() +
					 '&curso=' + $("#txtCurso").val();
	}
		  
  	xhttp.onreadystatechange = function() {
    	if (this.readyState == 4 && this.status == 200) {	    	
		     let JSONObject = JSON.parse(this.responseText);
		     	     
		     $("#txtId").val(JSONObject.idAluno);
		     carregarTabela();
		     $("#selectAlunos").val(idAluno);
		     $('.selectpicker').selectpicker('refresh');
		     ddlAlunos();
		     alert('Registro salvo com sucesso');	     
	    }
    	if(this.status == 404){
    		let erro = JSON.parse(this.responseText);
    		alert(erro.Status + ': ' + erro.Mensagem);
    		return;
    	}
	};
	
	if(idAluno == ""){//Se novo registro
		xhttp.open("POST", "http://localhost:8080/FTT_WEBAPI_CURSOS/AlunosAPI?"+parametros, true);
	}
	else{//Senão atualização
		xhttp.open("PUT", "http://localhost:8080/FTT_WEBAPI_CURSOS/AlunosAPI?"+parametros, true);
	}	  
	  
	  xhttp.send();
}

function deletarAluno() {
	let xhttp = new XMLHttpRequest();
	let idAluno = $("#txtId").val();
	
	if(idAluno == ""){
			alert('Selecione um aluno para excluir');
			return;
	}
		  
  	xhttp.onreadystatechange = function() {
    	if (this.readyState == 4 && this.status == 200) {	    	
		     let JSONObject = JSON.parse(this.responseText);
		     	     
		     $("#txtId").val(''); 
			 $("#txtNome").val(''); 
		     $("#txtEmail").val('');
		     $("#txtCurso").val('');
		     $("#selectAlunos").val('0');		     
		     $('.selectpicker').selectpicker('refresh');
		     carregarTabela();
		     alert(JSONObject.Status + ': ' + JSONObject.Message);	     
	    }
    	if(this.status == 404){
    		let erro = JSON.parse(this.responseText);
    		alert(erro.Status + ': ' + erro.Mensagem);
    		return;
    	}
	};
		
		xhttp.open("DELETE", "http://localhost:8080/FTT_WEBAPI_CURSOS/AlunosAPI?idAluno="+idAluno, true);	  
		xhttp.send();
}

function incluirFalta(){
	
	//Verifica se a data já está na lista
	var ddl = document.getElementById("selectDataFaltas");
    var txt = "All dates: ";    
    for (let i = 1; i < ddl.length; i++) {
        txt = txt + "\n" + ddl.options[i].text;
    }
    alert(txt);
	
	let xhttp = new XMLHttpRequest();
	let idAluno = $("#selectFaltaAlunos").val();
	
	if(idAluno == "0"){
		let data = new Date();
		alert('Selecione um aluno para incluir a falta do dia ' + data.toLocaleDateString());
		return;
	}	
		  
  	xhttp.onreadystatechange = function() {
    	if (this.readyState == 4 && this.status == 200) {	    	
		     let JSONObject = JSON.parse(this.responseText);
		     
		     $("#selectDataFaltas").val('0');		     
		     $('.selectpicker').selectpicker('refresh');
		     getAllFaltas(idAluno);
		     alert('Falta incluida com sucesso');
		     
	    }
    	else if(this.status == 404){
    		let erro = JSON.parse(this.responseText);
    		alert(erro.Status + ': ' + erro.Mensagem);
    		return;
    	}
	};
		
		xhttp.open("POST", "http://localhost:8080/FTT_WEBAPI_CURSOS/FaltasAPI?idAluno="+idAluno, true);
		xhttp.send();
}

function deletarFalta(){
	let xhttp = new XMLHttpRequest();
	let idAluno = $("#selectFaltaAlunos").val();
	let idFalta = $("#selectDataFaltas").val();
	
	if(idAluno == "0"){
			alert('Selecione um aluno para excluir');
			return;
	}
	
	if(idFalta == "0"){
		alert('Selecione a data da falta para excluir');
		return;
	}
		  
  	xhttp.onreadystatechange = function() {
    	if (this.readyState == 4 && this.status == 200) {	    	
		     let JSONObject = JSON.parse(this.responseText);
		     
		     $("#selectDataFaltas").val('0');		     
		     $('.selectpicker').selectpicker('refresh');
		     getAllFaltas(idAluno);
		     alert(JSONObject.Status + ': ' + JSONObject.Message);
		     
	    }
    	else if(this.status == 404){
    		let erro = JSON.parse(this.responseText);
    		alert(erro.Status + ': ' + erro.Mensagem);
    		return;
    	}
	};
		
		xhttp.open("DELETE", "http://localhost:8080/FTT_WEBAPI_CURSOS/FaltasAPI?idAluno="+idAluno+"&idFalta="+idFalta, true);	  
		xhttp.send();
}


function carregarTabelaNotas() {
	
	var xhttp = new XMLHttpRequest();
	let idAluno = $("#selectNotaAlunos").val();
	$("#tabelaNotaAlunos").empty();//Limpar table
	
  	xhttp.onreadystatechange = function() {
	
  		if (this.readyState == 4 && this.status == 200) {
    
  			var JSONObject = JSON.parse(this.responseText);
		    
	    	 let notas = "";
	    	 let tamLista = JSONObject.listaNotas.length;
	    	 
	    	 for(let j = 0; j < tamLista; j++){
	    		 if(j == tamLista - 1)
	    			 notas += JSONObject.listaNotas[j].nota;
	    		 else
	    			 notas += JSONObject.listaNotas[j].nota+", ";
	    	 }
	    	 
     		$('#tabelaNotaAlunos').append("<tr><td>" + JSONObject.idAluno + "</td>" +
	     								  "<td>" + JSONObject.nome + "</td>" +
	     								  "<td>" + JSONObject.curso + "</td>" +
	     								  "<td>" + notas + "</td></tr>");     	
  		}
  	}
	  xhttp.open("GET", "http://localhost:8080/FTT_WEBAPI_CURSOS/AlunosAPI?idAluno="+idAluno, true);
	  xhttp.send();
}
   

function incluirNota(){
	
	let xhttp = new XMLHttpRequest();
	let idAluno = $("#selectNotaAlunos").val();
	let notaAluno = $("#txtNotaAluno").val();
	
	if(idAluno == "0"){
		let data = new Date();
		alert('Selecione um aluno para incluir a nota');		
		return;
	}	
	
	if(notaAluno == ""){		
		alert('Digite a nota do aluno');
		$("#txtNotaAluno").select();
		return;
	}
	
	if (isNaN(notaAluno)) {
		alert('Digite um valor númerico para a nota do aluno');
		$("#txtNotaAluno").select();
		return;        
    }
		  
  	xhttp.onreadystatechange = function() {
    	if (this.readyState == 4 && this.status == 200) {	    	
		     let JSONObject = JSON.parse(this.responseText);
		     
		     carregarTabelaNotas();
		     alert('Nota incluida com sucesso');
		     
	    }
    	else if(this.status == 404){
    		let erro = JSON.parse(this.responseText);
    		alert(erro.Status + ': ' + erro.Mensagem);
    		return;
    	}
	};
		
		xhttp.open("POST", "http://localhost:8080/FTT_WEBAPI_CURSOS/NotasAPI?idAluno="+idAluno+"&nota="+notaAluno, true);
		xhttp.send();
}



