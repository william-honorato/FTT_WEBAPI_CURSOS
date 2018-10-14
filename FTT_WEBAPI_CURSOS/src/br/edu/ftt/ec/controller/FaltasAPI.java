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
import br.edu.cefsa.ftt.ec.model.Faltas;

/**
 * Servlet implementation class FaltasAPI
 */
@WebServlet("/FaltasAPI")
public class FaltasAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String JSON_ERRO_PARAMETERS = "{\"Status\" : \"Erro, Parametros Invalidos\"}";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FaltasAPI() {
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
		
		if(validId) {
			retorno = isValidNumber(request.getParameter("idFalta"));
			
			String dataCadastro = request.getParameter("dataCadastro");
			if(dataCadastro == null)//Se for nulo
				return false;
			else if(dataCadastro.equals(""))//Ou se for vazio
				return false;
			else {//Ou senão for número
				try {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
					Date dataCado = formatter.parse(dataCadastro);
				}catch(Exception e) {
					return false;
				}
			}			
		}
		    	
    	return retorno;    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String sIdAluno = request.getParameter("idAluno");		
		long idAluno = 0;
		ArrayList<Faltas> listaFaltas = new ArrayList<Faltas>();
		FaltasDao faltasDao = new FaltasDao();
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
				listaFaltas = new ArrayList<Faltas>(faltasDao.getFaltasAluno(idAluno));
			}
			else
			{
				listaFaltas = new ArrayList<Faltas>(faltasDao.getAllFaltas());					
			}
			
			if(listaFaltas.size() <= 0) {
				JsonObject jsonErro = new JsonObject();
				jsonErro.addProperty("Status", "Erro");
				jsonErro.addProperty("Mensagem", "Nao ha faltas cadastradas para esse aluno");
				jsonErro.addProperty("Time", String.valueOf(new Date()));			
				response.setStatus(404);
				response.getWriter().print(jsonErro.toString()); 
			}
			else {					
				Type listType  = new TypeToken<List<Faltas>>(){}.getType();//Pega o tipo da lista para enviar no gson
				json = gson.toJson(listaFaltas, listType);
				response.setContentType("application/json"); //MIME Type
				response.setCharacterEncoding("utf-8");
				response.getWriter().print(json);
			}			
			
		} catch (Exception e) {
			JsonObject jsonErro = new JsonObject();
			jsonErro.addProperty("Status", "Erro");
			jsonErro.addProperty("Mensagem", e.getMessage());
			jsonErro.addProperty("Time", String.valueOf(new Date()));			
			response.setStatus(404);
			response.getWriter().print(jsonErro.toString()); 
		}
		
		response.flushBuffer();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(isValidParameters(request, false)) {
			Faltas falta = new Faltas();
			
			falta.setIdAluno(request.getParameter("idAluno"));	        		
			
			System.out.print(falta);
			
			FaltasDao fd = new FaltasDao();
			
			String now = String.valueOf(new Date());
			
			try {
			   fd.addFalta(falta);
			} catch (Exception e) {
				System.err.println(now +  " - Ops!! - " + e.getMessage());
				System.err.println(now +  " - Ops!! - " + falta);
				e.printStackTrace();
				response.setStatus(404);
			}
			
			response.setContentType("application/json"); //MIME Type
			response.setCharacterEncoding("utf-8");
	
			Gson g = new Gson();		
			String jsonReturn = g.toJson(falta);
	
			// put some value pairs into the JSON object.		
			 response.getWriter().print(jsonReturn);	         
		}
		else {
			response.setStatus(404);
			response.getWriter().print(JSON_ERRO_PARAMETERS);	         
		}
		response.flushBuffer();
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(isValidParameters(request, true)) {
			
			Faltas falta = new Faltas();
			
			falta.setIdFalta(request.getParameter("idFalta"));
			falta.setIdAluno(request.getParameter("idAluno"));
			falta.setDataCadastro(request.getParameter("dataCadastro"));
					
			String now = String.valueOf(new Date());
			
			FaltasDao fd = new FaltasDao();			
			try {
			   fd.updateFaltas(falta);
			} catch (Exception e) {
				System.err.println(now +  " - Ops!! - " + e.getMessage());
				System.err.println(now +  " - Ops!! - " + falta);
				e.printStackTrace();
			}
			
			response.setContentType("application/json"); //MIME Type
			response.setCharacterEncoding("utf-8");
	
			Gson g = new Gson();		
			String jsonReturn = g.toJson(falta);
	
			// put some value pairs into the JSON object.		
			 response.getWriter().print(jsonReturn);
		}
		else {
			response.setStatus(404);
			response.getWriter().print(JSON_ERRO_PARAMETERS);	         
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(isValidNumber(request.getParameter("idFalta"))) {
			
			response.setContentType("application/json"); //MIME Type
			response.setCharacterEncoding("utf-8");
			
			try {
				
				String sIdFalta = request.getParameter("idFalta");
				long idFalta = Long.parseLong(sIdFalta);
				
				FaltasDao faltasDao = new FaltasDao();
				Faltas falta = faltasDao.getFaltaId(idFalta);
				boolean erro = true;
				
				if(falta != null) {
					if(falta.getIdAluno() > 0) {
						FaltasDao ad = new FaltasDao();			
						ad.deleteFalta(idFalta);
						erro = false;
					}						
				}
				
				String status = "OK";
				String message = "Falta deletada!";
				String now = String.valueOf(new Date());	
				
				if(erro) {
					status = "ERRO";
					message = "Erro ao excluir, falta nao encontrado!";				
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
			jsonErro.addProperty("Mensagem", "O ID da falta eh invalido");
			jsonErro.addProperty("Time", String.valueOf(new Date()));			
			response.setStatus(404);
			response.getWriter().print(jsonErro.toString());            
		}
		response.flushBuffer();
	}
}
