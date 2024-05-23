package kr.member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;


import kr.member.vo.MemberVo;
import kr.util.DBUtil;

public class MemberDao {
	/*
	 * 싱글턴 패턴은 생성자를 private으로 지정해서 외부에서 호출할 수 없도록 처리하고
	 * static 메서드를 호출해서 객체가 한 번만 생성되고 생성된 객체를
	 * 공유할 수 있도록 처리하는 방식을 의미함.
	 */

	//싱글톤
	private static MemberDao dao = new MemberDao();
	public static MemberDao getDao() {
		return dao;
	}
	private MemberDao() {};

	//사용자의 기능
	//회원 가입
	public void insertMember(MemberVo member)throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
		String sql =null;
		ResultSet rs = null;
		int num = 0; //시퀀스 번호 저장
		int cnt = 0;
		try {
			//커넥션풀로부터 커넥션 할당
			conn = DBUtil.getConnection();
			//오토 커밋 해제  
			conn.setAutoCommit(false);
			
			//SQL문 작성
			//회원 번호(mem_num) 생성
			//아래에 시퀀스를 넣으면 pstmt2와 pstmt3의 mem_num값이 다르게 나와 에러가난다.
			sql="SELECT member_seq.nextval FROM dual";
			//PreparedStatment 객체 생성
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				num = rs.getInt(1); //zmember_seq.nextval의 컬럼인덱스 (1개 밖에 안들어가있음)
			}
			//member 테이블에 회원정보넣기
			sql ="INSERT INTO member (mem_num,mem_id) VALUES(?,?)"; //auth를 넣지 않는 이유는 기본적으로 2가 들어가기 때문에
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setInt(1, num); //시퀀스 번호를 넣음으로써 충돌이 나지않게
			pstmt2.setString(2,member.getMem_id());
			pstmt2.executeUpdate();

			//member_detail 테이블에 회원정보 넣기
			//mem_grade와 mem_total,mem_reg은 기본값이 들어가기 때문에 명시 안해도됨. mem_modify는 마이페이지 수정시 사용
			sql = "INSERT INTO member_detail (mem_num,mem_name,mem_passwd,mem_birth,"
					+ "mem_phone,mem_email,mem_zipcode,mem_address1,mem_address2) VALUES (?,?,?,?,?,?,?,?,?)";
			
			pstmt3 = conn.prepareStatement(sql);
			//?에 데이터 바인딩
			pstmt3.setInt(++cnt,num); //시퀀스 번호
			pstmt3.setString(++cnt, member.getMem_name());
			pstmt3.setString(++cnt, member.getMem_passwd());
			pstmt3.setString(++cnt, member.getMem_birth());
			pstmt3.setString(++cnt, member.getMem_phone());
			pstmt3.setString(++cnt, member.getMem_email());
			pstmt3.setString(++cnt, member.getMem_zipcode());
			pstmt3.setString(++cnt, member.getMem_address1());
			pstmt3.setString(++cnt, member.getMem_address2());
			pstmt3.executeUpdate();
			
			//SQL 실행시 모두 성공하면 commit
			conn.commit();
		}catch(Exception e) {
			//SQL문이 하나라도 실패하면 rollback
			conn.rollback();
			throw new Exception(e);
		}finally {
			DBUtil.executeClose(null, pstmt3, null);
			DBUtil.executeClose(null, pstmt2, null);
			DBUtil.executeClose(rs, pstmt3, conn);
		}
	}

	   //ID 중복 체크 및 로그인 처리
	   public MemberVo checkMember(String mem_id)throws Exception{
	      Connection conn = null;
	      PreparedStatement pstmt = null;
	      ResultSet rs = null;
	      MemberVo member = null;
	      String sql = null;
	      
	      try {
	         //커넥션풀로부터 커넥션 할당
	         conn = DBUtil.getConnection();
	         //SQL문 작성
	         //zmember와 zmember_detail 테이블을 조인할 때
	         //누락된 데이터가 보여야 id 중복 체크 가능
	         sql = "SELECT * FROM member LEFT OUTER JOIN "
	            + "member_detail USING(mem_num) WHERE mem_id = ?";
	         //PreparedStatement 객체 생성
	         pstmt = conn.prepareStatement(sql);
	         //?에 데이터 바인딩
	         pstmt.setString(1, mem_id);
	         //SQL문 실행
	         rs = pstmt.executeQuery();
	         if(rs.next()) {
	            member = new MemberVo();
	            member.setMem_num(rs.getInt("mem_num"));
	            member.setMem_id(rs.getString("mem_id"));
	            member.setMem_auth(rs.getInt("mem_auth"));
	            member.setMem_passwd(rs.getString("mem_passwd"));
	            member.setMem_photo(rs.getString("mem_photo"));
	            member.setMem_email(rs.getString("mem_email"));//회원 탈퇴시 필요
	         }
	      }catch(Exception e) {
	         throw new Exception(e);
	      }finally {
	         DBUtil.executeClose(rs, pstmt, conn);
	      }
	      return member;
	   }

	//회원상세 정보
	public MemberVo getMember(int mem_num)throws Exception{
		MemberVo member = null;
		return member;
	}

	//회원정보 수정
	public void updateMember(MemberVo member)throws Exception{

	}

	//비밀번호 수정
	public void updatePassword(String mem_passwd, int mem_num) throws Exception{

	}

	//프로필 사진 수정
	public void updateMyPhoto(String mem_photo,int mem_num) throws Exception{

	}

	//회원 탈퇴(회원정보 삭제)
	public void deleteMember(int mem_num) throws Exception{

	}


	//관리자의 기능
	//전체 내용 개수, 검색 내용 개수
	public int getMemberCountByAdmin(String keyfield, String keyword) throws Exception{
		int count = 0;

		return count;
	}

	//목록, 검색 목록
	public List<MemberVo> getListMemberbyAdmin(int start, int end, String keyfield, String keyword) throws Exception{
		List<MemberVo> list = null;

		return list;
	}

	//회원등급 수정 
	public void updateMemberByAdmin(int mem_auth, int mem_num) throws Exception{

	}





}