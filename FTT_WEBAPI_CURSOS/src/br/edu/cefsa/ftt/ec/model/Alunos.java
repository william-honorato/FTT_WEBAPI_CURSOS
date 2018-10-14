package br.edu.cefsa.ftt.ec.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Alunos {
		
	long   idAluno;
	String nome,
    email,
    curso;	
	Date   dataCadastro;
	
	//Retorno GET
	ArrayList<Notas> listaNotas;
	ArrayList<Faltas> listaFaltas;
	
	public Alunos() {}
	
	public Alunos(String nome, String email, String curso) {
		super();		
		this.setNome(nome);
		this.setEmail(email);
		this.setCurso(curso);		
	}
	
	public Alunos(String idAluno,String nome, String email, String curso) {
		super();
		this.setIdAluno(idAluno);
		this.setNome(nome);
		this.setEmail(email);
		this.setCurso(curso);		
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
			System.err.println("Ops! Problema com o IdAluno: " + dataCadastro);
			e.printStackTrace();
		}
	}
	
	//-------------------------------------------------------------------------------------------------------------
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	//-------------------------------------------------------------------------------------------------------------
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	//-------------------------------------------------------------------------------------------------------------
	
	public String getCurso() {
		return curso;
	}
	public void setCurso(String curso) {
		this.curso = curso;
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
			System.err.println("Ops! Problema com a data de cadastro aluno: " + dataCadastro);
			e.printStackTrace();
		}		
	}
	
	//-------------------------------------------------------------------------------------------------------------
	
	public ArrayList<Notas> getListaNotas() {
		return listaNotas;
	}
	public void setListaNotas(ArrayList<Notas> listaNotas) {
		this.listaNotas = listaNotas;
	}
	
	//-------------------------------------------------------------------------------------------------------------
		
	public ArrayList<Faltas> getListaFaltas() {
		return listaFaltas;
	}
	public void setListaFaltas(ArrayList<Faltas> listaFaltas) {
		this.listaFaltas = listaFaltas;
	}
}
