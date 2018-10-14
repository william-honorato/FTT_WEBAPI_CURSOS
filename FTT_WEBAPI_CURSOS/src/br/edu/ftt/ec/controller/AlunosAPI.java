package br.edu.ftt.ec.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import br.edu.cefsa.ftt.ec.dao.AlunosDao;
import br.edu.cefsa.ftt.ec.model.Alunos;

/**
 * Servlet implementation class CursosAPI
 */
@WebServlet("/AlunosAPI")
public class AlunosAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static String JSON_ERRO_PARAMETERS = "{\"Status\" : \"Erro, Parametros Invalidos\"}";   
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlunosAPI() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private boolean isValidNumber(HttpServletRequest request) {
    	
    	if(request.getParameter("idAluno") == null)//Se for nulo
			return false;
		else if(request.getParameter("idAluno").equals(""))//Ou se for vazio
			return false;
		else {//Ou senão for número
			try {
				long val = Long.parseLong(request.getParameter("idAluno"));
			}catch(Exception e) {
				return false;
			}
		}
    	return true;
    }
    
    private boolean isValidParameters(HttpServletRequest request, boolean validId)//Se for para validar o id passar true
    {
    	boolean retorno = true;
    	
    	if(request.getParameter("nome") == null)
			return false;
    	else if(request.getParameter("nome").equals(""))
    		return false;

		if(request.getParameter("email") == null)
			return false;
		else if(request.getParameter("email").equals(""))
			return false;

		if(request.getParameter("curso") == null)
			return false;
		else if(request.getParameter("curso").equals(""))
			return false;
		
		if(validId) {
			retorno = isValidNumber(request);
		}
		    	
    	return retorno;    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String sIdAluno = "";
		long idAluno = 0;
		ArrayList<Alunos> listaAlunos = new ArrayList<Alunos>();
		AlunosDao alunosDao = new AlunosDao();
		String now = String.valueOf(new Date());		
		Gson gson = new Gson();
		JsonObject jsonErro = new JsonObject();
	    String json = "";
	    String mensagem = "";
	    response.setContentType("application/json"); //MIME Type
		response.setCharacterEncoding("utf-8");
		
		try {
			if(request.getParameter("idAluno") != null) 
			{
				sIdAluno = request.getParameter("idAluno").trim();
				if(sIdAluno.equals("") || sIdAluno.equals("0")) 
				{
					sIdAluno = null;
				}
			}
			else 
			{
				//retorna nulo se não tiver ? depois da url
				String qs = request.getQueryString();
				if(qs != null)
				{
					sIdAluno = null;
				}
			}
			
			if(sIdAluno != null)//Se valor do parametro for válido
			{
				if(!sIdAluno.equals("")) 
				{
					try {//Tenta converter para inteiro
						idAluno = Long.parseLong(sIdAluno);
					}
					catch (Exception e) {
						idAluno = -1;//NAN
					}
				}
				else 
				{
					idAluno = 0;//Trazer todos alunos
				}			
		
				if(idAluno == -1) 
				{//se NAN
					mensagem = "O valor do parametro eh invalido";
				}
				else if(idAluno > 0)
				{
					Alunos aluno = alunosDao.getAlunoId(idAluno);
					
					if(aluno != null) {
						if(aluno.getIdAluno() > 0)
							json = gson.toJson(aluno);
					}
					
					if(json.equals(""))
						mensagem = "Aluno nao encontrado no banco de dados";					
				}
				else
				{
					listaAlunos = new ArrayList<Alunos>(alunosDao.getAllAlunos());
					
					if(listaAlunos.size() > 0) {
						Type listType  = new TypeToken<List<Alunos>>(){}.getType();//Pega o tipo da lista para enviar no gson
						json = gson.toJson(listaAlunos, listType);
					}
					else {
						mensagem = "Nao existe aluno cadastrado no Banco de Dados";
					}
				}
			}
			else {			
				mensagem = "Parametro ou valor invalido";
			}
			
		 }catch (Exception e) {
			e.printStackTrace();				
			mensagem = e.getMessage();
		}		
		
		if(json.equals("")) {//Se json vazio erro
			jsonErro.addProperty("Status", "Erro");
			jsonErro.addProperty("Mensagem", mensagem);
			jsonErro.addProperty("Time", now);
			json = jsonErro.toString();
			response.setStatus(404);
		}
		
		response.getWriter().print(json);
		response.flushBuffer();
	}
		
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(isValidParameters(request, false)) {
			
			try {
				Alunos aluno = new Alunos(
											request.getParameter("nome"),
											request.getParameter("email"),
							        		request.getParameter("curso")
							        	 );
				
				AlunosDao ad = new AlunosDao();				
				String now = String.valueOf(new Date());				
				ad.addAlunos(aluno);			
				response.setContentType("application/json"); //MIME Type
				response.setCharacterEncoding("utf-8");
	
				Gson g = new Gson();		
				String jsonReturn = g.toJson(aluno);	
				response.getWriter().print(jsonReturn);	   
				
			} catch (Exception e) {
				JsonObject jsonErro = new JsonObject();
				jsonErro.addProperty("Status", "Erro");
				jsonErro.addProperty("Mensagem", e.getMessage());
				jsonErro.addProperty("Time", String.valueOf(new Date()));			
				response.setStatus(404);
				response.getWriter().print(jsonErro.toString()); 
			}
		}
		else {
			JsonObject jsonErro = new JsonObject();
			jsonErro.addProperty("Status", "Erro");
			jsonErro.addProperty("Mensagem", "Parametros ou valores invalidos");
			jsonErro.addProperty("Time", String.valueOf(new Date()));			
			response.setStatus(404);
			response.getWriter().print(jsonErro.toString());
		}
		response.flushBuffer();
	}
		
	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(isValidParameters(request, true)) {
			
			try {
				Alunos aluno = new Alunos(
				request.getParameter("idAluno"), //ID sera gerado no BD pela sequence
				request.getParameter("nome"),
				request.getParameter("email"),
        		request.getParameter("curso"));
					
				String now = String.valueOf(new Date());
			
				AlunosDao pd = new AlunosDao();
			
			   	pd.updateAlunos(aluno);			
			
				response.setContentType("application/json"); //MIME Type
				response.setCharacterEncoding("utf-8");
				
				//Devolve o aluno atualizado
				Gson gson = new Gson();
				String json = gson.toJson(aluno);
			   
				response.getWriter().print(json);
			
			} catch (Exception e) {
				JsonObject jsonErro = new JsonObject();
				jsonErro.addProperty("Status", "Erro");
				jsonErro.addProperty("Mensagem", e.getMessage());
				jsonErro.addProperty("Time", String.valueOf(new Date()));			
				response.setStatus(404);
				response.getWriter().print(jsonErro.toString()); 
			}
		}
		else {
			JsonObject jsonErro = new JsonObject();
			jsonErro.addProperty("Status", "Erro");
			jsonErro.addProperty("Mensagem", "Parametros ou valores invalidos");
			jsonErro.addProperty("Time", String.valueOf(new Date()));			
			response.setStatus(404);
			response.getWriter().print(jsonErro.toString());         
		}
		response.flushBuffer();
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if(isValidNumber(request)) {
			
			response.setContentType("application/json"); //MIME Type
			response.setCharacterEncoding("utf-8");
			
			try {
				String sIdAluno = request.getParameter("idAluno");
				long idAluno = Long.parseLong(sIdAluno);
				
				AlunosDao alunosDao = new AlunosDao();
				Alunos aluno = alunosDao.getAlunoId(idAluno);
				boolean erro = true;
				
				if(aluno != null) {
					if(aluno.getIdAluno() > 0) {
						alunosDao.deleteAlunos(idAluno);
						erro = false;
					}						
				}
				
				String status = "OK";
				String message = "Aluno deletado!";
				String now = String.valueOf(new Date());
				JsonObject json = new JsonObject();
			
				if(erro) {
					status = "ERRO";
					message = "Erro ao excluir, aluno nao encontrado!";	
					
				}
				
				json.addProperty("Status", status);
				json.addProperty("Message", message);
				json.addProperty("Time", now);
				response.getWriter().print(json.toString());
				
			} catch (Exception e) {
				JsonObject jsonErro = new JsonObject();
				jsonErro.addProperty("Status", "Erro");
				jsonErro.addProperty("Mensagem", e.getMessage());
				jsonErro.addProperty("Time", String.valueOf(new Date()));			
				response.setStatus(404);
				response.getWriter().print(jsonErro.toString()); 
			}	         
		}
		else {
			JsonObject jsonErro = new JsonObject();
			jsonErro.addProperty("Status", "Erro");
			jsonErro.addProperty("Mensagem", "O ID do aluno eh invalido");
			jsonErro.addProperty("Time", String.valueOf(new Date()));			
			response.setStatus(404);
			response.getWriter().print(jsonErro.toString());         
		}
		response.flushBuffer();
	}
}
