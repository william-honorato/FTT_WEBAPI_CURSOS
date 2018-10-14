package br.edu.cefsa.ftt.ec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.edu.cefsa.ftt.ec.model.Alunos;
import br.edu.cefsa.ftt.ec.model.Faltas;
import br.edu.cefsa.ftt.ec.model.Notas;
import br.edu.cefsa.ftt.util.DbUtil;

public class AlunosDao {

	private Connection connection;
	
	public AlunosDao() {
        connection = DbUtil.getConnection();
    }
	
	private long getLastId() {
		
		long idAluno = 0;

		try {

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT LAST_INSERT_ID() as ID;");

            if (rs.next()) {            	
            	idAluno = rs.getLong("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
		
		return idAluno;		
	}
	
    public Alunos addAlunos(Alunos aluno) {
        
    	try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO Alunos (Nome, Email, Curso, DataCadastro) VALUES (?, ?, ?, ?)");
            
            // Parameters start with 1
            preparedStatement.setString(1, aluno.getNome());
            preparedStatement.setString(2, aluno.getEmail());            
            preparedStatement.setString(3, aluno.getCurso());
            preparedStatement.setDate(4, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            
            preparedStatement.executeUpdate();
            
            aluno.setIdAluno(getLastId());//Pega o id do aluno inserido

        } catch (SQLException e) {
            e.printStackTrace();
            
            throw new ArithmeticException("AlunosDao: addAlunos: " + e.getMessage()); 
        }
    	
    	return aluno;
    } //addAlunos

    public void deleteAlunos(long idAluno) {
        try {
            
        	FaltasDao fd = new FaltasDao();
        	fd.deleteAllFaltas(idAluno);
        	NotasDao nd = new NotasDao();
        	nd.deleteAllNotas(idAluno);
        	
        	PreparedStatement preparedStatement = connection
                    .prepareStatement("DELETE FROM Alunos WHERE idAluno=?");
            
            // Parameters start with 1
            preparedStatement.setLong(1, idAluno);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ArithmeticException("AlunosDao: deleteAlunos: " + e.getMessage()); 
        }
    } //deleteAlunos

    public void updateAlunos(Alunos aluno) {
        try {        	
            PreparedStatement preparedStatement = connection
                    .prepareStatement("UPDATE Alunos SET Nome=?, " 
                    		                          + "Email=?, " 
                    		                          + "Curso=?, " 
                    		                          + "DataCadastro=? " 
                                               + "WHERE IdAluno=?");
            
            // Parameters start with 1
            preparedStatement.setString(1, aluno.getNome());
            preparedStatement.setString(2, aluno.getEmail());
            preparedStatement.setString(3, aluno.getCurso());
            preparedStatement.setDate(4, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            
            preparedStatement.setLong(5, aluno.getIdAluno());
            
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    } //updateAlunos

    public List<Alunos> getAllAlunos() {
        
    	List<Alunos> alunos = new ArrayList<Alunos>();
        
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Alunos");
            while (rs.next()) {
                
            	Alunos aluno = new Alunos();
            	
            	aluno.setIdAluno(rs.getLong("IdAluno"));
            	aluno.setDataCadastro(rs.getDate("DataCadastro"));
            	aluno.setNome(rs.getString("Nome"));
            	aluno.setCurso(rs.getString("Curso"));
            	aluno.setEmail(rs.getString("Email"));
            	
            	ArrayList<Faltas> listaFaltas = new ArrayList<Faltas>(new FaltasDao().getFaltasAluno(aluno.getIdAluno()));
            	aluno.setListaFaltas(listaFaltas);

            	ArrayList<Notas> listaNotas = new ArrayList<Notas>(new NotasDao().getNotasAluno(aluno.getIdAluno()));
            	aluno.setListaNotas(listaNotas);
            	
            	alunos.add(aluno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alunos;
    } //getAllAlunos

    public Alunos getAlunoId(long id) {

    	Alunos aluno = new Alunos();
        
    	try {
            PreparedStatement preparedStatement = connection.
                    prepareStatement("SELECT * from Alunos WHERE IdAluno=?");
            
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {            	
            	aluno.setIdAluno(rs.getLong("IdAluno"));
            	aluno.setDataCadastro(rs.getDate("DataCadastro"));
            	aluno.setNome(rs.getString("Nome"));
            	aluno.setCurso(rs.getString("Curso"));
            	aluno.setEmail(rs.getString("Email"));
            	
            	ArrayList<Faltas> listaFaltas = new ArrayList<Faltas>(new FaltasDao().getFaltasAluno(aluno.getIdAluno()));
            	aluno.setListaFaltas(listaFaltas);

            	ArrayList<Notas> listaNotas = new ArrayList<Notas>(new NotasDao().getNotasAluno(aluno.getIdAluno()));
            	aluno.setListaNotas(listaNotas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aluno;
    } //getAlunoId    
}
