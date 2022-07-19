package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public void getCon() {
		try{
			Context ctx=new InitialContext();
			Context env=(Context)ctx.lookup("java:comp/env");
			DataSource ds=(DataSource)env.lookup("jdbc/pool");
			
			con=ds.getConnection();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void insertBoard(BoardBean bean) {
		
getCon();
		
		int ref=0;  //글 그룹
		int re_step=1;  //새 글, 새 계층
		int re_level=1;

		try {
			//가장 큰 ref값을 읽어와라.
			String rsql="select max(ref) from board";
			
			pstmt=con.prepareStatement(rsql);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				ref=rs.getInt(1)+1;
			}
			
			String sql="insert into board values(bo_seq.NEXTVAL,?,?,?,?,sysdate,?,?,?,0,?)";
			
			pstmt=con.prepareStatement(sql);
			
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, re_step);
			pstmt.setInt(7, re_level);
			pstmt.setString(8, bean.getContent());
			
			pstmt.executeUpdate();
			con.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}


}
