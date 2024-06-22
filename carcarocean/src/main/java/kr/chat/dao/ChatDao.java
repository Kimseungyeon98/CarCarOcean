package kr.chat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import kr.chat.vo.ChatVo;
import kr.item.vo.ItemVo;
import kr.member.vo.MemberVo;
import kr.util.DBUtil;

public class ChatDao {
	private static ChatDao dao = new ChatDao();
	private ChatDao() {};
	public static ChatDao getDao() {
		return dao;
	};
	
	public void insertChat(ChatVo chat) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql = null;
		int cnt = 0;
		try {
			conn = DBUtil.getConnection();
			sql = "INSERT INTO CHAT (chat_num, item_num, chat_receiver, chat_giver, chat_message) values (chat_seq.nextval, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(++cnt, chat.getItem().getItem_num());
			pstmt.setInt(++cnt, chat.getReceiver().getMem_num());
			pstmt.setInt(++cnt, chat.getGiver().getMem_num());
			pstmt.setString(++cnt, chat.getChat_message());
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt, conn);
		}
	};
	//특정 대상과의 채팅
	public List<ChatVo> getListChat(int item_num, int chat_receiver, int chat_giver) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		String sql = null;
		List<ChatVo> list = null;
		int cnt = 0;
		try {
			conn = DBUtil.getConnection();
			sql = "select * from chat join member_detail on chat.chat_receiver=member_detail.mem_num join member_detail on chat.chat_giver=member_detail.mem_num where item_num=? and ((chat_receiver=? and chat_giver=?)or(chat_receiver=? and chat_giver=?)) order by chat_num";
			pstmt1 = conn.prepareStatement(sql);
			pstmt1.setInt(++cnt, item_num);
			pstmt1.setInt(++cnt, chat_receiver);
			pstmt1.setInt(++cnt, chat_giver);
			pstmt1.setInt(++cnt, chat_giver);
			pstmt1.setInt(++cnt, chat_receiver);
			rs=pstmt1.executeQuery();
			list = new ArrayList<>();
			while(rs.next()) {
				ChatVo chat = new ChatVo();
				chat.setChat_num(rs.getInt("chat_num"));
				
				chat.setItem(new ItemVo());
				chat.getItem().setItem_num(rs.getInt("item_num"));
				
				chat.setReceiver(new MemberVo());
				chat.getReceiver().setMem_num(rs.getInt("chat_receiver"));
				
				chat.setGiver(new MemberVo());
				chat.getGiver().setMem_num(rs.getInt("chat_giver"));
				
				chat.setChat_message(rs.getString("chat_message"));
				chat.setChat_reg(rs.getString("chat_reg"));
				
				chat.setChat_check(rs.getInt("chat_check"));
				
				list.add(chat);
			}
			
			sql = "update chat set chat_check=1 where item_num=? and chat_receiver=? and chat_giver=?";
			pstmt2 = conn.prepareStatement(sql);
			pstmt2.setInt(1, item_num);
			pstmt2.setInt(2, chat_receiver);
			pstmt2.setInt(3, chat_giver);
			pstmt2.executeUpdate();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(null, pstmt1, null);
			DBUtil.executeClose(null, pstmt2, null);
		}
		return list;
	}
	//특정 대상이 받은 모든 채팅
	public List<ChatVo> getAllListChat(int item_num, int chat_receiver) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		List<ChatVo> list = null;
		int cnt = 0;
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT chat.chat_message,chat.chat_reg, chat.chat_check,"
					+ "        receiver.mem_num as receiver_num, receiver.mem_id as receiver_id, receiver.mem_photo as receiver_photo,"
					+ "        giver.mem_num as giver_num, giver.mem_id as giver_id, giver.mem_photo as giver_photo "
					+ "FROM ("
					+ "    SELECT"
					+ "        chat_num,"
					+ "        item_num,"
					+ "        chat_receiver,"
					+ "        chat_giver,"
					+ "        chat_message,"
					+ "        chat_reg,"
					+ "        chat_check,"
					+ "        ROW_NUMBER() OVER (PARTITION BY chat_receiver, chat_giver ORDER BY chat_reg DESC) as rn"
					+ "    FROM chat"
					+ ") chat"
					+ "    join member receiver on chat.chat_receiver = receiver.mem_num"
					+ "    join member_detail receiver on chat.chat_receiver = receiver.mem_num"
					+ "    join member giver on chat.chat_giver = giver.mem_num"
					+ "    join member_detail giver on chat.chat_giver = giver.mem_num"
					+ " WHERE rn = 1 and item_num=? and chat_receiver=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(++cnt, item_num);
			pstmt.setInt(++cnt, chat_receiver);
			rs=pstmt.executeQuery();
			list = new ArrayList<>();
			while(rs.next()) {
				//chat -> chat_message, chat_reg, chat_check
				// receiver -> receiver_id, receiver_photo
				// giver -> giver_id, giver_photo
				ChatVo chat = new ChatVo();
				chat.setChat_message(rs.getString("chat_message"));
				chat.setChat_reg(rs.getString("chat_reg"));
				chat.setChat_check(rs.getInt("chat_check"));
				
				chat.setReceiver(new MemberVo());
				chat.getReceiver().setMem_num(rs.getInt("receiver_num"));
				chat.getReceiver().setMem_id(rs.getString("receiver_id"));
				chat.getReceiver().setMem_photo(rs.getString("receiver_photo"));
				
				chat.setGiver(new MemberVo());
				chat.getGiver().setMem_num(rs.getInt("giver_num"));
				chat.getGiver().setMem_id(rs.getString("giver_id"));
				chat.getGiver().setMem_photo(rs.getString("giver_photo"));
				
				list.add(chat);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}
	
	//해당 아이템의 채팅 개수
	public int itemChatCount(int item_num) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int cnt = 0;
		int count = 0;
		try {
			conn = DBUtil.getConnection();
			sql = "SELECT COUNT(distinct chat_giver) FROM CHAT WHERE ITEM_NUM=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(++cnt, item_num);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return count;
	}
	//특정 대상과 대화를 나눈 적이 있는 사람의 리스트
	public List<Integer> getSpecificList(int item_num, int chat_receiver) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = null;
		int cnt = 0;
		List<Integer> list = null;
		try {
			conn = DBUtil.getConnection();
			sql = "select distinct(chat_giver) from chat where item_num=? and chat_receiver=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(++cnt, item_num);
			pstmt.setInt(++cnt, chat_receiver);
			rs = pstmt.executeQuery();
			list = new ArrayList<>();
			while(rs.next()) {
				list.add(rs.getInt("chat_giver"));
			}
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			DBUtil.executeClose(rs, pstmt, conn);
		}
		return list;
	}
}
