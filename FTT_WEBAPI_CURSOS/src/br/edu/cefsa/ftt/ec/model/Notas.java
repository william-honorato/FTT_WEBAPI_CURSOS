package br.edu.cefsa.ftt.ec.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Notas {

	long   	idNota,
			idAluno;
	float	nota;	
	Date    dataCadastro;
	
	//-------------------------------------------------------------------------------------------------------------
	
	public long getIdNota() {
		return idNota;
	}
	public void setIdNota(long idNota) {
		this.idNota = idNota;
	}
	public void setIdNota(String idNota) {
		try {
			this.idNota = Long.parseLong(idNota);
		} catch (Exception e) {
			System.err.println("Ops! Problema com o idNota: " + dataCadastro);
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
	
	public float getNota() {
		return nota;
	}
	public void setNota(float nota) {
		this.nota = nota;
	}
	public void setNota(String nota) {
		try {
			this.nota = Float.parseFloat(nota);
		} catch (Exception e) {
			System.err.println("Ops! Problema com o nota: " + dataCadastro);
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
			System.err.println("Ops! Problema com a data de cadastro da nota: " + dataCadastro);
			e.printStackTrace();
		}	
	}
}
