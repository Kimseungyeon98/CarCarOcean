package kr.chat.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.chat.dao.ChatDao;
import kr.chat.vo.ChatVo;
import kr.controller.Action;

public class ChatListAction implements Action{

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		Integer user_num = (Integer)session.getAttribute("user_num");
		if(user_num==null) {
			return "/WEB-INF/views/common/logout.jsp";
		}
		
		int item_num = Integer.parseInt(request.getParameter("item_num"));

		ChatDao chatDao = ChatDao.getDao();
		
		List<ChatVo> chatList = chatDao.getAllListChat(item_num, user_num);
		
		request.setAttribute("item_num", item_num);
		request.setAttribute("chatList", chatList);
		
		return "/WEB-INF/views/chat/chatList.jsp";
	}

}
