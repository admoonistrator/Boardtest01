package test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	
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
	public int getAllCount() {
		getCon();
		
		//게시글 전체수
		int count=0;
		
		try {
			String sql="select count(*) from board";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				count=rs.getInt(1);
			}
			con.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public Vector<BoardBean> allBoard(int start, int end){
		//글쓰기: 여러분의 정보를 입력함 -> BoardBean에 저장됨(useBean) 
		//-> 테이블에 useBean에 저장했던 값들을 꺼내와서 테이블에 삽입
		Vector<BoardBean> v=new Vector<>();
		
		getCon();
		
		try {
			
			String sql="select * from (select A.*, Rownum Rnum from (select * from board order by ref desc, re_level asc)A) where Rnum>=? and Rnum<=?";
			pstmt=con.prepareStatement(sql);
			
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				//27명의 글이 db에 저장되어있다. 저장되어 있는 값을 자바빈에 저장할거임.-> 벡터에저장
				BoardBean bean=new BoardBean();
				
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString()); //객체를 문자열로
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));
				
				v.add(bean);
			}
			con.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return v;
	}
public BoardBean oneBoard(int num) {
		
		BoardBean bean=new BoardBean();
		
		getCon();
		
		try {
			String readsql="update board set readcount=readcount+1 where num=?";
			pstmt=con.prepareStatement(readsql);
			pstmt.setInt(1, num);
			
			pstmt.executeUpdate();
			
			String sql="select *from board where num=?";
			//DB에 값을 가져와 브라우저에 뿌려야함
			
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, num);
			
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				bean.setNum(rs.getInt(1));
				bean.setWriter(rs.getString(2));
				bean.setEmail(rs.getString(3));
				bean.setSubject(rs.getString(4));
				bean.setPassword(rs.getString(5));
				bean.setReg_date(rs.getDate(6).toString());
				bean.setRef(rs.getInt(7));
				bean.setRe_step(rs.getInt(8));
				bean.setRe_level(rs.getInt(9));
				bean.setReadcount(rs.getInt(10));
				bean.setContent(rs.getString(11));	
				
			}
			con.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return bean;
	
	}
public void reWriteBoard(BoardBean bean) {
	
	int ref=bean.getRef();//1
	int re_step=bean.getRe_step();//2
	int re_level=bean.getRe_level();//2
	
	getCon();
	
	try {
		String levelsql="update board set re_level=re_level+1 where ref=? and re_level>?";
		
		pstmt=con.prepareStatement(levelsql);
		
		pstmt.setInt(1,ref);
		pstmt.setInt(2, re_level);
		
		pstmt.executeUpdate();
		
		String sql="insert into board values(bo_seq.NEXTVAL,?,?,?,?,sysdate,?,?,?,0,?)";
		//댓글을 디비에 삽입시킨다.
		pstmt=con.prepareStatement(sql);
		
		pstmt.setString(1, bean.getWriter());
		pstmt.setString(2, bean.getEmail());
		pstmt.setString(3, bean.getSubject());
		pstmt.setString(4, bean.getPassword());
		pstmt.setInt(5, ref);
		pstmt.setInt(6, re_step+1);
		pstmt.setInt(7, re_level+1);
		pstmt.setString(8, bean.getContent());
		
		pstmt.executeUpdate();
		con.close();

	}catch(Exception e) {
		e.printStackTrace();
	}	
}

}
