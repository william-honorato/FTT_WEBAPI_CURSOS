package br.edu.ftt.ec.controller;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
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

import br.edu.cefsa.ftt.ec.dao.FaltasDao;
import br.edu.cefsa.ftt.ec.dao.NotasDao;
import br.edu.cefsa.ftt.ec.model.Faltas;
import br.edu.cefsa.ftt.ec.model.Notas;

/**
 * Servlet implementation class NotasAPI
 */
@WebServlet("/NotasAPI")
public class NotasAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String JSON_ERRO_PARAMETERS = "{\"Status\" : \"Erro, Parametros Invalidos\"}";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NotasAPI() {
        super();
        // TODO Auto-generated constructor stub
    }

    private boolean isValidNumber(String value) {
    	
    	if(value == null)//Se for nulo
			return false;
		else if(value.equals(""))//Ou se for vazio
			return false;
		else {//Ou senão for número
			try {
				long val = Long.parseLong(value);
			}catch(Exception e) {
				return false;
			}
		}
    	return true;
    }
    
    private boolean isValidParameters(HttpServletRequest request, boolean validId)//Se for para validar o id passar true
    {
    	boolean retorno = true;
    	
    	retorno = isValidNumber(request.getParameter("idAluno"));
    	
    	if(request.getParameter("nota") == null)//Se for nulo
			return false;
		else if(request.getParameter("nota").equals(""))//Ou se for vazio
			return false;
		else {//Ou senão for número
			try {
				float val = Float.parseFloat(request.getParameter("nota"));
			}catch(Exception e) {
				return false;
			}
		}
		
		if(validId) {
			retorno = isValidNumber(request.getParameter("idNota"));
		}
		    	
    	return retorno;    	
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String sIdAluno = request.getParameter("idAluno");		
		long idAluno = 0;
		ArrayList<Notas> listaNotas = new ArrayList<Notas>();
		NotasDao notasDao = new NotasDao();
		String now = String.valueOf(new Date());
		Gson gson = new Gson();
	    
	    String json = "";
		
		if(sIdAluno != null)
		{
			try {
				idAluno = Long.parseLong(sIdAluno);
			}
			catch (Exception e) {
				idAluno = 0;
			}
		}
		
		try {			  
			
			if(idAluno > 0)
			{
				listaNotas = new ArrayList<Notas>(notasDao.getNotasAluno(idAluno));
			}
			else
			{
				listaNotas = new ArrayList<Notas>(notasDao.getAllNotas());					
			}
			
			if(listaNotas.size() <= 0) {
				JsonObject jsonErro = new JsonObject();
				jsonErro.addProperty("Status", "Erro");
				jsonErro.addProperty("Mensagem", "Nao ha notas cadastradas para esse aluno");
				jsonErro.addProperty("Time", String.valueOf(new Date()));			
				response.setStatus(404);
				response.getWriter().print(jsonErro.toString()); 
			}
			else {
				Type listType  = new TypeToken<List<Notas>>(){}.getType();//Pega o tipo da lista para enviar no gson
				json = gson.toJson(listaNotas, listType);
				response.setContentType("application/json"); //MIME Type
				response.setCharacterEncoding("utf-8");
				response.getWriter().print(json);
				response.flushBuffer();
			}
			
		} catch (Exception e) {
			JsonObject jsonErro = new JsonObject();
			jsonErro.addProperty("Status", "Erro");
			jsonErro.addProperty("Mensagem", e.getMessage());
			jsonErro.addProperty("Time", String.valueOf(new Date()));			
			response.setStatus(404);
			response.getWriter().print(jsonErro.toString()); 
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(isValidParameters(request, false)) {
			Notas nota = new Notas();
			
			nota.setIdAluno(request.getParameter("idAluno"));
			nota.setNota(request.getParameter("nota"));
			
			System.out.print(nota);
			
			NotasDao nd = new NotasDao();
			
			String now = String.valueOf(new Date());
			
			try {
				nd.addNota(nota);
			} catch (Exception e) {
				System.err.println(now +  " - Ops!! - " + e.getMessage());
				System.err.println(now +  " - Ops!! - " + nota);
				e.printStackTrace();
			}
			
			response.setContentType("application/json"); //MIME Type
			response.setCharacterEncoding("utf-8");
	
			Gson g = new Gson();		
			String jsonReturn = g.toJson(nota);
	
			// put some value pairs into the JSON object.		
			 response.getWriter().print(jsonReturn);	         
		}
		else {
			response.getWriter().print(JSON_ERRO_PARAMETERS);	         
		}
		response.flushBuffer();
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(isValidParameters(request, true)) {
			
			Notas nota = new Notas();
			
			nota.setIdNota(request.getParameter("idNota"));
			nota.setIdAluno(request.getParameter("idAluno"));
			nota.setNota(request.getParameter("nota"));
					
			String now = String.valueOf(new Date());
			
			NotasDao nd = new NotasDao();
			
			try {
				nd.updateNotas(nota);
			} catch (Exception e) {
				System.err.println(now +  " - Ops!! - " + e.getMessage());
				System.err.println(now +  " - Ops!! - " + nota);
				e.printStackTrace();
			}
			
			response.setContentType("application/json"); //MIME Type
			response.setCharacterEncoding("utf-8");
	
			Gson g = new Gson();		
			String jsonReturn = g.toJson(nota);
	
			// put some value pairs into the JSON object.		
			 response.getWriter().print(jsonReturn);	 
		}
		else {
			response.getWriter().print(JSON_ERRO_PARAMETERS);	         
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(isValidNumber(request.getParameter("idNota"))) {
			
			response.setContentType("application/json"); //MIME Type
			response.setCharacterEncoding("utf-8");
			
			try {
				
				String sIdNota = request.getParameter("idNota");
				long idNota = Long.parseLong(sIdNota);
				NotasDao fd = new NotasDao();
								
				Notas nota = fd.getNotaId(idNota);
				boolean erro = true;
				
				if(nota != null) {
					if(nota.getIdAluno() > 0) {
						fd.deleteNota(idNota);	
						erro = false;
					}						
				}
				
				String status = "OK";
				String message = "Nota deletada!";
				String now = String.valueOf(new Date());
			
				if(erro) {
					status = "ERRO";
					message = "Erro ao excluir, nota nao encontrado!";				
				}		
			
				JsonObject json = new JsonObject();
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
			jsonErro.addProperty("Mensagem", "O ID da nota eh invalido");
			jsonErro.addProperty("Time", String.valueOf(new Date()));			
			response.setStatus(404);
			response.getWriter().print(jsonErro.toString());   	         
		}
		response.flushBuffer();
	}
}
