package kr.info_board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.info_board.vo.InfoBoardVo;
import kr.util.DBUtil;
import kr.util.StringUtil;

public class InfoBoardDao {
	private static InfoBoardDao dao = new InfoBoardDao();
	public static InfoBoardDao getDao() {
		return dao;
	}
	private InfoBoardDao() {}
	
	//글 등록
	public void insertInfo(InfoBoardVo info) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;
		try {
			conn  = DBUtil.getConnection();
			sql = "INSERT INTO info_board (info_board_num, mem_num, info_board_title, info_board_content, "
					+ "info_board_photo) VALUES (info_board_seq.nextval,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(++cnt, info.getMem_num());
			pstmt.setString(++cnt, info.getInfo_board_title());
			pstmt.setString(++cnt, info.getInfo_board_content());
			pstmt.setString(++cnt, info.getInfo_board_photo());
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	//총 글의 수, 검색
	public int getInfoCount(String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		String sub_sql = "";
		int count = 0;
		int cnt = 0;
		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "WHERE title LIKE '&' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "WHERE id LIKE '&' || ? || '%'";
				else if(keyfield.equals("3")) sub_sql += "WHERE content LIKE '&' || ? || '%'";
			}
		sql = "SELECT COUNT(*) FROM info_board JOIN member USING (mem_num)" + sub_sql;
		pstmt = conn.prepareStatement(sql);
		if(keyword != null && !"".equals(keyword)) {
			pstmt.setString(++cnt, keyword);
		}
		rs = pstmt.executeQuery();
		if(rs.next()) {
			count = rs.getInt(1);
		}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}
	//글 목록, 검색 글 목록
	public List<InfoBoardVo> getListInfo(int start, int end, String keyfield, String keyword) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<InfoBoardVo> list = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;
		try {
			conn = DBUtil.getConnection();
			if(keyword != null && !"".equals(keyword)) {
				if(keyfield.equals("1")) sub_sql += "WHERE title LIKE '&' || ? || '%'";
				else if(keyfield.equals("2")) sub_sql += "WHERE id LIKE '&' || ? || '%'";
				else if(keyfield.equals("3")) sub_sql += "WHERE content LIKE '&' || ? || '%'";
			}
			sql = "SELECT * FROM (SELECT a.*, rownum rnum FROM "
					+ "(SELECT * FROM info_board JOIN member USING(mem_num) " + sub_sql
					+ " ORDER BY info_board_num DESC)a) WHERE rnum >=? AND rnum <= ?";
			pstmt = conn.prepareStatement(sql);
			if(keyword != null && !"".equals(keyword)) {
				pstmt.setString(++cnt, keyword);
			}
			pstmt.setInt(++cnt, start);
			pstmt.setInt(++cnt, end);
	        
			rs = pstmt.executeQuery();
			list = new ArrayList<InfoBoardVo>();
			while(rs.next()) {
				InfoBoardVo info = new InfoBoardVo();
				info.setInfo_board_num(rs.getInt("info_board_num"));
				info.setInfo_board_title(StringUtil.useBrNoHTML(rs.getString("info_board_title")));
				info.setInfo_board_hit(rs.getInt("info_board_hit"));
				info.setInfo_board_reg(rs.getString("info_board_reg"));
				info.setMem_id(rs.getString("mem_id"));
				info.setInfo_board_report(rs.getInt("info_board_report"));
				list.add(info);
				
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}
	//글 상세
	public InfoBoardVo getInfo(int info_board_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		InfoBoardVo info = null;
		String sql = null;
		int cnt = 0;
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT info_board.*, member.mem_id, member_detail.mem_photo FROM info_board "
					+ "JOIN member ON info_board.mem_num = member.mem_num "
					+ "LEFT OUTER JOIN member_detail ON member.mem_num = member_detail.mem_num "
					+ "WHERE info_board_num=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(++cnt, info_board_num);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				info = new InfoBoardVo();
				info.setInfo_board_num(rs.getInt("info_board_num"));
				info.setInfo_board_title(rs.getString("info_board_title"));
				info.setInfo_board_content(rs.getString("info_board_content"));
				info.setInfo_board_hit(rs.getInt("info_board_hit"));
				info.setInfo_board_modify(rs.getString("info_board_modify"));
				info.setInfo_board_photo(rs.getString("info_board_photo"));
				info.setMem_num(rs.getInt("mem_num"));
				info.setMem_id(rs.getString("mem_id"));
				info.setMem_photo(rs.getString("mem_photo"));
				info.setInfo_board_reg(rs.getString("info_board_reg"));
			}
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return info;
	}
	//조회수 증가
	public void updateReadCount(int info_board_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;
		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE info_board SET info_board_hit=info_board_hit+1 WHERE info_board_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(++cnt, info_board_num);
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	//글 수정
	public void updateInfo(InfoBoardVo board) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		String sub_sql = "";
		int cnt = 0;
		
		try {
			conn = DBUtil.getConnection();
			if(board.getInfo_board_photo() != null && !"".equals(board.getInfo_board_photo())) {
				sub_sql += ",info_board_photo=?";
			}
			sql = "UPDATE info_board SET info_board_title=?, info_board_content=?, info_board_modify=SYSDATE " + sub_sql
					+ " WHERE info_board_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(++cnt, board.getInfo_board_title());
			pstmt.setString(++cnt, board.getInfo_board_content());
			if(board.getInfo_board_photo() != null && !"".equals(board.getInfo_board_photo())) {
				pstmt.setString(++cnt, board.getInfo_board_photo());
			}
			pstmt.setInt(++cnt, board.getInfo_board_num());
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	//글 삭제
	/*
	public void deleteBoard(int info_board_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 =null;
		String sql = null;
		int cnt = 0;
		try {
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			
			sql = "DELETE FROM info_board_comment WHERE info_board_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(++cnt, info_board_num);
			pstmt.executeUpdate();
			
			cnt = 0;
			
			sql = "DELETE FROM report_info_board WHERE info_board_num=?";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setInt(++cnt, info_board_num);
			pstmt2.executeUpdate();
			
			cnt = 0;
			
			sql="DELETE FROM info_board WHERE info_board_num=?";
			pstmt3 = conn.prepareStatement(sql);
			pstmt3.setInt(++cnt, info_board_num);
			pstmt3.executeUpdate();
			
			conn.commit();
		}catch(Exception e) {
			conn.rollback();
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt3, null);
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	*/
	//파일 삭제
	public void deleteFile(int info_board_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;
		try {
			conn = DBUtil.getConnection();
			sql = "UPDATE info_board SET info_board_photo ='' WHERE info_board_num=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(++cnt, info_board_num);
			pstmt.executeUpdate();
		}catch(Exception e) {
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	}
	//신고 등록
	
	//회원번호와 게시물 번호를 이용한 신고 정보
	
	//신고수
}