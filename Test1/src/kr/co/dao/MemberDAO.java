package kr.co.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import kr.co.domain.MemberDTO;

public class MemberDAO {

	
	DataSource datafactory;
	

	public MemberDAO() {
		try {
			Context ctx = new InitialContext();
			datafactory = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle11g");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	
	private void closeall(ResultSet rs, PreparedStatement pstmt, Connection conn) {
		try {
			if (rs != null) {
				rs.close();
			}
			
			if (pstmt != null) {
				
				pstmt.close();
			}
			if (conn != null) {
				
				conn.close();
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}


	public void insert(MemberDTO dto) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = "insert into member(usernum,id,pw,name,nickname,tell,address) values(?,?,?,?,?,?,?)";
		
		try {
			conn = datafactory.getConnection();
			pstmt = conn.prepareStatement(sql);
			
			int num = getNum(conn);
			
			pstmt.setInt(1, num);
			pstmt.setString(2, dto.getId());
			pstmt.setString(3, dto.getPw());
			pstmt.setString(4, dto.getName());
			pstmt.setString(5, dto.getNickname());
			pstmt.setInt(6, dto.getTell());
			pstmt.setString(7, dto.getAddress());
			
			
			pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			closeall(null, pstmt, conn);
		}
		
		
	}


	private int getNum(Connection conn) {
		int num = 0;
	      PreparedStatement pstmt = null;
	      String sql = "SELECT NVL2(MAX(usernum), MAX(usernum)+1, 1) FROM member";
	      ResultSet rs = null;
	      
	      try {
	         pstmt = conn.prepareStatement(sql);
	         rs = pstmt.executeQuery();
	         
	         if(rs.next()) {
	            num = rs.getInt(1);
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      } finally {
	         closeall(rs, pstmt, null);
	      }
	      
	      return num;
	   }
	}
	
	
	

