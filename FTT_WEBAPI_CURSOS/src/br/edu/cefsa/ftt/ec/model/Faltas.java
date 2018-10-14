package br.edu.cefsa.ftt.ec.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Faltas {
	
	long   	idFalta,
			idAluno,
			numeroFaltas;	
	Date    dataCadastro;
	
	//-------------------------------------------------------------------------------------------------------------
	
	public long getIdFalta() {
		return idFalta;
	}
	public void setIdFalta(long idFalta) {
		this.idFalta = idFalta;
	}
	public void setIdFalta(String idFalta) {
		try {
			this.idFalta = Long.parseLong(idFalta);
		} catch (Exception e) {
			System.err.println("Ops! Problema com o idFalta: " + dataCadastro);
			e.printStackTrace();
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------
	
	public long getIdAluno() {
		return idAluno;
	}
	public void setIdAluno(long idAluno) {
		this.idAluno = idAluno;
	}
	public void setIdAluno(String idAluno) {
		try {
			this.idAluno = Long.parseLong(idAluno);
		} catch (Exception e) {
			System.err.println("Ops! Problema com o idAluno: " + dataCadastro);
			e.printStackTrace();
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------
	
	public long getNumeroFaltas() {
		return numeroFaltas;
	}	
	public void setNumeroFaltas(long numeroFaltas) {
		this.numeroFaltas = numeroFaltas;
	}
	public void setNumeroFaltas(String numeroFaltas) {
		try {
			this.numeroFaltas = Long.parseLong(numeroFaltas);
		} catch (Exception e) {
			System.err.println("Ops! Problema com o numeroFaltas: " + dataCadastro);
			e.printStackTrace();
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------
	
	public Date getDataCadastro() {
		return dataCadastro;
	}
	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	public void setDataCadastro(String dataCadastro) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 

		try { 
			this.dataCadastro = formatter.parse(dataCadastro);
		} catch (Exception e) {
			System.err.println("Ops! Problema com a data de cadastro da falta: " + dataCadastro);
			e.printStackTrace();
		}	
	}	
}
